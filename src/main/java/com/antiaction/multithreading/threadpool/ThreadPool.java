/*
 * Advanced ThreadPool manager.
 * Copyright (C) 2001, 2002, 2003  Nicholas Clarke
 *
 */

/*
 * History:
 *
 * 28-Jan-2002 : Dispatcher use changed to IWorker making it generic.
 *               Implements IBroker interface.
 * 06-Feb-2002 : Threshold field and set method added.
 * 14-Feb-2002 : Added ThreadGroups.
 * 15-Feb-2002 : Redefined threshold.
 *             : Added synchronized checkIn/Out.
 *             : Spawner Thread implemented.
 * 16-Feb-2002 : activeCount() changed to field.
 *             : attach()/detach() methods added.
 * 12-Aug-2002 : Added spawn counter instead of waiting for attaching threads.
 * 13-Aug-2002 : Removed spawn counter and added a list of worker objects and threads.
 * 14-Dec-2002 : Added exit boolean.
 * 15-Dec-2002 : stop().
 * 16-Dec-2002 : Idle management.
 * 17-Dec-2002 : Idle management.
 * 18-Dec-2002 : Idle management debugging.
 * 21-Jun-2003 : Moved to antiaction.com package.
 * 10-Jul-2003 : Added properties to init().
 * 19-Nov-2003 : Moved ThreadPool code to seperat class.
 * 23-Nov-2003 : IWorker interface renamed to IThreadWorker.
 * 23-Nov-2003 : Modified against new interfaces.
 *
 */

package com.antiaction.multithreading.threadpool;

import java.util.HashMap;

/**
 * Advanced ThreadPool manager.
 *
 * @version 1.00
 * @author Nicholas Clarke <mayhem[at]antiaction[dot]com>
 */
public class ThreadPool implements IThreadPool {

	/** Shutdown boolean. */
	private boolean exit = false;

	/** Has thread been started. */
	private boolean running = false;

	/** ThreadGroup. */
	private ThreadGroup threadGroup;

	/** Original thread, used for cloning. */
	private IThreadWorker threadWorker;

	/** Minimum workers. */
	private int min = 1;
	/** Threshold - minimum free workers before more are spawned but below max. */
	private int threshold = 1;
	/** Maximum workers. */
	private int max = 1;

	/** Initial sample second interval. */
	private int idleSampleSecsInit = 60 * 1;
	/** Sample second interval. */
	private int idleSampleSecs = 60;
	/** Lastmodified idlecount timeout. */
	private int lastModifiedTimeout = 1000 * 60 * 30;

	/** Min idle number compared/set each time a worker goes busy. */
	private int minIdle = 0;
	/** Worker stop count. */
	private int stop = 0;

	/** Workers started-stopped. */
	private int workers = 0;
	/** Worker Threads. */
	private int workerThreads = 0;
	/** Working Threads. */
	private int workingThreads = 0;
	/** Idle Threads. */
	private int idleThreads = 0;

	//private List workerList;

	/** Array of idle objects in a linked list sorted by index=idlecountindex. */
	private IdleObject[] idleObjects;
	/** First idle object in linked list. */
	private IdleObject rootIdleObj = null;

	public ThreadPool() {
		threadGroup = new ThreadGroup( "ThreadPool" );
		threadPool = this;
		//workerList = new ArrayList();
	}

	/* Javadoc Inherited. */
	public boolean init(HashMap props) {
		String strMin = (String)props.get( "min" );
		String strThreshold = (String)props.get( "threshold" );
		String strMax = (String)props.get( "max" );

		if ( ( strMin != null ) && ( strMin.length() > 0 ) ) {
			try {
				min = Integer.parseInt( strMin );
			}
			catch (NumberFormatException e) {
				min = 2;
			}
			//return false;
		}

		if ( ( strThreshold != null ) && ( strThreshold.length() > 0 ) ) {
			try {
				threshold = Integer.parseInt( strThreshold );
			}
			catch (NumberFormatException e) {
				threshold = 2;
			}
			//return false;
		}

		if ( ( strMax != null ) && ( strMax.length() > 0 ) ) {
			try {
				max = Integer.parseInt( strMax );
			}
			catch (NumberFormatException e) {
				max = 2;
			}
			//return false;
		}

		return true;
	}

	/* Javadoc Inherited. */
	public void setThreadMold(IThreadWorker _threadWorker) {
		threadWorker = _threadWorker;
		threadWorker.setThreadPool( this );
	}

	/* Javadoc Inherited. */
	public synchronized void register() {
		++workerThreads;
		++workingThreads;
	}

	/* Javadoc Inherited. */
	public synchronized void unregister() {
		--workerThreads;
		--workingThreads;
	}

	/* Javadoc Inherited. */
	public synchronized void checkIn() {
		--workingThreads;
		++idleThreads;
	}

	/* Javadoc Inherited. */
	public synchronized void checkOut() {
		++workingThreads;
		--idleThreads;
		if ( idleThreads < minIdle ) {
			minIdle = idleThreads;
		}
	}

	/* Javadoc Inherited. */
	public synchronized boolean stop() {
		if ( stop == 0 ) {
			return false;
		}
		else {
			--stop;
			return true;
		}
	}

	public boolean start() {
		Thread t;

		if ( running ) {
			throw new IllegalStateException( "ThreadPool already initialized!" );
		}

		t = new Thread( threadGroup, new InnerThread() );
		t.start();

		running = true;

		System.out.println( "ThreadPool initialized." );
		System.out.println( " min: " + min + " - threshold: " + threshold + " - max: " + max );
		
		return true;
	}

	/** ThreadPool object. */
	private IThreadPool threadPool;

	class InnerThread implements Runnable {

		public InnerThread() {
		}

		public void run() {
			IThreadWorker w;
			Thread t;
			int n;

			long ctm = 0;

			int sampleSeconds = idleSampleSecsInit;
			int localMinIdle = 0;

			/*
			 * Idle monitoring objects.
			 */

			idleObjects = new IdleObject[ max + 1 ];
			IdleObject prevIdleObj = null;
			IdleObject idleObj = null;
			IdleObject nextIdleObj = null;

			for ( int i=0; i<=max; ++i ) {
				idleObj = new IdleObject( i );
				idleObjects[ i ] = idleObj;
				if ( i > 0 ) {
					prevIdleObj.next = idleObj;
					idleObj.prev = prevIdleObj;
				}
				prevIdleObj = idleObj;
			}

			rootIdleObj = idleObjects[ 0 ];

			/*
			 * Monitor loop.
			 */

			while ( !exit ) {
				// debug
				//System.out.println( workerThreads + " - " + workingThreads + " - " + idleThreads );
				//System.out.println( workerList.size() );

				if ( ( workers < max ) && ( idleThreads < threshold ) ) {
					/*
					 * Add.
					 */
					n = threshold - idleThreads;
					if ( ( workers + n > max ) ) {
						n = max - workers;
					}
					for( int i=0; i<n; ++i ) {
						w = (IThreadWorker)threadWorker.clone();
						w.setThreadPool( threadPool );
						t = new Thread( threadGroup, w );
						//t.setPriority( 1 );
						t.start();

						++workers;
						//workerList.add( new WorkerEntry( w, t ) );

						// debug
						//System.out.println( "Worker spawned." );
					}
				}
				else {
					/*
					 * New min idle.
					 */
					if ( sampleSeconds < 0 ) {
						sampleSeconds = idleSampleSecs;
						synchronized( this ) {
							localMinIdle = minIdle;
							minIdle = idleThreads;
						}
						// debug
						//System.out.println( workers + " - " + localMinIdle);

						/*
						 * Last modified.
						 */

						ctm = System.currentTimeMillis();
						idleObjects[ localMinIdle ].lastModified = ctm;

						/*
						 * Remove old idleObjects.
						 */
						idleObj = rootIdleObj;

						while ( idleObj != null ) {
							if ( ( ctm - idleObj.lastModified ) > ( lastModifiedTimeout ) ) {
								prevIdleObj = idleObj.prev;
								nextIdleObj = idleObj.next;
								if ( nextIdleObj != null ) {
									nextIdleObj.prev = prevIdleObj;
								}
								if ( prevIdleObj != null ) {
									prevIdleObj.next = nextIdleObj;
								}
								else {
									rootIdleObj = nextIdleObj;
								}
								idleObj.prev = null;
								idleObj.next = null;
								idleObj = nextIdleObj;
							}
							else {
								idleObj = idleObj.next;
							}
						}
						/*
						 * Insert new IdleObject.
						 */
						idleObj = idleObjects[ localMinIdle ];
						if ( rootIdleObj == null ) {
							rootIdleObj = idleObj;
						}
						else if ( ( idleObj.prev == null ) && ( idleObj.next == null ) && ( idleObj != rootIdleObj ) ) {
							prevIdleObj = null;
							nextIdleObj = rootIdleObj;

							boolean b = true;
							/*
							 * Compare.
							 */
							while ( b ) {
								if ( idleObj.index < nextIdleObj.index ) {
									b = false;
								}
								else {
									prevIdleObj = nextIdleObj;
									nextIdleObj = nextIdleObj.next;
									if ( nextIdleObj == null ) {
										b = false;
									}
								}
							}
							/*
							 * Insert.
							 */
							if ( prevIdleObj == null ) {
								rootIdleObj = idleObj;
							}
							else {
								prevIdleObj.next = idleObj;
								idleObj.prev = prevIdleObj;
							}
							if ( nextIdleObj != null ) {
								idleObj.next = nextIdleObj;
								nextIdleObj.prev = idleObj;
							}
						}
						/*
						 * Print.
						 */
						/*
						 idleObj = rootIdleObj;
						 while ( idleObj != null ) {
							 System.out.println( idleObj.index);
							 idleObj = idleObj.next;
						}
						*/
						/*
						 * Stop.
						 */
						int postWorkers;
						if ( ( rootIdleObj != null ) && ( rootIdleObj.index > threshold ) && ( workers > min ) ) {
							postWorkers = workers - ( rootIdleObj.index - threshold );
							if ( postWorkers < min ) {
								postWorkers = min;
							}
							stop += ( workers - postWorkers );
							workers -= ( workers - postWorkers );

							// debug
							//System.out.println( "Stopping: " + stop );
						}
					}
				}

				try {
					Thread.sleep( 1000 );
					--sampleSeconds;
				}
				catch (InterruptedException e) {
				}
			}
		}

/*
		public void validate() {
			IdleObject prevIdleObj = null;
			IdleObject idleObj = rootIdleObj;

			while ( idleObj != null ) {
				// debug
				System.out.println( idleObj );
				if ( idleObj.prev == idleObj ) {
					System.out.println( "error: prev" );
					System.exit( 1 );
				}
				if ( idleObj.next == idleObj ) {
					System.out.println( "error: next" );
					System.exit( 1 );
				}
				prevIdleObj = idleObj;
				idleObj = idleObj.next;
			}
		}
*/
	}

	class IdleObject {

		int index;
		long lastModified;

		IdleObject prev = null;
		IdleObject next = null;

		IdleObject(int _index) {
			index = _index;
		}

	}

	/*
	class WorkerEntry {

		public final IThreadWorker worker;
		public final Thread thread;
		public final long initTime;

		public WorkerEntry(IThreadWorker _worker, Thread _thread) {
			worker = _worker;
			thread = _thread;
			initTime = System.currentTimeMillis();
		}

	}
	*/

}

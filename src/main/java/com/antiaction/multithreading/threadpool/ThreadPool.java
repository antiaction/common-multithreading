/*
 * Advanced ThreadPool manager.
 * Copyright (C) 2001, 2002, 2003, 2010  Nicholas Clarke
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
 * 19-Nov-2003 : Moved ThreadPool code to separate class.
 * 23-Nov-2003 : IWorker interface renamed to IThreadWorker.
 * 23-Nov-2003 : Modified against new interfaces.
 * 30-Aug-2010 : Separated monitoring thread from pool.
 * 31-Aug-2010 : Renamed several variables and refactored some methods.
 * 06-Sep-2010 : Added check pool for dead threads and various other enhancements.
 *
 */

package com.antiaction.multithreading.threadpool;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.antiaction.multithreading.resourcemanage.IResourcePool;
import com.antiaction.multithreading.resourcemanage.ResourceManager;

/**
 * Advanced ThreadPool.
 *
 * @version 1.00
 * @author Nicholas Clarke <mayhem[at]antiaction[dot]com>
 */
public class ThreadPool implements IThreadPool, IResourcePool {

	/** Shutdown boolean. */
	protected boolean exit = false;

	/** Has thread been started. */
	protected boolean running = false;

	/** Resource manager monitoring instance. */
	protected ResourceManager resourceManager;

	/** Resource manager monitoring thread. */
	protected Thread resourceManagerThread;

	/** ThreadPool object. */
	protected IThreadPool threadPool;

	/** ThreadGroup. */
	protected ThreadGroup threadGroup;

	/** Original thread, used for cloning. */
	protected IThreadWorker threadWorker;

	/** Total amount of thread allocated. */
	protected int allocated_threads = 0;

	/** Idle thread. */
	protected int idle_threads = 0;

	/** Thread to stop. */
	protected int overflowing_threads = 0;

	/** Worker Threads. */
	protected int registered_threads = 0;

	/** Working Threads. */
	protected int busy_threads = 0;

	protected List threadList;

	protected List idleList;

	protected Set busySet;

	public ThreadPool() {
		resourceManager = new ResourceManager( this );

		threadGroup = new ThreadGroup( "ThreadPool" );
		threadPool = this;
		threadList = new ArrayList();
		idleList = new ArrayList();
		busySet = new HashSet();
	}

	/* Javadoc Inherited. */
	public boolean init(Map props) {
		String strMin = (String)props.get( "min" );
		String strThreshold = (String)props.get( "threshold" );
		String strMax = (String)props.get( "max" );

		int min = 1;
		int threshold = 1;
		int max = 1;

		if ( ( strMin != null ) && ( strMin.length() > 0 ) ) {
			try {
				min = Integer.parseInt( strMin );
			}
			catch (NumberFormatException e) {
				min = 1;
			}
		}

		if ( ( strThreshold != null ) && ( strThreshold.length() > 0 ) ) {
			try {
				threshold = Integer.parseInt( strThreshold );
			}
			catch (NumberFormatException e) {
				threshold = 1;
			}
		}

		if ( ( strMax != null ) && ( strMax.length() > 0 ) ) {
			try {
				max = Integer.parseInt( strMax );
			}
			catch (NumberFormatException e) {
				max = 1;
			}
		}

		resourceManager.setMin( min );
		resourceManager.setThreshold( threshold );
		resourceManager.setMax( max );

		return true;
	}

	public boolean start() {
		if ( !running ) {
			resourceManagerThread = new Thread( threadGroup, resourceManager );
			resourceManagerThread.start();

			running = true;

			System.out.println( "ThreadPool initialized." );
			System.out.println( " min: " + resourceManager.getMin() + " - threshold: " + resourceManager.getThreshold() + " - max: " + resourceManager.getMax() );
		}

		return running;
	}

	/* Javadoc Inherited. */
	public void setThreadMold(IThreadWorker _threadWorker) {
		threadWorker = _threadWorker;
		threadWorker.setThreadPool( this );
	}

	/* Javadoc Inherited. */
	public void register() {
		synchronized ( this ) {
			++registered_threads;
			++busy_threads;
		}
	}

	/* Javadoc Inherited. */
	public void unregister() {
		synchronized ( this ) {
			--registered_threads;
			--busy_threads;
		}
	}

	/* Javadoc Inherited. */
	public void checkIn() {
		synchronized ( this ) {
			--busy_threads;
			++idle_threads;
			resourceManager.update( allocated_threads, idle_threads );
		}
	}

	/* Javadoc Inherited. */
	public void checkOut() {
		synchronized ( this ) {
			++busy_threads;
			--idle_threads;
			resourceManager.update( allocated_threads, idle_threads );
		}
	}

	/* Javadoc Inherited. */
	public boolean stop() {
		synchronized ( this ) {
			if ( overflowing_threads <= 0 ) {
				return false;
			}
			else {
				--overflowing_threads;
				return true;
			}
		}
	}

	public void allocate(int n) {
		IThreadWorker w;
		Thread t;
		for( int i=0; i<n; ++i ) {
			w = (IThreadWorker)threadWorker.clone();
			w.setThreadPool( threadPool );
			t = new Thread( threadGroup, w );
			//t.setPriority( 1 );
			t.start();

			synchronized ( this ) {
				++allocated_threads;
				resourceManager.update( allocated_threads, idle_threads );
			}

			//workerList.add( new WorkerEntry( w, t ) );

			// debug
			//System.out.println( "Worker spawned." );
		}
	}

	public void release(int n) {
		synchronized ( this ) {
			overflowing_threads += n;
			allocated_threads -= n;
			resourceManager.update( allocated_threads, idle_threads );
		}
	}

	public void check_pool() {
		List checkList;
		Thread t;
		synchronized ( this ) {
			checkList = new ArrayList( threadList );
		}
		int i = 0;
		while ( i<checkList.size() ) {
			t = (Thread)checkList.get( i );
			if ( !t.isAlive() ) {
				synchronized ( this ) {
					if ( busySet.contains( t ) ) {
						--allocated_threads;
						--registered_threads;
						--busy_threads;
						busySet.remove( t );
					}
					else if ( idleList.contains( t ) ) {
						--allocated_threads;
						--registered_threads;
						--idle_threads;
						idleList.remove( t );
					}
					threadList.remove( t );
				}

				// debug
				System.out.println( "Dead thread: " + t );
			}
			++i;
		}
		resourceManager.update( allocated_threads, idle_threads );
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

/*
 * Created on 21/01/2012
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

package com.antiaction.multithreading.threadpool;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class TestThreadPool {

	@Test
	public void test() {
		Map props = new HashMap();
		props.put( "min", "1" );
		props.put( "min-idle", "1" );
		props.put( "max", "1" );	
		ThreadPool tp = new ThreadPool();
		tp.setThreadMold( new Worker() );
		tp.init( props );
		tp.start();

		try {
			Thread.sleep( 10000 );
		}
		catch(InterruptedException e) {
		}
	}

	public static class Worker implements IThreadWorker {

		IThreadPool threadPool;

		public Object clone() {
			return new Worker();
		}

		public void setThreadPool(IThreadPool _threadPool) {
			this.threadPool = _threadPool;
		}

		public void run() {
			threadPool.register();
			while ( threadPool.stop() ) {
				threadPool.checkOut();
				try {
					Thread.sleep( 1000 );
				}
				catch (InterruptedException e) {
				}
				threadPool.checkIn();
			}
			threadPool.unregister();
		}

	}

}

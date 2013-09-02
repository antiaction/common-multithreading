/*
 * Created on 26/01/2012
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

package com.antiaction.multithreading.resourcemanage;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class TestResourceManager implements IResourcePool {

	@Test
	public void test_resourcemanager_getset() {
		ResourceManager resMan = new ResourceManager( this );

		Assert.assertEquals( 1, resMan.min );
		Assert.assertEquals( 1, resMan.threshold );
		Assert.assertEquals( 1, resMan.max );
		Assert.assertEquals( 60, resMan.idleSampleSecsInit );
		Assert.assertEquals( 60, resMan.idleSampleSecs );
		Assert.assertEquals( 30 * 60 * 1000, resMan.lastTimeIdleTimeoutMillis );

		Assert.assertEquals( 1, resMan.getMin() );
		Assert.assertEquals( 1, resMan.getThreshold() );
		Assert.assertEquals( 1, resMan.getMax() );
		Assert.assertEquals( 60, resMan.getIdleSampleSecsInit() );
		Assert.assertEquals( 60, resMan.getIdleSampleSecs() );
		Assert.assertEquals( 30 * 60 * 1000, resMan.getLastTimeIdleTimeoutMillis() );

		resMan.setMin( 2 );
		resMan.setThreshold( 4 );
		resMan.setMax( 16 );
		resMan.setIdleSampleSecsInit( 5 );
		resMan.setIdleSampleSecs( 10 );
		resMan.setLastTimeIdleTimeoutMillis( 3 * 60 * 1000 );

		Assert.assertEquals( 2, resMan.min );
		Assert.assertEquals( 4, resMan.threshold );
		Assert.assertEquals( 16, resMan.max );
		Assert.assertEquals( 5, resMan.idleSampleSecsInit );
		Assert.assertEquals( 10, resMan.idleSampleSecs );
		Assert.assertEquals( 3 * 60 * 1000, resMan.lastTimeIdleTimeoutMillis );

		Assert.assertEquals( 2, resMan.getMin() );
		Assert.assertEquals( 4, resMan.getThreshold() );
		Assert.assertEquals( 16, resMan.getMax() );
		Assert.assertEquals( 5, resMan.getIdleSampleSecsInit() );
		Assert.assertEquals( 10, resMan.getIdleSampleSecs() );
		Assert.assertEquals( 3 * 60 * 1000, resMan.getLastTimeIdleTimeoutMillis() );

		Assert.assertFalse( resMan.running );
		Assert.assertFalse( resMan.exit );

		Assert.assertEquals( 0, resMan.allocated );
		Assert.assertEquals( 0, resMan.idle );
		Assert.assertEquals( 0, resMan.minIdle );

		resMan.update( 2, 1 );

		Assert.assertEquals( 2, resMan.allocated );
		Assert.assertEquals( 1, resMan.idle );
		Assert.assertEquals( 0, resMan.minIdle );

		resMan.update( 1, 2 );

		Assert.assertEquals( 1, resMan.allocated );
		Assert.assertEquals( 2, resMan.idle );
		Assert.assertEquals( 0, resMan.minIdle );

		resMan.update( 0, 0 );

		Assert.assertEquals( 0, resMan.allocated );
		Assert.assertEquals( 0, resMan.idle );
		Assert.assertEquals( 0, resMan.minIdle );
	}

	private ResourceManager resMan;

	private int allocated;

	private int idle;

	private int release;

	@Test
	public void test_resourcemanager_thread() {
		resMan = new ResourceManager( this );

		allocated = 0;
		idle = 0;
		release = 0;

		resMan.setMin( 2 );
		resMan.setThreshold( 4 );
		resMan.setMax( 16 );
		resMan.setIdleSampleSecsInit( 2 );
		resMan.setIdleSampleSecs( 2 );
		resMan.setLastTimeIdleTimeoutMillis( 5 * 60 * 1000 );

		Thread rmThread = new Thread( resMan );
		rmThread.start();

		try {
			Thread.sleep( 1 * 1000 );
			idleDec();
			Thread.sleep( 1 * 1000 );
			idleDec();
			Thread.sleep( 1 * 1000 );
			idleDec();
			Thread.sleep( 1 * 1000 );
			idleDec();
			Thread.sleep( 1 * 1000 );
			idleDec();
			Thread.sleep( 1 * 1000 );
			idleDec();
			Thread.sleep( 1 * 1000 );
			idleDec();
			Thread.sleep( 1 * 1000 );
			idleDec();
			Thread.sleep( 1 * 1000 );
			idleDec();
			Thread.sleep( 1 * 1000 );
			idleDec();
			Thread.sleep( 1 * 1000 );
			idleDec();
			Thread.sleep( 1 * 1000 );
			idleDec();
			Thread.sleep( 1 * 1000 );
			idleDec();
			Thread.sleep( 1 * 1000 );
			idleDec();
			Thread.sleep( 1 * 1000 );
			idleDec();
			Thread.sleep( 1 * 1000 );
			idleDec();
			Thread.sleep( 1 * 1000 );
		}
		catch (InterruptedException e) {
			e.printStackTrace();
			Assert.fail( "Exception not expected!" );
		}
		resMan.stop();
	}

	public void idleDec() {
		synchronized ( resMan ) {
			idle -= 1;
		}
		resMan.update( allocated, idle );
	}

	public void allocate(int n) {
		System.out.println( "Allocate: " + n );
		synchronized ( resMan ) {
			allocated += n;
			idle += n;
		}
		resMan.update( allocated, idle );
	}

	public void release(int n) {
		System.out.println( "Release: " + n );
		synchronized ( resMan ) {
			allocated -= n;
			release += n;
			if (n < idle) {
				Assert.fail( "Request release of more resources than are idle!" );
			}
			idle -= n;
		}
		resMan.update( allocated, idle );
	}

	public void check_pool() {
		System.out.println( "check pool" );
	}

}

package com.antiaction.multithreading.threadlocal;

import java.text.DateFormat;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class TestThreadLocalSimpleDateFormat {

    private ThreadLocalSimpleDateFormat threadLocalSimpleDateFormatPool;
    private DateFormat df1;
    private DateFormat df2;
    private DateFormat df3;
    private DateFormat df4;

    @Test
	public void test() {
	    threadLocalSimpleDateFormatPool = ThreadLocalSimpleDateFormat.getInstanceDefaultTZ("[dd/MMM/yyyy:HH:mm:ss Z']'");
	    Runnable r1 = new TestRunnable1();
	    Runnable r2 = new TestRunnable2();
	    new Thread(r1).start();
	    synchronized (r1) {
		    try {
			    r1.wait();
		    }
		    catch (InterruptedException e) {
		    	Assert.fail("Unexpected exception!");
		    }
	    }
	    new Thread(r2).start();
	    synchronized (r2) {
		    try {
			    r2.wait();
		    }
		    catch (InterruptedException e) {
		    	Assert.fail("Unexpected exception!");
		    }
	    }
	    Assert.assertTrue(df1 == df2);
	    Assert.assertTrue(df3 == df4);
	    Assert.assertFalse(df1 == df3);
	    Assert.assertFalse(df2 == df4);
	}

    public class TestRunnable1 implements Runnable {
		@Override
		public void run() {
		    df1 = threadLocalSimpleDateFormatPool.getDateFormat();
		    df2 = threadLocalSimpleDateFormatPool.getDateFormat();
			synchronized (this) {
				this.notify();
			}
			try {
				Thread.sleep(1000);
			}
			catch (InterruptedException e) {
			}
		}
    }

    public class TestRunnable2 implements Runnable {
		@Override
		public void run() {
			df3 = threadLocalSimpleDateFormatPool.getDateFormat();
			df4 = threadLocalSimpleDateFormatPool.getDateFormat();
			synchronized (this) {
				this.notify();
			}
			try {
				Thread.sleep(1000);
			}
			catch (InterruptedException e) {
			}
		}
    }

}

/*
 * Created on 31/08/2010
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

package com.antiaction.multithreading.threadpool;

public interface IResourcePool {

	public void allocate(int n);

	public void release(int n);

}

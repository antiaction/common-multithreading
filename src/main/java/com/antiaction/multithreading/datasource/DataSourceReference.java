/*
 * Created on 14/06/2011
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

package com.antiaction.multithreading.datasource;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

import javax.sql.DataSource;

/**
 * Implements a weak-reference to the datasource using the finalize method to detect when the datasource can be shutdown.
 * @author Nicholas
 *
 */
public class DataSourceReference implements DataSource {

	private DataSourcePoolImpl ds = null;

	protected DataSourceReference(DataSourcePoolImpl ds) {
		this.ds = ds;
	}

	public static DataSource getDataSource(Map attribs, Map props) {
		return new DataSourceReference( DataSourcePoolImpl.getDataSource( attribs, props ) );
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#finalize()
	 */
	//@Override
	protected void finalize() throws Throwable {
		// debug
		System.out.println( this + ".finalize()" );
		ds.stop();
		super.finalize();
	}

	public Connection getConnection() throws SQLException {
		return ds.getConnection();
	}

	public Connection getConnection(String username, String password) throws SQLException {
		return ds.getConnection( username, password );
	}

	public PrintWriter getLogWriter() throws SQLException {
		return ds.getLogWriter();
	}

	public void setLogWriter(PrintWriter out) throws SQLException {
		ds.setLogWriter( out );
	}

	public void setLoginTimeout(int seconds) throws SQLException {
		ds.setLoginTimeout( seconds );
	}

	public int getLoginTimeout() throws SQLException {
		return ds.getLoginTimeout();
	}

	/*
	 * JDK6
	 */

	public <T> T unwrap(Class<T> iface) throws SQLException {
		return ds.unwrap( iface );
	}

	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		return isWrapperFor( iface );
	}

}

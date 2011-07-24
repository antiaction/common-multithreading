/*
 * Created on 14/06/2011
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

package com.antiaction.raptor.base;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

import javax.sql.DataSource;

import com.antiaction.multithreading.datasource.DataSourcePoolImpl;

public class DataSourceReference implements DataSource {

	private DataSourcePoolImpl ds = null;

	protected DataSourceReference(DataSourcePoolImpl ds) {
		this.ds = ds;
	}

	public static DataSource getDataSource(Map<String, String> attribs, Map<String, String> props) {
		return new DataSourceReference( DataSourcePoolImpl.getDataSource( attribs, props ) );
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#finalize()
	 */
	@Override
	protected void finalize() throws Throwable {
		// debug
		System.out.println( this + ".finalize()" );
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

}

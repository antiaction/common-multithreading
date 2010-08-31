/*
 * Created on 29/08/2010
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

package com.antiaction.common.database;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

public class DataSourcePoolImpl implements DataSource {

	protected int timeout = 0;

	protected PrintWriter log_writer = null;

	protected DataSourcePoolImpl() {
	}

	public static DataSource getDataSource() {
		return null;
	}

	public void setLoginTimeout(int seconds) throws SQLException {
		timeout = seconds;
	}

	public int getLoginTimeout() throws SQLException {
		return timeout;
	}

	public void setLogWriter(PrintWriter out) throws SQLException {
		log_writer = out;
	}

	public PrintWriter getLogWriter() throws SQLException {
		return log_writer;
	}

	public Connection getConnection(String username, String password) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public Connection getConnection() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

}

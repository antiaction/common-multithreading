/*
 * Created on 30/08/2010
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

package com.antiaction.multithreading.datasource;

import java.sql.Connection;
import java.sql.SQLException;

import com.antiaction.common.database.ConnectionWrapper;

public class ConnectionPooled extends ConnectionWrapper {

	protected DataSourcePoolImpl dspi;

	protected boolean bClosed = false;

	public static ConnectionPooled getInstance(DataSourcePoolImpl dspi, Connection connection) throws SQLException {
		ConnectionPooled cp = new ConnectionPooled();
		cp.dspi = dspi;
		cp.connection = connection;
		cp.bClosed = connection.isClosed();
		return cp;
	}

	public boolean isClosed() throws SQLException {
		return (bClosed && connection.isClosed());
	}

	public void close() throws SQLException {
		if ( !bClosed ) {
			bClosed = true;
			dspi.closeConnection( this );
			connection = null;
		}
	}

	public Connection getOriginalConnection() {
		return connection;
	}

}

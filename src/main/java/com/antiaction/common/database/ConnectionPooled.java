/*
 * Created on 30/08/2010
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

package com.antiaction.common.database;

import java.sql.Connection;
import java.sql.SQLException;

public class ConnectionPooled extends ConnectionWrapper {

	protected boolean bClosed = false;

	public static ConnectionPooled getInstance(Connection connection) throws SQLException {
		ConnectionPooled cp = new ConnectionPooled();
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
			connection = null;
		}
	}

}

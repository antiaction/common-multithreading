/*
 * Statement wrapper.
 * Copyright (C) 2011, 2013  Nicholas Clarke
 *
 */

/*
 * History:
 *
 * 19-Aug-2011 : Initial implementation.
 * 05-Sep-2013 : Added logging.
 *
 */

package com.antiaction.multithreading.datasource;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

public class StatementWrapper implements Statement, Comparable {

    /** Logging mechanism. */
	private static Logger logger = Logger.getLogger( StatementWrapper.class.getName() );

	private static int keyInc = 1;

	protected long key;

	private boolean bClosed;

	private Statement stm;

	private ConnectionPooled connection;

	public StatementWrapper(Statement stm, ConnectionPooled connection) {
		this.stm = stm;
		this.connection = connection;
		synchronized (this.getClass()) {
			key = keyInc++;
		}
	}

	public int compareTo(Object o) {
		long dx = ((StatementWrapper)o).key - key;
		if ( dx > 0 ) {
			return 1;
		}
		else if ( dx < 0 ) {
			return -1;
		}
		else {
			return 0;
		}
	}

	public ResultSet executeQuery(String sql) throws SQLException {
		return stm.executeQuery( sql );
	}

	public int executeUpdate(String sql) throws SQLException {
		return stm.executeUpdate( sql );
	}

	public void close() throws SQLException {
		if ( !bClosed ) {
			stm.close();
			connection.closeStatement( this );
			stm = null;
			connection = null;
			bClosed = true;
		}
	}

	public int getMaxFieldSize() throws SQLException {
		if ( stm == null ) {
			logger.log( Level.SEVERE, "Database statement closed!" );
			throw new SQLException( "Database statement closed!" );
		}
		return stm.getMaxFieldSize();
	}

	public void setMaxFieldSize(int max) throws SQLException {
		if ( stm == null ) {
			logger.log( Level.SEVERE, "Database statement closed!" );
			throw new SQLException( "Database statement closed!" );
		}
		stm.setMaxFieldSize( max );
	}

	public int getMaxRows() throws SQLException {
		if ( stm == null ) {
			logger.log( Level.SEVERE, "Database statement closed!" );
			throw new SQLException( "Database statement closed!" );
		}
		return stm.getMaxRows();
	}

	public void setMaxRows(int max) throws SQLException {
		if ( stm == null ) {
			logger.log( Level.SEVERE, "Database statement closed!" );
			throw new SQLException( "Database statement closed!" );
		}
		stm.setMaxRows( max );
	}

	public void setEscapeProcessing(boolean enable) throws SQLException {
		if ( stm == null ) {
			logger.log( Level.SEVERE, "Database statement closed!" );
			throw new SQLException( "Database statement closed!" );
		}
		stm.setEscapeProcessing( enable );
	}

	public int getQueryTimeout() throws SQLException {
		if ( stm == null ) {
			logger.log( Level.SEVERE, "Database statement closed!" );
			throw new SQLException( "Database statement closed!" );
		}
		return stm.getQueryTimeout();
	}

	public void setQueryTimeout(int seconds) throws SQLException {
		if ( stm == null ) {
			logger.log( Level.SEVERE, "Database statement closed!" );
			throw new SQLException( "Database statement closed!" );
		}
		stm.setQueryTimeout( seconds );
	}

	public void cancel() throws SQLException {
		if ( stm == null ) {
			logger.log( Level.SEVERE, "Database statement closed!" );
			throw new SQLException( "Database statement closed!" );
		}
		stm.cancel();
	}

	public SQLWarning getWarnings() throws SQLException {
		if ( stm == null ) {
			logger.log( Level.SEVERE, "Database statement closed!" );
			throw new SQLException( "Database statement closed!" );
		}
		return stm.getWarnings();
	}

	public void clearWarnings() throws SQLException {
		if ( stm == null ) {
			logger.log( Level.SEVERE, "Database statement closed!" );
			throw new SQLException( "Database statement closed!" );
		}
		stm.clearWarnings();
	}

	public void setCursorName(String name) throws SQLException {
		if ( stm == null ) {
			logger.log( Level.SEVERE, "Database statement closed!" );
			throw new SQLException( "Database statement closed!" );
		}
		stm.setCursorName( name );
	}

	public boolean execute(String sql) throws SQLException {
		if ( stm == null ) {
			logger.log( Level.SEVERE, "Database statement closed!" );
			throw new SQLException( "Database statement closed!" );
		}
		return stm.execute( sql );
	}

	public ResultSet getResultSet() throws SQLException {
		if ( stm == null ) {
			logger.log( Level.SEVERE, "Database statement closed!" );
			throw new SQLException( "Database statement closed!" );
		}
		return stm.getResultSet();
	}

	public int getUpdateCount() throws SQLException {
		if ( stm == null ) {
			logger.log( Level.SEVERE, "Database statement closed!" );
			throw new SQLException( "Database statement closed!" );
		}
		return stm.getUpdateCount();
	}

	public boolean getMoreResults() throws SQLException {
		if ( stm == null ) {
			logger.log( Level.SEVERE, "Database statement closed!" );
			throw new SQLException( "Database statement closed!" );
		}
		return stm.getMoreResults();
	}

	public void setFetchDirection(int direction) throws SQLException {
		if ( stm == null ) {
			logger.log( Level.SEVERE, "Database statement closed!" );
			throw new SQLException( "Database statement closed!" );
		}
		stm.setFetchDirection( direction );
	}

	public int getFetchDirection() throws SQLException {
		if ( stm == null ) {
			logger.log( Level.SEVERE, "Database statement closed!" );
			throw new SQLException( "Database statement closed!" );
		}
		return stm.getFetchDirection();
	}

	public void setFetchSize(int rows) throws SQLException {
		if ( stm == null ) {
			logger.log( Level.SEVERE, "Database statement closed!" );
			throw new SQLException( "Database statement closed!" );
		}
		stm.setFetchSize( rows );
	}

	public int getFetchSize() throws SQLException {
		if ( stm == null ) {
			logger.log( Level.SEVERE, "Database statement closed!" );
			throw new SQLException( "Database statement closed!" );
		}
		return stm.getFetchSize();
	}

	public int getResultSetConcurrency() throws SQLException {
		if ( stm == null ) {
			logger.log( Level.SEVERE, "Database statement closed!" );
			throw new SQLException( "Database statement closed!" );
		}
		return stm.getResultSetConcurrency();
	}

	public int getResultSetType() throws SQLException {
		if ( stm == null ) {
			logger.log( Level.SEVERE, "Database statement closed!" );
			throw new SQLException( "Database statement closed!" );
		}
		return stm.getResultSetType();
	}

	public void addBatch(String sql) throws SQLException {
		if ( stm == null ) {
			logger.log( Level.SEVERE, "Database statement closed!" );
			throw new SQLException( "Database statement closed!" );
		}
		stm.addBatch( sql );
	}

	public void clearBatch() throws SQLException {
		if ( stm == null ) {
			logger.log( Level.SEVERE, "Database statement closed!" );
			throw new SQLException( "Database statement closed!" );
		}
		stm.clearBatch();
	}

	public int[] executeBatch() throws SQLException {
		if ( stm == null ) {
			logger.log( Level.SEVERE, "Database statement closed!" );
			throw new SQLException( "Database statement closed!" );
		}
		return stm.executeBatch();
	}

	public Connection getConnection() throws SQLException {
		if ( stm == null ) {
			logger.log( Level.SEVERE, "Database statement closed!" );
			throw new SQLException( "Database statement closed!" );
		}
		return stm.getConnection();
	}

	public boolean getMoreResults(int current) throws SQLException {
		if ( stm == null ) {
			logger.log( Level.SEVERE, "Database statement closed!" );
			throw new SQLException( "Database statement closed!" );
		}
		return stm.getMoreResults( current );
	}

	public ResultSet getGeneratedKeys() throws SQLException {
		if ( stm == null ) {
			logger.log( Level.SEVERE, "Database statement closed!" );
			throw new SQLException( "Database statement closed!" );
		}
		return stm.getGeneratedKeys();
	}

	public int executeUpdate(String sql, int autoGeneratedKeys) throws SQLException {
		if ( stm == null ) {
			logger.log( Level.SEVERE, "Database statement closed!" );
			throw new SQLException( "Database statement closed!" );
		}
		return stm.executeUpdate( sql, autoGeneratedKeys );
	}

	public int executeUpdate(String sql, int[] columnIndexes) throws SQLException {
		if ( stm == null ) {
			logger.log( Level.SEVERE, "Database statement closed!" );
			throw new SQLException( "Database statement closed!" );
		}
		return stm.executeUpdate( sql, columnIndexes );
	}

	public int executeUpdate(String sql, String[] columnNames) throws SQLException {
		if ( stm == null ) {
			logger.log( Level.SEVERE, "Database statement closed!" );
			throw new SQLException( "Database statement closed!" );
		}
		return stm.executeUpdate( sql, columnNames );
	}

	public boolean execute(String sql, int autoGeneratedKeys) throws SQLException {
		if ( stm == null ) {
			logger.log( Level.SEVERE, "Database statement closed!" );
			throw new SQLException( "Database statement closed!" );
		}
		return stm.execute( sql, autoGeneratedKeys );
	}

	public boolean execute(String sql, int[] columnIndexes) throws SQLException {
		if ( stm == null ) {
			logger.log( Level.SEVERE, "Database statement closed!" );
			throw new SQLException( "Database statement closed!" );
		}
		return stm.execute( sql, columnIndexes );
	}

	public boolean execute(String sql, String[] columnNames) throws SQLException {
		if ( stm == null ) {
			logger.log( Level.SEVERE, "Database statement closed!" );
			throw new SQLException( "Database statement closed!" );
		}
		return stm.execute( sql, columnNames );
	}

	public int getResultSetHoldability() throws SQLException {
		if ( stm == null ) {
			logger.log( Level.SEVERE, "Database statement closed!" );
			throw new SQLException( "Database statement closed!" );
		}
		return stm.getResultSetHoldability();
	}

	/*
	 * JDK6
	 */

	public boolean isPoolable() throws SQLException {
		if ( stm == null ) {
			logger.log( Level.SEVERE, "Database statement closed!" );
			throw new SQLException( "Database statement closed!" );
		}
		return stm.isPoolable();
	}

	public void setPoolable(boolean poolable) throws SQLException {
		if ( stm == null ) {
			logger.log( Level.SEVERE, "Database statement closed!" );
			throw new SQLException( "Database statement closed!" );
		}
		stm.setPoolable( poolable );
	}

	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		if ( stm == null ) {
			logger.log( Level.SEVERE, "Database statement closed!" );
			throw new SQLException( "Database statement closed!" );
		}
		return stm.isWrapperFor( iface );
	}

	public <T> T unwrap(Class<T> iface) throws SQLException {
		if ( stm == null ) {
			logger.log( Level.SEVERE, "Database statement closed!" );
			throw new SQLException( "Database statement closed!" );
		}
		return stm.unwrap( iface );
	}

	// FIXME
	public boolean isClosed() throws SQLException {
		if ( stm != null ) {
			return stm.isClosed();
		}
		else {
			return false;
		}
	}

}

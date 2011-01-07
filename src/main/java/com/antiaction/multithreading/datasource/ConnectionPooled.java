/*
 * Created on 30/08/2010
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

package com.antiaction.multithreading.datasource;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Savepoint;
import java.sql.Statement;
import java.util.Map;

public class ConnectionPooled implements Connection {

	/** Owning Connection Pool. */
	protected DataSourcePoolImpl dspi;

	/** Wrapped Connection object. */
	protected Connection connection;

	/** Has this wrapper been closed. */
	protected boolean bClosed = false;

	/** When wrapper was last called. */
	public long last_called = 0;

	public static ConnectionPooled getInstance(DataSourcePoolImpl dspi, Connection connection) throws SQLException {
		ConnectionPooled cp = new ConnectionPooled();
		cp.dspi = dspi;
		cp.connection = connection;
		cp.bClosed = connection.isClosed();
		cp.last_called = System.currentTimeMillis();
		return cp;
	}

	public Connection getOriginalConnection() {
		return connection;
	}

	public boolean isClosed() throws SQLException {
		boolean b = bClosed && connection.isClosed();
		last_called = System.currentTimeMillis();
		return b;
	}

	public void close() throws SQLException {
		if ( !bClosed ) {
			bClosed = true;
			dspi.closeConnection( this );
			connection = null;
			last_called = System.currentTimeMillis();
		}
	}

	public void clearWarnings() throws SQLException {
		connection.clearWarnings();
		last_called = System.currentTimeMillis();
	}

	public SQLWarning getWarnings() throws SQLException {
		SQLWarning warning = connection.getWarnings();
		last_called = System.currentTimeMillis();
		return warning;
	}

	public boolean isReadOnly() throws SQLException {
		boolean b = connection.isReadOnly();
		last_called = System.currentTimeMillis();
		return b;
	}

	public void setReadOnly(boolean readOnly) throws SQLException {
		connection.setReadOnly( readOnly );
		last_called = System.currentTimeMillis();
	}

	public void setHoldability(int holdability) throws SQLException {
		connection.setHoldability( holdability );
		last_called = System.currentTimeMillis();
	}

	public int getHoldability() throws SQLException {
		int holdability = connection.getHoldability();
		last_called = System.currentTimeMillis();
		return holdability;
	}

	public void setTypeMap(Map map) throws SQLException {
		connection.setTypeMap( map );
		last_called = System.currentTimeMillis();
	}

	public Map getTypeMap() throws SQLException {
		Map map = connection.getTypeMap();
		last_called = System.currentTimeMillis();
		return map;
	}

	public void setCatalog(String catalog) throws SQLException {
		connection.setCatalog( catalog );
		last_called = System.currentTimeMillis();
	}

	public String getCatalog() throws SQLException {
		String catalog = connection.getCatalog();
		last_called = System.currentTimeMillis();
		return catalog;
	}

	public DatabaseMetaData getMetaData() throws SQLException {
		DatabaseMetaData metadata = connection.getMetaData();
		last_called = System.currentTimeMillis();
		return metadata;
	}

	public boolean getAutoCommit() throws SQLException {
		boolean b = connection.getAutoCommit();
		last_called = System.currentTimeMillis();
		return b;
	}

	public void setAutoCommit(boolean autoCommit) throws SQLException {
		connection.setAutoCommit( autoCommit );
		last_called = System.currentTimeMillis();
	}

	public void commit() throws SQLException {
		connection.commit();
		last_called = System.currentTimeMillis();
	}

	public void rollback() throws SQLException {
		connection.rollback();
		last_called = System.currentTimeMillis();
	}

	public Savepoint setSavepoint() throws SQLException {
		Savepoint savepoint = connection.setSavepoint();
		last_called = System.currentTimeMillis();
		return savepoint;
	}

	public Savepoint setSavepoint(String name) throws SQLException {
		Savepoint savepoint = connection.setSavepoint( name );
		last_called = System.currentTimeMillis();
		return savepoint;
	}

	public void rollback(Savepoint savepoint) throws SQLException {
		connection.rollback( savepoint );
		last_called = System.currentTimeMillis();
	}

	public void releaseSavepoint(Savepoint savepoint) throws SQLException {
		connection.releaseSavepoint( savepoint );
		last_called = System.currentTimeMillis();
	}

	public void setTransactionIsolation(int level) throws SQLException {
		connection.setTransactionIsolation( level );
		last_called = System.currentTimeMillis();
	}

	public int getTransactionIsolation() throws SQLException {
		int isolation = connection.getTransactionIsolation();
		last_called = System.currentTimeMillis();
		return isolation;
	}

	public CallableStatement prepareCall(String sql) throws SQLException {
		CallableStatement cstatement = connection.prepareCall( sql );
		last_called = System.currentTimeMillis();
		return cstatement;
	}

	public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
		CallableStatement cstatement = connection.prepareCall( sql, resultSetType, resultSetConcurrency );
		last_called = System.currentTimeMillis();
		return cstatement;
	}

	public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
		CallableStatement cstatement = connection.prepareCall( sql, resultSetType, resultSetConcurrency, resultSetHoldability );
		last_called = System.currentTimeMillis();
		return cstatement;
	}

	public PreparedStatement prepareStatement(String sql) throws SQLException {
		PreparedStatement pstatement = connection.prepareStatement( sql );
		last_called = System.currentTimeMillis();
		return pstatement;
	}

	public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
		PreparedStatement pstatement = connection.prepareStatement( sql, resultSetType, resultSetConcurrency );
		last_called = System.currentTimeMillis();
		return pstatement;
	}
		
	public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
		PreparedStatement pstatement = connection.prepareStatement( sql, resultSetType, resultSetConcurrency, resultSetHoldability );
		last_called = System.currentTimeMillis();
		return pstatement;
	}

	public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys) throws SQLException {
		PreparedStatement pstatement = connection.prepareStatement( sql, autoGeneratedKeys );
		last_called = System.currentTimeMillis();
		return pstatement;
	}

	public PreparedStatement prepareStatement(String sql, int[] columnIndexes) throws SQLException {
		PreparedStatement pstatement = connection.prepareStatement( sql, columnIndexes );
		last_called = System.currentTimeMillis();
		return pstatement;
	}

	public PreparedStatement prepareStatement(String sql, String[] columnNames) throws SQLException {
		PreparedStatement pstatement = connection.prepareStatement( sql, columnNames);
		last_called = System.currentTimeMillis();
		return pstatement;
	}

	public Statement createStatement() throws SQLException {
		Statement statement = connection.createStatement();
		last_called = System.currentTimeMillis();
		return statement;
	}

	public Statement createStatement(int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
		Statement statement = connection.createStatement( resultSetType, resultSetConcurrency, resultSetHoldability );
		last_called = System.currentTimeMillis();
		return statement;
	}

	public Statement createStatement(int resultSetType, int resultSetConcurrency) throws SQLException {
		Statement statement = connection.createStatement( resultSetType, resultSetConcurrency );
		last_called = System.currentTimeMillis();
		return statement;
	}
	
	public String nativeSQL(String sql) throws SQLException {
		String nativeSql = connection.nativeSQL( sql );
		last_called = System.currentTimeMillis();
		return nativeSql;
	}

}

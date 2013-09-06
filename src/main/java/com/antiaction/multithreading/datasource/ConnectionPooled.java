/*
 * Pooled Connection wrapper.
 * Copyright (C) 2010, 2011, 2013  Nicholas Clarke
 *
 */

/*
 * History:
 *
 * 30-Aug-2010 : Initial implementation.
 * 05-Sep-2013 : Added logging.
 *
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
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConnectionPooled implements Connection {

    /** Logging mechanism. */
	private static Logger logger = Logger.getLogger( ConnectionPooled.class.getName() );

	/** Owning Connection Pool. */
	protected DataSourcePoolImpl dspi;

	/** Wrapped Connection object. */
	protected Connection connection;

	/** Has this wrapper been closed. */
	protected boolean bClosed = false;

	/** When wrapper was last called. */
	public long last_called = 0;

	/** Set of open <code>Statements</code>. */
	public Set open_statements = new TreeSet();

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

	public int closeOpenStatements() {
		int closed = 0;
		synchronized (open_statements) {
			if ( open_statements.size() > 0 ) {
				Object[] stmts = new Statement[ open_statements.size() ];
				stmts = open_statements.toArray( stmts );
				Statement stmt;
				for ( int i=0; i<stmts.length; ++i ) {
					stmt = (Statement)stmts[ i ];
					if ( stmt != null ) {
						try {
							stmt.close();
							++closed;
						}
						catch (SQLException e) {
							// debug
							System.out.println( "SQLException closing Statement." );
						}
					}
				}
			}
		}
		return closed;
	}

	public void closeStatement(Statement stm) {
		synchronized ( open_statements ) {
			open_statements.remove( stm );
		}
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
			synchronized ( open_statements ) {
				open_statements.clear();
				open_statements = null;
			}
		}
	}

	public void clearWarnings() throws SQLException {
		if ( connection == null ) {
			logger.log( Level.SEVERE, "Database connection closed!" );
			throw new SQLException( "Database connection closed!" );
		}
		connection.clearWarnings();
		last_called = System.currentTimeMillis();
	}

	public SQLWarning getWarnings() throws SQLException {
		if ( connection == null ) {
			logger.log( Level.SEVERE, "Database connection closed!" );
			throw new SQLException( "Database connection closed!" );
		}
		SQLWarning warning = connection.getWarnings();
		last_called = System.currentTimeMillis();
		return warning;
	}

	public boolean isReadOnly() throws SQLException {
		if ( connection == null ) {
			logger.log( Level.SEVERE, "Database connection closed!" );
			throw new SQLException( "Database connection closed!" );
		}
		boolean b = connection.isReadOnly();
		last_called = System.currentTimeMillis();
		return b;
	}

	public void setReadOnly(boolean readOnly) throws SQLException {
		if ( connection == null ) {
			logger.log( Level.SEVERE, "Database connection closed!" );
			throw new SQLException( "Database connection closed!" );
		}
		connection.setReadOnly( readOnly );
		last_called = System.currentTimeMillis();
	}

	public void setHoldability(int holdability) throws SQLException {
		if ( connection == null ) {
			logger.log( Level.SEVERE, "Database connection closed!" );
			throw new SQLException( "Database connection closed!" );
		}
		connection.setHoldability( holdability );
		last_called = System.currentTimeMillis();
	}

	public int getHoldability() throws SQLException {
		if ( connection == null ) {
			logger.log( Level.SEVERE, "Database connection closed!" );
			throw new SQLException( "Database connection closed!" );
		}
		int holdability = connection.getHoldability();
		last_called = System.currentTimeMillis();
		return holdability;
	}

	public void setTypeMap(Map map) throws SQLException {
		if ( connection == null ) {
			logger.log( Level.SEVERE, "Database connection closed!" );
			throw new SQLException( "Database connection closed!" );
		}
		connection.setTypeMap( map );
		last_called = System.currentTimeMillis();
	}

	public Map getTypeMap() throws SQLException {
		if ( connection == null ) {
			logger.log( Level.SEVERE, "Database connection closed!" );
			throw new SQLException( "Database connection closed!" );
		}
		Map map = connection.getTypeMap();
		last_called = System.currentTimeMillis();
		return map;
	}

	public void setCatalog(String catalog) throws SQLException {
		if ( connection == null ) {
			logger.log( Level.SEVERE, "Database connection closed!" );
			throw new SQLException( "Database connection closed!" );
		}
		connection.setCatalog( catalog );
		last_called = System.currentTimeMillis();
	}

	public String getCatalog() throws SQLException {
		if ( connection == null ) {
			logger.log( Level.SEVERE, "Database connection closed!" );
			throw new SQLException( "Database connection closed!" );
		}
		String catalog = connection.getCatalog();
		last_called = System.currentTimeMillis();
		return catalog;
	}

	public DatabaseMetaData getMetaData() throws SQLException {
		if ( connection == null ) {
			logger.log( Level.SEVERE, "Database connection closed!" );
			throw new SQLException( "Database connection closed!" );
		}
		DatabaseMetaData metadata = connection.getMetaData();
		last_called = System.currentTimeMillis();
		return metadata;
	}

	public boolean getAutoCommit() throws SQLException {
		if ( connection == null ) {
			logger.log( Level.SEVERE, "Database connection closed!" );
			throw new SQLException( "Database connection closed!" );
		}
		boolean b = connection.getAutoCommit();
		last_called = System.currentTimeMillis();
		return b;
	}

	public void setAutoCommit(boolean autoCommit) throws SQLException {
		if ( connection == null ) {
			logger.log( Level.SEVERE, "Database connection closed!" );
			throw new SQLException( "Database connection closed!" );
		}
		connection.setAutoCommit( autoCommit );
		last_called = System.currentTimeMillis();
	}

	public void commit() throws SQLException {
		if ( connection == null ) {
			logger.log( Level.SEVERE, "Database connection closed!" );
			throw new SQLException( "Database connection closed!" );
		}
		connection.commit();
		last_called = System.currentTimeMillis();
	}

	public void rollback() throws SQLException {
		if ( connection == null ) {
			logger.log( Level.SEVERE, "Database connection closed!" );
			throw new SQLException( "Database connection closed!" );
		}
		connection.rollback();
		last_called = System.currentTimeMillis();
	}

	public Savepoint setSavepoint() throws SQLException {
		if ( connection == null ) {
			logger.log( Level.SEVERE, "Database connection closed!" );
			throw new SQLException( "Database connection closed!" );
		}
		Savepoint savepoint = connection.setSavepoint();
		last_called = System.currentTimeMillis();
		return savepoint;
	}

	public Savepoint setSavepoint(String name) throws SQLException {
		if ( connection == null ) {
			logger.log( Level.SEVERE, "Database connection closed!" );
			throw new SQLException( "Database connection closed!" );
		}
		Savepoint savepoint = connection.setSavepoint( name );
		last_called = System.currentTimeMillis();
		return savepoint;
	}

	public void rollback(Savepoint savepoint) throws SQLException {
		if ( connection == null ) {
			logger.log( Level.SEVERE, "Database connection closed!" );
			throw new SQLException( "Database connection closed!" );
		}
		connection.rollback( savepoint );
		last_called = System.currentTimeMillis();
	}

	public void releaseSavepoint(Savepoint savepoint) throws SQLException {
		if ( connection == null ) {
			logger.log( Level.SEVERE, "Database connection closed!" );
			throw new SQLException( "Database connection closed!" );
		}
		connection.releaseSavepoint( savepoint );
		last_called = System.currentTimeMillis();
	}

	public void setTransactionIsolation(int level) throws SQLException {
		if ( connection == null ) {
			logger.log( Level.SEVERE, "Database connection closed!" );
			throw new SQLException( "Database connection closed!" );
		}
		connection.setTransactionIsolation( level );
		last_called = System.currentTimeMillis();
	}

	public int getTransactionIsolation() throws SQLException {
		if ( connection == null ) {
			logger.log( Level.SEVERE, "Database connection closed!" );
			throw new SQLException( "Database connection closed!" );
		}
		int isolation = connection.getTransactionIsolation();
		last_called = System.currentTimeMillis();
		return isolation;
	}

	/*
	 * Statements.
	 */

	public CallableStatement prepareCall(String sql) throws SQLException {
		if ( connection == null ) {
			logger.log( Level.SEVERE, "Database connection closed!" );
			throw new SQLException( "Database connection closed!" );
		}
		CallableStatement cstatement = connection.prepareCall( sql );
		last_called = System.currentTimeMillis();
		cstatement = new CallableStatementWrapper( cstatement, this );
		synchronized ( open_statements ) {
			open_statements.add( cstatement );
		}
		return cstatement;
	}

	public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
		if ( connection == null ) {
			logger.log( Level.SEVERE, "Database connection closed!" );
			throw new SQLException( "Database connection closed!" );
		}
		CallableStatement cstatement = connection.prepareCall( sql, resultSetType, resultSetConcurrency );
		last_called = System.currentTimeMillis();
		cstatement = new CallableStatementWrapper( cstatement, this );
		synchronized ( open_statements ) {
			open_statements.add( cstatement );
		}
		return cstatement;
	}

	public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
		if ( connection == null ) {
			logger.log( Level.SEVERE, "Database connection closed!" );
			throw new SQLException( "Database connection closed!" );
		}
		CallableStatement cstatement = connection.prepareCall( sql, resultSetType, resultSetConcurrency, resultSetHoldability );
		last_called = System.currentTimeMillis();
		cstatement = new CallableStatementWrapper( cstatement, this );
		synchronized ( open_statements ) {
			open_statements.add( cstatement );
		}
		return cstatement;
	}

	public PreparedStatement prepareStatement(String sql) throws SQLException {
		if ( connection == null ) {
			logger.log( Level.SEVERE, "Database connection closed!" );
			throw new SQLException( "Database connection closed!" );
		}
		PreparedStatement pstatement = connection.prepareStatement( sql );
		last_called = System.currentTimeMillis();
		pstatement = new PreparedStatementWrapper( pstatement, this );
		synchronized ( open_statements ) {
			open_statements.add( pstatement );
		}
		return pstatement;
	}

	public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
		if ( connection == null ) {
			logger.log( Level.SEVERE, "Database connection closed!" );
			throw new SQLException( "Database connection closed!" );
		}
		PreparedStatement pstatement = connection.prepareStatement( sql, resultSetType, resultSetConcurrency );
		last_called = System.currentTimeMillis();
		pstatement = new PreparedStatementWrapper( pstatement, this );
		synchronized ( open_statements ) {
			open_statements.add( pstatement );
		}
		return pstatement;
	}
		
	public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
		if ( connection == null ) {
			logger.log( Level.SEVERE, "Database connection closed!" );
			throw new SQLException( "Database connection closed!" );
		}
		PreparedStatement pstatement = connection.prepareStatement( sql, resultSetType, resultSetConcurrency, resultSetHoldability );
		last_called = System.currentTimeMillis();
		pstatement = new PreparedStatementWrapper( pstatement, this );
		synchronized ( open_statements ) {
			open_statements.add( pstatement );
		}
		return pstatement;
	}

	public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys) throws SQLException {
		if ( connection == null ) {
			logger.log( Level.SEVERE, "Database connection closed!" );
			throw new SQLException( "Database connection closed!" );
		}
		PreparedStatement pstatement = connection.prepareStatement( sql, autoGeneratedKeys );
		last_called = System.currentTimeMillis();
		pstatement = new PreparedStatementWrapper( pstatement, this );
		synchronized ( open_statements ) {
			open_statements.add( pstatement );
		}
		return pstatement;
	}

	public PreparedStatement prepareStatement(String sql, int[] columnIndexes) throws SQLException {
		if ( connection == null ) {
			logger.log( Level.SEVERE, "Database connection closed!" );
			throw new SQLException( "Database connection closed!" );
		}
		PreparedStatement pstatement = connection.prepareStatement( sql, columnIndexes );
		last_called = System.currentTimeMillis();
		pstatement = new PreparedStatementWrapper( pstatement, this );
		synchronized ( open_statements ) {
			open_statements.add( pstatement );
		}
		return pstatement;
	}

	public PreparedStatement prepareStatement(String sql, String[] columnNames) throws SQLException {
		if ( connection == null ) {
			logger.log( Level.SEVERE, "Database connection closed!" );
			throw new SQLException( "Database connection closed!" );
		}
		PreparedStatement pstatement = connection.prepareStatement( sql, columnNames);
		last_called = System.currentTimeMillis();
		pstatement = new PreparedStatementWrapper( pstatement, this );
		synchronized ( open_statements ) {
			open_statements.add( pstatement );
		}
		return pstatement;
	}

	public Statement createStatement() throws SQLException {
		if ( connection == null ) {
			logger.log( Level.SEVERE, "Database connection closed!" );
			throw new SQLException( "Database connection closed!" );
		}
		Statement statement = connection.createStatement();
		last_called = System.currentTimeMillis();
		statement = new StatementWrapper( statement, this );
		synchronized ( open_statements ) {
			open_statements.add( statement );
		}
		return statement;
	}

	public Statement createStatement(int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
		if ( connection == null ) {
			logger.log( Level.SEVERE, "Database connection closed!" );
			throw new SQLException( "Database connection closed!" );
		}
		Statement statement = connection.createStatement( resultSetType, resultSetConcurrency, resultSetHoldability );
		last_called = System.currentTimeMillis();
		statement = new StatementWrapper( statement, this );
		synchronized ( open_statements ) {
			open_statements.add( statement );
		}
		return statement;
	}

	public Statement createStatement(int resultSetType, int resultSetConcurrency) throws SQLException {
		if ( connection == null ) {
			logger.log( Level.SEVERE, "Database connection closed!" );
			throw new SQLException( "Database connection closed!" );
		}
		Statement statement = connection.createStatement( resultSetType, resultSetConcurrency );
		last_called = System.currentTimeMillis();
		statement = new StatementWrapper( statement, this );
		synchronized ( open_statements ) {
			open_statements.add( statement );
		}
		return statement;
	}
	
	public String nativeSQL(String sql) throws SQLException {
		if ( connection == null ) {
			logger.log( Level.SEVERE, "Database connection closed!" );
			throw new SQLException( "Database connection closed!" );
		}
		String nativeSql = connection.nativeSQL( sql );
		last_called = System.currentTimeMillis();
		return nativeSql;
	}

}

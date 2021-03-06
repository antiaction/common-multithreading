/*
 * JDBC DataSource implementation.
 * Copyright (C) 2004, 2007, 2010, 2011, 2013  Nicholas Clarke
 *
 */

/*
 * History:
 *
 * 09-Aug-2004 : Simple DataSource implementation.
 * 01-Jul-2007 : Username/password properties used to get connection if specified.
 * 29-Aug-2010 : Started on an implementation using a connection pool.
 * 05-Sep-2010 : Implemented the release method.
 * 06-Sep-2010 : Added check pool for closed connections and various other enhancements.
 * 20-Jun-2011 : Added a query to check if the connection is alive.
 * 27-Jun-2011 : Refactored init and commented the variables.
 * 10-Sep-2011 : Forgot to stm.close(). Hope this fixes my sudden memory leak.
 * 05-Sep-2013 : Added logging.
 *
 */

package com.antiaction.multithreading.datasource;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.sql.DataSource;

import com.antiaction.multithreading.resourcemanage.IResourcePool;
import com.antiaction.multithreading.resourcemanage.ResourceManager;

/**
 * JDBC DataSource Connection Pool implementation.
 *
 * <display-name>
 * <jndi-name>
 * <driver-class>
 * <connection-url>
 * <user-name>
 * <password>
 *
 * @version 1.00
 * @author Nicholas Clarke <mayhem[at]antiaction[dot]com>
 */
public class DataSourcePoolImpl implements DataSource, IResourcePool {

    /** Logging mechanism. */
	private static Logger logger = Logger.getLogger( DataSourcePoolImpl.class.getName() );

	/*
	 * Thread.
	 */

	/** Shutdown boolean. */
	protected boolean exit = false;

	/** Has thread been started. */
	protected boolean running = false;

	/** Resource manager monitoring instance. */
	protected ResourceManager resourceManager;

	/** Resource manager monitoring thread. */
	protected Thread resourceManagerThread;

	/*
	 * DataSource configuration.
	 */

	/** Connection timeout. */
	protected int timeout = 0;

	/** <code>DataSource</code> log writer. */
	protected PrintWriter log_writer = null;

	protected Map props;

	/** Display name. */
	protected String ds_name;

	/** Connection Url. */
	protected String ds_url;

	/** Connection username. */
	protected String ds_username;

	/** Connection password. */
	protected String ds_password;

	/** Minimum resources allocated. */
	protected int min;

	/** Minimum idle resources allocated. */
	protected int minIdle;

	/** Maximum resources allocated. */
	protected int max;

	/** Connection alive Sql. */
	protected String ds_validation_query;

	/*
	 * Internal state.
	 */

	protected SQLException lastOpenException;

	protected int allocated = 0;

	protected int idle = 0;

	protected List idleList;

	protected Set busySet;

	protected DataSourcePoolImpl() {
		resourceManager = new ResourceManager( this );
		idleList = new ArrayList( 16 );
		busySet = new HashSet( 16 );
	}

	public static DataSourcePoolImpl getDataSource(Map config, Map props) {
		DataSourcePoolImpl dataSource = null;

		Class driverClass = null;
		Object driverObject = null;

		String driverClassName = (String)config.get( "driver-class" );
		if ( driverClassName != null && driverClassName.length() > 0 ) {
			try {
				driverClass = Class.forName( driverClassName );
				driverObject = driverClass.newInstance();
			}
			catch (ClassNotFoundException e) {
				logger.log( Level.SEVERE, "Error: could not find jdbc driver.", e );
			}
			catch (InstantiationException e) {
				logger.log( Level.SEVERE, "Error: could not instantiate jdbc driver.", e );
			}
			catch (IllegalAccessException e) {
				logger.log( Level.SEVERE, "Error: could not access jdbc driver.", e );
			}
		}
		else {
			logger.log( Level.WARNING, "JDBC class not supplied." );
		}

		if ( driverObject != null ) {
			String connectionUrl = (String)config.get( "connection-url" );
			if ( connectionUrl != null && connectionUrl.length() > 0 ) {
				String displayName = (String)config.get( "display-name" );
				String userName = (String)config.get( "user-name" );
				String password = (String)config.get( "password" );
				String validation_query = (String)config.get( "validation-query" );
				if ( validation_query == null ) {
					validation_query = "SELECT 1";
				}
				else if ( validation_query.length() == 0 ) {
					validation_query = null;
				}
				dataSource = new DataSourcePoolImpl();
				dataSource.props = (Map)((HashMap)config).clone();
				dataSource.ds_name = displayName;
				dataSource.ds_url = connectionUrl;
				dataSource.ds_username = userName;
				dataSource.ds_password = password;
				dataSource.min = getMapInt( config, "min", 4 );
				dataSource.minIdle = getMapInt( config, "min-idle", 4 );
				dataSource.max = getMapInt( config, "max", 16 );
				dataSource.ds_validation_query = validation_query;
				dataSource.start();
			}
			else {
				logger.log( Level.SEVERE, "Error: invalid jbdc url." );
			}
		}

		return dataSource;
	}

	private static int getMapInt(Map map, String name, int defaultValue) {
		int value = defaultValue;
		if ( map != null && name != null && name.length() > 0 && map.containsKey( name ) ) {
			try {
				value = Integer.parseInt( (String)map.get( name ) );
			}
			catch (NumberFormatException e) {
			}
		}
		return value;
	}

	public boolean start() {
		if ( !running ) {
			resourceManager.setMin( min );
			resourceManager.setThreshold( minIdle );
			resourceManager.setMax( max );

			resourceManagerThread = new Thread( resourceManager );
			resourceManagerThread.start();

			exit = false;
			running = true;

			System.out.println( "Connection pool initialized." );
			System.out.println( " min: " + resourceManager.getMin() + " - threshold: " + resourceManager.getThreshold() + " - max: " + resourceManager.getMax() );
		}

		return running;
	}

	public void stop() {
		if ( running ) {
			exit = true;
			running = false;
			resourceManager.stop();
			//resourceManager = null;
			resourceManagerThread = null;
			log_writer = null;
		}
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
		Connection conn = null;
		if ( running ) {
			Properties connprops = new Properties();
			if ( username != null && username.length() > 0 ) {
				connprops.setProperty( "user", username );
			}
			if ( password != null && password.length() > 0 ) {
				connprops.setProperty( "password", password );
			}
			conn = DriverManager.getConnection( ds_url, connprops );
		}
		return conn;
	}

	public Connection getConnection() throws SQLException {
		Connection conn = null;
		if ( running ) {
			synchronized ( this ) {
				if ( idle > 0 ) {
					conn = (Connection)idleList.remove( idleList.size() - 1 );
					conn = ConnectionPooled.getInstance( this, conn );
					if ( check_connection_open( conn ) ) {
						--idle;
						busySet.add( conn );
						resourceManager.update( allocated, idle );
					}
					else {
						conn = null;
					}
				}
			}
			if ( conn == null ) {
				conn = openConnection();
				if ( conn != null ) {
					synchronized ( this ) {
						++allocated;
						conn = ConnectionPooled.getInstance( this, conn );
						busySet.add( conn );
						resourceManager.update( allocated, idle );
					}
				}
			}
		}
		return conn;
	}

	public void allocate(int n) {
		Connection conn;
		for ( int i=0; i<n; ++i ) {
			conn = openConnection();
			if ( conn != null ) {
				synchronized ( this ) {
					++allocated;
					++idle;
					idleList.add( conn );
					resourceManager.update( allocated, idle );
				}
			}
			else {
				return;
			}
		}
	}

	public void release(int n) {
		Connection conn = null;
		while ( n > 0 ) {
			synchronized ( this ) {
				if ( idle > 0 ) {
					--idle;
					--allocated;
					conn = (Connection)idleList.remove( 0 );
				}
				else {
					n = 0;
				}
			}
			if ( conn != null ) {
				try {
					conn.close();
				}
				catch (SQLException e) {
					// Suppress sql exception.
				}
				conn = null;
			}
		}
	}

	public void check_pool() {
		List checkList;
		Connection conn;
		synchronized ( this ) {
			checkList = new ArrayList( idleList );
		}
		int i = 0;
		while ( i < checkList.size() ) {
			conn = (Connection)checkList.get( i );
			check_connection_open( conn );
			++i;
		}
		resourceManager.update( allocated, idle );
	}

	protected boolean check_connection_open(Connection conn) {
		boolean bClosed = true;
		Statement stm = null;
		ResultSet rs = null;
		try {
			bClosed = conn.isClosed();
			if ( !bClosed && ds_validation_query != null && ds_validation_query.length() > 0 ) {
				stm = conn.createStatement();
				rs = stm.executeQuery( ds_validation_query );
			}
		}
		catch (SQLException e) {
			// Suppress no route to host.
			bClosed = true;
		}
		finally {
			if ( rs != null ) {
				try {
					rs.close();
				}
				catch (SQLException e) {
				}
				rs = null;
			}
			if ( stm != null ) {
				try {
					stm.close();
				}
				catch (SQLException e) {
				}
				stm = null;
			}
		}
		if ( bClosed ) {
			synchronized ( this ) {
				if ( idleList.contains( conn ) ) {
					--idle;
					--allocated;
					idleList.remove( conn );
				}
			}
			logger.log( Level.INFO, "Pooled connection closed unexpectedly: " + conn );
		}
		return !bClosed;
	}

	protected Connection openConnection() {
		Connection conn = null;
		Properties connprops = new Properties();
		if ( ds_username != null && ds_username.length() > 0 ) {
			connprops.setProperty( "user", ds_username );
		}
		if ( ds_password != null && ds_password.length() > 0 ) {
			connprops.setProperty( "password", ds_password );
		}
		try {
			long dt = System.currentTimeMillis();
			conn = DriverManager.getConnection( ds_url, connprops );
			lastOpenException = null;
			dt = System.currentTimeMillis() - dt;
			logger.log( Level.INFO, "Database connect time: " + dt + " ms." );
		}
		catch (SQLException e) {
			// Suppress no route to host.
			if ( lastOpenException == null ) {
				lastOpenException = e;
				logger.log( Level.SEVERE, e.toString(), e );
			}
		}
		return conn;
	}

	protected void closeConnection(ConnectionPooled conn) {
		synchronized ( this ) {
			busySet.remove( conn );
			Connection orgConn = conn.getOriginalConnection();
			// Close any open statements.
			int openStatements = conn.closeOpenStatements();
			if (openStatements > 0 ) {
				logger.log( Level.WARNING, "Statement(s) not closed: "  + openStatements );
			}
			// Check for open connection.
			boolean bIsClosed = true;
			try {
				bIsClosed = orgConn.isClosed();
			}
			catch (SQLException e) {
				// Suppress sql exception.
			}
			// Insert connection in idle list if it's still open.
			if ( !bIsClosed ) {
				++idle;
				idleList.add( orgConn );
			}
			else {
				--allocated;
			}
		}
	}

	/*
	 * JDK6
	 */

	public <T> T unwrap(Class<T> iface) throws SQLException {
		return null;
	}

	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		return false;
	}

}

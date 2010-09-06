/*
 * JDBC DataSource implementation.
 * Copyright (C) 2004, 2007, 2010  Nicholas Clarke
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
 *
 */

package com.antiaction.multithreading.datasource;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

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

	/** Shutdown boolean. */
	protected boolean exit = false;

	/** Has thread been started. */
	protected boolean running = false;

	/** Resource manager monitoring instance. */
	protected ResourceManager resourceManager;

	/** Resource manager monitoring thread. */
	protected Thread resourceManagerThread;

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

	protected int allocated = 0;

	protected int idle = 0;

	protected List idleList;

	protected Set busySet;

	protected DataSourcePoolImpl() {
		resourceManager = new ResourceManager( this );
		resourceManager.setMin( 4 );
		resourceManager.setThreshold( 4 );
		resourceManager.setMax( 16 );
		idleList = new ArrayList( 16 );
		busySet = new HashSet( 16 );
	}

	public static DataSource getDataSource(Map config, Map props) {
		DataSourcePoolImpl dataSource = null;

		String driverClassName = (String)config.get( "driver-class" );
		if ( driverClassName != null && driverClassName.length() > 0 ) {
			Class driverClass = null;
			Object driverObject = null;
			try {
				driverClass = Class.forName( driverClassName );
				driverObject = driverClass.newInstance();
			}
			catch (ClassNotFoundException e) {
				System.out.println( "Error: could not find jdbc driver." );
				e.printStackTrace();
			}
			catch (InstantiationException e) {
				System.out.println( "Error: could not instantiate jdbc driver." );
				e.printStackTrace();
			}
			catch (IllegalAccessException e) {
				System.out.println( "Error: could not access jdbc driver." );
				e.printStackTrace();
			}

			if ( driverObject != null ) {
				String connectionUrl = (String)config.get( "connection-url" );
				if ( connectionUrl != null && connectionUrl.length() > 0 ) {
					String displayName = (String)config.get( "display-name" );
					String userName = (String)config.get( "user-name" );
					String password = (String)config.get( "password" );

					dataSource = new DataSourcePoolImpl();
					dataSource.props = (Map)((HashMap)config).clone();
					dataSource.ds_name = displayName;
					dataSource.ds_url = connectionUrl;
					dataSource.ds_username = userName;
					dataSource.ds_password = password;

					dataSource.start();
				}
				else {
					System.out.println( "Error: invalid jbdc url." );
				}
			}
		}
		else {
			System.out.println( "Error: invalid jdbc class." );
		}

		return dataSource;
	}

	public boolean start() {
		if ( !running ) {
			resourceManagerThread = new Thread( resourceManager );
			resourceManagerThread.start();

			running = true;

			System.out.println( "Connection pool initialized." );
			System.out.println( " min: " + resourceManager.getMin() + " - threshold: " + resourceManager.getThreshold() + " - max: " + resourceManager.getMax() );
		}

		return running;
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
		Connection conn;
		Properties connprops = new Properties();
		if ( username != null && username.length() > 0 ) {
			connprops.setProperty( "user", username );
		}
		if ( password != null && password.length() > 0 ) {
			connprops.setProperty( "password", password );
		}

		conn = DriverManager.getConnection( ds_url, connprops );

		return conn;
	}

	public Connection getConnection() throws SQLException {
		Connection conn = null;
		synchronized ( this ) {
			if ( idle > 0 ) {
				--idle;
				conn = (Connection)idleList.remove( idleList.size() - 1 );
				conn = ConnectionPooled.getInstance( this, conn );
				busySet.add( conn );
				resourceManager.update( allocated, idle );
			}
		}
		if ( conn == null ) {
			conn = openConnection();
			synchronized ( this ) {
				++allocated;
				conn = ConnectionPooled.getInstance( this, conn );
				busySet.add( conn );
				resourceManager.update( allocated, idle );
			}
		}
		return conn;
	}

	public void allocate(int n) {
		Connection conn;
		for ( int i=0; i<n; ++i ) {
			conn = openConnection();
			synchronized ( this ) {
				++allocated;
				++idle;
				idleList.add( conn );
				resourceManager.update( allocated, idle );
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
			try {
				if ( conn.isClosed() ) {
					synchronized ( this ) {
						if ( idleList.contains( conn ) ) {
							--idle;
							--allocated;
							idleList.remove( conn );
						}
					}

					// debug
					System.out.println( "Closed connection: " + conn );
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			++i;
		}
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
			dt = System.currentTimeMillis() - dt;
			// debug
			System.out.println( "Database connect time: " + dt + " ms." );
		}
		catch (SQLException e) {
			e.printStackTrace();
		}

		return conn;
	}

	protected void closeConnection(ConnectionPooled conn) {
		synchronized ( this ) {
			busySet.remove( conn );
			Connection orgConn = conn.getOriginalConnection();
			try {
				if ( !orgConn.isClosed() ) {
					++idle;
					idleList.add( orgConn );
				}
				else {
					--allocated;
				}
			}
			catch (SQLException e) {
				--allocated;
				e.printStackTrace();
			}
		}
	}

}

/*
 * CallableStatement wrapper.
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

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Date;
import java.sql.Ref;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CallableStatementWrapper extends PreparedStatementWrapper implements CallableStatement {

    /** Logging mechanism. */
	private static Logger logger = Logger.getLogger( CallableStatementWrapper.class.getName() );

	private CallableStatement stm;

	public CallableStatementWrapper(CallableStatement stm, ConnectionPooled connection) {
		super( stm, connection );
		this.stm = stm;
	}

	/*
	 * Register.
	 */

	public void registerOutParameter(int parameterIndex, int sqlType) throws SQLException {
		if ( stm == null ) {
			logger.log( Level.SEVERE, "Database statement closed!" );
			throw new SQLException( "Database statement closed!" );
		}
		stm.registerOutParameter( parameterIndex, sqlType );
	}

	public void registerOutParameter(int parameterIndex, int sqlType, int scale) throws SQLException {
		if ( stm == null ) {
			logger.log( Level.SEVERE, "Database statement closed!" );
			throw new SQLException( "Database statement closed!" );
		}
		stm.registerOutParameter( parameterIndex, sqlType, scale );
	}

	public void registerOutParameter(int paramIndex, int sqlType, String typeName) throws SQLException {
		if ( stm == null ) {
			logger.log( Level.SEVERE, "Database statement closed!" );
			throw new SQLException( "Database statement closed!" );
		}
		stm.registerOutParameter( paramIndex, sqlType, typeName );
	}

	public void registerOutParameter(String parameterName, int sqlType) throws SQLException {
		if ( stm == null ) {
			logger.log( Level.SEVERE, "Database statement closed!" );
			throw new SQLException( "Database statement closed!" );
		}
		stm.registerOutParameter( parameterName, sqlType );
	}

	public void registerOutParameter(String parameterName, int sqlType, int scale) throws SQLException {
		if ( stm == null ) {
			logger.log( Level.SEVERE, "Database statement closed!" );
			throw new SQLException( "Database statement closed!" );
		}
		stm.registerOutParameter( parameterName, sqlType, scale );
	}

	public void registerOutParameter(String parameterName, int sqlType, String typeName) throws SQLException {
		if ( stm == null ) {
			logger.log( Level.SEVERE, "Database statement closed!" );
			throw new SQLException( "Database statement closed!" );
		}
		stm.registerOutParameter( parameterName, sqlType, typeName );
	}

	/*
	 * Parameters.
	 */

	public boolean wasNull() throws SQLException {
		if ( stm == null ) {
			logger.log( Level.SEVERE, "Database statement closed!" );
			throw new SQLException( "Database statement closed!" );
		}
		return stm.wasNull();
	}

	public Array getArray(int i) throws SQLException {
		if ( stm == null ) {
			logger.log( Level.SEVERE, "Database statement closed!" );
			throw new SQLException( "Database statement closed!" );
		}
		return stm.getArray( i );
	}

	public Array getArray(String parameterName) throws SQLException {
		if ( stm == null ) {
			logger.log( Level.SEVERE, "Database statement closed!" );
			throw new SQLException( "Database statement closed!" );
		}
		return stm.getArray( parameterName );
	}

	public void setAsciiStream(String parameterName, InputStream x, int length) throws SQLException {
		if ( stm == null ) {
			logger.log( Level.SEVERE, "Database statement closed!" );
			throw new SQLException( "Database statement closed!" );
		}
		stm.setAsciiStream( parameterName, x, length );
	}

	public boolean getBoolean(int parameterIndex) throws SQLException {
		if ( stm == null ) {
			logger.log( Level.SEVERE, "Database statement closed!" );
			throw new SQLException( "Database statement closed!" );
		}
		return stm.getBoolean( parameterIndex );
	}

	public boolean getBoolean(String parameterName) throws SQLException {
		if ( stm == null ) {
			logger.log( Level.SEVERE, "Database statement closed!" );
			throw new SQLException( "Database statement closed!" );
		}
		return stm.getBoolean( parameterName );
	}

	public BigDecimal getBigDecimal(int parameterIndex, int scale) throws SQLException {
		if ( stm == null ) {
			logger.log( Level.SEVERE, "Database statement closed!" );
			throw new SQLException( "Database statement closed!" );
		}
		return stm.getBigDecimal( parameterIndex, scale );
	}

	public BigDecimal getBigDecimal(int parameterIndex) throws SQLException {
		if ( stm == null ) {
			logger.log( Level.SEVERE, "Database statement closed!" );
			throw new SQLException( "Database statement closed!" );
		}
		return stm.getBigDecimal( parameterIndex );
	}

	public void setBigDecimal(String parameterName, BigDecimal x) throws SQLException {
		if ( stm == null ) {
			logger.log( Level.SEVERE, "Database statement closed!" );
			throw new SQLException( "Database statement closed!" );
		}
		stm.setBigDecimal( parameterName, x );
	}

	public BigDecimal getBigDecimal(String parameterName) throws SQLException {
		if ( stm == null ) {
			logger.log( Level.SEVERE, "Database statement closed!" );
			throw new SQLException( "Database statement closed!" );
		}
		return stm.getBigDecimal( parameterName );
	}

	public void setBinaryStream(String parameterName, InputStream x, int length) throws SQLException {
		if ( stm == null ) {
			logger.log( Level.SEVERE, "Database statement closed!" );
			throw new SQLException( "Database statement closed!" );
		}
		stm.setBinaryStream( parameterName, x, length );
	}

	public Blob getBlob(int i) throws SQLException {
		if ( stm == null ) {
			logger.log( Level.SEVERE, "Database statement closed!" );
			throw new SQLException( "Database statement closed!" );
		}
		return stm.getBlob( i );
	}

	public Blob getBlob(String parameterName) throws SQLException {
		if ( stm == null ) {
			logger.log( Level.SEVERE, "Database statement closed!" );
			throw new SQLException( "Database statement closed!" );
		}
		return stm.getBlob( parameterName );
	}

	public void setBoolean(String parameterName, boolean x) throws SQLException {
		if ( stm == null ) {
			logger.log( Level.SEVERE, "Database statement closed!" );
			throw new SQLException( "Database statement closed!" );
		}
		stm.setBoolean( parameterName, x );
	}

	public byte getByte(int parameterIndex) throws SQLException {
		if ( stm == null ) {
			logger.log( Level.SEVERE, "Database statement closed!" );
			throw new SQLException( "Database statement closed!" );
		}
		return stm.getByte( parameterIndex );
	}

	public void setByte(String parameterName, byte x) throws SQLException {
		if ( stm == null ) {
			logger.log( Level.SEVERE, "Database statement closed!" );
			throw new SQLException( "Database statement closed!" );
		}
		stm.setByte( parameterName, x );
	}

	public byte getByte(String parameterName) throws SQLException {
		if ( stm == null ) {
			logger.log( Level.SEVERE, "Database statement closed!" );
			throw new SQLException( "Database statement closed!" );
		}
		return stm.getByte( parameterName );
	}

	public byte[] getBytes(int parameterIndex) throws SQLException {
		if ( stm == null ) {
			logger.log( Level.SEVERE, "Database statement closed!" );
			throw new SQLException( "Database statement closed!" );
		}
		return stm.getBytes( parameterIndex );
	}

	public void setBytes(String parameterName, byte[] x) throws SQLException {
		if ( stm == null ) {
			logger.log( Level.SEVERE, "Database statement closed!" );
			throw new SQLException( "Database statement closed!" );
		}
		stm.setBytes( parameterName, x );
	}

	public byte[] getBytes(String parameterName) throws SQLException {
		if ( stm == null ) {
			logger.log( Level.SEVERE, "Database statement closed!" );
			throw new SQLException( "Database statement closed!" );
		}
		return stm.getBytes( parameterName );
	}

	public void setCharacterStream(String parameterName, Reader reader, int length) throws SQLException {
		if ( stm == null ) {
			logger.log( Level.SEVERE, "Database statement closed!" );
			throw new SQLException( "Database statement closed!" );
		}
		stm.setCharacterStream( parameterName, reader, length );
	}

	public Clob getClob(int i) throws SQLException {
		if ( stm == null ) {
			logger.log( Level.SEVERE, "Database statement closed!" );
			throw new SQLException( "Database statement closed!" );
		}
		return stm.getClob( i );
	}

	public Clob getClob(String parameterName) throws SQLException {
		if ( stm == null ) {
			logger.log( Level.SEVERE, "Database statement closed!" );
			throw new SQLException( "Database statement closed!" );
		}
		return stm.getClob( parameterName );
	}

	public Date getDate(int parameterIndex) throws SQLException {
		if ( stm == null ) {
			logger.log( Level.SEVERE, "Database statement closed!" );
			throw new SQLException( "Database statement closed!" );
		}
		return stm.getDate( parameterIndex );
	}

	public Date getDate(int parameterIndex, Calendar cal) throws SQLException {
		if ( stm == null ) {
			logger.log( Level.SEVERE, "Database statement closed!" );
			throw new SQLException( "Database statement closed!" );
		}
		return stm.getDate( parameterIndex, cal );
	}

	public void setDate(String parameterName, Date x) throws SQLException {
		if ( stm == null ) {
			logger.log( Level.SEVERE, "Database statement closed!" );
			throw new SQLException( "Database statement closed!" );
		}
		stm.setDate( parameterName, x );
	}

	public void setDate(String parameterName, Date x, Calendar cal) throws SQLException {
		if ( stm == null ) {
			logger.log( Level.SEVERE, "Database statement closed!" );
			throw new SQLException( "Database statement closed!" );
		}
		stm.setDate( parameterName, x, cal );
	}

	public Date getDate(String parameterName) throws SQLException {
		if ( stm == null ) {
			logger.log( Level.SEVERE, "Database statement closed!" );
			throw new SQLException( "Database statement closed!" );
		}
		return stm.getDate( parameterName );
	}

	public Date getDate(String parameterName, Calendar cal) throws SQLException {
		if ( stm == null ) {
			logger.log( Level.SEVERE, "Database statement closed!" );
			throw new SQLException( "Database statement closed!" );
		}
		return stm.getDate( parameterName, cal );
	}

	public double getDouble(int parameterIndex) throws SQLException {
		if ( stm == null ) {
			logger.log( Level.SEVERE, "Database statement closed!" );
			throw new SQLException( "Database statement closed!" );
		}
		return stm.getDouble( parameterIndex );
	}

	public void setDouble(String parameterName, double x) throws SQLException {
		if ( stm == null ) {
			logger.log( Level.SEVERE, "Database statement closed!" );
			throw new SQLException( "Database statement closed!" );
		}
		stm.setDouble( parameterName, x );
	}

	public double getDouble(String parameterName) throws SQLException {
		if ( stm == null ) {
			logger.log( Level.SEVERE, "Database statement closed!" );
			throw new SQLException( "Database statement closed!" );
		}
		return stm.getDouble( parameterName );
	}

	public float getFloat(int parameterIndex) throws SQLException {
		if ( stm == null ) {
			logger.log( Level.SEVERE, "Database statement closed!" );
			throw new SQLException( "Database statement closed!" );
		}
		return stm.getFloat( parameterIndex );
	}

	public void setFloat(String parameterName, float x) throws SQLException {
		if ( stm == null ) {
			logger.log( Level.SEVERE, "Database statement closed!" );
			throw new SQLException( "Database statement closed!" );
		}
		stm.setFloat( parameterName, x );
	}

	public float getFloat(String parameterName) throws SQLException {
		if ( stm == null ) {
			logger.log( Level.SEVERE, "Database statement closed!" );
			throw new SQLException( "Database statement closed!" );
		}
		return stm.getFloat( parameterName );
	}

	public int getInt(int parameterIndex) throws SQLException {
		if ( stm == null ) {
			logger.log( Level.SEVERE, "Database statement closed!" );
			throw new SQLException( "Database statement closed!" );
		}
		return stm.getInt( parameterIndex );
	}

	public void setInt(String parameterName, int x) throws SQLException {
		if ( stm == null ) {
			logger.log( Level.SEVERE, "Database statement closed!" );
			throw new SQLException( "Database statement closed!" );
		}
		stm.setInt( parameterName, x );
	}

	public int getInt(String parameterName) throws SQLException {
		if ( stm == null ) {
			logger.log( Level.SEVERE, "Database statement closed!" );
			throw new SQLException( "Database statement closed!" );
		}
		return stm.getInt( parameterName );
	}

	public void setLong(String parameterName, long x) throws SQLException {
		if ( stm == null ) {
			logger.log( Level.SEVERE, "Database statement closed!" );
			throw new SQLException( "Database statement closed!" );
		}
		stm.setLong( parameterName, x );
	}

	public long getLong(int parameterIndex) throws SQLException {
		if ( stm == null ) {
			logger.log( Level.SEVERE, "Database statement closed!" );
			throw new SQLException( "Database statement closed!" );
		}
		return stm.getLong( parameterIndex );
	}

	public long getLong(String parameterName) throws SQLException {
		if ( stm == null ) {
			logger.log( Level.SEVERE, "Database statement closed!" );
			throw new SQLException( "Database statement closed!" );
		}
		return stm.getLong( parameterName );
	}

	public void setNull(String parameterName, int sqlType) throws SQLException {
		if ( stm == null ) {
			logger.log( Level.SEVERE, "Database statement closed!" );
			throw new SQLException( "Database statement closed!" );
		}
		stm.setNull( parameterName, sqlType );
	}

	public void setNull(String parameterName, int sqlType, String typeName) throws SQLException {
		if ( stm == null ) {
			logger.log( Level.SEVERE, "Database statement closed!" );
			throw new SQLException( "Database statement closed!" );
		}
		stm.setNull( parameterName, sqlType, typeName );
	}

	public Object getObject(int parameterIndex) throws SQLException {
		if ( stm == null ) {
			logger.log( Level.SEVERE, "Database statement closed!" );
			throw new SQLException( "Database statement closed!" );
		}
		return stm.getObject( parameterIndex );
	}

	public Object getObject(int i, Map map) throws SQLException {
		if ( stm == null ) {
			logger.log( Level.SEVERE, "Database statement closed!" );
			throw new SQLException( "Database statement closed!" );
		}
		return stm.getObject( i, map );
	}

	public void setObject(String parameterName, Object x, int targetSqlType, int scale) throws SQLException {
		if ( stm == null ) {
			logger.log( Level.SEVERE, "Database statement closed!" );
			throw new SQLException( "Database statement closed!" );
		}
		stm.setObject( parameterName, x, targetSqlType, scale );
	}

	public void setObject(String parameterName, Object x, int targetSqlType) throws SQLException {
		if ( stm == null ) {
			logger.log( Level.SEVERE, "Database statement closed!" );
			throw new SQLException( "Database statement closed!" );
		}
		stm.setObject( parameterName, x, targetSqlType );
	}

	public void setObject(String parameterName, Object x) throws SQLException {
		if ( stm == null ) {
			logger.log( Level.SEVERE, "Database statement closed!" );
			throw new SQLException( "Database statement closed!" );
		}
		stm.setObject( parameterName, x );
	}

	public Object getObject(String parameterName) throws SQLException {
		if ( stm == null ) {
			logger.log( Level.SEVERE, "Database statement closed!" );
			throw new SQLException( "Database statement closed!" );
		}
		return stm.getObject( parameterName );
	}

	public Object getObject(String parameterName, Map map) throws SQLException {
		if ( stm == null ) {
			logger.log( Level.SEVERE, "Database statement closed!" );
			throw new SQLException( "Database statement closed!" );
		}
		return stm.getObject( parameterName, map );
	}

	public Ref getRef(int i) throws SQLException {
		if ( stm == null ) {
			logger.log( Level.SEVERE, "Database statement closed!" );
			throw new SQLException( "Database statement closed!" );
		}
		return stm.getRef( i );
	}

	public Ref getRef(String parameterName) throws SQLException {
		if ( stm == null ) {
			logger.log( Level.SEVERE, "Database statement closed!" );
			throw new SQLException( "Database statement closed!" );
		}
		return stm.getRef( parameterName );
	}

	public short getShort(int parameterIndex) throws SQLException {
		if ( stm == null ) {
			logger.log( Level.SEVERE, "Database statement closed!" );
			throw new SQLException( "Database statement closed!" );
		}
		return stm.getShort( parameterIndex );
	}

	public void setShort(String parameterName, short x) throws SQLException {
		if ( stm == null ) {
			logger.log( Level.SEVERE, "Database statement closed!" );
			throw new SQLException( "Database statement closed!" );
		}
		stm.setShort( parameterName, x );
	}

	public short getShort(String parameterName) throws SQLException {
		if ( stm == null ) {
			logger.log( Level.SEVERE, "Database statement closed!" );
			throw new SQLException( "Database statement closed!" );
		}
		return stm.getShort( parameterName );
	}

	public String getString(int parameterIndex) throws SQLException {
		if ( stm == null ) {
			logger.log( Level.SEVERE, "Database statement closed!" );
			throw new SQLException( "Database statement closed!" );
		}
		return stm.getString( parameterIndex );
	}

	public void setString(String parameterName, String x) throws SQLException {
		if ( stm == null ) {
			logger.log( Level.SEVERE, "Database statement closed!" );
			throw new SQLException( "Database statement closed!" );
		}
		stm.setString( parameterName, x );
	}

	public String getString(String parameterName) throws SQLException {
		if ( stm == null ) {
			logger.log( Level.SEVERE, "Database statement closed!" );
			throw new SQLException( "Database statement closed!" );
		}
		return stm.getString( parameterName );
	}

	public Time getTime(int parameterIndex) throws SQLException {
		if ( stm == null ) {
			logger.log( Level.SEVERE, "Database statement closed!" );
			throw new SQLException( "Database statement closed!" );
		}
		return stm.getTime( parameterIndex );
	}

	public Time getTime(int parameterIndex, Calendar cal) throws SQLException {
		if ( stm == null ) {
			logger.log( Level.SEVERE, "Database statement closed!" );
			throw new SQLException( "Database statement closed!" );
		}
		return stm.getTime( parameterIndex, cal );
	}

	public void setTime(String parameterName, Time x) throws SQLException {
		if ( stm == null ) {
			logger.log( Level.SEVERE, "Database statement closed!" );
			throw new SQLException( "Database statement closed!" );
		}
		stm.setTime( parameterName, x );
	}

	public void setTime(String parameterName, Time x, Calendar cal) throws SQLException {
		if ( stm == null ) {
			logger.log( Level.SEVERE, "Database statement closed!" );
			throw new SQLException( "Database statement closed!" );
		}
		stm.setTime( parameterName, x, cal );
	}

	public Time getTime(String parameterName) throws SQLException {
		if ( stm == null ) {
			logger.log( Level.SEVERE, "Database statement closed!" );
			throw new SQLException( "Database statement closed!" );
		}
		return stm.getTime( parameterName );
	}

	public Time getTime(String parameterName, Calendar cal) throws SQLException {
		if ( stm == null ) {
			logger.log( Level.SEVERE, "Database statement closed!" );
			throw new SQLException( "Database statement closed!" );
		}
		return stm.getTime( parameterName, cal );
	}

	public Timestamp getTimestamp(int parameterIndex) throws SQLException {
		if ( stm == null ) {
			logger.log( Level.SEVERE, "Database statement closed!" );
			throw new SQLException( "Database statement closed!" );
		}
		return stm.getTimestamp( parameterIndex );
	}

	public Timestamp getTimestamp(int parameterIndex, Calendar cal) throws SQLException {
		if ( stm == null ) {
			logger.log( Level.SEVERE, "Database statement closed!" );
			throw new SQLException( "Database statement closed!" );
		}
		return stm.getTimestamp( parameterIndex, cal );
	}

	public void setTimestamp(String parameterName, Timestamp x) throws SQLException {
		if ( stm == null ) {
			logger.log( Level.SEVERE, "Database statement closed!" );
			throw new SQLException( "Database statement closed!" );
		}
		stm.setTimestamp( parameterName, x);
	}

	public void setTimestamp(String parameterName, Timestamp x, Calendar cal) throws SQLException {
		if ( stm == null ) {
			logger.log( Level.SEVERE, "Database statement closed!" );
			throw new SQLException( "Database statement closed!" );
		}
		stm.setTimestamp( parameterName, x, cal );
	}

	public Timestamp getTimestamp(String parameterName) throws SQLException {
		if ( stm == null ) {
			logger.log( Level.SEVERE, "Database statement closed!" );
			throw new SQLException( "Database statement closed!" );
		}
		return stm.getTimestamp( parameterName );
	}

	public Timestamp getTimestamp(String parameterName, Calendar cal) throws SQLException {
		if ( stm == null ) {
			logger.log( Level.SEVERE, "Database statement closed!" );
			throw new SQLException( "Database statement closed!" );
		}
		return stm.getTimestamp( parameterName, cal );
	}

	public URL getURL(int parameterIndex) throws SQLException {
		if ( stm == null ) {
			logger.log( Level.SEVERE, "Database statement closed!" );
			throw new SQLException( "Database statement closed!" );
		}
		return stm.getURL( parameterIndex );
	}

	public void setURL(String parameterName, URL val) throws SQLException {
		if ( stm == null ) {
			logger.log( Level.SEVERE, "Database statement closed!" );
			throw new SQLException( "Database statement closed!" );
		}
		stm.setURL( parameterName, val );
	}

	public URL getURL(String parameterName) throws SQLException {
		if ( stm == null ) {
			logger.log( Level.SEVERE, "Database statement closed!" );
			throw new SQLException( "Database statement closed!" );
		}
		return stm.getURL( parameterName );
	}

}

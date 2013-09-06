/*
 * PreparedStatement wrapper.
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
import java.sql.Clob;
import java.sql.Date;
import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PreparedStatementWrapper extends StatementWrapper implements PreparedStatement {

    /** Logging mechanism. */
	private static Logger logger = Logger.getLogger( PreparedStatementWrapper.class.getName() );

	private PreparedStatement stm;

	public PreparedStatementWrapper(PreparedStatement stm, ConnectionPooled connection) {
		super( stm, connection );
		this.stm = stm;
	}

	/*
	 * Parameters.
	 */

	public void clearParameters() throws SQLException {
		if ( stm == null ) {
			logger.log( Level.SEVERE, "Database statement closed!" );
			throw new SQLException( "Database statement closed!" );
		}
		stm.clearParameters();
	}

	public void setArray(int i, Array x) throws SQLException {
		if ( stm == null ) {
			logger.log( Level.SEVERE, "Database statement closed!" );
			throw new SQLException( "Database statement closed!" );
		}
		stm.setArray( i, x );
	}

	public void setAsciiStream(int parameterIndex, InputStream x, int length) throws SQLException {
		if ( stm == null ) {
			logger.log( Level.SEVERE, "Database statement closed!" );
			throw new SQLException( "Database statement closed!" );
		}
		stm.setAsciiStream( parameterIndex, x, length );
	}

	public void setBigDecimal(int parameterIndex, BigDecimal x) throws SQLException {
		if ( stm == null ) {
			logger.log( Level.SEVERE, "Database statement closed!" );
			throw new SQLException( "Database statement closed!" );
		}
		stm.setBigDecimal( parameterIndex, x );
	}

	public void setBinaryStream(int parameterIndex, InputStream x, int length) throws SQLException {
		if ( stm == null ) {
			logger.log( Level.SEVERE, "Database statement closed!" );
			throw new SQLException( "Database statement closed!" );
		}
		stm.setBinaryStream( parameterIndex, x, length );
	}

	public void setBlob(int i, Blob x) throws SQLException {
		if ( stm == null ) {
			logger.log( Level.SEVERE, "Database statement closed!" );
			throw new SQLException( "Database statement closed!" );
		}
		stm.setBlob( i, x );
	}

	public void setBoolean(int parameterIndex, boolean x) throws SQLException {
		if ( stm == null ) {
			logger.log( Level.SEVERE, "Database statement closed!" );
			throw new SQLException( "Database statement closed!" );
		}
		stm.setBoolean( parameterIndex, x );
	}

	public void setByte(int parameterIndex, byte x) throws SQLException {
		if ( stm == null ) {
			logger.log( Level.SEVERE, "Database statement closed!" );
			throw new SQLException( "Database statement closed!" );
		}
		stm.setByte( parameterIndex, x );
	}

	public void setBytes(int parameterIndex, byte[] x) throws SQLException {
		if ( stm == null ) {
			logger.log( Level.SEVERE, "Database statement closed!" );
			throw new SQLException( "Database statement closed!" );
		}
		stm.setBytes( parameterIndex, x );
	}

	public void setCharacterStream(int parameterIndex, Reader reader, int length) throws SQLException {
		if ( stm == null ) {
			logger.log( Level.SEVERE, "Database statement closed!" );
			throw new SQLException( "Database statement closed!" );
		}
		stm.setCharacterStream( parameterIndex, reader, length );
	}

	public void setClob(int i, Clob x) throws SQLException {
		if ( stm == null ) {
			logger.log( Level.SEVERE, "Database statement closed!" );
			throw new SQLException( "Database statement closed!" );
		}
		stm.setClob( i, x );
	}

	public void setDate(int parameterIndex, Date x) throws SQLException {
		if ( stm == null ) {
			logger.log( Level.SEVERE, "Database statement closed!" );
			throw new SQLException( "Database statement closed!" );
		}
		stm.setDate( parameterIndex, x );
	}

	public void setDate(int parameterIndex, Date x, Calendar cal) throws SQLException {
		if ( stm == null ) {
			logger.log( Level.SEVERE, "Database statement closed!" );
			throw new SQLException( "Database statement closed!" );
		}
		stm.setDate( parameterIndex, x, cal );
	}

	public void setDouble(int parameterIndex, double x) throws SQLException {
		if ( stm == null ) {
			logger.log( Level.SEVERE, "Database statement closed!" );
			throw new SQLException( "Database statement closed!" );
		}
		stm.setDouble( parameterIndex, x );
	}

	public void setFloat(int parameterIndex, float x) throws SQLException {
		if ( stm == null ) {
			logger.log( Level.SEVERE, "Database statement closed!" );
			throw new SQLException( "Database statement closed!" );
		}
		stm.setFloat( parameterIndex, x );
	}

	public void setInt(int parameterIndex, int x) throws SQLException {
		if ( stm == null ) {
			logger.log( Level.SEVERE, "Database statement closed!" );
			throw new SQLException( "Database statement closed!" );
		}
		stm.setInt( parameterIndex, x );
	}

	public void setLong(int parameterIndex, long x) throws SQLException {
		if ( stm == null ) {
			logger.log( Level.SEVERE, "Database statement closed!" );
			throw new SQLException( "Database statement closed!" );
		}
		stm.setLong( parameterIndex, x );
	}

	public void setNull(int paramIndex, int sqlType, String typeName) throws SQLException {
		if ( stm == null ) {
			logger.log( Level.SEVERE, "Database statement closed!" );
			throw new SQLException( "Database statement closed!" );
		}
		stm.setNull( paramIndex, sqlType, typeName );
	}

	public void setNull(int parameterIndex, int sqlType) throws SQLException {
		if ( stm == null ) {
			logger.log( Level.SEVERE, "Database statement closed!" );
			throw new SQLException( "Database statement closed!" );
		}
		stm.setNull( parameterIndex, sqlType );
	}

	public void setObject(int parameterIndex, Object x, int targetSqlType, int scale) throws SQLException {
		if ( stm == null ) {
			logger.log( Level.SEVERE, "Database statement closed!" );
			throw new SQLException( "Database statement closed!" );
		}
		stm.setObject( parameterIndex, x, targetSqlType, scale );
	}

	public void setObject(int parameterIndex, Object x, int targetSqlType) throws SQLException {
		if ( stm == null ) {
			logger.log( Level.SEVERE, "Database statement closed!" );
			throw new SQLException( "Database statement closed!" );
		}
		stm.setObject( parameterIndex, x, targetSqlType );
	}

	public void setObject(int parameterIndex, Object x) throws SQLException {
		if ( stm == null ) {
			logger.log( Level.SEVERE, "Database statement closed!" );
			throw new SQLException( "Database statement closed!" );
		}
		stm.setObject( parameterIndex, x );
	}

	public void setRef(int i, Ref x) throws SQLException {
		if ( stm == null ) {
			logger.log( Level.SEVERE, "Database statement closed!" );
			throw new SQLException( "Database statement closed!" );
		}
		stm.setRef( i, x );
	}

	public void setShort(int parameterIndex, short x) throws SQLException {
		if ( stm == null ) {
			logger.log( Level.SEVERE, "Database statement closed!" );
			throw new SQLException( "Database statement closed!" );
		}
		stm.setShort( parameterIndex, x );
	}

	public void setString(int parameterIndex, String x) throws SQLException {
		if ( stm == null ) {
			logger.log( Level.SEVERE, "Database statement closed!" );
			throw new SQLException( "Database statement closed!" );
		}
		stm.setString( parameterIndex, x );
	}

	public void setTime(int parameterIndex, Time x) throws SQLException {
		if ( stm == null ) {
			logger.log( Level.SEVERE, "Database statement closed!" );
			throw new SQLException( "Database statement closed!" );
		}
		stm.setTime( parameterIndex, x );
	}

	public void setTime(int parameterIndex, Time x, Calendar cal) throws SQLException {
		if ( stm == null ) {
			logger.log( Level.SEVERE, "Database statement closed!" );
			throw new SQLException( "Database statement closed!" );
		}
		stm.setTime( parameterIndex, x, cal );
	}

	public void setTimestamp(int parameterIndex, Timestamp x) throws SQLException {
		if ( stm == null ) {
			logger.log( Level.SEVERE, "Database statement closed!" );
			throw new SQLException( "Database statement closed!" );
		}
		stm.setTimestamp( parameterIndex, x );
	}

	public void setTimestamp(int parameterIndex, Timestamp x, Calendar cal) throws SQLException {
		if ( stm == null ) {
			logger.log( Level.SEVERE, "Database statement closed!" );
			throw new SQLException( "Database statement closed!" );
		}
		stm.setTimestamp( parameterIndex, x, cal );
	}

	public void setUnicodeStream(int parameterIndex, InputStream x, int length) throws SQLException {
		if ( stm == null ) {
			logger.log( Level.SEVERE, "Database statement closed!" );
			throw new SQLException( "Database statement closed!" );
		}
		stm.setUnicodeStream( parameterIndex, x, length );
	}

	public void setURL(int parameterIndex, URL x) throws SQLException {
		if ( stm == null ) {
			logger.log( Level.SEVERE, "Database statement closed!" );
			throw new SQLException( "Database statement closed!" );
		}
		stm.setURL( parameterIndex, x );
	}

	/*
	 * Metadata.
	 */

	public ResultSetMetaData getMetaData() throws SQLException {
		if ( stm == null ) {
			logger.log( Level.SEVERE, "Database statement closed!" );
			throw new SQLException( "Database statement closed!" );
		}
		return stm.getMetaData();
	}

	public ParameterMetaData getParameterMetaData() throws SQLException {
		if ( stm == null ) {
			logger.log( Level.SEVERE, "Database statement closed!" );
			throw new SQLException( "Database statement closed!" );
		}
		return stm.getParameterMetaData();
	}

	/*
	 * Batch.
	 */

	public void addBatch() throws SQLException {
		if ( stm == null ) {
			logger.log( Level.SEVERE, "Database statement closed!" );
			throw new SQLException( "Database statement closed!" );
		}
		stm.addBatch();
	}

	/*
	 * Execute.
	 */

	public boolean execute() throws SQLException {
		if ( stm == null ) {
			logger.log( Level.SEVERE, "Database statement closed!" );
			throw new SQLException( "Database statement closed!" );
		}
		return stm.execute();
	}

	public ResultSet executeQuery() throws SQLException {
		if ( stm == null ) {
			logger.log( Level.SEVERE, "Database statement closed!" );
			throw new SQLException( "Database statement closed!" );
		}
		return stm.executeQuery();
	}

	public int executeUpdate() throws SQLException {
		if ( stm == null ) {
			logger.log( Level.SEVERE, "Database statement closed!" );
			throw new SQLException( "Database statement closed!" );
		}
		return stm.executeUpdate();
	}

}

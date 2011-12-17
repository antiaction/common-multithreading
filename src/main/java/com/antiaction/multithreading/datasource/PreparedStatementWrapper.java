/*
 * Created on 19/11/2011
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
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

public class PreparedStatementWrapper extends StatementWrapper implements PreparedStatement {

	private PreparedStatement stm;

	public PreparedStatementWrapper(PreparedStatement stm, ConnectionPooled connection) {
		super( stm, connection );
		this.stm = stm;
	}

	/*
	 * Parameters.
	 */

	public void clearParameters() throws SQLException {
		stm.clearParameters();
	}

	public void setArray(int i, Array x) throws SQLException {
		stm.setArray( i, x );
	}

	public void setAsciiStream(int parameterIndex, InputStream x, int length) throws SQLException {
		stm.setAsciiStream( parameterIndex, x, length );
	}

	public void setBigDecimal(int parameterIndex, BigDecimal x) throws SQLException {
		stm.setBigDecimal( parameterIndex, x );
	}

	public void setBinaryStream(int parameterIndex, InputStream x, int length) throws SQLException {
		stm.setBinaryStream( parameterIndex, x, length );
	}

	public void setBlob(int i, Blob x) throws SQLException {
		stm.setBlob( i, x );
	}

	public void setBoolean(int parameterIndex, boolean x) throws SQLException {
		stm.setBoolean( parameterIndex, x );
	}

	public void setByte(int parameterIndex, byte x) throws SQLException {
		stm.setByte( parameterIndex, x );
	}

	public void setBytes(int parameterIndex, byte[] x) throws SQLException {
		stm.setBytes( parameterIndex, x );
	}

	public void setCharacterStream(int parameterIndex, Reader reader, int length) throws SQLException {
		stm.setCharacterStream( parameterIndex, reader, length );
	}

	public void setClob(int i, Clob x) throws SQLException {
		stm.setClob( i, x );
	}

	public void setDate(int parameterIndex, Date x) throws SQLException {
		stm.setDate( parameterIndex, x );
	}

	public void setDate(int parameterIndex, Date x, Calendar cal) throws SQLException {
		stm.setDate( parameterIndex, x, cal );
	}

	public void setDouble(int parameterIndex, double x) throws SQLException {
		stm.setDouble( parameterIndex, x );
	}

	public void setFloat(int parameterIndex, float x) throws SQLException {
		stm.setFloat( parameterIndex, x );
	}

	public void setInt(int parameterIndex, int x) throws SQLException {
		stm.setInt( parameterIndex, x );
	}

	public void setLong(int parameterIndex, long x) throws SQLException {
		stm.setLong( parameterIndex, x );
	}

	public void setNull(int paramIndex, int sqlType, String typeName) throws SQLException {
		stm.setNull( paramIndex, sqlType, typeName );
	}

	public void setNull(int parameterIndex, int sqlType) throws SQLException {
		stm.setNull( parameterIndex, sqlType );
	}

	public void setObject(int parameterIndex, Object x, int targetSqlType, int scale) throws SQLException {
		stm.setObject( parameterIndex, x, targetSqlType, scale );
	}

	public void setObject(int parameterIndex, Object x, int targetSqlType) throws SQLException {
		stm.setObject( parameterIndex, x, targetSqlType );
	}

	public void setObject(int parameterIndex, Object x) throws SQLException {
		stm.setObject( parameterIndex, x );
	}

	public void setRef(int i, Ref x) throws SQLException {
		stm.setRef( i, x );
	}

	public void setShort(int parameterIndex, short x) throws SQLException {
		stm.setShort( parameterIndex, x );
	}

	public void setString(int parameterIndex, String x) throws SQLException {
		stm.setString( parameterIndex, x );
	}

	public void setTime(int parameterIndex, Time x) throws SQLException {
		stm.setTime( parameterIndex, x );
	}

	public void setTime(int parameterIndex, Time x, Calendar cal) throws SQLException {
		stm.setTime( parameterIndex, x, cal );
	}

	public void setTimestamp(int parameterIndex, Timestamp x) throws SQLException {
		stm.setTimestamp( parameterIndex, x );
	}

	public void setTimestamp(int parameterIndex, Timestamp x, Calendar cal) throws SQLException {
		stm.setTimestamp( parameterIndex, x, cal );
	}

	public void setUnicodeStream(int parameterIndex, InputStream x, int length) throws SQLException {
		stm.setUnicodeStream( parameterIndex, x, length );
	}

	public void setURL(int parameterIndex, URL x) throws SQLException {
		stm.setURL( parameterIndex, x );
	}

	/*
	 * Metadata.
	 */

	public ResultSetMetaData getMetaData() throws SQLException {
		return stm.getMetaData();
	}

	public ParameterMetaData getParameterMetaData() throws SQLException {
		return stm.getParameterMetaData();
	}

	/*
	 * Batch.
	 */

	public void addBatch() throws SQLException {
		stm.addBatch();
	}

	/*
	 * Execute.
	 */

	public boolean execute() throws SQLException {
		return stm.execute();
	}

	public ResultSet executeQuery() throws SQLException {
		return stm.executeQuery();
	}

	public int executeUpdate() throws SQLException {
		return stm.executeUpdate();
	}

}

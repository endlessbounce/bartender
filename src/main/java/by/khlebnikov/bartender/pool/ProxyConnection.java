package by.khlebnikov.bartender.pool;

import java.sql.*;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executor;

/**
 * Proxy class to wrap around the Connection instance
 */
public class ProxyConnection implements Connection{

    // Vars ---------------------------------------------------------------------------------------
    private Connection connection;

    // Constructors -------------------------------------------------------------------------------
    ProxyConnection(Connection connection) {
        this.connection = connection;
    }

    // Actions ------------------------------------------------------------------------------------

    /**
     * @see java.sql.Connection#createStatement()
     */
    @Override
    public Statement createStatement() throws SQLException {
        return connection.createStatement();
    }

    /**
     * @see java.sql.Connection#prepareStatement(String sql)
     */
    @Override
    public PreparedStatement prepareStatement(String sql) throws SQLException {
        return connection.prepareStatement(sql);
    }

    /**
     * @see java.sql.Connection#prepareCall(String sql)
     */
    @Override
    public CallableStatement prepareCall(String sql) throws SQLException {
        return connection.prepareCall(sql);
    }

    /**
     * @see java.sql.Connection#nativeSQL(String sql)
     */
    @Override
    public String nativeSQL(String sql) throws SQLException {
        return connection.nativeSQL(sql);
    }

    /**
     * @see java.sql.Connection#setAutoCommit(boolean autoCommit)
     */
    @Override
    public void setAutoCommit(boolean autoCommit) throws SQLException {
        connection.setAutoCommit(autoCommit);
    }

    /**
     * @see java.sql.Connection#getAutoCommit()
     */
    @Override
    public boolean getAutoCommit() throws SQLException {
        return connection.getAutoCommit();
    }

    /**
     * @see java.sql.Connection#commit()
     */
    @Override
    public void commit() throws SQLException {
        connection.commit();
    }

    /**
     * @see java.sql.Connection#rollback()
     */
    @Override
    public void rollback() throws SQLException {
        connection.rollback();
    }

    /**
     * Closes ProxyConnection and releases wrapped connection
     */
    @Override
    public void close() throws SQLException {
        ConnectionPool.getInstance().releaseConnection(this);
    }

    /**
     * Closes Connection
     * @see java.sql.Connection#close()
     */
    void closeConnection() throws SQLException{
        connection.close();
    }

    /**
     * @see java.sql.Connection#isClosed()
     */
    @Override
    public boolean isClosed() throws SQLException {
        return connection.isClosed();
    }

    /**
     * @see java.sql.Connection#getMetaData()
     */
    @Override
    public DatabaseMetaData getMetaData() throws SQLException {
        return connection.getMetaData();
    }

    /**
     * @see java.sql.Connection#setReadOnly(boolean readOnly)
     */
    @Override
    public void setReadOnly(boolean readOnly) throws SQLException {
        connection.setReadOnly(readOnly);
    }

    /**
     * @see java.sql.Connection#isReadOnly()
     */
    @Override
    public boolean isReadOnly() throws SQLException {
        return connection.isReadOnly();
    }

    /**
     * @see java.sql.Connection#setCatalog(String catalog)
     */
    @Override
    public void setCatalog(String catalog) throws SQLException {
        connection.setCatalog(catalog);
    }

    /**
     * @see java.sql.Connection#getCatalog()
     */
    @Override
    public String getCatalog() throws SQLException {
        return connection.getCatalog();
    }

    /**
     * @see java.sql.Connection#setTransactionIsolation(int level)
     */
    @Override
    public void setTransactionIsolation(int level) throws SQLException {
        connection.setTransactionIsolation(level);
    }

    /**
     * @see java.sql.Connection#getTransactionIsolation()
     */
    @Override
    public int getTransactionIsolation() throws SQLException {
        return connection.getTransactionIsolation();
    }

    /**
     * @see java.sql.Connection#getWarnings()
     */
    @Override
    public SQLWarning getWarnings() throws SQLException {
        return connection.getWarnings();
    }

    /**
     * @see java.sql.Connection#clearWarnings()
     */
    @Override
    public void clearWarnings() throws SQLException {
        connection.clearWarnings();
    }

    /**
     * @see java.sql.Connection#createStatement(int resultSetType, int resultSetConcurrency)
     */
    @Override
    public Statement createStatement(int resultSetType, int resultSetConcurrency) throws SQLException {
        return connection.createStatement(resultSetType, resultSetConcurrency);
    }

    /**
     * @see java.sql.Connection#prepareStatement(String sql, int resultSetType, int resultSetConcurrency)
     */
    @Override
    public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
        return connection.prepareStatement(sql, resultSetType, resultSetConcurrency);
    }

    /**
     * @see java.sql.Connection#prepareCall(String sql, int resultSetType, int resultSetConcurrency)
     */
    @Override
    public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
        return connection.prepareCall(sql, resultSetType, resultSetConcurrency);
    }

    /**
     * @see java.sql.Connection#getTypeMap()
     */
    @Override
    public Map<String, Class<?>> getTypeMap() throws SQLException {
        return connection.getTypeMap();
    }

    /**
     * @see java.sql.Connection#setTypeMap(Map<String, Class<?>>)
     */
    @Override
    public void setTypeMap(Map<String, Class<?>> map) throws SQLException {
        connection.setTypeMap(map);
    }

    /**
     * @see java.sql.Connection#setHoldability(int holdability)
     */
    @Override
    public void setHoldability(int holdability) throws SQLException {
        connection.setHoldability(holdability);
    }

    /**
     * @see java.sql.Connection#getHoldability()
     */
    @Override
    public int getHoldability() throws SQLException {
        return connection.getHoldability();
    }

    /**
     * @see java.sql.Connection#setSavepoint()
     */
    @Override
    public Savepoint setSavepoint() throws SQLException {
        return connection.setSavepoint();
    }

    /**
     * @see java.sql.Connection#setSavepoint(String name)
     */
    @Override
    public Savepoint setSavepoint(String name) throws SQLException {
        return connection.setSavepoint(name);
    }

    /**
     * @see java.sql.Connection#rollback(Savepoint savepoint)
     */
    @Override
    public void rollback(Savepoint savepoint) throws SQLException {
        connection.rollback(savepoint);
    }

    /**
     * @see java.sql.Connection#releaseSavepoint(Savepoint savepoint)
     */
    @Override
    public void releaseSavepoint(Savepoint savepoint) throws SQLException {
        connection.releaseSavepoint(savepoint);
    }

    /**
     * @see java.sql.Connection#createStatement(int resultSetType, int resultSetConcurrency, int resultSetHoldability)
     */
    @Override
    public Statement createStatement(int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        return connection.createStatement(resultSetType, resultSetConcurrency, resultSetHoldability);
    }

    /**
     * @see java.sql.Connection#prepareStatement(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability)
     */
    @Override
    public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        return connection.prepareStatement(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
    }

    /**
     * @see java.sql.Connection#prepareCall(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability)
     */
    @Override
    public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        return connection.prepareCall(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
    }

    /**
     * @see java.sql.Connection#prepareStatement(String sql, int autoGeneratedKeys)
     */
    @Override
    public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys) throws SQLException {
        return connection.prepareStatement(sql, autoGeneratedKeys);
    }

    /**
     * @see java.sql.Connection#prepareStatement(String sql, int[] columnIndexes)
     */
    @Override
    public PreparedStatement prepareStatement(String sql, int[] columnIndexes) throws SQLException {
        return connection.prepareStatement(sql, columnIndexes);
    }

    /**
     * @see java.sql.Connection#prepareStatement(String sql, String[] columnNames)
     */
    @Override
    public PreparedStatement prepareStatement(String sql, String[] columnNames) throws SQLException {
        return connection.prepareStatement(sql, columnNames);
    }

    /**
     * @see java.sql.Connection#createClob()
     */
    @Override
    public Clob createClob() throws SQLException {
        return connection.createClob();
    }

    /**
     * @see java.sql.Connection#createBlob()
     */
    @Override
    public Blob createBlob() throws SQLException {
        return connection.createBlob();
    }

    /**
     * @see java.sql.Connection#createNClob()
     */
    @Override
    public NClob createNClob() throws SQLException {
        return connection.createNClob();
    }

    /**
     * @see java.sql.Connection#createSQLXML()
     */
    @Override
    public SQLXML createSQLXML() throws SQLException {
        return connection.createSQLXML();
    }

    /**
     * @see java.sql.Connection#isValid(int timeout)
     */
    @Override
    public boolean isValid(int timeout) throws SQLException {
        return connection.isValid(timeout);
    }

    /**
     * @see java.sql.Connection#setClientInfo(String name, String value)
     */
    @Override
    public void setClientInfo(String name, String value) throws SQLClientInfoException {
        connection.setClientInfo(name, value);
    }

    /**
     * @see java.sql.Connection#setClientInfo(Properties properties)
     */
    @Override
    public void setClientInfo(Properties properties) throws SQLClientInfoException {
        connection.setClientInfo(properties);
    }

    /**
     * @see java.sql.Connection#getClientInfo(String name)
     */
    @Override
    public String getClientInfo(String name) throws SQLException {
        return connection.getClientInfo(name);
    }

    /**
     * @see java.sql.Connection#getClientInfo()
     */
    @Override
    public Properties getClientInfo() throws SQLException {
        return connection.getClientInfo();
    }

    /**
     * @see java.sql.Connection#createArrayOf(String typeName, Object[] elements)
     */
    @Override
    public Array createArrayOf(String typeName, Object[] elements) throws SQLException {
        return connection.createArrayOf(typeName, elements);
    }

    /**
     * @see java.sql.Connection#createStruct(String typeName, Object[] attributes)
     */
    @Override
    public Struct createStruct(String typeName, Object[] attributes) throws SQLException {
        return connection.createStruct(typeName, attributes);
    }

    /**
     * @see java.sql.Connection#setSchema(String)
     */
    @Override
    public void setSchema(String schema) throws SQLException {
        connection.setSchema(schema);
    }

    /**
     * @see java.sql.Connection#getSchema()
     */
    @Override
    public String getSchema() throws SQLException {
        return connection.getSchema();
    }

    /**
     * @see java.sql.Connection#abort(Executor)
     */
    @Override
    public void abort(Executor executor) throws SQLException {
        connection.abort(executor);
    }

    /**
     * @see java.sql.Connection#setNetworkTimeout(Executor, int)
     */
    @Override
    public void setNetworkTimeout(Executor executor, int milliseconds) throws SQLException {
        connection.setNetworkTimeout(executor, milliseconds);
    }

    /**
     * @see java.sql.Connection#getNetworkTimeout()
     */
    @Override
    public int getNetworkTimeout() throws SQLException {
        return connection.getNetworkTimeout();
    }

    /**
     * @see java.sql.Connection#unwrap(Class<T>)
     */
    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        return connection.unwrap(iface);
    }

    /**
     * @see java.sql.Connection#isWrapperFor(java.lang.Class<?>)
     */
    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return connection.isWrapperFor(iface);
    }
}

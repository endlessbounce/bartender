package test.khlebnikov.bartender.pool;

import by.khlebnikov.bartender.pool.ConnectionPool;
import by.khlebnikov.bartender.pool.ProxyConnection;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import java.sql.SQLException;
import java.util.concurrent.TimeUnit;

public class ConnectionPoolTest {

    @AfterMethod
    public void cleanUp() {
        ConnectionPool.getInstance().closeAll();
        ConnectionPool.getInstance().setConnectionTimeLimit(10);
    }

    @Test
    public void getInstance() throws Exception {
        Assert.assertNotNull(ConnectionPool.getInstance());
    }

    @Test
    public void getConnectionPos() throws Exception {
        Assert.assertNotNull(ConnectionPool.getInstance().getConnection());
    }

    @Test
    public void getConnectionTimeoutPos() throws Exception {
        ProxyConnection connection = null;
        ConnectionPool.getInstance().setTimeout(3);

        for (int i = 0; i <= 10; i++) {
            connection = ConnectionPool.getInstance().getConnection();
        }

        Assert.assertNotNull(connection);
    }

    @Test(expectedExceptions = SQLException.class)
    public void getConnectionTimeoutNeg() throws Exception {
        ConnectionPool.getInstance().setTimeout(1);
        for (int i = 0; i <= 10; i++) {
            ConnectionPool.getInstance().getConnection();
        }
    }

    /**
     * If a connection was properly closed, we will get the same object twice
     *
     * @throws Exception
     */
    @Test
    public void releaseConnection() throws Exception {
        ProxyConnection con = ConnectionPool.getInstance().getConnection();
        con.close();
        ProxyConnection con1 = ConnectionPool.getInstance().getConnection();

        Assert.assertEquals(con, con1);
    }

    /**
     * If all connections were closed forcefully, we will get two different objects
     *
     * @throws Exception
     */
    @Test
    public void closeAll() throws Exception {
        ProxyConnection con = ConnectionPool.getInstance().getConnection();
        ConnectionPool.getInstance().closeAll();
        ProxyConnection con1 = ConnectionPool.getInstance().getConnection();

        Assert.assertNotEquals(con, con1);
    }

    @Test (expectedExceptions = SQLException.class)
    public void deregisterDriver() throws Exception {
        ConnectionPool.getInstance().deregisterDriver();
        ConnectionPool.getInstance().getConnection();
    }

}
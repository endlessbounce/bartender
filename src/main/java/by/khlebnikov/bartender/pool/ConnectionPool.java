package by.khlebnikov.bartender.pool;

import by.khlebnikov.bartender.constant.ConstDatabase;
import by.khlebnikov.bartender.reader.PropertyReader;
import by.khlebnikov.bartender.utility.TimeGenerator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Connection test.khlebnikov.bartender.service.pool singleton class. Serves connections for the application.
 */
public final class ConnectionPool {

    // Constants ----------------------------------------------------------------------------------
    private static final List<ProxyConnection> IDLE_CONNECTIONS = new ArrayList<>();
    private static final List<ProxyConnection> ACTIVE_CONNECTIONS = new ArrayList<>();
    private static final String URL = PropertyReader.getConfigProperty(ConstDatabase.URL);
    private static final String USER = PropertyReader.getConfigProperty(ConstDatabase.LOGIN);
    private static final String PASSWORD = PropertyReader.getConfigProperty(ConstDatabase.PASSWORD);
    private static final String DRIVER = PropertyReader.getConfigProperty(ConstDatabase.DRIVER);
    private static final int POOL_SIZE = 10;

    // Vars ---------------------------------------------------------------------------------------
    /*time that connections are given to execute SQL queries*/
    private static int connectionTimeLimit = 10;
    /*timeout that a thread must wait between attempts to get a connection*/
    private static int timeout = 2;
    private static int driverLoadAttempt = 5;
    private static Logger logger = LogManager.getLogger();
    private static ReentrantLock lock = new ReentrantLock();
    private static ConnectionPool instance;
    private static AtomicBoolean isPoolCreated = new AtomicBoolean(false);
    private static Condition waitForConnection = lock.newCondition();

    // Constructors -------------------------------------------------------------------------------

    /**
     * Private constructor for a singleton test.khlebnikov.bartender.service.pool.
     * There are 5 attempts that are given to try to load the driver
     */
    private ConnectionPool() {
        if (instance != null) {
            throw new RuntimeException("Already initialized.");
        }

        while (driverLoadAttempt-- != 0) {
            try {
                Class.forName(DRIVER);
                driverLoadAttempt = 0;
            } catch (ClassNotFoundException e) {
            /*is not handled as we don't really have a choice in case of failure*/
                logger.error("failed to load driver at attempt #" + driverLoadAttempt, e);
            }
        }
    }

    // Getters & Setters --------------------------------------------------------------------------

    public static int getConnectionTimeLimit() {
        return connectionTimeLimit;
    }

    public static void setConnectionTimeLimit(int sec) {
        ConnectionPool.connectionTimeLimit = sec;
    }

    public static int getTimeout() {
        return timeout;
    }

    public static void setTimeout(int sec) {
        ConnectionPool.timeout = sec;
    }

    // Actions ------------------------------------------------------------------------------------

    /**
     * Returns this instance of the test.khlebnikov.bartender.service.pool
     *
     * @return the instance of the test.khlebnikov.bartender.service.pool
     */
    public static ConnectionPool getInstance() {

        if (!isPoolCreated.get()) {
            lock.lock();
            try {
                if (instance == null) {
                    instance = new ConnectionPool();
                    isPoolCreated.set(true);
                }
            } finally {
                lock.unlock();
            }
        }

        return instance;
    }

    /**
     * Returns new connection. If there are idle connections in the IDLE_CONNECTIONS list - returns one from it.
     * If there are no idle connections, but ACTIVE_CONNECTIONS list isn't full - create a new connection.
     * If there are no free connections and can't create a new one, wait for 2 seconds for a connection
     * to appear. 5 attempts are given to wait and check for an idle connection, otherwise check if there
     * are connections that have been busy for a longer period of time than the allowed limit.
     * (calls findOverdueConnection() method).
     *
     * @return ProxyConnection
     * @throws SQLException if thrown, will be handled in the DAO layer
     */
    public ProxyConnection getConnection() throws SQLException, InterruptedException {
        ProxyConnection connection = null;
        int attempt = 5;

        while (connection == null && attempt-- != 0) {
            lock.lock();
            try {
                if (!IDLE_CONNECTIONS.isEmpty()) {
                    connection = IDLE_CONNECTIONS.remove(0);
                } else if (ACTIVE_CONNECTIONS.size() < POOL_SIZE) {
                    connection = createConnection();
                } else {

                    if (attempt != 0) {
                        waitForConnection.await(timeout, TimeUnit.SECONDS);
                    } else {//claim overdue connection
                        connection = findOverdueConnection();
                    }
                }
            } finally {
                lock.unlock();
            }
        }

        connection.setOperationStartTime(TimeGenerator.currentTime());
        return connection;
    }

    /**
     * Releases connection and puts it back into active connections list.
     *
     * @param connection ProxyConnection
     * @throws SQLException if an error occurs, it will be handled on the DAO level
     */
    public void releaseConnection(ProxyConnection connection) throws SQLException {
        lock.lock();
        try {
            ACTIVE_CONNECTIONS.remove(connection);
            if (connection.isValid(0)) {
                rollbackChanges(connection);
            }
            IDLE_CONNECTIONS.add(connection);
        } finally {
            lock.unlock();
        }
    }

    /**
     * Closes all connections
     */
    public void closeAll() {
        lock.lock();
        try {
            closeAllInList(ACTIVE_CONNECTIONS);
            closeAllInList(IDLE_CONNECTIONS);
        } finally {
            lock.unlock();
        }
    }

    /**
     * The methodology of blanket driver deregistration is dangerous.
     * Some drivers returned by the DriverManager.getDrivers() method may have been
     * loaded by the parent ClassLoader (i.e., the servlet container's classloader)
     * not the webapp context's ClassLoader (e.g., they may be in the container's lib folder,
     * not the webapp's, and therefore shared across the whole container and be in use elsewhere).
     * Deregistering these will affect any other webapps which may be using
     * them (or even the container itself).
     */
    public void deregisterDriver() {
        // Deregister JDBC drivers in this context's ClassLoader:
        // Get the webapp's ClassLoader
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        // Loop through all drivers
        Enumeration<Driver> drivers = DriverManager.getDrivers();
        while (drivers.hasMoreElements()) {
            Driver driver = drivers.nextElement();
            if (driver.getClass().getClassLoader() == loader) {
                // This driver was registered by the webapp's ClassLoader, so deregister it:
                try {
                    DriverManager.deregisterDriver(driver);
                } catch (SQLException ex) {
                    logger.catching(ex);
                }
            }
        }
    }

    /**
     * Prevents cloning if this class is inherited
     *
     * @return Object
     * @throws CloneNotSupportedException
     */
    @Override
    protected Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }

    /**
     * Forces all connections in the list to get closed
     *
     * @param connectionList either list of active or idle connections
     */
    private void closeAllInList(List<ProxyConnection> connectionList) {
        for (int i = connectionList.size(); i > 0; i--) {
            ProxyConnection connection = connectionList.remove(i - 1);

            try {
                if (!connection.getAutoCommit()) {
                    connection.rollback();
                }
                connection.closeConnection();
            } catch (SQLException e) {
                /*There's no need in special handling for such exceptions in case of redeploy*/
                logger.catching(e);
            }

        }
    }

    /**
     * In case if all available connections are busy and wait timeout is elapsed,
     * an attempt to find on overdue connection is done. If such a connection is found,
     * its changes are rollbacked and it is removed from the ACTIVE_CONNECTIONS list,
     * so a new connection can be created.
     * This overdue connection is closed in case it's broken.
     *
     * @throws SQLException
     */
    private ProxyConnection findOverdueConnection() throws SQLException {
        long currentTime = TimeGenerator.currentTime();

        lock.lock();
        try {

            ProxyConnection overdueConnection = ACTIVE_CONNECTIONS.stream()
                    .filter(con -> (currentTime - con.getOperationStartTime()) > connectionTimeLimit)
                    .sorted(Comparator.comparing(ProxyConnection::getOperationStartTime).reversed())
                    .findFirst()
                    .orElseThrow(() -> new SQLException("Unable to create or find a free connection."));

            rollbackChanges(overdueConnection);
            ACTIVE_CONNECTIONS.remove(overdueConnection);
            overdueConnection.closeConnection();
            return createConnection();
        } finally {
            lock.unlock();
        }
    }

    /**
     * Rollbacks all changes for a given connection
     *
     * @param connection ProxyConnection
     * @throws SQLException
     */
    private void rollbackChanges(ProxyConnection connection) throws SQLException {
        if (!connection.getAutoCommit()) {
            connection.rollback();
            connection.setAutoCommit(true);
        }
    }

    /**
     * Creates new ProxyConnection
     *
     * @return ProxyConnection
     * @throws SQLException
     */
    private ProxyConnection createConnection() throws SQLException {
        ProxyConnection connection = new ProxyConnection(DriverManager.getConnection(URL, USER, PASSWORD));
        ACTIVE_CONNECTIONS.add(connection);
        return connection;
    }
}
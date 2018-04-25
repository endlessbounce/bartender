package by.khlebnikov.bartender.pool;

import by.khlebnikov.bartender.constant.ConstDatabase;
import by.khlebnikov.bartender.exception.DataAccessException;
import by.khlebnikov.bartender.reader.PropertyReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Connection pool singleton class. Serves connections for the application.
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
    private static Logger logger = LogManager.getLogger();
    private static ReentrantLock lock = new ReentrantLock();
    private static ConnectionPool instance;
    private static AtomicBoolean isPoolCreated = new AtomicBoolean(false);

    // Constructors -------------------------------------------------------------------------------

    /**
     * Private constructor for a singleton pool
     */
    private ConnectionPool() {
        if (instance != null) {
            throw new RuntimeException("Already initialized.");
        }
        try {
            Class.forName(DRIVER);
        } catch (ClassNotFoundException e) {
            logger.catching(e);
        }
    }

    /**
     * Returns this instance of the pool
     *
     * @return the instance of the pool
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
     * Returns new connection.
     *
     * @return
     * @throws SQLException if thrown, will be handled in the DAO layer
     */
    public ProxyConnection getConnection() throws SQLException {
        ProxyConnection connection = null;

        while (connection == null) {
            lock.lock();
            try {
                if (!IDLE_CONNECTIONS.isEmpty()) {
                    connection = IDLE_CONNECTIONS.remove(0);
                } else if (ACTIVE_CONNECTIONS.size() < POOL_SIZE) {
                    connection = new ProxyConnection(DriverManager.getConnection(URL, USER, PASSWORD));
                    ACTIVE_CONNECTIONS.add(connection);
                }
            } finally {
                lock.unlock();
            }
        }

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
                if (!connection.getAutoCommit()) {
                    connection.rollback();
                    connection.setAutoCommit(true);
                }
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
                /*Just making a log because it doesn't make sense to throw an exception
                * when the app is being shut down or server restarts.
                * Moreover, there's no special handling for such exceptions on the higher layers*/
                logger.catching(e);
            }

        }
    }
}
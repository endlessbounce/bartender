package by.khlebnikov.bartender.pool;

import by.khlebnikov.bartender.constant.Constant;
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

public final class ConnectionPool {
    private static Logger logger = LogManager.getLogger();
    private static ConnectionPool instance;
    private static ReentrantLock lock = new ReentrantLock();
    private static final List<ProxyConnection> idleConnections = new ArrayList<>();
    private static final List<ProxyConnection> activeConnections = new ArrayList<>();
    private static final String URL = PropertyReader.getConfigProperty(Constant.DB_URL);
    private static final String USER = PropertyReader.getConfigProperty(Constant.DB_LOGIN);
    private static final String PASSWORD = PropertyReader.getConfigProperty(Constant.DB_PASSWORD);
    private static final String DRIVER = PropertyReader.getConfigProperty(Constant.DB_DRIVER);
    private static final int POOL_SIZE = 10;
    private static AtomicBoolean poolCreated = new AtomicBoolean(false);

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

    public static ConnectionPool getInstance() {

        if (!poolCreated.get()) {
            lock.lock();
            try {
                if (instance == null) {
                    instance = new ConnectionPool();
                    poolCreated.set(true);
                }
            } finally {
                lock.unlock();
            }
        }

        return instance;
    }

    public ProxyConnection getConnection() throws InterruptedException {
        ProxyConnection connection = null;

        while (connection == null) {
            lock.lock();
            try {
                if (!idleConnections.isEmpty()) {
                    connection = idleConnections.remove(0);
                } else if (activeConnections.size() < POOL_SIZE) {
                    try {
                        connection = new ProxyConnection(DriverManager.getConnection(URL, USER, PASSWORD));
                        activeConnections.add(connection);
                    } catch (SQLException e) {
                        logger.catching(e);
                    }
                }
            } finally {
                lock.unlock();
            }
        }

        return connection;
    }

    public void releaseConnection(ProxyConnection connection) {
        lock.lock();
        try {
            activeConnections.remove(connection);
            if (connection.isValid(0)) {
                if (!connection.getAutoCommit()) {
                    connection.rollback();
                    connection.setAutoCommit(true);
                }
            }
            idleConnections.add(connection);
        } catch (SQLException e) {
            logger.catching(e);
        } finally {
            lock.unlock();
        }
    }

    public void closeAll() {
        lock.lock();
        try {
            closeAllInList(activeConnections);
            closeAllInList(idleConnections);
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
    public void deregisterDriver(){
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

    @Override
    protected Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }

    private void closeAllInList(List<ProxyConnection> connectionList){
        for (int i = connectionList.size(); i > 0; i--) {
            ProxyConnection connection = connectionList.remove(i - 1);

            try {
                if (!connection.getAutoCommit()) {
                    connection.rollback();
                }
                connection.closeConnection();
            } catch (SQLException e) {
                logger.catching(e);
            }

        }
    }
}

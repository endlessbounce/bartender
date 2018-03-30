package by.khlebnikov.bartender.pool;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantLock;

public final class ConnectionPool {
    private static Logger logger = LogManager.getLogger();
    private static ConnectionPool instance;
    private static ReentrantLock lock = new ReentrantLock();
    private static final List<ProxyConnection> idleConnections = new ArrayList<>();
    private static final List<ProxyConnection> activeConnections = new ArrayList<>();
    private static final String URL = "jdbc:mysql://localhost:3306/bartender";
    private static final String USER = "root";
    private static final String PASSWORD = "admin";
    private static final int POOL_SIZE = 10;
    private static AtomicBoolean poolCreated = new AtomicBoolean(false);

    private ConnectionPool() {
        if (instance != null) {
            throw new RuntimeException("Already initialized.");
        }
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            logger.catching(e);
        }
    }

    public static ConnectionPool getInstance() {

        if(!poolCreated.get()){
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

        while(connection == null){
            lock.lock();
            try{
                if(!idleConnections.isEmpty()){
                    connection = idleConnections.remove(0);
                } else if(activeConnections.size() < POOL_SIZE){
                    try {
                        connection = new ProxyConnection(DriverManager.getConnection(URL, USER, PASSWORD));
                        activeConnections.add(connection);
                    } catch (SQLException e) {
                        logger.catching(e);
                    }
                }
            }finally {
                lock.unlock();
            }
        }

        return connection;
    }

    public void releaseConnection(ProxyConnection connection) {
        lock.lock();
        try{
            activeConnections.remove(connection);
            if (connection.isValid(0)){
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

    //TODO driver deregister + close all connections via listeners
    @Override
    protected Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }
}

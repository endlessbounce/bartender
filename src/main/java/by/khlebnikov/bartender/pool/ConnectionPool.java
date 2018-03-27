package by.khlebnikov.bartender.pool;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

public final class ConnectionPool {
    private static ConnectionPool instance;
    private static ReentrantLock lock = new ReentrantLock();
    private static final List<ProxyConnection> idleConnections = new ArrayList<>();
    private static final List<ProxyConnection> activeConnections = new ArrayList<>();
    private static final int NUMBER_OF_CONNECTIONS = 10;
    private static final String URL = "jdbc:mysql://localhost:3306/bartender/?useSSL=false";
    private static final String USER = "root";
    private static final String PASSWORD = "admin";
    private Logger logger = LogManager.getLogger();

    private ConnectionPool() {
        if (instance != null) {
            throw new IllegalStateException("Already initialized.");
        }
    }

    public static ConnectionPool getInstance() {
        lock.lock();
        try {
            if (instance == null) {
                instance = new ConnectionPool();
            }
        } finally {
            lock.unlock();
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
                } else if(activeConnections.size() < NUMBER_OF_CONNECTIONS){
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

    public void closeConnection(ProxyConnection connection) {
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

    @Override
    protected Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }
}

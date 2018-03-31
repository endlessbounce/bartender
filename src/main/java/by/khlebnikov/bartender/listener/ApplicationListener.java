package by.khlebnikov.bartender.listener;

import by.khlebnikov.bartender.pool.ConnectionPool;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class ApplicationListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent event) {
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {
        // First close any background tasks which may be using the DB
        // Close DB connection pools
        ConnectionPool.getInstance().closeAll();
        ConnectionPool.getInstance().deregisterDriver();
    }

}
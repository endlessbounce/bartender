package by.khlebnikov.bartender.listener;

import by.khlebnikov.bartender.pool.ConnectionPool;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * Class used to carry out some functions on Init or Destroy of the app's ServletContext
 */
@WebListener
public class ApplicationListener implements ServletContextListener {

    // Actions ------------------------------------------------------------------------------------

    /**
     * Is not implemented
     *
     * @param event
     */
    @Override
    public void contextInitialized(ServletContextEvent event) {
    }

    /**
     * Closes all connections in the pool and deregisters JDBC driver
     *
     * @param event
     */
    @Override
    public void contextDestroyed(ServletContextEvent event) {
        // First close any background tasks which may be using the DB
        // Close DB connection pools
        ConnectionPool.getInstance().closeAll();
        ConnectionPool.getInstance().deregisterDriver();
    }

}
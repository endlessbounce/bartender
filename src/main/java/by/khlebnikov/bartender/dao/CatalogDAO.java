package by.khlebnikov.bartender.dao;

import by.khlebnikov.bartender.pool.ConnectionPool;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class CatalogDao {
    private Logger logger = LogManager.getLogger();
    private ConnectionPool pool = ConnectionPool.getInstance();

    public ArrayList<String> findFormData(String query) {
        ArrayList<String> result = new ArrayList<>();

        try (Connection connection = pool.getConnection();
             Statement statement = connection.createStatement()
        ) {
            ResultSet rs = statement.executeQuery(query);

            while (rs.next()) {
                String data = rs.getString(1);
                result.add(data);
                logger.debug("data read: " + data);
            }

        } catch (SQLException | InterruptedException e) {
            logger.catching(e);
        }

        return result;
    }
}

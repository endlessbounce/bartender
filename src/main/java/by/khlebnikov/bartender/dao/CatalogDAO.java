package by.khlebnikov.bartender.dao;

import by.khlebnikov.bartender.constant.ConstLocale;
import by.khlebnikov.bartender.constant.ConstQueryCatalog;
import by.khlebnikov.bartender.exception.DataAccessException;
import by.khlebnikov.bartender.pool.ConnectionPool;
import by.khlebnikov.bartender.reader.PropertyReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class CatalogDao {
    // Constants ----------------------------------------------------------------------------------
    private static String QUERY_INGREDIENT = PropertyReader.getQueryProperty(ConstQueryCatalog.INGREDIENT);
    private static String QUERY_INGREDIENT_LANG = PropertyReader.getQueryProperty(ConstQueryCatalog.INGREDIENT_LANG);
    private static String QUERY_BASE = PropertyReader.getQueryProperty(ConstQueryCatalog.BASE_DRINK);
    private static String QUERY_BASE_LANG = PropertyReader.getQueryProperty(ConstQueryCatalog.BASE_DRINK_LANG);
    private static String QUERY_TYPE = PropertyReader.getQueryProperty(ConstQueryCatalog.DRINK_TYPE);
    private static String QUERY_TYPE_LANG = PropertyReader.getQueryProperty(ConstQueryCatalog.DRINK_TYPE_LANG);

    // Vars ---------------------------------------------------------------------------------------
    private Logger logger = LogManager.getLogger();
    private ConnectionPool pool = ConnectionPool.getInstance();

    // Actions ------------------------------------------------------------------------------------
    public ArrayList<String> findIngredient(String language) throws DataAccessException {
        return ConstLocale.EN.equals(language) ? findFormData(QUERY_INGREDIENT) : findFormData(QUERY_INGREDIENT_LANG);
    }

    public ArrayList<String> findBaseDrink(String language) throws DataAccessException {
        return ConstLocale.EN.equals(language) ? findFormData(QUERY_BASE) : findFormData(QUERY_BASE_LANG);
    }

    public ArrayList<String> findDrinkGroup(String language) throws DataAccessException {
        return ConstLocale.EN.equals(language) ? findFormData(QUERY_TYPE) : findFormData(QUERY_TYPE_LANG);
    }

    private ArrayList<String> findFormData(String query) throws DataAccessException {
        ArrayList<String> result = new ArrayList<>();

        try (Connection connection = pool.getConnection();
             Statement statement = connection.createStatement()
        ) {
            ResultSet rs = statement.executeQuery(query);

            while (rs.next()) {
                result.add(rs.getString(1));
            }

            logger.debug("Form data read: " + result);
        } catch (SQLException | InterruptedException e) {
            throw new DataAccessException("Form data found: " + result, e);
        }

        return result;
    }
}

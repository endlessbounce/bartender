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

/**
 * Class providing methods to access the database to retrieve information for catalog page
 */
public class CatalogDao {

    // Constants ----------------------------------------------------------------------------------
    private static final String QUERY_INGREDIENT = PropertyReader.getQueryProperty(ConstQueryCatalog.INGREDIENT);
    private static final String QUERY_INGREDIENT_LANG = PropertyReader.getQueryProperty(ConstQueryCatalog.INGREDIENT_LANG);
    private static final String QUERY_BASE = PropertyReader.getQueryProperty(ConstQueryCatalog.BASE_DRINK);
    private static final String QUERY_BASE_LANG = PropertyReader.getQueryProperty(ConstQueryCatalog.BASE_DRINK_LANG);
    private static final String QUERY_TYPE = PropertyReader.getQueryProperty(ConstQueryCatalog.DRINK_TYPE);
    private static final String QUERY_TYPE_LANG = PropertyReader.getQueryProperty(ConstQueryCatalog.DRINK_TYPE_LANG);

    // Vars ---------------------------------------------------------------------------------------
    private static Logger logger = LogManager.getLogger();

    // Actions ------------------------------------------------------------------------------------

    /**
     * Finds all ingredients for the given language
     *
     * @param language current language of the user
     * @return list of ingredients
     * @throws DataAccessException is thrown when a database error occurs
     */
    public ArrayList<String> findIngredient(String language) throws DataAccessException {
        return ConstLocale.EN.equals(language) ? findFormData(QUERY_INGREDIENT) : findFormData(QUERY_INGREDIENT_LANG);
    }

    /**
     * Finds all names of base drinks for the given language
     *
     * @param language current language of the user
     * @return list of base drinks
     * @throws DataAccessException is thrown when a database error occurs
     */
    public ArrayList<String> findBaseDrink(String language) throws DataAccessException {
        return ConstLocale.EN.equals(language) ? findFormData(QUERY_BASE) : findFormData(QUERY_BASE_LANG);
    }

    /**
     * Finds all drink groups for the given language
     *
     * @param language current language of the user
     * @return list of drink groups
     * @throws DataAccessException is thrown when a database error occurs
     */
    public ArrayList<String> findDrinkGroup(String language) throws DataAccessException {
        return ConstLocale.EN.equals(language) ? findFormData(QUERY_TYPE) : findFormData(QUERY_TYPE_LANG);
    }

    // Helper methods ------------------------------------------------------------------------

    /**
     * Executes a query and returns a list of required items
     *
     * @param query chosen query
     * @return list of items
     * @throws DataAccessException is thrown when a database error occurs
     */
    private ArrayList<String> findFormData(String query) throws DataAccessException {
        ArrayList<String> result = new ArrayList<>();

        try (Connection connection = ConnectionPool.getInstance().getConnection();
             Statement statement = connection.createStatement()
        ) {
            ResultSet rs = statement.executeQuery(query);

            while (rs.next()) {
                result.add(rs.getString(1));
            }

            logger.debug("Form data read: " + result);
        } catch (SQLException e) {
            throw new DataAccessException("Form data found: " + result, e);
        }

        return result;
    }
}

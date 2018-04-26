package by.khlebnikov.bartender.dao;

import by.khlebnikov.bartender.constant.ConstQueryUser;
import by.khlebnikov.bartender.constant.ConstTableUser;
import by.khlebnikov.bartender.constant.Constant;
import by.khlebnikov.bartender.entity.User;
import by.khlebnikov.bartender.exception.DataAccessException;
import by.khlebnikov.bartender.pool.ConnectionPool;
import by.khlebnikov.bartender.reader.PropertyReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.sql.rowset.serial.SerialBlob;
import java.sql.*;
import java.util.Optional;

/**
 * Class providing methods to access the database and retrieve information about users
 */
public class UserDao {

    // Constants ----------------------------------------------------------------------------------
    private static final String SAVE_QUERY = PropertyReader.getQueryProperty(ConstQueryUser.ADD);
    private static final String UPDATE_QUERY = PropertyReader.getQueryProperty(ConstQueryUser.UPDATE);
    private static final String IS_FAVOURITE_QUERY = PropertyReader.getQueryProperty(ConstQueryUser.IS_FAVOURITE);
    private static final String FIND_BY_COOKIE_QUERY = PropertyReader.getQueryProperty(ConstQueryUser.FIND_BY_COOKIE);
    private static final String FIND_BY_EMAIL_QUERY = PropertyReader.getQueryProperty(ConstQueryUser.FIND_BY_EMAIL);
    private static final String DELETE_QUERY = PropertyReader.getQueryProperty(ConstQueryUser.DELETE);

    // Actions ------------------------------------------------------------------------------------

    /**
     * Saves user to the database
     *
     * @param user user to save
     * @return true if the user has been saved successfully, false otherwise
     * @throws DataAccessException is thrown when a database error occurs
     */
    public boolean save(User user) throws DataAccessException {
        return executeUpdateUser(user, SAVE_QUERY);
    }

    /**
     * Updates user to the database
     *
     * @param user user to update
     * @return true if the user has been updated successfully, false otherwise
     * @throws DataAccessException is thrown when a database error occurs
     */
    public boolean update(User user) throws DataAccessException {
        return executeUpdateUser(user, UPDATE_QUERY);
    }

    /**
     * Finds user by cookie
     *
     * @param cookie persistent cookie, used to track long sessions (stay in the system option)
     * @return optional of User, or empty if not found
     * @throws DataAccessException is thrown when a database error occurs
     */
    public Optional<User> findByCookie(String cookie) throws DataAccessException {
        return find(cookie, FIND_BY_COOKIE_QUERY);
    }

    /**
     * Finds user by email
     *
     * @param email user's email
     * @return optional of User, or empty if not found
     * @throws DataAccessException is thrown when a database error occurs
     */
    public Optional<User> findByEmail(String email) throws DataAccessException {
        return find(email, FIND_BY_EMAIL_QUERY);
    }

    /**
     * Deletes a user from the database and all his data
     *
     * @param userId of the user
     * @return true if the operation succeeded and false otherwise
     * @throws DataAccessException is thrown when a database error occurs
     */
    public boolean delete(int userId) throws DataAccessException {
        try (Connection connection = ConnectionPool.getInstance().getConnection();
             PreparedStatement prepStatement = connection.prepareStatement(DELETE_QUERY)
        ) {
            prepStatement.setInt(1, userId);
            return prepStatement.executeUpdate() == Constant.EQUALS_1;
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }

    /**
     * Checks if a cocktail has been added by a user to his list of favourite cocktails
     *
     * @param userId     user's ID
     * @param cocktailId cocktail's ID
     * @return true if the cocktail is user's favourite, false otherwise
     * @throws DataAccessException is thrown when a database error occurs
     */
    public boolean isFavourite(int userId, int cocktailId) throws DataAccessException {
        try (Connection connection = ConnectionPool.getInstance().getConnection();
             PreparedStatement prepStatement = connection.prepareStatement(IS_FAVOURITE_QUERY)
        ) {
            prepStatement.setInt(1, cocktailId);
            prepStatement.setInt(2, userId);
            ResultSet resultSet = prepStatement.executeQuery();
            resultSet.next();

            return resultSet.getInt(1) == Constant.EQUALS_1;
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }

    // Helper methods -----------------------------------------------------------------------------

    /**
     * Finds a user by a search parameter according to the query
     *
     * @param searchParameter either email or cookie
     * @param query           chosen query
     * @return Optional of a user, or an empty one
     * @throws DataAccessException is thrown when a database error occurs
     */
    private Optional<User> find(String searchParameter, String query) throws DataAccessException {
        Optional<User> result = Optional.empty();
        User user = null;

        try (Connection connection = ConnectionPool.getInstance().getConnection();
             PreparedStatement prepStatement = connection.prepareStatement(query)
        ) {
            prepStatement.setString(1, searchParameter);
            ResultSet resSet = prepStatement.executeQuery();

            if (resSet.next()) {
                int dbID = resSet.getInt(ConstTableUser.ID);
                String dbName = resSet.getString(ConstTableUser.NAME);
                String dbEmail = resSet.getString(ConstTableUser.EMAIL);

                Blob hashBlob = resSet.getBlob(ConstTableUser.HASH);
                int hashLength = (int) hashBlob.length();
                byte[] dbHash = hashBlob.getBytes(1, hashLength);

                Blob saltBlob = resSet.getBlob(ConstTableUser.SALT);
                int saltLength = (int) saltBlob.length();
                byte[] dbSalt = saltBlob.getBytes(1, saltLength);

                user = new User(dbName, dbEmail, dbHash, dbSalt);
                user.setId(dbID);
                result = Optional.of(user);
            }

        } catch (SQLException e) {
            throw new DataAccessException("User found: " + user, e);
        }

        return result;
    }

    /**
     * Saves or updates a user
     *
     * @param user  user to save/update
     * @param query query to execute
     * @return true if the database has been updated successfully, false otherwise
     * @throws DataAccessException is thrown when a database error occurs
     */
    private boolean executeUpdateUser(User user, String query) throws DataAccessException {
        try (Connection connection = ConnectionPool.getInstance().getConnection();
             PreparedStatement prepStatement = connection.prepareStatement(query)
        ) {
            prepStatement.setString(1, user.getName());
            prepStatement.setBlob(2, new SerialBlob(user.getHashKey()));
            prepStatement.setBlob(3, new SerialBlob(user.getSalt()));
            prepStatement.setString(4, user.getUniqueCookie());
            prepStatement.setString(5, user.getEmail());

            return prepStatement.executeUpdate() == Constant.EQUALS_1;
        } catch (SQLException e) {
            throw new DataAccessException("User updated: " + user, e);
        }
    }

}
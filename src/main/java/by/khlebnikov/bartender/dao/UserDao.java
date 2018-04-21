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

public class UserDao {
    // Constants ----------------------------------------------------------------------------------
    private static final String SAVE_QUERY = PropertyReader.getQueryProperty(ConstQueryUser.ADD);
    private static final String UPDATE_QUERY = PropertyReader.getQueryProperty(ConstQueryUser.UPDATE);
    private static final String IS_FAVOURITE_QUERY = PropertyReader.getQueryProperty(ConstQueryUser.IS_FAVOURITE);
    private static final String FIND_BY_COOKIE_QUERY = PropertyReader.getQueryProperty(ConstQueryUser.FIND_BY_COOKIE);
    private static final String FIND_BY_EMAIL_QUERY = PropertyReader.getQueryProperty(ConstQueryUser.FIND_BY_EMAIL);

    // Vars ---------------------------------------------------------------------------------------
    private Logger logger = LogManager.getLogger();
    private ConnectionPool pool = ConnectionPool.getInstance();

    // Actions ------------------------------------------------------------------------------------
    public boolean save(User user) throws DataAccessException {
        return executeUpdateUser(user, SAVE_QUERY);
    }

    public boolean update(User user) throws DataAccessException {
        return executeUpdateUser(user, UPDATE_QUERY);
    }

    public Optional<User> findByCookie(String cookie) throws DataAccessException {
        return find(cookie, FIND_BY_COOKIE_QUERY);
    }

    public Optional<User> findByEmail(String email) throws DataAccessException {
        return find(email, FIND_BY_EMAIL_QUERY);
    }

    public boolean isFavourite(int userId, int cocktailId) throws DataAccessException {
        boolean result = false;

        try (Connection connection = pool.getConnection();
             PreparedStatement prepStatement = connection.prepareStatement(IS_FAVOURITE_QUERY)
        ) {
            prepStatement.setInt(1, cocktailId);
            prepStatement.setInt(2, userId);
            ResultSet resultSet = prepStatement.executeQuery();

            if (resultSet.next()) {
                int match = resultSet.getInt(1);
                result = match == Constant.EQUALS_1;
                logger.debug("result: " + result + " user " + userId + " likes cocktail " + cocktailId);
            }
        } catch (InterruptedException | SQLException e) {
            throw new DataAccessException("Is favourite: " + result, e);
        }

        return result;
    }

    // Helper methods -----------------------------------------------------------------------------
    private Optional<User> find(String searchParameter, String query) throws DataAccessException {
        Optional<User> result = Optional.empty();
        User user = null;

        try (Connection connection = pool.getConnection();
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

        } catch (SQLException | InterruptedException e) {
            throw new DataAccessException("User found: " + user, e);
        }

        return result;
    }

    private boolean executeUpdateUser(User user, String query) throws DataAccessException {
        boolean result;

        try (Connection connection = pool.getConnection();
             PreparedStatement prepStatement = connection.prepareStatement(query)
        ) {
            prepStatement.setString(1, user.getName());
            prepStatement.setBlob(2, new SerialBlob(user.getHashKey()));
            prepStatement.setBlob(3, new SerialBlob(user.getSalt()));
            prepStatement.setString(4, user.getUniqueCookie());
            prepStatement.setString(5, user.getEmail());
            int updated = prepStatement.executeUpdate();
            result = updated == Constant.EQUALS_1;
        } catch (InterruptedException | SQLException e) {
            throw new DataAccessException("User updated: " + user, e);
        }

        return result;
    }
}
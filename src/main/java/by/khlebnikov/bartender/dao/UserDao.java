package by.khlebnikov.bartender.dao;

import by.khlebnikov.bartender.constant.ConstQueryUser;
import by.khlebnikov.bartender.constant.ConstTableUser;
import by.khlebnikov.bartender.constant.Constant;
import by.khlebnikov.bartender.entity.User;
import by.khlebnikov.bartender.pool.ConnectionPool;
import by.khlebnikov.bartender.reader.PropertyReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.sql.rowset.serial.SerialBlob;
import java.sql.*;
import java.util.Optional;

public class UserDao {
    private Logger logger = LogManager.getLogger();
    private ConnectionPool pool = ConnectionPool.getInstance();

    public boolean save(User user) {
        boolean result = false;
        String query = PropertyReader.getQueryProperty(ConstQueryUser.ADD);

        try (Connection connection = pool.getConnection();
             PreparedStatement prepStatement = connection.prepareStatement(query)
        ) {
            prepStatement.setString(1, user.getName());
            prepStatement.setString(2, user.getEmail());
            prepStatement.setBlob(3, new SerialBlob(user.getHashKey()));
            prepStatement.setBlob(4, new SerialBlob(user.getSalt()));
            int updated = prepStatement.executeUpdate();
            result = updated == Constant.EQUALS_1;
        } catch (InterruptedException | SQLException e) {
            logger.catching(e);
        }

        return result;
    }

    public boolean update(User user) {
        boolean result = false;
        String query = PropertyReader.getQueryProperty(ConstQueryUser.UPDATE);

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
            logger.catching(e);
        }

        return result;
    }

    public Optional<User> findByEmail(String email) {
        String query = PropertyReader.getQueryProperty(ConstQueryUser.FIND_BY_EMAIL);
        return executeQueryUser(email, query);
    }

    public Optional<User> findByCookie(String cookie) {
        String query = PropertyReader.getQueryProperty(ConstQueryUser.FIND_BY_COOKIE);
        return executeQueryUser(cookie, query);
    }

    public boolean isFavourite(int userId, int cocktailId) {
        boolean result = false;
        String query = PropertyReader.getQueryProperty(ConstQueryUser.IS_FAVOURITE);

        logger.debug("isFavourite query: " + query);

        try (Connection connection = pool.getConnection();
             PreparedStatement prepStatement = connection.prepareStatement(query)
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
            logger.catching(e);
        }

        return result;
    }

    public boolean deleteFromFavourite(int userId, int cocktailId) {
        String query = PropertyReader.getQueryProperty(ConstQueryUser.DELETE_FAVOURITE);
        logger.debug("deleteFromFavourite query: " + query);
        return executeUpdateFavourite(userId, cocktailId, query);
    }

    public boolean saveFavourite(int userId, int cocktailId) {
        String query = PropertyReader.getQueryProperty(ConstQueryUser.SAVE_FAVOURITE);
        logger.debug("saveFavourite query: " + query);
        return executeUpdateFavourite(userId, cocktailId, query);
    }

    private boolean executeUpdateFavourite(int userId, int cocktailId, String query) {
        int updated = 0;
        try (Connection connection = pool.getConnection();
             PreparedStatement prepStatement = connection.prepareStatement(query)
        ) {
            prepStatement.setInt(1, cocktailId);
            prepStatement.setInt(2, userId);
            updated = prepStatement.executeUpdate();
        } catch (InterruptedException | SQLException e) {
            logger.catching(e);
        }
        return updated == Constant.EQUALS_1;
    }

    private Optional<User> executeQueryUser(String searchParameter, String query) {
        Optional<User> result = Optional.empty();

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

                User user = new User(dbName, dbEmail, dbHash, dbSalt);
                user.setId(dbID);
                result = Optional.of(user);
            }

        } catch (SQLException | InterruptedException e) {
            logger.catching(e);
        }

        return result;
    }
}
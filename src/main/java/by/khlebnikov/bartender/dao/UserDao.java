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
    private Logger logger = LogManager.getLogger();
    private ConnectionPool pool = ConnectionPool.getInstance();

    public boolean save(User user) throws DataAccessException {
        boolean result;
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
            throw new DataAccessException("User saved: " + user, e);
        }

        return result;
    }

    public Optional<User> find(String searchParameter, String query) throws DataAccessException {
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

    public boolean update(User user) throws DataAccessException {
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
            throw new DataAccessException("User updated: " + result, e);
        }

        return result;
    }

    public boolean isFavourite(int userId, int cocktailId) throws DataAccessException {
        boolean result = false;
        String query = PropertyReader.getQueryProperty(ConstQueryUser.IS_FAVOURITE);

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
            throw new DataAccessException("Is favourite: " + result, e);
        }

        return result;
    }

    /**
     * Works with favourite or created by user cocktails
     *
     * @param userId
     * @param cocktailId
     * @param query
     * @return
     */
    public boolean executeUpdateCocktail(int userId, int cocktailId, String query) throws DataAccessException {
        int updated = 0;
        try (Connection connection = pool.getConnection();
             PreparedStatement prepStatement = connection.prepareStatement(query)
        ) {
            prepStatement.setInt(1, cocktailId);
            prepStatement.setInt(2, userId);
            updated = prepStatement.executeUpdate();
        } catch (InterruptedException | SQLException e) {
            throw new DataAccessException("BD is updated: " + updated, e);
        }
        return updated == Constant.EQUALS_1;
    }
}
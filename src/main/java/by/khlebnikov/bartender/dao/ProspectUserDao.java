package by.khlebnikov.bartender.dao;

import by.khlebnikov.bartender.constant.ConstQueryProspect;
import by.khlebnikov.bartender.constant.ConstTableProspect;
import by.khlebnikov.bartender.constant.Constant;
import by.khlebnikov.bartender.entity.ProspectUser;
import by.khlebnikov.bartender.exception.DataAccessException;
import by.khlebnikov.bartender.pool.ConnectionPool;
import by.khlebnikov.bartender.reader.PropertyReader;

import javax.sql.rowset.serial.SerialBlob;
import java.sql.*;
import java.util.Optional;

/**
 * Class providing methods to access the database and retrieve information about prospect users
 */
public class ProspectUserDao {

    // Constants ----------------------------------------------------------------------------------
    private static final String SAVE_QUERY = PropertyReader.getQueryProperty(ConstQueryProspect.ADD);
    private static final String FIND_QUERY = PropertyReader.getQueryProperty(ConstQueryProspect.FIND);
    private static final String DELETE_QUERY = PropertyReader.getQueryProperty(ConstQueryProspect.DELETE);

    // Actions ------------------------------------------------------------------------------------

    /**
     * Saves prospect user to the database
     *
     * @param prospectUser user to save
     * @return true if the user has been saved successfully, otherwise false
     * @throws DataAccessException is thrown when a database error occurs
     */
    public boolean save(ProspectUser prospectUser) throws DataAccessException {
        boolean result;
        int updated = 0;

        try (Connection connection = ConnectionPool.getInstance().getConnection();
             PreparedStatement prepStatement = connection.prepareStatement(SAVE_QUERY)
        ) {
            prepStatement.setString(1, prospectUser.getName());
            prepStatement.setString(2, prospectUser.getEmail());
            prepStatement.setBlob(3, new SerialBlob(prospectUser.getHashKey()));
            prepStatement.setBlob(4, new SerialBlob(prospectUser.getSalt()));
            prepStatement.setLong(5, prospectUser.getExpiration());
            prepStatement.setLong(6, prospectUser.getCode());
            updated = prepStatement.executeUpdate();
            result = updated == Constant.EQUALS_1;
        } catch (SQLException e) {
            throw new DataAccessException("Database response: " + updated, e);
        }

        return result;
    }

    /**
     * Finds prospect user by his email
     *
     * @param email email of a prospect user
     * @return Optional of prospect user
     * @throws DataAccessException is thrown when a database error occurs
     */
    public Optional<ProspectUser> find(String email) throws DataAccessException {
        ProspectUser prospectUser = null;
        Optional<ProspectUser> result = Optional.empty();

        try (Connection connection = ConnectionPool.getInstance().getConnection();
             PreparedStatement prepStatement = connection.prepareStatement(FIND_QUERY)
        ) {
            prepStatement.setString(1, email);
            ResultSet rs = prepStatement.executeQuery();

            if (rs.next()) {
                String dbName = rs.getString(ConstTableProspect.NAME);
                String dbEmail = rs.getString(ConstTableProspect.EMAIL);

                Blob hashBlob = rs.getBlob(ConstTableProspect.HASH);
                int lengthHash = (int) hashBlob.length();
                byte[] dbHash = hashBlob.getBytes(1, lengthHash);

                Blob hashSalt = rs.getBlob(ConstTableProspect.SALT);
                int lengthSalt = (int) hashSalt.length();
                byte[] dbSalt = hashSalt.getBytes(1, lengthSalt);

                long dbExpiration = rs.getLong(ConstTableProspect.EXPIRATION);
                long dbCode = rs.getLong(ConstTableProspect.CODE);

                prospectUser = new ProspectUser(dbName, dbEmail, dbHash, dbSalt, dbExpiration, dbCode);
                result = Optional.of(prospectUser);
            }

        } catch (SQLException e) {
            throw new DataAccessException("Found prospect user: " + prospectUser, e);
        }

        return result;
    }

    /**
     * Deletes prospect user from the database
     *
     * @param email email of a prospect user
     * @return true if the user has been removed from the database successfully, false otherwise
     * @throws DataAccessException is thrown when a database error occurs
     */
    public boolean delete(String email) throws DataAccessException {
        try (Connection connection = ConnectionPool.getInstance().getConnection();
             PreparedStatement prepStatement = connection.prepareStatement(DELETE_QUERY)
        ) {
            prepStatement.setString(1, email);
            return prepStatement.executeUpdate() == Constant.EQUALS_1;
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }

}

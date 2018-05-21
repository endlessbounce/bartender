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
    private static final String FIND_BY_EMAIL = PropertyReader.getQueryProperty(ConstQueryProspect.FIND_BY_EMAIL);
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
        try (Connection connection = ConnectionPool.getInstance().getConnection();
             PreparedStatement prepStatement = connection.prepareStatement(SAVE_QUERY)
        ) {
            prepStatement.setString(1, prospectUser.getName());
            prepStatement.setString(2, prospectUser.getEmail());
            prepStatement.setBlob(3, new SerialBlob(prospectUser.getHashKey()));
            prepStatement.setBlob(4, new SerialBlob(prospectUser.getSalt()));
            prepStatement.setLong(5, prospectUser.getExpiration());
            prepStatement.setString(6, prospectUser.getCode());

            return prepStatement.executeUpdate() == Constant.EQUALS_1;
        } catch (SQLException | InterruptedException e) {
            throw new DataAccessException(e);
        }
    }

    /**
     * Finds a prospect by confirmation code, sent onto his email
     *
     * @param confirmationCode confirmation code
     * @return optional of Prospect User
     * @throws DataAccessException is thrown when a database error occurs
     */
    public Optional<ProspectUser> findByCode(String confirmationCode) throws DataAccessException {
        return find(confirmationCode, FIND_QUERY);
    }

    /**
     * Finds a prospect by his email
     *
     * @param email user's email
     * @return optional of Prospect User
     * @throws DataAccessException is thrown when a database error occurs
     */
    public Optional<ProspectUser> findByEmail(String email) throws DataAccessException {
        return find(email, FIND_BY_EMAIL);
    }

    /**
     * Deletes prospect user from the database
     *
     * @param confirmationCode confirmation code of the prospect user sent from email
     * @return true if the user has been removed from the database successfully, false otherwise
     * @throws DataAccessException is thrown when a database error occurs
     */
    public boolean delete(String confirmationCode) throws DataAccessException {
        try (Connection connection = ConnectionPool.getInstance().getConnection();
             PreparedStatement prepStatement = connection.prepareStatement(DELETE_QUERY)
        ) {
            prepStatement.setString(1, confirmationCode);
            return prepStatement.executeUpdate() == Constant.EQUALS_1;
        } catch (SQLException | InterruptedException e) {
            throw new DataAccessException(e);
        }
    }

    // Helpers ------------------------------------------------------------------------------------

    /**
     * Finds prospect user by his email
     *
     * @param param email or confirmation code
     * @param query find by email or confirmation code
     * @return Optional of prospect user
     * @throws DataAccessException is thrown when a database error occurs
     */
    private Optional<ProspectUser> find(String param, String query) throws DataAccessException {
        ProspectUser prospectUser = null;
        Optional<ProspectUser> result = Optional.empty();

        try (Connection connection = ConnectionPool.getInstance().getConnection();
             PreparedStatement prepStatement = connection.prepareStatement(query)
        ) {
            prepStatement.setString(1, param);
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
                String dbCode = rs.getString(ConstTableProspect.CODE);

                prospectUser = new ProspectUser(dbName, dbEmail, dbHash, dbSalt, dbExpiration, dbCode);
                result = Optional.of(prospectUser);
            }

        } catch (SQLException | InterruptedException e) {
            throw new DataAccessException("Found prospect user: " + prospectUser, e);
        }

        return result;
    }
}

package by.khlebnikov.bartender.dao;

import by.khlebnikov.bartender.constant.Constant;
import by.khlebnikov.bartender.entity.ProspectUser;
import by.khlebnikov.bartender.reader.PropertyReader;
import by.khlebnikov.bartender.pool.ConnectionPool;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.sql.rowset.serial.SerialBlob;
import java.sql.*;
import java.util.Optional;

public class ProspectUserDao {
    private Logger logger = LogManager.getLogger();
    private ConnectionPool pool = ConnectionPool.getInstance();

    public boolean save(ProspectUser prospectUser){
        boolean result = false;
        String query = PropertyReader.getQueryProperty(Constant.PROSPECT_DAO_ADD);

        try(Connection connection = pool.getConnection();
            PreparedStatement prepStatement = connection.prepareStatement(query)
        ){
            prepStatement.setString(1, prospectUser.getName());
            prepStatement.setString(2, prospectUser.getEmail());
            prepStatement.setBlob(3, new SerialBlob(prospectUser.getHashKey()));
            prepStatement.setBlob(4, new SerialBlob(prospectUser.getSalt()));
            prepStatement.setLong(5, prospectUser.getExpiration());
            prepStatement.setLong(6, prospectUser.getCode());
            int res = prepStatement.executeUpdate();

            if(res == 1){
                result = true;
            }
        } catch (SQLException | InterruptedException e) {
            logger.catching(e);
        }

        return result;
    }

    public Optional<ProspectUser> find(String email){
        Optional<ProspectUser> result = Optional.empty();
        String query = PropertyReader.getQueryProperty(Constant.PROSPECT_DAO_FIND);

        try(Connection connection = pool.getConnection();
            PreparedStatement prepStatement = connection.prepareStatement(query)
        ){
            prepStatement.setString(1, email);
            ResultSet rs = prepStatement.executeQuery();

            if(rs.next()){
                String dbName = rs.getString(Constant.DB_PROSPECT_NAME);
                String dbEmail = rs.getString(Constant.DB_PROSPECT_EMAIL);

                Blob hashBlob = rs.getBlob(Constant.DB_PROSPECT_HASH);
                int lentghHash = (int)hashBlob.length();
                byte[] dbHash = hashBlob.getBytes(1, lentghHash);

                Blob hashSalt = rs.getBlob(Constant.DB_PROSPECT_SALT);
                int lentghSalt = (int)hashSalt.length();
                byte[] dbSalt = hashSalt.getBytes(1, lentghSalt);

                long dbExpiration = rs.getLong(Constant.DB_PROSPECT_EXPIRATION);
                long dbCode = rs.getLong(Constant.DB_PROSPECT_CODE);
                result = Optional.of(new ProspectUser(dbName, dbEmail, dbHash, dbSalt, dbExpiration, dbCode));
            }

        } catch (SQLException | InterruptedException e) {
            logger.catching(e);
        }

        return result;
    }

    public boolean delete(String email) {
        boolean result = false;
        String query = PropertyReader.getQueryProperty(Constant.PROSPECT_DAO_DELETE);

        try(Connection connection = pool.getConnection();
            PreparedStatement prepStatement = connection.prepareStatement(query)
        ){
            prepStatement.setString(1, email);
            int res = prepStatement.executeUpdate();

            if(res == 1){
                result = true;
            }
        } catch (SQLException | InterruptedException e) {
            logger.catching(e);
        }

        return result;
    }
}

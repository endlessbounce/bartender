package by.khlebnikov.bartender.dao;

import by.khlebnikov.bartender.constant.Constant;
import by.khlebnikov.bartender.entity.ProspectUser;
import by.khlebnikov.bartender.reader.PropertyReader;
import by.khlebnikov.bartender.pool.ConnectionPool;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
            prepStatement.setString(3, prospectUser.getPassword());
            prepStatement.setLong(4, prospectUser.getExpiration());
            prepStatement.setLong(5, prospectUser.getCode());
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
                String dbPassword = rs.getString(Constant.DB_PROSPECT_PASSWORD);
                long dbExpiration = rs.getLong(Constant.DB_PROSPECT_EXPIRATION);
                long dbCode = rs.getLong(Constant.DB_PROSPECT_CODE);
                result = Optional.of(new ProspectUser(dbName, dbEmail, dbPassword, dbExpiration, dbCode));
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

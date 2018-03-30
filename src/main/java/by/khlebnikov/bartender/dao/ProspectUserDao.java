package by.khlebnikov.bartender.dao;

import by.khlebnikov.bartender.entity.ProspectUser;
import by.khlebnikov.bartender.pool.ConnectionPool;
import by.khlebnikov.bartender.pool.ProxyConnection;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.Properties;

public class ProspectUserDao {
    private Logger logger = LogManager.getLogger();
    private ConnectionPool pool = ConnectionPool.getInstance();
    private Properties properties;

    public ProspectUserDao(String queryPropertyPath) {
        properties = new Properties();
        try {
            properties.load(new FileInputStream(queryPropertyPath));
        } catch (IOException e) {
            logger.catching(e);
        }
    }

    public boolean save(ProspectUser prospectUser){
        boolean result = false;
        PreparedStatement prepStatement = null;
        Connection connection = null;

        try {
            connection = pool.getConnection();
            prepStatement = connection.prepareStatement(properties.getProperty("prospectdao.addprospect"));

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
        }  finally{
            pool.releaseConnection((ProxyConnection) connection);
            if (prepStatement != null) {
                try {
                    prepStatement.close();
                } catch (SQLException e) {
                    logger.catching(e);
                }
            }
        }
        return result;
    }

    public Optional<ProspectUser> find(String email){
        Optional<ProspectUser> result = Optional.empty();
        PreparedStatement prepStatement = null;
        Connection connection = null;

        try {
            connection = pool.getConnection();
            prepStatement = connection.prepareStatement(properties.getProperty("prospectdao.find"));
            prepStatement.setString(1, email);
            ResultSet rs = prepStatement.executeQuery();

            if(rs.next()){
                String dbName = rs.getString("pr_name");
                String dbEmail = rs.getString("pr_email");
                String dbPassword = rs.getString("pr_password");
                long dbExpiration = rs.getLong("pr_expiration");
                long dbCode = rs.getLong("pr_code");
                result = Optional.of(new ProspectUser(dbName, dbEmail, dbPassword, dbExpiration, dbCode));
            }

        } catch (SQLException | InterruptedException e) {
            logger.catching(e);
        }  finally{
            pool.releaseConnection((ProxyConnection) connection);
            if (prepStatement != null) {
                try {
                    prepStatement.close();
                } catch (SQLException e) {
                    logger.catching(e);
                }
            }
        }
        return result;
    }

    public boolean delete(String email) {
        boolean result = false;
        PreparedStatement prepStatement = null;
        Connection connection = null;

        try {
            connection = pool.getConnection();
            prepStatement = connection.prepareStatement(properties.getProperty("prospectdao.delete"));

            prepStatement.setString(1, email);
            int res = prepStatement.executeUpdate();

            if(res == 1){
                result = true;
            }
        } catch (SQLException | InterruptedException e) {
            logger.catching(e);
        }  finally{
            pool.releaseConnection((ProxyConnection) connection);
            if (prepStatement != null) {
                try {
                    prepStatement.close();
                } catch (SQLException e) {
                    logger.catching(e);
                }
            }
        }
        return result;
    }
}

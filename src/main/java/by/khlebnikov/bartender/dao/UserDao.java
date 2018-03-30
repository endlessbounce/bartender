package by.khlebnikov.bartender.dao;

import by.khlebnikov.bartender.entity.User;
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

public class UserDao {
    private Logger logger = LogManager.getLogger();
    private ConnectionPool pool = ConnectionPool.getInstance();
    private Properties properties;

    public UserDao(String queryPropertyPath) {
        properties = new Properties();
        try {
            properties.load(new FileInputStream(queryPropertyPath));
        } catch (IOException e) {
            logger.catching(e);
        }
    }

    public boolean save(User user) {
        boolean result = false;
        PreparedStatement ps = null;
        Connection con = null;
        //todo use try with resources
        //todo add transaction?
        try {
            con = pool.getConnection();
            ps = con.prepareStatement(properties.getProperty("userdao.adduser"));
            ps.setString(1, user.getName());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPasword());
            int res = ps.executeUpdate();

            if (res == 1) {
                result = true;
            }
        } catch (InterruptedException e) {
            logger.catching(e);
        } catch (SQLException e) {
            logger.catching(e);
        } finally {
            pool.releaseConnection((ProxyConnection) con);
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    logger.catching(e);
                }
            }
        }
        return result;
    }

    public Optional<User> find(String email){
        Optional<User> result = Optional.empty();
        PreparedStatement prepStatement = null;
        Connection connection = null;

        try {
            connection = pool.getConnection();
            prepStatement = connection.prepareStatement(properties.getProperty("userdao.find"));
            prepStatement.setString(1, email);
            ResultSet rs = prepStatement.executeQuery();

            if(rs.next()){
                String dbName = rs.getString("us_name");
                String dbEmail = rs.getString("us_email");
                String dbPassword = rs.getString("us_password");
                result = Optional.of(new User(dbName, dbEmail, dbPassword));
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

    public boolean delete(User account) {
        boolean result = false;
        return result;
    }
}

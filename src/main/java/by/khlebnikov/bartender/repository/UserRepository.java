package by.khlebnikov.bartender.repository;

import by.khlebnikov.bartender.constant.Constant;
import by.khlebnikov.bartender.entity.User;
import by.khlebnikov.bartender.pool.ConnectionPool;
import by.khlebnikov.bartender.reader.PropertyReader;
import by.khlebnikov.bartender.specification.UserSpecification;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class UserRepository {
    private Logger logger = LogManager.getLogger();
    private ConnectionPool pool = ConnectionPool.getInstance();

    public boolean save(User user) {
        boolean result = false;
        String query = PropertyReader.getQueryProperty(Constant.USER_REP_ADD);

        try(Connection connection = pool.getConnection();
            PreparedStatement prepStatement = connection.prepareStatement(query)
        ){
            prepStatement.setString(1, user.getName());
            prepStatement.setString(2, user.getEmail());
            prepStatement.setString(3, user.getPassword());
            int res = prepStatement.executeUpdate();

            if (res == 1) {
                result = true;
            }
        } catch (InterruptedException e) {
            logger.catching(e);
        } catch (SQLException e) {
            logger.catching(e);
        }

        return result;
    }

    public Optional<User> find(String email){
        Optional<User> result = Optional.empty();
        String query = PropertyReader.getQueryProperty(Constant.USER_REP_FIND);

        try(Connection connection = pool.getConnection();
            PreparedStatement prepStatement = connection.prepareStatement(query)
        ){
            prepStatement.setString(1, email);
            ResultSet rs = prepStatement.executeQuery();

            if(rs.next()){
                String dbName = rs.getString(Constant.DB_USER_NAME);
                String dbEmail = rs.getString(Constant.DB_USER_EMAIL);
                String dbPassword = rs.getString(Constant.DB_USER_PASSWORD);
                result = Optional.of(new User(dbName, dbEmail, dbPassword));
            }

        } catch (SQLException | InterruptedException e) {
            logger.catching(e);
        }

        return result;
    }

    public boolean update(User user) {
        boolean result = false;
        String query = PropertyReader.getQueryProperty(Constant.USER_REP_UPDATE);

        try(Connection connection = pool.getConnection();
            PreparedStatement prepStatement = connection.prepareStatement(query)
        ) {
            prepStatement.setString(1, user.getName());
            prepStatement.setString(2, user.getPassword());
            prepStatement.setString(3, user.getUniqueCookie());
            prepStatement.setString(4, user.getEmail());
            int res = prepStatement.executeUpdate();

            if (res == 1) {
                result = true;
            }
        } catch (InterruptedException e) {
            logger.catching(e);
        } catch (SQLException e) {
            logger.catching(e);
        }

        return result;
    }

    public boolean delete(User user) {
        boolean result = false;
        return result;
    }

    public List<User> query(UserSpecification spec){
        return spec.specified();
    }
}

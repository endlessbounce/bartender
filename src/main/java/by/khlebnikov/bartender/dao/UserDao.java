package by.khlebnikov.bartender.dao;

import by.khlebnikov.bartender.constant.ConstQueryUser;
import by.khlebnikov.bartender.constant.ConstTableUser;
import by.khlebnikov.bartender.entity.User;
import by.khlebnikov.bartender.pool.ConnectionPool;
import by.khlebnikov.bartender.reader.PropertyReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.sql.rowset.serial.SerialBlob;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserDao {
    private Logger logger = LogManager.getLogger();
    private ConnectionPool pool = ConnectionPool.getInstance();

    public boolean save(User user) {
        boolean result = false;
        String query = PropertyReader.getQueryProperty(ConstQueryUser.ADD);

        try(Connection connection = pool.getConnection();
            PreparedStatement prepStatement = connection.prepareStatement(query)
        ){
            prepStatement.setString(1, user.getName());
            prepStatement.setString(2, user.getEmail());
            prepStatement.setBlob(3, new SerialBlob(user.getHashKey()));
            prepStatement.setBlob(4, new SerialBlob(user.getSalt()));
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
        String query = PropertyReader.getQueryProperty(ConstQueryUser.FIND);

        try(Connection connection = pool.getConnection();
            PreparedStatement prepStatement = connection.prepareStatement(query)
        ){
            prepStatement.setString(1, email);
            ResultSet rs = prepStatement.executeQuery();

            if(rs.next()){
                String dbName = rs.getString(ConstTableUser.NAME);
                String dbEmail = rs.getString(ConstTableUser.EMAIL);

                Blob hashBlob = rs.getBlob(ConstTableUser.HASH);
                int hashLength = (int)hashBlob.length();
                byte [] dbHash = hashBlob.getBytes(1, hashLength);

                Blob saltBlob = rs.getBlob(ConstTableUser.SALT);
                int saltLength = (int)saltBlob.length();
                byte [] dbSalt = saltBlob.getBytes(1, saltLength);

                result = Optional.of(new User(dbName,
                        dbEmail,
                        dbHash,
                        dbSalt));
            }

        } catch (SQLException | InterruptedException e) {
            logger.catching(e);
        }

        return result;
    }

    public boolean update(User user) {
        boolean result = false;
        String query = PropertyReader.getQueryProperty(ConstQueryUser.UPDATE);

        try(Connection connection = pool.getConnection();
            PreparedStatement prepStatement = connection.prepareStatement(query)
        ) {
            prepStatement.setString(1, user.getName());
            prepStatement.setBlob(2, new SerialBlob(user.getHashKey()));
            prepStatement.setBlob(3, new SerialBlob(user.getSalt()));
            prepStatement.setString(4, user.getUniqueCookie());
            prepStatement.setString(5, user.getEmail());
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
        //TODO something here
    }

    public List<User> findByCookie(String cookie) {
        ArrayList<User> userList = new ArrayList<>();
        String query = PropertyReader.getQueryProperty(ConstQueryUser.BY_COOKIE);
        User user;

        try (Connection connection = pool.getConnection();
             PreparedStatement prepStatement = connection.prepareStatement(query)
        ) {
            prepStatement.setString(1, cookie);
            ResultSet resSet = prepStatement.executeQuery();

            while (resSet.next()) {
                String dbName = resSet.getString(ConstTableUser.NAME);
                String dbEmail = resSet.getString(ConstTableUser.EMAIL);

                Blob hashBlob = resSet.getBlob(ConstTableUser.HASH);
                int hashLength = (int) hashBlob.length();
                byte[] dbHash = hashBlob.getBytes(1, hashLength);

                Blob saltBlob = resSet.getBlob(ConstTableUser.SALT);
                int saltLength = (int) saltBlob.length();
                byte[] dbSalt = saltBlob.getBytes(1, saltLength);

                user = new User(dbName, dbEmail, dbHash, dbSalt);
                userList.add(user);
            }
        } catch (SQLException | InterruptedException e) {
            logger.catching(e);
        }
        return userList;
    }
}
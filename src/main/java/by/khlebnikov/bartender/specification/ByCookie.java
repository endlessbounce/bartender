package by.khlebnikov.bartender.specification;

import by.khlebnikov.bartender.constant.Constant;
import by.khlebnikov.bartender.entity.User;
import by.khlebnikov.bartender.reader.PropertyReader;
import by.khlebnikov.bartender.pool.ConnectionPool;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.sql.rowset.serial.SerialBlob;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ByCookie implements UserSpecification {
    private Logger logger = LogManager.getLogger();
    private ConnectionPool pool;
    private String query = PropertyReader.getQueryProperty(Constant.USER_REP_BY_COOKIE);
    private String cookie;

    public ByCookie(String cookie) {
        this.cookie = cookie;
        this.pool = ConnectionPool.getInstance();
    }

    @Override
    public List<User> specified() {
        ArrayList<User> userList = new ArrayList<>();
        User user;

        try (Connection connection = pool.getConnection();
             PreparedStatement prepStatement = connection.prepareStatement(query)
        ) {
            prepStatement.setString(1, cookie);

            try (ResultSet resSet = prepStatement.executeQuery()) {
                while (resSet.next()) {
                    String dbName = resSet.getString(Constant.DB_USER_NAME);
                    String dbEmail = resSet.getString(Constant.DB_USER_EMAIL);

                    Blob hashBlob = resSet.getBlob(Constant.DB_USER_HASH);
                    int hashLength = (int)hashBlob.length();
                    byte [] dbHash = hashBlob.getBytes(1, hashLength);

                    Blob saltBlob = resSet.getBlob(Constant.DB_USER_SALT);
                    int saltLength = (int)saltBlob.length();
                    byte [] dbSalt = saltBlob.getBytes(1, saltLength);

                    user = new User(dbName, dbEmail, dbHash, dbSalt);
                    userList.add(user);
                }
            }

        } catch (SQLException | InterruptedException e) {
            logger.catching(e);
        }
        return userList;
    }
}

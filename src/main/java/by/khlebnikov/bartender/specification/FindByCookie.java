package by.khlebnikov.bartender.specification;

import by.khlebnikov.bartender.constant.ConstQueryUser;
import by.khlebnikov.bartender.constant.ConstTableUser;
import by.khlebnikov.bartender.entity.User;
import by.khlebnikov.bartender.pool.ConnectionPool;
import by.khlebnikov.bartender.reader.PropertyReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FindByCookie implements UserSpecification {
    private Logger logger = LogManager.getLogger();
    private ConnectionPool pool;
    private String query = PropertyReader.getQueryProperty(ConstQueryUser.BY_COOKIE);
    private String cookie;

    public FindByCookie(String cookie) {
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

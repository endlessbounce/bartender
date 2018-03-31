package by.khlebnikov.bartender.specification;

import by.khlebnikov.bartender.constant.Constant;
import by.khlebnikov.bartender.entity.User;
import by.khlebnikov.bartender.reader.PropertyReader;
import by.khlebnikov.bartender.pool.ConnectionPool;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
                    user = new User(resSet.getString(Constant.DB_USER_NAME),
                                    resSet.getString(Constant.DB_USER_EMAIL),
                                    resSet.getString(Constant.DB_USER_PASSWORD));
                    userList.add(user);
                }
            }

        } catch (SQLException | InterruptedException e) {
            logger.catching(e);
        }
        return userList;
    }
}

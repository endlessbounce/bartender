package by.khlebnikov.bartender.repository;

import by.khlebnikov.bartender.entity.User;
import by.khlebnikov.bartender.pool.ConnectionPool;
import by.khlebnikov.bartender.specification.Specification;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserRepository {
    private static final String ADD_USER = "INSERT INTO `bartender`.`user`\n" +
            "(`us_name`,\n" +
            " `us_email`,\n" +
            " `us_password`)\n" +
            "VALUES\n" + " (?,?,?);";
    private static final String REMOVE_USER = "";
    private static final String UPDATE_USER = "";
    private ConnectionPool pool = ConnectionPool.getInstance();
    private Logger logger = LogManager.getLogger();

    public boolean addUser(String name, String email, String password){
        boolean result = false;
        try {
            Connection con = pool.getConnection();
            PreparedStatement ps = con.prepareStatement(ADD_USER);
            ps.setString(1, name);
            ps.setString(2, email);
            ps.setString(3, password);
            int res = ps.executeUpdate();

            if(res == 1){
                result = true;
            }
        } catch (InterruptedException e) {
            logger.catching(e);
        } catch (SQLException e) {
            logger.catching(e);
        }
        return result;
    }
    public boolean removeUser(User account){
        boolean result = false;
        return result;
    }
    public boolean updateUser(User account){
        boolean result = false;
        return result;
    }

    List<User> query(Specification specification){
        return new ArrayList<User>();
    }

}

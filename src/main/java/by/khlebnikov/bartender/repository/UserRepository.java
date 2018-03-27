package by.khlebnikov.bartender.repository;

import by.khlebnikov.bartender.entity.User;
import by.khlebnikov.bartender.pool.ConnectionPool;
import by.khlebnikov.bartender.specification.Specification;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class UserRepository {
    private static final String ADD_USER = "INSERT INTO user(us_name, us_, phone ) VALUES(?,?,?)";
    private static final String REMOVE_USER = "";
    private static final String UPDATE_USER = "";
    private ConnectionPool pool = ConnectionPool.getInstance();
    private Logger logger = LogManager.getLogger();

    void addUser(User account){
        try {
            Connection con = pool.getConnection();
        } catch (InterruptedException e) {
            logger.catching(e);
        }
    }
    void removeUser(User account){

    }
    void updateUser(User account){

    }

    List<User> query(Specification specification){
        return new ArrayList<User>();
    }

}

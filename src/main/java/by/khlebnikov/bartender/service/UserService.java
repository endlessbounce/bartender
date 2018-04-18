package by.khlebnikov.bartender.service;

import by.khlebnikov.bartender.constant.ConstQueryUser;
import by.khlebnikov.bartender.dao.ProspectUserDao;
import by.khlebnikov.bartender.dao.UserDao;
import by.khlebnikov.bartender.entity.ProspectUser;
import by.khlebnikov.bartender.entity.User;
import by.khlebnikov.bartender.reader.PropertyReader;
import by.khlebnikov.bartender.utility.Password;
import by.khlebnikov.bartender.utility.Utility;
import by.khlebnikov.bartender.validator.Validator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

public class UserService {
    private UserDao userDao;
    private ProspectUserDao prospectUserDao;
    private Password passwordGenerator;

    public UserService() {
        this.userDao = new UserDao();
        this.prospectUserDao = new ProspectUserDao();
        this.passwordGenerator = new Password();
    }

    public Optional<User> findUser(String email) {
        String query = PropertyReader.getQueryProperty(ConstQueryUser.FIND_BY_EMAIL);
        return userDao.find(email, query);
    }

    public boolean updateUser(User user){
        return userDao.update(user);
    }

    public Optional<User> checkUser(String email, String password) {
        String query = PropertyReader.getQueryProperty(ConstQueryUser.FIND_BY_EMAIL);
        Optional<User> userOpt = userDao.find(email, query);

        if(userOpt.isPresent()){
            User user = userOpt.get();
            boolean hashMatch = passwordGenerator.isExpectedPassword(password.toCharArray(),
                    user.getSalt(),
                    user.getHashKey());

            if(!hashMatch){
                userOpt = Optional.empty();
            }
        }

        return userOpt;
    }

    public Optional<User> findUserByCookie(String cookieId){
        String query = PropertyReader.getQueryProperty(ConstQueryUser.FIND_BY_COOKIE);
        return userDao.find(cookieId, query);
    }

    public boolean changingPasswordUser(String email, String confirmationCode) {
        String dbCode = "";

        if (!Validator.checkString(email) || !Validator.checkString(confirmationCode)) {
            return false;
        }

        Optional<ProspectUser> userOpt = prospectUserDao.find(email);

        if (userOpt.isPresent()) {
            ProspectUser user = userOpt.get();
            dbCode = String.valueOf(user.getCode());
        }

        return confirmationCode.equals(dbCode);
    }

    public boolean registerUser(User user) {
        return userDao.save(user);
    }

    public Optional<User> checkProspectUser(String email, String confirmationCode) {
        Optional<User> result = Optional.empty();

        if (!Validator.checkString(email) || !Validator.checkString(confirmationCode)) {
            return result;
        }

        Optional<ProspectUser> userOpt = prospectUserDao.find(email);

        /*check if user appealed for registration and registration time is not expired*/
        if (userOpt.isPresent()) {
            ProspectUser user = userOpt.get();
            String code = String.valueOf(user.getCode());
            long currentTime = Utility.currentTime();

            if(confirmationCode.equals(code) && user.getExpiration() > currentTime){
                result = Optional.of(new User(user.getName(),
                        user.getEmail(),
                        user.getHashKey(),
                        user.getSalt()));
            }
        }

        return result;
    }

    public boolean registerProspectUser(ProspectUser prospectUser) {
        return prospectUserDao.save(prospectUser);
    }

    public boolean deleteProspectUser(String email){
        return prospectUserDao.delete(email);
    }

    public boolean isProspectRegistered(String email){
        Optional<ProspectUser> userOpt = prospectUserDao.find(email);
        return userOpt.isPresent();
    }

    public boolean isFavouriteCocktail(int userId, int cocktailId){
        return userDao.isFavourite(userId, cocktailId);
    }

    public boolean deleteFavourite(int userId, int cocktailId){
        String query = PropertyReader.getQueryProperty(ConstQueryUser.DELETE_FAVOURITE);
        return userDao.executeUpdateCocktail(userId, cocktailId, query);
    }

    public boolean addFavourite(int userId, int cocktailId){
        String query = PropertyReader.getQueryProperty(ConstQueryUser.SAVE_FAVOURITE);
        return userDao.executeUpdateCocktail(userId, cocktailId, query);
    }

    public boolean deleteCreated(int userId, int cocktailId) {
        String query = PropertyReader.getQueryProperty(ConstQueryUser.DELETE_CREATED);
        return userDao.executeUpdateCocktail(userId, cocktailId, query);
    }
}

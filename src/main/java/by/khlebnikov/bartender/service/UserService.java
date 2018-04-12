package by.khlebnikov.bartender.service;

import by.khlebnikov.bartender.dao.ProspectUserDao;
import by.khlebnikov.bartender.entity.ProspectUser;
import by.khlebnikov.bartender.entity.User;
import by.khlebnikov.bartender.dao.UserDao;
import by.khlebnikov.bartender.utility.Password;
import by.khlebnikov.bartender.utility.Utility;
import by.khlebnikov.bartender.validator.Validator;

import java.util.List;
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
        return userDao.findByEmail(email);
    }

    public Optional<User> checkUser(String email, String password) {
        Optional<User> userOpt = userDao.findByEmail(email);

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

    public List<User> findUserByCookie(String cookieId){
        return userDao.findByCookie(cookieId);
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

    public boolean registerProspectUser(ProspectUser prospectUser) {
        return prospectUserDao.save(prospectUser);
    }

    public boolean isUserRegistered(String email){
        Optional<User> userOpt = userDao.findByEmail(email);
        return userOpt.isPresent();
    }

    public boolean isProspectRegistered(String email){
        Optional<ProspectUser> userOpt = prospectUserDao.find(email);
        return userOpt.isPresent();
    }

    public boolean isFavouriteCocktail(int userId, int cocktailId){
        return userDao.isFavourite(userId, cocktailId);
    }

    public boolean deleteFromFavourite(int userId, int cocktailId){
        return userDao.isFavourite(userId, cocktailId);
    }

    public boolean deleteProspectUser(String email){
        return prospectUserDao.delete(email);
    }

    public boolean updateUser(User user){
        return userDao.update(user);
    }
}

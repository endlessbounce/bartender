package by.khlebnikov.bartender.logic;

import by.khlebnikov.bartender.dao.ProspectUserDao;
import by.khlebnikov.bartender.entity.ProspectUser;
import by.khlebnikov.bartender.dao.UserDao;
import by.khlebnikov.bartender.entity.User;
import by.khlebnikov.bartender.utility.ApplicationUtility;
import by.khlebnikov.bartender.validator.Validator;

import java.util.Optional;

public class UserService {
    private UserDao userDao;
    private ProspectUserDao prospectUserDao;

    public UserService(String queryPropertyPath) {
        this.userDao = new UserDao(queryPropertyPath);
        this.prospectUserDao = new ProspectUserDao(queryPropertyPath);
    }

    public Optional<User> findUser(String email) {
        return userDao.find(email);
    }

    public Optional<User> checkUser(String email, String password) {
        Optional<User> userOpt = userDao.find(email);

        if(userOpt.isPresent()){
            User user = userOpt.get();
            boolean correctPassword = user.getPasword().equals(password);

            if(!correctPassword){
                userOpt = Optional.empty();
            }
        }

        return userOpt;
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
            long currentTime = ApplicationUtility.currentTime();

            if(confirmationCode.equals(code) && user.getExpiration() > currentTime){
                result = Optional.of(new User(user.getName(), user.getEmail(), user.getPassword()));
            }
        }

        return result;
    }

    public boolean registerUser(User user) {
        return userDao.save(user);
    }

    public boolean registerProspectUser(ProspectUser prospectUser) {
        return prospectUserDao.save(prospectUser);
    }

    public boolean isUserRegistered(String email){
        Optional<User> userOpt = userDao.find(email);
        return userOpt.isPresent();
    }

    public boolean isProspectRegistered(String email){
        Optional<ProspectUser> userOpt = prospectUserDao.find(email);
        return userOpt.isPresent();
    }

    public boolean deleteProspectUser(String email){
        return prospectUserDao.delete(email);
    }

}

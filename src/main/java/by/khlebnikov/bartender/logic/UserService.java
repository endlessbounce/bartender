package by.khlebnikov.bartender.logic;

import by.khlebnikov.bartender.dao.ProspectUserDao;
import by.khlebnikov.bartender.entity.ProspectUser;
import by.khlebnikov.bartender.entity.User;
import by.khlebnikov.bartender.repository.UserRepository;
import by.khlebnikov.bartender.specification.ByCookie;
import by.khlebnikov.bartender.utility.Password;
import by.khlebnikov.bartender.utility.Utility;
import by.khlebnikov.bartender.validator.Validator;

import java.util.List;
import java.util.Optional;

public class UserService {
    private UserRepository userRepository;
    private ProspectUserDao prospectUserDao;
    private Password passwordGenerator;

    public UserService() {
        this.userRepository = new UserRepository();
        this.prospectUserDao = new ProspectUserDao();
        this.passwordGenerator = new Password();
    }

    public Optional<User> findUser(String email) {
        return userRepository.find(email);
    }

    public Optional<User> checkUser(String email, String password) {
        Optional<User> userOpt = userRepository.find(email);

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
        return userRepository.query(new ByCookie(cookieId));
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
        return userRepository.save(user);
    }

    public boolean registerProspectUser(ProspectUser prospectUser) {
        return prospectUserDao.save(prospectUser);
    }

    public boolean isUserRegistered(String email){
        Optional<User> userOpt = userRepository.find(email);
        return userOpt.isPresent();
    }

    public boolean isProspectRegistered(String email){
        Optional<ProspectUser> userOpt = prospectUserDao.find(email);
        return userOpt.isPresent();
    }

    public boolean deleteProspectUser(String email){
        return prospectUserDao.delete(email);
    }

    public boolean updateUser(User user){
        return userRepository.update(user);
    }
}

package by.khlebnikov.bartender.service;

import by.khlebnikov.bartender.dao.ProspectUserDao;
import by.khlebnikov.bartender.dao.UserDao;
import by.khlebnikov.bartender.entity.ProspectUser;
import by.khlebnikov.bartender.entity.User;
import by.khlebnikov.bartender.exception.DataAccessException;
import by.khlebnikov.bartender.exception.ServiceException;
import by.khlebnikov.bartender.utility.Password;
import by.khlebnikov.bartender.utility.Utility;
import by.khlebnikov.bartender.validator.Validator;

import java.util.Optional;

public class UserService {
    // Vars ---------------------------------------------------------------------------------------
    private UserDao userDao;
    private ProspectUserDao prospectUserDao;
    private Password passwordGenerator;

    // Constructors -------------------------------------------------------------------------------
    public UserService() {
        this.userDao = new UserDao();
        this.prospectUserDao = new ProspectUserDao();
        this.passwordGenerator = new Password();
    }

    // Actions ------------------------------------------------------------------------------------
    public Optional<User> findUser(String email) throws ServiceException {
        Optional<User> userOptional;

        try {
            userOptional = userDao.findByEmail(email);
        } catch (DataAccessException e) {
            throw new ServiceException("Email: " + email, e);
        }

        return userOptional;
    }

    public boolean updateUser(User user) throws ServiceException {
        boolean result;

        try {
            result = userDao.update(user);
        } catch (DataAccessException e) {
            throw new ServiceException("Updating user: " + user, e);
        }

        return result;
    }

    public Optional<User> checkUser(String email, String password) throws ServiceException {
        Optional<User> userOpt;

        try {
            userOpt = userDao.findByEmail(email);
        } catch (DataAccessException e) {
            throw new ServiceException("Looking for user with: " + email + ",\n password: " + password, e);
        }

        if (userOpt.isPresent()) {
            User user = userOpt.get();
            boolean hashMatch = passwordGenerator.isExpectedPassword(password.toCharArray(),
                    user.getSalt(),
                    user.getHashKey());

            if (!hashMatch) {
                userOpt = Optional.empty();
            }
        }

        return userOpt;
    }

    public Optional<User> findUserByCookie(String cookieId) throws ServiceException {
        Optional<User> userOptional;

        try {
            userOptional = userDao.findByCookie(cookieId);
        } catch (DataAccessException e) {
            throw new ServiceException("Cookie: " + cookieId, e);
        }

        return userOptional;
    }

    public boolean changingPasswordUser(String email, String confirmationCode) throws ServiceException {
        String dbCode = "";
        Optional<ProspectUser> userOpt;

        if (!Validator.checkString(email) || !Validator.checkString(confirmationCode)) {
            return false;
        }

        try {
            userOpt = prospectUserDao.find(email);
        } catch (DataAccessException e) {
            throw new ServiceException("Checked email: " + email + ",\n confirmation code: " + confirmationCode, e);
        }

        if (userOpt.isPresent()) {
            ProspectUser user = userOpt.get();
            dbCode = String.valueOf(user.getCode());
        }

        return confirmationCode.equals(dbCode);
    }

    public boolean saveUser(User user) throws ServiceException {
        boolean result;

        try {
            result = userDao.save(user);
        } catch (DataAccessException e) {
            throw new ServiceException("Saving user: " + user, e);
        }

        return result;
    }

    public boolean saveProspectUser(ProspectUser prospectUser) throws ServiceException {
        boolean result;

        try {
            result = prospectUserDao.save(prospectUser);
        } catch (DataAccessException e) {
            throw new ServiceException("Saving prospect user: " + prospectUser, e);
        }

        return result;
    }

    public Optional<User> checkProspectUser(String email, String confirmationCode) throws ServiceException {
        Optional<User> result = Optional.empty();
        Optional<ProspectUser> userOpt;

        if (!Validator.checkString(email) || !Validator.checkString(confirmationCode)) {
            return result;
        }
        try {
            userOpt = prospectUserDao.find(email);
        } catch (DataAccessException e) {
            throw new ServiceException("Checked email: " + email + ",\n confirmation code: " + confirmationCode, e);
        }

        /*check if user appealed for registration and registration time is not expired*/
        if (userOpt.isPresent()) {
            ProspectUser user = userOpt.get();
            String code = String.valueOf(user.getCode());
            long currentTime = Utility.currentTime();

            if (confirmationCode.equals(code) && user.getExpiration() > currentTime) {
                result = Optional.of(new User(user.getName(),
                        user.getEmail(),
                        user.getHashKey(),
                        user.getSalt()));
            }
        }

        return result;
    }

    public boolean deleteProspectUser(String email) throws ServiceException {
        boolean result;

        try {
            result = prospectUserDao.delete(email);
        } catch (DataAccessException e) {
            throw new ServiceException("Deleting by email: " + email, e);
        }

        return result;
    }

    public boolean isProspectRegistered(String email) throws ServiceException {
        Optional<ProspectUser> userOpt;

        try {
            userOpt = prospectUserDao.find(email);
        } catch (DataAccessException e) {
            throw new ServiceException("Checking by email: " + email, e);
        }

        return userOpt.isPresent();
    }

    public boolean isFavouriteCocktail(int userId, int cocktailId) throws ServiceException {
        boolean result;

        try {
            result = userDao.isFavourite(userId, cocktailId);
        } catch (DataAccessException e) {
            throw new ServiceException("User id: " + userId + ",\n cocktail id" + cocktailId, e);
        }

        return result;
    }
}

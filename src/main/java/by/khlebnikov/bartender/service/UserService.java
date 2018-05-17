package by.khlebnikov.bartender.service;

import by.khlebnikov.bartender.dao.ProspectUserDao;
import by.khlebnikov.bartender.dao.UserDao;
import by.khlebnikov.bartender.entity.ProspectUser;
import by.khlebnikov.bartender.entity.User;
import by.khlebnikov.bartender.exception.DataAccessException;
import by.khlebnikov.bartender.exception.ServiceException;
import by.khlebnikov.bartender.utility.HashCoder;
import by.khlebnikov.bartender.validator.Validator;

import java.util.Optional;

/**
 * Class representing service layer for User and Prospect User models.
 */
public class UserService {

    // Vars ---------------------------------------------------------------------------------------
    private UserDao userDao;
    private ProspectUserDao prospectUserDao;
    private HashCoder hashCoder;

    // Constructors -------------------------------------------------------------------------------
    public UserService() {
        this.userDao = new UserDao();
        this.prospectUserDao = new ProspectUserDao();
        this.hashCoder = new HashCoder();
    }

    // Actions ------------------------------------------------------------------------------------

    /**
     * Finds user by email
     *
     * @param email of a user
     * @return Optional of a User if he has been found, empty Optional otherwise
     * @throws ServiceException is thrown in case of an error in the underlying code
     */
    public Optional<User> findUserByEmail(String email) throws ServiceException {
        try {
            return userDao.findByEmail(email);
        } catch (DataAccessException e) {
            throw new ServiceException("Email: " + email, e);
        }
    }

    /**
     * Finds a user by persistent (long-session) cookie
     *
     * @param cookieId persistent cookie's ID
     * @return Optional of a User if he has been found, empty Optional otherwise
     * @throws ServiceException is thrown in case of an error in the underlying code
     */
    public Optional<User> findUserByCookie(String cookieId) throws ServiceException {
        try {
            return userDao.findByCookie(cookieId);
        } catch (DataAccessException e) {
            throw new ServiceException("Cookie: " + cookieId, e);
        }
    }

    /**
     * Finds a user by ID
     *
     * @param userId user's ID
     * @return Optional of a User if he has been found, empty Optional otherwise
     * @throws ServiceException is thrown in case of an error in the underlying code
     */
    public Optional<User> findUserById(String userId) throws ServiceException {
        try {
            return userDao.findById(userId);
        } catch (DataAccessException e) {
            throw new ServiceException("User id: " + userId, e);
        }
    }

    /**
     * Checks if this prospect user has appealed for registration
     *
     * @param confirmationCode confirmation code that has been send to this prospect user via email
     * @return Optional of a User if the prospect was trying to register, empty Optional otherwise
     * @throws ServiceException is thrown in case of an error in the underlying code
     */
    public Optional<ProspectUser> findProspectByCode(String confirmationCode) throws ServiceException {
        try {
            return prospectUserDao.findByCode(confirmationCode);
        } catch (DataAccessException e) {
            throw new ServiceException("Confirmation code: " + confirmationCode, e);
        }
    }

    /**
     * Updates a user
     *
     * @param user to update
     * @return true if the user has been updated successfully, false otherwise
     * @throws ServiceException is thrown in case of an error in the underlying code
     */
    public boolean updateUser(User user) throws ServiceException {
        try {
            return userDao.update(user);
        } catch (DataAccessException e) {
            throw new ServiceException("Updating user: " + user, e);
        }
    }

    /**
     * Checks if a user is registered and if submitted data matches to the data from the database
     *
     * @param email    email of the user
     * @param password password of the user
     * @return Optional of a User if he is registered, empty Optional otherwise
     * @throws ServiceException is thrown in case of an error in the underlying code
     */
    public Optional<User> checkUser(String email, String password) throws ServiceException {
        Optional<User> userOpt;

        try {
            userOpt = userDao.findByEmail(email);
        } catch (DataAccessException e) {
            throw new ServiceException("Looking for user with: " + email + ",\n password: " + password, e);
        }

        if (userOpt.isPresent()) {
            User user = userOpt.get();
            boolean hashMatch = hashCoder.isExpectedPassword(password.toCharArray(),
                    user.getSalt(),
                    user.getHashKey());

            if (!hashMatch) {
                userOpt = Optional.empty();
            }
        }

        return userOpt;
    }

    /**
     * Saves user to the database
     *
     * @param user to save to the database
     * @return true if the user has been saved to the database successfully, false otherwise
     * @throws ServiceException is thrown in case of an error in the underlying code
     */
    public boolean saveUser(User user) throws ServiceException {
        try {
            return userDao.save(user);
        } catch (DataAccessException e) {
            throw new ServiceException("Saving user: " + user, e);
        }
    }

    /**
     * Saves prospect user to the database
     *
     * @param prospectUser to save to the database
     * @return true if the prospect user has been saved to the database successfully, false otherwise
     * @throws ServiceException is thrown in case of an error in the underlying code
     */
    public boolean saveProspectUser(ProspectUser prospectUser) throws ServiceException {
        try {
            return prospectUserDao.save(prospectUser);
        } catch (DataAccessException e) {
            throw new ServiceException("Saving prospect user: " + prospectUser, e);
        }
    }

    /**
     * Deletes prospect user from the database
     *
     * @param confirmationCode confirmation code of the prospect user sent from email
     * @return true if the prospect has been deleted successfully, false otherwise
     * @throws ServiceException is thrown in case of an error in the underlying code
     */
    public boolean deleteProspectUser(String confirmationCode) throws ServiceException {
        try {
            return prospectUserDao.delete(confirmationCode);
        } catch (DataAccessException e) {
            throw new ServiceException("Deleting by confirmation code: " + confirmationCode, e);
        }
    }

    /**
     * Deletes a user from the database and all his data
     *
     * @param userId of the user
     * @return true if the operation succeeded and false otherwise
     * @throws ServiceException is thrown in case of an error in the underlying code
     */
    public boolean deleteUser(int userId) throws ServiceException {
        try {
            return userDao.delete(userId);
        } catch (DataAccessException e) {
            throw new ServiceException("Deleting user id: " + userId, e);
        }
    }

    /**
     * Checks if such prospect user is registered
     *
     * @param email prospect's email
     * @return true if the prospect user is registered, false otherwise
     * @throws ServiceException is thrown in case of an error in the underlying code
     */
    public boolean isProspectRegistered(String email) throws ServiceException {
        try {
            return prospectUserDao.findByEmail(email).isPresent();
        } catch (DataAccessException e) {
            throw new ServiceException("Checking prospect by email: " + email, e);
        }
    }

    /**
     * Checks if this cocktail is in the list of the favourite ones of the user
     *
     * @param userId     user's ID
     * @param cocktailId cocktail's ID
     * @return true if the cocktail is favourite, false otherwise
     * @throws ServiceException is thrown in case of an error in the underlying code
     */
    public boolean isFavouriteCocktail(int userId, int cocktailId) throws ServiceException {
        try {
            return userDao.isFavourite(userId, cocktailId);
        } catch (DataAccessException e) {
            throw new ServiceException("User id: " + userId + ",\n cocktail id" + cocktailId, e);
        }
    }
}

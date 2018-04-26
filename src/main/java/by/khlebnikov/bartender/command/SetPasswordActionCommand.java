package by.khlebnikov.bartender.command;

import by.khlebnikov.bartender.constant.ConstAttribute;
import by.khlebnikov.bartender.constant.ConstPage;
import by.khlebnikov.bartender.constant.ConstParameter;
import by.khlebnikov.bartender.entity.User;
import by.khlebnikov.bartender.exception.CommandException;
import by.khlebnikov.bartender.exception.ServiceException;
import by.khlebnikov.bartender.service.UserService;
import by.khlebnikov.bartender.tag.MessageType;
import by.khlebnikov.bartender.utility.HashCoder;
import by.khlebnikov.bartender.validator.Validator;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

/**
 * Class to reset users' passwords (update records in the database)
 */
public class SetPasswordActionCommand implements Command {

    // Vars ---------------------------------------------------------------------------------------
    private UserService service;
    private HashCoder hashCoder;

    // Constructors -------------------------------------------------------------------------------
    public SetPasswordActionCommand() {
        this.service = new UserService();
        this.hashCoder = new HashCoder();
    }

    // Actions ------------------------------------------------------------------------------------

    /**
     * Checks if a new password and its confirmation match. If so,
     * updates the user's record in the database. Returns result page with a message
     *
     * @param request HttpServletRequest request
     * @return the result page with a message
     * @throws CommandException is thrown if an exception on lower levels happens
     */
    @Override
    public String execute(HttpServletRequest request) throws CommandException {
        String page = ConstPage.RESULT;
        String email = (String) request.getSession().getAttribute(ConstParameter.EMAIL);
        String password = request.getParameter(ConstParameter.PASSWORD);
        String confirmation = request.getParameter(ConstParameter.CONFIRMATION);
        boolean correctInput = Validator.checkRegistrationData(ConstParameter.USER, email, password, confirmation, request);
        User user = null;

        try {
            Optional<User> userOpt = service.findUserByEmail(email);

            if (correctInput && userOpt.isPresent()) {
                byte[] salt = hashCoder.getNextSalt();
                Optional<byte[]> hashOpt = hashCoder.hash(password.toCharArray(), salt);

                if (hashOpt.isPresent()) {
                    byte[] hashKey = hashOpt.get();

                    /*update hash key and salt*/
                    user = userOpt.get();
                    user.setHashKey(hashKey);
                    user.setSalt(salt);
                    service.updateUser(user);

                    request.getSession().removeAttribute(ConstParameter.EMAIL);
                    request.setAttribute(ConstAttribute.MESSAGE_TYPE, MessageType.PASSWORD_CHANGED);
                } else {
                    request.setAttribute(ConstAttribute.MESSAGE_TYPE, MessageType.HASH_ERROR);
                }

            } else {
                page = ConstPage.SET_PASSWORD;
                request.setAttribute(ConstParameter.PASSWORD, password);
                request.setAttribute(ConstParameter.CONFIRMATION, confirmation);

                if (!userOpt.isPresent()) {
                    request.setAttribute(ConstAttribute.MESSAGE_TYPE, MessageType.USER_NOT_REGISTERED);
                }
            }
        } catch (ServiceException e) {
            throw new CommandException("Updating user: " + user, e);
        }

        return page;
    }
}

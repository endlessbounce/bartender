package by.khlebnikov.bartender.command;

import by.khlebnikov.bartender.constant.ConstAttribute;
import by.khlebnikov.bartender.constant.ConstParameter;
import by.khlebnikov.bartender.constant.ConstPage;
import by.khlebnikov.bartender.entity.User;
import by.khlebnikov.bartender.service.UserService;
import by.khlebnikov.bartender.reader.PropertyReader;
import by.khlebnikov.bartender.tag.MessageType;
import by.khlebnikov.bartender.utility.Password;
import by.khlebnikov.bartender.validator.Validator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

/**
 * Checks if new password and its confirmation match,
 * then updates user's record in the database.
 * Returns result page with result message
 */
public class SetPasswordActionCommand implements Command {
    private Logger logger = LogManager.getLogger();
    private UserService service;
    private Password passwordGenerator;

    public SetPasswordActionCommand() {
        this.service = new UserService();
        this.passwordGenerator = new Password();
    }

    @Override
    public String execute(HttpServletRequest request) {
        String page = PropertyReader.getConfigProperty(ConstPage.RESULT);
        String email = (String) request.getSession().getAttribute(ConstParameter.EMAIL);
        String password = request.getParameter(ConstParameter.PASSWORD);
        String confirmation = request.getParameter(ConstParameter.CONFIRMATION);

        boolean correctInput = Validator.checkRegistrationData(ConstParameter.USER, email, password, confirmation, request);
        Optional<User> userOpt = service.findUser(email);

        if (correctInput && userOpt.isPresent()) {
            byte [] salt = passwordGenerator.getNextSalt();
            Optional<byte []> hashOpt = passwordGenerator.hash(password.toCharArray(), salt);

            if(!hashOpt.isPresent()){
                request.setAttribute(ConstAttribute.MESSAGE_TYPE, MessageType.HASH_ERROR);
                return page;
            }

            byte [] hashKey = hashOpt.get();

            /*update hash key and salt*/
            User user = userOpt.get();
            user.setHashKey(hashKey);
            user.setSalt(salt);
            service.updateUser(user);

            request.getSession().removeAttribute(ConstParameter.EMAIL);
            request.setAttribute(ConstAttribute.MESSAGE_TYPE, MessageType.PASSWORD_CHANGED);

        } else {
            page = PropertyReader.getConfigProperty(ConstPage.SET_PASSWORD);
            request.setAttribute(ConstParameter.PASSWORD, password);
            request.setAttribute(ConstParameter.CONFIRMATION, confirmation);

            if(!userOpt.isPresent()){
                request.setAttribute(ConstAttribute.MESSAGE_TYPE, MessageType.USER_NOT_REGISTERED);
            }
        }

        return page;
    }
}

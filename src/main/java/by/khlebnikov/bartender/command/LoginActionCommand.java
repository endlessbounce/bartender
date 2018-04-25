package by.khlebnikov.bartender.command;

import by.khlebnikov.bartender.constant.ConstAttribute;
import by.khlebnikov.bartender.constant.ConstPage;
import by.khlebnikov.bartender.constant.ConstParameter;
import by.khlebnikov.bartender.constant.Constant;
import by.khlebnikov.bartender.entity.User;
import by.khlebnikov.bartender.exception.CommandException;
import by.khlebnikov.bartender.exception.ServiceException;
import by.khlebnikov.bartender.service.UserService;
import by.khlebnikov.bartender.tag.MessageType;
import by.khlebnikov.bartender.utility.CodeGenerator;
import by.khlebnikov.bartender.utility.CookieHandler;
import by.khlebnikov.bartender.validator.Validator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

/**
 * Class logging in the user
 */
public class LoginActionCommand implements Command, CommandWithResponse {

    // Vars ---------------------------------------------------------------------------------------
    private static Logger logger = LogManager.getLogger();
    private UserService service;
    private HttpServletResponse response;

    // Constructors -------------------------------------------------------------------------------
    public LoginActionCommand() {
        this.service = new UserService();
    }

    // Actions ------------------------------------------------------------------------------------

    /**
     * Receives login data submitted by user and processes it. If such user is registered, it
     * logs him in, otherwise displays an error message.
     *
     * @param request HttpServletRequest request
     * @return the home page in case of successful login, or login page in case of failure
     * @throws CommandException is thrown if an exception on lower levels happens
     */
    @Override
    public String execute(HttpServletRequest request) throws CommandException {
        String email = request.getParameter(ConstParameter.EMAIL);
        String password = request.getParameter(ConstParameter.PASSWORD);
        String logged = request.getParameter(ConstParameter.STAY_LOGGED);
        boolean correctInput = Validator.checkLoginData(email, password, request);
        User user = null;

        logger.debug("stay logged option is chosen: " + logged);

        if (correctInput) {

            try {
                Optional<User> userOpt = service.checkUser(email, password);

                if (userOpt.isPresent()) {
                    user = userOpt.get();
                    request.getSession().setAttribute(ConstAttribute.USER_NAME, user.getName());
                    request.getSession().setAttribute(ConstAttribute.USER_ID, user.getId());

                    //checking stay logged in checkbox
                    if (ConstParameter.TRUE.equals(logged)) {
                        String uid = CodeGenerator.uniqueId();
                        user.setUniqueCookie(uid);
                        service.updateUser(user);

                        Cookie cookie = CookieHandler.persistingCookie(uid);
                        response.addCookie(cookie);

                        Cookie oldSession = new Cookie(Constant.OLD_SESSION, ConstParameter.TRUE);
                        oldSession.setMaxAge(-1);
                        response.addCookie(oldSession);

                        logger.debug("singed in: " + uid);
                    }

                    return ConstPage.HOME;
                } else {
                    request.setAttribute(ConstAttribute.MESSAGE_TYPE, MessageType.INCORRECT_EMAIL_OR_PASSWORD);
                }
            } catch (ServiceException e) {
                throw new CommandException("Stay logged chosen: " + logged + ",\n user to update: " + user, e);
            }
        }

        request.setAttribute(ConstParameter.EMAIL, email);
        request.setAttribute(ConstParameter.PASSWORD, password);
        return ConstPage.LOGIN;
    }

    /**
     * Sets response instance, which is used to attach cookies in case if user preferred to
     * stay logged in the system
     *
     * @param response HttpServletResponse response
     */
    @Override
    public void setResponse(HttpServletResponse response) {
        this.response = response;
    }
}

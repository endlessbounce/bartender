package by.khlebnikov.bartender.command;

import by.khlebnikov.bartender.constant.ConstAttribute;
import by.khlebnikov.bartender.constant.ConstParameter;
import by.khlebnikov.bartender.constant.Constant;
import by.khlebnikov.bartender.entity.User;
import by.khlebnikov.bartender.exception.CommandException;
import by.khlebnikov.bartender.exception.ServiceException;
import by.khlebnikov.bartender.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Optional;

/**
 * Class implementing logging out command
 */
public class LogoutCommand implements Command, CommandWithResponse {

    // Vars ---------------------------------------------------------------------------------------
    private static Logger logger = LogManager.getLogger();
    private UserService service;
    private HttpServletResponse response;

    // Constructors -------------------------------------------------------------------------------
    public LogoutCommand() {
        this.service = new UserService();
    }

    // Actions ------------------------------------------------------------------------------------

    /**
     * Deletes all cookies of a logged user and invalidates the session
     *
     * @param request HttpServletRequest request
     * @return null so controller redirects the user to the index page
     * @throws CommandException is thrown if an exception on lower levels happens
     */
    @Override
    public String execute(HttpServletRequest request) throws CommandException {
        Cookie[] cookieArr = request.getCookies();
        HttpSession session = request.getSession();
        User user = null;

        // A negative value means that the cookie is not stored persistently and will
        // be deleted when the Web browser exits. A zero value causes the cookie to be deleted.
        for (Cookie cookie : cookieArr) {
            switch (cookie.getName()) {
                case ConstParameter.STAY_LOGGED:
                    try {
                        Optional<User> userOpt = service.findUserByCookie(cookie.getValue());

                        if (userOpt.isPresent()) {
                            user = userOpt.get();
                            user.setUniqueCookie(null);
                            service.updateUser(user);
                        }
                    } catch (ServiceException e) {
                        throw new CommandException("Cookie name: " + cookie.getName() + ",\nuser: " + user, e);
                    }


                    // To delete a cookie, we need to create a cookie that have the same
                    // name with the cookie that we want to delete. We also need to set
                    // the max age of the cookie to 0 and then add it to the Servlet's
                    // response method.
                    cookie.setMaxAge(0);
                    response.addCookie(cookie);
                    logger.debug("deleted cookie: " + cookie.getValue());
                    break;
                case Constant.OLD_SESSION:
                    cookie.setMaxAge(0);
                    response.addCookie(cookie);
                    logger.debug("deleted cookie: " + cookie.getValue());
                    break;
            }
        }

        /*save locale*/
        String chosenLanguage = (String) session.getAttribute(ConstAttribute.CHOSEN_LANGUAGE);
        String locale = (String) session.getAttribute(ConstParameter.LOCALE);

        session.invalidate();

        /*create new session with chosen locale*/
        session = request.getSession(true);
        session.setAttribute(ConstAttribute.CHOSEN_LANGUAGE, chosenLanguage);
        session.setAttribute(ConstParameter.LOCALE, locale);

        return null;
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

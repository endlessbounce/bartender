package by.khlebnikov.bartender.filter;

import by.khlebnikov.bartender.constant.ConstAttribute;
import by.khlebnikov.bartender.constant.ConstParameter;
import by.khlebnikov.bartender.constant.Constant;
import by.khlebnikov.bartender.entity.User;
import by.khlebnikov.bartender.exception.ServiceException;
import by.khlebnikov.bartender.service.UserService;
import by.khlebnikov.bartender.utility.CookieHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

/**
 * WebFilter which defines whether the user has been logged in or not.
 */
@WebFilter(urlPatterns = {"/*"})
public class LoggedCookieFilter implements Filter {

    // Vars ---------------------------------------------------------------------------------------
    private static Logger logger = LogManager.getLogger();

    // Actions ------------------------------------------------------------------------------------

    /**
     * Is not implemented
     *
     * @param config
     * @throws ServletException
     */
    @Override
    public void init(FilterConfig config) throws ServletException {
    }

    /**
     * Defines if it's needed to restore logged in condition of the user after browser or server restart.
     *
     * @param request
     * @param response
     * @param next
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain next)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = ((HttpServletRequest) request);
        HttpServletResponse httpResponse = ((HttpServletResponse) response);

        Cookie[] cookieArr = httpRequest.getCookies();

        if (cookieArr != null) {
            Optional<Cookie> loggedCookieOpt = CookieHandler.getCookie(cookieArr, ConstParameter.STAY_LOGGED);
            Optional<Cookie> oldSessionCookieOpt = CookieHandler.getCookie(cookieArr, Constant.OLD_SESSION);

            logger.debug("loggedCookieOpt: " + loggedCookieOpt.isPresent());
            logger.debug("oldSessionCookieOpt: " + oldSessionCookieOpt.isPresent());

            /* If logged in user restarts browser Old Session Cookie will be dead
             * and the data will be reloaded from the DB.*/
            boolean userLoggedIn = loggedCookieOpt.isPresent();
            boolean browserRestarted = !oldSessionCookieOpt.isPresent();
            /* In case if a user was logged in and didn't close his browser,
             * and the server restarted at this moment, the 2 cookies will persist in browser,
             * so we need to keep track if the session has info about user*/
            boolean serverRestarted = httpRequest.getSession().getAttribute(ConstAttribute.USER_NAME) == null;

            boolean case1 = userLoggedIn && browserRestarted;
            boolean case2 = userLoggedIn && !browserRestarted && serverRestarted;

            logger.debug("case1: " + case1);
            logger.debug("case2: " + case2);

            if (case1 || case2) {
                Optional<User> userOpt = null;
                try {
                    userOpt = new UserService().findUserByCookie(loggedCookieOpt.get().getValue());

                    if (userOpt.isPresent()) {
                        User user = userOpt.get();
                        httpRequest.getSession().setAttribute(ConstAttribute.USER_NAME, user.getName());
                        httpRequest.getSession().setAttribute(ConstAttribute.USER_ID, user.getId());
                    }
                } catch (ServiceException e) {
                    logger.catching(e);
                }

                /*The cookie to mark new session*/
                Cookie oldSession = new Cookie(Constant.OLD_SESSION, ConstParameter.TRUE);
                oldSession.setMaxAge(-1);
                httpResponse.addCookie(oldSession);
            }
        }

        next.doFilter(request, response);
    }

    /**
     * Is not implemented
     */
    @Override
    public void destroy() {
    }
}

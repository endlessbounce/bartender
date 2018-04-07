package by.khlebnikov.bartender.filter;

import by.khlebnikov.bartender.constant.ConstAttribute;
import by.khlebnikov.bartender.constant.ConstParameter;
import by.khlebnikov.bartender.constant.Constant;
import by.khlebnikov.bartender.entity.User;
import by.khlebnikov.bartender.service.UserService;
import by.khlebnikov.bartender.utility.Utility;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@WebFilter(urlPatterns = { "/*" })
public class LoggedCookieFilter implements Filter {
    private Logger logger = LogManager.getLogger();

    public void init(FilterConfig config) throws ServletException {
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain next)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = ((HttpServletRequest) request);
        HttpServletResponse httpResponse = ((HttpServletResponse) response);

        Cookie[] cookieArr = httpRequest.getCookies();

        if (cookieArr != null) {
            Optional<Cookie> loggedCookieOpt = Utility.getCookie(cookieArr, ConstParameter.STAY_LOGGED);
            Optional<Cookie> oldSessionCookieOpt = Utility.getCookie(cookieArr, Constant.OLD_SESSION);

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
                List<User> userList = new UserService().findUserByCookie(loggedCookieOpt.get().getValue());

                if (!userList.isEmpty()) {
                    String name = userList.get(0).getName();
                    httpRequest.getSession().setAttribute(ConstAttribute.USER_NAME, name);
                }

                /*The cookie to mark new session*/
                Cookie oldSession = new Cookie(Constant.OLD_SESSION, ConstParameter.TRUE);
                oldSession.setMaxAge(-1);
                httpResponse.addCookie(oldSession);
            }
        }

        next.doFilter(request, response);
    }

    public void destroy() {
    }
}

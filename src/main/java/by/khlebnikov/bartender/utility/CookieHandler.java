package by.khlebnikov.bartender.utility;

import by.khlebnikov.bartender.constant.ConstParameter;
import by.khlebnikov.bartender.constant.Constant;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

/**
 * Utility class for work with cookies
 */
public class CookieHandler {

    // Actions ------------------------------------------------------------------------------------

    /**
     * Creates and returns a persistent cookie for long-session users
     *
     * @param uid unique ID for session identification
     * @return created cookie
     */
    public static Cookie persistingCookie(String uid) {
        Cookie cookie = new Cookie(ConstParameter.STAY_LOGGED, uid);
        cookie.setMaxAge(Constant.YEAR);
        return cookie;
    }

    /**
     * Looks for a cookie with the specified name and returns it
     *
     * @param cookieArray array to look for a cookie in
     * @param cookieName  cookie to find
     * @return Optional of cookie if it has been found, or an empty Optional
     */
    public static Optional<Cookie> getCookie(Cookie[] cookieArray, String cookieName) {
        Logger logger = LogManager.getLogger();
        Optional<Cookie> cookieOpt = Optional.empty();

        for (Cookie cookie : cookieArray) {
            if (cookieName.equals(cookie.getName())) {
                logger.debug(cookie.getName() + " : " + cookie.getValue());
                cookieOpt = Optional.of(cookie);
            }
        }

        return cookieOpt;
    }

    /**
     * Checks if the request has a persistent cookie
     *
     * @param request HttpServletRequest
     * @return true if a user is in long session
     */
    public static boolean isLoggedUser(HttpServletRequest request) {
        Cookie[] cookieArr = request.getCookies();
        Optional<Cookie> loggedCookieOpt = getCookie(cookieArr, ConstParameter.STAY_LOGGED);
        return loggedCookieOpt.isPresent();
    }
}

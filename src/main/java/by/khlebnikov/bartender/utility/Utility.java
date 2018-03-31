package by.khlebnikov.bartender.utility;

import by.khlebnikov.bartender.constant.Constant;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.Cookie;
import java.util.Calendar;
import java.util.Optional;
import java.util.UUID;

public class Utility {
    public static long expirationTime() {
        return Calendar.getInstance().getTimeInMillis() / Constant.TO_SECONDS + Constant.TIME_TO_CONFIRM;
    }

    public static long currentTime() {
        return Calendar.getInstance().getTimeInMillis() / Constant.TO_SECONDS;
    }

    public static long generateCode() {
        return (long) ((Math.random() + 1) * Constant.MULTIPLIER);
    }

    public static Cookie persistingCookie(String id) {
        Cookie cookie = new Cookie(Constant.STAY_LOGGED, id);
        cookie.setMaxAge(Constant.YEAR);
        return cookie;
    }

    public static String uniqueId() {
        return UUID.randomUUID().toString();
    }

    public static Optional<Cookie> getCookie(Cookie[] cookieArray, String cookieName) {
        Logger logger = LogManager.getLogger();
        Optional<Cookie> cookieOpt = Optional.empty();

        for (Cookie cookie : cookieArray) {
            if (cookieName.equals(cookie.getName())){
                logger.debug(cookie.getName() + " : " + cookie.getValue());
                cookieOpt = Optional.of(cookie);
            }
        }

        return cookieOpt;
    }
}
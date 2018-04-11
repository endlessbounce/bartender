package by.khlebnikov.bartender.utility;

import by.khlebnikov.bartender.constant.ConstLocale;
import by.khlebnikov.bartender.constant.ConstParameter;
import by.khlebnikov.bartender.constant.ConstQueryCocktail;
import by.khlebnikov.bartender.constant.Constant;
import by.khlebnikov.bartender.reader.PropertyReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Optional;
import java.util.UUID;

public class Utility {
    private Logger logger = LogManager.getLogger();

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
        Cookie cookie = new Cookie(ConstParameter.STAY_LOGGED, id);
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
            if (cookieName.equals(cookie.getName())) {
                logger.debug(cookie.getName() + " : " + cookie.getValue());
                cookieOpt = Optional.of(cookie);
            }
        }

        return cookieOpt;
    }

    public static boolean isLoggedUser(HttpServletRequest request) {
        Cookie[] cookieArr = request.getCookies();
        Optional<Cookie> loggedCookieOpt = getCookie(cookieArr, ConstParameter.STAY_LOGGED);
        return loggedCookieOpt.isPresent();
    }

    public String buildQuery(String locale,
                             String drinkType,
                             String baseDrink,
                             ArrayList<String> ingredientList) {

        StringBuilder query;
        String outerQuery;
        String groupName;
        String baseName;
        String subqueryPart1;
        String subqueryPart2;
        String subqueryPart3;

        if (ConstLocale.EN.equals(locale)) {
            outerQuery = PropertyReader.getQueryProperty(ConstQueryCocktail.ALL_INGRED_QUERY);
            groupName = ConstQueryCocktail.GROUP_NAME;
            baseName = ConstQueryCocktail.BASE_NAME;
            subqueryPart1 = PropertyReader.getQueryProperty(ConstQueryCocktail.SUBQUERY_PART_1);
            subqueryPart2 = PropertyReader.getQueryProperty(ConstQueryCocktail.SUBQUERY_PART_2);
            subqueryPart3 = PropertyReader.getQueryProperty(ConstQueryCocktail.SUBQUERY_PART_3);
        } else {
            outerQuery = PropertyReader.getQueryProperty(ConstQueryCocktail.ALL_INGRED_QUERY_LANG);
            groupName = ConstQueryCocktail.GROUP_NAME_RUS;
            baseName = ConstQueryCocktail.BASE_NAME_RUS;
            subqueryPart1 = PropertyReader.getQueryProperty(ConstQueryCocktail.SUBQUERY_PART_1_LANG);
            subqueryPart2 = PropertyReader.getQueryProperty(ConstQueryCocktail.SUBQUERY_PART_2_LANG);
            subqueryPart3 = PropertyReader.getQueryProperty(ConstQueryCocktail.SUBQUERY_PART_3_LANG);
        }

        query = new StringBuilder(outerQuery);

        if (drinkType != null) {
            query.append(groupName)
                    .append(drinkType)
                    .append("\" ")
                    .append(PropertyReader.getQueryProperty(ConstQueryCocktail.AND_GROUP));
        }

        if (baseDrink != null) {
            query.append(baseName)
                    .append(baseDrink)
                    .append("\" ")
                    .append(PropertyReader.getQueryProperty(ConstQueryCocktail.AND_BASE));
        }

        //build filter by ingredients subquery
        if (!ingredientList.isEmpty()) {
            query.append(subqueryPart1)
                    .append(ingredientList.size())
                    .append(subqueryPart2);

            for (int i = 0; i < ingredientList.size(); i++) {
                query.append("\"")
                        .append(ingredientList.get(i))
                        .append("\"");

                if (i != ingredientList.size() - 1) {
                    query.append(",");
                }
            }

            query.append(subqueryPart3);
        }

        query.append(";");
        logger.debug("query: " + query);

        return query.toString();
    }

}

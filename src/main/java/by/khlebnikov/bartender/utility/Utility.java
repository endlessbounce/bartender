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
import javax.xml.bind.DatatypeConverter;
import java.io.*;
import java.util.Calendar;
import java.util.Optional;
import java.util.UUID;

public class Utility {
    // Constants ----------------------------------------------------------------------------------
    private static String GROUP_AND_BASE = PropertyReader.getQueryProperty(ConstQueryCocktail.GROUP_AND_BASE);
    private static String ALL_INGRED_QUERY = PropertyReader.getQueryProperty(ConstQueryCocktail.ALL_INGRED_QUERY);
    private static String ALL_INGRED_QUERY_LANG = PropertyReader.getQueryProperty(ConstQueryCocktail.ALL_INGRED_QUERY_LANG);
    private static String GROUP_PART = PropertyReader.getQueryProperty(ConstQueryCocktail.GROUP_PART);
    private static String GROUP_PART_LANG = PropertyReader.getQueryProperty(ConstQueryCocktail.GROUP_PART_LANG);
    private static String BASE_PART = PropertyReader.getQueryProperty(ConstQueryCocktail.BASE_PART);
    private static String BASE_PART_LANG = PropertyReader.getQueryProperty(ConstQueryCocktail.BASE_PART_LANG);
    private static String SUBQUERY_PART_1 = PropertyReader.getQueryProperty(ConstQueryCocktail.SUBQUERY_PART_1);
    private static String SUBQUERY_PART_1_LANG = PropertyReader.getQueryProperty(ConstQueryCocktail.SUBQUERY_PART_1_LANG);
    private static String SUBQUERY_PART_2 = PropertyReader.getQueryProperty(ConstQueryCocktail.SUBQUERY_PART_2);
    private static String SUBQUERY_PART_2_LANG = PropertyReader.getQueryProperty(ConstQueryCocktail.SUBQUERY_PART_2_LANG);
    private static String SUBQUERY_PART_3 = PropertyReader.getQueryProperty(ConstQueryCocktail.SUBQUERY_PART_3);
    private static String SUBQUERY_PART_3_LANG = PropertyReader.getQueryProperty(ConstQueryCocktail.SUBQUERY_PART_3_LANG);

    // Actions ------------------------------------------------------------------------------------
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

    public static String buildQuery(boolean drinkTypeSelected, boolean baseDrinkSelected,
                                    int optionNum, String language) {
        StringBuilder query = new StringBuilder();
        String groupPart;
        String basePart;
        String subQueryPart1;
        String subQueryPart2;
        String subQueryPart3;

        if (ConstLocale.EN.equals(language)) {
            query.append(ALL_INGRED_QUERY);
            groupPart = GROUP_PART;
            basePart = BASE_PART;
            subQueryPart1 = SUBQUERY_PART_1;
            subQueryPart2 = SUBQUERY_PART_2;
            subQueryPart3 = SUBQUERY_PART_3;
        } else {
            query.append(ALL_INGRED_QUERY_LANG);
            groupPart = GROUP_PART_LANG;
            basePart = BASE_PART_LANG;
            subQueryPart1 = SUBQUERY_PART_1_LANG;
            subQueryPart2 = SUBQUERY_PART_2_LANG;
            subQueryPart3 = SUBQUERY_PART_3_LANG;
        }

        if (drinkTypeSelected) {
            query.append(groupPart);
        }

        if (baseDrinkSelected) {
            query.append(basePart);
        }

        query.append(GROUP_AND_BASE);

        if (optionNum > 0) {
            query.append(subQueryPart1)
                    .append(optionNum)
                    .append(subQueryPart2)
                    .append(buildQuestionPart(optionNum))
                    .append(subQueryPart3);
        }

        return query.append(Constant.SEMICOLON)
                .toString();
    }

    public static String convertBase64ToImage(String base64String, HttpServletRequest httpRequest) throws IOException {
        String[] partArr = base64String.split(Constant.COMMA);
        String base64Image = partArr[1];

        String extension = partArr[0].replace(Constant.BASE64_START, Constant.DOT)
                .replace(Constant.BASE64, Constant.EMPTY);

        String relativePath = httpRequest.getServletContext().getRealPath(Constant.EMPTY);
        String imagePath = Constant.IMG_FOLDER + Utility.uniqueId() + extension;

        File outputFile = new File(relativePath + imagePath);

        byte[] imageByte = DatatypeConverter.parseBase64Binary(base64Image);
        try (BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(outputFile))) {
            out.write(imageByte);
        }

        return imagePath;
    }

    private static String buildQuestionPart(int numOfSubstitutes){
        StringBuilder questionPart = new StringBuilder();

        for(int i = 0; i< numOfSubstitutes; i++){
            questionPart.append(Constant.QUESTION);

            if (i != numOfSubstitutes - 1) {
                questionPart.append(Constant.COMMA);
            }
        }

        return questionPart.toString();
    }
}

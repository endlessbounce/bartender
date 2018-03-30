package by.khlebnikov.bartender.constant;

public class Constant {
    public static final String QUERY_PROPERTY_PATH = "/WEB-INF/classes/queries.properties";
    public static final String EMAIL_PROPERTY_PATH = "/WEB-INF/classes/email.properties";

    public static final String NAME = "name";
    public static final String EMAIL = "email";
    public static final String PASSWORD = "password";
    public static final String CONFIRMATION = "confirmation";
    public static final String CODE = "code";

    public static final String CODE_PARAM = "&code=";
    public static final String EMAIL_PARAM = "&email=";

    public static final int TIME_TO_CONFIRM = 900;//15 minutes
    public static final int MIN_PASSWORD_LENGTH = 7;
    public static final int MAX_PASSWORD_LENGTH = 32;
}

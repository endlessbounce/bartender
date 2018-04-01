package by.khlebnikov.bartender.constant;

public class Constant {
    //Path constants
    public static final String EMAIL_PROPERTY_PATH = "/WEB-INF/classes/email.properties";

    //Common use constants
    public static final String EMAIL = "email";
    public static final String LOCALE = "locale";
    public static final String CHOSEN_LOCALE = "ChosenLocale";
    public static final String CODE = "code";
    public static final String COMMAND = "command";
    public static final String CONFIG = "config";
    public static final String CONFIRMATION = "confirmation";
    public static final String MESSAGES = "messages";
    public static final String MESSAGE_TYPE = "MessageType";
    public static final String NAME = "name";
    public static final String PASSWORD = "password";
    public static final String QUERIES = "queries";
    public static final String TRUE = "true";
    public static final String STAY_LOGGED = "logged";
    public static final String OLD_SESSION = "oldSession";
    public static final String USER = "user";
    public static final String USER_NAME = "userName";
    public static final String UTF8 = "UTF-8";
    public static final String ENCODING = "requestEncoding";
    public static final String CONTENT_TYPE = "text/html; charset=UTF-8";

    //Request constants
    public static final String CODE_PARAM = "&code=";
    public static final String EMAIL_PARAM = "&email=";

    //Locale constants
    public static final String EN = "EN";
    public static final String EN_LOW = "en";
    public static final String EN_US = "en_US";
    public static final String RU = "RU";
    public static final String RU_LOW = "ru";
    public static final String RU_RU = "ru_RU";
    public static final String US = "US";

    //Integer constants
    public static final int MULTIPLIER = 1000000;
    public static final int MIN_PASSWORD_LENGTH = 7;
    public static final int MAX_PASSWORD_LENGTH = 32;
    public static final int TIME_TO_CONFIRM = 120;//2 minutes
    public static final int TO_SECONDS = 1000;
    public static final int YEAR = 60*60*24*365;

    //Config property keys constants
    public static final String PAGE_HOME = "path.page.home";
    public static final String PAGE_INDEX = "path.page.index";
    public static final String PAGE_LOGIN = "path.page.login";
    public static final String PAGE_REMINDER = "path.page.reminder";
    public static final String PAGE_RESULT = "path.page.result";
    public static final String PAGE_REGISTRATION = "path.page.registration";
    public static final String PAGE_CATALOG = "path.page.catalog";
    public static final String PAGE_SETTINGS = "path.page.settings";
    public static final String PAGE_PROFILE = "path.page.profile";
    public static final String DB_DRIVER = "database.driver";
    public static final String DB_LOGIN = "database.login";
    public static final String DB_PASSWORD = "database.password";
    public static final String DB_URL = "database.url";

    //Query property keys constants
    public static final String USER_REP_ADD = "userrep.adduser";
    public static final String USER_REP_FIND = "userrep.find";
    public static final String USER_REP_UPDATE = "userrep.update";
    public static final String USER_REP_BY_COOKIE = "userrep.bycookie";
    public static final String PROSPECT_DAO_ADD = "prospectdao.addprospect";
    public static final String PROSPECT_DAO_FIND = "prospectdao.find";
    public static final String PROSPECT_DAO_DELETE = "prospectdao.delete";

    //Tables
    public static final String DB_USER_NAME = "us_name";
    public static final String DB_USER_EMAIL = "us_email";
    public static final String DB_USER_PASSWORD = "us_password";

    public static final String DB_PROSPECT_NAME = "pr_name";
    public static final String DB_PROSPECT_EMAIL = "pr_email";
    public static final String DB_PROSPECT_PASSWORD = "pr_password";
    public static final String DB_PROSPECT_EXPIRATION = "pr_expiration";
    public static final String DB_PROSPECT_CODE = "pr_code";
}

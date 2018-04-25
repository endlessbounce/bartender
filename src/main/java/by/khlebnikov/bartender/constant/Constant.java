package by.khlebnikov.bartender.constant;

/**
 * Constant utility class containing general constants of the application.
 */
public final class Constant {

    // Constants ----------------------------------------------------------------------------------
    public static final String EMAIL_PROPERTY_PATH = "/WEB-INF/classes/email.properties";
    public static final String OLD_SESSION = "oldSession";
    public static final String UTF8 = "UTF-8";
    public static final String QUOTE_REGEX = "[\"]";
    public static final String QUOTE_ESCAPE = "\\\\\"";
    public static final String ISO_8859 = "ISO-8859-1";
    public static final String ENCODING = "requestEncoding";
    public static final String BASE64 = ";base64";
    public static final String BASE64_START = "data:image/";
    public static final String EMPTY = "";
    public static final String IMG_FOLDER = "/img/";
    public static final String DOT = ".";
    public static final String COMMA = ",";
    public static final String SEMICOLON = ";";
    public static final String QUESTION = "?";
    public static final String CONTENT_TYPE = "text/html; charset=UTF-8";
    public static final String DEFAULT_COCKTAIL = "/img/defaultCocktail.png";
    public static final String URL_BARTENDER = "http://localhost:8080/controller";
    public static final int MULTIPLIER = 1000000;
    public static final int MIN_PASSWORD_LENGTH = 7;
    public static final int MAX_PASSWORD_LENGTH = 32;
    public static final int TIME_TO_CONFIRM = 120;//2 minutes
    public static final int TO_SECONDS = 1000;
    public static final int YEAR = 60*60*24*365;
    public static final int ITERATIONS = 10000;
    public static final int KEY_LENGTH = 256;
    public static final int EQUALS_1 = 1;
    public static final int ERROR_500 = 500;

    // Constructors -------------------------------------------------------------------------------
    private Constant() {
    }

}

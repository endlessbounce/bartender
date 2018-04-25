package by.khlebnikov.bartender.constant;

/**
 * Constant utility class containing names of columns in the 'user' table.
 */
public final class ConstTableUser {

    // Constants ----------------------------------------------------------------------------------
    public static final String ID = "us_id";
    public static final String NAME = "us_name";
    public static final String EMAIL = "us_email";
    public static final String HASH = "us_password";
    public static final String SALT = "us_salt";
    public static final String DATE = "us_registration_date";
    public static final String COOKIE = "us_cookie";

    // Constructors -------------------------------------------------------------------------------
    private ConstTableUser() {
    }
}

package by.khlebnikov.bartender.constant;

/**
 * Constant utility class containing names of columns in the 'prospect user' table.
 */
public final class ConstTableProspect {

    // Constants ----------------------------------------------------------------------------------
    public static final String NAME = "pr_name";
    public static final String EMAIL = "pr_email";
    public static final String HASH = "pr_password";
    public static final String SALT = "pr_salt";
    public static final String EXPIRATION = "pr_expiration";
    public static final String CODE = "pr_code";

    // Constructors -------------------------------------------------------------------------------
    private ConstTableProspect() {
    }
}

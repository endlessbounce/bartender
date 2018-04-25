package by.khlebnikov.bartender.constant;

/**
 * Constant utility class containing keys to user queries in the queries resource bundle.
 */
public final class ConstQueryUser {

    // Constants ----------------------------------------------------------------------------------
    public static final String ADD = "userdao.save";
    public static final String FIND_BY_EMAIL = "userdao.findbyemail";
    public static final String UPDATE = "userdao.update";
    public static final String FIND_BY_COOKIE = "userdao.findbycookie";
    public static final String IS_FAVOURITE = "userdao.isfavourite";
    public static final String DELETE_FAVOURITE = "userdao.deletefavourite";
    public static final String DELETE_CREATED = "userdao.deletecreated";
    public static final String SAVE_FAVOURITE = "userdao.savefavourite";
    public static final String FIND_ALL_FAVOURITE = "userdao.findallfavourite";
    public static final String FIND_ALL_FAVOURITE_LANG = "userdao.findallfavouritelang";
    public static final String FIND_ALL_CREATED = "userdao.findallcreated";
    public static final String FIND_ALL_CREATED_LANG = "userdao.findallcreatedlang";

    // Constructors -------------------------------------------------------------------------------
    private ConstQueryUser() {
    }
}

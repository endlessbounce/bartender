package by.khlebnikov.bartender.command;

/**
 * Enum containing all types of possible actions (commands)
 */
public enum CommandType {

    // Constants ----------------------------------------------------------------------------------
    /*returns the page with login form*/
    LOGIN(new LoginCommand()),

    /* BUTTON: sends login data (email and password) to server*/
    LOGIN_ACTION(new LoginActionCommand()),

    /* BUTTON: sends login data (email and password) to server*/
    SEARCH(new SearchCommand()),

    /*logs out the user*/
    LOGOUT(new LogoutCommand()),

    /*returns the page with registration form*/
    REGISTER(new RegisterCommand()),

    /* BUTTON: sends user data from registration form to the server*/
    CONFIRM(new ConfirmEmailCommand()),

    /*this gets confirmation code from user's email and handles registration*/
    REGISTER_ACTION(new RegisterActionCommand()),

    /*returns the page for changing password with 1 field -
    * email on which link for resetting should be sent*/
    RESET(new ResetCommand()),

    /* BUTTON: this sends email from the password reset page to the server,
    * and sends email to user*/
    RESET_ACTION(new ResetActionCommand()),

    /*returns index page with chosen locale*/
    LOCALE(new LocaleCommand()),

    /*returns catalog page*/
    CATALOG(new CatalogCommand()),

    /*returns profile page*/
    PROFILE(new ProfileCommand()),

    /*returns settings page*/
    SETTINGS(new SettingCommand()),

    /*returns the page with form for changing password (2 inputs)*/
    SET_NEW(new SetPasswordCommand()),

    /* BUTTON: sends password and its confirmation to server
    * and updates record with new password*/
    SET_NEW_ACTION(new SetPasswordActionCommand()),

    /*Returns glossary page*/
    GLOSSARY(new GlossaryCommand()),

    /*Returns a cocktail created by user*/
    USER_COCKTAIL(new UserCocktailCommand()),

    /*Returns the page with a chosen cocktail*/
    COCKTAIL(new CocktailCommand());

    // Vars ---------------------------------------------------------------------------------------
    private Command command;

    // Constructors -------------------------------------------------------------------------------
    CommandType(Command command) {
        this.command = command;
    }

    // Actions ------------------------------------------------------------------------------------
    public Command getCommand() {
        return command;
    }
}

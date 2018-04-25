package by.khlebnikov.bartender.command;

import by.khlebnikov.bartender.constant.ConstPage;

import javax.servlet.http.HttpServletRequest;

/**
 * Class to render login page to the user
 */
public class LoginCommand implements Command {

    // Actions ------------------------------------------------------------------------------------

    /**
     * Returns the login page if a user is going to log in
     *
     * @param request HttpServletRequest request
     * @return the login page with the login form
     */
    @Override
    public String execute(HttpServletRequest request) {
        return ConstPage.LOGIN;
    }
}

package by.khlebnikov.bartender.command;

import by.khlebnikov.bartender.constant.ConstPage;

import javax.servlet.http.HttpServletRequest;

/**
 * Class rendering registration form to users
 */
public class RegisterCommand implements Command {

    // Actions ------------------------------------------------------------------------------------

    /**
     * Returns the registration page
     *
     * @param request HttpServletRequest request
     * @return the registration page
     */
    @Override
    public String execute(HttpServletRequest request) {
        return ConstPage.REGISTRATION;
    }
}

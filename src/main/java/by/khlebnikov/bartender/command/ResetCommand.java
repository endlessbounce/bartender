package by.khlebnikov.bartender.command;

import by.khlebnikov.bartender.constant.ConstPage;
import by.khlebnikov.bartender.reader.PropertyReader;

import javax.servlet.http.HttpServletRequest;

/**
 * Class to render reset page to users, where they should submit their email
 * to send verification link to
 */
public class ResetCommand implements Command {

    // Actions ------------------------------------------------------------------------------------

    /**
     * Returns the reset page
     *
     * @param request HttpServletRequest request
     * @return the reset page
     */
    @Override
    public String execute(HttpServletRequest request) {
        return ConstPage.RESET;
    }
}

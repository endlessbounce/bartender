package by.khlebnikov.bartender.command;

import by.khlebnikov.bartender.constant.ConstPage;

import javax.servlet.http.HttpServletRequest;

/**
 * Class defining command to handle default page rendering
 */
public class DefaultCommand implements Command {

    // Actions ------------------------------------------------------------------------------------

    /**
     * Returns default page in case of wrong request address or other errors.
     *
     * @param request HttpServletRequest request
     * @return the path to the home page
     */
    @Override
    public String execute(HttpServletRequest request) {
        return ConstPage.HOME;
    }
}

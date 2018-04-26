package by.khlebnikov.bartender.command;

import by.khlebnikov.bartender.constant.ConstPage;
import by.khlebnikov.bartender.constant.ConstParameter;
import by.khlebnikov.bartender.exception.CommandException;

import javax.servlet.http.HttpServletRequest;

/**
 * Class representing command showing a user results of a search
 */
public class SearchCommand implements Command {

    // Actions ------------------------------------------------------------------------------------

    /**
     * Returns to the user search page
     *
     * @param request HttpServletRequest request
     * @return the search page
     * @throws CommandException is thrown if an exception on lower levels happens
     */
    @Override
    public String execute(HttpServletRequest request) throws CommandException {
        String text = request.getParameter(ConstParameter.TEXT);

        if (text != null) {
            request.setAttribute(ConstParameter.TEXT, text);
        }

        return ConstPage.SEARCH_RESULT;
    }
}

package by.khlebnikov.bartender.command;

import by.khlebnikov.bartender.constant.ConstPage;
import by.khlebnikov.bartender.dao.QueryType;
import by.khlebnikov.bartender.exception.CommandException;

import javax.servlet.http.HttpServletRequest;

/**
 * Class to render a page with a cocktail created by a user
 */
public class UserCocktailCommand implements Command {

    // Actions ------------------------------------------------------------------------------------

    /**
     * Finds in the database chosen by a user created cocktail and returns it
     *
     * @param request HttpServletRequest request
     * @return result page if the cocktail was not found, otherwise the cocktail's page
     * @throws CommandException is thrown if an exception on lower levels happens
     */
    @Override
    public String execute(HttpServletRequest request) throws CommandException {
        boolean isCreated = true;
        return new CocktailCommand().processRequest(request, isCreated, QueryType.FIND_CREATED, ConstPage.USER_COCKTAIL);
    }
}

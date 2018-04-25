package by.khlebnikov.bartender.command;

import by.khlebnikov.bartender.constant.*;
import by.khlebnikov.bartender.dao.QueryType;
import by.khlebnikov.bartender.entity.Cocktail;
import by.khlebnikov.bartender.exception.CommandException;
import by.khlebnikov.bartender.exception.ServiceException;
import by.khlebnikov.bartender.service.CocktailService;
import by.khlebnikov.bartender.tag.MessageType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

/**
 * Class representing command, returning the path to a page displaying information
 * about chosen cocktail.
 */
public class CocktailCommand implements Command {

    // Vars ---------------------------------------------------------------------------------------
    private static Logger logger = LogManager.getLogger();
    private CocktailService cocktailService;

    // Constructors -------------------------------------------------------------------------------
    public CocktailCommand() {
        this.cocktailService = new CocktailService();
    }

    // Actions ------------------------------------------------------------------------------------

    /**
     * Reads the id of a cocktail chosen by user to view. If such cocktail exists,
     * returns the page to the cocktail, otherwise informational page.
     *
     * @param request HttpServletRequest request
     * @return the path to a cocktail's page
     * @throws CommandException if an exception on lower levels happens
     */
    @Override
    public String execute(HttpServletRequest request) throws CommandException {
        int cocktailId = Integer.parseInt(request.getParameter(ConstParameter.ID));
        String language = (String) request.getSession().getAttribute(ConstAttribute.CHOSEN_LANGUAGE);
        boolean isCreated = false;
        String page;

        Optional<Cocktail> cocktailOpt;

        try {
            cocktailOpt = cocktailService.find(QueryType.FIND, cocktailId, language, isCreated);
        } catch (ServiceException e) {
            throw new CommandException(e);
        }

        if (cocktailOpt.isPresent()) {
            request.setAttribute(ConstParameter.COCKTAIL, cocktailOpt.get());
            page = ConstPage.COCKTAIL;

            logger.debug("chosen cocktail: " + cocktailOpt.get());
        } else {
            request.setAttribute(ConstAttribute.MESSAGE_TYPE, MessageType.COCKTAIL_NOT_FOUND);
            page = ConstPage.RESULT;
        }

        return page;
    }

}

package by.khlebnikov.bartender.command;

import by.khlebnikov.bartender.constant.ConstAttribute;
import by.khlebnikov.bartender.constant.ConstPage;
import by.khlebnikov.bartender.constant.ConstParameter;
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
 * Class to render a page with a cocktail created by a user
 */
public class UserCocktailCommand implements Command {

    // Vars ---------------------------------------------------------------------------------------
    private static Logger logger = LogManager.getLogger();
    private CocktailService cocktailService;

    // Constructors -------------------------------------------------------------------------------
    public UserCocktailCommand() {
        this.cocktailService = new CocktailService();
    }

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
        int cocktailId = Integer.parseInt(request.getParameter(ConstParameter.ID));
        String language = (String) request.getSession().getAttribute(ConstAttribute.CHOSEN_LANGUAGE);
        boolean isCreated = true;
        Optional<Cocktail> cocktailOpt;
        String page;

        try {
            /*RU language means to fetch from the column where created cocktail has been saved*/
            cocktailOpt = cocktailService.find(QueryType.FIND_CREATED, cocktailId, language, isCreated);
        } catch (ServiceException e) {
            throw new CommandException(e);
        }

        if (cocktailOpt.isPresent()) {
            request.setAttribute(ConstParameter.COCKTAIL, cocktailOpt.get());
            page = ConstPage.USER_COCKTAIL;

            logger.debug("chosen created cocktail: " + cocktailOpt.get());
        } else {
            request.setAttribute(ConstAttribute.MESSAGE_TYPE, MessageType.COCKTAIL_NOT_FOUND);
            page = ConstPage.RESULT;
        }
        return page;
    }
}

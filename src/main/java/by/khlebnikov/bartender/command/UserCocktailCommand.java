package by.khlebnikov.bartender.command;

import by.khlebnikov.bartender.constant.*;
import by.khlebnikov.bartender.dao.QueryType;
import by.khlebnikov.bartender.entity.Cocktail;
import by.khlebnikov.bartender.exception.ControllerException;
import by.khlebnikov.bartender.exception.ServiceException;
import by.khlebnikov.bartender.reader.PropertyReader;
import by.khlebnikov.bartender.service.CocktailService;
import by.khlebnikov.bartender.tag.MessageType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

public class UserCocktailCommand implements Command {
    private Logger logger = LogManager.getLogger();
    private CocktailService cocktailService;

    public UserCocktailCommand() {
        this.cocktailService = new CocktailService();
    }

    @Override
    public String execute(HttpServletRequest request) throws ControllerException {
        int cocktailId = Integer.parseInt(request.getParameter(ConstParameter.ID));
        String language = (String) request.getSession().getAttribute(ConstAttribute.CHOSEN_LANGUAGE);
        boolean isCreated = true;
        Optional<Cocktail> cocktailOpt;
        String page;

        try{
            /*RU language means to fetch from the column where created cocktail has been saved*/
            cocktailOpt = cocktailService.find(QueryType.FIND_CREATED, cocktailId, language, isCreated);
        } catch (ServiceException e) {
            throw new ControllerException(e);
        }

        if (cocktailOpt.isPresent()) {
            request.setAttribute(ConstParameter.COCKTAIL, cocktailOpt.get());
            logger.debug("chosen created cocktail: " + cocktailOpt.get());

            page = PropertyReader.getConfigProperty(ConstPage.USER_COCKTAIL);
        } else {
            request.setAttribute(ConstAttribute.MESSAGE_TYPE, MessageType.COCKTAIL_NOT_FOUND);
            page = PropertyReader.getConfigProperty(ConstPage.RESULT);
        }
        return page;
    }
}

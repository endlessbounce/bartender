package by.khlebnikov.bartender.command;

import by.khlebnikov.bartender.constant.*;
import by.khlebnikov.bartender.entity.Cocktail;
import by.khlebnikov.bartender.reader.PropertyReader;
import by.khlebnikov.bartender.service.CocktailService;
import by.khlebnikov.bartender.tag.MessageType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

public class CocktailCommand implements Command {
    private Logger logger = LogManager.getLogger();
    private CocktailService cocktailService;

    public CocktailCommand() {
        this.cocktailService = new CocktailService();
    }

    @Override
    public String execute(HttpServletRequest request) {
        int id = Integer.parseInt(request.getParameter(ConstParameter.ID));
        boolean isCreated = false;
        String language = (String) request.getSession().getAttribute(ConstAttribute.CHOSEN_LANGUAGE);
        String page;
        String query;

        if (ConstLocale.EN.equals(language)) {
            query = PropertyReader.getQueryProperty(ConstQueryCocktail.FIND_BY_ID);
        } else {
            query = PropertyReader.getQueryProperty(ConstQueryCocktail.FIND_BY_ID_LANG);
        }

        Optional<Cocktail> cocktailOpt = cocktailService.find(id, language, isCreated, query);

        if (cocktailOpt.isPresent()) {
            request.setAttribute(ConstParameter.COCKTAIL, cocktailOpt.get());
            logger.debug("chosen cocktail: " + cocktailOpt.get());

            page = PropertyReader.getConfigProperty(ConstPage.COCKTAIL);
        } else {
            request.setAttribute(ConstAttribute.MESSAGE_TYPE, MessageType.COCKTAIL_NOT_FOUND);
            page = PropertyReader.getConfigProperty(ConstPage.RESULT);
        }

        return page;
    }
}

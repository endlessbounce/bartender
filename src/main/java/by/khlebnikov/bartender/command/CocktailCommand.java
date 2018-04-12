package by.khlebnikov.bartender.command;

import by.khlebnikov.bartender.constant.ConstAttribute;
import by.khlebnikov.bartender.constant.ConstPage;
import by.khlebnikov.bartender.constant.ConstParameter;
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
        String language = (String) request.getSession().getAttribute(ConstAttribute.CHOSEN_LANGUAGE);
        String page;

        logger.debug("cocktail to fetch: " + id);

        Optional<Cocktail> cocktailOpt = cocktailService.findChosenCocktail(id, language);

        if(cocktailOpt.isPresent()){
            Cocktail cocktail = cocktailOpt.get();

            request.setAttribute(ConstParameter.COCKTAIL_NAME, cocktail.getName());
            request.setAttribute(ConstParameter.COCKTAIL_SLOGAN, cocktail.getSlogan());
            request.setAttribute(ConstParameter.COCKTAIL_RECIPE, cocktail.getRecipe());
            request.setAttribute(ConstParameter.COCKTAIL_TYPE, cocktail.getType());
            request.setAttribute(ConstParameter.COCKTAIL_BASE, cocktail.getBaseDrink());
            request.setAttribute(ConstParameter.COCKTAIL_URI, cocktail.getUri());
            request.setAttribute(ConstParameter.COCKTAIL_INGREDIENT, cocktail.getIngredientList());
            request.setAttribute(ConstParameter.COCKTAIL_ID, cocktail.getId());

            logger.debug("chosen cocktail: " + cocktail);

            page = PropertyReader.getConfigProperty(ConstPage.COCKTAIL);
        }else{
            request.setAttribute(ConstAttribute.MESSAGE_TYPE, MessageType.COCKTAIL_NOT_FOUND);
            page = PropertyReader.getConfigProperty(ConstPage.RESULT);
        }

        return page;
    }
}

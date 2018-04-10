package by.khlebnikov.bartender.service;

import by.khlebnikov.bartender.constant.ConstParameter;
import by.khlebnikov.bartender.constant.ConstQueryCocktail;
import by.khlebnikov.bartender.entity.Cocktail;
import by.khlebnikov.bartender.dao.CocktailDao;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.ws.rs.core.MultivaluedMap;
import java.util.ArrayList;
import java.util.List;

public class CocktailService {
    private Logger logger = LogManager.getLogger();
    CocktailDao cocktailDao;

    public CocktailService() {
        this.cocktailDao = new CocktailDao();
    }

    public List<Cocktail> findCocktails(MultivaluedMap params) {
        ArrayList<String> ingredientList = new ArrayList<>();
        String drinkType = null;
        String baseDrink = null;
        String locale = null;

        for (Object key : params.keySet()) {
            if (ConstQueryCocktail.DRINK_TYPE.equals(key)) {
                drinkType = (String) params.getFirst(key);
            } else if (ConstQueryCocktail.BASE_DRINK.equals(key)) {
                baseDrink = (String) params.getFirst(key);
            } else if (ConstParameter.LOCALE.equals(key)) {
                locale = (String) params.getFirst(key);
            } else {
                ingredientList.add((String) params.getFirst(key));
            }
        }

        List<Cocktail> cocktailList = cocktailDao.findAllByParameter(locale,
                drinkType,
                baseDrink,
                ingredientList);
        logger.debug("search by parameters: " + cocktailList);

        /*fill each cocktail's list of portions with ingredients*/
        for (Cocktail cocktail : cocktailList) {
            cocktailDao.findIngredients(locale, cocktail);
        }
        logger.debug("next batch with ingredients: " + cocktailList);

        return cocktailList;
    }
}

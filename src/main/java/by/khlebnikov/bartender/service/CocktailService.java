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
import java.util.Optional;

public class CocktailService {
    private Logger logger = LogManager.getLogger();
    CocktailDao cocktailDao;

    public CocktailService() {
        this.cocktailDao = new CocktailDao();
    }

    public Optional<Cocktail> findChosenCocktail(int id, String language) {
        Optional<Cocktail> cocktailOpt = cocktailDao.findCocktail(id, language);
        Cocktail cocktail;

        if (cocktailOpt.isPresent()) {
            cocktail = cocktailOpt.get();
            cocktail.setIngredientList(cocktailDao.findIngredients(language, cocktail.getId()));
        }

        return cocktailOpt;
    }

    public List<Cocktail> findCocktails(MultivaluedMap params) {
        ArrayList<String> ingredientList = new ArrayList<>();
        String drinkType = null;
        String baseDrink = null;
        String language = null;

        for (Object key : params.keySet()) {
            if (ConstQueryCocktail.DRINK_TYPE.equals(key)) {
                drinkType = (String) params.getFirst(key);
            } else if (ConstQueryCocktail.BASE_DRINK.equals(key)) {
                baseDrink = (String) params.getFirst(key);
            } else if (ConstParameter.LOCALE.equals(key)) {
                language = (String) params.getFirst(key);
            } else {
                String value = (String) params.getFirst(key);
                value = value.replaceAll("[\"]", "\\\\\"");
                logger.debug("replaced value: " + value);
                ingredientList.add(value);
            }
        }

        List<Cocktail> cocktailList = cocktailDao.findAllByParameter(language,
                drinkType,
                baseDrink,
                ingredientList);

        /*find each cocktail's portions with ingredients*/
        for (Cocktail cocktail : cocktailList) {
            cocktail.setIngredientList(cocktailDao.findIngredients(language, cocktail.getId()));
        }
        logger.debug("chosen by parameters cocktails: " + cocktailList);

        return cocktailList;
    }
}

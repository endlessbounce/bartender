package by.khlebnikov.bartender.service;

import by.khlebnikov.bartender.constant.ConstParameter;
import by.khlebnikov.bartender.constant.ConstQueryCocktail;
import by.khlebnikov.bartender.constant.Constant;
import by.khlebnikov.bartender.entity.Cocktail;
import by.khlebnikov.bartender.dao.CocktailDao;
import by.khlebnikov.bartender.exception.ServiceException;
import by.khlebnikov.bartender.utility.Utility;
import by.khlebnikov.bartender.validator.Validator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.MultivaluedMap;
import java.io.IOException;
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

        if (cocktailOpt.isPresent()) {
            Cocktail cocktail = cocktailOpt.get();
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
                value = value.replaceAll(Constant.QUOTE_REGEX, Constant.QUOTE_ESCAPE);
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

    public List<Cocktail> findAllFavourite(MultivaluedMap params, int userId) {
        String language = (String) params.getFirst(ConstParameter.LOCALE);
        List<Cocktail> favouriteList = cocktailDao.findAllFavourite(language, userId);

        if(!favouriteList.isEmpty()){
            favouriteList.forEach(cocktail -> cocktail.setIngredientList(cocktailDao.findIngredients(language, cocktail.getId())));
        }

        logger.debug("return favourite list : " + favouriteList);
        return favouriteList;
    }

    public boolean addCreated(int userId,
                              Cocktail cocktail,
                              HttpServletRequest httpRequest,
                              MultivaluedMap params) throws ServiceException {
        String language = (String) params.getFirst(ConstParameter.LOCALE);
        String uri = cocktail.getUri();
        boolean stringOk = Validator.checkString(uri);

        if(!stringOk){
            cocktail.setUri(Constant.DEFAULT_COCKTAIL);
        }else if(uri.startsWith(Constant.BASE64_START) && uri.contains(Constant.BASE64)){
            try {
                cocktail.setUri(Utility.convertBase64ToImage(uri, httpRequest));
            } catch (IOException e) {
                logger.catching(e);
                throw new ServiceException(e);
            }
        }

        logger.debug("imparting cocktail to DAO: " + cocktail);

        return cocktailDao.saveCreated(userId, cocktail, language);
    }
}

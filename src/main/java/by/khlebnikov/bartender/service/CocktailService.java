package by.khlebnikov.bartender.service;

import by.khlebnikov.bartender.constant.*;
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

/**
 * Works with cocktails
 */
public class CocktailService {
    private Logger logger = LogManager.getLogger();
    CocktailDao cocktailDao;

    public CocktailService() {
        this.cocktailDao = new CocktailDao();
    }

    /**
     * Finds cocktail by its id
     * @param cocktailId
     * @param language
     * @return
     */
    public Optional<Cocktail> find(int cocktailId, String language, boolean isCreated, String query) {
        Optional<Cocktail> cocktailOpt = cocktailDao.find(cocktailId, language, isCreated, query);

        if (cocktailOpt.isPresent()) {
            Cocktail cocktail = cocktailOpt.get();
            cocktail.setIngredientList(cocktailDao.findIngredients(language, cocktail.getId()));
        }

        return cocktailOpt;
    }

    /**
     * Finds all favourite or created by user cocktails
     * @param language
     * @param query
     * @param userId
     * @return
     */
    public List<Cocktail> findAll(String language, String query, int userId, boolean isCreated) {
        List<Cocktail> createdList = cocktailDao.findAll(userId, query, language, isCreated);
        fillWithIngredient(createdList, language);

        return createdList;
    }

    /**
     * Finds all cocktails by parameters from the form on catalog page
     * @param params
     * @return
     */
    public List<Cocktail> findAllMatching(MultivaluedMap params) {
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

        logger.debug("request parameters: " + language +
                ", drinkType: " + drinkType +
                ", baseDrink: "  + baseDrink +
                ", ingredientList: " + ingredientList );

        List<Cocktail> cocktailList = cocktailDao.findAllMatching(language,
                drinkType,
                baseDrink,
                ingredientList);

        /*find each cocktail's portions with ingredients*/
        for (Cocktail cocktail : cocktailList) {
            cocktail.setIngredientList(cocktailDao.findIngredients(language, cocktail.getId()));
        }

        return cocktailList;
    }

    /**
     * Persists a cocktail
     * @param userId
     * @param cocktail
     * @param httpRequest
     * @param params
     * @return
     * @throws ServiceException
     */
    public boolean save(int userId,
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

        return cocktailDao.save(userId, cocktail, language);
    }

    /**
     * Finds all ingredients of a cocktail
     * @param cocktailList
     * @param language
     */
    private void fillWithIngredient(List<Cocktail> cocktailList, String language){
        if(!cocktailList.isEmpty()){
            cocktailList.forEach(cocktail -> cocktail.setIngredientList(cocktailDao.findIngredients(language, cocktail.getId())));
        }
    }
}

package by.khlebnikov.bartender.service;

import by.khlebnikov.bartender.constant.ConstParameter;
import by.khlebnikov.bartender.constant.ConstQueryCocktail;
import by.khlebnikov.bartender.constant.Constant;
import by.khlebnikov.bartender.dao.CocktailDao;
import by.khlebnikov.bartender.entity.Cocktail;
import by.khlebnikov.bartender.exception.DataAccessException;
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
     *
     * @param cocktailId
     * @param language
     * @return
     */
    public Optional<Cocktail> find(int cocktailId, String language, boolean isCreated, String query) throws ServiceException {
        Optional<Cocktail> cocktailOpt;
        try {
            cocktailOpt = cocktailDao.find(cocktailId, language, isCreated, query);

            if (cocktailOpt.isPresent()) {
                Cocktail cocktail = cocktailOpt.get();
                cocktail.setIngredientList(cocktailDao.findIngredients(language, cocktail.getId()));
            }
        } catch (DataAccessException e) {
            throw new ServiceException("Find a cocktail with cocktail id: " + cocktailId +
                    ",\n chosen language: " + language +
                    ",\n is created by user: " + isCreated +
                    ",\n query: " + query, e);
        }

        return cocktailOpt;
    }

    /**
     * Finds all favourite or created by user cocktails
     *
     * @param language
     * @param query
     * @param userId
     * @return
     */
    public List<Cocktail> findAll(String language, String query, int userId, boolean isCreated) throws ServiceException {
        List<Cocktail> createdList;

        try {
            createdList = cocktailDao.findAll(userId, query, language, isCreated);
            fillWithIngredient(createdList, language);
        } catch (DataAccessException e) {
            throw new ServiceException("Find all cocktails with user id: " + userId +
                    ",\n language: " + language +
                    ",\n is cocktail created by user: " + isCreated +
                    ",\n query: " + query, e);
        }

        return createdList;
    }

    /**
     * Finds all cocktails by parameters from the form on catalog page
     *
     * @param params
     * @return
     */
    public List<Cocktail> findAllMatching(MultivaluedMap params) throws ServiceException {
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
                ",\n drinkType: " + drinkType +
                ",\n baseDrink: " + baseDrink +
                ",\n ingredientList: " + ingredientList);

        List<Cocktail> cocktailList;

        try {
            cocktailList = cocktailDao.findAllMatching(language,
                    drinkType,
                    baseDrink,
                    ingredientList);

            /*find each cocktail's portions with ingredients*/
            for (Cocktail cocktail : cocktailList) {
                cocktail.setIngredientList(cocktailDao.findIngredients(language, cocktail.getId()));
            }
        } catch (DataAccessException e) {
            throw new ServiceException("Find cocktails by parameters: type of drink: " + drinkType +
                    ", base drink: " + baseDrink +
                    ", ingredients: " + ingredientList +
                    ", language: " + language, e);
        }

        return cocktailList;
    }

    /**
     * Persists a cocktail
     *
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
        boolean result;

        if (!stringOk) {
            cocktail.setUri(Constant.DEFAULT_COCKTAIL);
        } else if (uri.startsWith(Constant.BASE64_START) && uri.contains(Constant.BASE64)) {
            try {
                cocktail.setUri(Utility.convertBase64ToImage(uri, httpRequest));
            } catch (IOException e) {
                throw new ServiceException("Exception while converting BASE64 to image", e);
            }
        }

        logger.debug("imparting cocktail to DAO: " + cocktail);

        try {
            result = cocktailDao.save(userId, cocktail, language);
        } catch (DataAccessException e) {
            throw new ServiceException("Save created cocktail with user id: " + userId +
                    ",\n language" + language +
                    ",\n cocktail:" + cocktail, e);
        }

        return result;
    }

    /**
     * Finds all ingredients of a cocktail
     *
     * @param cocktailList
     * @param language
     */
    private void fillWithIngredient(List<Cocktail> cocktailList, String language) throws DataAccessException {
        if (!cocktailList.isEmpty()) {
            for (Cocktail cocktail : cocktailList) {
                cocktail.setIngredientList(cocktailDao.findIngredients(language, cocktail.getId()));
            }
        }
    }
}

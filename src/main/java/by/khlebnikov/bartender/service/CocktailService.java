package by.khlebnikov.bartender.service;

import by.khlebnikov.bartender.constant.ConstParameter;
import by.khlebnikov.bartender.constant.ConstQueryCocktail;
import by.khlebnikov.bartender.constant.Constant;
import by.khlebnikov.bartender.dao.CocktailDao;
import by.khlebnikov.bartender.dao.QueryType;
import by.khlebnikov.bartender.entity.Cocktail;
import by.khlebnikov.bartender.exception.DataAccessException;
import by.khlebnikov.bartender.exception.ServiceException;
import by.khlebnikov.bartender.utility.Utility;
import by.khlebnikov.bartender.validator.Validator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MultivaluedMap;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Works with cocktails
 */
public class CocktailService {
    // Vars ----------------------------------------------------------------------------------
    private Logger logger = LogManager.getLogger();
    private CocktailDao cocktailDao;

    // Constructors --------------------------------------------------------------------------
    public CocktailService() {
        this.cocktailDao = new CocktailDao();
    }

    // Actions -------------------------------------------------------------------------------
    /**
     * Finds cocktail by its id
     *
     * @param cocktailId
     * @param language
     * @return
     */
    public Optional<Cocktail> find(QueryType queryType, int cocktailId, String language, boolean isCreated)
            throws ServiceException {
        Optional<Cocktail> cocktailOpt;
        try {
            cocktailOpt = cocktailDao.findByCocktailId(queryType, cocktailId, language, isCreated);

            if (cocktailOpt.isPresent()) {
                Cocktail cocktail = cocktailOpt.get();
                cocktail.setIngredientList(cocktailDao.findIngredients(language, cocktail.getId(), isCreated));
            }
        } catch (DataAccessException e) {
            throw new ServiceException("Find a cocktail with cocktail id: " + cocktailId +
                    ",\n chosen language: " + language +
                    ",\n is created by user: " + isCreated +
                    ",\n query type: " + queryType, e);
        }

        return cocktailOpt;
    }


    public List<Cocktail> findAll(QueryType queryType, String language, int userId, boolean isCreated)
            throws ServiceException {
        List<Cocktail> createdList;

        try {
            createdList = cocktailDao.findAllByUserId(queryType, userId, language, isCreated);
            fillWithIngredient(createdList, language, isCreated);
        } catch (DataAccessException e) {
            throw new ServiceException("Find all cocktails with user id: " + userId +
                    ",\n language: " + language +
                    ",\n is cocktail created by user: " + isCreated, e);
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
        boolean isCreated = false;

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
            cocktailList = cocktailDao.findAllMatching(language, drinkType, baseDrink, ingredientList);
            fillWithIngredient(cocktailList, language, isCreated);
        } catch (DataAccessException e) {
            throw new ServiceException("Find cocktails by parameters: type of drink: " + drinkType +
                    ", base drink: " + baseDrink +
                    ", ingredients: " + ingredientList +
                    ", language: " + language, e);
        }

        return cocktailList;
    }

    public boolean saveCreated(int userId, Cocktail cocktail, MultivaluedMap params, HttpServletRequest httpRequest)
            throws ServiceException {
        return executeUpdateCocktail(userId, QueryType.SAVE, cocktail, params, httpRequest);
    }

    public boolean updateCreated(int userId, Cocktail cocktail, MultivaluedMap params,  HttpServletRequest httpRequest)
            throws ServiceException {
        return executeUpdateCocktail(userId, QueryType.UPDATE, cocktail, params, httpRequest);
    }

    public boolean executeUpdateCocktail(int userId, int cocktailId, QueryType queryType)
            throws ServiceException {
        try {
            switch (queryType) {
                case DELETE_CREATED:
                    return cocktailDao.deleteCreated(userId, cocktailId);
                case DELETE_FAVOURITE:
                    return cocktailDao.deleteFavourite(userId, cocktailId);
                case SAVE_FAVOURITE:
                    return cocktailDao.saveFavourite(userId, cocktailId);
                default:
                    return false;
            }

        } catch (DataAccessException e) {
            throw new ServiceException("Query type" + queryType, e);
        }
    }

    // Helper methods ------------------------------------------------------------------------
    private boolean executeUpdateCocktail(int userId, QueryType queryType, Cocktail cocktail, MultivaluedMap params,  HttpServletRequest httpRequest)
            throws ServiceException {
        String relativePath = httpRequest.getServletContext().getRealPath(Constant.EMPTY);
        String language = (String) params.getFirst(ConstParameter.LOCALE);
        String uri = cocktail.getUri();
        boolean stringOk = Validator.checkString(uri);

        try {
            if (!stringOk) {
                cocktail.setUri(Constant.DEFAULT_COCKTAIL);
            } else if (uri.startsWith(Constant.BASE64_START) && uri.contains(Constant.BASE64)) {
                cocktail.setUri(Utility.convertBase64ToImage(uri, relativePath));
            }

            logger.debug("imparting cocktail to DAO: " + cocktail + ", Query type: " + queryType);

            switch (queryType) {
                case SAVE:
                    return cocktailDao.saveCreated(userId, cocktail, language);
                case UPDATE:
                    return cocktailDao.updateCreated(userId, cocktail, language);
                default:
                    return false;
            }
        } catch (DataAccessException | IOException e) {
            throw new ServiceException("Query type: " + queryType + ",\n language" + language +
                    ",\n cocktail:" + cocktail, e);
        }
    }

    /**
     * Finds all ingredients of a cocktail
     *
     * @param cocktailList
     * @param language
     */
    private void fillWithIngredient(List<Cocktail> cocktailList, String language, boolean isCreated) throws DataAccessException {
        if (!cocktailList.isEmpty()) {
            for (Cocktail cocktail : cocktailList) {
                cocktail.setIngredientList(cocktailDao.findIngredients(language, cocktail.getId(), isCreated));
            }
        }
    }
}

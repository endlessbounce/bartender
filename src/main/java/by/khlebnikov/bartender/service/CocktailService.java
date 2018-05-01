package by.khlebnikov.bartender.service;

import by.khlebnikov.bartender.constant.ConstParameter;
import by.khlebnikov.bartender.constant.ConstQueryCocktail;
import by.khlebnikov.bartender.constant.Constant;
import by.khlebnikov.bartender.dao.CocktailDao;
import by.khlebnikov.bartender.dao.QueryType;
import by.khlebnikov.bartender.entity.Cocktail;
import by.khlebnikov.bartender.exception.DataAccessException;
import by.khlebnikov.bartender.exception.ServiceException;
import by.khlebnikov.bartender.utility.ImageConverter;
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
 * Class representing service layer for work with cocktail data
 */
public class CocktailService {

    // Vars ----------------------------------------------------------------------------------
    private static Logger logger = LogManager.getLogger();
    private CocktailDao cocktailDao;

    // Constructors --------------------------------------------------------------------------
    public CocktailService() {
        this.cocktailDao = new CocktailDao();
    }

    // Actions -------------------------------------------------------------------------------

    /**
     * Finds a cocktail by its id
     *
     * @param queryType  type of query (find classic or find created cocktail)
     * @param cocktailId cocktail's ID
     * @param language   current user's location
     * @param isCreated  true if the cocktail is created, false if it's classic
     * @return Optional of a Cocktail if it has been found, or an empty Optional otherwise
     * @throws ServiceException is thrown in case of an error in the underlying layers
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

    /**
     * Finds all favourite or created cocktails
     *
     * @param queryType type of query
     * @param language  current locale of a user
     * @param userId    user's ID
     * @param isCreated true if the cocktail is created, false if it's classic
     * @return list of cocktails found
     * @throws ServiceException is thrown in case of an error in the underlying layers
     */
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
     * Finds all cocktails matching to selected options from catalog page (i.e. ingredients, base drink,
     * group)
     *
     * @param params MultivaluedMap of parameters of cocktails selected by user
     * @return list of matching cocktails
     * @throws ServiceException is thrown in case of an error in the underlying layers
     */
    public List<Cocktail> findAllMatching(MultivaluedMap params) throws ServiceException {
        List<Cocktail> cocktailList;
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

        logger.debug("request parameters: " + language + ",\n drinkType: " + drinkType +
                ",\n baseDrink: " + baseDrink + ",\n ingredientList: " + ingredientList);

        try {
            cocktailList = cocktailDao.findAllMatching(language, drinkType, baseDrink, ingredientList);
            fillWithIngredient(cocktailList, language, isCreated);
        } catch (DataAccessException e) {
            throw new ServiceException("Find cocktails by parameters: type of drink: " + drinkType +
                    ", base drink: " + baseDrink + ", ingredients: " + ingredientList + ", language: " + language, e);
        }

        return cocktailList;
    }

    /**
     * Saves given cocktail to the databse
     *
     * @param userId      user's ID
     * @param cocktail    cocktail to save
     * @param params      MultivaluedMap containing parameters of request
     * @param httpRequest HttpServletRequest
     * @return true in case of successful save into the database, false otherwise
     * @throws ServiceException is thrown in case of an error in the underlying layers
     */
    public boolean saveCreated(int userId, Cocktail cocktail, MultivaluedMap params, HttpServletRequest httpRequest)
            throws ServiceException {
        return executeUpdateCocktail(userId, QueryType.SAVE, cocktail, params, httpRequest);
    }

    /**
     * Updates created cocktail
     *
     * @param userId      user's ID
     * @param cocktail    cocktail to update
     * @param params      MultivaluedMap containing parameters of request
     * @param httpRequest HttpServletRequest
     * @return true in case of successful update into the database, false otherwise
     * @throws ServiceException is thrown in case of an error in the underlying layers
     */
    public boolean updateCreated(int userId, Cocktail cocktail, MultivaluedMap params, HttpServletRequest httpRequest)
            throws ServiceException {
        return executeUpdateCocktail(userId, QueryType.UPDATE, cocktail, params, httpRequest);
    }

    /**
     * Executes delete operation over created and favourite cocktails,
     * and also save operation over favourite cocktails
     *
     * @param userId     user's ID
     * @param cocktailId cocktail's ID
     * @param queryType  type of query
     * @return true in case of successful update into the database, false otherwise
     * @throws ServiceException is thrown in case of an error in the underlying layers
     */
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

    /**
     * Executes save or update operation over given created cocktail
     *
     * @param userId      user's ID
     * @param queryType   type of query (save or update)
     * @param cocktail    created cocktail to save or update
     * @param params      MultivaluedMap parameters or request (with locale)
     * @param httpRequest HttpServletRequest
     * @return true in case of successful update into the database, false otherwise
     * @throws ServiceException is thrown in case of an error in the underlying layers
     */
    private boolean executeUpdateCocktail(int userId, QueryType queryType, Cocktail cocktail, MultivaluedMap params, HttpServletRequest httpRequest)
            throws ServiceException {
        String relativePath = httpRequest.getServletContext().getRealPath(Constant.EMPTY);
        String language = (String) params.getFirst(ConstParameter.LOCALE);
        String uri = cocktail.getUri();
        boolean stringOk = Validator.checkString(uri);

        try {
            if (!stringOk) {
                cocktail.setUri(Constant.DEFAULT_COCKTAIL);
            } else if (uri.startsWith(Constant.BASE64_START) && uri.contains(Constant.BASE64)) {
                cocktail.setUri(ImageConverter.convertBase64ToImage(uri, relativePath));
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
     * Fills all the cocktails' lists of ingredients with their's portions
     *
     * @param cocktailList list of cocktails
     * @param language     current location of a user
     * @param isCreated    true if the cocktail is created, false if it's classic
     * @throws DataAccessException is thrown in case of an error in the underlying layers
     */
    private void fillWithIngredient(List<Cocktail> cocktailList, String language, boolean isCreated) throws DataAccessException {
        if (!cocktailList.isEmpty()) {
            for (Cocktail cocktail : cocktailList) {
                cocktail.setIngredientList(cocktailDao.findIngredients(language, cocktail.getId(), isCreated));
            }
        }
    }
}

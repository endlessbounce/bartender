package by.khlebnikov.bartender.dao;

import by.khlebnikov.bartender.constant.*;
import by.khlebnikov.bartender.entity.Cocktail;
import by.khlebnikov.bartender.entity.Portion;
import by.khlebnikov.bartender.exception.DataAccessException;
import by.khlebnikov.bartender.pool.ConnectionPool;
import by.khlebnikov.bartender.reader.PropertyReader;
import by.khlebnikov.bartender.utility.Utility;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.UnsupportedEncodingException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CocktailDao {
    // Constants ----------------------------------------------------------------------------------
    private static String FIND = PropertyReader.getQueryProperty(ConstQueryCocktail.FIND_BY_ID);
    private static String FIND_LANG = PropertyReader.getQueryProperty(ConstQueryCocktail.FIND_BY_ID_LANG);
    private static String FIND_CREATED = PropertyReader.getQueryProperty(ConstQueryCocktail.FIND_CREATED_BY_ID);
    private static String FIND_ALL_CREATED = PropertyReader.getQueryProperty(ConstQueryUser.FIND_ALL_CREATED);
    private static String FIND_ALL_CREATED_LANG = PropertyReader.getQueryProperty(ConstQueryUser.FIND_ALL_CREATED_LANG);
    private static String FIND_ALL_FAVOURITE = PropertyReader.getQueryProperty(ConstQueryUser.FIND_ALL_FAVOURITE);
    private static String FIND_ALL_FAVOURITE_LANG = PropertyReader.getQueryProperty(ConstQueryUser.FIND_ALL_FAVOURITE_LANG);
    private static String INGREDIENT_CREATED = PropertyReader.getQueryProperty(ConstQueryCocktail.INGREDIENT_CREATED);
    private static String INGREDIENT = PropertyReader.getQueryProperty(ConstQueryCocktail.INGREDIENT);
    private static String INGREDIENT_RUS = PropertyReader.getQueryProperty(ConstQueryCocktail.INGREDIENT_RUS);
    private static String LAST_INSERTED_ID = PropertyReader.getQueryProperty(ConstQueryCocktail.LAST_INSERTED_ID);
    private static String SAVE_COCKTAIL = PropertyReader.getQueryProperty(ConstQueryCocktail.SAVE_COCKTAIL);
    private static String SAVE_COCKTAIL_RUS = PropertyReader.getQueryProperty(ConstQueryCocktail.SAVE_COCKTAIL_RUS);
    private static String SAVE_COMBINATION = PropertyReader.getQueryProperty(ConstQueryCocktail.SAVE_COMBINATION);
    private static String SAVE_COMBINATION_RUS = PropertyReader.getQueryProperty(ConstQueryCocktail.SAVE_COMBINATION_RUS);
    private static String DELETE_FAVOURITE = PropertyReader.getQueryProperty(ConstQueryUser.DELETE_FAVOURITE);
    private static String DELETE_CREATED = PropertyReader.getQueryProperty(ConstQueryUser.DELETE_CREATED);
    private static String SAVE_FAVOURITE = PropertyReader.getQueryProperty(ConstQueryUser.SAVE_FAVOURITE);

    // Vars ---------------------------------------------------------------------------------------
    private Logger logger = LogManager.getLogger();
    private ConnectionPool pool = ConnectionPool.getInstance();

    // Actions ------------------------------------------------------------------------------------
    public Optional<Cocktail> findByCocktailId(CocktailQueryType qType, int cocktailId, String language, boolean isCreated)
            throws DataAccessException {
        List<Cocktail> cocktailList = find(cocktailId, defineQuery(ConstLocale.EN.equals(language), qType), isCreated, language);
        return Optional.ofNullable(cocktailList.get(0));
    }

    /**
     * Find favourite or created by user cocktails
     *
     * @param userId
     * @param query
     * @param language
     * @param isCreated
     * @return
     */
    public List<Cocktail> findAllByUserId(CocktailQueryType qType, int userId, String language, boolean isCreated)
            throws DataAccessException {
        return find(userId, defineQuery(ConstLocale.EN.equals(language), qType), isCreated, language);
    }

    /**
     * As user may choose different number of parameters for cocktail,
     * we need to build request each time.
     *
     * @param language       locale of the user
     * @param drinkType      chosen (or not) drink type
     * @param baseDrink      chosen (or not) base drink
     * @param ingredientList chosen ingredients
     * @return list of cocktails which meet all the parameters
     */
    public List<Cocktail> findAllMatching(String language, String drinkType, String baseDrink, ArrayList<String> ingredientList)
            throws DataAccessException {
        ArrayList<Cocktail> result = new ArrayList<>();
        boolean drinkTypeSelected = drinkType != null;
        boolean baseDrinkSelected = baseDrink != null;
        int optionNum = ingredientList.size();
        int queryPosition = 0;
        boolean isCreated = false;

        String query = Utility.buildQuery(drinkTypeSelected, baseDrinkSelected, optionNum, language);
        logger.debug("Search by parameters query: " + query);

        try (Connection connection = pool.getConnection();
             PreparedStatement prepStatement = connection.prepareStatement(query)
        ) {
            if (drinkTypeSelected) {
                prepStatement.setString(++queryPosition, drinkType);
            }

            if (baseDrinkSelected) {
                prepStatement.setString(++queryPosition, baseDrink);
            }

            if (optionNum > 0) {
                for (int i = 0; i < optionNum; i++) {
                    prepStatement.setString(++queryPosition, ingredientList.get(i));
                }
            }

            ResultSet resSet = prepStatement.executeQuery();

            while (resSet.next()) {
                result.add(mapCocktailToObject(resSet, language, isCreated));
            }
        } catch (SQLException | InterruptedException | UnsupportedEncodingException e) {
            throw new DataAccessException("Cocktails found: " + result +
                    ",\n query: " + query, e);
        }

        return result;
    }

    public boolean save(int userId, Cocktail cocktail, String language) throws DataAccessException {
        boolean isEnglish = ConstLocale.EN.equals(language);
        boolean cocktailSaved = false;
        boolean combinationSaved = false;
        int savedCocktailId = 0;
        String querySaveCocktail = defineQuery(isEnglish, CocktailQueryType.SAVE);
        String querySaveCombination = defineQuery(isEnglish, CocktailQueryType.SAVE_COMB);

        try (Connection connection = pool.getConnection()) {

            try (PreparedStatement prepStatementCocktail = connection.prepareStatement(querySaveCocktail);
                 PreparedStatement prepStatementCombination = connection.prepareStatement(querySaveCombination);
                 Statement statement = connection.createStatement()) {
                connection.setAutoCommit(false);

                prepStatementCocktail.setString(1, cocktail.getName());
                prepStatementCocktail.setString(2, cocktail.getRecipe());
                prepStatementCocktail.setString(3, cocktail.getSlogan());
                prepStatementCocktail.setString(4, cocktail.getType());
                prepStatementCocktail.setString(5, cocktail.getBaseDrink());
                prepStatementCocktail.setString(6, cocktail.getUri());
                prepStatementCocktail.setInt(7, userId);

                int firstResult = prepStatementCocktail.executeUpdate();
                cocktailSaved = firstResult == Constant.EQUALS_1;

                //get id of last saved cocktail
                ResultSet resultSet = statement.executeQuery(LAST_INSERTED_ID);
                if (resultSet.next()) {
                    savedCocktailId = resultSet.getInt(1);
                }

                //now save ingredients to combination table
                for (Portion portion : cocktail.getIngredientList()) {
                    prepStatementCombination.setString(1, portion.getIngredientName());
                    prepStatementCombination.setInt(2, savedCocktailId);
                    prepStatementCombination.setString(3, portion.getAmount());
                    int result = prepStatementCombination.executeUpdate();
                    combinationSaved = combinationSaved && (result == Constant.EQUALS_1);
                }

                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                throw new DataAccessException("Cocktail was saved: " + cocktailSaved
                        + ",\n combination was saved: " + combinationSaved, e);
            } finally {
                connection.setAutoCommit(true);
            }

        } catch (SQLException | InterruptedException e) {
            throw new DataAccessException("Transaction: save cocktail query: " + querySaveCocktail +
                    ",\n save ingredients query: " + querySaveCombination +
                    ",\n id of saved cocktail: " + savedCocktailId, e);
        }

        return cocktailSaved && combinationSaved;
    }

    /**
     * Looks for ingredients and their portions for a given cocktail
     *
     * @param language   locale of user
     * @param cocktailId id of cocktail
     * @return the list of ingredients and proportions for a cocktail
     */
    public ArrayList<Portion> findIngredients(String language, int cocktailId, boolean isCreated)
            throws DataAccessException {
        ArrayList<Portion> ingredientList = new ArrayList<>();
        boolean isEnglish = ConstLocale.EN.equals(language);
        String ingredientName = isEnglish ? ConstTableIngredient.NAME : ConstTableIngredient.NAME_RUS;
        String query;
        String ingredientAmount;

        if(isEnglish && isCreated){
            query = INGREDIENT_CREATED;
            ingredientAmount = ConstTableCombination.PORTION_LANG;
        } else if(isEnglish && !isCreated){
            query = INGREDIENT;
            ingredientAmount = ConstTableCombination.PORTION;
        } else {
            query = INGREDIENT_RUS;
            ingredientAmount = ConstTableCombination.PORTION_LANG;
        }

        try (Connection connection = pool.getConnection();
             PreparedStatement prepStatement = connection.prepareStatement(query)
        ) {
            prepStatement.setInt(1, cocktailId);
            ResultSet resultSet = prepStatement.executeQuery();

            while (resultSet.next()) {
                Portion portion = new Portion();
                portion.setIngredientName(resultSet.getString(ingredientName));
                portion.setAmount(resultSet.getString(ingredientAmount));
                ingredientList.add(portion);
                logger.debug("Portion found: " + portion);
            }

        } catch (SQLException | InterruptedException e) {
            throw new DataAccessException("Find ingredients for a cocktail with: cocktail id: " + cocktailId +
                    ", found ingredients: " + ingredientList + ", is created : " + isCreated + ", query: " + query, e);
        }

        return ingredientList;
    }

    public boolean deleteFavouriteCocktail(int userId, int cocktailId) throws DataAccessException {
        return executeUpdateCocktail(userId, cocktailId, DELETE_FAVOURITE);
    }

    public boolean deleteCreatedCocktail(int userId, int cocktailId) throws DataAccessException {
        return executeUpdateCocktail(userId, cocktailId, DELETE_CREATED);
    }

    public boolean saveFavouriteCocktail(int userId, int cocktailId) throws DataAccessException {
        return executeUpdateCocktail(userId, cocktailId, SAVE_FAVOURITE);
    }

    private ArrayList<Cocktail> find(int id, String query, boolean isCreated, String language)
            throws DataAccessException {
        ArrayList<Cocktail> selectedCocktail = new ArrayList<>();

        try (Connection connection = pool.getConnection();
             PreparedStatement prepStatement = connection.prepareStatement(query)
        ) {
            prepStatement.setInt(1, id);
            ResultSet resSet = prepStatement.executeQuery();

            while (resSet.next()) {
                selectedCocktail.add(mapCocktailToObject(resSet, language, isCreated));
            }
        } catch (SQLException | InterruptedException | UnsupportedEncodingException e) {
            throw new DataAccessException("Find by id: " + id +
                    ", chosen language: " + language +
                    ", created by user cocktail: " + isCreated +
                    ", cocktails found: " + selectedCocktail +
                    ", query: " + query, e);
        }

        return selectedCocktail;
    }

    /**
     * Works with favourite or created by user cocktails
     *
     * @param userId
     * @param cocktailId
     * @param query
     * @return
     */
    private boolean executeUpdateCocktail(int userId, int cocktailId, String query)
            throws DataAccessException {
        int updated = 0;
        try (Connection connection = pool.getConnection();
             PreparedStatement prepStatement = connection.prepareStatement(query)
        ) {
            prepStatement.setInt(1, cocktailId);
            prepStatement.setInt(2, userId);
            updated = prepStatement.executeUpdate();
        } catch (InterruptedException | SQLException e) {
            throw new DataAccessException("BD is updated: " + updated, e);
        }
        return updated == Constant.EQUALS_1;
    }

    /**
     * Private method to read cocktails from ResultSet
     *
     * @param resSet   ResultSet of a query
     * @param language Locale language
     * @return read Cocktail
     * @throws SQLException is handled in on this level
     */
    private Cocktail mapCocktailToObject(ResultSet resSet, String language, boolean isCreated)
            throws SQLException, UnsupportedEncodingException {
        Cocktail cocktail = new Cocktail();
        boolean isEnglish = ConstLocale.EN.equals(language);
        String base = isEnglish ? ConstTableCocktail.BASE_NAME : ConstTableCocktail.BASE_NAME_RUS;
        String type = isEnglish ? ConstTableCocktail.GROUP_NAME : ConstTableCocktail.GROUP_NAME_RUS;
        String name = null;
        String recipe = null;
        String slogan = null;

        if (isEnglish && isCreated || !isEnglish) {
            name = ConstTableCocktail.NAME_LANG;
            recipe = ConstTableCocktail.RECIPE_LANG;
            slogan = ConstTableCocktail.SLOGAN_LANG;
        }else if(isEnglish && !isCreated){
            name = ConstTableCocktail.NAME;
            recipe = ConstTableCocktail.RECIPE;
            slogan = ConstTableCocktail.SLOGAN;
        }

        cocktail.setName(resSet.getString(name));
        cocktail.setRecipe(new String(resSet.getString(recipe).getBytes(Constant.ISO_8859), Constant.UTF8));
        cocktail.setSlogan(resSet.getString(slogan));
        cocktail.setBaseDrink(resSet.getString(base));
        cocktail.setType(resSet.getString(type));
        cocktail.setCreationDate(resSet.getDate(ConstTableCocktail.DATE));
        cocktail.setUri(resSet.getString(ConstTableCocktail.URI));
        cocktail.setId(resSet.getInt(ConstTableCocktail.ID));

        return cocktail;
    }

    private String defineQuery(boolean isEnglish, CocktailQueryType queryType) {
        switch (queryType) {
            case FIND:
                return isEnglish ? FIND : FIND_LANG;
            case FIND_CREATED:
                return isEnglish ? FIND_CREATED : FIND_LANG;
            case ALL_FAVOURITE:
                return isEnglish ? FIND_ALL_FAVOURITE : FIND_ALL_FAVOURITE_LANG;
            case ALL_CREATED:
                return isEnglish ? FIND_ALL_CREATED : FIND_ALL_CREATED_LANG;
            case SAVE:
                return isEnglish ? SAVE_COCKTAIL : SAVE_COCKTAIL_RUS;
            case SAVE_COMB:
                return isEnglish ? SAVE_COMBINATION : SAVE_COMBINATION_RUS;
            default:
                return Constant.EMPTY;
        }
    }
}

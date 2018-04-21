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
    private static final String FIND = PropertyReader.getQueryProperty(ConstQueryCocktail.FIND_BY_ID);
    private static final String FIND_LANG = PropertyReader.getQueryProperty(ConstQueryCocktail.FIND_BY_ID_LANG);
    private static final String FIND_CREATED = PropertyReader.getQueryProperty(ConstQueryCocktail.FIND_CREATED_BY_ID);
    private static final String FIND_ALL_CREATED = PropertyReader.getQueryProperty(ConstQueryUser.FIND_ALL_CREATED);
    private static final String FIND_ALL_CREATED_LANG = PropertyReader.getQueryProperty(ConstQueryUser.FIND_ALL_CREATED_LANG);
    private static final String FIND_ALL_FAVOURITE = PropertyReader.getQueryProperty(ConstQueryUser.FIND_ALL_FAVOURITE);
    private static final String FIND_ALL_FAVOURITE_LANG = PropertyReader.getQueryProperty(ConstQueryUser.FIND_ALL_FAVOURITE_LANG);
    private static final String INGREDIENT_CREATED = PropertyReader.getQueryProperty(ConstQueryCocktail.INGREDIENT_CREATED);
    private static final String INGREDIENT = PropertyReader.getQueryProperty(ConstQueryCocktail.INGREDIENT);
    private static final String INGREDIENT_RUS = PropertyReader.getQueryProperty(ConstQueryCocktail.INGREDIENT_RUS);
    private static final String LAST_INSERTED_ID = PropertyReader.getQueryProperty(ConstQueryCocktail.LAST_INSERTED_ID);
    private static final String SAVE_COCKTAIL = PropertyReader.getQueryProperty(ConstQueryCocktail.SAVE_COCKTAIL);
    private static final String SAVE_COCKTAIL_RUS = PropertyReader.getQueryProperty(ConstQueryCocktail.SAVE_COCKTAIL_RUS);
    private static final String SAVE_COMBINATION = PropertyReader.getQueryProperty(ConstQueryCocktail.SAVE_COMBINATION);
    private static final String SAVE_COMBINATION_RUS = PropertyReader.getQueryProperty(ConstQueryCocktail.SAVE_COMBINATION_RUS);
    private static final String DELETE_FAVOURITE = PropertyReader.getQueryProperty(ConstQueryUser.DELETE_FAVOURITE);
    private static final String DELETE_CREATED = PropertyReader.getQueryProperty(ConstQueryUser.DELETE_CREATED);
    private static final String SAVE_FAVOURITE = PropertyReader.getQueryProperty(ConstQueryUser.SAVE_FAVOURITE);
    private static final String UPDATE_CREATED = PropertyReader.getQueryProperty(ConstQueryCocktail.UPDATE_CREATED);
    private static final String UPDATE_CREATED_LANG = PropertyReader.getQueryProperty(ConstQueryCocktail.UPDATE_CREATED_LANG);
    private static final String DELETE_PORTION = PropertyReader.getQueryProperty(ConstQueryCocktail.DELETE_PORTION);

    // Vars ---------------------------------------------------------------------------------------
    private Logger logger = LogManager.getLogger();
    private ConnectionPool pool = ConnectionPool.getInstance();

    // Actions ------------------------------------------------------------------------------------
    public Optional<Cocktail> findByCocktailId(QueryType qType, int cocktailId, String language, boolean isCreated)
            throws DataAccessException {
        List<Cocktail> cocktailList = find(cocktailId, defineQuery(ConstLocale.EN.equals(language), qType), isCreated, language);
        return Optional.ofNullable(cocktailList.get(0));
    }

    public List<Cocktail> findAllByUserId(QueryType qType, int userId, String language, boolean isCreated)
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

    public ArrayList<Portion> findIngredients(String language, int cocktailId, boolean isCreated)
            throws DataAccessException {
        ArrayList<Portion> ingredientList = new ArrayList<>();
        boolean isEnglish = ConstLocale.EN.equals(language);
        String ingredientName = isEnglish ? ConstTableIngredient.NAME : ConstTableIngredient.NAME_RUS;
        String query;
        String ingredientAmount;

        if (isEnglish && isCreated) {
            query = INGREDIENT_CREATED;
            ingredientAmount = ConstTableCombination.PORTION_LANG;
        } else if (isEnglish && !isCreated) {
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
                portion.setId(resultSet.getInt(ConstTableCombination.INGREDIENT_ID));
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

    public boolean saveCreated(int userId, Cocktail cocktail, String language) throws DataAccessException {
        boolean isEnglish = ConstLocale.EN.equals(language);
        String queryCocktail = defineQuery(isEnglish, QueryType.SAVE);
        String queryCombination = defineQuery(isEnglish, QueryType.SAVE_COMB);
        return executeUpdateCocktail(userId, cocktail, queryCocktail, queryCombination, false);
    }

    public boolean saveFavourite(int userId, int cocktailId) throws DataAccessException {
        return executeUpdateCocktail(userId, cocktailId, SAVE_FAVOURITE);
    }

    public boolean updateCreated(int userId, Cocktail cocktail, String language) throws DataAccessException {
        boolean isEnglish = ConstLocale.EN.equals(language);
        String queryCocktail = defineQuery(isEnglish, QueryType.UPDATE);
        String queryCombination = defineQuery(isEnglish, QueryType.SAVE_COMB);
        return executeUpdateCocktail(userId, cocktail, queryCocktail, queryCombination, true);
    }

    public boolean deleteCreated(int userId, int cocktailId) throws DataAccessException {
        return executeUpdateCocktail(userId, cocktailId, DELETE_CREATED);
    }

    public boolean deleteFavourite(int userId, int cocktailId) throws DataAccessException {
        return executeUpdateCocktail(userId, cocktailId, DELETE_FAVOURITE);
    }

    // Helper methods ------------------------------------------------------------------------
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

    private boolean executeUpdateCocktail(int userId, Cocktail cocktail, String queryCocktail, String queryComb, boolean isUpdate)
            throws DataAccessException {
        boolean cocktailSaved = false;
        boolean combinationSaved = false;

        try (Connection connection = pool.getConnection()) {

            try (PreparedStatement prepStCocktail = connection.prepareStatement(queryCocktail);
                 PreparedStatement prepStComb = connection.prepareStatement(queryComb);
                 Statement statement = connection.createStatement()) {
                connection.setAutoCommit(false);

                prepStCocktail.setString(1, cocktail.getName());
                prepStCocktail.setString(2, cocktail.getRecipe());
                prepStCocktail.setString(3, cocktail.getSlogan());
                prepStCocktail.setString(4, cocktail.getType());
                prepStCocktail.setString(5, cocktail.getBaseDrink());
                prepStCocktail.setString(6, cocktail.getUri());

                if (isUpdate) {
                    prepStCocktail.setInt(7, cocktail.getId());
                } else {
                    prepStCocktail.setInt(7, userId);
                }

                cocktailSaved = Constant.EQUALS_1 == prepStCocktail.executeUpdate();
                combinationSaved = isUpdate ? updatePortion(cocktail, prepStComb) : savePortion(cocktail, statement, prepStComb);

                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                throw new DataAccessException("Cocktail was saved: " + cocktailSaved
                        + ",\n combination was saved: " + combinationSaved, e);
            } finally {
                connection.setAutoCommit(true);
            }

        } catch (SQLException | InterruptedException e) {
            throw new DataAccessException("Transaction: save cocktail query: " + queryCocktail +
                    ",\n save ingredients query: " + queryComb, e);
        }

        logger.debug("cocktailSaved: " + cocktailSaved + ", combinationSaved: " + combinationSaved);
        return cocktailSaved && combinationSaved;
    }

    /**
     * Saves portions of a cocktail into the database. Statement being equal to null indicates
     * that a cocktail is being updated, and there's no need to get its ID from the database as it's
     * already present in the cocktail entity
     * @param cocktail to save / update
     * @param statement for execution LAST_INSERTED_ID query
     * @param prepStCombination for multiple execution of INSERT queries into 'combination' table
     * @return
     * @throws SQLException
     */
    private boolean savePortion(Cocktail cocktail, Statement statement, PreparedStatement prepStCombination)
            throws SQLException {
        int savedId = 0;
        boolean combinationSaved = true;

        if(statement != null){
            ResultSet resultSet = statement.executeQuery(LAST_INSERTED_ID);
            if (resultSet.next()) {
                savedId = resultSet.getInt(1);
                logger.debug("saved/updated cocktail's id: " + savedId);
            }
        }else{
            savedId = cocktail.getId();
        }


        // save ingredients to combination table
        for (Portion portion : cocktail.getIngredientList()) {
            prepStCombination.setString(1, portion.getIngredientName());
            prepStCombination.setInt(2, savedId);
            prepStCombination.setString(3, portion.getAmount());
            int result = prepStCombination.executeUpdate();
            combinationSaved = combinationSaved && (result == Constant.EQUALS_1);
        }

        return combinationSaved;
    }

    /**
     * Updates portions by first removing all ingredients of a cocktail and then inserting new records.
     * This is necessary as it's not known beforehand which ingredients will be added or removed by a user.
     *
     * @param cocktail          cocktail being edited
     * @param prepStCombination prepared statement
     * @return boolean result if records have been updated or not
     * @throws SQLException
     * @throws InterruptedException
     */
    private boolean updatePortion(Cocktail cocktail, PreparedStatement prepStCombination)
            throws SQLException, InterruptedException {
        boolean deleted;
        boolean saved;
        int updated;

        try (Connection connection = pool.getConnection();
             PreparedStatement prepStatement = connection.prepareStatement(DELETE_PORTION)
        ) {
            prepStatement.setInt(1, cocktail.getId());
            updated = prepStatement.executeUpdate();
        }
        deleted = updated == Constant.EQUALS_1;
        saved = savePortion(cocktail, null, prepStCombination);

        logger.debug("deleted and saved: " + deleted + " " + saved);
        return deleted && saved;
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
        } else if (isEnglish && !isCreated) {
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

    private String defineQuery(boolean isEnglish, QueryType queryType) {
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
            case UPDATE:
                return isEnglish ? UPDATE_CREATED : UPDATE_CREATED_LANG;
            default:
                return Constant.EMPTY;
        }
    }
}

package by.khlebnikov.bartender.dao;

import by.khlebnikov.bartender.constant.*;
import by.khlebnikov.bartender.entity.Cocktail;
import by.khlebnikov.bartender.entity.Portion;
import by.khlebnikov.bartender.exception.DataAccessException;
import by.khlebnikov.bartender.pool.ConnectionPool;
import by.khlebnikov.bartender.reader.PropertyReader;
import by.khlebnikov.bartender.utility.QueryBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.UnsupportedEncodingException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Class providing methods to access the database and retrieve information about cocktails
 */
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
    private static Logger logger = LogManager.getLogger();

    // Actions ------------------------------------------------------------------------------------

    /**
     * Finds a cocktail by its ID
     *
     * @param qType      type of query
     * @param cocktailId ID of cocktail
     * @param language   current language of a user
     * @param isCreated  true if a cocktail has been created by a user, otherwise false
     * @return empty Optional of the cocktail was not found, otherwise chosen cocktail within Optional
     * @throws DataAccessException is thrown when a database error occurs
     */
    public Optional<Cocktail> findByCocktailId(QueryType qType, int cocktailId, String language, boolean isCreated)
            throws DataAccessException {
        List<Cocktail> cocktailList = find(cocktailId, defineQuery(ConstLocale.EN.equals(language), qType), isCreated, language);
        return Optional.ofNullable(cocktailList.get(0));
    }

    /**
     * Finds all cocktails by user ID (either favourite or created)
     *
     * @param qType     type of query
     * @param userId    ID id a user
     * @param language  current language of a user
     * @param isCreated true if a cocktail has been created by a user, otherwise false
     * @return list of cocktails matched
     * @throws DataAccessException is thrown when a database error occurs
     */
    public List<Cocktail> findAllByUserId(QueryType qType, int userId, String language, boolean isCreated)
            throws DataAccessException {
        return find(userId, defineQuery(ConstLocale.EN.equals(language), qType), isCreated, language);
    }

    /**
     * Finds all cocktails matching selected parameters.
     * As user may choose different number of parameters for a cocktail,
     * we need to build request each time.
     *
     * @param language       current language of a user
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

        String query = QueryBuilder.build(drinkTypeSelected, baseDrinkSelected, optionNum, language);
        logger.debug("Search by parameters query: " + query);

        try (Connection connection = ConnectionPool.getInstance().getConnection();
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
        } catch (SQLException | UnsupportedEncodingException | InterruptedException e) {
            throw new DataAccessException("Cocktails found: " + result +
                    ",\n query: " + query, e);
        }

        return result;
    }

    /**
     * Finds all ingredients that a cocktail includes
     *
     * @param language   current language of a user
     * @param cocktailId ID of cocktail
     * @param isCreated  true if a cocktail has been created by a user, otherwise false
     * @return a list of portions for a cocktail (ingredients and amounts)
     * @throws DataAccessException is thrown when a database error occurs
     */
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

        try (Connection connection = ConnectionPool.getInstance().getConnection();
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
            }

        } catch (SQLException | InterruptedException e) {
            throw new DataAccessException("Find ingredients for a cocktail with: cocktail id: " + cocktailId +
                    ", found ingredients: " + ingredientList + ", is created : " + isCreated + ", query: " + query, e);
        }

        return ingredientList;
    }

    /**
     * Saves created by a user cocktail to the database
     *
     * @param userId   ID of a user
     * @param cocktail cocktail to save
     * @param language chosen current language of a user
     * @return true if the cocktail has been saved successfully, otherwise false
     * @throws DataAccessException is thrown when a database error occurs
     */
    public boolean saveCreated(int userId, Cocktail cocktail, String language) throws DataAccessException {
        boolean isEnglish = ConstLocale.EN.equals(language);
        String queryCocktail = defineQuery(isEnglish, QueryType.SAVE);
        String queryCombination = defineQuery(isEnglish, QueryType.SAVE_COMB);
        return executeUpdateCocktail(userId, cocktail, queryCocktail, queryCombination, false);
    }

    /**
     * Saves liked by a user cocktail to the database
     *
     * @param userId     ID of a user
     * @param cocktailId ID of a cocktail
     * @return true if the cocktail has been saved to 'favourite' table, otherwise false
     * @throws DataAccessException is thrown when a database error occurs
     */
    public boolean saveFavourite(int userId, int cocktailId) throws DataAccessException {
        return executeUpdateCocktail(userId, cocktailId, SAVE_FAVOURITE);
    }

    /**
     * Updates created by a user cocktail
     *
     * @param userId   ID of a user
     * @param cocktail cocktail to update
     * @param language chosen current language of a user
     * @return true if the cocktail has been updated successfully, false otherwise
     * @throws DataAccessException is thrown when a database error occurs
     */
    public boolean updateCreated(int userId, Cocktail cocktail, String language) throws DataAccessException {
        boolean isEnglish = ConstLocale.EN.equals(language);
        String queryCocktail = defineQuery(isEnglish, QueryType.UPDATE);
        String queryCombination = defineQuery(isEnglish, QueryType.SAVE_COMB);
        return executeUpdateCocktail(userId, cocktail, queryCocktail, queryCombination, true);
    }

    /**
     * Deletes created by a user cocktail
     *
     * @param userId     ID of a user
     * @param cocktailId ID of a cocktail to delete
     * @return true if the cocktail has been deleted successfully, false otherwise
     * @throws DataAccessException is thrown when a database error occurs
     */
    public boolean deleteCreated(int userId, int cocktailId) throws DataAccessException {
        return executeUpdateCocktail(userId, cocktailId, DELETE_CREATED);
    }

    /**
     * Removes a cocktail from the list of user's favourite cocktails
     *
     * @param userId     ID of a user
     * @param cocktailId ID of a cocktail to remove
     * @return true if the cocktail has been removed successfully, false otherwise
     * @throws DataAccessException is thrown when a database error occurs
     */
    public boolean deleteFavourite(int userId, int cocktailId) throws DataAccessException {
        return executeUpdateCocktail(userId, cocktailId, DELETE_FAVOURITE);
    }

    // Helper methods -----------------------------------------------------------------------------

    /**
     * Finds cocktails either by their ID or user's ID
     *
     * @param id        of a cocktail or a user, depending on the query
     * @param query     query to execute
     * @param isCreated true if a cocktail is created, and false otherwise (vintage cocktail)
     * @param language  current language of a user
     * @return list of cocktails found
     * @throws DataAccessException
     */
    private ArrayList<Cocktail> find(int id, String query, boolean isCreated, String language)
            throws DataAccessException {
        ArrayList<Cocktail> selectedCocktail = new ArrayList<>();

        try (Connection connection = ConnectionPool.getInstance().getConnection();
             PreparedStatement prepStatement = connection.prepareStatement(query)
        ) {
            prepStatement.setInt(1, id);
            ResultSet resSet = prepStatement.executeQuery();

            while (resSet.next()) {
                selectedCocktail.add(mapCocktailToObject(resSet, language, isCreated));
            }
        } catch (SQLException | UnsupportedEncodingException | InterruptedException e) {
            throw new DataAccessException("Find by id: " + id +
                    ", chosen language: " + language +
                    ", created by user cocktail: " + isCreated +
                    ", cocktails found: " + selectedCocktail +
                    ", query: " + query, e);
        }

        return selectedCocktail;
    }

    /**
     * Executes delete favourite or created cocktail operations, and save favourite operation
     *
     * @param userId     ID of a user
     * @param cocktailId ID of a cocktail
     * @param query      query to execute
     * @return true if the database has been updated successfully, false otherwise
     */
    private boolean executeUpdateCocktail(int userId, int cocktailId, String query)
            throws DataAccessException {
        try (Connection connection = ConnectionPool.getInstance().getConnection();
             PreparedStatement prepStatement = connection.prepareStatement(query)
        ) {
            prepStatement.setInt(1, cocktailId);
            prepStatement.setInt(2, userId);
            return prepStatement.executeUpdate() == Constant.EQUALS_1;
        } catch (SQLException | InterruptedException e) {
            throw new DataAccessException("Failed to update.", e);
        }
    }

    /**
     * Executes update / save operations
     *
     * @param userId        ID of a user
     * @param cocktail      cocktail to save / uprate
     * @param queryCocktail query to save the cocktail
     * @param queryComb     query to save portions of ingredients
     * @param isUpdate      true if it's update query, false if it's save query
     * @return true if the cocktail has been updated / saved successfully, false otherwise
     * @throws DataAccessException is thrown when a database error occurs
     */
    private boolean executeUpdateCocktail(int userId, Cocktail cocktail, String queryCocktail, String queryComb, boolean isUpdate)
            throws DataAccessException {
        boolean cocktailSaved = false;
        boolean combinationSaved = false;

        try (Connection connection = ConnectionPool.getInstance().getConnection()) {

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
                combinationSaved = isUpdate
                        ? updatePortion(cocktail, prepStComb)
                        : savePortion(cocktail, statement, prepStComb);

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
     *
     * @param cocktail          cocktail to save / update
     * @param statement         for execution LAST_INSERTED_ID query
     * @param prepStCombination for multiple execution of INSERT queries into 'combination' table
     * @return true if the portion has been saved successfully, false otherwise
     * @throws SQLException is thrown when a database error occurs
     */
    private boolean savePortion(Cocktail cocktail, Statement statement, PreparedStatement prepStCombination)
            throws SQLException {
        int savedId = 0;
        boolean combinationSaved = true;

        if (statement != null) {
            ResultSet resultSet = statement.executeQuery(LAST_INSERTED_ID);
            if (resultSet.next()) {
                savedId = resultSet.getInt(1);
                logger.debug("saved/updated cocktail's id: " + savedId);
            }
        } else {
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
     * @throws SQLException         is thrown when a database error occurs
     * @throws InterruptedException is thrown when a database error occurs
     */
    private boolean updatePortion(Cocktail cocktail, PreparedStatement prepStCombination)
            throws SQLException, InterruptedException, DataAccessException {
        boolean deleted;
        boolean saved;
        int updated;

        try (Connection connection = ConnectionPool.getInstance().getConnection();
             PreparedStatement prepStatement = connection.prepareStatement(DELETE_PORTION)
        ) {
            prepStatement.setInt(1, cocktail.getId());
            updated = prepStatement.executeUpdate();
        }
        deleted = updated == Constant.EQUALS_1;
        saved = savePortion(cocktail, null, prepStCombination);

        logger.debug("update portion meth: deleted and saved: " + deleted + " " + saved);
        return deleted && saved;
    }

    /**
     * Read cocktails from ResultSet.
     *
     * @param resSet    ResultSet of a query
     * @param language  current language of a user
     * @param isCreated indicates whether a cocktail is a classic one or a user-created one
     * @return mapped to object cocktail
     * @throws SQLException is thrown when a database error occurs
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

    /**
     * Defines query by its type and language
     *
     * @param isEnglish true if current locale is English, false otherwise
     * @param queryType type of a query
     * @return selected query
     */
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

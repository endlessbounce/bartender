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
    private Logger logger = LogManager.getLogger();
    private ConnectionPool pool = ConnectionPool.getInstance();

    /**
     * Find a cocktail by its ID
     *
     * @param cocktailId
     * @param language
     * @param isCreated
     * @param query
     * @return
     */
    public Optional<Cocktail> find(int cocktailId, String language, boolean isCreated, String query) throws DataAccessException {
        Optional<Cocktail> result = Optional.empty();
        Cocktail cocktail = null;

        try (Connection connection = pool.getConnection();
             PreparedStatement prepStatement = connection.prepareStatement(query)
        ) {
            prepStatement.setInt(1, cocktailId);
            ResultSet resultSet = prepStatement.executeQuery();

            if (resultSet.next()) {
                cocktail = mapCocktailToObject(resultSet, language, isCreated);
                result = Optional.of(cocktail);
            }

        } catch (SQLException | InterruptedException | UnsupportedEncodingException e) {
            throw new DataAccessException("Cocktail found: " + cocktail, e);
        }

        return result;
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
    public List<Cocktail> findAll(int userId, String query, String language, boolean isCreated) throws DataAccessException {
        ArrayList<Cocktail> selectedCocktail = new ArrayList<>();

        try (Connection connection = pool.getConnection();
             PreparedStatement prepStatement = connection.prepareStatement(query)
        ) {
            prepStatement.setInt(1, userId);
            ResultSet resSet = prepStatement.executeQuery();

            while (resSet.next()) {
                selectedCocktail.add(mapCocktailToObject(resSet, language, isCreated));
            }
        } catch (SQLException | InterruptedException | UnsupportedEncodingException e) {
            throw new DataAccessException("Find all cocktails created by user with: id of user: " + userId +
                    ", chosen language: " + language +
                    ", created by user cocktail: " + isCreated +
                    ", cocktails found: " + selectedCocktail +
                    ", query: " + query, e);
        }

        return selectedCocktail;
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
    public List<Cocktail> findAllMatching(String language,
                                          String drinkType,
                                          String baseDrink,
                                          ArrayList<String> ingredientList) throws DataAccessException {
        ArrayList<Cocktail> result = new ArrayList<>();
        boolean drinkTypeSelected = false;
        boolean baseDrinkSelected = false;
        int optionNum = ingredientList.size();
        int queryPosition = 0;
        boolean isCreated = false;

        if (drinkType != null) {
            drinkTypeSelected = true;
        }

        if (baseDrink != null) {
            baseDrinkSelected = true;
        }

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

    /**
     * Looks for ingredients and their portions for a given cocktail
     *
     * @param language   locale of user
     * @param cocktailId id of cocktail
     * @return the list of ingredients and proportions for a cocktail
     */
    public ArrayList<Portion> findIngredients(String language, int cocktailId) throws DataAccessException {
        ArrayList<Portion> ingredientList = new ArrayList<>();
        String query;
        String ingredientName;
        String amountOfIngredient;

        if (ConstLocale.EN.equals(language)) {
            query = PropertyReader.getQueryProperty(ConstQueryCocktail.INGREDIENT);
            ingredientName = ConstTableIngredient.NAME;
            amountOfIngredient = ConstTableCombination.PORTION;
        } else {
            query = PropertyReader.getQueryProperty(ConstQueryCocktail.INGREDIENT_RUS);
            ingredientName = ConstTableIngredient.NAME_RUS;
            amountOfIngredient = ConstTableCombination.PORTION_LANG;
        }

        try (Connection connection = pool.getConnection();
             PreparedStatement prepStatement = connection.prepareStatement(query)
        ) {
            prepStatement.setInt(1, cocktailId);
            ResultSet resultSet = prepStatement.executeQuery();

            while (resultSet.next()) {
                Portion portion = new Portion();
                portion.setIngredientName(resultSet.getString(ingredientName));
                portion.setAmount(resultSet.getString(amountOfIngredient));
                ingredientList.add(portion);
            }

        } catch (SQLException | InterruptedException e) {
            throw new DataAccessException("Find ingredients for a cocktail with: cocktail id: " + cocktailId +
                    ", found ingredients: " + ingredientList +
                    ", query: " + query, e);
        }

        return ingredientList;
    }

    public boolean save(int userId, Cocktail cocktail, String language) throws DataAccessException {
        boolean combinationSaved = false;
        boolean cocktailSaved = false;
        int savedCocktailId = 0;
        String querySaveCocktail;
        String querySaveCombination;
        String queryLastSavedId = PropertyReader.getQueryProperty(ConstQueryCocktail.LAST_INSERTED_ID);

        if (ConstLocale.EN.equals(language)) {
            querySaveCocktail = PropertyReader.getQueryProperty(ConstQueryCocktail.SAVE_COCKTAIL);
            querySaveCombination = PropertyReader.getQueryProperty(ConstQueryCocktail.SAVE_COMBINATION);
        } else {
            querySaveCocktail = PropertyReader.getQueryProperty(ConstQueryCocktail.SAVE_COCKTAIL_RUS);
            querySaveCombination = PropertyReader.getQueryProperty(ConstQueryCocktail.SAVE_COMBINATION_RUS);
        }

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
                ResultSet resultSet = statement.executeQuery(queryLastSavedId);

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
     * Private method to read cocktails from ResultSet
     *
     * @param resSet   ResultSet of a query
     * @param language Locale language
     * @return read Cocktail
     * @throws SQLException is handled in on this level
     */
    private Cocktail mapCocktailToObject(ResultSet resSet,
                                         String language,
                                         boolean isCreated)
            throws SQLException, UnsupportedEncodingException {
        Cocktail cocktail = new Cocktail();

        String name;
        String recipe;
        String slogan;
        String base;
        String type;

        if (ConstLocale.EN.equals(language)) {

            if (isCreated) {
                name = ConstTableCocktail.NAME_LANG;
                recipe = ConstTableCocktail.RECIPE_LANG;
                slogan = ConstTableCocktail.SLOGAN_LANG;
            } else {
                name = ConstTableCocktail.NAME;
                recipe = ConstTableCocktail.RECIPE;
                slogan = ConstTableCocktail.SLOGAN;
            }

            base = ConstTableCocktail.BASE_NAME;
            type = ConstTableCocktail.GROUP_NAME;
        } else {
            name = ConstTableCocktail.NAME_LANG;
            recipe = ConstTableCocktail.RECIPE_LANG;
            slogan = ConstTableCocktail.SLOGAN_LANG;
            base = ConstTableCocktail.BASE_NAME_RUS;
            type = ConstTableCocktail.GROUP_NAME_RUS;
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
}

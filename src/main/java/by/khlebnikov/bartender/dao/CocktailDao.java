package by.khlebnikov.bartender.dao;

import by.khlebnikov.bartender.constant.*;
import by.khlebnikov.bartender.entity.Cocktail;
import by.khlebnikov.bartender.entity.Portion;
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


    public Optional<Cocktail> findCocktail(int cocktailId, String language) {
        Optional<Cocktail> result = Optional.empty();
        String query;
        String name;
        String recipe;
        String slogan;
        String base;
        String type;

        if (ConstLocale.EN.equals(language)) {
            query = PropertyReader.getQueryProperty(ConstQueryCocktail.FIND_BY_ID);
            name = ConstTableCocktail.NAME;
            recipe = ConstTableCocktail.RECIPE;
            slogan = ConstTableCocktail.SLOGAN;
            base = ConstTableCocktail.BASE_NAME;
            type = ConstTableCocktail.GROUP_NAME;
        } else {
            query = PropertyReader.getQueryProperty(ConstQueryCocktail.FIND_BY_ID_LANG);
            name = ConstTableCocktail.NAME_LANG;
            recipe = ConstTableCocktail.RECIPE_LANG;
            slogan = ConstTableCocktail.SLOGAN_LANG;
            base = ConstTableCocktail.BASE_NAME_RUS;
            type = ConstTableCocktail.GROUP_NAME_RUS;
        }

        try (Connection connection = pool.getConnection();
             PreparedStatement prepStatement = connection.prepareStatement(query)
        ) {
            prepStatement.setInt(1, cocktailId);
            ResultSet resultSet = prepStatement.executeQuery();

            if (resultSet.next()) {
                Cocktail cocktail = new Cocktail();
                cocktail.setName(resultSet.getString(name));
                try {
                    cocktail.setRecipe(new String(resultSet.getString(recipe).getBytes(Constant.ISO_8859), Constant.UTF8));
                } catch (UnsupportedEncodingException e) {
                    logger.debug(e);
                }
                cocktail.setSlogan(resultSet.getString(slogan));
                cocktail.setBaseDrink(resultSet.getString(base));
                cocktail.setType(resultSet.getString(type));
                cocktail.setCreationDate(resultSet.getDate(ConstTableCocktail.DATE));
                cocktail.setUri(resultSet.getString(ConstTableCocktail.URI));
                cocktail.setId(cocktailId);
                result = Optional.of(cocktail);
            }

        } catch (SQLException | InterruptedException e) {
            logger.catching(e);
        }

        return result;
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
    public List<Cocktail> findAllByParameter(String language,
                                             String drinkType,
                                             String baseDrink,
                                             ArrayList<String> ingredientList) {

        ArrayList<Cocktail> result = new ArrayList<>();

        logger.debug("request parameters: " + language
                + drinkType + ", drinkType: "
                + baseDrink + ", baseDrink: "
                + ingredientList + ", ingredientList: ");

        String query = Utility.buildQuery(language, drinkType, baseDrink, ingredientList);
        String name;

        logger.debug("buildQuery: " + query);


        if (ConstLocale.EN.equals(language)) {
            name = ConstTableCocktail.NAME;
        } else {
            name = ConstTableCocktail.NAME_LANG;
        }

        try (Connection connection = pool.getConnection();
             Statement statement = connection.createStatement()
        ) {
            ResultSet resSet = statement.executeQuery(query);

            while (resSet.next()) {
                result.add(readCocktail(resSet, name));
            }
        } catch (SQLException | InterruptedException e) {
            logger.catching(e);
        }

        return result;
    }


    public List<Cocktail> findAllFavourite(String language, int userId) {
        ArrayList<Cocktail> favouriteList = new ArrayList<>();
        String query;
        String name;

        if (ConstLocale.EN.equals(language)) {
            query = PropertyReader.getQueryProperty(ConstQueryUser.FIND_ALL_FAVOURITE);
            name = ConstTableCocktail.NAME;
        } else {
            query = PropertyReader.getQueryProperty(ConstQueryUser.FIND_ALL_FAVOURITE_LANG);
            name = ConstTableCocktail.NAME_LANG;
        }

        logger.debug("findAllFavourite: " + query + " lang: " + language);

        try (Connection connection = pool.getConnection();
             PreparedStatement prepStatement = connection.prepareStatement(query)
        ) {
            prepStatement.setInt(1, userId);
            ResultSet resSet = prepStatement.executeQuery();

            while (resSet.next()) {
                favouriteList.add(readCocktail(resSet, name));
            }
        } catch (SQLException | InterruptedException e) {
            logger.catching(e);
        }

        return favouriteList;
    }

    /**
     * Looks for ingredients and their portions for a given cocktail
     *
     * @param language   locale of user
     * @param cocktailId id of cocktail
     * @return the list of ingredients and proportions for a cocktail
     */
    public ArrayList<Portion> findIngredients(String language, int cocktailId) {
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

        logger.debug("findIngredients query: " + query);

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
            logger.catching(e);
        }

        return ingredientList;
    }

    public boolean saveCreated(int userId, Cocktail cocktail, String language) {
        boolean cocktailSaved = false;
        boolean combinationSaved = false;

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

        logger.debug("save cocktail = " + querySaveCocktail + "\n save combination = " + querySaveCombination);

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
                int savedCocktailId = 0;

                if(resultSet.next()){
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
                logger.catching(e);
            } finally {
                connection.setAutoCommit(true);
            }

        } catch (SQLException | InterruptedException e) {
            logger.catching(e);
        }

        return cocktailSaved && combinationSaved;
    }

    /**
     * Private method to read cocktails from ResultSet
     *
     * @param resSet ResultSet of a query
     * @param name   Cocktail name
     * @return read Cocktail
     * @throws SQLException is handled in on this level
     */
    private Cocktail readCocktail(ResultSet resSet, String name) throws SQLException {
        Cocktail cocktail = new Cocktail();
        cocktail.setName(resSet.getString(name));
        cocktail.setUri(resSet.getString(ConstTableCocktail.URI));
        cocktail.setId(Integer.parseInt(resSet.getString(ConstTableCocktail.ID)));
        return cocktail;
    }
}

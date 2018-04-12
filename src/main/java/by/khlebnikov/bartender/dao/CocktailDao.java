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
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CocktailDao {
    private Logger logger = LogManager.getLogger();
    private ConnectionPool pool = ConnectionPool.getInstance();


    public Optional<Cocktail> findCocktail(int id, String language) {
        Optional<Cocktail> result = Optional.empty();
        StringBuilder query = new StringBuilder();
        String name;
        String recipe;
        String slogan;
        String base;
        String type;

        if (ConstLocale.EN.equals(language)) {
            query.append(PropertyReader.getQueryProperty(ConstQueryCocktail.FIND_BY_ID));
            name = ConstTableCocktail.NAME;
            recipe = ConstTableCocktail.RECIPE;
            slogan = ConstTableCocktail.SLOGAN;
            base = ConstTableCocktail.BASE_NAME;
            type = ConstTableCocktail.GROUP_NAME;
        } else {
            query.append(PropertyReader.getQueryProperty(ConstQueryCocktail.FIND_BY_ID_LANG));
            name = ConstTableCocktail.NAME_LANG;
            recipe = ConstTableCocktail.RECIPE_LANG;
            slogan = ConstTableCocktail.SLOGAN_LANG;
            base = ConstTableCocktail.BASE_NAME_RUS;
            type = ConstTableCocktail.GROUP_NAME_RUS;
        }

        query.append(id).append(Constant.QUOTE_SEMOCOLON);

        try (Connection connection = pool.getConnection();
             Statement statement = connection.createStatement()
        ) {
            ResultSet rs = statement.executeQuery(query.toString());

            if (rs.next()) {
                Cocktail cocktail = new Cocktail();
                cocktail.setName(rs.getString(name));
                try {
                    cocktail.setRecipe(new String(rs.getString(recipe).getBytes(Constant.ISO_8859), Constant.UTF8));
                } catch (UnsupportedEncodingException e) {
                    logger.debug(e);
                }
                cocktail.setSlogan(rs.getString(slogan));
                cocktail.setBaseDrink(rs.getString(base));
                cocktail.setType(rs.getString(type));
                cocktail.setCreationDate(rs.getDate(ConstTableCocktail.DATE));
                cocktail.setUri(rs.getString(ConstTableCocktail.URI));
                cocktail.setId(id);
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

        String query = new Utility().buildQuery(language, drinkType, baseDrink, ingredientList);

        try (Connection connection = pool.getConnection();
             Statement statement = connection.createStatement()
        ) {
            ResultSet rs = statement.executeQuery(query);
            String name;

            if (ConstLocale.EN.equals(language)) {
                name = ConstTableCocktail.NAME;
            } else {
                name = ConstTableCocktail.NAME_LANG;
            }

            while (rs.next()) {
                Cocktail cocktail = new Cocktail();
                cocktail.setName(rs.getString(name));
                cocktail.setUri(rs.getString(ConstTableCocktail.URI));
                cocktail.setId(Integer.parseInt(rs.getString(ConstTableCocktail.ID)));
                result.add(cocktail);
            }

        } catch (SQLException | InterruptedException e) {
            logger.catching(e);
        }

        return result;
    }

    /**
     * Looks for ingredients and their portions for a given cocktail
     *
     * @param language locale of user
     * @param id       id of cocktail
     * @return the list of ingredients and proportions for a cocktail
     */
    public ArrayList<Portion> findIngredients(String language, int id) {
        ArrayList<Portion> ingredientList = new ArrayList<>();
        StringBuilder query = new StringBuilder();
        String ingredientName;
        String amountOfIngredient;

        if (ConstLocale.EN.equals(language)) {
            query.append(PropertyReader.getQueryProperty(ConstQueryCocktail.INGREDIENT));
            ingredientName = ConstTableIngredient.NAME;
            amountOfIngredient = ConstTableCombination.PORTION;
        } else {
            query.append(PropertyReader.getQueryProperty(ConstQueryCocktail.INGREDIENT_RUS));
            ingredientName = ConstTableIngredient.NAME_RUS;
            amountOfIngredient = ConstTableCombination.PORTION_LANG;
        }

        query.append(Constant.QUOTE)
                .append(id)
                .append(Constant.QUOTE_SEMOCOLON);

        logger.debug("chosen query: " + query);

        try (Connection connection = pool.getConnection();
             Statement statement = connection.createStatement()
        ) {
            ResultSet rs = statement.executeQuery(query.toString());

            while (rs.next()) {
                Portion portion = new Portion();
                portion.setIngredientName(rs.getString(ingredientName));
                portion.setAmount(rs.getString(amountOfIngredient));
                ingredientList.add(portion);
            }

        } catch (SQLException | InterruptedException e) {
            logger.catching(e);
        }

        return ingredientList;
    }
}

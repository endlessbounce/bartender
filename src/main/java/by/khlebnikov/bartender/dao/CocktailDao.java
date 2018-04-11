package by.khlebnikov.bartender.dao;

import by.khlebnikov.bartender.constant.ConstLocale;
import by.khlebnikov.bartender.constant.ConstQueryCocktail;
import by.khlebnikov.bartender.constant.ConstTableCocktail;
import by.khlebnikov.bartender.entity.Cocktail;
import by.khlebnikov.bartender.entity.Portion;
import by.khlebnikov.bartender.pool.ConnectionPool;
import by.khlebnikov.bartender.reader.PropertyReader;
import by.khlebnikov.bartender.utility.Utility;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CocktailDao {
    private Logger logger = LogManager.getLogger();
    private ConnectionPool pool = ConnectionPool.getInstance();

    /**
     * As user may choose different number of parameters for cocktail,
     * we need to build request each time.
     *
     * @param locale locale of the user
     * @param drinkType chosen (or not) drink type
     * @param baseDrink chosen (or not) base drink
     * @param ingredientList chosen ingredients
     * @return list of cocktails which meet all the parameters
     */
    public List<Cocktail> findAllByParameter(String locale,
                                             String drinkType,
                                             String baseDrink,
                                             ArrayList<String> ingredientList) {

        ArrayList<Cocktail> result = new ArrayList<>();

        logger.debug("request parameters: " + locale + ", locale: "
                + drinkType + ", drinkType: "
                + baseDrink + ", baseDrink: "
                + ingredientList + ", ingredientList: ");

        String query = new Utility().buildQuery(locale, drinkType, baseDrink, ingredientList);

        try (Connection connection = pool.getConnection();
             Statement statement = connection.createStatement()
        ) {
            ResultSet rs = statement.executeQuery(query);
            String columnName;

            if (ConstLocale.EN.equals(locale)) {
                columnName = ConstTableCocktail.NAME;
            }else{
                columnName = ConstTableCocktail.NAME_LANG;
            }

            while (rs.next()) {
                Cocktail cocktail = new Cocktail();
                cocktail.setName(rs.getString(columnName));
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
     * Fills ingredients of cocktails with their parameters
     * @param locale of the user
     * @param cocktail
     */
    public void findIngredients(String locale, Cocktail cocktail){
        String query;

        if (ConstLocale.EN.equals(locale)) {
            query = PropertyReader.getQueryProperty(ConstQueryCocktail.INGREDIENT) +
                    "\"" + cocktail.getId() + "\";";
        } else {
            query = PropertyReader.getQueryProperty(ConstQueryCocktail.INGREDIENT_RUS) +
                    "\"" + cocktail.getId() + "\";";
        }

        logger.debug("chosen query: " + query);

        try (Connection connection = pool.getConnection();
             Statement statement = connection.createStatement()
        ) {
            ResultSet rs = statement.executeQuery(query);

            cocktail.setIngredientList(new ArrayList<>());

            while (rs.next()) {
                Portion portion = new Portion();
                portion.setIngredientName(rs.getString(1));
                cocktail.getIngredientList().add(portion);
            }

        } catch (SQLException | InterruptedException e) {
            logger.catching(e);
        }
    }
}

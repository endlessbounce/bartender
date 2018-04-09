package by.khlebnikov.bartender.specification;

import by.khlebnikov.bartender.constant.*;
import by.khlebnikov.bartender.entity.Cocktail;
import by.khlebnikov.bartender.entity.Portion;
import by.khlebnikov.bartender.pool.ConnectionPool;
import by.khlebnikov.bartender.reader.PropertyReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Fetches ingredients of a given cocktail
 */
public class FindIngredientOfCocktail implements Specification<Cocktail>{
    private Logger logger = LogManager.getLogger();
    private ConnectionPool pool;
    private String locale;
    private Cocktail cocktail;

    public FindIngredientOfCocktail(String locale, Cocktail cocktail) {
        this.locale = locale;
        this.cocktail = cocktail;
        this.pool = ConnectionPool.getInstance();
    }

    @Override
    public List<Cocktail> specified() {
        ArrayList<Cocktail> result = new ArrayList<>();
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

        return result;
    }
}

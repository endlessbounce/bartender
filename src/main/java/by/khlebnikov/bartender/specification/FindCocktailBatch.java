package by.khlebnikov.bartender.specification;

import by.khlebnikov.bartender.constant.ConstLocale;
import by.khlebnikov.bartender.constant.ConstQuery;
import by.khlebnikov.bartender.constant.ConstQueryCocktail;
import by.khlebnikov.bartender.constant.ConstTableCocktail;
import by.khlebnikov.bartender.entity.Cocktail;
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

public class FindCocktailBatch implements Specification<Cocktail> {
    private Logger logger = LogManager.getLogger();
    private ConnectionPool pool;
    private String locale;
    private String append;

    public FindCocktailBatch(String locale, long limit, long offset) {
        this.locale = locale;
        this.append = ConstQuery.LIMIT + limit + ConstQuery.OFFSET + offset + ";";
        this.pool = ConnectionPool.getInstance();
    }

    @Override
    public List<Cocktail> specified() {
        ArrayList<Cocktail> result = new ArrayList<>();
        String query;

        if (ConstLocale.EN.equals(locale)) {
            query = PropertyReader.getQueryProperty(ConstQueryCocktail.NEXT_BATCH) + append;
        } else {
            query = PropertyReader.getQueryProperty(ConstQueryCocktail.NEXT_BATCH_LANG) + append;
        }

        try (Connection connection = pool.getConnection();
             Statement statement = connection.createStatement()
        ) {
            ResultSet rs = statement.executeQuery(query);

            while (rs.next()) {
                Cocktail cocktail = new Cocktail();
                cocktail.setName(rs.getString(ConstTableCocktail.NAME));
                cocktail.setUri(rs.getString(ConstTableCocktail.URI));
                cocktail.setId(Integer.parseInt(rs.getString(ConstTableCocktail.ID)));
                result.add(cocktail);
            }

        } catch (SQLException | InterruptedException e) {
            logger.catching(e);
        }

        return result;
    }
}

package by.khlebnikov.bartender.repository;

import by.khlebnikov.bartender.entity.Cocktail;
import by.khlebnikov.bartender.pool.ConnectionPool;
import by.khlebnikov.bartender.specification.Specification;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class CocktailDao {
    private Logger logger = LogManager.getLogger();
    private ConnectionPool pool = ConnectionPool.getInstance();


    public List<Cocktail> query(Specification spec){
        return spec.specified();
    }
}

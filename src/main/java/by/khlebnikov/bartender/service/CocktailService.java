package by.khlebnikov.bartender.service;

import by.khlebnikov.bartender.entity.Cocktail;
import by.khlebnikov.bartender.repository.CocktailDao;
import by.khlebnikov.bartender.specification.FindCocktailBatch;
import by.khlebnikov.bartender.specification.FindIngredientOfCocktail;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class CocktailService {
    private Logger logger = LogManager.getLogger();
    CocktailDao cocktailDao;

    public CocktailService() {
        this.cocktailDao = new CocktailDao();
    }

    public List<Cocktail> getNextBatch(String locale, long limit, long offset){

        //get next <limit> records from cocktails table, starting from <offset>
        List<Cocktail> cocktailList = cocktailDao.query(new FindCocktailBatch(locale, limit, offset));
        logger.debug("next batch: " + cocktailList);

        /*fill each cocktail's list of portions with ingredients*/
        cocktailList.forEach(cocktail -> cocktailDao.query(new FindIngredientOfCocktail(locale, cocktail)));
        logger.debug("next batch with ingredients: " + cocktailList);

        return cocktailList;
    }
}

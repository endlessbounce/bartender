package by.khlebnikov.bartender.service;

import by.khlebnikov.bartender.constant.ConstLocale;
import by.khlebnikov.bartender.constant.ConstQueryCatalog;
import by.khlebnikov.bartender.dao.CatalogDao;
import by.khlebnikov.bartender.reader.PropertyReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

/**
 * This class serves the catalog page of the application
 * providing information to REST resources.
 * These methods return data to fill in cocktail selection froms.
 */
public class CatalogService {
    private Logger logger = LogManager.getLogger();
    private CatalogDao dao;

    public CatalogService() {
        this.dao = new CatalogDao();
    }

    public ArrayList<String> findDrinkTypeAll(String locale) {
        String query = "";

        if (ConstLocale.EN.equals(locale)) {
            query = PropertyReader.getQueryProperty(ConstQueryCatalog.DRINK_TYPE);
        } else {
            query = PropertyReader.getQueryProperty(ConstQueryCatalog.DRINK_TYPE_LANG);
        }

        logger.debug("findDrinkTypeAll query " + query);
        return dao.findFormData(query);
    }

    public ArrayList<String> findBaseDrinkAll(String locale) {
        String query = "";

        if (ConstLocale.EN.equals(locale)) {
            query = PropertyReader.getQueryProperty(ConstQueryCatalog.BASE_DRINK);
        } else {
            query = PropertyReader.getQueryProperty(ConstQueryCatalog.BASE_DRINK_LANG);
        }

        logger.debug("findBaseDrinkAll query " + query);
        return dao.findFormData(query);
    }

    public ArrayList<String> findIngredientAll(String locale) {
        String query = "";

        if (ConstLocale.EN.equals(locale)) {
            query = PropertyReader.getQueryProperty(ConstQueryCatalog.INGREDIENT);
        } else {
            query = PropertyReader.getQueryProperty(ConstQueryCatalog.INGREDIENT_LANG);
        }

        logger.debug("findIngredientAll query " + query);
        return dao.findFormData(query);
    }
}

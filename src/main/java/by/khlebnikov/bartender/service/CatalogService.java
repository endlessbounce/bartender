package by.khlebnikov.bartender.service;

import by.khlebnikov.bartender.dao.CatalogDao;
import by.khlebnikov.bartender.dao.QueryType;
import by.khlebnikov.bartender.exception.DataAccessException;
import by.khlebnikov.bartender.exception.ServiceException;

import java.util.ArrayList;

/**
 * Returns data to fill in cocktail selection forms.
 */
public class CatalogService {
    // Vars ---------------------------------------------------------------------------------------
    private CatalogDao dao;

    // Constructors -------------------------------------------------------------------------------
    public CatalogService() {
        this.dao = new CatalogDao();
    }

    // Actions ------------------------------------------------------------------------------------
    public ArrayList<String> findFormData(QueryType type, String language) throws ServiceException {
        ArrayList<String> result;

        try {
            switch (type) {
                case BASE_DRINK:
                    result = dao.findBaseDrink(language);
                    break;
                case INGREDIENT:
                    result = dao.findIngredient(language);
                    break;
                case DRINK_GROUP:
                    result = dao.findDrinkGroup(language);
                    break;
                default:
                    result = new ArrayList<>();
            }
        } catch (DataAccessException e) {
            throw new ServiceException("Chosen data type: " + type + ",\n language: " + language, e);
        }

        return result;
    }
}

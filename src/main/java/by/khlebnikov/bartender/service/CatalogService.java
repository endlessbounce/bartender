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

    /**
     * Finds form data according to the QueryType: base drink or drink group for dropdowns,
     * and ingredients for checkboxes.
     *
     * @param type     of query
     * @param language current location of a user
     * @return list of items for the form
     * @throws ServiceException is thrown in case of an error in the underlying layers
     */
    public ArrayList<String> findFormData(QueryType type, String language) throws ServiceException {
        try {
            switch (type) {
                case BASE_DRINK:
                    return dao.findBaseDrink(language);
                case INGREDIENT:
                    return dao.findIngredient(language);
                case DRINK_GROUP:
                    return dao.findDrinkGroup(language);
                default:
                    return new ArrayList<>();
            }
        } catch (DataAccessException e) {
            throw new ServiceException("Chosen data type: " + type + ",\n language: " + language, e);
        }
    }
}

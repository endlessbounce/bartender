package by.khlebnikov.bartender.service;

import by.khlebnikov.bartender.dao.CatalogDao;
import by.khlebnikov.bartender.exception.DataAccessException;
import by.khlebnikov.bartender.exception.ServiceException;

import java.util.ArrayList;

/**
 * Returns data to fill in cocktail selection forms.
 */
public class CatalogService {
    private CatalogDao dao;

    public CatalogService() {
        this.dao = new CatalogDao();
    }

    public ArrayList<String> findFormData(String query) throws ServiceException {
        ArrayList<String> result;

        try {
            result = dao.findFormData(query);
        } catch (DataAccessException e) {
            throw new ServiceException("Query: " + query, e);
        }

        return result;
    }
}

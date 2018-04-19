package by.khlebnikov.bartender.resource;

import by.khlebnikov.bartender.constant.ConstLocale;
import by.khlebnikov.bartender.constant.ConstQueryCatalog;
import by.khlebnikov.bartender.entity.FormData;
import by.khlebnikov.bartender.exception.ResourceException;
import by.khlebnikov.bartender.exception.ServiceException;
import by.khlebnikov.bartender.reader.PropertyReader;
import by.khlebnikov.bartender.service.CatalogService;
import by.khlebnikov.bartender.validator.Validator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/catalog/form/data")
public class CatalogResource {
    private Logger logger = LogManager.getLogger();
    private CatalogService service;

    public CatalogResource() {
        this.service = new CatalogService();
    }

    /**
     * Returns an entity of FormData, containing information for dropdown lists and
     * checkboxes
     * @param locale current locale of a user
     * @return FormData entity
     * @throws ResourceException is thrown in case of internal server exception
     */
    @GET
    @Path("/{locale}")
    @Produces(MediaType.APPLICATION_JSON)
    public FormData getCatalogFormData(@PathParam("locale") String locale) throws ResourceException {
        boolean correctString = Validator.checkString(locale);
        FormData data = new FormData();
        String queryIngredient;
        String queryBase;
        String queryType;

        if (correctString) {
            locale = locale.trim();

            if (ConstLocale.EN.equals(locale)) {
                queryIngredient = PropertyReader.getQueryProperty(ConstQueryCatalog.INGREDIENT);
                queryBase = PropertyReader.getQueryProperty(ConstQueryCatalog.BASE_DRINK);
                queryType = PropertyReader.getQueryProperty(ConstQueryCatalog.DRINK_TYPE);
            } else {
                queryIngredient = PropertyReader.getQueryProperty(ConstQueryCatalog.INGREDIENT_LANG);
                queryBase = PropertyReader.getQueryProperty(ConstQueryCatalog.BASE_DRINK_LANG);
                queryType = PropertyReader.getQueryProperty(ConstQueryCatalog.DRINK_TYPE_LANG);
            }

            logger.debug("resource: /catalog/form/data" + " locale " + locale);

            try {
                data.setIngredient(service.findFormData(queryIngredient));
                data.setBaseDrink(service.findFormData(queryBase));
                data.setDrinkType(service.findFormData(queryType));
            } catch (ServiceException e) {
                throw new ResourceException("Chosen locale: " + locale, e);
            }
        }

        logger.debug(data.toString());
        return data;
    }
}
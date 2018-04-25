package by.khlebnikov.bartender.resource;

import by.khlebnikov.bartender.dao.QueryType;
import by.khlebnikov.bartender.entity.FormData;
import by.khlebnikov.bartender.exception.ResourceException;
import by.khlebnikov.bartender.exception.ServiceException;
import by.khlebnikov.bartender.service.CatalogService;
import by.khlebnikov.bartender.validator.Validator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Class providing a resource to get form data for catalog page (dropdowns and checkboxes)
 */
@Path("/catalog/form/data")
public class CatalogResource {

    // Vars ---------------------------------------------------------------------------------------
    private static Logger logger = LogManager.getLogger();
    private CatalogService catalogService;

    // Constructors -------------------------------------------------------------------------------
    public CatalogResource() {
        this.catalogService = new CatalogService();
    }

    // Resources ----------------------------------------------------------------------------------

    /**
     * Returns an entity of FormData, containing information for dropdown lists and
     * checkboxes
     *
     * @param language current language of a user
     * @return FormData entity
     * @throws ResourceException is thrown in case of internal server exception
     */
    @GET
    @Path("/{language}")
    @Produces(MediaType.APPLICATION_JSON)
    public FormData getCatalogFormData(@PathParam("language") String language) throws ResourceException {
        boolean correctString = Validator.checkString(language);
        FormData data = new FormData();

        if (correctString) {
            language = language.trim();

            try {
                data.setIngredient(catalogService.findFormData(QueryType.INGREDIENT, language));
                data.setBaseDrink(catalogService.findFormData(QueryType.BASE_DRINK, language));
                data.setDrinkType(catalogService.findFormData(QueryType.DRINK_GROUP, language));
            } catch (ServiceException e) {
                throw new ResourceException("Chosen language: " + language, e);
            }
        }

        logger.debug("resource: /catalog/form/data" + " language " + language +
                       "\nreturn FormData: " + data.toString());
        return data;
    }
}
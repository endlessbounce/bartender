package by.khlebnikov.bartender.resource;

import by.khlebnikov.bartender.entity.FormData;
import by.khlebnikov.bartender.service.CatalogService;
import by.khlebnikov.bartender.validator.Validator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;

@Path("/catalog/form/data")
public class CatalogResource {
    private Logger logger = LogManager.getLogger();
    private CatalogService service;

    public CatalogResource() {
        this.service = new CatalogService();
    }

    /*map method to an HTTP method*/
    @GET
    @Path("/{locale}")
    @Produces(MediaType.APPLICATION_JSON)
    public FormData getCatalogFormData(@PathParam("locale") String locale){
        boolean correctString = Validator.checkString(locale);
        FormData data = new FormData();

        if(correctString){
            locale = locale.trim();

            logger.debug("resource: /catalog/form/data" + " locale " + locale);

            data.setBaseDrink(service.findBaseDrinkAll(locale));
            data.setDrinkType(service.findDrinkTypeAll(locale));
            data.setIngredient(service.findIngredientAll(locale));
        }

        logger.debug(data.toString());
        return data;
    }
}
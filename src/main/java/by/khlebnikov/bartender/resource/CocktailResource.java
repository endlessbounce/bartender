package by.khlebnikov.bartender.resource;

import by.khlebnikov.bartender.entity.Cocktail;
import by.khlebnikov.bartender.exception.ResourceException;
import by.khlebnikov.bartender.exception.ServiceException;
import by.khlebnikov.bartender.service.CocktailService;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriInfo;
import java.util.List;

/**
 * Jersey creates new resources for every request
 */
@Path("/cocktails")
public class CocktailResource {
    private CocktailService cocktailService;

    public CocktailResource() {
        this.cocktailService = new CocktailService();
    }

    /*map method to an HTTP method*/
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Cocktail> getCocktail(@Context UriInfo uriInfo) throws ResourceException {
        MultivaluedMap params = uriInfo.getQueryParameters();
        List<Cocktail> cocktailList;

        try {
            cocktailList = cocktailService.findAllMatching(params);
        } catch (ServiceException e) {
            throw new ResourceException("Exception while reading cocktails", e);
        }

        return cocktailList;
    }
}

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
 * Class providing a resource to obtaining all cocktails' data.
 */
@Path("/cocktails")
public class CocktailResource {

    // Vars ---------------------------------------------------------------------------------------
    private CocktailService cocktailService;

    // Constructors -------------------------------------------------------------------------------
    public CocktailResource() {
        this.cocktailService = new CocktailService();
    }

    // Resources ----------------------------------------------------------------------------------

    /**
     * Receives a string of parameters chosen by a user to filter cocktails
     *
     * @param uriInfo URI Info containing request parameters
     * @return a list of matching to the parameters cocktails
     * @throws ResourceException
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Cocktail> getCocktail(@Context UriInfo uriInfo) throws ResourceException {
        MultivaluedMap params = uriInfo.getQueryParameters();

        try {
            return cocktailService.findAllMatching(params);
        } catch (ServiceException e) {
            throw new ResourceException("Exception while reading cocktails", e);
        }
    }
}

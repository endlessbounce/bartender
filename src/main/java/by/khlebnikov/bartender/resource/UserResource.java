package by.khlebnikov.bartender.resource;

import by.khlebnikov.bartender.constant.ConstLocale;
import by.khlebnikov.bartender.constant.ConstParameter;
import by.khlebnikov.bartender.constant.ConstQueryUser;
import by.khlebnikov.bartender.entity.Cocktail;
import by.khlebnikov.bartender.exception.ResourceException;
import by.khlebnikov.bartender.exception.ServiceException;
import by.khlebnikov.bartender.reader.PropertyReader;
import by.khlebnikov.bartender.service.CocktailService;
import by.khlebnikov.bartender.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriInfo;
import java.util.List;


/**
 * User resource provides API to work with cocktail and user data
 */
@Path("/user")
@Produces(MediaType.APPLICATION_JSON)//applies to each method
@Consumes(MediaType.APPLICATION_JSON)
public class UserResource {
    private Logger logger = LogManager.getLogger();
    private UserService userService;
    private CocktailService cocktailService;
    @Context
    private HttpServletRequest httpRequest;

    public UserResource() {
        this.userService = new UserService();
        this.cocktailService = new CocktailService();
    }

    /*map method to an HTTP method*/
    @GET
    @Path("/{userId}/favourite/{cocktailId}")
    public Cocktail isFavourite(
            @PathParam("userId") int userId,
            @PathParam("cocktailId") int cocktailId) {
        boolean isFavourite = userService.isFavouriteCocktail(userId, cocktailId);
        Cocktail cocktail = new Cocktail();

        //to confirm simply return new object with id of the cocktail
        if (isFavourite) {
            cocktail.setId(cocktailId);
        }

        return cocktail;
    }

    @GET
    @Path("/{userId}/favourite")
    public List<Cocktail> getAllFavourite(
            @PathParam("userId") int userId,
            @Context UriInfo uriInfo) {
        MultivaluedMap params = uriInfo.getQueryParameters();
        String language = (String) params.getFirst(ConstParameter.LOCALE);
        boolean isCreated = false;
        String query;

        if (ConstLocale.EN.equals(language)) {
            query = PropertyReader.getQueryProperty(ConstQueryUser.FIND_ALL_FAVOURITE);
        } else {
            query = PropertyReader.getQueryProperty(ConstQueryUser.FIND_ALL_FAVOURITE_LANG);
        }
        logger.debug("getAllFavourite: " + query);

        return cocktailService.findAll(language, query, userId, isCreated);
    }


    @GET
    @Path("/{userId}/created")
    public List<Cocktail> getAllCreated(
            @PathParam("userId") int userId,
            @Context UriInfo uriInfo) {
        MultivaluedMap params = uriInfo.getQueryParameters();
        String language = (String) params.getFirst(ConstParameter.LOCALE);
        boolean isCreated = true;
        String query;

        if (ConstLocale.EN.equals(language)) {
            query = PropertyReader.getQueryProperty(ConstQueryUser.FIND_ALL_CREATED);
        } else {
            query = PropertyReader.getQueryProperty(ConstQueryUser.FIND_ALL_CREATED_LANG);
        }
        logger.debug("findAllCreated: " + query);

        return cocktailService.findAll(language, query, userId, isCreated);
    }

    @DELETE
    @Path("/{userId}/favourite/{cocktailId}")
    public void deleteFromFavourite(
            @PathParam("userId") int userId,
            @PathParam("cocktailId") int cocktailId) {
        userService.deleteFromFavourite(userId, cocktailId);
    }

    @POST
    @Path("/{userId}/favourite")
    public void addToFavourite(
            @PathParam("userId") int userId, Cocktail cocktail) {
        logger.debug("POSTing cocktail to favourite: " + cocktail);
        userService.addFavourite(userId, cocktail.getId());
    }

    @POST
    @Path("/{userId}/created")
    public void addCreated(
            @PathParam("userId") int userId,
            @Context UriInfo uriInfo,
            Cocktail cocktail) throws ResourceException {
        logger.debug("POSTing created cocktail for user: " + userId + " .... " + cocktail);

        MultivaluedMap params = uriInfo.getQueryParameters();

        try {
            cocktailService.save(userId, cocktail, httpRequest, params);
        } catch (ServiceException e) {
            throw new ResourceException(e);
        }
    }
}
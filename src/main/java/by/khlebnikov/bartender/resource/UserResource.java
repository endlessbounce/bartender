package by.khlebnikov.bartender.resource;

import by.khlebnikov.bartender.constant.ConstParameter;
import by.khlebnikov.bartender.constant.ConstQueryUser;
import by.khlebnikov.bartender.dao.QueryType;
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
@Produces(MediaType.APPLICATION_JSON)
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

    /**
     * Finds a favourite cocktail by its id
     *
     * @param userId     user's ID
     * @param cocktailId favourite cocktail's ID
     * @return Cocktail entity
     * @throws ResourceException in case of internal exception
     */
    @GET
    @Path("/{userId}/favourite/{cocktailId}")
    public Cocktail isFavourite(
            @PathParam("userId") int userId,
            @PathParam("cocktailId") int cocktailId) throws ResourceException {
        Cocktail cocktail = new Cocktail();
        boolean isFavourite;

        try {
            isFavourite = userService.isFavouriteCocktail(userId, cocktailId);
        } catch (ServiceException e) {
            throw new ResourceException("Failed to figure out if cocktail + " + cocktailId +
                    " is favourite for user " + userId, e);
        }

        //to confirm simply return new object with id of the cocktail
        if (isFavourite) {
            cocktail.setId(cocktailId);
        }

        return cocktail;
    }

    /**
     * Finds all favourite cocktails of given user
     *
     * @param userId  user's ID
     * @param uriInfo info of URI to get request parameters
     * @return list of favourite cocktails
     * @throws ResourceException in case of internal exception
     */
    @GET
    @Path("/{userId}/favourite")
    public List<Cocktail> getAllFavourite(
            @PathParam("userId") int userId,
            @Context UriInfo uriInfo) throws ResourceException {
        MultivaluedMap params = uriInfo.getQueryParameters();
        String language = (String) params.getFirst(ConstParameter.LOCALE);
        boolean isCreated = false;

        try {
            return cocktailService.findAll(QueryType.ALL_FAVOURITE, language, userId, isCreated);
        } catch (ServiceException e) {
            throw new ResourceException("Failed to find favourite cocktails for user id=" + userId, e);
        }
    }


    /**
     * Finds all created by user cocktails
     *
     * @param userId  user's ID
     * @param uriInfo info of URI to get request parameters
     * @return list of created cocktails
     * @throws ResourceException in case of internal exception
     */
    @GET
    @Path("/{userId}/created")
    public List<Cocktail> getAllCreated(
            @PathParam("userId") int userId,
            @Context UriInfo uriInfo) throws ResourceException {
        MultivaluedMap params = uriInfo.getQueryParameters();
        String language = (String) params.getFirst(ConstParameter.LOCALE);
        boolean isCreated = true;

        try {
            return cocktailService.findAll(QueryType.ALL_CREATED, language, userId, isCreated);
        } catch (ServiceException e) {
            throw new ResourceException("Failed to find created cocktails for user id=" + userId, e);
        }
    }

    /**
     * Deletes chosen cocktail from list of favourite ones
     *
     * @param userId     user's ID
     * @param cocktailId cocktail's ID
     * @throws ResourceException in case of internal exception
     */
    @DELETE
    @Path("/{userId}/favourite/{cocktailId}")
    public void deleteFromFavourite(
            @PathParam("userId") int userId,
            @PathParam("cocktailId") int cocktailId) throws ResourceException {
        String queryDeleteFavourite = PropertyReader.getQueryProperty(ConstQueryUser.DELETE_FAVOURITE);
        try {
            cocktailService.executeUpdateCocktail(userId, cocktailId, queryDeleteFavourite);
        } catch (ServiceException e) {
            throw new ResourceException("Failed to delete favourite cocktail with id: " + cocktailId +
                    " for user id: " + userId, e);
        }
    }

    /**
     * Deletes a cocktail from the list of created ones
     *
     * @param userId     user's ID
     * @param cocktailId cocktail's ID
     * @throws ResourceException in case of internal exception
     */
    @DELETE
    @Path("/{userId}/created/{cocktailId}")
    public void deleteCreated(
            @PathParam("userId") int userId,
            @PathParam("cocktailId") int cocktailId) throws ResourceException {
        String deleteQuery = PropertyReader.getQueryProperty(ConstQueryUser.DELETE_CREATED);

        try {
            cocktailService.executeUpdateCocktail(userId, cocktailId, deleteQuery);
        } catch (ServiceException e) {
            throw new ResourceException("Failed to delete created cocktail with id: " + cocktailId +
                    " for user id: " + userId, e);
        }
    }

    /**
     * Saves favourite cocktail to the database
     *
     * @param userId   user's ID
     * @param cocktail cocktail entity
     * @throws ResourceException in case of internal exception
     */
    @POST
    @Path("/{userId}/favourite")
    public void addToFavourite(
            @PathParam("userId") int userId, Cocktail cocktail) throws ResourceException {
        String addFavouriteQuery = PropertyReader.getQueryProperty(ConstQueryUser.SAVE_FAVOURITE);
        logger.debug("POSTing cocktail to favourite: " + cocktail);

        try {
            cocktailService.executeUpdateCocktail(userId, cocktail.getId(), addFavouriteQuery);
        } catch (ServiceException e) {
            throw new ResourceException("Failed to add cocktail: " + cocktail +
                    " to favourite for user id: " + userId, e);
        }
    }

    /**
     * Saves created by user cocktail to the database
     *
     * @param userId   user's ID
     * @param uriInfo  info of URI to get request parameters
     * @param cocktail cocktail entity
     * @throws ResourceException in case of internal exception
     */
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

    @PUT
    @Path("/{userId}/created/{cocktailId}")
    public void updateCreated(
            @PathParam("userId") int userId,
            @PathParam("cocktailId") int cocktailId,
            @Context UriInfo uriInfo,
            Cocktail cocktail) throws ResourceException {
        logger.debug("POSTing created cocktail for user: " + userId + " .... " + cocktail);

        MultivaluedMap params = uriInfo.getQueryParameters();

        try {
            cocktailService.save(userId, cocktail, httpRequest, params);
        } catch (ServiceException e) {
            throw new ResourceException("Error while updating cocktail id=" + cocktail +
                    " for user id=" + userId, e);
        }
    }
}
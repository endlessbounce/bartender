package by.khlebnikov.bartender.resource;

import by.khlebnikov.bartender.constant.ConstAttribute;
import by.khlebnikov.bartender.constant.ConstParameter;
import by.khlebnikov.bartender.constant.ConstQueryCocktail;
import by.khlebnikov.bartender.constant.Constant;
import by.khlebnikov.bartender.dao.QueryType;
import by.khlebnikov.bartender.entity.Cocktail;
import by.khlebnikov.bartender.entity.User;
import by.khlebnikov.bartender.exception.ResourceException;
import by.khlebnikov.bartender.exception.ServiceException;
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
import java.util.Optional;

/**
 * User resource provides API to work with cocktail and user data
 */
@Path("/user")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserResource {

    // Vars ---------------------------------------------------------------------------------------
    private static Logger logger = LogManager.getLogger();
    private UserService userService;
    private CocktailService cocktailService;
    @Context
    private HttpServletRequest httpRequest;

    // Constructors -------------------------------------------------------------------------------
    public UserResource() {
        this.userService = new UserService();
        this.cocktailService = new CocktailService();
    }

    // Resources ----------------------------------------------------------------------------------

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
        try {
            cocktailService.executeUpdateCocktail(userId, cocktailId, QueryType.DELETE_FAVOURITE);
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

        try {
            cocktailService.executeUpdateCocktail(userId, cocktailId, QueryType.DELETE_CREATED);
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
        logger.debug("POSTing cocktail to favourite: " + cocktail);
        try {
            cocktailService.executeUpdateCocktail(userId, cocktail.getId(), QueryType.SAVE_FAVOURITE);
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
            cocktailService.saveCreated(userId, cocktail, params, httpRequest);
        } catch (ServiceException e) {
            throw new ResourceException(e);
        }
    }

    /**
     * Updates created by a user cocktail
     *
     * @param userId     user's ID
     * @param cocktailId cocktail's ID
     * @param uriInfo    info of URI to get request parameters from
     * @param cocktail   cocktail to update
     * @throws ResourceException
     */
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
            cocktailService.updateCreated(userId, cocktail, params, httpRequest);
        } catch (ServiceException e) {
            throw new ResourceException("Error while updating cocktail id=" + cocktail +
                    " for user id=" + userId, e);
        }
    }

    /**
     * Deletes a user from the database
     *
     * @param userId user's ID
     * @throws ResourceException in case of internal exception
     */
    @DELETE
    @Path("/{userId}")
    public void deleteUser(
            @PathParam("userId") int userId) throws ResourceException {
        try {
            userService.deleteUser(userId);
        } catch (ServiceException e) {
            throw new ResourceException("Failed to delete user id:" + userId, e);
        }
    }

    /**
     * Updates created by a user cocktail
     *
     * @param userId     user's ID
     * @param cocktailId cocktail's ID
     * @param uriInfo    info of URI to get request parameters from
     * @param cocktail   cocktail to update
     * @throws ResourceException
     */
    @PUT
    @Path("/{userId}")
    public void updateUser(
            @PathParam("userId") int userId,
            @Context UriInfo uriInfo,
            User user) throws ResourceException {
        MultivaluedMap params = uriInfo.getQueryParameters();
        boolean isNameChanged = false;
        boolean isUpdated;
        String value;

        try {
            Optional<User> userOptional = userService.findUserById(Integer.toString(userId));

            if (userOptional.isPresent()) {
                User oldUser = userOptional.get();

                for (Object key : params.keySet()) {
                    value = (String) params.getFirst(key);

                    switch (value) {
                        case ConstParameter.NAME:
                            oldUser.setName(user.getName());
                            isNameChanged = true;
                            break;
                        case ConstParameter.PASSWORD:

                            break;
                        case ConstParameter.EMAIL:
                            oldUser.setEmail(user.getEmail());
                            break;
                    }
                }

                isUpdated = userService.updateUser(oldUser);

                if (isNameChanged && isUpdated) {
                    httpRequest.getSession().setAttribute(ConstAttribute.USER_NAME, user.getName());
                } else if(!isUpdated){
                    throw new ResourceException("User wasn't updated: " + user);
                }
            }

        } catch (ServiceException e) {
            throw new ResourceException("Error updating user: " + user, e);
        }
    }
}
package by.khlebnikov.bartender.resource;

import by.khlebnikov.bartender.entity.Cocktail;
import by.khlebnikov.bartender.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;


/**
 * User resource provides API to work with cocktail and user data
 */
@Path("/user")
@Produces(MediaType.APPLICATION_JSON)//applies to each method
@Consumes(MediaType.APPLICATION_JSON)
public class UserResource {
    private Logger logger = LogManager.getLogger();
    private UserService userService;

    public UserResource() {
        this.userService = new UserService();
    }

    /*map method to an HTTP method*/
    @GET
    @Path("/{userId}/favourite/{cocktailId}")
    public Cocktail isFavourite(
            @PathParam("userId") int userId,
            @PathParam("cocktailId") int cocktailId)
    {
        boolean isFavourite = userService.isFavouriteCocktail(userId, cocktailId);
        Cocktail cocktail = new Cocktail();

        //to confirm simply return new object with id of the cocktail
        if(isFavourite){
            cocktail.setId(cocktailId);
        }

        return cocktail;
    }

    @DELETE
    @Path("/{userId}/favourite/{cocktailId}")
    public void deleteFromFavourite(
            @PathParam("userId") int userId,
            @PathParam("cocktailId") int cocktailId)
    {
        userService.deleteFromFavourite(userId, cocktailId);
    }

    @POST
    @Path("/{userId}/favourite")
    public void addToFavourite(
            @PathParam("userId") int userId, Cocktail cocktail)
    {
        logger.debug("POSTing cocktail: " + cocktail);
        userService.addFavourite(userId, cocktail.getId());
    }
}
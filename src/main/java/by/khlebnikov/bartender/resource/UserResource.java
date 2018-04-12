package by.khlebnikov.bartender.resource;

import by.khlebnikov.bartender.entity.Cocktail;
import by.khlebnikov.bartender.service.UserService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;


/**
 * User resource provides API to work with cocktail and user data
 */
@Path("/user")
public class UserResource {
    private UserService userService;

    public UserResource() {
        this.userService = new UserService();
    }

    /*map method to an HTTP method*/
    @GET
    @Path("/{userId}/favourite/{cocktailId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Cocktail getCatalogFormData(
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
    @Produces(MediaType.APPLICATION_JSON)
    public Cocktail deleteFromFavourite(
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
}
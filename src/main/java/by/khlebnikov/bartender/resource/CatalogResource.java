package by.khlebnikov.bartender.resource;

import by.khlebnikov.bartender.entity.Cocktail;
import by.khlebnikov.bartender.service.CatalogService;
import by.khlebnikov.bartender.service.CocktailService;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/catalog/form/data")
public class CatalogResource {
    private CatalogService service;

    public CatalogResource() {
        this.service = new CatalogService();
    }

    /*map method to an HTTP method*/
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Cocktail> getCatalogFormData(){
        return null;
    }
}
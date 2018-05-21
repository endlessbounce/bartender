package test.khlebnikov.bartender.service;

import by.khlebnikov.bartender.dao.QueryType;
import by.khlebnikov.bartender.entity.Cocktail;
import by.khlebnikov.bartender.entity.Portion;
import by.khlebnikov.bartender.exception.ServiceException;
import by.khlebnikov.bartender.service.CocktailService;
import org.mockito.Mockito;
import org.testng.Assert;
import org.testng.annotations.Test;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.MultivaluedHashMap;
import java.util.ArrayList;
import java.util.Random;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class CocktailServiceTest {
    private static final int USER_ID = 60;
    private static final int FAVOURITE_ID = 9;
    private CocktailService service = new CocktailService();

    @Test
    public void saveCreatedEn() throws Exception {
        Assert.assertTrue(processCreated("EN",
                generateName(),
                "Liqueur",
                "Bahama Mama Tropical Drinks",
                "honey liqueur"
                , "save"));
    }

    @Test
    public void saveCreatedRu() throws Exception {
        Assert.assertTrue(processCreated("RU",
                generateName(),
                "Смеси",
                "Старинные коктейли",
                "яблочный сок",
                "save"));
    }

    @Test(threadPoolSize = 2, invocationCount = 5, timeOut = 10000)
    public void updateCreatedEn() throws Exception {
        Assert.assertTrue(processCreated("EN",
                generateName(),
                "Liqueur",
                "Bahama Mama Tropical Drinks",
                "honey liqueur",
                "update"));
    }

    @Test
    public void updateCreatedRu() throws Exception {
        Assert.assertTrue(processCreated("RU",
                generateName(),
                "Смеси",
                "Старинные коктейли",
                "яблочный сок",
                "update"));
    }

    @Test
    public void executeUpdateCocktailSaveFav() throws Exception {
        Assert.assertTrue(service.executeUpdateCocktail(USER_ID, FAVOURITE_ID, QueryType.SAVE_FAVOURITE));
    }

    @Test
    public void findFavourite() throws Exception {
        Assert.assertTrue(service.find(QueryType.FIND, FAVOURITE_ID, "EN", false)
                .isPresent());
    }

    @Test
    public void findCreated() throws Exception {
        Assert.assertTrue(service.find(QueryType.FIND_CREATED, randomCreatedCocktailId(), "EN", true)
                .isPresent());
    }

    @Test
    public void findAllFavourite() throws Exception {
        Assert.assertTrue(service.findAll(QueryType.ALL_FAVOURITE, "EN", USER_ID, false).size() > 0);
    }

    @Test
    public void findAllCreated() throws Exception {
        Assert.assertTrue(service.findAll(QueryType.ALL_CREATED, "EN", USER_ID, true).size() > 0);
    }

    @Test
    public void executeUpdateCocktailDeleteFav() throws Exception {
        Assert.assertTrue(service.executeUpdateCocktail(USER_ID, FAVOURITE_ID, QueryType.DELETE_FAVOURITE));
    }

    @Test
    public void executeUpdateCocktailDeleteCreated() throws Exception {
        Assert.assertTrue(service.executeUpdateCocktail(USER_ID, randomCreatedCocktailId(), QueryType.DELETE_CREATED));
    }

    @Test
    public void findAllMatchingEn() throws Exception {
        Assert.assertTrue(findAllMatching("Champagne Concoctions",
                "Liquor",
                "EN",
                "vodka",
                "APPLE STRUDEL"));
    }

    @Test
    public void findAllMatchingRu() throws Exception {
        Assert.assertTrue(findAllMatching("Шампанские смеси",
                "Спиртное",
                "RU",
                "водка",
                "ЯБЛОЧНЫЙ ШТРУДЕЛЬ"));
    }

    private boolean findAllMatching(String type,
                                    String base,
                                    String lang,
                                    String ingred,
                                    String name) throws Exception {
        MultivaluedHashMap map = new MultivaluedHashMap();
        map.add("type", type);
        map.add("base", base);
        map.add("locale", lang);
        map.add("ingredient1", ingred);

        return service.findAllMatching(map)
                .stream()
                .peek(System.out::println)
                .filter(cockt -> name.equals(cockt.getName()))
                .findAny()
                .isPresent();
    }

    /**
     * Tests update and save cocktail functions
     */
    private boolean processCreated(String lang,
                                   String name,
                                   String base,
                                   String type,
                                   String ingredient,
                                   String queryType) throws Exception {
        HttpServletRequest mockedRequest = Mockito.mock(HttpServletRequest.class);
        ArrayList<Portion> ingredients = new ArrayList<>();
        MultivaluedHashMap map = new MultivaluedHashMap();

        ingredients.add(new Portion(ingredient, "a bit"));
        map.add("locale", lang);

        Cocktail cocktail = new Cocktail(name, "test recipe", base, type, null, ingredients);

        switch (queryType) {
            case "save":
                return service.saveCreated(USER_ID, cocktail, map, mockedRequest);
            case "update":
                cocktail.setId(randomCreatedCocktailId());
                return service.updateCreated(USER_ID, cocktail, map, mockedRequest);
            default:
                return false;
        }

    }

    private int randomCreatedCocktailId() throws ServiceException {
        return service.findAll(QueryType.ALL_CREATED, "EN", USER_ID, true)
                .stream()
                .findAny()
                .orElse(new Cocktail())
                .getId();

    }

    private String generateName() {
        return new Random().ints(97, 122)
                .limit(10)
                .mapToObj(i -> String.valueOf((char) i))
                .collect(Collectors.joining());
    }
}

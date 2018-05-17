package test.khlebnikov.bartender.service;

import by.khlebnikov.bartender.constant.ConstLocale;
import by.khlebnikov.bartender.dao.QueryType;
import by.khlebnikov.bartender.service.CatalogService;
import org.testng.Assert;
import org.testng.annotations.Test;

public class CatalogServiceTest {
    CatalogService service = new CatalogService();

    @Test
    public void findFormDataIngredient() throws Exception {
        Assert.assertNotEquals(findFormData(QueryType.INGREDIENT, ConstLocale.EN, "vodka"), null);
    }

    @Test
    public void findFormDataIngredientRu() throws Exception {
        Assert.assertNotEquals(findFormData(QueryType.INGREDIENT, ConstLocale.RU, "водка"), null);
    }

    @Test
    public void findFormDataGroup() throws Exception {
        Assert.assertNotEquals(findFormData(QueryType.DRINK_GROUP, ConstLocale.EN, "Martini Madness"), null);
    }

    @Test
    public void findFormDataGroupRu() throws Exception {
        Assert.assertNotEquals(findFormData(QueryType.DRINK_GROUP, ConstLocale.RU, "Безумие Мартини"), null);
    }

    @Test
    public void findFormDataBase() throws Exception {
        Assert.assertNotEquals(findFormData(QueryType.BASE_DRINK, ConstLocale.EN, "Liqueur"), null);
    }

    @Test
    public void findFormDataBaseRu() throws Exception {
        Assert.assertNotEquals(findFormData(QueryType.BASE_DRINK, ConstLocale.RU, "Ликёр"), null);
    }

    private String findFormData(QueryType queryType, String lang, String param) throws Exception {
        return service.findFormData(queryType, lang)
                .stream()
                .filter(elem -> param.equals(elem))
                .findAny()
                .get();
    }
}
package by.khlebnikov.bartender.constant;

public class ConstQueryCocktail {
    public static final String INGREDIENT = "cocktailreposiory.ingredients";
    public static final String INGREDIENT_CREATED = "cocktailreposiory.ingredientscreated";
    public static final String INGREDIENT_RUS = "cocktailreposiory.ingredientsrus";

    //query for filtering by multiple parameters
    public static final String ALL_INGRED_QUERY = "cocktailreposiory.query1";
    public static final String ALL_INGRED_QUERY_LANG = "cocktailreposiory.query1lang";
    public static final String GROUP_PART = "cocktailreposiory.query2";
    public static final String GROUP_PART_LANG = "cocktailreposiory.query2lang";
    public static final String GROUP_AND_BASE = "cocktailreposiory.query2base";
    public static final String BASE_PART = "cocktailreposiory.query3";
    public static final String BASE_PART_LANG = "cocktailreposiory.query3lang";
    //subquery is built up from 3 parts
    public static final String SUBQUERY_PART_1 = "cocktailreposiory.subquery1";
    public static final String SUBQUERY_PART_1_LANG = "cocktailreposiory.subquery1lang";
    public static final String SUBQUERY_PART_2 = "cocktailreposiory.subquery2";
    public static final String SUBQUERY_PART_2_LANG = "cocktailreposiory.subquery2lang";
    public static final String SUBQUERY_PART_3 = "cocktailreposiory.subquery3";
    public static final String SUBQUERY_PART_3_LANG = "cocktailreposiory.subquery3lang";

    //find cocktail by id
    public static final String FIND_BY_ID = "cocktailreposiory.findcocktail";
    public static final String FIND_CREATED_BY_ID = "cocktailreposiory.findcocktailcreated";
    public static final String FIND_BY_ID_LANG = "cocktailreposiory.findcocktaillang";

    public static final String DRINK_TYPE = "type";
    public static final String BASE_DRINK = "base";

    //Save created cocktail
    public static final String SAVE_COCKTAIL = "cocktaildao.savecreatedcocktail";
    public static final String SAVE_COCKTAIL_RUS = "cocktaildao.savecreatedcocktailrus";
    public static final String SAVE_COMBINATION = "cocktaildao.savecombination";
    public static final String SAVE_COMBINATION_RUS = "cocktaildao.savecombinationrus";
    public static final String LAST_INSERTED_ID = "cocktaildao.lastinsertedid";

}

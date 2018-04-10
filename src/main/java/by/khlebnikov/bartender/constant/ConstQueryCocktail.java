package by.khlebnikov.bartender.constant;

public class ConstQueryCocktail {
    public static final String INGREDIENT = "cocktailreposiory.ingredients";
    public static final String INGREDIENT_RUS = "cocktailreposiory.ingredientsrus";

    //query for filtering by multiple parameters
    public static final String ALL_INGRED_QUERY = "cocktailreposiory.query1";
    public static final String AND_GROUP = "cocktailreposiory.query2";
    public static final String AND_BASE = "cocktailreposiory.query3";
    //subquery is built up from 3 parts
    public static final String SUBQUERY_PART_1 = "cocktailreposiory.subquery1";
    public static final String SUBQUERY_PART_2 = "cocktailreposiory.subquery2";
    public static final String SUBQUERY_PART_3 = "cocktailreposiory.subquery3";

    public static final String DRINK_TYPE = "type";
    public static final String BASE_DRINK = "base";

    public static final String GROUP_NAME = " AND `drink_group`.`group_name` = \"";
    public static final String GROUP_NAME_RUS = " AND `drink_group`.`group_name_rus` = \"";
    public static final String BASE_NAME = " AND `base_drink`.`base_name` = \"";
    public static final String BASE_NAME_RUS = " AND `base_drink`.`base_name_rus` = \"";

}

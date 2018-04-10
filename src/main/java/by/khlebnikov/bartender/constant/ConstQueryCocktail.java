package by.khlebnikov.bartender.constant;

public class ConstQueryCocktail {
    public static final String INGREDIENT = "cocktailreposiory.ingredients";
    public static final String INGREDIENT_RUS = "cocktailreposiory.ingredientsrus";

    public static final String AND_IN_SUBQUERY = "cocktailreposiory.andinsubquery";
    public static final String SUBQUERY = "cocktailreposiory.subquery";
    public static final String QUERY = "cocktailreposiory.parametricall";
    public static final String AND_GROUP = "cocktailreposiory.andgroup";
    public static final String AND_BASE = "cocktailreposiory.andbase";

    public static final String DRINK_TYPE = "type";
    public static final String BASE_DRINK = "base";
    public static final String AND = " AND ";
    public static final String OR = " OR ";

    public static final String INGREDIENT_NAME = " `ingredient`.`in_name` = \"";
    public static final String INGREDIENT_NAME_RUS = " `ingredient`.`in_name_rus` = \"";
    public static final String GROUP_NAME = " AND `drink_group`.`group_name` = \"";
    public static final String GROUP_NAME_RUS = " AND `drink_group`.`group_name_rus` = \"";
    public static final String BASE_NAME = " AND `base_drink`.`base_name` = \"";
    public static final String BASE_NAME_RUS = " AND `base_drink`.`base_name_rus` = \"";

}

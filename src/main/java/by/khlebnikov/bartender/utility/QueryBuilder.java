package by.khlebnikov.bartender.utility;

import by.khlebnikov.bartender.constant.ConstLocale;
import by.khlebnikov.bartender.constant.ConstQueryCocktail;
import by.khlebnikov.bartender.constant.Constant;
import by.khlebnikov.bartender.reader.PropertyReader;

/**
 * Class to build a query according to requests of users made from catalog page
 * during a parametrized search
 */
public class QueryBuilder {

    // Constants ----------------------------------------------------------------------------------
    private static final String GROUP_AND_BASE = PropertyReader.getQueryProperty(ConstQueryCocktail.GROUP_AND_BASE);
    private static final String ALL_INGRED_QUERY = PropertyReader.getQueryProperty(ConstQueryCocktail.ALL_INGRED_QUERY);
    private static final String ALL_INGRED_QUERY_LANG = PropertyReader.getQueryProperty(ConstQueryCocktail.ALL_INGRED_QUERY_LANG);
    private static final String GROUP_PART = PropertyReader.getQueryProperty(ConstQueryCocktail.GROUP_PART);
    private static final String GROUP_PART_LANG = PropertyReader.getQueryProperty(ConstQueryCocktail.GROUP_PART_LANG);
    private static final String BASE_PART = PropertyReader.getQueryProperty(ConstQueryCocktail.BASE_PART);
    private static final String BASE_PART_LANG = PropertyReader.getQueryProperty(ConstQueryCocktail.BASE_PART_LANG);
    private static final String SUBQUERY_PART_1 = PropertyReader.getQueryProperty(ConstQueryCocktail.SUBQUERY_PART_1);
    private static final String SUBQUERY_PART_1_LANG = PropertyReader.getQueryProperty(ConstQueryCocktail.SUBQUERY_PART_1_LANG);
    private static final String SUBQUERY_PART_2 = PropertyReader.getQueryProperty(ConstQueryCocktail.SUBQUERY_PART_2);
    private static final String SUBQUERY_PART_2_LANG = PropertyReader.getQueryProperty(ConstQueryCocktail.SUBQUERY_PART_2_LANG);
    private static final String SUBQUERY_PART_3 = PropertyReader.getQueryProperty(ConstQueryCocktail.SUBQUERY_PART_3);
    private static final String SUBQUERY_PART_3_LANG = PropertyReader.getQueryProperty(ConstQueryCocktail.SUBQUERY_PART_3_LANG);

    // Actions ------------------------------------------------------------------------------------

    /**
     * Generates  a query according to selected by a user parameters of cocktails
     * on the catalog page
     *
     * @param drinkTypeSelected true if the user has selected a drink type, false otherwise
     * @param baseDrinkSelected true if the user has selected a base drink, false otherwise
     * @param optionNum         number of selected ingredients
     * @param language          current locale of the user
     * @return query according to the request of the user
     */
    public static String build(boolean drinkTypeSelected, boolean baseDrinkSelected,
                               int optionNum, String language) {
        boolean isEnglish = ConstLocale.EN.equals(language);

        StringBuilder query = isEnglish ? new StringBuilder(ALL_INGRED_QUERY) : new StringBuilder(ALL_INGRED_QUERY_LANG);
        String groupPart = isEnglish ? GROUP_PART : GROUP_PART_LANG;
        String basePart = isEnglish ? BASE_PART : BASE_PART_LANG;
        String subQueryPart1 = isEnglish ? SUBQUERY_PART_1 : SUBQUERY_PART_1_LANG;
        String subQueryPart2 = isEnglish ? SUBQUERY_PART_2 : SUBQUERY_PART_2_LANG;
        String subQueryPart3 = isEnglish ? SUBQUERY_PART_3 : SUBQUERY_PART_3_LANG;

        if (drinkTypeSelected) {
            query.append(groupPart);
        }

        if (baseDrinkSelected) {
            query.append(basePart);
        }

        query.append(GROUP_AND_BASE);

        if (optionNum > 0) {
            query.append(subQueryPart1)
                    .append(optionNum)
                    .append(subQueryPart2)
                    .append(buildQuestionPart(optionNum))
                    .append(subQueryPart3);
        }

        return query.append(Constant.SEMICOLON)
                .toString();
    }

    // Helper methods -----------------------------------------------------------------------------

    /**
     * Generates a sequence of question marks for prepared statements
     *
     * @param numOfSubstitutes number of question marks in the sequence
     * @return generated string
     */
    private static String buildQuestionPart(int numOfSubstitutes) {
        StringBuilder questionPart = new StringBuilder();

        for (int i = 0; i < numOfSubstitutes; i++) {
            questionPart.append(Constant.QUESTION);

            if (i != numOfSubstitutes - 1) {
                questionPart.append(Constant.COMMA);
            }
        }

        return questionPart.toString();
    }
}

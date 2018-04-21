package by.khlebnikov.bartender.dao;

/**
 * Enum for defining query types
 */
public enum QueryType {

    /* indicates query from 'ingredient' table */
    INGREDIENT,

    /* indicates query from 'drink group' table */
    DRINK_GROUP,

    /* indicates query from 'base drink' table */
    BASE_DRINK,

    /* indicates query for finding all created cocktails */
    ALL_CREATED,

    /* indicates query for finding all favourite cocktails */
    ALL_FAVOURITE,

    /* indicates query for finding a classic cocktail */
    FIND,

    /* indicates query for finding a cocktail created by user */
    FIND_CREATED,

    /* indicates query for saving a cocktail created by user */
    SAVE,

    /* indicates query for saving combination of a cocktail created by user */
    SAVE_COMB,

    /* indicates query for adding a cocktail to user's favourite list */
    SAVE_FAVOURITE,

    /* indicates query for deleting a cocktail from user's favourite list */
    DELETE_FAVOURITE,

    /* indicates query for deleting a cocktail created by user */
    DELETE_CREATED,

    /* indicates query for updating a cocktail created by user */
    UPDATE
}

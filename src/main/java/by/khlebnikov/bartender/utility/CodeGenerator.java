package by.khlebnikov.bartender.utility;

import by.khlebnikov.bartender.constant.Constant;

import java.util.UUID;

/**
 * Utility class for code generation
 */
public class CodeGenerator {

    // Actions ------------------------------------------------------------------------------------

    /**
     * Generates a numeric code used for mail verification
     *
     * @return generated code
     */
    public static long generateCode() {
        return (long) ((Math.random() + 1) * Constant.MULTIPLIER);
    }

    /**
     * Generates a random unique ID
     *
     * @return generated unique ID
     */
    public static String uniqueId() {
        return UUID.randomUUID().toString();
    }
}

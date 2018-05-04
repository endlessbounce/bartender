package by.khlebnikov.bartender.utility;

import by.khlebnikov.bartender.constant.Constant;

import java.util.UUID;

/**
 * Utility class for code generation
 */
public class CodeGenerator {

    // Actions ------------------------------------------------------------------------------------
    /**
     * Generates a random unique ID
     *
     * @return generated unique ID
     */
    public static String uniqueId() {
        return UUID.randomUUID().toString();
    }
}

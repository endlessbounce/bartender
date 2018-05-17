package by.khlebnikov.bartender.utility;

import by.khlebnikov.bartender.constant.Constant;

import java.util.Calendar;

/**
 * Utility class to work with time
 */
public class TimeGenerator {

    // Actions ------------------------------------------------------------------------------------

    /**
     * Generates a number representing time that a user will have to confirm his registration
     * @return expiration time as a number
     */
    public static long expirationTime() {
        return currentTime() + Constant.TIME_TO_CONFIRM;
    }

    /**
     * Generates a number representing current time
     * @return current time as a number
     */
    public static long currentTime() {
        return Calendar.getInstance().getTimeInMillis() / Constant.TO_SECONDS;
    }
}

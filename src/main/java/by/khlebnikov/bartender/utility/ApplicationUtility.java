package by.khlebnikov.bartender.utility;

import by.khlebnikov.bartender.constant.Constant;

import java.util.Calendar;

public class ApplicationUtility {
    public static long expirationTime() {
        return Calendar.getInstance().getTimeInMillis() / 1000 + Constant.TIME_TO_CONFIRM;
    }

    public static long currentTime() {
        return Calendar.getInstance().getTimeInMillis() / 1000;
    }

    public static long generateCode() {
        return (long) ((Math.random() + 1) * 1000000);
    }
}

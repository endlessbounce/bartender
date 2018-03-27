package by.khlebnikov.bartender.manager;

import java.util.ResourceBundle;

public class PagePathManager {
    private final static ResourceBundle resourceBundle = ResourceBundle.getBundle("config");

    private PagePathManager() { }

    public static String getProperty(String key) {
        return resourceBundle.getString(key);
    }
}

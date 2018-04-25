package by.khlebnikov.bartender.reader;

import by.khlebnikov.bartender.constant.ConstBundle;
import by.khlebnikov.bartender.constant.ConstLocale;

import java.util.Locale;
import java.util.ResourceBundle;
import java.util.concurrent.locks.ReentrantLock;

/**
 * This class is a reader of application's property files
 */
public class PropertyReader {

    // Constants ----------------------------------------------------------------------------------
    private final static Locale RU_LOCALE = new Locale(ConstLocale.RU_LOW, ConstLocale.RU);
    private final static Locale EN_LOCALE = new Locale(ConstLocale.EN_LOW, ConstLocale.US);
    private final static ResourceBundle CONFIG_BUNDLE = ResourceBundle.getBundle(ConstBundle.CONFIG);
    private final static ResourceBundle QUERY_BUNDLE = ResourceBundle.getBundle(ConstBundle.QUERIES);
    private final static ResourceBundle RU_MESSAGE_BUNDLE = ResourceBundle.getBundle(ConstBundle.MESSAGES, RU_LOCALE);
    private final static ResourceBundle EN_MESSAGE_BUNDLE = ResourceBundle.getBundle(ConstBundle.MESSAGES, EN_LOCALE);

    // Vars ---------------------------------------------------------------------------------------
    private static ReentrantLock configlock = new ReentrantLock();
    private static ReentrantLock messagelock = new ReentrantLock();
    private static ReentrantLock querylock = new ReentrantLock();

    // Constructors -------------------------------------------------------------------------------
    private PropertyReader() {
    }

    // Actions ------------------------------------------------------------------------------------

    /**
     * Returns the value of a property read from the config resource bundle
     *
     * @param key of the property
     * @return value of the property
     */
    public static String getConfigProperty(String key) {
        configlock.lock();
        try {
            return CONFIG_BUNDLE.getString(key);
        } finally {
            configlock.unlock();
        }
    }

    /**
     * Returns the value of a property read from the message resource bundle
     *
     * @param key    of the property
     * @param locale current locale of the user
     * @return value of the property
     */
    public static String getMessageProperty(String key, String locale) {
        messagelock.lock();
        try {
            switch (locale) {
                case ConstLocale.RU_RU:
                    return RU_MESSAGE_BUNDLE.getString(key);
                case ConstLocale.EN_US:
                    return EN_MESSAGE_BUNDLE.getString(key);
                default:
                    return EN_MESSAGE_BUNDLE.getString(key);
            }
        } finally {
            messagelock.unlock();
        }
    }

    /**
     * Returns the value of a property read from the queries resource bundle
     *
     * @param key of the property
     * @return value of the property
     */
    public static String getQueryProperty(String key) {
        querylock.lock();
        try {
            return QUERY_BUNDLE.getString(key);
        } finally {
            querylock.unlock();
        }
    }
}

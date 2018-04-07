package by.khlebnikov.bartender.reader;

import by.khlebnikov.bartender.constant.ConstBundle;
import by.khlebnikov.bartender.constant.ConstLocale;

import java.util.Locale;
import java.util.ResourceBundle;
import java.util.concurrent.locks.ReentrantLock;

public class PropertyReader {
    private final static Locale ruLocale = new Locale(ConstLocale.RU_LOW, ConstLocale.RU);
    private final static Locale enLocale = new Locale(ConstLocale.EN_LOW, ConstLocale.US);
    private final static ResourceBundle configBundle = ResourceBundle.getBundle(ConstBundle.CONFIG);
    private final static ResourceBundle queryBundle = ResourceBundle.getBundle(ConstBundle.QUERIES);
    private final static ResourceBundle ruMessageBundle = ResourceBundle.getBundle(ConstBundle.MESSAGES, ruLocale);
    private final static ResourceBundle enMessageBundle = ResourceBundle.getBundle(ConstBundle.MESSAGES, enLocale);
    private static ReentrantLock configlock = new ReentrantLock();
    private static ReentrantLock messagelock = new ReentrantLock();
    private static ReentrantLock querylock = new ReentrantLock();


    private PropertyReader() {
    }

    public static String getConfigProperty(String key) {
        configlock.lock();
        try {
            return configBundle.getString(key);
        } finally {
            configlock.unlock();
        }
    }

    public static String getMessageProperty(String key, String locale) {
        String message;
        messagelock.lock();
        try {
            switch (locale){
                case ConstLocale.RU_RU:
                    message = ruMessageBundle.getString(key);
                    break;
                case ConstLocale.EN_US:
                    message = enMessageBundle.getString(key);
                    break;
                default:
                    message = enMessageBundle.getString(key);
                    break;
            }
            return message;
        } finally {
            messagelock.unlock();
        }
    }

    public static String getQueryProperty(String key) {
        querylock.lock();
        try {
            return queryBundle.getString(key);
        } finally {
            querylock.unlock();
        }
    }
}

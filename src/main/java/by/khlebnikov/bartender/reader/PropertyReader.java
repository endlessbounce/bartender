package by.khlebnikov.bartender.reader;

import by.khlebnikov.bartender.constant.Constant;

import java.util.Locale;
import java.util.ResourceBundle;
import java.util.concurrent.locks.ReentrantLock;

public class PropertyReader {
    private final static Locale ruLocale = new Locale(Constant.RU_LOW, Constant.RU);
    private final static Locale enLocale = new Locale(Constant.EN_LOW, Constant.US);
    private final static ResourceBundle configBundle = ResourceBundle.getBundle(Constant.CONFIG);
    private final static ResourceBundle emailBundle = ResourceBundle.getBundle(Constant.EMAIL);
    private final static ResourceBundle queryBundle = ResourceBundle.getBundle(Constant.QUERIES);
    private final static ResourceBundle ruMessageBundle = ResourceBundle.getBundle(Constant.MESSAGES, ruLocale);
    private final static ResourceBundle enMessageBundle = ResourceBundle.getBundle(Constant.MESSAGES, enLocale);
    private static ReentrantLock configlock = new ReentrantLock();
    private static ReentrantLock messagelock = new ReentrantLock();
    private static ReentrantLock emaillock = new ReentrantLock();
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
                case Constant.RU_RU:
                    message = ruMessageBundle.getString(key);
                    break;
                case Constant.EN_US:
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

    public static String getEmailProperty(String key) {
        emaillock.lock();
        try {
            return emailBundle.getString(key);
        } finally {
            emaillock.unlock();
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

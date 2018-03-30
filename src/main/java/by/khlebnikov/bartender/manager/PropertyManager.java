package by.khlebnikov.bartender.manager;

import java.util.ResourceBundle;
import java.util.concurrent.locks.ReentrantLock;

public class PropertyManager {
    private final static ResourceBundle configBundle = ResourceBundle.getBundle("config");
    private final static ResourceBundle messageBundle = ResourceBundle.getBundle("messages");
    private static ReentrantLock configlock = new ReentrantLock();
    private static ReentrantLock messagelock = new ReentrantLock();


    private PropertyManager() { }

    public static String getConfigProperty(String key) {
        configlock.lock();
        try{
            return configBundle.getString(key);
        }finally {
            configlock.unlock();
        }
    }

    public static String getMessageProperty(String key) {
        messagelock.lock();
        try{
            return messageBundle.getString(key);
        }finally {
            messagelock.unlock();
        }
    }
}

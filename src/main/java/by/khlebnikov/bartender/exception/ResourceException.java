package by.khlebnikov.bartender.exception;

public class ResourceException extends Exception {
    public ResourceException(String message, Throwable cause) { super(message, cause); }
    public ResourceException(Throwable cause) { super(cause); }
}

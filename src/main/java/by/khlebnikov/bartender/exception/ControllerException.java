package by.khlebnikov.bartender.exception;

public class ControllerException extends Exception{
    public ControllerException(String message, Throwable cause) { super(message, cause); }
    public ControllerException(Throwable cause) { super(cause); }
}

package by.khlebnikov.bartender.exception;

/**
 * This class represents a generic ServiceException. It should wrap any exception of the underlying
 * code, such as DataAccessExceptions.
 */
public class ServiceException extends Exception{

    // Constructors -------------------------------------------------------------------------------

    /**
     * Constructs a ServiceException with the given detail message and root cause.
     *
     * @param message The detail message of the ServiceException.
     * @param cause   The root cause of the ServiceException.
     */
    public ServiceException(String message, Throwable cause) { super(message, cause); }
}

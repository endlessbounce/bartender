package by.khlebnikov.bartender.exception;

/**
 * This class represents a generic ResourceException. It should wrap any exception of the underlying
 * code, such as ServiceExceptions.
 */
public class ResourceException extends Exception {

    // Constructors -------------------------------------------------------------------------------

    /**
     * Constructs a ResourceException with the given detail message and root cause.
     *
     * @param message The detail message of the ResourceException.
     * @param cause   The root cause of the ResourceException.
     */
    public ResourceException(String message, Throwable cause) { super(message, cause); }

    /**
     * Constructs a ResourceException with the given root cause.
     * @param cause cause The root cause of the ResourceException.
     */
    public ResourceException(Throwable cause) { super(cause); }

    /**
     * Constructs a ResourceException with the given detail message.
     *
     * @param message The detail message of the ResourceException.
     */
    public ResourceException(String message) { super(message); }
}

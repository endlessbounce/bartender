package by.khlebnikov.bartender.exception;

/**
 * This class represents a generic DataAccessException. It should wrap any exception of the underlying
 * code, such as SQLExceptions.
 */
public class DataAccessException extends Exception {

    // Constructors -------------------------------------------------------------------------------

    /**
     * Constructs a DataAccessException with the given detail message and root cause.
     *
     * @param message The detail message of the DataAccessException.
     * @param cause   The root cause of the DataAccessException.
     */
    public DataAccessException(String message, Throwable cause) { super(message, cause); }

    /**
     * Constructs a DataAccessException with the given detail message and root cause.
     *
     * @param cause   The root cause of the DataAccessException.
     */
    public DataAccessException(Throwable cause) { super(cause); }
}

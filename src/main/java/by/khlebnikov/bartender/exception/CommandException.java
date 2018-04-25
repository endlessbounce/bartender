package by.khlebnikov.bartender.exception;

/**
 * This class represents a generic CommandException. It should wrap any exception of the underlying
 * code, such as ServiceExceptions.
 */
public class CommandException extends Exception {

    // Constructors -------------------------------------------------------------------------------

    /**
     * Constructs a CommandException with the given detail message and root cause.
     *
     * @param message The detail message of the CommandException.
     * @param cause   The root cause of the CommandException.
     */
    public CommandException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a CommandException with the given root cause.
     * @param cause cause The root cause of the DAOException.
     */
    public CommandException(Throwable cause) {
        super(cause);
    }
}

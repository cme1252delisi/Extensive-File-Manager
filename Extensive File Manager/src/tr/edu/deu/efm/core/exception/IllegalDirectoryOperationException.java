package tr.edu.deu.efm.core.exception;

/**
 * Thrown to indicate that a requested file system operation cannot be performed 
 * due to a logical violation of directory rules.
 * <p>
 * This is a domain-specific, unchecked exception extending {@link RuntimeException}. 
 * It is typically fired by the core I/O layer when a user attempts an invalid action, 
 * such as trying to enter a text file using the {@code cd} command, or attempting 
 * to copy a directory into itself.
 * </p>
 * <p>
 * <b>Architectural Note:</b> By making this an unchecked exception, the core logic 
 * avoids cluttering method signatures with {@code throws} clauses. The Command layer 
 * (e.g., the Invoker) is responsible for catching this exception and gracefully 
 * converting it into a failed {@link tr.edu.deu.efm.command.api.CommandResult}.
 * </p>
 */
public class IllegalDirectoryOperationException extends RuntimeException {
    
    /**
     * Constructs a new exception with the specified detail message.
     *
     * @param message The detail message explaining the specific rule violation. 
     * This message is designed to be human-readable, as it will 
     * ultimately be carried to the UI layer and displayed to the user.
     */
    public IllegalDirectoryOperationException(String message) {
        super(message);
    }
}
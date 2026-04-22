package tr.edu.deu.efm.core.exception;

/**
 * Thrown to indicate that a targeted file or directory could not be found 
 * within the physical file system.
 * <p>
 * This is a domain-specific, unchecked exception extending {@link RuntimeException}. 
 * It is primarily triggered by the Core I/O layer (e.g., {@code DirectoryChanger}, 
 * {@code EntityRemover}) when an operation strictly requires an existing entity, 
 * but the provided path resolves to nothing (i.e., {@code Files.exists()} returns false).
 * </p>
 * <p>
 * <b>Architectural Note:</b> Acting as a crucial part of the error-handling pipeline, 
 * this exception prevents the low-level {@link java.nio.file.NoSuchFileException} 
 * from leaking into the Command and UI layers. The Invoker (Command layer) is expected 
 * to catch this exception and elegantly map it to a failed 
 * {@link tr.edu.deu.efm.command.api.CommandResult}.
 * </p>
 */
public class EntityNotFoundException extends RuntimeException {
    
    /**
     * Constructs a new exception with the specified detail message.
     *
     * @param message The detail message specifying the exact path or entity that was not found. 
     * It is recommended to format this message similar to standard POSIX terminal outputs 
     * (e.g., {@code "cd: /invalid/path: No such file or directory"}).
     */
    public EntityNotFoundException(String message) {
        super(message);
    }
}
package tr.edu.deu.efm.command.api;

/**
 * Defines the contract for providing metadata and documentation for a command.
 * <p>
 * By implementing this interface, a command becomes self-documenting. This allows
 * the system (such as a Help or Menu command) to dynamically discover and display 
 * command information without requiring hardcoded text elsewhere in the application.
 * </p>
 */
public interface Describable {

    /**
     * Retrieves the precise trigger word or name of the command.
     *
     * @return The command name (e.g., "ls", "rm", "cp") used by the parser to route the request.
     */
    String getName();  

    /**
     * Retrieves a brief, human-readable explanation of what the command does.
     *
     * @return A short summary of the command's purpose and primary behavior.
     */
    String getDescription();

    /**
     * Retrieves the correct syntax and available flags for executing the command.
     * <p>
     * Example: {@code rm [-r] [-v] <target_path>}
     * </p>
     *
     * @return The formatted usage string demonstrating how to properly call the command.
     */
    String getUsage();
}
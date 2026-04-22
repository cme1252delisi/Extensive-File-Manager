package tr.edu.deu.efm.command.api;

import java.util.Map;

/**
 * Defines the contract for the central command repository and routing mechanism.
 * <p>
 * The registry acts as a decoupled dictionary that maps user-facing trigger words 
 * (e.g., "ls", "rm") to their corresponding {@link Command} implementations. By abstracting 
 * this into an interface, the core application loop (the runner) can parse and execute 
 * commands without being tightly coupled to the concrete command classes.
 * </p>
 */
public interface CommandRegistry {

    /**
     * Registers a new command into the system under a specific trigger name.
     * <p>
     * This method is typically invoked exclusively during the application's initialization 
     * phase (the Composition Root / Config) to wire up the application's dependencies.
     * </p>
     *
     * @param name    The exact string trigger used to invoke the command from the CLI (e.g., "cd").
     * @param command The executable command instance to be bound to the specified name.
     */
    void register(String name, Command command);

    /**
     * Retrieves a registered command based on its assigned trigger name.
     *
     * @param name The trigger name parsed from the user's raw input.
     * @return The corresponding {@link Command} instance, or {@code null} if the command is unrecognized.
     */
    Command getCommand(String name);

    /**
     * Retrieves the complete, read-only mapping of all registered commands in the system.
     * <p>
     * This method is essential for system-wide operations that need to introspect 
     * available commands, such as dynamic help generation (e.g., {@code HelpCommand}) 
     * or future terminal auto-completion features.
     * </p>
     *
     * @return A map containing all trigger names and their associated command instances.
     */
    Map<String, Command> getRegistryMap();
}
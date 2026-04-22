package tr.edu.deu.efm.command.api;

/**
 * Represents the core execution contract for all commands within the EFM system.
 * <p>
 * Any command that performs an operation must implement this interface to ensure 
 * a standardized execution flow. It enforces the separation of concerns by 
 * guaranteeing that execution logic is isolated from parsing and presentation.
 * </p>
 */
public interface Executable {
	/**
     * Executes the specific business logic of the command based on the provided context.
     *
     * @param context The data transfer object containing the current session, 
     * target arguments, and specific flags requested by the user.
     * @return A {@link CommandResult} containing the success status of the operation 
     * and the formatted output message to be displayed to the user.
     */
	CommandResult execute(CommandContext context);
}

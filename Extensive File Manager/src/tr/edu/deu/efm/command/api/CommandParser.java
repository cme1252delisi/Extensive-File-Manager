package tr.edu.deu.efm.command.api;

/**
 * Defines the contract for transforming raw user input into a structured execution context.
 * <p>
 * The parser is the entry point of the command execution pipeline. It is responsible 
 * for tokenizing the input string, identifying the command name, extracting optional 
 * flags, and capturing positional arguments.
 * </p>
 */
public interface CommandParser {

    /**
     * Parses a raw input string and populates a {@link CommandContext}.
     *
     * @param rawInput The complete line of text entered by the user in the CLI.
     * @param session  The current active {@link EfmSession} to be linked to the context.
     * @return A fully populated {@link CommandContext} ready for execution.
     * @throws IllegalArgumentException If the input is empty or syntactically malformed.
     */
    CommandContext parse(String rawInput);
}
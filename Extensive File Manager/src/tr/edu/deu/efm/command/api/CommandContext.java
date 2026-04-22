package tr.edu.deu.efm.command.api;

import java.util.ArrayList;
import java.util.List;

/**
 * Encapsulates all necessary data required to execute a command within the EFM system.
 * <p>
 * Acting as a Data Transfer Object (DTO) between the parsing mechanism and the 
 * execution engine, the {@code CommandContext} prevents bloated method signatures 
 * (Parameter Object Pattern). It carries the parsed user input (arguments and flags) 
 * alongside the stateful {@link EfmSession}, providing the stateless commands with 
 * everything they need to perform their operations in isolation.
 * </p>
 */
public class CommandContext {
    
    private String commandName;
    private CommandFlags flags;
    private List<String> arguments;
    private EfmSession efmSession;
    
    /**
     * Constructs a new, empty command context with safe default values.
     * <p>
     * Initializes lists and flag objects to prevent {@link NullPointerException}s 
     * during the parsing phase.
     * </p>
     */
    public CommandContext() {
        this.setCommandName("");
        this.setFlags(new CommandFlags());
        this.setArguments(new ArrayList<>());
    }

    /**
     * @return The specific trigger word (e.g., "ls", "cd") that initiated this command.
     */
    public String getCommandName() {
        return commandName;
    }

    public void setCommandName(String commandName) {
        this.commandName = commandName;
    }

    /**
     * @return The collection of optional flags (e.g., '-r', '-f') parsed from the input.
     */
    public CommandFlags getFlags() {
        return flags;
    }
    
    /**
     * Convenience method to register a single flag character directly to the context.
     * * @param c The flag character to add.
     */
    public void addFlag(char c) {
        flags.addFlag(c);
    }

    public void setFlags(CommandFlags flags) {
        this.flags = flags;
    }

    /**
     * @return The list of positional arguments (typically file paths or names) provided by the user.
     */
    public List<String> getArguments() {
        return arguments;
    }
    
    /**
     * Convenience method to append a new positional argument to the context.
     * * @param argument The argument string to add.
     */
    public void addArgument(String argument) {
        this.arguments.add(argument);
    }

    public void setArguments(List<String> arguments) {
        this.arguments = arguments;
    }

    /**
     * Retrieves the stateful user session linked to this specific command execution.
     * <p>
     * This is the vital bridge that allows stateless commands to know about and 
     * interact with the global state (such as the Current Working Directory).
     * </p>
     * * @return The active {@link EfmSession} instance.
     */
    public EfmSession getEfmSession() {
        return efmSession;
    }

    public void setEfmSession(EfmSession efmSession) {
        this.efmSession = efmSession;
    }
}
package tr.edu.deu.efm.command.api;

import java.util.HashSet;
import java.util.Set;

/**
 * Encapsulates the parsing, storage, and retrieval of command-line flags (options).
 * <p>
 * In command-line interfaces, flags (e.g., {@code -r}, {@code -f}) modify the default 
 * behavior of a command. This class provides a centralized mechanism to handle both 
 * isolated flags (e.g., {@code -r -f}) and POSIX-style compound flags (e.g., {@code -rf}).
 * </p>
 * <p>
 * <b>Architectural Note:</b> Internally backed by a {@link java.util.HashSet}, this class 
 * automatically guarantees flag uniqueness. Redundant user inputs like {@code -r -r} 
 * or {@code -rr} are gracefully collapsed into a single active flag state.
 * </p>
 */
public class CommandFlags {

    private Set<Character> flags;

    /**
     * Constructs an empty flag container.
     */
    public CommandFlags() {
        this.flags = new HashSet<>();
    }

    /**
     * Manually registers a single flag character into the active set.
     *
     * @param flag The character representing the specific option (e.g., 'r', 'v').
     */
    public void addFlag(char flag) {
        flags.add(flag);
    }

    /**
     * Parses a flag argument string and extracts individual flag characters.
     * <p>
     * If the provided string begins with a hyphen ({@code -}), the hyphen is ignored, 
     * and every subsequent character is registered as an independent active flag.
     * Example: The input {@code "-rf"} will register both {@code 'r'} and {@code 'f'}.
     * </p>
     *
     * @param flagArgument The raw string argument parsed from the user input.
     */
    public void parseAndAddFlags(String flagArgument) {
        if (flagArgument != null && flagArgument.startsWith("-")) {
            for (int i = 1; i < flagArgument.length(); i++) {
                addFlag(flagArgument.charAt(i));
            }
        }
    }

    /**
     * Checks if a specific execution flag was requested by the user.
     * <p>
     * This is the primary method utilized by command implementations (e.g., {@code RmCommand}) 
     * to determine if they should execute alternative logic (like recursive deletion).
     * </p>
     *
     * @param flag The character to query against the active set.
     * @return {@code true} if the flag is present; {@code false} otherwise.
     */
    public boolean hasFlag(char flag) {
        return this.flags.contains(flag);
    }

    /**
     * Returns a human-readable representation of the currently active flags.
     * Primarily used for debugging or logging purposes.
     *
     * @return A string formatted as "Active Flags: -[flags]"
     */
    @Override
    public String toString() {
        return "Active Flags: -" + getFlagsAsString();
    }

    /**
     * Concatenates all active flag characters into a single continuous string.
     *
     * @return A string containing all unique flag characters (e.g., "rf").
     */
    private String getFlagsAsString() {
        StringBuilder sb = new StringBuilder();
        for (Character c : flags) {
            sb.append(c);
        }
        return sb.toString();
    }
}
package tr.edu.deu.efm.command.api;

/**
 * Represents the stateful environment and memory of a single user session within EFM.
 * <p>
 * While individual commands in the system are strictly stateless, the {@code EfmSession} 
 * acts as the central repository for dynamic contextual data that changes as the user 
 * interacts with the application. It is carried across the system via the 
 * {@link CommandContext}, ensuring that the core logic remains decoupled from global states.
 * </p>
 * <p>
 * <b>Future Scope:</b> This class is designed to be the container for all session-scoped 
 * data, which may eventually include command execution history, clipboard states (copy/paste), 
 * and custom user preferences.
 * </p>
 */
public class EfmSession {
    
    private String currentWorkingDirectory;
//  private List<String> commandHistory;
//  private Clipboard clipboard;

    /**
     * Initializes a new user session.
     * <p>
     * Mimicking the standard behavior of Unix-like terminal emulators (e.g., bash, zsh), 
     * the session defaults the starting directory to the user's home folder.
     * </p>
     */
    public EfmSession() {
        this.currentWorkingDirectory = System.getProperty("user.home");
    }

    /**
     * Retrieves the absolute path string of the directory where the user is currently located.
     * This value is essential for resolving relative paths (like ".." or "./folder").
     *
     * @return The absolute path string of the current working directory.
     */
    public String getCurrentWorkingDirectory() {
        return currentWorkingDirectory;
    }

    /**
     * Updates the session's current working directory.
     * <p>
     * This method is typically invoked by navigation commands (such as {@code cd}) 
     * upon the successful resolution and verification of a new target path.
     * </p>
     *
     * @param currentWorkingDirectory The new absolute path string to set as the active directory.
     */
    public void setCurrentWorkingDirectory(String currentWorkingDirectory) {
        this.currentWorkingDirectory = currentWorkingDirectory;
    }
}
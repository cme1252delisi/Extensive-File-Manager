package tr.edu.deu.efm.config;

/**
 * Serves as the central configuration repository for the EFM (Extensive File Manager) application.
 * <p>
 * This utility class holds global system settings that dictate the behavior of 
 * core I/O operations, logging mechanisms, and command output verbosity. By centralizing 
 * these flags, the system ensures consistent behavior across all command executions.
 * </p>
 * <p>
 * <b>Architectural Note:</b> Currently implemented as global mutable state (public static fields) 
 * for simplicity in the current phase. In future enterprise iterations, this could be refactored 
 * into an immutable Configuration object populated via an external {@code .properties} file 
 * to ensure thread safety and strict encapsulation.
 * </p>
 */
public class Settings {
    
	/**
     * Don't let anyone instantiate this class.
     */
    private Settings() {
        
    } 
    
    /**
     * Dictates the system-wide deletion policy.
     * <ul>
     * <li>{@code true}: Deletion operations should route files to a safe "Trash" directory (Soft Delete).</li>
     * <li>{@code false}: Deletion operations permanently remove entities from the disk (Hard Delete).</li>
     * </ul>
     */
    public static boolean removeSafely = true;
    
    /**
     * Controls the default output detail level for all terminal commands.
     * <ul>
     * <li>{@code true}: Commands behave as if the {@code -v} (verbose) flag was explicitly passed, printing detailed execution steps.</li>
     * <li>{@code false}: Commands operate silently or with minimal standard output unless overridden by a specific flag.</li>
     * </ul>
     */
    public static boolean verboseAsDefault = false;
    
    /**
     * Defines the collision resolution policy for file operations (e.g., Copy or Move).
     * <ul>
     * <li>{@code true}: Existing target files will be silently overwritten (utilizing {@code StandardCopyOption.REPLACE_EXISTING}).</li>
     * <li>{@code false}: Operations will safely abort and return a failure message to prevent accidental data loss.</li>
     * </ul>
     */
    public static boolean overWriteMode = false;
    
    /**
     * Master switch for the application's audit logging mechanism.
     * <ul>
     * <li>{@code true}: User commands, system events, and errors are recorded to the log file.</li>
     * <li>{@code false}: Background logging is completely disabled, which may improve performance but removes the audit trail.</li>
     * </ul>
     */
    public static boolean logEnabled = true;
}
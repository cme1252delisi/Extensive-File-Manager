package tr.edu.deu.efm.core.api;

/**
 * Defines the core contract for renaming files and directories within the file system.
 * <p>
 * This interface encapsulates the physical renaming logic, ensuring that
 * higher-level command layers remain agnostic to underlying I/O stream operations.
 * It mandates the use of a current directory anchor to perfectly resolve 
 * relative user paths.
 * </p>
 */
public interface EntityRenamer {

    /**
     * Renames the entity at the given path to the specified new name.
     *
     * @param currentDir The absolute path of the current working directory. This acts as 
     * the anchor for resolving relative entity paths correctly.
     * @param entityPath The relative or absolute path of the entity to be renamed.
     * @param newName    The new name for the entity (not a full path, just the target name).
     * @return An {@link OperationResult} indicating the outcome.
     */
    OperationResult rename(String currentDir, String entityPath, String newName);
}
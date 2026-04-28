package tr.edu.deu.efm.core.api;

/**
 * Defines the core contract for safely removing files and directories from the file system.
 * <p>
 * This interface utilizes a Strategy Pattern to handle different removal policies 
 * (e.g., Permanent Deletion vs. Moving to Trash/Recycle Bin). It requires the session's 
 * current directory to accurately resolve relative paths.
 * </p>
 */
public interface EntityRemover {

    /**
     * Removes the entity at the specified target path.
     *
     * @param currentDir The absolute path of the current working directory, acting as the anchor.
     * @param targetPath The relative or absolute path of the entity to be removed.
     * @param recursive  If true, allows the deletion of directories and all their nested contents.
     * @return An {@link OperationResult} indicating success or failure. On success, 
     * {@code affectedItems} contains the absolute paths of all deleted entities.
     */
    OperationResult remove(String currentDir, String targetPath, boolean recursive);
}
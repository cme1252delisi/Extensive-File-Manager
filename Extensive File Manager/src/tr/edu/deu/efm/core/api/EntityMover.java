package tr.edu.deu.efm.core.api;

/**
 * Defines the core contract for moving files and directories within the file system.
 * <p>
 * This interface encapsulates the physical movement logic. Similar to the Copier, 
 * it utilizes a Strategy Pattern to handle collision policies (Overwrite vs. Copy) 
 * independently of the Command layer.
 * </p>
 */
public interface EntityMover {
    
    /**
     * Moves the entity located at the source path to the specified destination.
     *
     * @param currentDir  The absolute path of the current working directory, used as 
     * the anchor for resolving relative paths correctly.
     * @param sourcePath  The relative or absolute path of the entity to be moved.
     * @param targetPath  The relative or absolute path of the destination.
     * @return An {@link OperationResult} indicating the outcome.
     */
    OperationResult move(String currentDir, String sourcePath, String targetPath);
}
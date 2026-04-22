package tr.edu.deu.efm.core.api;

/**
 * Defines the core contract for duplicating files and directories within the file system.
 * <p>
 * This interface encapsulates the physical duplication logic, ensuring that higher layers 
 * do not need to manage I/O streams or NIO configurations. Implementations are expected 
 * to handle naming collisions based on the system's global configuration.
 * </p>
 */
public interface EntityCopier {

    /**
     * Duplicates the entity located at the source path to the specified destination path.
     * <p>
     * <b>Collision Policy:</b>
     * The implementation must strictly adhere to the global {@code overWriteMode} setting:
     * <ul>
     * <li>If {@code overWriteMode} is <b>true</b>: The implementation should utilize 
     * {@code StandardCopyOption.REPLACE_EXISTING} to overwrite any existing file at the target path.</li>
     * <li>If {@code overWriteMode} is <b>false</b>: The operation must fail gracefully 
     * if the target path already exists, returning an {@link OperationResult} with a clear 
     * error message instead of throwing an unhandled Exception.</li>
     * </ul>
     * </p>
     * <p>
     * <b>Recursive Policy:</b>
     * When copying a directory, the implementation should perform a recursive deep copy 
     * to maintain the integrity of the directory tree.
     * </p>
     *
     * @param sourcePathStr The absolute path of the source entity to be copied.
     * @param targetPathStr The absolute path of the destination where the copy will be created.
     * @return An {@link OperationResult} indicating success or failure. On success, it contains 
     * the path of the created duplicate. On failure, it provides the specific reason 
     * (e.g., "Access Denied", "Target already exists").
     */
    OperationResult copy(String sourcePath, String targetPath);
}
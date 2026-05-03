package tr.edu.deu.efm.core.api;

/**
 * Defines the core contract for creating new entities (files or directories) in
 * the file system.
 * <p>
 * This interface utilizes a Strategy Pattern where specific implementations
 * decide whether the created entity is a directory (mkdir) or a file (touch).
 * It relies entirely on the Result Pattern, avoiding exception propagation.
 * </p>
 */
public interface EntityCreator {

	/**
	 * Creates a new entity at the specified path.
	 *
	 * @param currentDir The absolute path of the current working directory
	 *                   (anchor).
	 * @param targetPath The relative or absolute path of the new entity to be
	 *                   created.
	 * @return An {@link OperationResult} indicating success or providing a specific
	 *         error message.
	 */
	OperationResult create(String currentDir, String targetPath);
}
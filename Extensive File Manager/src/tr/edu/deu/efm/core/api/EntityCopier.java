package tr.edu.deu.efm.core.api;

/**
 * Defines the core contract for duplicating files and directories within the
 * file system.
 */
public interface EntityCopier {

	/**
	 * Duplicates the entity located at the source path to the specified
	 * destination.
	 *
	 * @param currentDirectory The absolute path of the current working directory.
	 * @param sourcePath       The relative or absolute path of the entity to be
	 *                         copied.
	 * @param targetPath       The relative or absolute path of the destination.
	 * @param strategy         The rulebook to consult when a file collision occurs.
	 * @return An {@link OperationResult} indicating the outcome.
	 */
	OperationResult copy(String currentDirectory, String sourcePath, String targetPath, ConfirmationStrategy strategy);
}
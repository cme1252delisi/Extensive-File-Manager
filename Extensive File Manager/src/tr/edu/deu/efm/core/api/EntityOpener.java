package tr.edu.deu.efm.core.api;

/**
 * Defines the core contract for opening files using the native operating
 * system's default applications.
 */
public interface EntityOpener {

	/**
	 * Opens the specified file using the OS default application.
	 *
	 * @param currentDir The absolute path of the current working directory.
	 * @param targetPath The relative or absolute path of the file to open.
	 * @return An {@link OperationResult} indicating success or failure.
	 */
	OperationResult open(String currentDir, String targetPath);
}
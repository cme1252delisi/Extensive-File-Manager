package tr.edu.deu.efm.core.api;

/**
 * Defines the core contract for converting files between different formats.
 */
public interface EntityConverter {

	/**
	 * Converts a source file to a target format.
	 *
	 * @param currentDir The absolute path of the current working directory.
	 * @param sourcePath The relative or absolute path of the source file.
	 * @param targetPath The relative or absolute path of the target file.
	 * @param overwrite  If true, an existing target file will be replaced.
	 * @return An {@link OperationResult} indicating success or failure.
	 */
	OperationResult convert(String currentDir, String sourcePath, String targetPath, boolean overwrite);
}
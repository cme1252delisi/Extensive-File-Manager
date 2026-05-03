package tr.edu.deu.efm.core.api;

/**
 * Defines the core contract for extracting/unzipping archives.
 */
public interface EntityExtractor {

	/**
	 * Extracts a zip archive to a specified destination.
	 *
	 * @param currentDir      The absolute path of the current working directory.
	 * @param archivePath     The relative or absolute path of the archive to
	 *                        extract.
	 * @param destinationPath The folder where the contents will be extracted.
	 * @param overwrite       If true, existing files will be replaced without
	 *                        error.
	 * @return An {@link OperationResult} indicating success or failure.
	 */
	OperationResult extract(String currentDir, String archivePath, String destinationPath, boolean overwrite);
}
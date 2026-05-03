package tr.edu.deu.efm.core.api;

/**
 * Defines the core contract for compressing/archiving files and directories.
 */
public interface EntityCompressor {

	/**
	 * Compresses a source file or directory into a zip archive.
	 *
	 * @param currentDir  The absolute path of the current working directory.
	 * @param sourcePath  The relative or absolute path of the file/folder to be
	 *                    compressed.
	 * @param archiveName The name or path of the target archive (e.g.,
	 *                    "backup.zip").
	 * @param overwrite   If true, existing archives will be replaced without error.
	 * @return An {@link OperationResult} indicating success or failure.
	 */
	OperationResult compress(String currentDir, String sourcePath, String archiveName, boolean overwrite);
}
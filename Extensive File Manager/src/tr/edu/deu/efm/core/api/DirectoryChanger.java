package tr.edu.deu.efm.core.api;

/**
 * Defines the core contract for resolving and changing the active directory
 * within the file system.
 * <p>
 * <b>Architectural Note (Anti-Corruption Layer):</b> This interface
 * purposefully accepts primitive {@code String} representations rather than
 * {@code java.nio.file.Path} objects. This design guarantees that physical file
 * system abstractions (NIO.2) remain strictly isolated within the core
 * implementation layer, preventing I/O dependencies from leaking into the
 * presentation or command execution layers.
 * </p>
 */
public interface DirectoryChanger {

	/**
	 * Computes the new absolute path by resolving the target path against the
	 * current directory.
	 * <p>
	 * This method evaluates both absolute paths (e.g., {@code "/var/log"}) and
	 * relative paths (e.g., {@code "../documents"}, {@code "./bin"}), applying
	 * standard normalization to resolve parent ({@code ..}) and current ({@code .})
	 * directory segments. It strictly verifies the existence and valid directory
	 * status of the resulting path before returning success.
	 * </p>
	 *
	 * @param currentDirectory The absolute path of the user's current working
	 *                         directory. This serves as the baseline for resolving
	 *                         relative targets.
	 * @param targetPath       The destination path requested by the user (absolute
	 *                         or relative).
	 * @return An {@link OperationResult} containing the new, normalized absolute
	 *         path if successful, or a specific failure message (e.g., "Not a
	 *         directory", "Permission denied") if invalid.
	 */
	OperationResult changeDirectory(String currentDirectory, String targetPath);
}
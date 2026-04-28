package tr.edu.deu.efm.core.impl.changer;

import tr.edu.deu.efm.core.api.DirectoryChanger;
import tr.edu.deu.efm.core.api.OperationResult;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;

/**
 * The default implementation of the {@link DirectoryChanger} interface.
 * <p>
 * This class handles the resolution of target paths relative to the current
 * working directory and validates whether the resolved path is a valid and
 * accessible directory. It strictly adheres to the Result Pattern, returning
 * structured {@link OperationResult} objects containing specific messages for
 * both success and failure scenarios, delegating presentation logic to the
 * Command layer.
 * </p>
 */
public class DefaultDirectoryChanger implements DirectoryChanger {

	/**
	 * Attempts to change the current working directory to the specified target
	 * path.
	 *
	 * @param currentDirectoryStr The absolute path of the current working
	 *                            directory.
	 * @param targetPathStr       The relative or absolute path of the target
	 *                            directory.
	 * @return An {@link OperationResult}. If successful, {@code isSuccess()} is
	 *         true, {@code message} contains a success log, and
	 *         {@code affectedItems} contains the new path. If it fails,
	 *         {@code isSuccess()} is false and {@code message} contains the error
	 *         reason.
	 */
	@Override
	public OperationResult changeDirectory(String currentDirectoryStr, String targetPathStr) {

		try {
			Path currentPath = Paths.get(currentDirectoryStr);
			Path resolvedPath = currentPath.resolve(targetPathStr).normalize();

			if (!Files.exists(resolvedPath)) {
				return new OperationResult(false, "cd: " + targetPathStr + ": No such file or directory",
						Collections.emptyList());
			}

			if (!Files.isDirectory(resolvedPath)) {
				return new OperationResult(false, "cd: " + targetPathStr + ": Not a directory",
						Collections.emptyList());
			}

			return new OperationResult(true, "cd: successfully changed directory to '" + resolvedPath.toString() + "'",
					Collections.singletonList(resolvedPath.toString()));

		} catch (Exception e) {
			return new OperationResult(false, "cd: failed to change directory -> " + e.getMessage(),
					Collections.emptyList());
		}
	}
}
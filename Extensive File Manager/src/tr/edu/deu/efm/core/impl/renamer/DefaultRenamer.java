package tr.edu.deu.efm.core.impl.renamer;

import tr.edu.deu.efm.core.api.EntityRenamer;
import tr.edu.deu.efm.core.api.OperationResult;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;

/**
 * The default implementation of the {@link EntityRenamer} interface.
 * <p>
 * This class is responsible for safely changing the name of a file or directory
 * within its current parent directory. It resolves relative paths against the
 * provided session directory. It strictly implements the Result Pattern,
 * ensuring that all expected errors (such as missing files or naming
 * collisions) and unexpected I/O exceptions are gracefully caught and returned
 * as structured {@link OperationResult} objects, without relying on exception
 * throwing for control flow.
 * </p>
 */
public class DefaultRenamer implements EntityRenamer {

	/**
	 * Renames the entity at the given path to the specified new name.
	 * <p>
	 * This implementation resolves the initial path using the provided
	 * {@code currentDir}. It then utilizes {@link Path#resolveSibling(String)} to
	 * determine the destination path safely within the exact same parent directory,
	 * ensuring the entity is renamed rather than moved to a different folder tree.
	 * </p>
	 *
	 * @param currentDir The absolute path of the current working directory, acting
	 *                   as the anchor.
	 * @param entityPath The relative or absolute path of the entity to be renamed.
	 * @param newName    The exact new name for the entity (must not be a full
	 *                   directory path).
	 * @return An {@link OperationResult}. If successful, {@code isSuccess()} is
	 *         true, {@code message} contains a success log, and
	 *         {@code affectedItems} contains the new absolute path. If failed,
	 *         {@code isSuccess()} is false and {@code message} explains the
	 *         collision or missing file error.
	 */
	@Override
	public OperationResult rename(String currentDir, String entityPath, String newName) {

		Path currentPath = Paths.get(currentDir);
		Path source = currentPath.resolve(entityPath).normalize();

		Path destination = source.resolveSibling(newName);

		if (!Files.exists(source)) {
			return new OperationResult(false, "rn: cannot rename '" + entityPath + "': No such file or directory",
					Collections.emptyList());
		}

		if (Files.exists(destination)) {
			return new OperationResult(false,
					"rn: cannot rename to '" + newName + "': Destination entity already exists",
					Collections.emptyList());
		}

		try {
			Files.move(source, destination);

			return new OperationResult(true, "rn: successfully renamed '" + entityPath + "' to '" + newName + "'",
					Collections.singletonList(destination.toString()));

		} catch (Exception e) {
			return new OperationResult(false, "rn: failed to rename '" + entityPath + "' -> " + e.getMessage(),
					Collections.emptyList());
		}
	}
}
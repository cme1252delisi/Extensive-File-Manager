package tr.edu.deu.efm.core.impl.mover;

import tr.edu.deu.efm.core.api.OperationResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;

/**
 * A safe implementation of the {@link EntityMover} strategy.
 * <p>
 * This class enforces a strict "no-overwrite" policy. If the destination entity
 * already exists, the operation immediately fails without attempting the move.
 * </p>
 */
public class CopyMover extends BaseMover {

	@Override
	public OperationResult move(String currentDir, String sourceStr, String destStr) {
		Path currentPath = Paths.get(currentDir);
		Path source = currentPath.resolve(sourceStr).normalize();
		Path dest = currentPath.resolve(destStr).normalize();

		Path actualDest = dest;
		if (Files.isDirectory(dest)) {
			actualDest = dest.resolve(source.getFileName());
		}

		if (Files.exists(actualDest)) {
			return new OperationResult(false,
					"mv: cannot move '" + sourceStr + "' to '" + destStr + "': Destination entity already exists",
					Collections.emptyList());
		}

		return internalMove(currentDir, sourceStr, destStr);
	}
}
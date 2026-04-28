package tr.edu.deu.efm.core.impl.copier;

import tr.edu.deu.efm.core.api.OperationResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;

/**
 * A safe implementation of the {@link EntityCopier} strategy.
 * <p>
 * This class enforces a strict "no-overwrite" policy. If the destination entity
 * already exists, the operation immediately fails and returns an unsuccessful
 * {@link OperationResult} without modifying the file system.
 * </p>
 */
public class CopyCopier extends BaseCopier {

	@Override
	public OperationResult copy(String currentDir, String sourceStr, String destStr) {
		Path currentPath = Paths.get(currentDir);
		Path source = currentPath.resolve(sourceStr).normalize();
		Path dest = currentPath.resolve(destStr).normalize();

		Path actualDest = Files.isDirectory(dest) ? dest.resolve(source.getFileName()) : dest;

		if (Files.exists(actualDest)) {
			return new OperationResult(false, "cp: cannot copy to '" + destStr + "': Destination entity already exists",
					Collections.emptyList());
		}

		return internalCopy(currentDir, sourceStr, destStr);
	}
}
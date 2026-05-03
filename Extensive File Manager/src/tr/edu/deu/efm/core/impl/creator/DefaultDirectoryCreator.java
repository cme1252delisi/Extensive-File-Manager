package tr.edu.deu.efm.core.impl.creator;

import tr.edu.deu.efm.core.api.EntityCreator;
import tr.edu.deu.efm.core.api.OperationResult;
import java.nio.file.*;
import java.util.Collections;

/**
 * Core implementation for creating directories.
 */
public class DefaultDirectoryCreator implements EntityCreator {

	@Override
	public OperationResult create(String currentDir, String targetPath) {
		try {
			Path currentPath = Paths.get(currentDir);
			Path target = currentPath.resolve(targetPath).normalize();

			if (Files.exists(target)) {
				return new OperationResult(false, "mkdir: cannot create directory '" + targetPath + "': File exists",
						Collections.emptyList());
			}

			Files.createDirectories(target);

			return new OperationResult(true, "mkdir: created directory '" + targetPath + "'",
					Collections.singletonList(target.toString()));

		} catch (Exception e) {
			return new OperationResult(false, "mkdir: error creating directory -> " + e.getMessage(),
					Collections.emptyList());
		}
	}
}
package tr.edu.deu.efm.core.impl.creator;

import tr.edu.deu.efm.core.api.EntityCreator;
import tr.edu.deu.efm.core.api.OperationResult;
import java.nio.file.*;
import java.nio.file.attribute.FileTime;
import java.time.Instant;
import java.util.Collections;

/**
 * Core implementation for creating empty files or updating timestamps.
 */
public class DefaultFileCreator implements EntityCreator {

	@Override
	public OperationResult create(String currentDir, String targetPath) {
		try {
			Path currentPath = Paths.get(currentDir);
			Path target = currentPath.resolve(targetPath).normalize();

			if (Files.exists(target)) {
				Files.setLastModifiedTime(target, FileTime.from(Instant.now()));
				return new OperationResult(true, "touch: updated timestamp of existing file '" + targetPath + "'",
						Collections.singletonList(target.toString()));
			}

			Files.createFile(target);

			return new OperationResult(true, "touch: created empty file '" + targetPath + "'",
					Collections.singletonList(target.toString()));

		} catch (Exception e) {
			return new OperationResult(false, "touch: error creating file -> " + e.getMessage(),
					Collections.emptyList());
		}
	}
}
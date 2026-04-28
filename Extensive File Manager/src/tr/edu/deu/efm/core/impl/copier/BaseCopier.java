package tr.edu.deu.efm.core.impl.copier;

import tr.edu.deu.efm.core.api.EntityCopier;
import tr.edu.deu.efm.core.api.OperationResult;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Collections;

/**
 * An abstract base class providing the core recursive deep-copy engine.
 * <p>
 * Subclasses utilize this engine by supplying specific {@link CopyOption}
 * varargs to enforce their unique collision policies (e.g., replace existing
 * vs. keep).
 * </p>
 */
public abstract class BaseCopier implements EntityCopier {

	protected OperationResult internalCopy(String currentDir, String sourceStr, String destStr, CopyOption... options) {
		try {
			Path currentPath = Paths.get(currentDir);
			Path source = currentPath.resolve(sourceStr).normalize();
			Path dest = currentPath.resolve(destStr).normalize();

			if (!Files.exists(source)) {
				return new OperationResult(false, "cp: cannot stat '" + sourceStr + "': No such file or directory",
						Collections.emptyList());
			}

			Path targetPath = Files.isDirectory(dest) ? dest.resolve(source.getFileName()) : dest;

			Files.walkFileTree(source, new SimpleFileVisitor<Path>() {
				@Override
				public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
					Path targetDir = targetPath.resolve(source.relativize(dir));
					try {
						Files.copy(dir, targetDir, options);
					} catch (FileAlreadyExistsException e) {
						if (!Files.isDirectory(targetDir)) {
							throw e;
						}
					}
					return FileVisitResult.CONTINUE;
				}

				@Override
				public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
					Files.copy(file, targetPath.resolve(source.relativize(file)), options);
					return FileVisitResult.CONTINUE;
				}
			});

			return new OperationResult(true,
					"cp: successfully copied '" + sourceStr + "' to '" + targetPath.toString() + "'",
					Collections.singletonList(targetPath.toString()));

		} catch (Exception e) {
			return new OperationResult(false, "cp: recursive copy error -> " + e.getMessage(), Collections.emptyList());
		}
	}
}
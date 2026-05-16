package tr.edu.deu.efm.core.impl.copier;

import tr.edu.deu.efm.core.api.EntityCopier;
import tr.edu.deu.efm.core.api.OperationResult;
import tr.edu.deu.efm.core.api.ConfirmationStrategy;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Collections;

public class DefaultCopier implements EntityCopier {

	@Override
	public OperationResult copy(String currentDir, String sourceStr, String destStr, ConfirmationStrategy strategy) {
		try {
			Path currentPath = Paths.get(currentDir);
			Path source = currentPath.resolve(sourceStr).normalize();
			Path dest = currentPath.resolve(destStr).normalize();

			if (!Files.exists(source)) {
				return new OperationResult(false, "cannot stat '" + sourceStr + "': No such file or directory",
						Collections.emptyList());
			}

			Path targetPath = Files.isDirectory(dest) ? dest.resolve(source.getFileName()) : dest;

			Files.walkFileTree(source, new SimpleFileVisitor<Path>() {
				@Override
				public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
					Path targetDir = targetPath.resolve(source.relativize(dir));
					if (!Files.exists(targetDir)) {
						Files.createDirectories(targetDir);
					}
					return FileVisitResult.CONTINUE;
				}

				@Override
				public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
					Path targetFile = targetPath.resolve(source.relativize(file));

					if (Files.exists(targetFile)) {
						boolean overwriteAllowed = strategy.askPermission(targetFile.getFileName().toString());
						if (!overwriteAllowed) {
							return FileVisitResult.CONTINUE;
						}
					}

					Files.copy(file, targetFile, StandardCopyOption.REPLACE_EXISTING);
					return FileVisitResult.CONTINUE;
				}
			});

			return new OperationResult(true,
					"successfully copied '" + sourceStr + "' to '" + targetPath.toString() + "'",
					Collections.singletonList(targetPath.toString()));

		} catch (Exception e) {
			return new OperationResult(false, "recursive copy error -> " + e.getMessage(), Collections.emptyList());
		}
	}
}
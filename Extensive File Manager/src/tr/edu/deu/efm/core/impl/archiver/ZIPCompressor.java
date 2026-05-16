package tr.edu.deu.efm.core.impl.archiver;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Collections;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import tr.edu.deu.efm.core.api.EntityCompressor;
import tr.edu.deu.efm.core.api.OperationResult;
import tr.edu.deu.efm.core.api.ConfirmationStrategy;

public class ZIPCompressor implements EntityCompressor {

	@Override
	public OperationResult compress(String currentDir, String sourcePath, String archiveName,
			ConfirmationStrategy strategy) {
		try {
			Path current = Paths.get(currentDir);
			Path source = current.resolve(sourcePath).normalize();
			Path zipTarget = current.resolve(archiveName).normalize();

			if (!Files.exists(source)) {
				return new OperationResult(false, "zip: source does not exist: " + sourcePath, Collections.emptyList());
			}

			if (!zipTarget.toString().endsWith(".zip")) {
				zipTarget = Paths.get(zipTarget.toString() + ".zip");
			}

			if (Files.exists(zipTarget)) {
				boolean overwriteAllowed = strategy.askPermission(zipTarget.getFileName().toString());
				if (!overwriteAllowed) {
					return new OperationResult(false,
							"zip: compression aborted. archive already exists: '" + zipTarget.getFileName() + "'.",
							Collections.emptyList());
				}
				Files.delete(zipTarget);
			}

			try (ZipOutputStream zos = new ZipOutputStream(Files.newOutputStream(zipTarget))) {
				Files.walkFileTree(source, new SimpleFileVisitor<Path>() {
					@Override
					public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
						String zipEntryName = source.getParent().relativize(file).toString().replace("\\", "/");
						zos.putNextEntry(new ZipEntry(zipEntryName));
						Files.copy(file, zos);
						zos.closeEntry();
						return FileVisitResult.CONTINUE;
					}

					@Override
					public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
						if (!dir.equals(source.getParent())) {
							String zipEntryName = source.getParent().relativize(dir).toString().replace("\\", "/")
									+ "/";
							zos.putNextEntry(new ZipEntry(zipEntryName));
							zos.closeEntry();
						}
						return FileVisitResult.CONTINUE;
					}
				});
			}

			return new OperationResult(true, "successfully compressed to '" + zipTarget.getFileName() + "'",
					Collections.singletonList(zipTarget.toString()));

		} catch (Exception e) {
			return new OperationResult(false, "zip: compression failed -> " + e.getMessage(), Collections.emptyList());
		}
	}
}
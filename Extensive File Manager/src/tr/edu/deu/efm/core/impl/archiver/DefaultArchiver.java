package tr.edu.deu.efm.core.impl.archiver;

import tr.edu.deu.efm.core.api.EntityCompressor;
import tr.edu.deu.efm.core.api.EntityExtractor;
import tr.edu.deu.efm.core.api.OperationResult;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Collections;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * Core implementation for both compressing and extracting ZIP archives.
 * Includes "Zip Slip" vulnerability protection and safe overwrite checks.
 */
public class DefaultArchiver implements EntityCompressor, EntityExtractor {

	@Override
	public OperationResult compress(String currentDir, String sourcePath, String archiveName, boolean overwrite) {
		try {
			Path current = Paths.get(currentDir);
			Path source = current.resolve(sourcePath).normalize();
			Path zipTarget = current.resolve(archiveName).normalize();

			if (!Files.exists(source)) {
				return new OperationResult(false, "Source does not exist: " + sourcePath, Collections.emptyList());
			}

			if (!zipTarget.toString().endsWith(".zip")) {
				zipTarget = Paths.get(zipTarget.toString() + ".zip");
			}

			if (Files.exists(zipTarget)) {
				if (!overwrite) {
					return new OperationResult(false, "Compression aborted. Archive already exists: '"
							+ zipTarget.getFileName() + "'. Use -f flag to overwrite.", Collections.emptyList());
				}
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

			return new OperationResult(true, "Successfully compressed to '" + zipTarget.getFileName() + "'",
					Collections.singletonList(zipTarget.toString()));

		} catch (Exception e) {
			return new OperationResult(false, "Compression failed -> " + e.getMessage(), Collections.emptyList());
		}
	}

	@Override
	public OperationResult extract(String currentDir, String archivePath, String destinationPath, boolean overwrite) {
		try {
			Path current = Paths.get(currentDir);
			Path archive = current.resolve(archivePath).normalize();
			Path destDir = current.resolve(destinationPath).normalize();

			if (!Files.exists(archive) || !archive.toString().endsWith(".zip")) {
				return new OperationResult(false, "Valid ZIP archive not found: " + archivePath,
						Collections.emptyList());
			}

			Files.createDirectories(destDir);

			try (ZipInputStream zis = new ZipInputStream(Files.newInputStream(archive))) {
				ZipEntry zipEntry = zis.getNextEntry();

				while (zipEntry != null) {
					Path newPath = destDir.resolve(zipEntry.getName()).normalize();

					if (!newPath.startsWith(destDir)) {
						return new OperationResult(false, "Security Exception: Zip Slip vulnerability detected!",
								Collections.emptyList());
					}

					if (zipEntry.isDirectory()) {
						Files.createDirectories(newPath);
					} else {
						if (newPath.getParent() != null && Files.notExists(newPath.getParent())) {
							Files.createDirectories(newPath.getParent());
						}

						if (Files.exists(newPath)) {
							if (!overwrite) {
								return new OperationResult(false, "Extraction aborted. File already exists: '"
										+ newPath.getFileName() + "'. Use -f flag to overwrite.",
										Collections.emptyList());
							}
						}

						Files.copy(zis, newPath, StandardCopyOption.REPLACE_EXISTING);
					}
					zipEntry = zis.getNextEntry();
				}
				zis.closeEntry();
			}

			return new OperationResult(true, "Successfully extracted to '" + destDir.getFileName() + "'",
					Collections.singletonList(destDir.toString()));

		} catch (Exception e) {
			return new OperationResult(false, "Extraction failed -> " + e.getMessage(), Collections.emptyList());
		}
	}
}
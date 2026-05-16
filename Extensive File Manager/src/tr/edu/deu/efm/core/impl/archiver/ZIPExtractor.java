package tr.edu.deu.efm.core.impl.archiver;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Collections;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import tr.edu.deu.efm.core.api.EntityExtractor;
import tr.edu.deu.efm.core.api.OperationResult;
import tr.edu.deu.efm.core.api.ConfirmationStrategy;

public class ZIPExtractor implements EntityExtractor {

	@Override
	public OperationResult extract(String currentDir, String archivePath, String destinationPath,
			ConfirmationStrategy strategy) {
		try {
			Path current = Paths.get(currentDir);
			Path archive = current.resolve(archivePath).normalize();
			Path destDir = current.resolve(destinationPath).normalize();

			if (!Files.exists(archive) || !archive.toString().endsWith(".zip")) {
				return new OperationResult(false, "unzip: valid ZIP archive not found: " + archivePath,
						Collections.emptyList());
			}

			Files.createDirectories(destDir);

			try (ZipInputStream zis = new ZipInputStream(Files.newInputStream(archive))) {
				ZipEntry zipEntry = zis.getNextEntry();

				while (zipEntry != null) {
					Path newPath = destDir.resolve(zipEntry.getName()).normalize();

					if (!newPath.startsWith(destDir)) {
						return new OperationResult(false, "unzip: security exception: ZIP slip vulnerability detected!",
								Collections.emptyList());
					}

					if (zipEntry.isDirectory()) {
						Files.createDirectories(newPath);
					} else {
						if (newPath.getParent() != null && Files.notExists(newPath.getParent())) {
							Files.createDirectories(newPath.getParent());
						}

						if (Files.exists(newPath)) {
							boolean overwriteAllowed = strategy.askPermission(newPath.getFileName().toString());
							if (!overwriteAllowed) {
								zipEntry = zis.getNextEntry();
								continue;
							}
						}

						Files.copy(zis, newPath, StandardCopyOption.REPLACE_EXISTING);
					}
					zipEntry = zis.getNextEntry();
				}
				zis.closeEntry();
			}

			return new OperationResult(true, "successfully extracted to '" + destDir.getFileName() + "'",
					Collections.singletonList(destDir.toString()));

		} catch (Exception e) {
			return new OperationResult(false, "unzip: extraction failed -> " + e.getMessage(), Collections.emptyList());
		}
	}
}
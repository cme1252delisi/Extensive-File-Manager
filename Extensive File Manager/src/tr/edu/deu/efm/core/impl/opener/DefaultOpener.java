package tr.edu.deu.efm.core.impl.opener;

import tr.edu.deu.efm.core.api.EntityOpener;
import tr.edu.deu.efm.core.api.OperationResult;

import java.awt.Desktop;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Arrays;
import java.util.List;

public class DefaultOpener implements EntityOpener {

	private static final List<String> EXECUTABLE_EXTENSIONS = Arrays.asList("exe", "bat", "msi", "sh", "py", "bin");

	@Override
	public OperationResult open(String currentDir, String targetPath) {
		try {
			Path currentPath = Paths.get(currentDir);
			Path target = currentPath.resolve(targetPath).normalize();
			File targetFile = target.toFile();

			if (!Files.exists(target)) {
				return new OperationResult(false, "File does not exist: " + targetPath, Collections.emptyList());
			}

			String fileName = targetFile.getName().toLowerCase();
			String extension = getExtension(fileName);

			if (EXECUTABLE_EXTENSIONS.contains(extension)) {
				return executeFile(targetFile);
			} else {
				return openWithDesktop(targetFile);
			}

		} catch (Exception e) {
			return new OperationResult(false, "Error: " + e.getMessage(), Collections.emptyList());
		}
	}

	private OperationResult executeFile(File file) throws Exception {
		String os = System.getProperty("os.name").toLowerCase();

		if (!os.contains("win")) {
			if (!file.canExecute()) {
				file.setExecutable(true);
			}
		}

		ProcessBuilder pb;
		if (file.getName().toLowerCase().endsWith(".msi") && os.contains("win")) {
			pb = new ProcessBuilder("msiexec", "/i", file.getAbsolutePath());
		} else {
			pb = new ProcessBuilder(file.getAbsolutePath());
		}

		pb.directory(file.getParentFile());
		pb.start();

		return new OperationResult(true, "Program started: " + file.getName(),
				Collections.singletonList(file.getAbsolutePath()));
	}

	private OperationResult openWithDesktop(File file) throws Exception {
		if (!Desktop.isDesktopSupported()) {
			return new OperationResult(false, "Desktop operations not supported.", Collections.emptyList());
		}
		Desktop.getDesktop().open(file);
		return new OperationResult(true, "File opened: " + file.getName(),
				Collections.singletonList(file.getAbsolutePath()));
	}

	private String getExtension(String fileName) {
		int lastDot = fileName.lastIndexOf('.');
		return (lastDot == -1) ? "" : fileName.substring(lastDot + 1);
	}
}
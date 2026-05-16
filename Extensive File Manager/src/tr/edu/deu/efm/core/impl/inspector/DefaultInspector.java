package tr.edu.deu.efm.core.impl.inspector;

import tr.edu.deu.efm.core.api.EntityInspector;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class DefaultInspector implements EntityInspector {

	@Override
	public boolean exists(String currentDir, String targetPath) {
		Path target = Paths.get(currentDir).resolve(targetPath).normalize();
		return Files.exists(target);
	}

	@Override
	public boolean isDirectory(String currentDir, String targetPath) {
		Path target = Paths.get(currentDir).resolve(targetPath).normalize();
		return Files.isDirectory(target);
	}
}
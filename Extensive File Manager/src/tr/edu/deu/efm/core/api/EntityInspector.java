package tr.edu.deu.efm.core.api;

public interface EntityInspector {
	boolean exists(String currentDir, String targetPath);

	boolean isDirectory(String currentDir, String targetPath);
}
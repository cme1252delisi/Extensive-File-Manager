package tr.edu.deu.efm.core.impl;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

import tr.edu.deu.efm.command.api.CommandFlags;

import java.io.IOException;

public class ExtensiveFileManager {

	/**
	 * Changes the name of an entity. It can be a file or a folder
	 * 
	 * @param entityPath The full path of the entity to be changed (e.g.,
	 *                   "C:/file.txt")
	 * @param newName    The new name of the entity (e.g., "new_file.txt")
	 * @return Returns true if the operation is successful, false if unsuccessful.
	 */
	public static boolean renameEntity(String entityPath, String newName) {
		Path source = Paths.get(entityPath);
		Path destination = source.resolveSibling(newName);

		try {
			if (!Files.exists(source)) {
				// System.err.println("Error: Path not found -> " + entityPath);
				return false;
			}

			if (Files.isDirectory(source)) {
				return renameDirectory(source, destination);
			} else {
				return renameFile(source, destination);
			}
		} catch (Exception e) {
			// System.err.println("Error Type: " + e.getClass().getSimpleName());
			// System.err.println("Problematic Path: " + e.getMessage());
			return false;
		}
	}

	private static boolean renameDirectory(Path source, Path destination) throws IOException {
		// System.out.println("Renaming directory: " + source.getFileName() + " to " + destination.getFileName());
		Files.move(source, destination);
		return true;
	}

	private static boolean renameFile(Path source, Path destination) throws IOException {
		// System.out.println("Renaming file: " + source.getFileName() + " to " + destination.getFileName());
		Files.move(source, destination);
		return true;
	}	
	
}
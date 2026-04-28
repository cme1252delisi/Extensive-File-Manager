package tr.edu.deu.efm.core.impl.lister;

import tr.edu.deu.efm.core.api.EntityLister;
import tr.edu.deu.efm.core.api.OperationResult;

import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The default implementation of the {@link EntityLister} interface.
 * <p>
 * This class is responsible for retrieving the contents of a directory or
 * details of a specific file. It intelligently resolves relative paths using
 * the session's current working directory. It supports filtering hidden files
 * and providing detailed metadata (e.g., file size and type). Furthermore, it
 * strictly adheres to the Result Pattern, ensuring all expected errors (like
 * missing paths or permission denials) and unexpected I/O exceptions are
 * gracefully caught and returned as structured {@link OperationResult} objects.
 * </p>
 */
public class DefaultLister implements EntityLister {

	/**
	 * Lists the contents of the specified path.
	 *
	 * @param currentDir The absolute path of the current working directory, used as
	 *                   the anchor for resolving relative target paths (e.g.,
	 *                   ".\Downloads" or "..").
	 * @param pathStr    The absolute or relative path of the directory (or file) to
	 *                   list.
	 * @param showHidden If true, includes hidden files (files starting with '.') in
	 *                   the output.
	 * @param detailed   If true, appends metadata (type and size) to each file
	 *                   name.
	 * @return An {@link OperationResult}. If successful, {@code isSuccess()} is
	 *         true and {@code affectedItems} contains the alphabetically sorted
	 *         list of file/folder names. If failed, {@code isSuccess()} is false
	 *         and {@code message} contains the error reason.
	 */
	@Override
	public OperationResult list(String currentDir, String pathStr, boolean showHidden, boolean detailed) {
		List<String> results = new ArrayList<>();
		try {
			Path currentPath = Paths.get(currentDir);
			Path path = currentPath.resolve(pathStr).normalize();

			if (!Files.exists(path)) {
				return new OperationResult(false, "ls: cannot access '" + pathStr + "': No such file or directory",
						Collections.emptyList());
			}

			if (!Files.isDirectory(path)) {
				String fileInfo = detailed ? getDetailedString(path) : path.getFileName().toString();
				return new OperationResult(true, "ls: successfully listed file '" + pathStr + "'",
						Collections.singletonList(fileInfo));
			}

			try (DirectoryStream<Path> stream = Files.newDirectoryStream(path)) {
				for (Path entry : stream) {
					String name = entry.getFileName().toString();

					if (!showHidden && name.startsWith(".")) {
						continue;
					}

					if (detailed) {
						results.add(getDetailedString(entry));
					} else {
						results.add(name);
					}
				}
			}

			Collections.sort(results);

			return new OperationResult(true, "ls: successfully listed contents of '" + pathStr + "'", results);

		} catch (AccessDeniedException e) {
			return new OperationResult(false, "ls: cannot open directory '" + pathStr + "': Permission denied",
					Collections.emptyList());
		} catch (Exception e) {
			return new OperationResult(false,
					"ls: fatal error occurred while listing '" + pathStr + "' -> " + e.getMessage(),
					Collections.emptyList());
		}
	}

	/**
	 * Formats a path into a detailed string containing its type, size, and name.
	 * * @param p The path of the file or directory to inspect.
	 * 
	 * @return A formatted string (e.g., "- 1024 bytes document.txt").
	 * @throws Exception If file attributes cannot be read.
	 */
	private String getDetailedString(Path p) throws Exception {
		BasicFileAttributes attr = Files.readAttributes(p, BasicFileAttributes.class);
		String type = attr.isDirectory() ? "d" : "-";
		return String.format("%s %12d bytes  %s", type, attr.size(), p.getFileName());
	}
}
package tr.edu.deu.efm.core.impl.remover;

import tr.edu.deu.efm.core.api.EntityRemover;
import tr.edu.deu.efm.core.api.OperationResult;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * An abstract base class providing the core recursive deletion engine.
 * <p>
 * This class implements the Result Pattern, gracefully handling common errors
 * like missing files or attempting to delete a directory without the recursive
 * flag. It uses {@link Files#walkFileTree} with {@code postVisitDirectory} to
 * ensure children are deleted before their parent folders. Subclasses must
 * implement the physical removal action via {@code performRemove}.
 * </p>
 */
public abstract class BaseRemover implements EntityRemover {

	@Override
	public OperationResult remove(String currentDir, String target, boolean recursive) {
		try {
			Path currentPath = Paths.get(currentDir);
			Path path = currentPath.resolve(target).normalize();

			if (!Files.exists(path)) {
				return new OperationResult(false, "rm: cannot remove '" + target + "': No such file or directory",
						Collections.emptyList());
			}

			if (Files.isDirectory(path) && !recursive) {
				return new OperationResult(false, "rm: cannot remove '" + target + "': Is a directory",
						Collections.emptyList());
			}

			List<String> affectedItems = new ArrayList<>();

			if (recursive && Files.isDirectory(path)) {
				walkAndPerform(path, affectedItems);
			} else {
				performRemove(path);
				affectedItems.add(path.toString());
			}

			return new OperationResult(true, "rm: successfully removed '" + target + "'", affectedItems);

		} catch (Exception e) {
			return new OperationResult(false, "rm: error occurred during removal -> " + e.getMessage(),
					Collections.emptyList());
		}
	}

	private void walkAndPerform(Path root, List<String> affectedItems) throws IOException {
		Files.walkFileTree(root, new SimpleFileVisitor<Path>() {

			@Override
			public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
				try {
					performRemove(file);
					affectedItems.add(file.toString());
				} catch (Exception e) {
					throw new IOException(e);
				}
				return FileVisitResult.CONTINUE;
			}

			@Override
			public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
				try {
					performRemove(dir);
					affectedItems.add(dir.toString());
				} catch (Exception e) {
					throw new IOException(e);
				}
				return FileVisitResult.CONTINUE;
			}
		});
	}

	/**
	 * The actual removal strategy to be implemented by subclasses.
	 */
	protected abstract void performRemove(Path path) throws Exception;
}
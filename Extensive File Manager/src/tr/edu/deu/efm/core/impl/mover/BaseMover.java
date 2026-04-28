package tr.edu.deu.efm.core.impl.mover;

import tr.edu.deu.efm.core.api.EntityMover;
import tr.edu.deu.efm.core.api.OperationResult;
import java.nio.file.*;
import java.util.Collections;

/**
 * An abstract base class providing the core move engine.
 * <p>
 * Subclasses utilize this engine by supplying specific {@link CopyOption}
 * varargs to enforce their unique collision policies (e.g., replace existing
 * vs. fail). Moving a directory in Java NIO.2 automatically moves its entire
 * contents, so a recursive file walker is not required here unlike the
 * deep-copy process.
 * </p>
 */
public abstract class BaseMover implements EntityMover {

	protected OperationResult internalMove(String currentDir, String sourceStr, String destStr, CopyOption... options) {
		try {
			Path currentPath = Paths.get(currentDir);
			Path source = currentPath.resolve(sourceStr).normalize();
			Path dest = currentPath.resolve(destStr).normalize();

			if (!Files.exists(source)) {
				return new OperationResult(false, "mv: cannot stat '" + sourceStr + "': No such file or directory",
						Collections.emptyList());
			}

			if (Files.isDirectory(dest)) {
				dest = dest.resolve(source.getFileName());
			}

			Files.move(source, dest, options);

			return new OperationResult(true, "mv: successfully moved '" + sourceStr + "' to '" + dest.toString() + "'",
					Collections.singletonList(dest.toString()));

		} catch (FileAlreadyExistsException e) {
			return new OperationResult(false, "mv: destination already exists: " + destStr, Collections.emptyList());
		} catch (Exception e) {
			return new OperationResult(false, "mv: error -> " + e.getMessage(), Collections.emptyList());
		}
	}
}
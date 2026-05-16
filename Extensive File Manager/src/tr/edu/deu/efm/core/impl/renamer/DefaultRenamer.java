package tr.edu.deu.efm.core.impl.renamer;

import tr.edu.deu.efm.core.api.EntityRenamer;
import tr.edu.deu.efm.core.api.OperationResult;
import tr.edu.deu.efm.core.api.ConfirmationStrategy;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Collections;

public class DefaultRenamer implements EntityRenamer {

	@Override
	public OperationResult rename(String currentDir, String entityPath, String newName, ConfirmationStrategy strategy) {

		Path currentPath = Paths.get(currentDir);
		Path source = currentPath.resolve(entityPath).normalize();
		Path destination = source.resolveSibling(newName);

		if (!Files.exists(source)) {
			return new OperationResult(false, "cannot rename '" + entityPath + "': No such file or directory",
					Collections.emptyList());
		}

		if (Files.exists(destination)) {
			boolean overwriteAllowed = strategy.askPermission(newName);
			if (!overwriteAllowed) {
				return new OperationResult(false,
						"cannot rename to '" + newName + "': Destination entity already exists",
						Collections.emptyList());
			}
		}

		try {
			Files.move(source, destination, StandardCopyOption.REPLACE_EXISTING);

			return new OperationResult(true, "successfully renamed '" + entityPath + "' to '" + newName + "'",
					Collections.singletonList(destination.toString()));

		} catch (Exception e) {
			return new OperationResult(false, "failed to rename '" + entityPath + "' -> " + e.getMessage(),
					Collections.emptyList());
		}
	}
}
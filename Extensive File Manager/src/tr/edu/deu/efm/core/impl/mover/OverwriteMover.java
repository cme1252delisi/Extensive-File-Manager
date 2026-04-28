package tr.edu.deu.efm.core.impl.mover;

import tr.edu.deu.efm.core.api.OperationResult;
import java.nio.file.StandardCopyOption;

/**
 * An aggressive implementation of the {@link EntityMover} strategy.
 * <p>
 * This class enforces an "overwrite" policy. It delegates the moving process to
 * the base engine while passing the {@link StandardCopyOption#REPLACE_EXISTING}
 * flag, ensuring that any existing files at the target destination are
 * replaced.
 * </p>
 */
public class OverwriteMover extends BaseMover {

	@Override
	public OperationResult move(String currentDir, String source, String destination) {
		return internalMove(currentDir, source, destination, StandardCopyOption.REPLACE_EXISTING);
	}
}
package tr.edu.deu.efm.core.impl.copier;

import tr.edu.deu.efm.core.api.EntityCopier;
import tr.edu.deu.efm.core.api.OperationResult;
import java.nio.file.StandardCopyOption;

/**
 * An aggressive implementation of the {@link EntityCopier} strategy.
 * <p>
 * This class enforces an "overwrite" policy. It delegates the copying process
 * to the base engine while explicitly passing the
 * {@link StandardCopyOption#REPLACE_EXISTING} flag, ensuring that any existing
 * files at the target destination are silently overwritten.
 * </p>
 */
public class OverwriteCopier extends BaseCopier {

	@Override
	public OperationResult copy(String currentDir, String source, String destination) {
		return internalCopy(currentDir, source, destination, StandardCopyOption.REPLACE_EXISTING);
	}
}
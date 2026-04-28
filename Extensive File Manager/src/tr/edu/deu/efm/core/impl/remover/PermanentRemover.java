package tr.edu.deu.efm.core.impl.remover;

import java.nio.file.Files;
import java.nio.file.Path;

/**
 * An implementation of the {@link EntityRemover} strategy that permanently
 * deletes files from the file system, bypassing the OS trash/recycle bin.
 */
public class PermanentRemover extends BaseRemover {

	@Override
	protected void performRemove(Path path) throws Exception {
		Files.deleteIfExists(path);
	}
}
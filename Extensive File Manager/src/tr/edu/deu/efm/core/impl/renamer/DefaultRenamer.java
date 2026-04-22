package tr.edu.deu.efm.core.impl.renamer;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import tr.edu.deu.efm.core.api.EntityRenamer;
import tr.edu.deu.efm.core.api.OperationResult;
import tr.edu.deu.efm.core.exception.EntityNotFoundException;

public class DefaultRenamer implements EntityRenamer {
	
	/**
	 * Changes the name of an entity. It can be a file or a folder
	 * 
	 * @param entityPath The full path of the entity to be changed (e.g.,
	 *                   "C:/file.txt")
	 * @param newName    The new name of the entity (e.g., "new_file.txt")
	 * @return Returns OperationResult.
	 */
	@Override
	public OperationResult rename(String entityPath, String newName) {
		Path source = Paths.get(entityPath);
		Path destination = source.resolveSibling(newName);
		List<String> affectedItems = new ArrayList<String>();

		try {
			if (!Files.exists(source)) {
				throw new EntityNotFoundException(entityPath);
			}

			Files.move(source, destination);
			affectedItems.add(entityPath);
			boolean success = true;
			
			return new OperationResult(success, affectedItems);
		} catch (Exception e) {
			boolean success = false;
			return new OperationResult(success, affectedItems);
		}
	}
	

}

package tr.edu.deu.efm.core.impl.lister;

import tr.edu.deu.efm.core.api.EntityLister;
import tr.edu.deu.efm.core.api.OperationResult;
import tr.edu.deu.efm.core.exception.EntityNotFoundException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DefaultLister implements EntityLister {

	@Override
	public OperationResult list(String pathStr) {
		Path targetPath = Paths.get(pathStr);

		if (!Files.exists(targetPath)) {
			throw new EntityNotFoundException("Cannot access '" + pathStr + "': No such file or directory");
		}

		List<String> affectedItems = new ArrayList<>();

		if (Files.isDirectory(targetPath)) {

			try (Stream<Path> stream = Files.list(targetPath)) {

				affectedItems = stream.map(p -> p.getFileName().toString()).collect(Collectors.toList());

			} catch (IOException e) {
				return new OperationResult(false, affectedItems);
			}

		} else {
			affectedItems.add(targetPath.getFileName().toString());
		}

		return new OperationResult(true, affectedItems);
	}
}
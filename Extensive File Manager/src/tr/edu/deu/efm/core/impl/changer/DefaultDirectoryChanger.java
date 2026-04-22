package tr.edu.deu.efm.core.impl.changer;

import tr.edu.deu.efm.core.api.DirectoryChanger;
import tr.edu.deu.efm.core.api.OperationResult;
import tr.edu.deu.efm.core.exception.EntityNotFoundException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DefaultDirectoryChanger implements DirectoryChanger {

    @Override
    public OperationResult changeDirectory(String currentDirectoryStr, String targetPathStr) {

        Path currentPath = Paths.get(currentDirectoryStr);
        Path resolvedPath = currentPath.resolve(targetPathStr).normalize();

        if (!Files.exists(resolvedPath)) {
            throw new EntityNotFoundException("cd: " + targetPathStr + ": No such file or directory");
        }
        
		List<String> affectedItems = new ArrayList<>();

        if (!Files.isDirectory(resolvedPath)) {
            return new OperationResult(false, affectedItems);
        }

        return new OperationResult(true, Collections.singletonList(resolvedPath.toString()));
    }
}
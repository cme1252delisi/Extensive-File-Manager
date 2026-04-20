package tr.edu.deu.efm.core.impl;

import tr.edu.deu.efm.core.api.EntityRemover;
import tr.edu.deu.efm.core.dto.OperationResult;
import tr.edu.deu.efm.core.exception.EntityNotFoundException;
import tr.edu.deu.efm.core.exception.IllegalDirectoryOperationException;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseRemover implements EntityRemover {

    @Override
    public OperationResult remove(String target, boolean recursive, boolean verbose) {
        Path path = Paths.get(target);

        if (!Files.exists(path)) {
            throw new EntityNotFoundException(target);
        }

        if (Files.isDirectory(path) && !recursive) {
            throw new IllegalDirectoryOperationException(target);
        }

        List<String> affectedItems = new ArrayList<>();

        try {
            if (recursive && Files.isDirectory(path)) {
                walkAndPerform(path, affectedItems); 
            } else {
                performRemove(path);
                affectedItems.add(path.toString());
            }
            return new OperationResult(true, affectedItems);

        } catch (Exception e) {
            return new OperationResult(false, affectedItems);
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

    protected abstract void performRemove(Path path) throws Exception;
}
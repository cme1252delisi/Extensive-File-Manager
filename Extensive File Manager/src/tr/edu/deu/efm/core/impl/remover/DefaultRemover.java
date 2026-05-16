package tr.edu.deu.efm.core.impl.remover;

import tr.edu.deu.efm.core.api.EntityRemover;
import tr.edu.deu.efm.core.api.OperationResult;
import tr.edu.deu.efm.core.api.ConfirmationStrategy;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DefaultRemover implements EntityRemover {

    @Override
    public OperationResult remove(String currentDir, String target, boolean recursive, boolean permanent, ConfirmationStrategy strategy) {
        try {
            Path currentPath = Paths.get(currentDir);
            Path path = currentPath.resolve(target).normalize();

            if (!Files.exists(path)) {
                return new OperationResult(false, "cannot remove '" + target + "': No such file or directory", Collections.emptyList());
            }

            if (Files.isDirectory(path) && !recursive) {
                return new OperationResult(false, "cannot remove '" + target + "': Is a directory", Collections.emptyList());
            }

            if (!permanent) {
                Path trashDir = Paths.get(System.getProperty("user.home"), ".efm_trash");
                if (!Files.exists(trashDir)) {
                    Files.createDirectories(trashDir);
                }

                boolean canMove = strategy.askPermission("rm: move '" + path.getFileName() + "' to trash?");
                if (!canMove) {
                    return new OperationResult(false, "operation aborted by user.", Collections.emptyList());
                }

                Path trashTarget = trashDir.resolve(path.getFileName().toString() + "_" + System.currentTimeMillis());
                Files.move(path, trashTarget, StandardCopyOption.REPLACE_EXISTING);

                return new OperationResult(true, "moved to trash: '" + target + "'", Collections.singletonList(trashTarget.toString()));
            }
            
            List<String> affectedItems = new ArrayList<>();
            List<String> errors = new ArrayList<>();

            if (recursive && Files.isDirectory(path)) {
                Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
                    @Override
                    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                        boolean canDelete = strategy.askPermission("rm: permanently remove regular file '" + file.getFileName() + "'?");
                        if (canDelete) {
                            try {
                                Files.delete(file);
                                affectedItems.add(file.toString());
                            } catch (Exception e) {
                                errors.add("cannot remove '" + file.getFileName() + "': " + e.getMessage());
                            }
                        }
                        return FileVisitResult.CONTINUE;
                    }

                    @Override
                    public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                        boolean canDelete = strategy.askPermission("rm: permanently remove directory '" + dir.getFileName() + "'?");
                        if (canDelete) {
                            try {
                                Files.delete(dir);
                                affectedItems.add(dir.toString());
                            } catch (DirectoryNotEmptyException e) {
                                errors.add("cannot remove '" + dir.getFileName() + "': Directory not empty");
                            } catch (Exception e) {
                                errors.add("cannot remove '" + dir.getFileName() + "': " + e.getMessage());
                            }
                        }
                        return FileVisitResult.CONTINUE;
                    }
                });
            } else {
                boolean canDelete = strategy.askPermission("rm: permanently remove regular file '" + path.getFileName() + "'?");
                if (canDelete) {
                    try {
                        Files.delete(path);
                        affectedItems.add(path.toString());
                    } catch (Exception e) {
                        return new OperationResult(false, "cannot remove '" + path.getFileName() + "': " + e.getMessage(), Collections.emptyList());
                    }
                }
            }

            if (!errors.isEmpty()) {
                String aggregatedErrors = String.join("\n", errors);
                return new OperationResult(false, aggregatedErrors, affectedItems);
            }

            return new OperationResult(true, "successfully processed removal of '" + target + "'", affectedItems);

        } catch (Exception e) {
            return new OperationResult(false, "error occurred during removal -> " + e.getMessage(), Collections.emptyList());
        }
    }
}
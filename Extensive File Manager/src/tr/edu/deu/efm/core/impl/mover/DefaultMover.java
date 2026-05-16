package tr.edu.deu.efm.core.impl.mover;

import tr.edu.deu.efm.core.api.EntityMover;
import tr.edu.deu.efm.core.api.OperationResult;
import tr.edu.deu.efm.core.api.ConfirmationStrategy;

import java.nio.file.*;
import java.util.Collections;

public class DefaultMover implements EntityMover {

    @Override
    public OperationResult move(String currentDir, String sourceStr, String destStr, ConfirmationStrategy strategy) {
        try {
            Path currentPath = Paths.get(currentDir);
            Path source = currentPath.resolve(sourceStr).normalize();
            Path dest = currentPath.resolve(destStr).normalize();

            if (!Files.exists(source)) {
                return new OperationResult(false, "cannot stat '" + sourceStr + "': No such file or directory", Collections.emptyList());
            }

            if (Files.isDirectory(dest)) {
                dest = dest.resolve(source.getFileName());
            }

            if (Files.exists(dest)) {
                boolean overwriteAllowed = strategy.askPermission(dest.getFileName().toString());
                
                if (!overwriteAllowed) {
                    return new OperationResult(false, "cannot move '" + sourceStr + "': Destination entity '" + dest.getFileName() + "' already exists.", Collections.emptyList());
                }
            }

            Files.move(source, dest, StandardCopyOption.REPLACE_EXISTING);

            return new OperationResult(true, "successfully moved '" + sourceStr + "' to '" + dest.toString() + "'", Collections.singletonList(dest.toString()));

        } catch (Exception e) {
            return new OperationResult(false, "error -> " + e.getMessage(), Collections.emptyList());
        }
    }
}
package tr.edu.deu.efm.core.impl.remover;

import java.awt.Desktop;
import java.nio.file.Path;

public class TrashBinRemover extends BaseRemover {

    @Override
    protected void performRemove(Path path) throws Exception {
        boolean supported = Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.MOVE_TO_TRASH);
        
        if (supported) {
            Desktop.getDesktop().moveToTrash(path.toFile());
        } else {
            throw new UnsupportedOperationException("Trash operation is not supported on this OS.");
        }
    }
}
package tr.edu.deu.efm.core.impl;

import java.nio.file.Files;
import java.nio.file.Path;

public class PermanentRemover extends BaseRemover {
    @Override
    protected void performRemove(Path path) throws Exception {
        Files.deleteIfExists(path);
    }
}

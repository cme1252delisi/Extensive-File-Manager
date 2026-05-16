package tr.edu.deu.efm.core.impl.converter.engine.image.impl;

import java.awt.image.BufferedImage;

public class StandardImageConverter extends BaseImageConverter {

    @Override
    protected BufferedImage processImage(BufferedImage originalImage) {
        return originalImage;
    }
}
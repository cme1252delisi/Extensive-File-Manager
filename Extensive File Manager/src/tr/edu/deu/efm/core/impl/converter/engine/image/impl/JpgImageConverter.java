package tr.edu.deu.efm.core.impl.converter.engine.image.impl;

import java.awt.*;
import java.awt.image.BufferedImage;

public class JpgImageConverter extends BaseImageConverter {

    @Override
    protected BufferedImage processImage(BufferedImage originalImage) {
        BufferedImage newImage = new BufferedImage(
                originalImage.getWidth(), 
                originalImage.getHeight(), 
                BufferedImage.TYPE_INT_RGB
        );
        Graphics2D g = newImage.createGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, originalImage.getWidth(), originalImage.getHeight());
        g.drawImage(originalImage, 0, 0, null);
        g.dispose();
        
        return newImage;
    }
}
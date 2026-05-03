package tr.edu.deu.efm.core.impl.converter.engine;

import tr.edu.deu.efm.core.api.EntityConverter;
import tr.edu.deu.efm.core.api.OperationResult;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;

/**
 * Conversion engine responsible for handling IMAGE family formats.
 * Uses Java's built-in ImageIO, requiring no external dependencies.
 */
public class ImageEngine implements EntityConverter {

    @Override
    public OperationResult convert(String currentDir, String sourcePath, String targetPath, boolean overwrite) {
        try {
            Path current = Paths.get(currentDir);
            File sourceFile = current.resolve(sourcePath).normalize().toFile();
            File targetFile = current.resolve(targetPath).normalize().toFile();

            BufferedImage image = ImageIO.read(sourceFile);
            if (image == null) {
                return new OperationResult(false, "Could not read the image. The file might be corrupted.", Collections.emptyList());
            }

            String targetExt = getExtension(targetFile.getName());
           
            if (targetExt.equalsIgnoreCase("jpg") || targetExt.equalsIgnoreCase("jpeg")) {
                BufferedImage newImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
                Graphics2D g = newImage.createGraphics();
                g.setColor(Color.WHITE);
                g.fillRect(0, 0, image.getWidth(), image.getHeight());
                g.drawImage(image, 0, 0, null);
                g.dispose();
                
                image = newImage;
            }

            boolean success = ImageIO.write(image, targetExt, targetFile);

            if (success) {
                return new OperationResult(true, "Image successfully converted to '" + targetFile.getName() + "'", Collections.singletonList(targetFile.getAbsolutePath()));
            } else {
                return new OperationResult(false, "No appropriate image writer found for format: " + targetExt, Collections.emptyList());
            }

        } catch (Exception e) {
            return new OperationResult(false, "Image conversion failed -> " + e.getMessage(), Collections.emptyList());
        }
    }

    private String getExtension(String fileName) {
        int lastDot = fileName.lastIndexOf('.');
        return (lastDot == -1) ? "" : fileName.substring(lastDot + 1);
    }
}
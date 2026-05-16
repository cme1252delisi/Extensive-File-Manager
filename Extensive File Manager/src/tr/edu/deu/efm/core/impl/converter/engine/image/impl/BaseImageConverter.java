package tr.edu.deu.efm.core.impl.converter.engine.image.impl;

import tr.edu.deu.efm.core.api.ConfirmationStrategy;
import tr.edu.deu.efm.core.api.EntityConverter;
import tr.edu.deu.efm.core.api.OperationResult;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;

public abstract class BaseImageConverter implements EntityConverter {

	@Override
	public OperationResult convert(String currentDir, String sourcePath, String targetPath,
			ConfirmationStrategy strategy) {
		try {
			Path current = Paths.get(currentDir);
			File sourceFile = current.resolve(sourcePath).normalize().toFile();
			File targetFile = current.resolve(targetPath).normalize().toFile();

			BufferedImage image = ImageIO.read(sourceFile);
			if (image == null) {
				return new OperationResult(false,
						"could not read the image. the file might be corrupted or unsupported.",
						Collections.emptyList());
			}

			BufferedImage processedImage = processImage(image);

			String targetExt = getExtension(targetFile.getName());
			boolean success = ImageIO.write(processedImage, targetExt, targetFile);

			if (success) {
				return new OperationResult(true, "successfully converted image to '" + targetFile.getName() + "'",
						Collections.singletonList(targetFile.getAbsolutePath()));
			} else {
				return new OperationResult(false, "no appropriate image writer found for format: " + targetExt,
						Collections.emptyList());
			}

		} catch (Exception e) {
			return new OperationResult(false, "image conversion failed -> " + e.getMessage(), Collections.emptyList());
		}
	}

	protected abstract BufferedImage processImage(BufferedImage originalImage);

	protected String getExtension(String fileName) {
		int lastDot = fileName.lastIndexOf('.');
		return (lastDot == -1) ? "" : fileName.substring(lastDot + 1);
	}
}
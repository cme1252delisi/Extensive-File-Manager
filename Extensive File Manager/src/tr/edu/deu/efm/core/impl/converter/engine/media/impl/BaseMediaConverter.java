package tr.edu.deu.efm.core.impl.converter.engine.media.impl;

import tr.edu.deu.efm.core.api.ConfirmationStrategy;
import tr.edu.deu.efm.core.api.EntityConverter;
import tr.edu.deu.efm.core.api.OperationResult;
import ws.schild.jave.Encoder;
import ws.schild.jave.MultimediaObject;
import ws.schild.jave.encode.EncodingAttributes;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;

public abstract class BaseMediaConverter implements EntityConverter {

	@Override
	public OperationResult convert(String currentDir, String sourcePath, String targetPath,
			ConfirmationStrategy strategy) {
		try {
			Path current = Paths.get(currentDir);
			File sourceFile = current.resolve(sourcePath).normalize().toFile();
			File targetFile = current.resolve(targetPath).normalize().toFile();

			String targetExt = getExtension(targetFile.getName()).toLowerCase();

			EncodingAttributes attrs = getEncodingAttributes(targetExt);

			Encoder encoder = new Encoder();
			MultimediaObject multimediaObject = new MultimediaObject(sourceFile);

			encoder.encode(multimediaObject, targetFile, attrs);

			return new OperationResult(true, "media successfully converted to '" + targetFile.getName() + "'",
					Collections.singletonList(targetFile.getAbsolutePath()));

		} catch (Exception e) {
			return new OperationResult(false, "media conversion failed -> " + e.getMessage(), Collections.emptyList());
		}
	}

	protected abstract EncodingAttributes getEncodingAttributes(String targetExt);

	protected String getExtension(String fileName) {
		int lastDot = fileName.lastIndexOf('.');
		return (lastDot == -1) ? "" : fileName.substring(lastDot + 1);
	}
}
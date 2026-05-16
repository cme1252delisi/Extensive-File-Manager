package tr.edu.deu.efm.core.impl.converter.engine.image;

import tr.edu.deu.efm.core.api.ConfirmationStrategy;
import tr.edu.deu.efm.core.api.EntityConverter;
import tr.edu.deu.efm.core.api.OperationResult;
import tr.edu.deu.efm.core.impl.converter.engine.image.impl.JpgImageConverter;
import tr.edu.deu.efm.core.impl.converter.engine.image.impl.StandardImageConverter;

import java.util.HashMap;
import java.util.Map;
import java.util.Collections;

public class ImageEngine implements EntityConverter {

	private final Map<String, EntityConverter> convertTools = new HashMap<>();

	public ImageEngine() {
		EntityConverter jpgConverter = new JpgImageConverter();
		EntityConverter standardConverter = new StandardImageConverter();

		convertTools.put("jpg", jpgConverter);
		convertTools.put("jpeg", jpgConverter);

		convertTools.put("png", standardConverter);
		convertTools.put("bmp", standardConverter);
		convertTools.put("gif", standardConverter);
	}

	@Override
	public OperationResult convert(String currentDir, String sourcePath, String targetPath,
			ConfirmationStrategy strategy) {
		String targetExt = getExtension(targetPath).toLowerCase();

		EntityConverter convertTool = convertTools.get(targetExt);

		if (convertTool == null) {
			return new OperationResult(false, "unsupported target image format: " + targetExt, Collections.emptyList());
		}

		return convertTool.convert(currentDir, sourcePath, targetPath, strategy);
	}

	private String getExtension(String path) {
		int lastDotIndex = path.lastIndexOf('.');
		if (lastDotIndex > 0 && lastDotIndex < path.length() - 1) {
			return path.substring(lastDotIndex + 1);
		}
		return "";
	}
}
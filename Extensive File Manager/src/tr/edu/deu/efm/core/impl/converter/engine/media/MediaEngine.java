package tr.edu.deu.efm.core.impl.converter.engine.media;

import tr.edu.deu.efm.core.api.ConfirmationStrategy;
import tr.edu.deu.efm.core.api.EntityConverter;
import tr.edu.deu.efm.core.api.OperationResult;
import tr.edu.deu.efm.core.impl.converter.engine.media.impl.AudioMediaConverter;
import tr.edu.deu.efm.core.impl.converter.engine.media.impl.VideoMediaConverter;

import java.util.HashMap;
import java.util.Map;
import java.util.Collections;

public class MediaEngine implements EntityConverter {

	private final Map<String, EntityConverter> convertTools = new HashMap<>();

	public MediaEngine() {
		EntityConverter audioConverter = new AudioMediaConverter();
		EntityConverter videoConverter = new VideoMediaConverter();

		convertTools.put("mp3", audioConverter);
		convertTools.put("wav", audioConverter);
		convertTools.put("flac", audioConverter);

		convertTools.put("mp4", videoConverter);
		convertTools.put("avi", videoConverter);
		convertTools.put("mkv", videoConverter);
	}

	@Override
	public OperationResult convert(String currentDir, String sourcePath, String targetPath,
			ConfirmationStrategy strategy) {
		String targetExt = getExtension(targetPath).toLowerCase();

		EntityConverter convertTool = convertTools.get(targetExt);

		if (convertTool == null) {
			return new OperationResult(false, "unsupported target media format: " + targetExt, Collections.emptyList());
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
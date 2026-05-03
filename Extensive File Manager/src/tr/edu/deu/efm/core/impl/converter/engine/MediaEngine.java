package tr.edu.deu.efm.core.impl.converter.engine;

import tr.edu.deu.efm.core.api.EntityConverter;
import tr.edu.deu.efm.core.api.OperationResult;
import ws.schild.jave.Encoder;
import ws.schild.jave.MultimediaObject;
import ws.schild.jave.encode.AudioAttributes;
import ws.schild.jave.encode.EncodingAttributes;
import ws.schild.jave.encode.VideoAttributes;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;

/**
 * Conversion engine responsible for handling MEDIA family formats. Uses JAVE2
 * (FFmpeg wrapper) to convert video and audio files.
 */
public class MediaEngine implements EntityConverter {

	@Override
	public OperationResult convert(String currentDir, String sourcePath, String targetPath, boolean overwrite) {
		try {
			Path current = Paths.get(currentDir);
			File sourceFile = current.resolve(sourcePath).normalize().toFile();
			File targetFile = current.resolve(targetPath).normalize().toFile();

			String targetExt = getExtension(targetFile.getName()).toLowerCase();

			EncodingAttributes attrs = new EncodingAttributes();
			attrs.setOutputFormat(targetExt);

			AudioAttributes audio = new AudioAttributes();
			attrs.setAudioAttributes(audio);

			if (!targetExt.equals("mp3") && !targetExt.equals("wav")) {
				VideoAttributes video = new VideoAttributes();
				attrs.setVideoAttributes(video);
			}

			Encoder encoder = new Encoder();
			MultimediaObject multimediaObject = new MultimediaObject(sourceFile);

			encoder.encode(multimediaObject, targetFile, attrs);

			return new OperationResult(true, "Media successfully converted to '" + targetFile.getName() + "'",
					Collections.singletonList(targetFile.getAbsolutePath()));

		} catch (Exception e) {
			return new OperationResult(false, "Media conversion failed -> " + e.getMessage(), Collections.emptyList());
		}
	}

	private String getExtension(String fileName) {
		int lastDot = fileName.lastIndexOf('.');
		return (lastDot == -1) ? "" : fileName.substring(lastDot + 1);
	}
}
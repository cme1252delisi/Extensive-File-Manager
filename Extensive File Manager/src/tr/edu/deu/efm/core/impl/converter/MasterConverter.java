package tr.edu.deu.efm.core.impl.converter;

import tr.edu.deu.efm.core.api.EntityConverter;
import tr.edu.deu.efm.core.api.FormatFamily;
import tr.edu.deu.efm.core.api.OperationResult;
import tr.edu.deu.efm.core.impl.converter.engine.DocumentEngine;
import tr.edu.deu.efm.core.impl.converter.engine.ImageEngine;
import tr.edu.deu.efm.core.impl.converter.engine.MediaEngine;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;

/**
 * The Master Converter acts as a Facade and Router. It validates formats,
 * checks compatibility, and delegates the actual conversion to the appropriate
 * specific engine.
 */
public class MasterConverter implements EntityConverter {

	@Override
	public OperationResult convert(String currentDir, String sourcePath, String targetPath, boolean overwrite) {
		try {
			Path current = Paths.get(currentDir);
			Path source = current.resolve(sourcePath).normalize();
			Path target = current.resolve(targetPath).normalize();

			if (!Files.exists(source)) {
				return new OperationResult(false, "Source file does not exist: " + sourcePath, Collections.emptyList());
			}

			if (Files.isDirectory(source)) {
				return new OperationResult(false, "Cannot convert a directory. Please specify a file.",
						Collections.emptyList());
			}

			if (Files.exists(target)) {
				if (!overwrite) {
					return new OperationResult(false, "Conversion aborted. Target file already exists: '"
							+ target.getFileName() + "'. Use -f flag to overwrite.", Collections.emptyList());
				}
			}

			String srcExt = getExtension(source.getFileName().toString());
			String tgtExt = getExtension(target.getFileName().toString());

			FormatFamily srcFamily = FormatFamily.fromExtension(srcExt);
			FormatFamily tgtFamily = FormatFamily.fromExtension(tgtExt);

			if (srcFamily == FormatFamily.UNKNOWN || tgtFamily == FormatFamily.UNKNOWN) {
				return new OperationResult(false,
						"Unsupported file format. Cannot convert '" + srcExt + "' to '" + tgtExt + "'.",
						Collections.emptyList());
			}

			if (srcFamily != tgtFamily) {
				return new OperationResult(false,
						"Incompatible conversion! Cannot convert " + srcFamily + " to " + tgtFamily + ".",
						Collections.emptyList());
			}

			EntityConverter engine = getEngineForFamily(srcFamily);
			if (engine == null) {
				return new OperationResult(false, "Conversion engine for " + srcFamily + " is not implemented yet.",
						Collections.emptyList());
			}

			return engine.convert(currentDir, sourcePath, targetPath, overwrite);

		} catch (Exception e) {
			return new OperationResult(false, "Conversion routing failed -> " + e.getMessage(),
					Collections.emptyList());
		}
	}

	private String getExtension(String fileName) {
		int lastDot = fileName.lastIndexOf('.');
		return (lastDot == -1) ? "" : fileName.substring(lastDot + 1);
	}

	private EntityConverter getEngineForFamily(FormatFamily family) {
		switch (family) {
		case IMAGE:
			return new ImageEngine(); 
		case DOCUMENT:
			return new DocumentEngine(); 
		case MEDIA:
			return new MediaEngine(); 
		default:
			return null;
		}
	}
}
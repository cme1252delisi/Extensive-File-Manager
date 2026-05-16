package tr.edu.deu.efm.core.impl.converter;

import tr.edu.deu.efm.core.api.EntityConverter;
import tr.edu.deu.efm.core.api.FormatFamily;
import tr.edu.deu.efm.core.api.OperationResult;
import tr.edu.deu.efm.core.api.ConfirmationStrategy;
import tr.edu.deu.efm.core.impl.converter.engine.document.DocumentEngine;
import tr.edu.deu.efm.core.impl.converter.engine.image.ImageEngine;
import tr.edu.deu.efm.core.impl.converter.engine.media.MediaEngine;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;

public class MasterConverter implements EntityConverter {

	@Override
	public OperationResult convert(String currentDir, String sourcePath, String targetPath,
			ConfirmationStrategy strategy) {
		try {
			Path current = Paths.get(currentDir);
			Path source = current.resolve(sourcePath).normalize();
			Path target = current.resolve(targetPath).normalize();

			if (!Files.exists(source)) {
				return new OperationResult(false, "convert: source file does not exist: " + sourcePath,
						Collections.emptyList());
			}

			if (Files.isDirectory(source)) {
				return new OperationResult(false, "convert: cannot convert a directory. please specify a file.",
						Collections.emptyList());
			}

			if (Files.exists(target)) {
				boolean overwriteAllowed = strategy.askPermission(target.getFileName().toString());
				if (!overwriteAllowed) {
					return new OperationResult(false,
							"convert: conversion aborted. target file already exists: '" + target.getFileName() + "'.",
							Collections.emptyList());
				}
			}

			String srcExt = getExtension(source.getFileName().toString());
			String tgtExt = getExtension(target.getFileName().toString());

			FormatFamily srcFamily = FormatFamily.fromExtension(srcExt);
			FormatFamily tgtFamily = FormatFamily.fromExtension(tgtExt);

			if (srcFamily == FormatFamily.UNKNOWN || tgtFamily == FormatFamily.UNKNOWN) {
				return new OperationResult(false,
						"convert: unsupported file format. cannot convert '" + srcExt + "' to '" + tgtExt + "'.",
						Collections.emptyList());
			}

			if (srcFamily != tgtFamily) {
				return new OperationResult(false,
						"convert: incompatible conversion! cannot convert " + srcFamily + " to " + tgtFamily + ".",
						Collections.emptyList());
			}

			EntityConverter engine = getEngineForFamily(srcFamily);
			if (engine == null) {
				return new OperationResult(false,
						"convert: conversion engine for " + srcFamily + " is not implemented yet.",
						Collections.emptyList());
			}

			return engine.convert(currentDir, sourcePath, targetPath, strategy);

		} catch (Exception e) {
			return new OperationResult(false, "convert: conversion routing failed -> " + e.getMessage(),
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
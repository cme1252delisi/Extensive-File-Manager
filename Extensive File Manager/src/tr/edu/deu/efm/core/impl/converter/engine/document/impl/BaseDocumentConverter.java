package tr.edu.deu.efm.core.impl.converter.engine.document.impl;

import tr.edu.deu.efm.core.api.ConfirmationStrategy;
import tr.edu.deu.efm.core.api.EntityConverter;
import tr.edu.deu.efm.core.api.OperationResult;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class BaseDocumentConverter implements EntityConverter {

	static {
		Logger.getLogger("org.apache.pdfbox").setLevel(Level.SEVERE);
		Logger.getLogger("org.apache.fontbox").setLevel(Level.SEVERE);
	}

	@Override
	public OperationResult convert(String currentDir, String sourcePath, String targetPath, ConfirmationStrategy strategy) {
		try {
			Path current = Paths.get(currentDir);
			File sourceFile = current.resolve(sourcePath).normalize().toFile();
			File targetFile = current.resolve(targetPath).normalize().toFile();

			String srcExt = getExtension(sourceFile.getName()).toLowerCase();
			if (isLibreOfficeAvailable() && (srcExt.equals("docx") || srcExt.equals("pdf"))) {
				return convertWithLibreOffice(sourceFile, targetFile);
			}

			return doConvert(sourceFile, targetFile);

		} catch (Exception e) {
			return new OperationResult(false, "document conversion failed -> " + e.getMessage(),
					Collections.emptyList());
		}
	}

	protected abstract OperationResult doConvert(File source, File target) throws Exception;

	protected String sanitizeForPDFBox(String text) {
		if (text == null)
			return "";
		return text.replace("ı", "i").replace("İ", "I").replace("ş", "s").replace("Ş", "S").replace("ğ", "g")
				.replace("Ğ", "G").replace("ç", "c").replace("Ç", "C").replace("ö", "o").replace("Ö", "O")
				.replace("ü", "u").replace("Ü", "U");
	}

	protected List<String> wrapText(String text, int maxCharsPerLine) {
		List<String> result = new java.util.ArrayList<>();
		String[] words = text.split(" ");
		StringBuilder currentLine = new StringBuilder();

		for (String word : words) {
			if (currentLine.length() + word.length() > maxCharsPerLine) {
				result.add(currentLine.toString());
				currentLine = new StringBuilder();
			}
			currentLine.append(word).append(" ");
		}
		if (currentLine.length() > 0)
			result.add(currentLine.toString());
		return result;
	}

	private String getExtension(String fileName) {
		int lastDot = fileName.lastIndexOf('.');
		return (lastDot == -1) ? "" : fileName.substring(lastDot + 1);
	}

	private boolean isLibreOfficeAvailable() {
		try {
			String checkCommand = System.getProperty("os.name").toLowerCase().contains("win") ? "where" : "which";
			ProcessBuilder pb = new ProcessBuilder(checkCommand, "soffice");
			return pb.start().waitFor() == 0;
		} catch (Exception e) {
			return false;
		}
	}

	private OperationResult convertWithLibreOffice(File source, File target) {
		try {
			String targetExt = getExtension(target.getName());
			ProcessBuilder pb = new ProcessBuilder("soffice", "--headless", "--convert-to", targetExt, "--outdir",
					target.getParent(), source.getAbsolutePath());
			if (pb.start().waitFor() == 0) {
				return new OperationResult(true, "professional conversion complete via LibreOffice.",
						Collections.singletonList(target.getAbsolutePath()));
			}
		} catch (Exception e) {
			return new OperationResult(false, "LibreOffice failed, falling back to Java engine.",
					Collections.emptyList());
		}
		return new OperationResult(false, "LibreOffice conversion failed.", Collections.emptyList());
	}
}
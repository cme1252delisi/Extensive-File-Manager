package tr.edu.deu.efm.core.impl.converter.engine.document;

import tr.edu.deu.efm.core.api.ConfirmationStrategy;
import tr.edu.deu.efm.core.api.EntityConverter;
import tr.edu.deu.efm.core.api.OperationResult;
import tr.edu.deu.efm.core.impl.converter.engine.document.impl.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Collections;

public class DocumentEngine implements EntityConverter {

	private final Map<String, EntityConverter> convertTools = new HashMap<>();

	public DocumentEngine() {
		convertTools.put("txt_pdf", new TxtToPdfConverter());
		convertTools.put("txt_docx", new TxtToDocxConverter());
		convertTools.put("pdf_docx", new PdfToDocxConverter());
		convertTools.put("pdf_txt", new PdfToTxtConverter());
		convertTools.put("docx_pdf", new DocxToPdfConverter());
		convertTools.put("docx_txt", new DocxToTxtConverter());
	}

	@Override
	public OperationResult convert(String currentDir, String sourcePath, String targetPath, ConfirmationStrategy strategy) {
		String sourceExt = getExtension(sourcePath);
		String targetExt = getExtension(targetPath);

		String routeKey = sourceExt + "_" + targetExt;

		EntityConverter convertTool = convertTools.get(routeKey);

		if (convertTool == null) {
			return new OperationResult(false, "unsupported document conversion: " + sourceExt + " to " + targetExt,
					Collections.emptyList());
		}

		return convertTool.convert(currentDir, sourcePath, targetPath, strategy);
	}

	private String getExtension(String path) {
		int lastDotIndex = path.lastIndexOf('.');
		if (lastDotIndex > 0 && lastDotIndex < path.length() - 1) {
			return path.substring(lastDotIndex + 1).toLowerCase();
		}
		return "";
	}
}
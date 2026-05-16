package tr.edu.deu.efm.core.impl.converter.engine.document.impl;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Collections;

import org.apache.pdfbox.pdmodel.PDDocument;

import tr.edu.deu.efm.core.api.OperationResult;

public class PdfToTxtConverter extends BaseDocumentConverter {
	protected OperationResult doConvert(File source, File target) throws Exception {
		try (PDDocument document = PDDocument.load(source)) {
			org.apache.pdfbox.text.PDFTextStripper stripper = new org.apache.pdfbox.text.PDFTextStripper();
			stripper.setSortByPosition(true);

			String text = stripper.getText(document);
			Files.write(target.toPath(), text.getBytes(StandardCharsets.UTF_8));
		}
		return new OperationResult(true, "successfully extracted sorted PDF text to: '" + target.getName() + "'",
				Collections.singletonList(target.getAbsolutePath()));
	}

}

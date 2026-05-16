package tr.edu.deu.efm.core.impl.converter.engine.document.impl;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Collections;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import tr.edu.deu.efm.core.api.OperationResult;

public class TxtToPdfConverter extends BaseDocumentConverter {

	protected OperationResult doConvert(File source, File target) throws Exception {
		List<String> lines = Files.readAllLines(source.toPath(), StandardCharsets.UTF_8);

		try (PDDocument pdfDoc = new PDDocument()) {
			PDPage page = new PDPage();
			pdfDoc.addPage(page);

			try (PDPageContentStream contentStream = new PDPageContentStream(pdfDoc, page)) {
				contentStream.setFont(PDType1Font.HELVETICA, 12);
				contentStream.beginText();
				contentStream.setLeading(14.5f);
				contentStream.newLineAtOffset(25, 700);

				for (String line : lines) {
					String safeLine = sanitizeForPDFBox(line);
					contentStream.showText(safeLine);
					contentStream.newLine();
				}
				contentStream.endText();
			}
			pdfDoc.save(target);
		}
		return new OperationResult(true, "successfully converted TXT to PDF: '" + target.getName() + "'",
				Collections.singletonList(target.getAbsolutePath()));
	}

}

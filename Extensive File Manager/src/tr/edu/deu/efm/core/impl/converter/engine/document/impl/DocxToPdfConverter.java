package tr.edu.deu.efm.core.impl.converter.engine.document.impl;

import java.io.File;
import java.io.FileInputStream;
import java.util.Collections;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import tr.edu.deu.efm.core.api.OperationResult;

public class DocxToPdfConverter extends BaseDocumentConverter {

	public OperationResult doConvert(File source, File target) throws Exception {
		try (FileInputStream fis = new FileInputStream(source);
				XWPFDocument docx = new XWPFDocument(fis);
				PDDocument pdf = new PDDocument();
				org.apache.poi.xwpf.extractor.XWPFWordExtractor extractor = new org.apache.poi.xwpf.extractor.XWPFWordExtractor(
						docx)) {

			String[] paragraphs = extractor.getText().split("\\r?\\n");

			PDPage page = new PDPage();
			pdf.addPage(page);
			PDPageContentStream contentStream = new PDPageContentStream(pdf, page);

			contentStream.setFont(PDType1Font.HELVETICA, 12);
			contentStream.beginText();
			contentStream.setLeading(14.5f);

			float startY = 700;
			contentStream.newLineAtOffset(25, startY);

			for (String para : paragraphs) {
				if (para.trim().isEmpty())
					continue;

				List<String> wrappedLines = wrapText(sanitizeForPDFBox(para), 90);

				for (String line : wrappedLines) {
					if (startY < 50) {
						contentStream.endText();
						contentStream.close();

						page = new PDPage();
						pdf.addPage(page);
						contentStream = new PDPageContentStream(pdf, page);
						contentStream.setFont(PDType1Font.HELVETICA, 12);
						contentStream.beginText();
						contentStream.setLeading(14.5f);
						startY = 700;
						contentStream.newLineAtOffset(25, startY);
					}
					contentStream.showText(line);
					contentStream.newLine();
					startY -= 14.5f;
				}
				contentStream.newLine();
				startY -= 14.5f;
			}
			contentStream.endText();
			contentStream.close();
			pdf.save(target);
		}
		return new OperationResult(true, "successfully converted DOCX to PDF: '" + target.getName() + "'",
				Collections.singletonList(target.getAbsolutePath()));
	}

}

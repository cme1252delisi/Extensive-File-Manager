package tr.edu.deu.efm.core.impl.converter.engine.document.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Collections;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

import tr.edu.deu.efm.core.api.OperationResult;

public class PdfToDocxConverter extends BaseDocumentConverter {
	protected OperationResult doConvert(File source, File target) throws Exception {
		try (PDDocument document = PDDocument.load(source)) {
			org.apache.pdfbox.text.PDFTextStripper stripper = new org.apache.pdfbox.text.PDFTextStripper();
			stripper.setSortByPosition(true);

			String text = stripper.getText(document);

			try (XWPFDocument docx = new XWPFDocument(); FileOutputStream out = new FileOutputStream(target)) {

				String[] lines = text.split("\\r?\\n");
				for (String line : lines) {
					if (line.trim().isEmpty())
						continue;
					XWPFParagraph paragraph = docx.createParagraph();
					XWPFRun run = paragraph.createRun();
					run.setText(line);
				}
				docx.write(out);
			}
		}
		return new OperationResult(true, "successfully converted PDF to DOCX: '" + target.getName() + "'",
				Collections.singletonList(target.getAbsolutePath()));
	}
}

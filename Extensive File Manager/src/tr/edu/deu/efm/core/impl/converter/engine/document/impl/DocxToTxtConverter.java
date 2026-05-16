package tr.edu.deu.efm.core.impl.converter.engine.document.impl;

import java.io.File;
import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Collections;

import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import tr.edu.deu.efm.core.api.OperationResult;

public class DocxToTxtConverter extends BaseDocumentConverter {

	protected OperationResult doConvert(File source, File target) throws Exception {
		try (FileInputStream fis = new FileInputStream(source);
				XWPFDocument docx = new XWPFDocument(fis);
				XWPFWordExtractor extractor = new XWPFWordExtractor(docx)) {

			String text = extractor.getText();
			Files.write(target.toPath(), text.getBytes(StandardCharsets.UTF_8));
		}
		return new OperationResult(true, "successfully extracted DOCX text to: '" + target.getName() + "'",
				Collections.singletonList(target.getAbsolutePath()));
	}

}

package tr.edu.deu.efm.core.impl.converter.engine.document.impl;

import tr.edu.deu.efm.core.api.OperationResult;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Collections;
import java.util.List;

public class TxtToDocxConverter extends BaseDocumentConverter {

	protected OperationResult doConvert(File source, File target) throws Exception {
		List<String> lines = Files.readAllLines(source.toPath(), StandardCharsets.UTF_8);

		try (XWPFDocument docx = new XWPFDocument(); FileOutputStream out = new FileOutputStream(target)) {

			for (String line : lines) {
				XWPFParagraph paragraph = docx.createParagraph();
				XWPFRun run = paragraph.createRun();
				run.setText(line);
			}
			docx.write(out);
		}
		return new OperationResult(true, "successfully converted TXT to DOCX: '" + target.getName() + "'",
				Collections.singletonList(target.getAbsolutePath()));
	}

}
package tr.edu.deu.efm.core.impl.converter.engine;

import tr.edu.deu.efm.core.api.EntityConverter;
import tr.edu.deu.efm.core.api.OperationResult;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Conversion engine responsible for handling DOCUMENT family formats. Uses
 * Apache PDFBox for PDFs and Apache POI for DOCX files.
 */
public class DocumentEngine implements EntityConverter {

	static {
		Logger.getLogger("org.apache.pdfbox").setLevel(Level.SEVERE);
		Logger.getLogger("org.apache.fontbox").setLevel(Level.SEVERE);
	}

	@Override
	public OperationResult convert(String currentDir, String sourcePath, String targetPath, boolean overwrite) {
		try {
			Path current = Paths.get(currentDir);
			File sourceFile = current.resolve(sourcePath).normalize().toFile();
			File targetFile = current.resolve(targetPath).normalize().toFile();

			String srcExt = getExtension(sourceFile.getName()).toLowerCase();
			String tgtExt = getExtension(targetFile.getName()).toLowerCase();

			if (isLibreOfficeAvailable() && (srcExt.equals("docx") || srcExt.equals("pdf"))) {
				return convertWithLibreOffice(sourceFile, targetFile);
			}

			if (srcExt.equals("txt") && tgtExt.equals("pdf")) {
				return txtToPdf(sourceFile, targetFile);
			} else if (srcExt.equals("txt") && tgtExt.equals("docx")) {
				return txtToDocx(sourceFile, targetFile);
			} else if (srcExt.equals("docx") && tgtExt.equals("txt")) {
				return docxToTxt(sourceFile, targetFile);
			} else if (srcExt.equals("docx") && tgtExt.equals("pdf")) {
				return docxToPdf(sourceFile, targetFile); // YENİ EKLENDİ!
			} else if (srcExt.equals("pdf") && tgtExt.equals("txt")) {
				return pdfToTxt(sourceFile, targetFile);
			} else if (srcExt.equals("pdf") && tgtExt.equals("docx")) {
				return pdfToDocx(sourceFile, targetFile);
			} else {
				return new OperationResult(false, "Document conversion from '" + srcExt + "' to '" + tgtExt
						+ "' is not supported by this engine.", Collections.emptyList());
			}

		} catch (Exception e) {
			return new OperationResult(false, "Document conversion failed -> " + e.getMessage(),
					Collections.emptyList());
		}
	}
	    
	private boolean isLibreOfficeAvailable() {
	    try {
	        String checkCommand = System.getProperty("os.name").toLowerCase().contains("win") ? "where" : "which";
	        
	        ProcessBuilder pb = new ProcessBuilder(checkCommand, "soffice");
	        Process p = pb.start();
	        
	        return p.waitFor() == 0;
	    } catch (Exception e) {
	        return false;
	    }
	}

    private OperationResult convertWithLibreOffice(File source, File target) {
        try {
            String targetExt = getExtension(target.getName());
            ProcessBuilder pb = new ProcessBuilder(
                "soffice", "--headless", "--convert-to", targetExt, 
                "--outdir", target.getParent(), source.getAbsolutePath()
            );
            
            Process p = pb.start();
            if (p.waitFor() == 0) {
                return new OperationResult(true, "Professional conversion complete via LibreOffice.", Collections.singletonList(target.getAbsolutePath()));
            }
        } catch (Exception e) {
            return new OperationResult(false, "LibreOffice failed, falling back to Java engine.", Collections.emptyList());
        }
        return new OperationResult(false, "LibreOffice conversion failed.", Collections.emptyList());
    }

	private OperationResult txtToPdf(File source, File target) throws Exception {
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
		return new OperationResult(true, "Successfully converted TXT to PDF: '" + target.getName() + "'",
				Collections.singletonList(target.getAbsolutePath()));
	}

	private OperationResult txtToDocx(File source, File target) throws Exception {
		List<String> lines = Files.readAllLines(source.toPath(), StandardCharsets.UTF_8);

		try (XWPFDocument docx = new XWPFDocument(); FileOutputStream out = new FileOutputStream(target)) {

			for (String line : lines) {
				XWPFParagraph paragraph = docx.createParagraph();
				XWPFRun run = paragraph.createRun();
				run.setText(line);
			}
			docx.write(out);
		}
		return new OperationResult(true, "Successfully converted TXT to DOCX: '" + target.getName() + "'",
				Collections.singletonList(target.getAbsolutePath()));
	}

	private OperationResult docxToTxt(File source, File target) throws Exception {
		try (FileInputStream fis = new FileInputStream(source);
				XWPFDocument docx = new XWPFDocument(fis);
				XWPFWordExtractor extractor = new XWPFWordExtractor(docx)) {

			String text = extractor.getText();
			Files.write(target.toPath(), text.getBytes(StandardCharsets.UTF_8));
		}
		return new OperationResult(true, "Successfully extracted DOCX text to: '" + target.getName() + "'",
				Collections.singletonList(target.getAbsolutePath()));
	}

	private String getExtension(String fileName) {
		int lastDot = fileName.lastIndexOf('.');
		return (lastDot == -1) ? "" : fileName.substring(lastDot + 1);
	}

	private String sanitizeForPDFBox(String text) {
		if (text == null)
			return "";
		return text.replace("ı", "i").replace("İ", "I").replace("ş", "s").replace("Ş", "S").replace("ğ", "g")
				.replace("Ğ", "G").replace("ç", "c").replace("Ç", "C").replace("ö", "o").replace("Ö", "O")
				.replace("ü", "u").replace("Ü", "U");
	}

	private OperationResult pdfToTxt(File source, File target) throws Exception {
		try (PDDocument document = PDDocument.load(source)) {
			org.apache.pdfbox.text.PDFTextStripper stripper = new org.apache.pdfbox.text.PDFTextStripper();
			stripper.setSortByPosition(true);

			String text = stripper.getText(document);
			Files.write(target.toPath(), text.getBytes(StandardCharsets.UTF_8));
		}
		return new OperationResult(true, "Successfully extracted sorted PDF text to: '" + target.getName() + "'",
				Collections.singletonList(target.getAbsolutePath()));
	}

	private OperationResult pdfToDocx(File source, File target) throws Exception {
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
		return new OperationResult(true, "Successfully converted PDF to DOCX: '" + target.getName() + "'",
				Collections.singletonList(target.getAbsolutePath()));
	}

	private OperationResult docxToPdf(File source, File target) throws Exception {
		try (FileInputStream fis = new FileInputStream(source);
				XWPFDocument docx = new XWPFDocument(fis);
				PDDocument pdf = new PDDocument()) {

			org.apache.poi.xwpf.extractor.XWPFWordExtractor extractor = new org.apache.poi.xwpf.extractor.XWPFWordExtractor(
					docx);
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
		return new OperationResult(true, "Successfully converted DOCX to PDF: '" + target.getName() + "'",
				Collections.singletonList(target.getAbsolutePath()));
	}

	private List<String> wrapText(String text, int maxCharsPerLine) {
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
		if (currentLine.length() > 0) {
			result.add(currentLine.toString());
		}
		return result;
	}

}
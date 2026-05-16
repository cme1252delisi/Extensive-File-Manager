package tr.edu.deu.efm.core.api;

public interface EntityConverter {
	OperationResult convert(String currentDir, String sourcePath, String targetPath, ConfirmationStrategy strategy);
}
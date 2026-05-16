package tr.edu.deu.efm.core.api;

public interface EntityDecryptor {
	OperationResult decrypt(String currentDir, String targetPath, char[] password, ConfirmationStrategy strategy);
}
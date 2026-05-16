package tr.edu.deu.efm.core.api;

public interface EntityEncryptor {
	OperationResult encrypt(String currentDir, String targetPath, char[] password, ConfirmationStrategy strategy);
}
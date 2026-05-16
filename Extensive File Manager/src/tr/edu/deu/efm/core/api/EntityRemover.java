package tr.edu.deu.efm.core.api;

public interface EntityRemover {
	OperationResult remove(String currentDir, String targetPath, boolean recursive, boolean permanent,
			ConfirmationStrategy strategy);
}
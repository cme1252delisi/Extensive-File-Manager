package tr.edu.deu.efm.core.api;

public interface EntityRenamer {
	OperationResult rename(String currentDir, String entityPath, String newName, ConfirmationStrategy strategy);
}
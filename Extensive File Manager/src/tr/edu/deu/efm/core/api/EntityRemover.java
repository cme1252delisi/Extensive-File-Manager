package tr.edu.deu.efm.core.api;

public interface EntityRemover {
	OperationResult remove(String path, boolean recursive, boolean verbose);
}

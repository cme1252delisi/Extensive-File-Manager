package tr.edu.deu.efm.core.api;

import tr.edu.deu.efm.core.dto.OperationResult;

public interface EntityRemover {
	OperationResult remove(String path, boolean recursive, boolean verbose);
}

package tr.edu.deu.efm.core.api;

import tr.edu.deu.efm.core.dto.OperationResult;

public interface EntityMover {
	OperationResult move(String path, String source, String destination);
}

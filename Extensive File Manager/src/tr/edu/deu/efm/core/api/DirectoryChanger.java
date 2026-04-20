package tr.edu.deu.efm.core.api;

import tr.edu.deu.efm.core.dto.OperationResult;

public interface DirectoryChanger {
	OperationResult changeDirectory(String path);
}

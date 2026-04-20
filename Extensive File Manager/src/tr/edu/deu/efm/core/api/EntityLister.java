package tr.edu.deu.efm.core.api;

import tr.edu.deu.efm.core.dto.OperationResult;

public interface EntityLister {
	OperationResult list(String path);

}

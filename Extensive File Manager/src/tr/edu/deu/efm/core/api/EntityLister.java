package tr.edu.deu.efm.core.api;

/**
 * Core interface for listing operations. High-level API that remains agnostic
 * of underlying I/O libraries.
 */
public interface EntityLister {
	/**
	 * Lists directory entities.
	 * 
	 * @param currentDirectory Current working directory of session.
	 * @param entityPath       The absolute path as a String.
	 * @param showHidden       Flag for -a
	 * @param detailed         Flag for -l
	 * @return OperationResult containing the list of entity names/details.
	 */
	OperationResult list(String currentDirectory, String entityPath, boolean showHidden, boolean detailed);
}
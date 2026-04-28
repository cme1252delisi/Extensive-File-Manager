package tr.edu.deu.efm.core.api;

import java.util.List;

/**
 * A Data Transfer Object (DTO) that encapsulates the outcome of core file
 * system operations.
 * <p>
 * This class acts as a secure communication bridge between the low-level
 * Infrastructure/Core layer and the high-level Command layer. By returning an
 * {@code OperationResult}, the core implementations (like copiers, movers, or
 * removers) can communicate success states and pass data without exposing
 * underlying I/O specific classes (e.g., {@code java.nio.file.Path}) or
 * throwing checked exceptions.
 * </p>
 */
public class OperationResult {
	private final boolean success;
	private final String message;
	private final List<String> affectedItems;

	/**
	 * Constructs a new OperationResult.
	 *
	 * @param success       {@code true} if the core operation completed without
	 *                      errors; {@code false} otherwise.
	 * @param message       An informational message about result of operation.
	 * @param affectedItems A dynamic list containing the payload of the operation.
	 *                      If the operation is successful, this typically contains
	 *                      the absolute paths or names of the manipulated entities.
	 */
	public OperationResult(boolean success, String message, List<String> affectedItems) {
		this.success = success;
		this.message = message;
		this.affectedItems = affectedItems;
	}

	/**
	 * Checks whether the underlying I/O operation was successful.
	 *
	 * @return {@code true} if successful, {@code false} if an error or conflict
	 *         occurred.
	 */
	public boolean isSuccess() {
		return success;
	}

	/**
	 * Retrieves an informational error or success message about operation result.
	 * 
	 * @return A String message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * Retrieves the list of items affected by the operation.
	 *
	 * @return A list of strings representing entities.
	 */
	public List<String> getAffectedItems() {
		return affectedItems;
	}

}
package tr.edu.deu.efm.core.exception;

public class EntityNotFoundException extends RuntimeException {
	public EntityNotFoundException(String message) {
        super(message);
    }
}
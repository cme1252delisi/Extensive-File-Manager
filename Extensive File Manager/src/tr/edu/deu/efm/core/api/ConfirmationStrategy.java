package tr.edu.deu.efm.core.api;

public interface ConfirmationStrategy {
    boolean askPermission(String message);
}
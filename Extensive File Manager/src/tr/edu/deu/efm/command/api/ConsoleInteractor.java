package tr.edu.deu.efm.command.api;

public interface ConsoleInteractor {

	char[] readPassword(String prompt);

	boolean askConfirmation(String prompt);

	void printMessage(String message);
}
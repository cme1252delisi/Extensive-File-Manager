package tr.edu.deu.efm.ui.impl;

import tr.edu.deu.efm.command.api.ConsoleInteractor;
import java.io.Console;
import java.util.Scanner;

public class StandardConsoleAdapter implements ConsoleInteractor {

	private final Scanner scanner;
	private final Console console;

	public StandardConsoleAdapter() {
		this.console = System.console();
		this.scanner = new Scanner(System.in);
	}

	@Override
	public char[] readPassword(String message) {
		if (console != null) {
			return console.readPassword(message);
		} else {
			System.out.print(message + " (WARNING: Password will be visible): ");
			return scanner.nextLine().toCharArray();
		}
	}

	@Override
	public boolean askConfirmation(String prompt) {
		System.out.print(prompt + " [y/N]: ");

		String answer;
		if (console != null) {
			answer = console.readLine();
		} else {
			answer = scanner.nextLine();
		}

		if (answer == null || answer.trim().isEmpty()) {
			return false;
		}

		answer = answer.trim().toLowerCase();
		return answer.equals("y") || answer.equals("yes");
	}

	@Override
	public void printMessage(String message) {
		System.out.println(message);
	}
}
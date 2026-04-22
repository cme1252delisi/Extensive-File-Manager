package tr.edu.deu.efm.util;

import java.util.Scanner;

public class ConsoleUtils {

	/**
	 * Don't let anyone instantiate this class.
	 */
	private ConsoleUtils() {

	}

	/**
	 * A command line dialog to interact with user
	 * 
	 * @param The message to be printed on the screen
	 * @return Returns true if user enters "Y", returns false if user enters "N".
	 */
	public static boolean yesNoDialog(String message, Scanner scr) {
		System.out.print(message);

		while (true) {
			String line = scr.nextLine().trim();

			if (line.equalsIgnoreCase("N")) {
				return false;
			} else if (line.equalsIgnoreCase("Y")) {
				return true;
			}
			System.out.print("Please enter 'Y' or 'N': ");
		}
	}

}

package tr.edu.deu.efm.ui;

import java.util.Scanner;
import tr.edu.deu.efm.command.api.Command;
import tr.edu.deu.efm.command.api.CommandContext;
import tr.edu.deu.efm.command.api.CommandRegistry;
import tr.edu.deu.efm.command.api.CommandResult;
import tr.edu.deu.efm.command.api.Session;
import tr.edu.deu.efm.command.api.CommandParser;
import tr.edu.deu.efm.command.impl.DefaultCommandParser;
import tr.edu.deu.efm.command.impl.DefaultCommandRegistry;
import tr.edu.deu.efm.config.RegistryConfig;

public class CommandLineInterface {

	private static Scanner scr = new Scanner(System.in); // global scanner
	private Session session = new Session();
	private CommandParser commandParser = new DefaultCommandParser();
	private CommandRegistry commandRegistry = new DefaultCommandRegistry();

	public CommandLineInterface() {
		commandRegistry = RegistryConfig.initializeCommandRegistry();
		commandLine();
	}

	private void commandLine() {
		System.out.println("Welcome to Extensive File Manager\n");
		String manual = "Type 'help' to see available commands.";
		System.out.println(manual);

		while (true) {
			CommandContext context = null;

			do {
				System.out.print(session.getCurrentWorkingDirectory() + ">>");
				String userInput = scr.nextLine();

				try {
					context = commandParser.parse(userInput);
					
                    if (context != null) {
                        context.setSession(session);
                    }

				} catch (IllegalArgumentException e) {
				}
			} while (context == null);

			CommandResult result = executeUserCommand(context);
			printCommandResult(result);

			if (result.isTerminateProgram()) {
				break;
			}
		}
	}

	private void printCommandResult(CommandResult result) {
		System.out.println(result.getMessage());
	}

	private CommandResult executeUserCommand(CommandContext context) {
		Command command = commandRegistry.getCommand(context.getCommandName());

		if (command != null) {
			return command.execute(context);
		} else {
			return new CommandResult(false, "Command not found: " + context.getCommandName());
		}
	}

}

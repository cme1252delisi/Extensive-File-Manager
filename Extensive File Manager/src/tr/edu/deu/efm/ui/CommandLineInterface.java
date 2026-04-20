package tr.edu.deu.efm.ui;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import tr.edu.deu.efm.command.*;
import tr.edu.deu.efm.command.api.Command;
import tr.edu.deu.efm.command.api.CommandContext;
import tr.edu.deu.efm.command.api.CommandResult;
import tr.edu.deu.efm.command.impl.CdCommand;
import tr.edu.deu.efm.command.impl.CpCommand;
import tr.edu.deu.efm.command.impl.ExitCommand;
import tr.edu.deu.efm.command.impl.HelpCommand;
import tr.edu.deu.efm.command.impl.LsCommand;
import tr.edu.deu.efm.command.impl.MvCommand;
import tr.edu.deu.efm.command.impl.RmCommand;

public class CommandLineInterface {

	private static Scanner scr = new Scanner(System.in);
	private Map<String, Command> commandRegistry = new HashMap<>();

	public CommandLineInterface() {
		commandLine();
	}

	public void commandLine() {

		System.out.println("Welcome to Extensive File Manager\n");
		String manual = "Type 'help' to see available commands.";		
		System.out.println(manual);

		while (true) {

			CommandContext context = new CommandContext();
			boolean valid = false;

			do {
				System.out.print(">>");
				String userInput = scr.nextLine();
				valid = CommandParser.parse(userInput, context);
			} while (!valid);
			
			CommandResult result = executeUserCommand(context);
			printCommandResult(result);
			
			if(result.isTerminateProgram()) {
				break;
			}
		}
	}
	
	private void printCommandResult(CommandResult result) {
		System.out.println(result.getMessage());
	}

	private CommandResult executeUserCommand(CommandContext context) {
		Command command = commandRegistry.get(context.getCommandName());

		if (command != null) {
			return command.execute(context);
		} else {
			return new CommandResult (false, "Command not found: " + context.getCommandName());
		}
	}

}

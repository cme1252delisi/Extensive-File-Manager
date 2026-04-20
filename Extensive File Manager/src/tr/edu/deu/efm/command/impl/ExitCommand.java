package tr.edu.deu.efm.command.impl;

import tr.edu.deu.efm.command.api.BaseCommand;
import tr.edu.deu.efm.command.api.CommandContext;
import tr.edu.deu.efm.command.api.CommandResult;

public class ExitCommand extends BaseCommand {

	public ExitCommand() {
		super("exit", "Exits the Extensive File Manager safely.", "Usage: exit");
	}

	@Override
	public CommandResult execute(CommandContext context) {
		boolean success = true;
		boolean terminateProgram = true;
		
		return new CommandResult(success, "Exiting Program...", terminateProgram);
	}

}

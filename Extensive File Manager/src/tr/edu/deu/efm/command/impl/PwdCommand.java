package tr.edu.deu.efm.command.impl;

import tr.edu.deu.efm.command.api.CommandContext;
import tr.edu.deu.efm.command.api.CommandResult;
import tr.edu.deu.efm.command.api.EfmSession;

public class PwdCommand extends BaseCommand {

	public PwdCommand() {
		super("pwd", "Prints current working directory.", "Usage: pwd");
	}

	@Override
	public CommandResult execute(CommandContext context) {
		
		EfmSession efmSession = context.getEfmSession();
		boolean success = true;
		
		return new CommandResult(success, efmSession.getCurrentWorkingDirectory());
	}

}

package tr.edu.deu.efm.command.impl;

import tr.edu.deu.efm.command.api.CommandContext;
import tr.edu.deu.efm.command.api.CommandResult;
import tr.edu.deu.efm.config.Settings;

public class ClearLogCommand extends BaseCommand {

	public ClearLogCommand() {
		super("clearlog", "Clears the application operation logs.", "Usage: clearlog");
	}

	@Override
	public CommandResult execute(CommandContext context) {
		logTransaction("INFO", "clearlog: log history has been successfully cleared.", true);

		boolean verbose = context.getFlags().hasFlag('v') || Settings.verboseAsDefault;
		String output = verbose ? "clearlog: log history has been successfully cleared." : "";

		return new CommandResult(true, output);
	}
}
package tr.edu.deu.efm.command.impl;

import tr.edu.deu.efm.command.api.CommandContext;
import tr.edu.deu.efm.command.api.CommandResult;
import tr.edu.deu.efm.config.Settings;

/**
 * Command implementation for printing the current working directory.
 * <p>
 * This command retrieves the simulated session directory. It supports the
 * verbose flag: by default, it prints only the raw absolute path. In verbose
 * mode, it provides a more descriptive output.
 * </p>
 */
public class PwdCommand extends BaseCommand {

	public PwdCommand() {
		super("pwd", "Prints the absolute path of the current working directory.", "Usage: pwd [-v]");
	}

	@Override
	public CommandResult execute(CommandContext context) {
		String currentDir = context.getSession().getCurrentWorkingDirectory();
		boolean verbose = context.getFlags().hasFlag('v');

		String output = (verbose || Settings.verboseAsDefault) ? "Current working directory: " + currentDir
				: currentDir;

		return new CommandResult(true, output);
	}
}
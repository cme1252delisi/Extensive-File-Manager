package tr.edu.deu.efm.command.impl;

import tr.edu.deu.efm.command.api.CommandContext;
import tr.edu.deu.efm.command.api.CommandResult;
import tr.edu.deu.efm.config.Settings;

/**
 * Command implementation for terminating the EFM shell safely.
 * <p>
 * This command supports the verbose flag. By default, it exits silently like
 * standard UNIX shells. If verbose mode is active, it prints a farewell message
 * before termination.
 * </p>
 */
public class ExitCommand extends BaseCommand {

	public ExitCommand() {
		super("exit", "Exits the Extensive File Manager safely.", "Usage: exit [-v]");
	}

	@Override
	public CommandResult execute(CommandContext context) {
		boolean verbose = context.getFlags().hasFlag('v');

		String output = (verbose || Settings.verboseAsDefault) ? "Exiting Extensive File Manager. Goodbye!" : "";

		return new CommandResult(true, output, true);
	}
}
package tr.edu.deu.efm.command.impl;

import java.util.List;

import tr.edu.deu.efm.command.api.CommandContext;
import tr.edu.deu.efm.command.api.CommandResult;
import tr.edu.deu.efm.config.Settings;
import tr.edu.deu.efm.core.api.EntityOpener;
import tr.edu.deu.efm.core.api.OperationResult;

/**
 * Command implementation for opening files via native OS applications.
 */
public class OpenCommand extends BaseCommand {

	private EntityOpener opener;

	public OpenCommand(EntityOpener opener) {
		super("open", "Opens a file using the system's default application.", "Usage: open [-v] <file_name>");
		this.opener = opener;
	}

	@Override
	public CommandResult execute(CommandContext context) {
		List<String> args = context.getArguments();

		if (args.isEmpty()) {
			return new CommandResult(false, "open: missing file operand");
		}

		String targetPath = args.get(0);
		String currentDir = context.getSession().getCurrentWorkingDirectory();
		boolean verbose = context.getFlags().hasFlag('v');

		OperationResult result = opener.open(currentDir, targetPath);

		if (!result.isSuccess()) {
			return new CommandResult(false, "open: " + result.getMessage());
		}

		String output = (verbose || Settings.verboseAsDefault) ? "open: " + result.getMessage() : "";
		return new CommandResult(true, output);
	}
}
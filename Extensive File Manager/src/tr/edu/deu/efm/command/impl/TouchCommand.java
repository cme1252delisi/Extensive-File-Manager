package tr.edu.deu.efm.command.impl;

import java.util.List;

import tr.edu.deu.efm.command.api.CommandContext;
import tr.edu.deu.efm.command.api.CommandResult;
import tr.edu.deu.efm.config.Settings;
import tr.edu.deu.efm.core.api.EntityCreator;
import tr.edu.deu.efm.core.api.OperationResult;

/**
 * Command implementation for creating empty files or updating access
 * timestamps.
 */
public class TouchCommand extends BaseCommand {

	private EntityCreator creator;

	public TouchCommand(EntityCreator creator) {
		super("touch", "Creates an empty file or updates the timestamp of an existing file.",
				"Usage: touch [-v] <file_name>");
		this.creator = creator;
	}

	@Override
	public CommandResult execute(CommandContext context) {
		List<String> args = context.getArguments();

		if (args.isEmpty()) {
			return new CommandResult(false, "touch: missing file operand");
		}

		String targetPath = args.get(0);
		String currentDir = context.getSession().getCurrentWorkingDirectory();
		boolean verbose = context.getFlags().hasFlag('v');

		OperationResult result = creator.create(currentDir, targetPath);

		if (!result.isSuccess()) {
			return new CommandResult(false, result.getMessage());
		}

		String output = (verbose || Settings.verboseAsDefault) ? result.getMessage() : "";
		return new CommandResult(true, output);
	}
}
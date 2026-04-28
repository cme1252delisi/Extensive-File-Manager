package tr.edu.deu.efm.command.impl;

import java.util.List;

import tr.edu.deu.efm.command.api.CommandContext;
import tr.edu.deu.efm.command.api.CommandFlags;
import tr.edu.deu.efm.command.api.CommandResult;
import tr.edu.deu.efm.config.Settings;
import tr.edu.deu.efm.core.api.EntityRemover;
import tr.edu.deu.efm.core.api.OperationResult;

/**
 * Command implementation for removing files or directories.
 * <p>
 * This command interfaces with the {@link EntityRemover} core API. It supports
 * recursive deletion flags and gracefully handles errors (like attempting to
 * delete a directory without the recursive flag) via the structured Result
 * Pattern.
 * </p>
 */
public class RmCommand extends BaseCommand {

	private EntityRemover remover;

	public RmCommand(EntityRemover remover) {
		super("rm", "Removes files or directories.", "Usage: rm [-r] [-v] <path>");
		this.remover = remover;
	}

	@Override
	public CommandResult execute(CommandContext context) {
		List<String> args = context.getArguments();
		CommandFlags flags = context.getFlags();

		if (args.isEmpty()) {
			return new CommandResult(false, "rm: missing operand");
		}

		String targetPath = args.get(0);
		String currentDir = context.getSession().getCurrentWorkingDirectory();
		boolean recursive = flags.hasFlag('r');
		boolean verbose = flags.hasFlag('v');

		OperationResult result = remover.remove(currentDir, targetPath, recursive);

		if (!result.isSuccess()) {
			return new CommandResult(false, result.getMessage());
		}

		StringBuilder output = new StringBuilder();
		if (verbose || Settings.verboseAsDefault) {
			for (String item : result.getAffectedItems()) {
				output.append("removed '").append(item).append("'\n");
			}
		}

		return new CommandResult(true, output.toString().trim());
	}
}
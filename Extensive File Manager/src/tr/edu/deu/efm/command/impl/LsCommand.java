package tr.edu.deu.efm.command.impl;

import java.util.List;

import tr.edu.deu.efm.command.api.CommandContext;
import tr.edu.deu.efm.command.api.CommandResult;
import tr.edu.deu.efm.core.api.EntityLister;
import tr.edu.deu.efm.core.api.OperationResult;

/**
 * Command implementation for listing files and directories.
 * <p>
 * This command delegates the listing operation to the {@link EntityLister} core
 * API. It handles relative paths properly by providing the session's current
 * directory and strictly uses the Result Pattern to display either the
 * formatted list or the specific error messages without relying on exceptions.
 * </p>
 */
public class LsCommand extends BaseCommand {

	private EntityLister lister;

	public LsCommand(EntityLister lister) {
		super("ls", "Lists files and directories in the current directory.", "Usage: ls [-l] [-a] [path]");
		this.lister = lister;
	}

	@Override
	public CommandResult execute(CommandContext context) {

		String currentDir = context.getSession().getCurrentWorkingDirectory();
		String targetPath = context.getArguments().isEmpty() ? "." : context.getArguments().get(0);

		boolean showHidden = context.getFlags().hasFlag('a');
		boolean detailed = context.getFlags().hasFlag('l');

		OperationResult result = lister.list(currentDir, targetPath, showHidden, detailed);

		if (!result.isSuccess()) {
			return new CommandResult(false, result.getMessage());
		}

		StringBuilder output = new StringBuilder();
		List<String> items = result.getAffectedItems();

		for (String item : items) {
			output.append(item).append("\n");
		}

		return new CommandResult(true, output.toString().trim());
	}
}
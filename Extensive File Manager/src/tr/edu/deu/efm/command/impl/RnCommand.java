package tr.edu.deu.efm.command.impl;

import java.util.List;

import tr.edu.deu.efm.command.api.CommandContext;
import tr.edu.deu.efm.command.api.CommandFlags;
import tr.edu.deu.efm.command.api.CommandResult;
import tr.edu.deu.efm.config.Settings;
import tr.edu.deu.efm.core.api.EntityRenamer;
import tr.edu.deu.efm.core.api.OperationResult;

/**
 * Command implementation for renaming files or directories.
 * <p>
 * This command interfaces with the {@link EntityRenamer} core API. It
 * gracefully handles naming collisions or missing files directly through the
 * returned DTO instead of catching exceptions.
 * </p>
 */
public class RnCommand extends BaseCommand {

	private EntityRenamer renamer;

	public RnCommand(EntityRenamer renamer) {
		super("rn", "Renames files or directories", "Usage: rn [-v] <path> <new_name>");
		this.renamer = renamer;
	}

	@Override
	public CommandResult execute(CommandContext context) {
		List<String> args = context.getArguments();
		CommandFlags flags = context.getFlags();

		if (args.size() < 2) {
			return new CommandResult(false, "rn: missing operand");
		}

		String entityPath = args.get(0);
		String newName = args.get(1);
		String currentDir = context.getSession().getCurrentWorkingDirectory();
		boolean verbose = flags.hasFlag('v');

		OperationResult result = renamer.rename(currentDir, entityPath, newName);

		if (!result.isSuccess()) {
			return new CommandResult(false, result.getMessage());
		}

		String output = (verbose || Settings.verboseAsDefault) ? result.getMessage() : "";
		return new CommandResult(true, output);
	}
}
package tr.edu.deu.efm.command.impl;

import java.util.List;

import tr.edu.deu.efm.command.api.CommandContext;
import tr.edu.deu.efm.command.api.CommandFlags;
import tr.edu.deu.efm.command.api.CommandResult;
import tr.edu.deu.efm.config.Settings;
import tr.edu.deu.efm.core.api.EntityRenamer;
import tr.edu.deu.efm.core.api.OperationResult;
import tr.edu.deu.efm.core.exception.EntityNotFoundException;

public class RnCommand extends BaseCommand {

	private EntityRenamer renamer;

	public RnCommand(EntityRenamer renamer) {
		super("rn", "Renamas files or directories", "Usage: rn [-] <path>");
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
		boolean verbose = flags.hasFlag('v');

		try {
			OperationResult result = renamer.rename(entityPath, newName);

			StringBuilder output = new StringBuilder();

			if (verbose || Settings.verboseAsDefault) {
				for (String item : result.getAffectedItems()) {
					output.append("renamed '").append(item).append("'\n");
				}
			}

			if (result.isSuccess()) {
				return new CommandResult(true, output.toString().trim());
			} else {
				return new CommandResult(false, "rn: failed to rename item.\n" + output.toString().trim());
			}

		} catch (EntityNotFoundException e) {
			return new CommandResult(false, "rn: cannot rename '" + entityPath + "': No such file or directory");
		} catch (Exception e) {
			return new CommandResult(false, "rn: a fatal error occurred -> " + e.getMessage());
		}

	}

}

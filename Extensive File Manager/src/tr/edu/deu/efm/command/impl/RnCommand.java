package tr.edu.deu.efm.command.impl;

import java.util.List;

import tr.edu.deu.efm.command.api.CommandContext;
import tr.edu.deu.efm.command.api.CommandFlags;
import tr.edu.deu.efm.command.api.CommandResult;
import tr.edu.deu.efm.command.api.ConsoleInteractor;
import tr.edu.deu.efm.config.Settings;
import tr.edu.deu.efm.core.api.ConfirmationStrategy;
import tr.edu.deu.efm.core.api.EntityRenamer;
import tr.edu.deu.efm.core.api.OperationResult;

public class RnCommand extends BaseCommand {

	private final EntityRenamer renamer;
	private final ConsoleInteractor console;

	public RnCommand(EntityRenamer renamer, ConsoleInteractor console) {
		super("rn", "Renames files or directories", "Usage: rn [-f] [-i] [-v] <path> <new_name>");
		this.renamer = renamer;
		this.console = console;
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

		boolean verbose = flags.hasFlag('v') || Settings.verboseAsDefault;
		boolean force = flags.hasFlag('f') || Settings.overWriteMode;
		boolean interactive = flags.hasFlag('i');

		ConfirmationStrategy strategy;

		if (force) {
			strategy = (fileName) -> true;
		} else if (interactive) {
			strategy = (fileName) -> console.askConfirmation("rn: overwrite '" + fileName + "'?");
		} else {
			strategy = (fileName) -> false;
		}

		OperationResult result = renamer.rename(currentDir, entityPath, newName, strategy);

		if (!result.isSuccess()) {
			logTransaction("ERROR", "rn: " + result.getMessage());
			return new CommandResult(false, result.getMessage());
		}

		logTransaction("SUCCESS", "rn: " + result.getMessage());

		String output = verbose ? "rn: " + result.getMessage() : "";
		return new CommandResult(true, output);
	}
}
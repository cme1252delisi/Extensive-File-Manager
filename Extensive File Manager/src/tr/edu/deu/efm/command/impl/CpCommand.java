package tr.edu.deu.efm.command.impl;

import java.util.List;

import tr.edu.deu.efm.command.api.CommandContext;
import tr.edu.deu.efm.command.api.CommandResult;
import tr.edu.deu.efm.command.api.Session;
import tr.edu.deu.efm.command.api.ConsoleInteractor;
import tr.edu.deu.efm.config.Settings;
import tr.edu.deu.efm.core.api.EntityCopier;
import tr.edu.deu.efm.core.api.OperationResult;
import tr.edu.deu.efm.core.api.ConfirmationStrategy;

public class CpCommand extends BaseCommand {

	private final EntityCopier copier;
	private final ConsoleInteractor console;

	public CpCommand(EntityCopier copier, ConsoleInteractor console) {
		super("cp", "Copies files or directories.", "Usage: cp [-v] [-f] [-i] <source> <destination>");
		this.copier = copier;
		this.console = console;
	}

	@Override
	public CommandResult execute(CommandContext context) {
		List<String> args = context.getArguments();

		if (args.size() < 2) {
			return new CommandResult(false, "cp: missing destination file operand");
		}

		Session session = context.getSession();
		String currentDir = session.getCurrentWorkingDirectory();

		boolean verbose = context.getFlags().hasFlag('v') || Settings.verboseAsDefault;
		boolean force = context.getFlags().hasFlag('f') || Settings.overWriteMode;
		boolean interactive = context.getFlags().hasFlag('i');

		String source = args.get(0);
		String destination = args.get(1);

		ConfirmationStrategy strategy;

		if (force) {
			strategy = (fileName) -> true;
		} else if (interactive) {
			strategy = (fileName) -> console.askConfirmation("cp: overwrite '" + fileName + "'?");
		} else {
			strategy = (fileName) -> false;
		}

		OperationResult result = copier.copy(currentDir, source, destination, strategy);

		if (!result.isSuccess()) {
			logTransaction("ERROR", "cp: " + result.getMessage());
			return new CommandResult(false, "cp: " + result.getMessage());
		}

		logTransaction("SUCCESS", "cp: " + result.getMessage());
		String output = (verbose) ? "cp: " + result.getMessage() : "";
		return new CommandResult(true, output);
	}
}
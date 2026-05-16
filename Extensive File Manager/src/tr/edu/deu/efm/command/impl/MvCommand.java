package tr.edu.deu.efm.command.impl;

import java.util.List;

import tr.edu.deu.efm.command.api.CommandContext;
import tr.edu.deu.efm.command.api.CommandResult;
import tr.edu.deu.efm.command.api.Session;

import tr.edu.deu.efm.command.api.ConsoleInteractor;
import tr.edu.deu.efm.config.Settings;
import tr.edu.deu.efm.core.api.ConfirmationStrategy;
import tr.edu.deu.efm.core.api.EntityMover;
import tr.edu.deu.efm.core.api.OperationResult;

public class MvCommand extends BaseCommand {
	private final EntityMover mover;
	private final ConsoleInteractor console;

	public MvCommand(EntityMover mover, ConsoleInteractor console) {
		super("mv", "Moves or renames files or directories.", "Usage: mv [-v] [-f] [-i] <source> <destination>");
		this.mover = mover;
		this.console = console;
	}

	@Override
	public CommandResult execute(CommandContext context) {
		List<String> args = context.getArguments();

		if (args.size() < 2) {
			return new CommandResult(false,
					"mv: missing destination file operand after '" + (args.isEmpty() ? "" : args.get(0)) + "'");
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
			strategy = (fileName) -> console.askConfirmation("mv: overwrite '" + fileName + "'?");
		} else {
			strategy = (fileName) -> false;
		}

		OperationResult result = mover.move(currentDir, source, destination, strategy);

		if (!result.isSuccess()) {
			logTransaction("ERROR", "mv: " + result.getMessage());
			return new CommandResult(false, "mv: " + result.getMessage());
		}

		logTransaction("SUCCESS", "mv: " + result.getMessage());
		String output = (verbose || Settings.verboseAsDefault) ? "mv: " + result.getMessage() : "";
		return new CommandResult(true, output);
	}
}
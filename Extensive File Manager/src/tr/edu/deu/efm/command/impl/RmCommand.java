package tr.edu.deu.efm.command.impl;

import java.util.List;

import tr.edu.deu.efm.command.api.CommandContext;
import tr.edu.deu.efm.command.api.CommandFlags;
import tr.edu.deu.efm.command.api.CommandResult;
import tr.edu.deu.efm.command.api.ConsoleInteractor;
import tr.edu.deu.efm.config.Settings;
import tr.edu.deu.efm.core.api.ConfirmationStrategy;
import tr.edu.deu.efm.core.api.EntityRemover;
import tr.edu.deu.efm.core.api.OperationResult;

public class RmCommand extends BaseCommand {

	private final EntityRemover remover;
	private final ConsoleInteractor console;

	public RmCommand(EntityRemover remover, ConsoleInteractor console) {
		super("rm", "Removes files or directories.", "Usage: rm [-r] [-f] [-i] [-p] [-v] <path>");
		this.remover = remover;
		this.console = console;
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
		boolean verbose = flags.hasFlag('v') || Settings.verboseAsDefault;
		boolean force = flags.hasFlag('f');
		boolean interactive = flags.hasFlag('i');
		boolean permanent = flags.hasFlag('p') || !Settings.removeSafely;

		ConfirmationStrategy strategy;

		if (force) {
			strategy = (msg) -> true;
		} else if (interactive) {
			strategy = (msg) -> console.askConfirmation(msg);
		} else {
			strategy = (msg) -> true;
		}

		OperationResult result = remover.remove(currentDir, targetPath, recursive, permanent, strategy);

		if (!result.isSuccess()) {
			logTransaction("ERROR", "rm: " + result.getMessage());
			return new CommandResult(false, result.getMessage());
		}

		StringBuilder output = new StringBuilder();
		if (verbose) {
			for (String item : result.getAffectedItems()) {
				output.append("rm: processed '").append(item).append("'\n");
			}
		}

		logTransaction("SUCCESS", "rm: " + result.getMessage());
		return new CommandResult(true, output.toString().trim());
	}
}
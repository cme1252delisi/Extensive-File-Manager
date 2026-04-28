package tr.edu.deu.efm.command.impl;

import java.util.List;

import tr.edu.deu.efm.command.api.CommandContext;
import tr.edu.deu.efm.command.api.CommandResult;
import tr.edu.deu.efm.command.api.Session;
import tr.edu.deu.efm.config.Settings;
import tr.edu.deu.efm.core.api.EntityMover;
import tr.edu.deu.efm.core.api.OperationResult;

/**
 * Command implementation for moving or renaming files or directories.
 * <p>
 * This command bridges the user input to the {@link EntityMover} core API. It
 * handles relative paths by passing the session's current directory and
 * strictly adheres to the Result Pattern to display specific success or error
 * messages without relying on exceptions or overloaded data lists.
 * </p>
 */
public class MvCommand extends BaseCommand {

	private EntityMover mover;

	public MvCommand(EntityMover mover) {
		super("mv", "Moves or renames files or directories.", "Usage: mv [-v] <source> <destination>");
		this.mover = mover;
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
		boolean verbose = context.getFlags().hasFlag('v');

		String source = args.get(0);
		String destination = args.get(1);

		OperationResult result = mover.move(currentDir, source, destination);

		if (!result.isSuccess()) {
			return new CommandResult(false, result.getMessage());
		}

		String output = (verbose || Settings.verboseAsDefault) ? result.getMessage() : "";
		return new CommandResult(true, output);
	}
}
package tr.edu.deu.efm.command.impl;

import java.util.List;

import tr.edu.deu.efm.command.api.CommandContext;
import tr.edu.deu.efm.command.api.CommandResult;
import tr.edu.deu.efm.command.api.Session;
import tr.edu.deu.efm.config.Settings;
import tr.edu.deu.efm.core.api.EntityCopier;
import tr.edu.deu.efm.core.api.OperationResult;

/**
 * Command implementation for copying files or directories.
 * <p>
 * This command utilizes the {@link EntityCopier} core API. It passes the
 * current session directory to accurately resolve relative paths and handles
 * the structured result without relying on exception handling.
 * </p>
 */
public class CpCommand extends BaseCommand {

	private final EntityCopier copier;

	public CpCommand(EntityCopier copier) {
		super("cp", "Copies files or directories.", "Usage: cp <source> <destination>");
		this.copier = copier;
	}

	@Override
	public CommandResult execute(CommandContext context) {
		List<String> args = context.getArguments();

		if (args.size() < 2) {
			return new CommandResult(false, "cp: missing destination file operand");
		}

		Session session = context.getSession();
		String currentDir = session.getCurrentWorkingDirectory();
		boolean verbose = context.getFlags().hasFlag('v');

		OperationResult result = copier.copy(currentDir, args.get(0), args.get(1));

		if (!result.isSuccess()) {
			return new CommandResult(false, result.getMessage());
		}

		String output = (verbose || Settings.verboseAsDefault) ? result.getMessage() : "";
		return new CommandResult(true, output);
	}
}
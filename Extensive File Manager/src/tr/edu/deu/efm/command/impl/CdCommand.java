package tr.edu.deu.efm.command.impl;

import java.util.List;

import tr.edu.deu.efm.command.api.CommandContext;
import tr.edu.deu.efm.command.api.CommandResult;
import tr.edu.deu.efm.command.api.Session;
import tr.edu.deu.efm.config.Settings;
import tr.edu.deu.efm.core.api.DirectoryChanger;
import tr.edu.deu.efm.core.api.OperationResult;

/**
 * Command implementation for changing the current working directory.
 * <p>
 * This command bridges the user input to the {@link DirectoryChanger} core API.
 * It strictly relies on the Result Pattern, updating the session's directory
 * only upon a successful operation reported by the core layer.
 * </p>
 */
public class CdCommand extends BaseCommand {

	private DirectoryChanger changer;

	public CdCommand(DirectoryChanger changer) {
		super("cd", "Changes the current working directory.", "Usage: cd <path>");
		this.changer = changer;
	}

	@Override
	public CommandResult execute(CommandContext context) {
		List<String> args = context.getArguments();

		if (args.isEmpty()) {
			return new CommandResult(false, "cd: missing operand");
		}

		String targetFolder = args.get(0);
		Session session = context.getSession();
		boolean verbose = context.getFlags().hasFlag('v');

		OperationResult result = changer.changeDirectory(session.getCurrentWorkingDirectory(), targetFolder);

		if (!result.isSuccess()) {
			return new CommandResult(false, result.getMessage());
		}

		session.setCurrentWorkingDirectory(result.getAffectedItems().get(0));

		String output = (verbose || Settings.verboseAsDefault) ? result.getMessage() : "";
		return new CommandResult(true, output);
	}
}
package tr.edu.deu.efm.command.impl;

import java.util.List;

import tr.edu.deu.efm.command.api.CommandContext;
import tr.edu.deu.efm.command.api.CommandResult;
import tr.edu.deu.efm.command.api.ConsoleInteractor;
import tr.edu.deu.efm.config.Settings;
import tr.edu.deu.efm.core.api.ConfirmationStrategy;
import tr.edu.deu.efm.core.api.EntityExtractor;
import tr.edu.deu.efm.core.api.OperationResult;

/**
 * Command implementation for extracting ZIP archives.
 */
public class UnzipCommand extends BaseCommand {

	private final EntityExtractor extractor;
	private final ConsoleInteractor console;

	public UnzipCommand(EntityExtractor extractor, ConsoleInteractor console) {
		super("unzip", "Extracts a ZIP archive.", "Usage: unzip [-f] [-i] [-v] <archive_name> [destination_folder]");
		this.extractor = extractor;
		this.console = console;
	}

	@Override
	public CommandResult execute(CommandContext context) {
		List<String> args = context.getArguments();

		if (args.isEmpty()) {
			return new CommandResult(false, "unzip: missing archive operand");
		}

		String archivePath = args.get(0);
		String destinationPath = args.size() > 1 ? args.get(1) : archivePath.replace(".zip", "");

		String currentDir = context.getSession().getCurrentWorkingDirectory();

		boolean verbose = context.getFlags().hasFlag('v') || Settings.verboseAsDefault;
		boolean force = context.getFlags().hasFlag('f') || Settings.overWriteMode;
		boolean interactive = context.getFlags().hasFlag('i');

		ConfirmationStrategy strategy;
		if (force) {
			strategy = (fileName) -> true;
		} else if (interactive) {
			strategy = (fileName) -> console.askConfirmation("unzip: overwrite '" + fileName + "'?");
		} else {
			strategy = (fileName) -> false;
		}

		OperationResult result = extractor.extract(currentDir, archivePath, destinationPath, strategy);

		if (!result.isSuccess()) {
			logTransaction("ERROR", "unzip: " + result.getMessage());
			return new CommandResult(false, result.getMessage()); // Hata mesajı core'dan geliyor
		}

		logTransaction("SUCCESS", "unzip: " + result.getMessage());
		String output = verbose ? "unzip: " + result.getMessage() : "";
		return new CommandResult(true, output);
	}
}
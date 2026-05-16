package tr.edu.deu.efm.command.impl;

import java.util.List;

import tr.edu.deu.efm.command.api.CommandContext;
import tr.edu.deu.efm.command.api.CommandResult;
import tr.edu.deu.efm.command.api.ConsoleInteractor;
import tr.edu.deu.efm.config.Settings;
import tr.edu.deu.efm.core.api.ConfirmationStrategy;
import tr.edu.deu.efm.core.api.EntityCompressor;
import tr.edu.deu.efm.core.api.OperationResult;

/**
 * Command implementation for compressing files/directories into a ZIP archive.
 */
public class ZipCommand extends BaseCommand {

	private final EntityCompressor compressor;
	private final ConsoleInteractor console;

	public ZipCommand(EntityCompressor compressor, ConsoleInteractor console) {
		super("zip", "Compresses a file or directory into a ZIP archive.",
				"Usage: zip [-f] [-i] [-v] <source> [archive_name]");
		this.compressor = compressor;
		this.console = console;
	}

	@Override
	public CommandResult execute(CommandContext context) {
		List<String> args = context.getArguments();

		if (args.isEmpty()) {
			return new CommandResult(false, "zip: missing source operand");
		}

		String sourcePath = args.get(0);
		String archiveName;

		if (args.size() > 1) {
			archiveName = args.get(1);
		} else {
			String cleanPath = sourcePath;
			if (cleanPath.endsWith("/") || cleanPath.endsWith("\\")) {
				cleanPath = cleanPath.substring(0, cleanPath.length() - 1);
			}
			int lastSlash = Math.max(cleanPath.lastIndexOf('/'), cleanPath.lastIndexOf('\\'));
			String baseName = (lastSlash == -1) ? cleanPath : cleanPath.substring(lastSlash + 1);
			archiveName = baseName + ".zip";
		}

		String currentDir = context.getSession().getCurrentWorkingDirectory();

		boolean verbose = context.getFlags().hasFlag('v') || Settings.verboseAsDefault;
		boolean force = context.getFlags().hasFlag('f') || Settings.overWriteMode;
		boolean interactive = context.getFlags().hasFlag('i');

		ConfirmationStrategy strategy;
		if (force) {
			strategy = (fileName) -> true;
		} else if (interactive) {
			strategy = (fileName) -> console.askConfirmation("zip: overwrite '" + fileName + "'?");
		} else {
			strategy = (fileName) -> false;
		}

		OperationResult result = compressor.compress(currentDir, sourcePath, archiveName, strategy);

		if (!result.isSuccess()) {
			logTransaction("ERROR", "zip: " + result.getMessage());
			return new CommandResult(false, result.getMessage());
		}

		logTransaction("SUCCESS", "zip: " + result.getMessage());

		String output = verbose ? "zip: " + result.getMessage() : "";
		return new CommandResult(true, output);
	}
}
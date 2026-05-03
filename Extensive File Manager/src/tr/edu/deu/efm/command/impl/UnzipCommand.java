package tr.edu.deu.efm.command.impl;

import java.util.List;

import tr.edu.deu.efm.command.api.CommandContext;
import tr.edu.deu.efm.command.api.CommandResult;
import tr.edu.deu.efm.config.Settings;
import tr.edu.deu.efm.core.api.EntityExtractor;
import tr.edu.deu.efm.core.api.OperationResult;

/**
 * Command implementation for extracting ZIP archives.
 */
public class UnzipCommand extends BaseCommand {

	private EntityExtractor extractor;

	public UnzipCommand(EntityExtractor extractor) {
		super("unzip", "Extracts a ZIP archive.", "Usage: unzip [-v] [-f] <archive_name> [destination_folder]");
		this.extractor = extractor;
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
		boolean verbose = context.getFlags().hasFlag('v');
		boolean overwrite = context.getFlags().hasFlag('f');

		OperationResult result = extractor.extract(currentDir, archivePath, destinationPath, overwrite);

		if (!result.isSuccess()) {
			return new CommandResult(false, "unzip: " + result.getMessage());
		}

		String output = (verbose || Settings.verboseAsDefault) ? "unzip: " + result.getMessage() : "";
		return new CommandResult(true, output);
	}
}
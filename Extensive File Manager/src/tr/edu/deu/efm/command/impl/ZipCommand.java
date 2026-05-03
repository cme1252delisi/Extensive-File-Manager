package tr.edu.deu.efm.command.impl;

import java.util.List;

import tr.edu.deu.efm.command.api.CommandContext;
import tr.edu.deu.efm.command.api.CommandResult;
import tr.edu.deu.efm.config.Settings;
import tr.edu.deu.efm.core.api.EntityCompressor;
import tr.edu.deu.efm.core.api.OperationResult;

/**
 * Command implementation for compressing files/directories into a ZIP archive.
 */
public class ZipCommand extends BaseCommand {

	private EntityCompressor compressor;

	public ZipCommand(EntityCompressor compressor) {
		super("zip", "Compresses a file or directory into a ZIP archive.",
				"Usage: zip [-v] [-f] <source> [archive_name]");
		this.compressor = compressor;
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
		boolean verbose = context.getFlags().hasFlag('v');
		boolean overwrite = context.getFlags().hasFlag('f');

		OperationResult result = compressor.compress(currentDir, sourcePath, archiveName, overwrite);

		if (!result.isSuccess()) {
			return new CommandResult(false, "zip: " + result.getMessage());
		}

		String output = (verbose || Settings.verboseAsDefault) ? "zip: " + result.getMessage() : "";
		return new CommandResult(true, output);
	}
}
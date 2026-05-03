package tr.edu.deu.efm.command.impl;

import java.util.List;

import tr.edu.deu.efm.command.api.CommandContext;
import tr.edu.deu.efm.command.api.CommandResult;
import tr.edu.deu.efm.config.Settings;
import tr.edu.deu.efm.core.api.EntityConverter;
import tr.edu.deu.efm.core.api.OperationResult;

/**
 * Command implementation for converting files between various formats.
 */
public class ConvertCommand extends BaseCommand {

	private EntityConverter converter;

	public ConvertCommand(EntityConverter converter) {
		super("convert", "Converts files between different formats (Images, Documents, Media).",
				"Usage: convert [-v] [-f] <source_file> <target_file>");
		this.converter = converter;
	}

	@Override
	public CommandResult execute(CommandContext context) {
		List<String> args = context.getArguments();

		if (args.size() < 2) {
			return new CommandResult(false, "convert: missing operand(s)\n" + getUsage());
		}

		String sourcePath = args.get(0);
		String targetPath = args.get(1);

		String currentDir = context.getSession().getCurrentWorkingDirectory();
		boolean verbose = context.getFlags().hasFlag('v');
		boolean overwrite = context.getFlags().hasFlag('f');

		OperationResult result = converter.convert(currentDir, sourcePath, targetPath, overwrite);

		if (!result.isSuccess()) {
			return new CommandResult(false, "convert: " + result.getMessage());
		}

		String output = (verbose || Settings.verboseAsDefault) ? "convert: " + result.getMessage() : "";
		return new CommandResult(true, output);
	}
}
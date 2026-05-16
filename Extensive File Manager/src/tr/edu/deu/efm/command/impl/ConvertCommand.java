package tr.edu.deu.efm.command.impl;

import java.util.List;

import tr.edu.deu.efm.command.api.CommandContext;
import tr.edu.deu.efm.command.api.CommandResult;
import tr.edu.deu.efm.command.api.ConsoleInteractor;
import tr.edu.deu.efm.config.Settings;
import tr.edu.deu.efm.core.api.ConfirmationStrategy;
import tr.edu.deu.efm.core.api.EntityConverter;
import tr.edu.deu.efm.core.api.OperationResult;

public class ConvertCommand extends BaseCommand {

	private final EntityConverter converter;
	private final ConsoleInteractor console;

	public ConvertCommand(EntityConverter converter, ConsoleInteractor console) {
		super("convert", "Converts files between different formats (Images, Documents, Media).",
				"Usage: convert [-f] [-i] [-v] <source_file> <target_file>");
		this.converter = converter;
		this.console = console;
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

		boolean verbose = context.getFlags().hasFlag('v') || Settings.verboseAsDefault;
		boolean force = context.getFlags().hasFlag('f') || Settings.overWriteMode;
		boolean interactive = context.getFlags().hasFlag('i');

		ConfirmationStrategy strategy;
		if (force) {
			strategy = (fileName) -> true;
		} else if (interactive) {
			strategy = (fileName) -> console.askConfirmation("convert: overwrite '" + fileName + "'?");
		} else {
			strategy = (fileName) -> false;
		}

		OperationResult result = converter.convert(currentDir, sourcePath, targetPath, strategy);

		if (!result.isSuccess()) {
			logTransaction("ERROR", "convert: " + result.getMessage());
			return new CommandResult(false, result.getMessage());
		}

		logTransaction("SUCCESS", "convert: " + result.getMessage());
		String output = verbose ? "convert: " + result.getMessage() : "";
		return new CommandResult(true, output);
	}
}
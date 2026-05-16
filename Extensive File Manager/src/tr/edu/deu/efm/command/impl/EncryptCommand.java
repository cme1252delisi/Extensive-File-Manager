package tr.edu.deu.efm.command.impl;

import tr.edu.deu.efm.command.api.CommandContext;
import tr.edu.deu.efm.command.api.CommandResult;
import tr.edu.deu.efm.command.api.ConsoleInteractor;
import tr.edu.deu.efm.config.Settings;
import tr.edu.deu.efm.core.api.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class EncryptCommand extends BaseCommand {

	private final EntityEncryptor encryptor;
	private final EntityCompressor compressor;
	private final EntityInspector inspector;
	private final EntityRemover remover;
	private final EntityRenamer renamer;
	private final ConsoleInteractor console;

	public EncryptCommand(EntityEncryptor encryptor, EntityCompressor compressor, EntityInspector inspector,
			EntityRemover remover, EntityRenamer renamer, ConsoleInteractor console) {
		super("encrypt", "Encrypts a file securely or zips and encrypts a directory.",
				"Usage: encrypt [-r] [-f] [-i] [-v] <target_path>");
		this.encryptor = encryptor;
		this.compressor = compressor;
		this.inspector = inspector;
		this.remover = remover;
		this.renamer = renamer;
		this.console = console;
	}

	@Override
	public CommandResult execute(CommandContext context) {
		List<String> args = context.getArguments();

		if (args.isEmpty()) {
			return new CommandResult(false, "encrypt: missing target path.\n" + getUsage());
		}

		String targetPathStr = args.get(0);
		String currentDir = context.getSession().getCurrentWorkingDirectory();

		boolean recursive = context.getFlags().hasFlag('r');
		boolean force = context.getFlags().hasFlag('f') || Settings.overWriteMode;
		boolean interactive = context.getFlags().hasFlag('i');
		boolean verbose = context.getFlags().hasFlag('v') || Settings.verboseAsDefault;

		if (!inspector.exists(currentDir, targetPathStr)) {
			return new CommandResult(false,
					"encrypt: cannot access '" + targetPathStr + "': No such file or directory");
		}

		if (inspector.isDirectory(currentDir, targetPathStr) && !recursive) {
			return new CommandResult(false,
					"encrypt: '" + targetPathStr + "' is a directory. Use -r flag to zip and encrypt.");
		}

		ConfirmationStrategy strategy;
		if (force) {
			strategy = (fileName) -> true;
		} else if (interactive) {
			strategy = (fileName) -> console.askConfirmation("encrypt: overwrite '" + fileName + "'?");
		} else {
			strategy = (fileName) -> false;
		}

		char[] password = console.readPassword("Enter password to encrypt '" + targetPathStr + "': ");
		if (password == null || password.length == 0) {
			return new CommandResult(false, "encrypt: password cannot be empty. aborted.");
		}

		char[] confirmPassword = console.readPassword("Confirm password: ");
		if (!Arrays.equals(password, confirmPassword)) {
			Arrays.fill(password, '0');
			if (confirmPassword != null)
				Arrays.fill(confirmPassword, '0');
			return new CommandResult(false, "encrypt: passwords do not match. aborted.");
		}

		OperationResult result;

		if (inspector.isDirectory(currentDir, targetPathStr)) {
			int lastSlash = Math.max(targetPathStr.lastIndexOf('/'), targetPathStr.lastIndexOf('\\'));
			String baseName = (lastSlash == -1) ? targetPathStr : targetPathStr.substring(lastSlash + 1);

			String tempZipName = baseName + "_temp.zip";
			String tempEncName = tempZipName + ".enc";
			String finalEncName = baseName + ".zip.enc";

			OperationResult zipResult = compressor.compress(currentDir, targetPathStr, tempZipName, (fileName) -> true);

			if (!zipResult.isSuccess()) {
				Arrays.fill(password, '0');
				Arrays.fill(confirmPassword, '0');
				return new CommandResult(false, zipResult.getMessage());
			}

			result = encryptor.encrypt(currentDir, tempZipName, password, (fileName) -> true);

			if (result.isSuccess()) {
				OperationResult renameResult = renamer.rename(currentDir, tempEncName, finalEncName, strategy);

				if (!renameResult.isSuccess()) {
					remover.remove(currentDir, tempEncName, false, true, (msg) -> true);
					result = new OperationResult(false, renameResult.getMessage(), Collections.emptyList());
				} else {
					result = new OperationResult(true, "successfully encrypted directory to '" + finalEncName + "'",
							renameResult.getAffectedItems());
				}
			}

			remover.remove(currentDir, tempZipName, false, true, (msg) -> true);

		} else {
			result = encryptor.encrypt(currentDir, targetPathStr, password, strategy);
		}

		Arrays.fill(password, '0');
		Arrays.fill(confirmPassword, '0');

		if (!result.isSuccess()) {
			logTransaction("ERROR", "failed to encrypt target: '" + targetPathStr + "' -> " + result.getMessage());
			return new CommandResult(false, result.getMessage());
		}

		logTransaction("SUCCESS", "successfully encrypted target: '" + targetPathStr + "'");
		String output = verbose ? result.getMessage() : "";
		return new CommandResult(true, output);
	}
}
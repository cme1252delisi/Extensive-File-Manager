package tr.edu.deu.efm.command.impl;

import tr.edu.deu.efm.command.api.CommandContext;
import tr.edu.deu.efm.command.api.CommandResult;
import tr.edu.deu.efm.command.api.ConsoleInteractor;
import tr.edu.deu.efm.config.Settings;
import tr.edu.deu.efm.core.api.*;

import java.util.Arrays;
import java.util.List;

public class DecryptCommand extends BaseCommand {

    private final EntityDecryptor decryptor;
    private final EntityExtractor extractor;
    private final EntityInspector inspector;
    private final EntityRemover remover;
    private final ConsoleInteractor console;

    public DecryptCommand(EntityDecryptor decryptor, EntityExtractor extractor, EntityInspector inspector,
            EntityRemover remover, ConsoleInteractor console) {
        super("decrypt", "Decrypts an AES-256 encrypted file.", "Usage: decrypt [-f] [-i] [-v] <target_path>");
        this.decryptor = decryptor;
        this.extractor = extractor;
        this.inspector = inspector;
        this.remover = remover;
        this.console = console;
    }

    @Override
    public CommandResult execute(CommandContext context) {
        List<String> args = context.getArguments();

        if (args.isEmpty()) {
            return new CommandResult(false, "decrypt: missing target path.\n" + getUsage());
        }

        String targetPathStr = args.get(0);
        String currentDir = context.getSession().getCurrentWorkingDirectory();

        boolean force = context.getFlags().hasFlag('f') || Settings.overWriteMode;
        boolean interactive = context.getFlags().hasFlag('i');
        boolean verbose = context.getFlags().hasFlag('v') || Settings.verboseAsDefault;

        if (!inspector.exists(currentDir, targetPathStr)) {
            return new CommandResult(false, "decrypt: cannot access '" + targetPathStr + "': No such file or directory");
        }

        ConfirmationStrategy strategy;
        if (force) {
            strategy = (fileName) -> true;
        } else if (interactive) {
            strategy = (fileName) -> console.askConfirmation("decrypt: overwrite '" + fileName + "'?");
        } else {
            strategy = (fileName) -> false;
        }

        char[] password = console.readPassword("Enter password to decrypt '" + targetPathStr + "': ");
        if (password == null || password.length == 0) {
            return new CommandResult(false, "decrypt: password cannot be empty. aborted.");
        }

        OperationResult result = decryptor.decrypt(currentDir, targetPathStr, password, strategy);
        Arrays.fill(password, '0');

        if (!result.isSuccess()) {
            logTransaction("ERROR", "decrypt: failed to decrypt target: '" + targetPathStr + "' -> " + result.getMessage());
            return new CommandResult(false, result.getMessage());
        }

        String decryptedPathStr = result.getAffectedItems().get(0);
        int lastSlash = Math.max(decryptedPathStr.lastIndexOf('/'), decryptedPathStr.lastIndexOf('\\'));
        String decryptedFileName = (lastSlash == -1) ? decryptedPathStr : decryptedPathStr.substring(lastSlash + 1);

        if (decryptedFileName.endsWith(".zip")) {
            boolean extract = console.askConfirmation("decrypt: The decrypted file is a ZIP archive. Extract it automatically?");
            
            if (extract) {
                OperationResult extractResult = extractor.extract(currentDir, decryptedPathStr, ".", strategy);
                if (extractResult.isSuccess()) {
                    remover.remove(currentDir, decryptedPathStr, false, true, (msg) -> true); 
                    
                    String finalFolderName = decryptedFileName.substring(0, decryptedFileName.length() - 4);
                    logTransaction("SUCCESS", "decrypt: successfully decrypted and restored folder: '" + finalFolderName + "'");
                    String output = verbose ? "decrypt: successfully decrypted and restored folder: '" + finalFolderName + "'" : "";
                    return new CommandResult(true, output);
                } else {
                    return new CommandResult(false, "decrypt: successfully decrypted but extraction failed -> " + extractResult.getMessage());
                }
            }
        }

        logTransaction("SUCCESS", "decrypt: successfully decrypted target: '" + targetPathStr + "'");
        String output = verbose ? "decrypt: " + result.getMessage() : "";
        return new CommandResult(true, output);
    }
}
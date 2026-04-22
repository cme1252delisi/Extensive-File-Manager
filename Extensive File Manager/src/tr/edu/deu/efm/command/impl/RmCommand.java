package tr.edu.deu.efm.command.impl;

import java.util.List;

import tr.edu.deu.efm.command.api.CommandContext;
import tr.edu.deu.efm.command.api.CommandFlags;
import tr.edu.deu.efm.command.api.CommandResult;
import tr.edu.deu.efm.config.Settings;
import tr.edu.deu.efm.core.api.EntityRemover;
import tr.edu.deu.efm.core.api.OperationResult;
import tr.edu.deu.efm.core.exception.EntityNotFoundException;
import tr.edu.deu.efm.core.exception.IllegalDirectoryOperationException;

public class RmCommand extends BaseCommand {
	
	private EntityRemover remover;

	public RmCommand(EntityRemover remover) {
		super("rm", "Removes files or directories.", "Usage: rm [-r] <path>");
		this.remover = remover; 	
	}

	@Override
    public CommandResult execute(CommandContext context) {
        List<String> args = context.getArguments();
        CommandFlags flags = context.getFlags();

        if (args.isEmpty()) {
            return new CommandResult(false, "rm: missing operand");
        }

        String targetPath = args.get(0);
        boolean recursive = flags.hasFlag('r');
        boolean verbose = flags.hasFlag('v');

        try {
            OperationResult result = remover.remove(targetPath, recursive, verbose);
            
            StringBuilder output = new StringBuilder();
            
            if (verbose || Settings.verboseAsDefault) {
                for (String item : result.getAffectedItems()) {
                    output.append("removed '").append(item).append("'\n");
                }
            }

            if (result.isSuccess()) {
                return new CommandResult(true, output.toString().trim());
            } else {
                return new CommandResult(false, "rm: failed to remove some items.\n" + output.toString().trim());
            }

        } catch (EntityNotFoundException e) {
            return new CommandResult(false, "rm: cannot remove '" + targetPath + "': No such file or directory");
        } catch (IllegalDirectoryOperationException e) {
            return new CommandResult(false, "rm: cannot remove '" + targetPath + "': Is a directory");
        } catch (Exception e) {
            return new CommandResult(false, "rm: a fatal error occurred -> " + e.getMessage());
        }
    }

}

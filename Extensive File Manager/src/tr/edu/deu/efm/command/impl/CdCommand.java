package tr.edu.deu.efm.command.impl;

import java.util.List;

import tr.edu.deu.efm.command.api.CommandContext;
import tr.edu.deu.efm.command.api.CommandResult;
import tr.edu.deu.efm.command.api.EfmSession;
import tr.edu.deu.efm.core.api.DirectoryChanger;
import tr.edu.deu.efm.core.api.OperationResult;
import tr.edu.deu.efm.core.exception.EntityNotFoundException;

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
        EfmSession session = context.getEfmSession();
        
        try {
            OperationResult result = changer.changeDirectory(session.getCurrentWorkingDirectory(), targetFolder);
            
            if (result.isSuccess()) {
                session.setCurrentWorkingDirectory(result.getAffectedItems().get(0));
                return new CommandResult(true, ""); 
            } else {
                return new CommandResult(false, result.getAffectedItems().get(0));
            }

        } catch (EntityNotFoundException e) {
            return new CommandResult(false, e.getMessage());
        }
    }

}

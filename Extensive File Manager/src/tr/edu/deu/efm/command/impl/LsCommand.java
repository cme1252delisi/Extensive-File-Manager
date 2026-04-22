package tr.edu.deu.efm.command.impl;

import java.util.List;

import tr.edu.deu.efm.command.api.CommandContext;
import tr.edu.deu.efm.command.api.CommandResult;
import tr.edu.deu.efm.core.api.EntityLister;
import tr.edu.deu.efm.core.api.OperationResult;

public class LsCommand extends BaseCommand {
	
	EntityLister lister;

	public LsCommand(EntityLister lister) {
		super("ls", "Lists files and directories in the current directory.", "Usage: ls [-l] [-a]");
		this.lister = lister;
	}

	@Override
    public CommandResult execute(CommandContext context) {
        
        String targetPathStr;

        if (context.getArguments().isEmpty()) {
            targetPathStr = context.getEfmSession().getCurrentWorkingDirectory().toString();
        } else {
            targetPathStr = context.getArguments().get(0);
        }

        try {
            OperationResult result = lister.list(targetPathStr);

            if (result.isSuccess()) {
                StringBuilder output = new StringBuilder();
                List<String> items = result.getAffectedItems();
                
                for (String item : items) {
                    output.append(item).append("\n");
                }
                
                return new CommandResult(true, output.toString().trim());
            } else {
                return new CommandResult(false, "ls: cannot access '" + targetPathStr + "': Permission denied or error");
            }

        } catch (Exception e) { 
            return new CommandResult(false, e.getMessage());
        }
    }

}

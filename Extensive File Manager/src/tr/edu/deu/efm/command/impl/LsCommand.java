package tr.edu.deu.efm.command.impl;

import tr.edu.deu.efm.command.api.BaseCommand;
import tr.edu.deu.efm.command.api.CommandContext;
import tr.edu.deu.efm.command.api.CommandResult;
import tr.edu.deu.efm.core.api.EntityLister;

public class LsCommand extends BaseCommand {
	
	EntityLister lister;

	public LsCommand(EntityLister lister) {
		super("ls", "Lists files and directories in the current directory.", "Usage: ls [-l] [-a]");
		this.lister = lister;
	}

	@Override
	public CommandResult execute(CommandContext context) {
		// TODO Auto-generated method stub
		return null;

	}

}

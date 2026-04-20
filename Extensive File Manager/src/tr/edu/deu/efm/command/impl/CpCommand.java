package tr.edu.deu.efm.command.impl;

import tr.edu.deu.efm.command.api.BaseCommand;
import tr.edu.deu.efm.command.api.CommandContext;
import tr.edu.deu.efm.command.api.CommandResult;
import tr.edu.deu.efm.core.api.EntityCopier;

public class CpCommand extends BaseCommand {
	
	private EntityCopier copier;

	public CpCommand(EntityCopier copier) {
		super("cp", "Copies files or directories.", "Usage: cp <source> <destination>");
		this.copier = copier;
	}

	@Override
	public CommandResult execute(CommandContext context) {
		// TODO Auto-generated method stub
		return null;

	}

}

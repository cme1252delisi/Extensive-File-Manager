package tr.edu.deu.efm.command.impl;

import tr.edu.deu.efm.command.api.CommandContext;
import tr.edu.deu.efm.command.api.CommandResult;
import tr.edu.deu.efm.core.api.EntityCopier;
import tr.edu.deu.efm.core.api.EntityMover;

public class MvCommand extends BaseCommand {
	
	private EntityMover mover;

	public MvCommand(EntityMover mover) {
		super("mv", "Moves or renames files or directories.", "Usage: mv <source> <destination>");
		this.mover = mover;
	}

	@Override
	public CommandResult execute(CommandContext context) {
		// TODO Auto-generated method stub
		return null;

	}

}

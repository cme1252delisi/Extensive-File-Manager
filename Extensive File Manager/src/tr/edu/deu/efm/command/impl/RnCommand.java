package tr.edu.deu.efm.command.impl;

import java.util.List;

import tr.edu.deu.efm.command.api.BaseCommand;
import tr.edu.deu.efm.command.api.CommandContext;
import tr.edu.deu.efm.command.api.CommandFlags;
import tr.edu.deu.efm.command.api.CommandResult;
import tr.edu.deu.efm.core.api.EntityRenamer;

public class RnCommand extends BaseCommand {
	
	private EntityRenamer renamer;

	public RnCommand(EntityRenamer renamer) {
		super("Rn", "Renamas files or directories", "Usage: rn [-] <path>");
		this.renamer = renamer;
	}
	
	@Override
    public CommandResult execute(CommandContext context) {
        List<String> args = context.getArguments();
        CommandFlags flags = context.getFlags();

        if (args.isEmpty()) {
            return new CommandResult(false, "rn: missing operand");
        }

       return null;
    }

}

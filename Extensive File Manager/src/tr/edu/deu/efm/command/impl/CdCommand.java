package tr.edu.deu.efm.command.impl;

import java.util.List;

import tr.edu.deu.efm.command.api.BaseCommand;
import tr.edu.deu.efm.command.api.CommandContext;
import tr.edu.deu.efm.command.api.CommandFlags;
import tr.edu.deu.efm.command.api.CommandResult;
import tr.edu.deu.efm.core.api.DirectoryChanger;

public class CdCommand extends BaseCommand {

	private DirectoryChanger directoryChanger;

	public CdCommand(DirectoryChanger directoryChanger) {
		super("cd", "Changes the current working directory.", "Usage: cd <path>");
		this.directoryChanger = directoryChanger;
	}

	@Override
	public CommandResult execute(CommandContext context) {

		List<String> args = context.getArguments();

		if (args.isEmpty()) {
			return new CommandResult(false, "cd: missing operand");
		}
		
		

		return null;

	}

}

package tr.edu.deu.efm.command.impl;

import tr.edu.deu.efm.command.api.Command;
import tr.edu.deu.efm.command.api.CommandContext;
import tr.edu.deu.efm.command.api.CommandFlags;
import tr.edu.deu.efm.command.api.CommandRegistry;
import tr.edu.deu.efm.command.api.CommandResult;

public class HelpCommand extends BaseCommand {

	private CommandRegistry commandRegistry;

	public HelpCommand(CommandRegistry commands) {
		super("help", "Displays help information for EFM commands.", "Usage: help");
		this.commandRegistry = commands;
	}

	@Override
	public CommandResult execute(CommandContext context) {
		CommandFlags flags = context.getFlags();
		StringBuilder sb = new StringBuilder();
		
		for (Command c : commandRegistry.getRegistryMap().values()) {
			sb.append(c.getName() + ": " + c.getDescription() + "\n");
			if (flags.hasFlag('l')) {
				sb.append(c.getUsage() + "\n");
			}
		}
		boolean success = true;
		return new CommandResult(success, sb.toString());
	}

}

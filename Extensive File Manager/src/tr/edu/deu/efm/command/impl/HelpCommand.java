package tr.edu.deu.efm.command.impl;

import tr.edu.deu.efm.command.api.Command;
import tr.edu.deu.efm.command.api.CommandContext;
import tr.edu.deu.efm.command.api.CommandFlags;
import tr.edu.deu.efm.command.api.CommandRegistry;
import tr.edu.deu.efm.command.api.CommandResult;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Command implementation for displaying system help and command documentation.
 */
public class HelpCommand extends BaseCommand {

	private CommandRegistry commandRegistry;

	public HelpCommand(CommandRegistry commands) {
		super("help", "Displays help information for EFM commands.", "Usage: help [-l]");
		this.commandRegistry = commands;
	}

	@Override
	public CommandResult execute(CommandContext context) {
		CommandFlags flags = context.getFlags();
		StringBuilder sb = new StringBuilder();

		List<Command> sortedCommands = new ArrayList<>(commandRegistry.getAllCommands());
		sortedCommands.sort(Comparator.comparing(Command::getName));

		sb.append("Extensive File Manager (EFM) - Available Commands:\n");
		sb.append("--------------------------------------------------\n");

		for (Command c : sortedCommands) {
			sb.append(String.format("%-10s : %s\n", c.getName(), c.getDescription()));

			if (flags.hasFlag('l')) {
				sb.append(String.format("             %s\n", c.getUsage()));
			}
		}

		return new CommandResult(true, sb.toString().trim());
	}
}
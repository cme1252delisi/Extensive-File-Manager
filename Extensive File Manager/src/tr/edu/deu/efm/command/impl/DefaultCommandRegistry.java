package tr.edu.deu.efm.command.impl;

import tr.edu.deu.efm.command.api.Command;
import tr.edu.deu.efm.command.api.CommandRegistry;

import java.util.HashMap;
import java.util.Map;

public class DefaultCommandRegistry implements CommandRegistry {

	private final Map<String, Command> commands = new HashMap<>();

	@Override
	public Map<String, Command> getRegistryMap() {
		return commands;
	}

	@Override
	public void register(String name, Command command) {
		commands.put(name, command);
	}

	@Override
	public Command getCommand(String name) {
		return commands.get(name);
	}
}
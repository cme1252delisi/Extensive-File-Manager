package tr.edu.deu.efm.command.api;

import java.util.HashMap;
import java.util.Map;

public class CommandRegistry {
	private final Map<String, Command> commands = new HashMap<>();
	
	public Map<String, Command> getRegistryMap() {
		return commands;
	}

	public void register(String name, Command command) {
		commands.put(name, command);
	}

	public Command getCommand(String name) {
		return commands.get(name);
	}
}
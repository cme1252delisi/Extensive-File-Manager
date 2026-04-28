package tr.edu.deu.efm.command.impl;

import tr.edu.deu.efm.command.api.Command;
import tr.edu.deu.efm.command.api.CommandRegistry;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * The default implementation of the {@link CommandRegistry}.
 * <p>
 * This class acts as the central hub for registering and retrieving available
 * EFM commands. It strictly enforces encapsulation by hiding its internal
 * {@link HashMap} structure, providing only safe, read-only access to the
 * registered commands.
 * </p>
 */
public class DefaultCommandRegistry implements CommandRegistry {

	private final Map<String, Command> commands = new HashMap<>();

	/**
	 * Retrieves all registered commands safely.
	 * 
	 * @return An unmodifiable collection of commands, protecting the internal
	 *         registry from external modification.
	 */
	@Override
	public Collection<Command> getAllCommands() {
		return Collections.unmodifiableCollection(commands.values());
	}

	@Override
	public void register(String name, Command command) {
		commands.put(name.toLowerCase(), command);
	}

	@Override
	public Command getCommand(String name) {
		return commands.get(name.toLowerCase());
	}
}
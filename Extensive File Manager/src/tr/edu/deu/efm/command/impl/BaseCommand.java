package tr.edu.deu.efm.command.impl;

import tr.edu.deu.efm.command.api.Command;

/**
 * An abstract base class that provides a skeletal implementation of the
 * {@link Command} interface.
 * <p>
 * This class is designed to adhere to the DRY (Don't Repeat Yourself) principle
 * by minimizing the boilerplate code required to create new commands. It
 * centralizes the common metadata properties—such as the command's name,
 * description, and usage instructions.
 * </p>
 * <p>
 * Concrete command implementations (e.g., {@code LsCommand}, {@code CdCommand})
 * should extend this class and supply these core details via the constructor,
 * allowing them to focus solely on their unique execution logic.
 * </p>
 */
public abstract class BaseCommand implements Command {

	private final String name;
	private final String description;
	private final String usage;

	/**
	 * Constructs a new BaseCommand with the specified metadata.
	 *
	 * @param name        The trigger word for the command (e.g., "ls", "cd").
	 * @param description A brief explanation of what the command does.
	 * @param usage       The syntax demonstrating how to use the command (e.g.,
	 *                    "Usage: cd <path>").
	 */
	public BaseCommand(String name, String description, String usage) {
		this.name = name;
		this.description = description;
		this.usage = usage;
	}

	/**
	 * Retrieves the executable name of the command.
	 *
	 * @return The command name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Retrieves the description of the command's functionality.
	 *
	 * @return A short description string.
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Retrieves the usage instructions or syntax for the command.
	 *
	 * @return The usage string.
	 */
	public String getUsage() {
		return usage;
	}
}
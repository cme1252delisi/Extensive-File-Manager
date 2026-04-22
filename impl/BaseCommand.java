package tr.edu.deu.efm.command.impl;

import tr.edu.deu.efm.command.api.Command;

public abstract class BaseCommand implements Command {
	private String name;
	private String description;
	private String usage;

	public BaseCommand(String name, String description, String usage) {
		this.name = name;
		this.description = description;
		this.usage = usage;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public String getUsage() {
		return usage;
	}
}

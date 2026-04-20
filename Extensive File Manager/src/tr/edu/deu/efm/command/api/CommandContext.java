package tr.edu.deu.efm.command.api;

import java.util.ArrayList;
import java.util.List;

public class CommandContext {
    private String commandName;
    private CommandFlags flags;
    private List<String> arguments;
    
    public CommandContext() {
        this.setCommandName("");
        this.setFlags(new CommandFlags());
        this.setArguments(new ArrayList<>());
    }

	public String getCommandName() {
		return commandName;
	}

	public void setCommandName(String commandName) {
		this.commandName = commandName;
	}

	public CommandFlags getFlags() {
		return flags;
	}
	
	public void addFlag(char c) {
		flags.addFlag(c);
	}

	public void setFlags(CommandFlags flags) {
		this.flags = flags;
	}

	public List<String> getArguments() {
		return arguments;
	}
	
	public void addArgument(String argument) {
		this.arguments.add(argument);
	}

	public void setArguments(List<String> arguments) {
		this.arguments = arguments;
	}
    
}
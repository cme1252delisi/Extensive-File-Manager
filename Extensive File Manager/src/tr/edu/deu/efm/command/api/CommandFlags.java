package tr.edu.deu.efm.command.api;

import java.util.HashSet;
import java.util.Set;

public class CommandFlags {

	private Set<Character> flags;

	public CommandFlags() {
		this.flags = new HashSet<>();
	}

	public void addFlag(char flag) {
		flags.add(flag);
	}

	public void parseAndAddFlags(String flagArgument) {
		if (flagArgument != null && flagArgument.startsWith("-")) {
			for (int i = 1; i < flagArgument.length(); i++) {
				addFlag(flagArgument.charAt(i));
			}
		}
	}

	public boolean hasFlag(char flag) {
		return this.flags.contains(flag);
	}

	@Override
	public String toString() {
		return "Active Flags: -" + getFlagsAsString();
	}

	private String getFlagsAsString() {
		StringBuilder sb = new StringBuilder();
		for (Character c : flags) {
			sb.append(c);
		}
		return sb.toString();
	}
}
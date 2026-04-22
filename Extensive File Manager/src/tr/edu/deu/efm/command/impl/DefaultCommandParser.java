package tr.edu.deu.efm.command.impl;

import tr.edu.deu.efm.command.api.CommandContext;
import tr.edu.deu.efm.command.api.CommandParser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DefaultCommandParser implements CommandParser {

	private static final Pattern COMMAND_PATTERN = Pattern.compile("\"([^\"]*)\"|(\\S+)");

	@Override
	public CommandContext parse(String rawInput) {
		if (rawInput == null || rawInput.trim().isEmpty()) {
			throw new IllegalArgumentException("No input provided.");
		}

		CommandContext context = new CommandContext();

		Matcher matcher = COMMAND_PATTERN.matcher(rawInput);
		boolean isFirstToken = true;

		while (matcher.find()) {
			String token = matcher.group(1) != null ? matcher.group(1) : matcher.group(2);

			if (isFirstToken) {
				context.setCommandName(token.toLowerCase());
				isFirstToken = false;
			} else {
				if (token.startsWith("-") && token.length() > 1) {
					for (int i = 1; i < token.length(); i++) {
						context.getFlags().addFlag(token.charAt(i));
					}
				} else {
					context.getArguments().add(token);
				}
			}
		}

		if (isFirstToken) {
			throw new IllegalArgumentException("Invalid command format.");
		}

		return context;
	}
}
package tr.edu.deu.efm.command.api;

public class CommandResult {
	private final boolean success;
	private final String message;
	private final boolean terminateProgram;

	public CommandResult(boolean success, String message) {
		this.success = success;
		this.message = message;
		this.terminateProgram = false;
	}

	public CommandResult(boolean success, String message, boolean terminateProgram) {
		this.success = success;
		this.message = message;
		this.terminateProgram = terminateProgram;
	}

	public boolean isSuccess() {
		return success;
	}

	public String getMessage() {
		return message;
	}

	public boolean isTerminateProgram() {
		return terminateProgram;
	}

}
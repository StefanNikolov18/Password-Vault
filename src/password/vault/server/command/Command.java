package password.vault.server.command;

public interface Command {
    CommandResult execute(String[] args, String currentUser);
}

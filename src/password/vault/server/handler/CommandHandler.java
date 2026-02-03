package password.vault.server.handler;

import password.vault.server.command.*;
import password.vault.server.repository.UserRepository;
import password.vault.server.service.auth.AuthenticationService;
import password.vault.server.service.vault.VaultService;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class CommandHandler {
    private final Map<String, Command> commands = new HashMap<>();
    private String currentUser = null;

    public CommandHandler(UserRepository userRepository) throws IOException {
        if (userRepository == null) {
            throw new IllegalArgumentException("userRepository cannot be null");
        }

        AuthenticationService authService = new AuthenticationService(userRepository);
        commands.put("register", new RegisterCommand(authService));
        commands.put("login", new LoginCommand(authService));

        VaultService vaultService = new VaultService(userRepository);
        commands.put("retrieve-credentials", new RetrieveCredentialCommand(vaultService));
        commands.put("generate-password", new GeneratePasswordCommand(vaultService));
        commands.put("add-password",  new AddPasswordCommand(vaultService));
        commands.put("remove-password", new RemovePasswordCommand(vaultService));
    }

    public String execute(String commandLine) {
        if (commandLine == null || commandLine.isBlank()) {
            throw new IllegalArgumentException("commandLine cannot be null");
        }

        String[] parts = commandLine.split(" ");
        String cmd = parts[0];
        String[] args = Arrays.copyOfRange(parts, 1, parts.length);

        if (cmd.equals("logout")) {
            if (currentUser == null) {
                return "You are not logged in";
            }
            currentUser = null;
            return "Logout successful.";
        }

        Command command = commands.get(cmd);
        if (command == null) {
            return "Unknown command: " + cmd + "! Type help for more information.";
        }

        CommandResult result = command.execute(args, currentUser); //given parameters and currentUser
        if (result.newUser() != null) {
            currentUser = result.newUser();
        }

        return result.message();
    }

}

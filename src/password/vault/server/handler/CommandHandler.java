package password.vault.server.handler;

import password.vault.server.command.Command;
import password.vault.server.command.LoginCommand;
import password.vault.server.command.RegisterCommand;
import password.vault.server.command.RetrieveCredentialCommand;
import password.vault.server.command.GeneratePasswordCommand;
import password.vault.server.command.AddPasswordCommand;
import password.vault.server.command.RemovePasswordCommand;
import password.vault.server.command.CommandResult;

import password.vault.server.repository.UserRepository;
import password.vault.server.service.auth.AuthenticationService;
import password.vault.server.service.vault.VaultService;
import password.vault.server.session.Session;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class CommandHandler {
    private final Map<String, Command> commands = new HashMap<>();
    private String currentUser = null;

    private final Session loginSession = new Session();

    public CommandHandler(UserRepository userRepository) throws IOException {
        if (userRepository == null) {
            throw new IllegalArgumentException("userRepository cannot be null");
        }

        AuthenticationService authService = new AuthenticationService(userRepository);
        commands.put("register", new RegisterCommand(authService));
        commands.put("login", new LoginCommand(authService));

        VaultService vaultService = new VaultService();
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

        if (cmd.equals("logout")) { // logout
            return logout();
        }

        Command command = commands.get(cmd);
        if (command == null) {
            return "Unknown command: " + cmd + "! Type help for more information.";
        }

        if (isSessionExpired()) {
            currentUser = null;
            return "Session has expired. Please login again.";
        }
        loginSession.refreshSession();
        CommandResult result = command.execute(args, currentUser); //given parameters and currentUser
        handleLogin(cmd, result.newUser());

        return result.message();
    }

    private boolean isSessionExpired() {
        return currentUser != null && loginSession.isSessionExpired();
    }

    private String logout() {
        if (currentUser == null) {
            return "You are not logged in";
        }
        currentUser = null;
        return "Logout successful.";
    }

    private void handleLogin(String cmd, String username) {
        if ("login".equals(cmd) && username != null) {
            loginSession.startSession();
            currentUser = username;
        }
    }

}

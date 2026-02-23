package password.vault.server.handler;

import password.vault.server.command.Command;
import password.vault.server.command.unauthenticated.HelpCommand;
import password.vault.server.command.unauthenticated.LoginCommand;
import password.vault.server.command.unauthenticated.RegisterCommand;
import password.vault.server.command.authenticated.RetrieveCredentialCommand;
import password.vault.server.command.authenticated.GeneratePasswordCommand;
import password.vault.server.command.authenticated.AddPasswordCommand;
import password.vault.server.command.authenticated.RemovePasswordCommand;
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

    private static class CommandInfo {
        Command command;
        boolean requiresAuth;

        CommandInfo(Command command, boolean requiresAuth) {
            this.command = command;
            this.requiresAuth = requiresAuth;
        }
    }

    private final Map<String, CommandInfo> commands = new HashMap<>();
    private String currentUser = null;

    private final Session loginSession = new Session();

    public CommandHandler(UserRepository userRepository) throws IOException {
        if (userRepository == null) {
            throw new IllegalArgumentException("userRepository cannot be null");
        }

        AuthenticationService authService = new AuthenticationService(userRepository);
        commands.put("register", new CommandInfo(new RegisterCommand(authService), false));
        commands.put("login", new CommandInfo(new LoginCommand(authService), false));
        commands.put("help", new CommandInfo(new HelpCommand(), false));

        VaultService vaultService = new VaultService();
        commands.put("retrieve-credentials", new CommandInfo(new RetrieveCredentialCommand(vaultService), true));
        commands.put("generate-password", new CommandInfo(new GeneratePasswordCommand(vaultService), true));
        commands.put("add-password",  new CommandInfo(new AddPasswordCommand(vaultService), true));
        commands.put("remove-password", new CommandInfo(new RemovePasswordCommand(vaultService), true));
    }

    public String execute(String commandLine) {
        if (commandLine == null || commandLine.isBlank()) {
            throw new IllegalArgumentException("CommandLine cannot be null or blank!");
        }

        String[] parts = commandLine.split(" ");
        String cmd = parts[0];
        String[] args = Arrays.copyOfRange(parts, 1, parts.length);

        if (cmd.equals("logout")) { // logout
            return logout();
        }

        CommandInfo cmdInfo = commands.get(cmd);
        if (cmdInfo == null) {
            return "Unknown command: " + cmd + "! Type help for more information.";
        }
        Command command = cmdInfo.command;

        if (cmdInfo.requiresAuth && isSessionExpired()) {
            setCurrentUser(null);
            return "Session has expired. Please login again.";
        }
        loginSession.refreshSession();
        CommandResult result = command.execute(args, currentUser); //given parameters and currentUser
        handleLogin(cmd, result.newUser());

        return result.message();
    }

    private String logout() {
        if (currentUser == null) {
            return "You are not logged in";
        }
        setCurrentUser(null);
        return "Logout successful.";
    }

    boolean isSessionExpired() {
        return currentUser != null && loginSession.isSessionExpired();
    }

    private void handleLogin(String cmd, String username) {
        if ("login".equals(cmd) && username != null) {
            loginSession.startSession();
            setCurrentUser(username);
        }
    }

    public String getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(String currentUser) {
        this.currentUser = currentUser;
    }

    Map<String, CommandInfo> getCommands() {
        return commands;
    }
}

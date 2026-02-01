package password.vault.server.handler;

import password.vault.server.repository.UserRepository;
import password.vault.server.service.auth.AuthenticationResult;
import password.vault.server.service.auth.AuthenticationService;
import password.vault.server.service.VaultService;

import java.util.Arrays;

public class CommandHandler {

    private AuthenticationService authService;    // for register/login user in the userRepository
    private VaultService vaultService;                      // for retrieve/generate/add/remove password
    private String currentUser = null;

    public CommandHandler(UserRepository userRepository) {
        if (userRepository == null) {
            throw new IllegalArgumentException("UserRepository is null");
        }
        this.authService = new AuthenticationService(userRepository);
        this.vaultService = new VaultService(userRepository);
    }

    public String execute(String commandLine) {
        if (commandLine == null || commandLine.isBlank()) {
            return "Invalid command line! Type help for more information.";
        }

        String[] line = commandLine.split(" ");
        String cmd = line[0];

        String[] args = Arrays.copyOfRange(line, 1, line.length);

        return switch (cmd) {
            case "register" -> register(args);
            case "login" ->  login(args);
            case "logout" -> logout(args);
            case "retrieve-credentials" -> retrieveCredentials(args);
            case "generate-password" -> generatePassword(args);
            case "add-password" -> addPassword(args);
            case "remove-password" -> removePassword(args);
            case "disconnect" -> disconnect();
            case "help" -> help();
            default -> "Invalid command! Type help for more information.";
        };
    }

    private String register(String[] args) {
        if (currentUser != null) {
            return "Already logged in!";
        }

        AuthenticationResult result = authService.register(args);
        if (result.success()) {
            currentUser = result.username();
        }

        return result.message();
    }

    private String login(String[] args) {
        if (currentUser != null) {
            return "Already logged in!";
        }

        AuthenticationResult result = authService.login(args);
        if (result.success()) {
            currentUser = result.username();
        }

        return result.message();
    }

    private String logout(String[] args) {
        if (currentUser == null) {
            return "No one to log out!";
        }

        this.currentUser = null;
        return "Logout successful";
    }

    private String retrieveCredentials(String[] args) {
        return "Undefined yet!";
    }

    private String generatePassword(String[] args) {
        return "Undefined yet!";
    }

    private String addPassword(String[] args) {
        return "Undefined yet!";
    }

    private String removePassword(String[] args) {
        return "Undefined yet!";
    }

    private String disconnect() {
        return "Undefined yet!";
    }

    private String help() {
        return "Undefined yet!";
    }

}

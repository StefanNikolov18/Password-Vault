package password.vault.server.handler;

import password.vault.server.algorithm.AES;
import password.vault.server.algorithm.SymmetricBlockCipher;
import password.vault.server.auth.repository.UserRepository;
import password.vault.server.exception.CipherException;
import password.vault.server.util.SecretKeyGenerator;
import password.vault.server.util.Sha256Hashing;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class CommandHandler {
    private boolean isLogged = false;
    private String currentUser = null;

    private UserRepository userRepository;
    private SymmetricBlockCipher cipher;

    public CommandHandler(UserRepository userRepository) throws NoSuchAlgorithmException {
        this.userRepository = userRepository;
        this.cipher = new AES(SecretKeyGenerator.generateSecretKey());
    }

    public String execute(String commandLine) throws CipherException {
        if (commandLine == null || commandLine.isBlank()) {
            return "Invalid command line! Type help for more information.";
        }

        String[] args = commandLine.split(" ");
        String cmd = args[0];

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
        return "Undefined yet!";
    }

    private String login(String[] args) throws CipherException {
        if (args.length != 2) {
            return "Invalid command line! Must give username and password to login! Type help for more information.";
        } else if (isLogged) {
            return "Already logged in!";
        }

        String givenUsername = args[0];
        String givenPassword = args[1];

        if (!userRepository.usernameExists(givenUsername)) {
            return "Invalid username or password! Please try again.";
        }

        String hashedGivenPassword = Sha256Hashing.hashing(givenPassword);

        if (!hashedGivenPassword.equals(userRepository.getHashedPassword(givenUsername))) {
            return "Invalid username or password! Please try again.";
        }

        isLogged = true;
        currentUser = givenUsername;

        return "Login successful!";
    }

    private String logout(String[] args) {
        return "Undefined yet!";
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

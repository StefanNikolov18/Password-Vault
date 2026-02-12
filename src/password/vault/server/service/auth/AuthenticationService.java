package password.vault.server.service.auth;

import password.vault.server.command.CommandResult;
import password.vault.server.repository.UserRepository;
import password.vault.server.algorithm.hashing.Sha256Hashing;

import java.io.IOException;

// for login and register
public class AuthenticationService {

    private final UserRepository userRepository;

    public AuthenticationService(UserRepository userRepository) {
        if (userRepository == null) {
            throw new IllegalArgumentException("userRepository cannot be null!");
        }
        this.userRepository = userRepository;
    }

    public CommandResult register(String username, String password) {
        if (null == username || null == password) {
            throw new IllegalArgumentException("username or password is null in register!");
        }

        if (userRepository.usernameExists(username)) {
            return new CommandResult( null,
                    "Username already exists! Please choose another one.");
        }

        String hashGivenPassword = Sha256Hashing.hashing(password);
        try {
            userRepository.addNewUser(username, hashGivenPassword); //adding new user in the system
        } catch (IOException e) {   //error
            return new CommandResult(null,
                    "Server error occurred while registering.");
        }

        return new CommandResult( username,
                "Registration successful!");
    }

    public CommandResult login(String username, String password) {
        if (username == null || password == null) {
            throw new IllegalArgumentException("username or password is null in login!");
        }

        if (!userRepository.usernameExists(username)) {
            return new CommandResult( null,
                    "Invalid username or password! Please try again.");
        }

        String hashedGivenPassword = Sha256Hashing.hashing(password);

        if ( !hashedGivenPassword.equals(userRepository.getHashedPassword(username))) {
            return new CommandResult( null,
                    "Invalid username or password! Please try again.");
        }

        return new CommandResult( username,
                "Login successful");
    }
}

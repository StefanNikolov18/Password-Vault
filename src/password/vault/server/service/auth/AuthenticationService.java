package password.vault.server.service.auth;

import password.vault.server.repository.UserRepository;
import password.vault.server.util.Sha256Hashing;

// for login and register
public class AuthenticationService {

    private static final int REGISTER_NEEDED_ARGUMENTS = 3;
    private static final int LOGIN_NEEDED_ARGUMENTS = 2;

    private final UserRepository userRepository;

    public AuthenticationService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public AuthenticationResult register(String[] args) {
        if (args.length != REGISTER_NEEDED_ARGUMENTS) {
            return new AuthenticationResult(false, null,
                    "Invalid command line arguments! Must be given register <username>" +
                            " <password> <repeat-password>! Type help for more information.");
        } else if (!args[1].equals(args[2])) {
            return new AuthenticationResult(false, null,
                    "Passwords don't match! Please try again.");
        } else if (userRepository.usernameExists(args[0])) {
            return new AuthenticationResult(false, null,
                    "Username already exists! Please choose another one.");
        }

        String givenUsername = args[0];

        String hashGivenPassword = Sha256Hashing.hashing(args[1]);

        userRepository.addNewUser(givenUsername, hashGivenPassword); //adding new user

        return new AuthenticationResult(true, givenUsername,
                "Registration successful. You are logged in.");
    }

    public AuthenticationResult login(String[] args) {
        if (args.length != LOGIN_NEEDED_ARGUMENTS) {
            return new AuthenticationResult(false, null,
                    "Invalid command line! Must be given login " +
                            "<username> <password> to login! Type help for more information.");
        }

        String givenUsername = args[0];
        String givenPassword = args[1];

        if (!userRepository.usernameExists(givenUsername)) {
            return new AuthenticationResult(false, null,
                    "Invalid username or password! Please try again.");
        }

        String hashedGivenPassword = Sha256Hashing.hashing(givenPassword);

        if ( !hashedGivenPassword.equals(userRepository.getHashedPassword(givenUsername))) {
            return new AuthenticationResult(false, null,
                    "Invalid username or password! Please try again.");
        }

        return new AuthenticationResult(true, givenUsername,
                "Login successful");
    }
}

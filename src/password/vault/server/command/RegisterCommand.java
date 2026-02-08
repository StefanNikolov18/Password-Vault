package password.vault.server.command;

import password.vault.server.service.auth.AuthenticationService;

public class RegisterCommand implements Command {
    private static final int REGISTER_NEEDED_ARGUMENTS = 3;

    private final AuthenticationService authService;

    public RegisterCommand(AuthenticationService authService) {
        if (authService == null) {
            throw new IllegalArgumentException("authService must not be null!");
        }
        this.authService = authService;
    }

    @Override
    public CommandResult execute(String[] args, String currentUser) {
        if (args == null) {
            throw new IllegalArgumentException("args must not be null!");
        }

        if (currentUser != null) {
            return new CommandResult(null,
                    "Already logged in as " + currentUser + "!");
        }

        if (args.length != REGISTER_NEEDED_ARGUMENTS) {
            return new CommandResult(null,
                    "Invalid command line arguments! Must be given register <username>" +
                            " <password> <repeat-password>! Type help for more information.");
        } else if (!args[1].equals(args[2])) {
            return new CommandResult(null,
                    "Passwords don't match! Please try again.");
        }

        String username = args[0];
        String password = args[1];

        return authService.register(username, password);
    }
}

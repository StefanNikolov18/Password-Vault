package password.vault.server.command.unauthenticated;

import password.vault.server.command.Command;
import password.vault.server.command.CommandResult;
import password.vault.server.service.auth.AuthenticationService;

public class LoginCommand implements Command {
    private static final int LOGIN_NEEDED_ARGUMENTS = 2;

    private final AuthenticationService authenticationService;

    public LoginCommand(AuthenticationService authenticationService) {
        if (authenticationService == null) {
            throw new IllegalArgumentException("AuthenticationService cannot be null!");
        }
        this.authenticationService = authenticationService;
    }

    @Override
    public CommandResult execute(String[] args, String currentUser) {
        if (args == null) {
            throw new IllegalArgumentException("Arguments cannot be null!");
        }
        if (currentUser != null) {
            return new CommandResult(null,
                    "Already logged in as " + currentUser + "!");
        }

        if (args.length != LOGIN_NEEDED_ARGUMENTS) {
            return new CommandResult( null,
                    "Invalid command line! Must be given login " +
                            "<username> <password> to login! ");
        }

        String username = args[0];
        String password = args[1];

        return authenticationService.login(username, password);
    }
}

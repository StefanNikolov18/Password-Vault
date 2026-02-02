package password.vault.server.command;

import password.vault.server.service.auth.AuthenticationService;

public class LoginCommand implements Command {
    private AuthenticationService authenticationService;

    public LoginCommand(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @Override
    public CommandResult execute(String[] args, String currentUser) {
        if (currentUser != null) {
            return new CommandResult(null,
                    "Already logged in as " + currentUser + "!");
        }

        return authenticationService.login(args);
    }
}

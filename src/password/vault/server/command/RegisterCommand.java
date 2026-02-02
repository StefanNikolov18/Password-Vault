package password.vault.server.command;

import password.vault.server.service.auth.AuthenticationService;

public class RegisterCommand implements Command {
    private AuthenticationService authService;

    public RegisterCommand(AuthenticationService authService) {
        this.authService = authService;
    }

    @Override
    public CommandResult execute(String[] args, String currentUser) {
        if (currentUser != null) {
            return new CommandResult(null,
                    "Already logged in as " + currentUser + "!");
        }

        return authService.register(args);
    }
}

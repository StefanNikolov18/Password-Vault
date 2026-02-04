package password.vault.server.command;

import password.vault.server.service.vault.VaultService;

public class RetrieveCredentialCommand implements Command {
    private static final int RETRIEVE_CREDENTIAL_NEEDED_ARGUMENTS = 2;

    private final VaultService vaultService;

    public RetrieveCredentialCommand(VaultService vaultService) {
        this.vaultService = vaultService;
    }

    @Override
    public CommandResult execute(String[] args, String currentUser) {
        if (currentUser == null) {
            return new CommandResult(null, "You need to login first!");
        }

        if (args.length != RETRIEVE_CREDENTIAL_NEEDED_ARGUMENTS) {
            return new CommandResult(currentUser, "Invalid arguments! "
                    + "Must be given retrieve-credential <website> <username>"
                    + "Please try again.");
        }

        String website = args[0];
        String usernameSite = args[1];

        return vaultService.retrieveCredentials(website, usernameSite , currentUser);
    }
}

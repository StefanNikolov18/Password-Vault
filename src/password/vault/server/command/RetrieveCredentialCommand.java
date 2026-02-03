package password.vault.server.command;

import password.vault.server.service.vault.VaultService;

public class RetrieveCredentialCommand implements Command {
    private final VaultService vaultService;

    public RetrieveCredentialCommand(VaultService vaultService) {
        this.vaultService = vaultService;
    }

    @Override
    public CommandResult execute(String[] args, String currentUser) {
        if (currentUser == null) {
            return new CommandResult(null, "You need to login first!");
        }

        return vaultService.retrieveCredentials(args, currentUser);
    }
}

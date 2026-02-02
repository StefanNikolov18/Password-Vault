package password.vault.server.command;

import password.vault.server.service.vault.VaultService;

public class RetrieveCredentialCommand implements Command {
    private VaultService vaultService;

    public RetrieveCredentialCommand(VaultService vaultService) {
        this.vaultService = vaultService;
    }

    @Override
    public CommandResult execute(String[] args, String currentUser) {
        return null;
    }
}

package password.vault.server.command;

import password.vault.server.service.vault.VaultService;

public class GeneratePasswordCommand implements Command {
    private VaultService vaultService;

    public GeneratePasswordCommand(VaultService vaultService) {
        this.vaultService = vaultService;
    }

    @Override
    public CommandResult execute(String[] args, String currentUser) {
        return null;
    }
}

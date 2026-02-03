package password.vault.server.command;

import password.vault.server.service.vault.VaultService;

public class RemovePasswordCommand implements Command {
    private VaultService vaultService;

    public RemovePasswordCommand(VaultService vaultService) {
        this.vaultService = vaultService;
    }

    @Override
    public CommandResult execute(String[] args, String currentUser) {
        if (currentUser == null) {
            return new CommandResult(null, "You need to login first!");
        }

        return null;
    }
}

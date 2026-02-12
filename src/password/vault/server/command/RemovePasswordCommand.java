package password.vault.server.command;

import password.vault.server.service.vault.VaultService;

public class RemovePasswordCommand implements Command {
    private static final int REMOVE_PASSWORD_NEEDED_ARGUMENTS = 2;

    private final VaultService vaultService;

    public RemovePasswordCommand(VaultService vaultService) {
        if (vaultService == null) {
            throw new IllegalArgumentException("VaultService cannot be null!");
        }
        this.vaultService = vaultService;
    }

    @Override
    public CommandResult execute(String[] args, String currentUser) {
        if (args == null) {
            throw new IllegalArgumentException("Args cannot be null!");
        }
        if (currentUser == null) {
            return new CommandResult(null, "You need to login first!");
        }

        if (args.length != REMOVE_PASSWORD_NEEDED_ARGUMENTS) {
            return new CommandResult(currentUser, "Invalid arguments! "
                    + "Must be given remove-password <website> <username>!");
        }

        String website = args[0];
        String usernameSite = args[1];

        return vaultService.removePassword(website, usernameSite, currentUser);
    }
}

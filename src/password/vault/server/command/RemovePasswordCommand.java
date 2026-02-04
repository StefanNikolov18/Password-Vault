package password.vault.server.command;

import password.vault.server.service.vault.VaultService;

public class RemovePasswordCommand implements Command {
    private static final int REMOVE_PASSWORD_NEEDED_ARGUMENTS = 2;

    private final VaultService vaultService;

    public RemovePasswordCommand(VaultService vaultService) {
        this.vaultService = vaultService;
    }

    @Override
    public CommandResult execute(String[] args, String currentUser) {
        if (currentUser == null) {
            return new CommandResult(null, "You need to login first!");
        }

        if (args.length != REMOVE_PASSWORD_NEEDED_ARGUMENTS) {
            return new CommandResult(currentUser, "Invalid arguments! "
                    + "Must be given remove-password <website> <username>"
                    + "Type help for more information.");
        }

        String website = args[0];
        String usernameSite = args[1];

        return vaultService.removePassword(website, usernameSite, currentUser);
    }
}

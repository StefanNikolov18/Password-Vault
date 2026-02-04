package password.vault.server.command;

import password.vault.server.service.vault.VaultService;

public class AddPasswordCommand implements Command {
    private static final int ADD_PASSWORD_NEEDED_ARGUMENTS = 3;

    private final VaultService vaultService;

    public AddPasswordCommand(VaultService vaultService) {
        this.vaultService = vaultService;
    }

    @Override
    public CommandResult execute(String[] args, String currentUser) {
        if (currentUser == null) {
            return new CommandResult(null, "You need to login first!");
        }

        if (args.length != ADD_PASSWORD_NEEDED_ARGUMENTS) {
            return new CommandResult(currentUser,
                    "Invalid arguments fo add-password <website> <username> <password>!"
                            + "Please try again.");
        }

        String website = args[0];
        String usernameSite = args[1];
        String password = args[2];

        return vaultService.addPassword(website, usernameSite, password, currentUser);
    }
}

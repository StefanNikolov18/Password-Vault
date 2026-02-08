package password.vault.server.command;

import password.vault.server.service.vault.VaultService;

public class GeneratePasswordCommand implements Command {
    private static final int GENERATE_PASSWORD_NEEDED_ARGUMENTS = 2;

    private final VaultService vaultService;

    public GeneratePasswordCommand(VaultService vaultService) {
        if (vaultService == null) {
            throw new IllegalArgumentException("VaultService cannot be null!");
        }
        this.vaultService = vaultService;
    }

    @Override
    public CommandResult execute(String[] args, String currentUser) {
        if (args == null) {
            throw new IllegalArgumentException("Arguments cannot be null!");
        }
        if (currentUser == null) {
            return new CommandResult(null, "You need to login first!");
        }

        if (args.length !=  GENERATE_PASSWORD_NEEDED_ARGUMENTS) {
            return new CommandResult(currentUser,
                    "Invalid arguments! "
                            + "Must be given generate-password <website> <username>"
                            + "Please try again.");
        }

        String website = args[0];
        String usernameSite = args[1];

        return vaultService.generatePassword(website, usernameSite, currentUser);
    }
}

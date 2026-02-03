package password.vault.server.service.vault;

import password.vault.server.algorithm.cipher.CipherFactory;
import password.vault.server.algorithm.cipher.SymmetricBlockCipher;
import password.vault.server.command.CommandResult;
import password.vault.server.exception.CipherException;
import password.vault.server.repository.UserRepository;
import password.vault.server.utils.KeyLoaderSingleton;

import javax.crypto.SecretKey;
import java.io.IOException;

// for add/retrieve/remove/generate passwords
public class VaultService {
    private static final int RETRIEVE_CREDENTIAL_NEEDED_ARGUMENTS = 2;
    private static final int ADD_PASSWORD_NEEDED_ARGUMENTS = 3;

    private UserRepository userRepository;
    private SymmetricBlockCipher cipher;
    private VaultRepository vaultRepo;

    public VaultService(UserRepository userRepository) throws IOException {
        this.userRepository = userRepository;

        SecretKey key = KeyLoaderSingleton.getInstance().getSecretKey();
        this.cipher =  CipherFactory.getCipher("AES", key);

        vaultRepo = new VaultRepository();
    }

    public CommandResult retrieveCredentials(String[] args, String currentUser) {
        if (args.length != RETRIEVE_CREDENTIAL_NEEDED_ARGUMENTS) {
            return new CommandResult(currentUser, "Invalid arguments! "
                    + "Must be given retrieve-credential <website> <username>"
                    + "Please try again.");
        }

        String website = args[0];
        String usernameSite = args[1];
        try {
            VaultResponse vaultResult = vaultRepo.getDecryptedPasswordForWebsite(website, usernameSite, currentUser);
            if (!vaultResult.success()) {
                return new CommandResult(currentUser, vaultResult.message());
            }

            String encryptedPassword = vaultResult.message();
            String decryptedPassword = cipher.decrypt(encryptedPassword);

            return new CommandResult(currentUser, decryptedPassword);
        } catch (IOException | CipherException e) {
            return new CommandResult(currentUser, "Problem finding your password for " + website);
        }

    }

    public CommandResult addPassword(String[] args, String currentUser) {
        if (args.length != ADD_PASSWORD_NEEDED_ARGUMENTS) {
            return new CommandResult(currentUser,
                   "Invalid arguments fo add-password <website> <username>!"
                            + "Please try again.");
        }

        String website = args[0];
        String usernameSite = args[1];
        String password = args[2];

        // check password with API

        try {
            String cryptedPassword = cipher.encrypt(password);
            VaultResponse vaultResult = vaultRepo.addNewPasswordForWebsite(website, usernameSite, cryptedPassword, currentUser);

            return new CommandResult(currentUser, vaultResult.message());

        } catch ( IOException | CipherException ex) {
            return new CommandResult(currentUser, "Problem adding your password for " + website);
        }

    }

}

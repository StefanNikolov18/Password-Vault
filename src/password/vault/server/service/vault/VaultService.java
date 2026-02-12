package password.vault.server.service.vault;

import password.vault.server.algorithm.cipher.CipherFactory;
import password.vault.server.algorithm.cipher.SymmetricBlockCipher;
import password.vault.server.command.CommandResult;
import password.vault.server.exception.CipherException;
import password.vault.server.exception.EnzoicPasswordClientException;
import password.vault.server.integration.enzoic.EnzoicPasswordClient;
import password.vault.server.integration.enzoic.EnzoicPasswordResponse;
import password.vault.server.utils.SecretKeyLoaderSingleton;
import password.vault.server.utils.PasswordGenerator;

import java.io.IOException;

// for add/retrieve/remove/generate passwords
public class VaultService {

    private final SymmetricBlockCipher cipher;

    private final VaultRepository vaultRepo;
    private final EnzoicPasswordClient enzoicClient;

    public VaultService() throws IOException {
        this(new VaultRepository(),
                new EnzoicPasswordClient(),
                CipherFactory.getCipher(
                        "AES",
                        SecretKeyLoaderSingleton.getInstance().getSecretKey())
        );
    }

    VaultService(VaultRepository repo,
                 EnzoicPasswordClient client,
                 SymmetricBlockCipher cipher) {
        this.vaultRepo = repo;
        this.enzoicClient = client;
        this.cipher = cipher;
    }

    public CommandResult retrieveCredentials(
            String website, String usernameSite, String currentUser) {
        if (website == null || usernameSite == null || currentUser == null) {
            throw new IllegalArgumentException("website or usernameSite or currentUser is null");
        }

        try {
            VaultResponse vaultResult = vaultRepo.getDecryptedPasswordForWebsite(
                    website, usernameSite, currentUser);
            if (!vaultResult.success()) {
                return new CommandResult(currentUser, vaultResult.message());
            }

            String encryptedPassword = vaultResult.message();
            String decryptedPassword = cipher.decrypt(encryptedPassword);

            return new CommandResult(currentUser, decryptedPassword);
        } catch (IOException | CipherException e) {
            throw new RuntimeException("Failed to retrieve credentials for " + currentUser, e);
        }

    }

    public CommandResult addPassword(
            String website,
            String  usernameSite,
            String password,
            String currentUser) {

        if (website == null || usernameSite == null ||
                password == null || currentUser == null) {
            throw new IllegalArgumentException("Invalid arguments in addPassword!");
        }

        try {
            // check password with Enzoic password API
            EnzoicPasswordResponse response = enzoicClient.getResponse(password);
            if (response.revealedInExposure()) {            //true == isWeak
                return new CommandResult(currentUser,
                        "Password is too week and is revealed in exposure!");
            }

            String cryptedPassword = cipher.encrypt(password);
            VaultResponse vaultResult = vaultRepo.addNewPasswordForWebsite(
                    website, usernameSite, cryptedPassword, currentUser);

            return new CommandResult(currentUser, vaultResult.message());

        } catch ( IOException | CipherException | EnzoicPasswordClientException ex) {
            throw new RuntimeException("Failed to add password for " + currentUser, ex);
        }

    }

    public CommandResult generatePassword(
            String website, String usernameSite, String currentUser) {
        if (usernameSite == null || website == null || currentUser == null) {
            throw new IllegalArgumentException("Null arguments in generatePassword!");
        }

        try {
            String genPassword = PasswordGenerator.generatePassword(enzoicClient); // throws
            String cryptedPassword = cipher.encrypt(genPassword);
            VaultResponse vaultResult = vaultRepo.addNewPasswordForWebsite(
                    website, usernameSite, cryptedPassword, currentUser);

            return new CommandResult(currentUser, genPassword);
        } catch (EnzoicPasswordClientException | CipherException | IOException ex) {
            throw new RuntimeException("Failed to generate password for " + currentUser, ex);
        }

    }

    public CommandResult removePassword(
            String website, String usernameSite, String currentUser) {
        if (usernameSite == null || website == null || currentUser == null) {
            throw new IllegalArgumentException("Null arguments in removePassword!");
        }

        try {
            VaultResponse result = vaultRepo.removePassword(website, usernameSite, currentUser);

            return new CommandResult(currentUser, result.message());
        } catch (IOException ex) {
            throw new RuntimeException("Failed to remove password for " + currentUser, ex);
        }
    }

}

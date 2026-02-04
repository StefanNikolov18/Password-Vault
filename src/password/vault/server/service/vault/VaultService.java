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

import javax.crypto.SecretKey;
import java.io.IOException;

// for add/retrieve/remove/generate passwords
public class VaultService {

    private SymmetricBlockCipher cipher;

    private VaultRepository vaultRepo;
    private EnzoicPasswordClient enzoicClient;

    public VaultService() throws IOException {

        SecretKey key = SecretKeyLoaderSingleton.getInstance().getSecretKey();
        this.cipher =  CipherFactory.getCipher("AES", key);

        vaultRepo = new VaultRepository();
        enzoicClient = new EnzoicPasswordClient();
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
            return new CommandResult(currentUser, "Problem finding your password for " + website);
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
            return new CommandResult(currentUser,
                    "Problem adding your password for " + website);
        }

    }

    public CommandResult generatePassword(
            String website, String usernameSite, String currentUser) {
        if (usernameSite == null || website == null || currentUser == null) {
            throw new IllegalArgumentException("Null arguments in generatePassword!");
        }

        try {
            String genPassword = PasswordGenerator.generatePassword();
            String cryptedPassword = cipher.encrypt(genPassword);
            VaultResponse vaultResult = vaultRepo.addNewPasswordForWebsite(
                    website, usernameSite, cryptedPassword, currentUser);

            return new CommandResult(currentUser, genPassword);
        } catch (CipherException | IOException ex) {
            return new CommandResult(currentUser,
                    "Problem generating your password for " + website);
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
            return new CommandResult(currentUser, "Problem removing your password for " + website);
        }
    }

}

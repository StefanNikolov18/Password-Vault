package password.vault.server.service.vault;

import password.vault.server.algorithm.CipherFactory;
import password.vault.server.algorithm.SymmetricBlockCipher;
import password.vault.server.repository.UserRepository;

import javax.crypto.SecretKey;
import java.io.IOException;

// for add/retrieve/remove/generate passwords
public class VaultService {
    private static final int RETRIEVE_CREDENTIAL_NEEDED_ARGUMENTS = 2;


    private UserRepository userRepository;
    private SymmetricBlockCipher cipher;
    private VaultRepository vaultRepo;

    public VaultService(UserRepository userRepository) throws IOException {
        this.userRepository = userRepository;

        SecretKey key = KeyLoaderSingleton.getInstance().getSecretKey();
        this.cipher =  CipherFactory.getCipher("AES", key);

        vaultRepo = new VaultRepository();
    }

   /* public String retrieveCredentials(String[] args, String currentUser) {
        if (args.length != RETRIEVE_CREDENTIAL_NEEDED_ARGUMENTS) {
            return "Invalid command line arguments! " +
                            "Must be given retrieve-credential <website> <username>" +
                            "Please try again.";
        }

        String website = args[0];

        String username = args[1];
        if (!username.equals(currentUser)) {
            return "Invalid username given!";
        }

        String encryptedPassword = VaultRepository.getEncryptedPassword(website, currentUser);

        if (encryptedPassword == null) {
            return "No credentials found for website: " + website;
        }

       String decryptedPassword =
        return "";
    }*/

}

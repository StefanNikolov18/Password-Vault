package password.vault.server.service.vault;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

// for reading from vault files
public class VaultRepository {
    private static final String VAULT_DIR_PATH = "data/vault/";

    public VaultResponse getDecryptedPasswordForWebsite(String website, String usernameSite, String currentUser) throws IOException {
        if (currentUser == null || website == null) {
            throw  new IllegalArgumentException("CurrentUser and Website are mandatory!");
        }

        List<String> vaultUser =  getFromVaultFile(currentUser);
        if (vaultUser == null) {
            return new VaultResponse(false, "Server problem while reading vault file!");
        }

        String decryptedPasswordForWebsite = vaultUser.stream()
                .filter(line -> line.startsWith(website + " "))
                .filter(line -> line.contains(usernameSite))
                .map(line -> line.split(" ")[2])
                .findFirst().orElse(null);

        if (decryptedPasswordForWebsite == null) {
            return new VaultResponse(false, "No website found with " + usernameSite + "!");
        }

        return new VaultResponse(true, decryptedPasswordForWebsite);
    }

    private List<String> getFromVaultFile(String filename) {
        List<String> list = new ArrayList<>();

        String pathname = VAULT_DIR_PATH + filename + ".vault";
        File file = new File(pathname);

        if (!file.exists()) {
            System.out.println("Vault file does not exist: " + filename);
            return null;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                list.add(line);
            }
        } catch (IOException e) {
            System.err.println("Error reading vault file: " + e.getMessage());
            return null;
        }

        return list;
    }

    public VaultResponse addNewPasswordForWebsite(
            String website,
            String usernameWebsite,
            String cryptedPassword,
            String currentUser) throws IOException {

        if (currentUser == null || website == null ||  usernameWebsite == null || cryptedPassword == null) {
            throw  new IllegalArgumentException("CurrentUse, Website, password, usernameWebsite are mandatory!");
        }

        // append new information for <currentUser>.vault
        final String path = VAULT_DIR_PATH + currentUser + ".vault";
        try (FileWriter fw = new FileWriter(path, true)) { // 'true' = append
            fw.write(website + " " + usernameWebsite + " " + cryptedPassword + System.lineSeparator());
        }

        return new VaultResponse(true,
                "Added new password for website: " + website +
                        "with username: " + usernameWebsite);
    }
}

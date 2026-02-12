package password.vault.server.service.vault;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

// for reading from vault files
public class VaultRepository {
    private static final String VAULT_DIR_PATH = "data/vault/";

    public VaultResponse getDecryptedPasswordForWebsite(
            String website,
            String usernameSite,
            String currentUser) throws IOException {
        if (currentUser == null || website ==  null || usernameSite == null) {
            throw  new IllegalArgumentException("CurrentUser and Website are mandatory!");
        }
        List<String> vaultUser;
        try {
            vaultUser =  getFromVaultFile(currentUser);
        } catch (IOException e) {
            throw  new IOException("Error while reading vault file for " + currentUser + "!", e);
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

    List<String> getFromVaultFile(String filename) throws IOException {
        List<String> list = new ArrayList<>();

        String pathname = VAULT_DIR_PATH + filename + ".vault";
        File file = new File(pathname);

        if (!file.exists()) {
            throw new IOException("File " + pathname + " doesn't exist!");
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                list.add(line);
            }
        }

        return list;
    }

    public VaultResponse addNewPasswordForWebsite(
            String website,
            String usernameWebsite,
            String cryptedPassword,
            String currentUser) throws IOException {

        if (currentUser == null
                || website == null
                ||  usernameWebsite == null
                || cryptedPassword == null) {
            throw  new IllegalArgumentException("CurrentUse, Website, password, usernameWebsite are mandatory!");
        }

        // append new information for <currentUser>.vault
        final String path = VAULT_DIR_PATH + currentUser + ".vault";
        try (FileWriter fw = new FileWriter(path, true)) { // 'true' = append
            fw.write(website + " " + usernameWebsite + " " + cryptedPassword + System.lineSeparator());
        }

        return new VaultResponse(true,
                "Added new password for website: " + website +
                        " with username: " + usernameWebsite);
    }

    public VaultResponse removePassword(String website,
                                        String usernameWebsite,
                                        String currentUser) throws IOException {
        if (currentUser == null || website == null ||  usernameWebsite == null) {
            throw  new IllegalArgumentException("CurrentUse, Website, password are mandatory!");
        }
        List<String> vaultUser;
        try {
            vaultUser = getFromVaultFile(currentUser);

        } catch (IOException e) {
            throw  new IOException("Error while reading vault file!", e);
        }

        int size = vaultUser.size();
        List<String> result = vaultUser.stream() //remove line with website and username
                .filter(line -> (!line.startsWith(website) || !line.contains(usernameWebsite)))
                .toList();
        if (result.size() == size) {
            return new VaultResponse(false, "No password found!");
        }

        final String path = VAULT_DIR_PATH + currentUser + ".vault";
        try (FileWriter fw = new FileWriter(path)) {         // append == false
            for (String line: result) {
                fw.write(line +  System.lineSeparator());
            }
        }

        return new VaultResponse(true, "Password successfully removed from vault!");
    }
}

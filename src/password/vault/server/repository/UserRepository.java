package password.vault.server.repository;

import password.vault.server.repository.loader.UserFileLoader;
import password.vault.server.repository.loader.UserLoader;

import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.Map;

public class UserRepository {
    private static final String USERS_PATH = "data/users.db";
    private static final String VAULT_DIR_PATH = "data/vault/";

    private Map<String, String> users = new HashMap<>();

    public UserRepository(InputStream userInputStream) throws IOException {
        try {
            UserLoader loader = new UserFileLoader(userInputStream);
            this.users = loader.load();
        } catch (IOException e) {
            throw new IOException("Failed to load users database", e);
        }
        System.out.println("Users are loaded from database. -> " + users.size() + " users.");
    }

    public boolean usernameExists(String username) {
        return this.users.containsKey(username);
    }

    public String getHashedPassword(String username) {
        return this.users.get(username);
    }

    // synchronized section for multiple clients
    public synchronized void addNewUser(String username, String hashPassword) throws IOException {
        this.users.put(username, hashPassword);

        saveInDataBase(username, hashPassword);
        createVaultFile(username);
    }

    void saveInDataBase(String username, String hashPassword) throws IOException {
        String newUserLine = username + ":" + hashPassword;
        try (FileWriter fw = new FileWriter(USERS_PATH, true)) { // 'true' = append
            fw.write(newUserLine + System.lineSeparator());
            System.out.println("User appended successfully.");
        }
    }

    void createVaultFile(String username) throws IOException {
        String newFileName = username + ".vault";
        Path path = Path.of(VAULT_DIR_PATH, newFileName);

        if (Files.notExists(path)) {
            Files.createFile(path);
            System.out.println("Vault file created: " + path.toAbsolutePath());
        }

        Files.writeString(
                path,
                "",                         // initial content
                StandardOpenOption.CREATE,
                StandardOpenOption.WRITE
        );

    }

}

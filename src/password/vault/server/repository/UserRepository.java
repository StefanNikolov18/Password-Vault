package password.vault.server.repository;

import password.vault.server.repository.loader.UserFileLoader;
import password.vault.server.repository.loader.UserLoader;

import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class UserRepository {

    private Map<String, String> users = new HashMap<>();

    public UserRepository(InputStream userInputStream) throws IOException {
        UserLoader loader = new UserFileLoader(userInputStream);
        this.users = loader.load();
        System.out.println("Users are loaded from database. -> " + users.size() + " users.");
    }

    public boolean usernameExists(String username) {
        return this.users.containsKey(username);
    }

    public String getHashedPassword(String username) {
        return this.users.get(username);
    }

    public void addNewUser(String username, String hashPassword) {
        this.users.put(username, hashPassword);
        String newUserLine = username + ":" + hashPassword;
        try (FileWriter fw = new FileWriter("data/users.db", true)) { // 'true' = append
            fw.write(newUserLine + System.lineSeparator());  // добавяме нов ред
            System.out.println("User appended successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

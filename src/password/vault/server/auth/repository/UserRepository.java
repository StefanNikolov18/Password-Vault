package password.vault.server.auth.repository;

import password.vault.server.auth.loader.UserFileLoader;
import password.vault.server.auth.loader.UserLoader;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class UserRepository {

    private Map<String, String> users = new HashMap<>();

    public UserRepository(InputStream userInputStream) throws IOException {
        UserLoader loader = new UserFileLoader(userInputStream);
        this.users = loader.load();
    }

    public boolean usernameExists(String username) {
        return this.users.containsKey(username);
    }

    public String getHashedPassword(String username) {
        return this.users.get(username);
    }
}

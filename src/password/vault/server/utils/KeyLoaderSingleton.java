package password.vault.server.utils;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;

public class KeyLoaderSingleton {
    private static final String SECRET_KEY_PATH = "data/secret.key";

    private static KeyLoaderSingleton instance;
    private SecretKey secretKey;

    private KeyLoaderSingleton() throws IOException {
        String base64Key = new String(Files.readAllBytes(Paths.get(SECRET_KEY_PATH)));
        byte[] decodedKey = Base64.getDecoder().decode(base64Key);

        this.secretKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");
    }

    public static synchronized KeyLoaderSingleton getInstance() throws IOException {
        if (instance == null) {
            instance = new KeyLoaderSingleton();
        }
        return instance;
    }

    public SecretKey getSecretKey() {
        return secretKey;
    }

}

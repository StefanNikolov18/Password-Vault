package password.vault.server.provider;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class SecretKeyProvider {
    private final SecretKey secretKey;

    public SecretKeyProvider() {
        String key = System.getenv("SECRET_KEY");

        if (key == null || key.isBlank()) {
            throw new IllegalArgumentException("SECRET_KEY not set");
        }

        byte[] decoded = Base64.getDecoder().decode(key);
        this.secretKey = new SecretKeySpec(decoded, 0, decoded.length, "AES");
    }

    public SecretKey getSecretKey() {
        return secretKey;
    }
}

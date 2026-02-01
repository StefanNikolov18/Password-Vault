package password.vault.server.util;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
//import java.util.Base64;

public class SecretKeyGenerator {
    private static final String ENCRYPTION_ALGORITHM = "AES";
    private static final int KEY_SIZE_IN_BITS = 256;

    public static SecretKey generateSecretKey() throws NoSuchAlgorithmException {

        KeyGenerator keyGenerator = KeyGenerator.getInstance(ENCRYPTION_ALGORITHM);
        keyGenerator.init(KEY_SIZE_IN_BITS);
        SecretKey secretKey = keyGenerator.generateKey();

        // In order to view the key in some text representation, we'll convert it to Base64 format
        // Comment the next two lines if that's not needed
        //String base64Key = Base64.getEncoder().encodeToString(secretKey.getEncoded());
        //System.out.println("Generated Secret Key (Base64-encoded): " + base64Key);

        return secretKey;
    }
}

package password.vault.server.algorithm.cipher;

import javax.crypto.SecretKey;

public class CipherFactory {

    public static SymmetricBlockCipher getCipher(String algorithm, SecretKey key) {
        return switch (algorithm.toUpperCase()) {
            case "AES" -> new AES(key);
            default -> throw new IllegalArgumentException("Unknown cipher algorithm: " + algorithm);
        };
    }

}

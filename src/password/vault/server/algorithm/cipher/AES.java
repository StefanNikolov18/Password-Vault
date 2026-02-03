package password.vault.server.algorithm.cipher;

import password.vault.server.exception.CipherException;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class AES implements SymmetricBlockCipher {
    private final SecretKey secretKey;

    /**
     * Encrypts/decrypts data using AES (Rijndael) algorithm with the provided secret key.
     *
     * @param secretKey the encryption/decryption key
     * @throws IllegalArgumentException if secretKey is null
     */
    public AES(SecretKey secretKey) {
        if (secretKey == null) {
            throw new IllegalArgumentException("Secret key cannot be null.");
        }

        this.secretKey = secretKey;
    }

    /**
     * Encrypts a plain text string and returns a Base64 encoded cipher text.
     */
    public String encrypt(String plainText) throws CipherException {
        if (plainText == null) {
            throw new IllegalArgumentException("Input cannot be null");
        }

        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);

            byte[] encryptedBytes = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(encryptedBytes);

        } catch (Exception e) {
            throw new CipherException("Error during encryption", e);
        }
    }

    /**
     * Decrypts a Base64 encoded cipher text string and returns the plain text.
     */
    public String decrypt(String cipherText) throws CipherException {
        if (cipherText == null) {
            throw new IllegalArgumentException("Input cannot be null");
        }

        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);

            byte[] decodedBytes = Base64.getDecoder().decode(cipherText);
            byte[] decryptedBytes = cipher.doFinal(decodedBytes);

            return new String(decryptedBytes, StandardCharsets.UTF_8);

        } catch (Exception e) {
            throw new CipherException("Error during decryption", e);
        }
    }
}
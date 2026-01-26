package password.vault.algorithm;

import password.vault.exception.CipherException;

import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.SecretKey;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class AES {
    private static final int KILOBYTE = 1024;
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

    public void encrypt(InputStream inputStream, OutputStream outputStream)
            throws CipherException {

        if (inputStream == null || outputStream == null) {
            throw new IllegalArgumentException("Input and output stream parameters cannot be null.");
        }

        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);

            try (var cipherOutputStream = new CipherOutputStream(outputStream, cipher)) {
                byte[] buffer = new byte[KILOBYTE];
                int bytesRead;

                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    cipherOutputStream.write(buffer, 0, bytesRead);
                }
            }
        } catch (IOException e) {
            throw new CipherException("Error while IO.", e);
        } catch (Exception e) {
            throw new CipherException("Error during encryption occurred!", e);
        }

    }

    public void decrypt(InputStream inputStream, OutputStream outputStream)
            throws CipherException {

        if (inputStream == null || outputStream == null) {
            throw new IllegalArgumentException("Input and output stream parameters cannot be null.");
        }

        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);

            try (var cipherOut = new CipherOutputStream(outputStream, cipher)) {
                byte[] buffer = new byte[KILOBYTE];
                int bytesRead;

                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    cipherOut.write(buffer, 0, bytesRead);
                }
            }
        } catch (IOException e) {
            throw new CipherException("Error while IO.", e);
        } catch (Exception e) {
            throw new CipherException("Error during decryption", e);
        }
    }
}
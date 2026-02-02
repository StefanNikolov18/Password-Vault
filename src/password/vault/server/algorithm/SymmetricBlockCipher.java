package password.vault.server.algorithm;

import password.vault.server.exception.CipherException;

public interface SymmetricBlockCipher {

    /**
     * Encrypts the data from inputStream and puts it into outputStream
     *
     * @param plainText text to encrypt
     * @throws CipherException if the encrypt/decrypt operation cannot be completed successfully
     */
    String encrypt(String plainText) throws CipherException;

    /**
     * Decrypts the data from inputStream and puts it into outputStream
     *
     * @param plainText text to decript
     * @throws CipherException if the encrypt/decrypt operation cannot be completed successfully
     */
    String decrypt(String plainText) throws CipherException;
}

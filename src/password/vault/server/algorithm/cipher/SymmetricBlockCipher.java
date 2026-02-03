package password.vault.server.algorithm.cipher;

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
     * @param plainText text to decrypt
     * @throws CipherException if the encrypt/decrypt operation cannot be completed successfully
     */
    String decrypt(String plainText) throws CipherException;
}

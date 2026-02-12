package password.vault.server.algorithm.cipher;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import password.vault.server.exception.CipherException;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import static org.junit.jupiter.api.Assertions.*;

class AESTest {

    private SecretKey secretKey;
    private AES aes;

    @BeforeEach
    void setUp() throws Exception {
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(128);
        secretKey = keyGen.generateKey();

        aes = new AES(secretKey);
    }

    @Test
    void testConstructorNullSecretKeyThrowsException() {
        assertThrows(IllegalArgumentException.class,
                () -> new AES(null),
                "Secret key must not be null!");
    }

    @Test
    void testConstructorNotNullKeyDoesNotThrowsException(){
        assertDoesNotThrow(
                () ->  new AES(secretKey),
                "Secret key is not null and exception should not be thrown"
        );
    }

    @Test
    void testEncryptAndDecryptReturnsOriginalText()
            throws CipherException {
        String plainText = "HelloWorld123!";

        String cipherText = aes.encrypt(plainText);
        assertNotNull(cipherText,
                "Encrypted text should not be null");

        String decrypted = aes.decrypt(cipherText);
        assertEquals(plainText, decrypted,
                "Decrypted text should match the original");
    }

    @Test
    void testEncryptWithNullThrowsException() {
        assertThrows(IllegalArgumentException.class,
                () -> aes.encrypt(null),
                "Text cannot be null!");
    }

    @Test
    void testDecryptWithNullThrowsException() {
        assertThrows(IllegalArgumentException.class,
                () -> aes.decrypt(null),
                "Text cannot be null!");
    }


    @Test
    void testEncryptProducesDifferentFromPlainText()
            throws CipherException, CipherException {
        String plainText = "SecretMessage";

        String cipherText = aes.encrypt(plainText);

        assertNotEquals(plainText, cipherText,
                "Encrypted text should not equal the plain text");
    }

}

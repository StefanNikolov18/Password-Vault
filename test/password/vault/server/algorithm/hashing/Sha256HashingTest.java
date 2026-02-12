package password.vault.server.algorithm.hashing;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Sha256HashingTest {
    @Test
    void testHashingProduces64CharHex() {
        String password = "Password123!";
        String hash = Sha256Hashing.hashing(password);

        assertNotNull(hash, "Hash should not be null");
        assertEquals(64, hash.length(), "SHA-256 hash should be 64 characters");
    }

    @Test
    void testHashingWithEmptyString() {
        String password = "";
        String actualHash = Sha256Hashing.hashing(password);

        assertNotNull(actualHash, "Hash of empty string should not be null");
        assertEquals(64, actualHash.length(), "SHA-256 hash should be 64 hex characters");
    }

    @Test
    void testHashingWithNullThrowsException() {
        assertThrows(IllegalArgumentException.class,
                () -> Sha256Hashing.hashing(null),
                "Hashing null password should throw IllegalArgumentException!");
    }

    @Test
    void testHashingIsDeterministic() {
        String password = "deterministic";
        String hash1 = Sha256Hashing.hashing(password);
        String hash2 = Sha256Hashing.hashing(password);

        assertEquals(hash1, hash2, "Hashing should be deterministic for the same input!");
    }
}

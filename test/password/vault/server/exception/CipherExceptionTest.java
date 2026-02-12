package password.vault.server.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CipherExceptionTest {

    @Test
    void testConstructorWithMessage() {
        CipherException ex = new CipherException("Test message");

        assertEquals("Test message", ex.getMessage(),
                "Exception should store the provided message");
    }

    @Test
    void testConstructorWithMessageAndCause() {
        Throwable cause = new RuntimeException("Cause");
        CipherException ex = new CipherException("Test message", cause);

        assertEquals("Test message", ex.getMessage(),
                "Exception should store the provided message");
        assertEquals(cause, ex.getCause(),
                "Exception should store the provided cause");
    }
}
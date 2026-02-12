package password.vault.server.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EnzoicPasswordClientExceptionTest {

    @Test
    void testConstructorWithMessage() {
        EnzoicPasswordClientException ex = new EnzoicPasswordClientException("Test message");

        assertEquals("Test message", ex.getMessage(),
                "Exception should store the provided message");
    }

    @Test
    void testConstructorWithMessageAndCause() {
        Throwable cause = new RuntimeException("Cause");
        EnzoicPasswordClientException ex = new EnzoicPasswordClientException("Test message", cause);

        assertEquals("Test message", ex.getMessage(),
                "Exception should store the provided message");
        assertEquals(cause, ex.getCause(),
                "Exception should store the provided cause");
    }
}

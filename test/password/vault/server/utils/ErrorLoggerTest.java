package password.vault.server.utils;

import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class ErrorLoggerTest {

    @Test
    void testLogCreatesLogFile() {
        Exception ex = new RuntimeException("Test exception");

        ErrorLogger.log(ex, "testUser");

        Path logPath = Path.of("data/logs/error.log");

        assertTrue(Files.exists(logPath));
    }
}
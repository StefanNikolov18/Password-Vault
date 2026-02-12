package password.vault.server.utils;

import org.junit.jupiter.api.*;

import java.io.IOException;
import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

public class EnzoicConfigSingletonTest {

    @Test
    void testGetInstanceReturnsInstance() throws IOException {
        assertNotNull( EnzoicConfigSingleton.getInstance());
    }

    @Test
    void testGetInstanceReturnsSameInstance() throws IOException {

        assertSame(EnzoicConfigSingleton.getInstance(), EnzoicConfigSingleton.getInstance());
    }

    @Test
    void testApiKeyIsLoaded() throws IOException {
        assertNotNull(EnzoicConfigSingleton.getInstance().getApiKey());
    }

    @Test
    void testApiSecretIsLoaded() throws IOException {
        assertNotNull( EnzoicConfigSingleton.getInstance().getApiSecret());
    }
}

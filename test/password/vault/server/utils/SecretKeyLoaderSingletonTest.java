package password.vault.server.utils;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class SecretKeyLoaderSingletonTest {

    @Test
    void testGetInstanceReturnsInstance() throws IOException {

        assertNotNull(  SecretKeyLoaderSingleton.getInstance());
    }

    @Test
    void testGetInstanceReturnsSameInstance() throws IOException {

        assertSame(SecretKeyLoaderSingleton.getInstance(), SecretKeyLoaderSingleton.getInstance());
    }

    @Test
    void testGetSecretKeyNotNull() throws IOException {

        assertNotNull(SecretKeyLoaderSingleton.getInstance().getSecretKey());
    }


}
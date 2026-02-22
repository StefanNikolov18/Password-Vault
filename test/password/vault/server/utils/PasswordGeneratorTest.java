package password.vault.server.utils;

import org.junit.jupiter.api.Test;
import password.vault.server.algorithm.PasswordGenerator;
import password.vault.server.integration.enzoic.EnzoicPasswordClient;
import password.vault.server.integration.enzoic.EnzoicPasswordResponse;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PasswordGeneratorTest {
    EnzoicPasswordClient client = mock(EnzoicPasswordClient.class);
    EnzoicPasswordResponse response = mock(EnzoicPasswordResponse.class);

    @Test
    void testGeneratePasswordReturnsPasswordWhenNotExposedNotNull() throws Exception {

        when(response.revealedInExposure()).thenReturn(false);
        when(client.getResponse(any())).thenReturn(response);

        assertNotNull( PasswordGenerator.generatePassword(client));
    }

    @Test
    void testGeneratePasswordReturnsPasswordWithCorrectLength() throws Exception {
        when(response.revealedInExposure()).thenReturn(false);
        when(client.getResponse(any())).thenReturn(response);

        String password = PasswordGenerator.generatePassword(client);
        assertEquals(22, password.length());
    }

    @Test
    void testGeneratePasswordThrowsExceptionWhenAllAttemptsFail() throws Exception {

        when(response.revealedInExposure()).thenReturn(true);
        when(client.getResponse(any())).thenReturn(response);

        assertThrows(RuntimeException.class,
                () -> PasswordGenerator.generatePassword(client));
    }

}

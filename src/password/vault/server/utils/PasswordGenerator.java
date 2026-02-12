package password.vault.server.utils;

import password.vault.server.exception.EnzoicPasswordClientException;
import password.vault.server.integration.enzoic.EnzoicPasswordClient;
import password.vault.server.integration.enzoic.EnzoicPasswordResponse;

import java.security.SecureRandom;

public class PasswordGenerator {
    private static final String CHARSET =
            "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*";
    private static final int PASSWORD_LENGTH = 22;
    private static final int MAX_ATTEMPTS =  10;

    private PasswordGenerator() {

    }

    public static String generatePassword(EnzoicPasswordClient enzoicPasswordClient)
        throws EnzoicPasswordClientException {
        SecureRandom random = new SecureRandom();
        for (int attempt = 1; attempt <= MAX_ATTEMPTS; ++attempt) {
            StringBuilder sb = new StringBuilder(PASSWORD_LENGTH); //local
            for (int i = 0; i < PASSWORD_LENGTH; i++) {
                int index = random.nextInt(CHARSET.length());
                sb.append(CHARSET.charAt(index));
            }

            String password = sb.toString();
            EnzoicPasswordResponse response = enzoicPasswordClient.getResponse(password);
            if (!response.revealedInExposure()) {            //true == isWeak
                return password;
            }

        }

        throw new RuntimeException("Generating password was unsuccessful!");
    }
}

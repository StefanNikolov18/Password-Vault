package password.vault.server.utils;

import java.security.SecureRandom;

public class PasswordGenerator {
    private static final String CHARSET =
            "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*";
    private static final int PASSWORD_LENGTH = 22;

    private PasswordGenerator() {

    }

    public static String generatePassword() {
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder(PASSWORD_LENGTH);
        for (int i = 0; i < PASSWORD_LENGTH; i++) {
            int index = random.nextInt(CHARSET.length());
            sb.append(CHARSET.charAt(index));
        }

        return sb.toString();
    }
}

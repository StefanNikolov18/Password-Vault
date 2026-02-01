package password.vault.server.service.auth;

public record AuthenticationResult(boolean success, String username, String message) {

    public AuthenticationResult {
        if (message == null) {
            throw new IllegalArgumentException("Message cannot be null!");
        }
        if (success && username == null) {
            throw new IllegalArgumentException("Username cannot be null in success!");
        }
    }
}

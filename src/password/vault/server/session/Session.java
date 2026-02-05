package password.vault.server.session;

public class Session {
    private static final long TIMEOUT = 60_000; //1 minute

    private long lastActivity;

    public void startSession() {
        lastActivity = System.currentTimeMillis();
    }

    public void refreshSession() {
        lastActivity = System.currentTimeMillis();
    }

    public boolean isSessionExpired() {
        long now = System.currentTimeMillis();
        return (now - lastActivity) > TIMEOUT;
    }

}

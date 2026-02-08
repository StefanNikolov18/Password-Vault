package password.vault.server.session;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SessionTest {
    @Test
    public void testSessionIsNotExpiredImmediately() {
        Session session = new Session();
        session.startSession();
        assertFalse(session.isSessionExpired());
    }

    @Test
    public void testSessionIsExpired() {
        Session session = new Session();
        session.startSession();
        session.setLastActivity(session.getLastActivity() - (Session.getTIMEOUT() + 1_000));
        assertTrue(session.isSessionExpired());
    }

    @Test
    void testSessionIsNotExpired(){
        Session session = new Session();
        session.startSession();
        session.refreshSession();
        assertFalse(session.isSessionExpired());
    }
}

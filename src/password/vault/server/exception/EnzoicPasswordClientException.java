package password.vault.server.exception;

public class EnzoicPasswordClientException extends Exception {

    public EnzoicPasswordClientException(String message) {
        super(message);
    }

    public EnzoicPasswordClientException(String message, Throwable cause) {
        super(message, cause);
    }

}

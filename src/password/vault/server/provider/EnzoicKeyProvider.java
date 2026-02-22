package password.vault.server.provider;

public class EnzoicKeyProvider {

    private final String apiKey;
    private final String apiSecret;

    public EnzoicKeyProvider() {
        this.apiKey = System.getenv("ENZOIC_API_KEY");
        this.apiSecret = System.getenv("ENZOIC_API_SECRET");

        if (apiKey == null || apiKey.isBlank()) {
            throw new IllegalArgumentException("ApiKey not configured!");
        }

        if (apiSecret == null || apiSecret.isBlank()) {
            throw new IllegalArgumentException("ApiSecret not configured!");
        }
    }

    public String getApiKey() {
        return apiKey;
    }

    public String getApiSecret() {
        return apiSecret;
    }
}

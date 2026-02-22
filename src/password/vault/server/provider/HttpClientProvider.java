package password.vault.server.provider;

import java.net.http.HttpClient;

public class HttpClientProvider {
    private static final HttpClient CLIENT = HttpClient.newHttpClient();

    public static HttpClient getClient() {
        return CLIENT;
    }
}
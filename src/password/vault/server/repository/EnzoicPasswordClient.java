package password.vault.server.repository;

import com.google.gson.Gson;
import password.vault.server.algorithm.hashing.Sha256Hashing;
import password.vault.server.exception.EnzoicPasswordClientException;
import password.vault.server.utils.EnzoicConfigSingleton;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class EnzoicPasswordClient {
    private static final String ENZOIC_BASE_URL = "https://api.enzoic.com/passwords";
    private final String apiKey;
    private final String apiSecret;

    private final HttpClient client;
    private static final Gson GSON = new Gson();

    public EnzoicPasswordClient() throws IOException {
        this.apiKey = EnzoicConfigSingleton.getInstance().getApiKey();
        this.apiSecret = EnzoicConfigSingleton.getInstance().getApiSecret();

        this.client = HttpClient.newHttpClient();
    }

    public EnzoicPasswordResponse getResponse(String password)
            throws EnzoicPasswordClientException {
        if (password == null) {
            throw new IllegalArgumentException("Given password cannot be null!");
        }
        URI uri = buildUri(password);
        String authEncoded = buildAuthorization();

        HttpRequest request = HttpRequest.newBuilder(uri)
                .header("Authorization", "basic " + authEncoded)
                .build();
        try {
            var response = client.send(request, HttpResponse.BodyHandlers.ofString());
            int status = response.statusCode();
            String body = response.body();

            final int revealedStatus = 200;
            final int unrevealedStatus = 404;
            if (status == revealedStatus) {
                return GSON.fromJson(body, EnzoicPasswordResponse.class);
            } else if (status == unrevealedStatus) {
                return new EnzoicPasswordResponse(false,
                        0,
                        0);
            }

            throw new  EnzoicPasswordClientException("Enzoic client ended with status: " + status );
        } catch (Exception e) {
            throw new EnzoicPasswordClientException("Client error: " + e.getMessage());
        }
    }

    private URI buildUri(String password) throws EnzoicPasswordClientException {
        String sha256Password = Sha256Hashing.hashing(password);

        StringBuilder q = new StringBuilder(ENZOIC_BASE_URL);
        q.append("?sha256=").append(sha256Password);

        return URI.create(q.toString());
    }

    private String buildAuthorization() {
        String auth = apiKey + ":" + apiSecret;
        return Base64.getEncoder().encodeToString(auth.getBytes(StandardCharsets.UTF_8));
    }

}

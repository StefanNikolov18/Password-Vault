package password.vault.server.integration.enzoic;

import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import password.vault.server.exception.EnzoicPasswordClientException;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class EnzoicPasswordClientTest {

    private HttpClient httpClientMock;
    private HttpResponse<String> httpResponseMock;
    private EnzoicPasswordClient enzoicClient;

    private static final Gson GSON = new Gson();

    @BeforeEach
    void setUp() throws Exception {
        httpClientMock = mock(HttpClient.class);
        httpResponseMock = mock(HttpResponse.class);

        enzoicClient = new EnzoicPasswordClient(httpClientMock);
    }

    @Test
    void testConstructorWithoutParamsDoesNotThrowException()
        throws IOException {
        assertDoesNotThrow(() -> new EnzoicPasswordClient(),
                "It should not throw exception!");
    }
    // Null input
    @Test
    void testGetResponseNullPasswordThrowsException() {
        assertThrows(
                IllegalArgumentException.class,
                () -> enzoicClient.getResponse(null),
                "getResponse password is null and should throw IllegalArgumentException!");
    }

    // HTTP 200
    @Test
    void testGetResponseHttp200ReturnsParsedResponse()
            throws Exception {
        String json = GSON.toJson(new EnzoicPasswordResponse(true,
                5, 100));

        when(httpResponseMock.statusCode()).thenReturn(200);
        when(httpResponseMock.body()).thenReturn(json);
        when(httpClientMock.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenReturn(httpResponseMock);

        EnzoicPasswordResponse response = enzoicClient.getResponse("password123");

        assertTrue(
                response.revealedInExposure(),
                "getResponse should return a response with revealedInExposure=true for HTTP 200"
        );
    }

    // HTTP 404
    @Test
    void testGetResponseHttp404ReturnsUnrevealedResponse() throws Exception {
        String json = GSON.toJson(new EnzoicPasswordResponse(false,
                0, 0));
        when(httpResponseMock.statusCode()).thenReturn(404);
        when(httpResponseMock.body()).thenReturn(json);
        when(httpClientMock.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenReturn(httpResponseMock);

        EnzoicPasswordResponse response = enzoicClient.getResponse("password123");

        assertFalse(
                response.revealedInExposure(),
                "getResponse should return a response with revealedInExposure=false for HTTP 404"
        );
    }

    // HTTP error
    @Test
    void testGetResponseHttpErrorThrowsEnzoicPasswordClientException()
            throws Exception {
        when(httpResponseMock.statusCode()).thenReturn(500);
        when(httpResponseMock.body()).thenReturn("{\"status\":\"error\"}");
        when(httpClientMock.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenReturn(httpResponseMock);

        assertThrows(
                EnzoicPasswordClientException.class,
                () -> enzoicClient.getResponse("password123"),
                "getResponse should throw EnzoicPasswordClientException for HTTP error"
        );
    }

}
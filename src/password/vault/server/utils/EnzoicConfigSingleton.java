package password.vault.server.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class EnzoicConfigSingleton {
    private static final String CONFIG_PATH = "data/api/enzoic.config";
    private static EnzoicConfigSingleton instance;

    private String apiKey;
    private String apiSecret;

    private EnzoicConfigSingleton() throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(CONFIG_PATH));

        boolean keyFound = false;
        boolean secretFound = false;
        for (String line : lines) {
            line = line.trim();

            if (line.startsWith("ENZOIC_API_KEY=")) {
                apiKey = line.split("=")[1].trim();
                keyFound = true;
            } else if (line.startsWith("ENZOIC_API_SECRET=")) {
                apiSecret = line.split("=")[1].trim();
                secretFound = true;
            }
        }

        if (!keyFound || !secretFound) {
            throw new IOException("Enzoic API Key and API Secret are missing!");
        }
    }

    public static synchronized EnzoicConfigSingleton getInstance() throws IOException {
        if (instance == null) {
            instance = new EnzoicConfigSingleton();
        }
        return instance;
    }

    public String getApiKey() {
        return apiKey;
    }

    public String getApiSecret() {
        return apiSecret;
    }

}

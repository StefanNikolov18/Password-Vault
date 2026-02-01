package password.vault.server.auth.loader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class UserFileLoader implements UserLoader {

    private InputStream userFileStream;

    public UserFileLoader(InputStream userFileStream) {
        this.userFileStream = userFileStream;
    }

    @Override
    public Map<String, String> load() throws IOException {
        Map<String, String> data = new HashMap<>();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(this.userFileStream))) {
            String line;
            int lineNum = 0;
            while ((line = reader.readLine()) != null) {
                lineNum++;

                line = line.trim();
                if (line.isBlank()) {
                    continue;
                }

                //format -> user:password
                String[] parts = line.split(":");
                if (parts.length == 2) {
                    data.put(parts[0], parts[1]);
                } else {
                    throw new IOException("Corrupted user file at line " + lineNum + ": " + line);
                }
            }
        }

        return data;
    }
}

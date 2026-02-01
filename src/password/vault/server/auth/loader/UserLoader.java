package password.vault.server.auth.loader;

import java.io.IOException;
import java.util.Map;

public interface UserLoader {
    //load users in data
    Map<String, String> load() throws IOException;
}

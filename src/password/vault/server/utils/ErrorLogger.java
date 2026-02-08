package password.vault.server.utils;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class ErrorLogger {
    private static final String LOG_FILE = "data/logs/error.log";

    public static synchronized void log(Exception ex, String user) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(LOG_FILE, true))) { //append = true
            pw.println("Time: " + System.currentTimeMillis());
            if (user != null) pw.println("User: " + user);
            pw.println("Exception: " + ex.getClass().getName());
            ex.printStackTrace(pw);
            pw.println("----");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
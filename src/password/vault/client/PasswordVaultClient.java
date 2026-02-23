package password.vault.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class PasswordVaultClient {
    private static final int SERVER_PORT = 4444;

    public static void main() {

        try (Socket socket = new Socket("localhost", SERVER_PORT);
             PrintWriter writer = new PrintWriter(socket.getOutputStream(), true); // autoflush on
             BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             Scanner scanner = new Scanner(System.in)) {

            Thread.currentThread().setName("Client thread " + socket.getLocalPort());
            System.out.println("Connected to the server.");

            while (true) {
                System.out.print("> ");
                String command = scanner.nextLine(); // read a line from the console

                writer.println(command); // send the message to the server

                String serverReply = readServerReply(reader);

                if (serverReply == null || serverReply.equalsIgnoreCase("disconnect")) {
                    break;
                }
                System.out.println(serverReply);
            }
            System.out.println("Goodbye!");
        } catch (IOException e) {
            System.err.println("Problem connecting to Password Vault server!");
        }
    }

    private static String readServerReply(BufferedReader reader) throws IOException {
        StringBuilder sb = new StringBuilder();
        String line;
        boolean receivedSmt = false;

        while ((line = reader.readLine()) != null) {
            receivedSmt = true;
            if (line.equals("END")) break;
            sb.append(line).append("\n");
        }

        if (!receivedSmt) {
            return null; // connection closed
        }

        return sb.toString().trim();
    }
}


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

            Thread.currentThread().setName("Echo client thread " + socket.getLocalPort());
            System.out.println("Connected to the server.");

            while (true) {
                System.out.print("> ");
                String command = scanner.nextLine(); // read a line from the console

                writer.println(command); // send the message to the server

                String serverReply = reader.readLine(); // read the response from the server
                System.out.println(serverReply);
                System.out.println();
            }

        } catch (IOException e) {
            throw new RuntimeException("There is a problem with the network communication", e);
        }
    }
}


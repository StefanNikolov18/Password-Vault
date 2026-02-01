package password.vault.server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PasswordVaultServer {

    private static final int SERVER_PORT = 4444;
    private static final int MAX_THREADS = 2;

    public static void main(String... args) {
        Thread.currentThread().setName("PasswordVaultServer-Main");

        ExecutorService executor = Executors.newFixedThreadPool(MAX_THREADS);

        try (ServerSocket serverSocket = new ServerSocket(SERVER_PORT)) {

            InetAddress address = InetAddress.getLocalHost();
            System.out.println("Password Vault Server started on "
                    + address.getHostAddress() + ":" + SERVER_PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected: "
                        + clientSocket.getInetAddress() + ":" + clientSocket.getPort());

                ClientRequestHandler clientHandler = new ClientRequestHandler(clientSocket);

                executor.execute(clientHandler);
            }

        } catch (IOException e) {
            System.err.println("Server error occurred. Check logs.");
            // Logger.log(e);
        } finally {
            executor.shutdown();
        }
    }
}
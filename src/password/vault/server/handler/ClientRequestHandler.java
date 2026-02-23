package password.vault.server.handler;

import password.vault.server.repository.UserRepository;
import password.vault.server.utils.ErrorLogger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientRequestHandler implements Runnable {
    private final Socket socket;
    private final UserRepository userRepository;

    public ClientRequestHandler(Socket socket, UserRepository userRepository) {
        this.socket = socket;
        this.userRepository = userRepository;
    }

    @Override
    public void run() {
        Thread.currentThread().setName("Client Request Handler for " + socket.getRemoteSocketAddress());

        try (PrintWriter out = new PrintWriter(socket.getOutputStream(), true); // autoflush on
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             socket) { // resources created elsewhere can also be declared here and will be auto-closed

            CommandHandler cmdHandler = new CommandHandler(this.userRepository);

            String commandLine;
            while ((commandLine = in.readLine()) != null) { // read the message from the client
                System.out.println("Message received from client: " + commandLine);
                if (isDisconnect(commandLine, out)) {
                    break;
                }
                try {
                    String message = cmdHandler.execute(commandLine);
                    sendMessage(message, out);  // send response back to the client
                } catch (Exception e) {
                    System.out.println("Error handling client " + socket.getRemoteSocketAddress()
                            + ": " + e.getMessage());
                    ErrorLogger.log(e, socket.getRemoteSocketAddress().toString()); // every exception from cmdHandler
                    out.println("An unexpected error occurred. " +
                            "Please try again later or contact support with the logs.");
                }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private boolean isDisconnect(String commandLine, PrintWriter out) {
        if (commandLine.equalsIgnoreCase("disconnect")) {
            out.println("disconnect"); // to the client
            System.out.println("Disconnected from client " + socket.getRemoteSocketAddress());
            return true;
        }
        return false;
    }

    private void sendMessage(String message, PrintWriter out) {
        out.print(message);
        out.print("\nEND\n");
        out.flush();
    }
}

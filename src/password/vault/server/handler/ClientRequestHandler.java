package password.vault.server.handler;

import password.vault.server.repository.UserRepository;

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
                String message = cmdHandler.execute(commandLine);
                out.println(message); // send response back to the client
            }

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

    }
}

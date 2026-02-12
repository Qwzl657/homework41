import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class EchoServer {

    private final int port;

    private EchoServer(int port) {
        this.port = port;
    }

    public static EchoServer bindToPort(int port) {
        return new EchoServer(port);
    }

    public void run() {
        try (ServerSocket server = new ServerSocket(port)) {

            while (true) {
                try (Socket socket = server.accept()) {
                    handle(socket);
                }
            }

        } catch (IOException e) {
            System.out.printf("Port %d is busy.%n", port);
        }
    }

    private void handle(Socket socket) throws IOException {

        try (Scanner reader = new Scanner(socket.getInputStream(), "UTF-8");
             PrintWriter writer = new PrintWriter(socket.getOutputStream(), true)) {

            while (reader.hasNextLine()) {

                String message = reader.nextLine().strip();
                System.out.println("Got: " + message);

                if ("bye".equalsIgnoreCase(message)) {
                    return;
                }

                String reversed = new StringBuilder(message)
                        .reverse()
                        .toString();

                writer.println(reversed);
            }
        }
    }

    public static void main(String[] args) {
        EchoServer.bindToPort(8089).run();
    }
}
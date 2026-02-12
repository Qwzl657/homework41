import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class EchoClient {

    private final String host;
    private final int port;

    private EchoClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public static EchoClient connectTo(int port) {
        return new EchoClient("127.0.0.1", port);
    }

    public void run() {

        System.out.println("Чтобы выйти напишите 'bye'");

        try (Socket socket = new Socket(host, port);
             Scanner userInput = new Scanner(System.in, "UTF-8");
             Scanner serverInput = new Scanner(socket.getInputStream(), "UTF-8");
             PrintWriter writer = new PrintWriter(socket.getOutputStream(), true)) {

            while (true) {

                String message = userInput.nextLine();
                writer.println(message);

                if ("bye".equalsIgnoreCase(message)) {
                    return;
                }

                if (serverInput.hasNextLine()) {
                    String response = serverInput.nextLine();
                    System.out.println(response);
                }
            }

        } catch (IOException e) {
            System.out.printf("Can't connect to %s:%d%n", host, port);
        }
    }

    public static void main(String[] args) {
        EchoClient.connectTo(8089).run();
    }
}
import java.io.*;
import java.net.*;
import java.util.Scanner;

public class ChatClient {

    public static void main(String[] args) {
        String host;
        int port = 1728;

        try (Scanner userInput = new Scanner(System.in)) {
            System.out.print("Enter server IP address (127.0.0.1 or localhost): ");
            host = userInput.nextLine();

            Socket socket = new Socket(host, port);
            System.out.println("Connected to server.");

            BufferedReader incoming = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter outgoing = new PrintWriter(socket.getOutputStream(), true);

            // Thread to listen for messages from server
            Thread listener = new Thread(() -> {
                try {
                    String serverMessage;
                    while ((serverMessage = incoming.readLine()) != null) {
                        if (serverMessage.equals("ENTER_USERNAME")) {
                            System.out.print("Enter username: ");
                        } else if (serverMessage.startsWith("USERLIST:")) {
                            String users = serverMessage.substring(9);
                            System.out.println("\nConnected users: " + users);
                        } else {
                            System.out.println(serverMessage);
                        }
                    }
                } catch (IOException e) {
                    System.out.println("Disconnected from server.");
                }
            });

            listener.start();

            while (true) {
                String message = userInput.nextLine();
                outgoing.println(message);

                if (message.equalsIgnoreCase("quit")) {
                    break;
                }
            }

            socket.close();

        } catch (IOException e) {
            System.out.println("Client error: " + e.getMessage());
        }
    }
}
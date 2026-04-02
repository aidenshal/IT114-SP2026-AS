import java.io.*;
import java.net.*;
import java.util.Scanner;

/* ChatServer.java
 * A simple server program that listens for a connection from a client, and then
 * exchanges messages with the client until either side sends "quit" (or an error occurs).
 *
 * Basic algorithm for the Server:
 * 1. Listen for connection requests on specified port.
 * 2. Accept connection request
 * 3. Listen for message from client (InputStream)
 * 4. Send message back to client (OutputStream)
 * 5. Repeat steps 3 and 4 until either side sends "quit" (or error occurs)
 * 6. Close connection.
 */

public class ChatServer {
    public static void main(String[] args) {

        int port = 1728; // The port on which the server listens.

        ServerSocket listener = null; // Listens for a connection request.
        Socket connection = null; // For communication with the client.

        BufferedReader incoming = null; // Stream for receiving data from client.
        PrintWriter outgoing = null; // Stream for sending data to client.
        String messageOut; // A message to be sent to the client.
        String messageIn; // A message received from the client.

        Scanner userInput = null; // Standard input, for reading lines of input from the user.

        // 1. Open ServerSocket to listen for connection requests on specified port.
        try {
            listener = new ServerSocket(port);

            System.out.println("Listening on port " + listener.getLocalPort());

            // 2. Wait for a client connection request, and when one arrives, accept it and close the Server Socket.
            connection = listener.accept();
            listener.close();

            // 3. Set up input and output streams for the new Socket created.
            incoming = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            outgoing = new PrintWriter(connection.getOutputStream(), true);

            System.out.println("Connected. Waiting for the first message.");
        }
        // If failed to open/accept connection, print error message and end the program.
        catch (Exception e) {
            System.out.println("Error: Could not start server or accept connection.");
            System.out.println(e.getMessage());
            return;
        }

        // 4. Exchange messages with the client.
        try {
            userInput = new Scanner(System.in);
            System.out.println("NOTE: Enter 'quit' to end the program.\n");

            while (true) {
                // Server waits to receive a message first.
                System.out.println("WAITING...");

                // Receive message from Client and print
                messageIn = incoming.readLine();

                if (messageIn == null) {
                    throw new IOException("Client disconnected.");
                }

                System.out.println("RECEIVED:  " + messageIn);

                // If message received from client is "quit", then close the connection and end the program.
                if (messageIn.equalsIgnoreCase("quit")) {
                    System.out.println("Connection closed.");
                    break;
                }

                System.out.print("SEND:      ");

                // Have Server send a message back to the client.
                messageOut = userInput.nextLine();
                outgoing.println(messageOut);

                // If message sent to client is "quit", then close the connection and end the program.
                if (messageOut.equalsIgnoreCase("quit")) {
                    System.out.println("Connection closed.");
                    break;
                }

                // Check for errors while transmitting message.
                if (outgoing.checkError()) {
                    throw new IOException("Error occurred while transmitting message.");
                }
            }
        }
        // If failed to exchange messages, print error message and end the program.
        catch (Exception e) {
            System.out.println("Error during chat: " + e.getMessage());
        }
        // 5. Close the connection, whether there's an error or not.
        finally {
            if (userInput != null) {
                userInput.close();
            }
            try {
                if (incoming != null) incoming.close();
            } catch (Exception e) {
            }
            try {
                if (outgoing != null) outgoing.close();
            } catch (Exception e) {
            }
            try {
                if (connection != null) connection.close();
            } catch (Exception e) {
            }
            try {
                if (listener != null && !listener.isClosed()) listener.close();
            } catch (Exception e) {
            }
        }

    } // end main()

} // end class ChatServer
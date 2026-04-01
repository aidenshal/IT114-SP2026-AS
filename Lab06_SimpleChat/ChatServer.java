import java.net.*;
import java.util.Scanner;
import java.io.*;

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

        ServerSocket listener; // Listens for a connection request.
        Socket connection; // For communication with the client.

        BufferedReader incoming; // Stream for receiving data from client.
        PrintWriter outgoing; // Stream for sending data to client.
        String messageOut; // A message to be sent to the client.
        String messageIn; // A message received from the client.

        Scanner userInput; // Standard input, for reading lines of input from the user.

        // 1. Open ServerSocket to listen for connection requests on specified port.
        try {
            //TODO: Create ServerSocket to listen on port
                //YOUR CODE HERE
            
            System.out.println("Listening on port " + listener.getLocalPort());

            // 2. Wait for a client connection request, and when one arrives, accept it and close the Server Socket.
            // We only want one client for this lab. For more than one client, keep the listener open.
                //YOUR CODE HERE.

            // 3. TODO: Set up input and output streams for the new Socket created.
                //YOUR CODE HERE
            
            System.out.println("Connected.  Waiting for the first message.");
        }
        // TODO: If failed to open/accept connection, print error message and end the program.
        catch (Exception e) {
            //YOUR CODE HERE
        }
        // 4. Exchange messages with the client.
        try {
            userInput = new Scanner(System.in);
            System.out.println("NOTE: Enter 'quit' to end the program.\n");
            while (true) {
                // Server waits to receive a message first.
                System.out.println("WAITING...");

                // TODO: Receive message from Client and print 
                // If message received from client is "quit", then close the connection and end
                // the program.
                    //YOUR CODE HERE

                System.out.print("SEND:      ");
               
                // TODO: Have Server send a message back to the client.
                // If message sent to client is "quit", then close the connection and end the
                // program.
                    //YOUR CODE HERE
                
                // Check for errors while transmitting message.
                if (outgoing.checkError()) {
                    userInput.close();
                    throw new IOException("Error occurred while transmitting message.");
                }
            }
        }
        // TODO: If failed to exchange messages, print error message and end the program.
        catch (Exception e) {
            //YOUR CODE HERE
        }
        // 5. Close the connection, whether there's an error or not.
        finally {
            try {
                connection.close();

            } catch (Exception e) {
                // Ignore errors that occur while closing the connection.
            }
        }

    } // end main()

} // end class ChatServer
import java.io.*;
import java.net.*;

public class ClientHandler extends Thread {

    private Socket socket;
    private BufferedReader incoming;
    private PrintWriter outgoing;
    private String username;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            incoming = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            outgoing = new PrintWriter(socket.getOutputStream(), true);

            // Ask for username until unique
            while (true) {
                outgoing.println("ENTER_USERNAME");
                String attemptedName = incoming.readLine();

                if (attemptedName == null) {
                    return;
                }

                attemptedName = attemptedName.trim();

                if (attemptedName.isEmpty()) {
                    outgoing.println("SERVER: Username cannot be empty.");
                    continue;
                }

                if (ChatServer.addClient(attemptedName, this)) {
                    username = attemptedName;
                    outgoing.println("SERVER: Welcome, " + username + "!");
                    break;
                } else {
                    outgoing.println("SERVER: Username already taken.");
                }
            }

            String message;
            while ((message = incoming.readLine()) != null) {

                if (message.equalsIgnoreCase("quit")) {
                    break;
                }

                if (message.equalsIgnoreCase("/history")) {
                    sendChatHistory();
                    continue;
                }

                if (message.startsWith("/rps challenge ")) {
                    String[] parts = message.split(" ", 3);

                    if (parts.length < 3) {
                        sendMessage("SERVER: Usage -> /rps challenge username");
                    } else {
                        String opponent = parts[2];
                        ChatServer.startRPSChallenge(username, opponent);
                    }

                    continue;
                }

                // DM format: /dm username message here
                if (message.startsWith("/dm ")) {
                    String[] parts = message.split(" ", 3);
                    if (parts.length < 3) {
                        sendMessage("SERVER: Usage -> /dm username message");
                    } else {
                        String recipient = parts[1];
                        String dmMessage = parts[2];
                        ChatServer.sendPrivateMessage(username, recipient, dmMessage);
                    }
                } else {
                    ChatServer.broadcast(username + ": " + message);
                }
            }

        } catch (IOException e) {
            System.out.println("Connection error with " + username + ": " + e.getMessage());
        } finally {
            ChatServer.removeClient(username);
            closeEverything();
        }
    }

    private void sendChatHistory() {
        File logFile = new File("chat_log.txt");

        if (!logFile.exists() || logFile.length() == 0) {
            sendMessage("SERVER: No chat history available.");
            return;
        }

        try (BufferedReader fileReader = new BufferedReader(new FileReader(logFile))) {
            String line;
            while ((line = fileReader.readLine()) != null) {
                sendMessage(line);
            }
        } catch (IOException e) {
            sendMessage("SERVER: Error reading chat history.");
        }
    }

    public void sendMessage(String message) {
        if (outgoing != null) {
            outgoing.println(message);
        }
    }

    private void closeEverything() {
        try {
            if (incoming != null) incoming.close();
        } catch (Exception e) {
        }
        try {
            if (outgoing != null) outgoing.close();
        } catch (Exception e) {
        }
        try {
            if (socket != null) socket.close();
        } catch (Exception e) {
        }
    }
}
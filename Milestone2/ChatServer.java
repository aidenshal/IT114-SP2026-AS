import java.io.*;
import java.net.*;
import java.util.concurrent.ConcurrentHashMap;

public class ChatServer {

    private static final int PORT = 1728;

    // username -> handler
    private static final ConcurrentHashMap<String, ClientHandler> clients = new ConcurrentHashMap<>();

    private static class RPSGame {
        String player1;
        String player2;
        String player1Move;
        String player2Move;

        RPSGame(String player1, String player2) {
            this.player1 = player1;
            this.player2 = player2;
        }
    }

    private static final ConcurrentHashMap<String, RPSGame> activeRPSGames = new ConcurrentHashMap<>();

    public static void main(String[] args) {
        System.out.println("Server starting on port " + PORT);

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("New client connected: " + socket.getInetAddress());

                ClientHandler handler = new ClientHandler(socket);
                handler.start();
            }
        } catch (IOException e) {
            System.out.println("Server error: " + e.getMessage());
        }
    }

    public static synchronized boolean addClient(String username, ClientHandler handler) {
        if (clients.containsKey(username)) {
            return false;
        }
        clients.put(username, handler);
        broadcast("SERVER: " + username + " joined the chat.");
        sendUserListToAll();
        return true;
    }

    public static synchronized void removeClient(String username) {
        if (username != null && clients.remove(username) != null) {
            broadcast("SERVER: " + username + " left the chat.");
            sendUserListToAll();
        }
    }

    public static synchronized void saveMessage(String message) {
        try (PrintWriter writer = new PrintWriter(new FileWriter("chat_log.txt", true))) {
            writer.println(message);
        } catch (IOException e) {
            System.out.println("Error saving message: " + e.getMessage());
        }
    }

    public static void broadcast(String message) {
        saveMessage(message);

        for (ClientHandler client : clients.values()) {
            client.sendMessage(message);
        }
    }

    public static void sendPrivateMessage(String sender, String recipient, String message) {
        ClientHandler target = clients.get(recipient);
        ClientHandler source = clients.get(sender);

        if (target != null) {
            saveMessage("[DM " + sender + " -> " + recipient + "]: " + message);

            target.sendMessage("[DM from " + sender + "]: " + message);
            if (source != null) {
                source.sendMessage("[DM to " + recipient + "]: " + message);
            }
        } else {
            if (source != null) {
                source.sendMessage("SERVER: User '" + recipient + "' not found.");
            }
        }
    }

    public static synchronized void startRPSChallenge(String sender, String recipient) {
        ClientHandler target = clients.get(recipient);
        ClientHandler source = clients.get(sender);

        if (target == null) {
            if (source != null) {
                source.sendMessage("SERVER: User '" + recipient + "' not found.");
            }
            return;
        }

        RPSGame game = new RPSGame(sender, recipient);

        activeRPSGames.put(sender, game);
        activeRPSGames.put(recipient, game);

        source.sendMessage("SERVER: RPS challenge started with " + recipient + ".");
        target.sendMessage("SERVER: RPS challenge started with " + sender + ".");
    }

    public static void sendUserListToAll() {
        StringBuilder sb = new StringBuilder("USERLIST:");
        for (String username : clients.keySet()) {
            sb.append(username).append(",");
        }

        String userListMessage = sb.toString();
        for (ClientHandler client : clients.values()) {
            client.sendMessage(userListMessage);
        }
    }
}
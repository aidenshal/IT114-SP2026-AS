import java.io.*;
import java.net.*;
import java.util.concurrent.ConcurrentHashMap;

public class ChatServer {

    private static final int PORT = 1728;

    // username -> handler
    private static final ConcurrentHashMap<String, ClientHandler> clients = new ConcurrentHashMap<>();

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
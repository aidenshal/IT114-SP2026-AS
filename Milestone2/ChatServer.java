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
        boolean accepted;

        RPSGame(String player1, String player2) {
            this.player1 = player1;
            this.player2 = player2;
            this.accepted = false;
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

        source.sendMessage("SERVER: RPS challenge sent to " + recipient + ".");
        target.sendMessage("SERVER: " + sender + " challenged you to RPS. Type /rps accept " + sender + " or /rps decline " + sender + ".");
    }

    public static synchronized void acceptRPSChallenge(String player, String challenger) {
        RPSGame game = activeRPSGames.get(player);
        ClientHandler playerHandler = clients.get(player);
        ClientHandler challengerHandler = clients.get(challenger);

        if (game == null || !game.player1.equals(challenger) || !game.player2.equals(player)) {
            if (playerHandler != null) {
                playerHandler.sendMessage("SERVER: No pending RPS challenge from " + challenger + ".");
            }
            return;
        }

        game.accepted = true;

        if (playerHandler != null) {
            playerHandler.sendMessage("SERVER: You accepted the RPS challenge from " + challenger + ". Use /rps move rock, /rps move paper, or /rps move scissors.");
        }

        if (challengerHandler != null) {
            challengerHandler.sendMessage("SERVER: " + player + " accepted your RPS challenge. Use /rps move rock, /rps move paper, or /rps move scissors.");
        }
    }

    public static synchronized void declineRPSChallenge(String player, String challenger) {
        RPSGame game = activeRPSGames.get(player);
        ClientHandler playerHandler = clients.get(player);
        ClientHandler challengerHandler = clients.get(challenger);

        if (game == null || !game.player1.equals(challenger) || !game.player2.equals(player)) {
            if (playerHandler != null) {
                playerHandler.sendMessage("SERVER: No pending RPS challenge from " + challenger + ".");
            }
            return;
        }

        activeRPSGames.remove(game.player1);
        activeRPSGames.remove(game.player2);

        if (playerHandler != null) {
            playerHandler.sendMessage("SERVER: You declined the RPS challenge from " + challenger + ".");
        }

        if (challengerHandler != null) {
            challengerHandler.sendMessage("SERVER: " + player + " declined your RPS challenge.");
        }
    }

    public static synchronized void submitRPSMove(String player, String move) {
        ClientHandler source = clients.get(player);

        if (!move.equals("rock") && !move.equals("paper") && !move.equals("scissors")) {
            if (source != null) {
                source.sendMessage("SERVER: Invalid move. Use /rps move rock, /rps move paper, or /rps move scissors.");
            }
            return;
        }

        RPSGame game = activeRPSGames.get(player);

        if (game == null) {
            if (source != null) {
                source.sendMessage("SERVER: You are not in an active RPS challenge.");
            }
            return;
        }

        if (!game.accepted) {
            if (source != null) {
                source.sendMessage("SERVER: RPS challenge has not been accepted yet.");
            }
            return;
        }

        if (player.equals(game.player1)) {
            game.player1Move = move;
        } else if (player.equals(game.player2)) {
            game.player2Move = move;
        }

        source.sendMessage("SERVER: You chose " + move + ".");

        if (game.player1Move != null && game.player2Move != null) {
            ClientHandler player1Handler = clients.get(game.player1);
            ClientHandler player2Handler = clients.get(game.player2);

            String result;

            if (game.player1Move.equals(game.player2Move)) {
                result = "SERVER: RPS result: Tie! Both players chose " + game.player1Move + ".";
            } else if (
                (game.player1Move.equals("rock") && game.player2Move.equals("scissors")) ||
                (game.player1Move.equals("paper") && game.player2Move.equals("rock")) ||
                (game.player1Move.equals("scissors") && game.player2Move.equals("paper"))
            ) {
                result = "SERVER: RPS result: " + game.player1 + " wins! " +
                        game.player1Move + " beats " + game.player2Move + ".";
            } else {
                result = "SERVER: RPS result: " + game.player2 + " wins! " +
                        game.player2Move + " beats " + game.player1Move + ".";
            }

            if (player1Handler != null) {
                player1Handler.sendMessage(result);
            }

            if (player2Handler != null) {
                player2Handler.sendMessage(result);
            }

            activeRPSGames.remove(game.player1);
            activeRPSGames.remove(game.player2);
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
import java.io.*;
import java.net.*;

import java.util.*;
import Classes.Post;

public class Server {
    private static final int PORT = 12345;
    public static List<ClientHandler> clients = new ArrayList<>();
    public static List<Post> messageList = new ArrayList<>();
    public static List<MessageGroup> groups = new ArrayList<>(); // Added

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server started. Listening on port " + PORT);
            for(int i = 0; i < 5; i++)
            {
                groups.add(new MessageGroup(i, "Group" + i));
            }
            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("New client connected: " + socket);

                ClientHandler clientHandler = new ClientHandler(socket);
                clients.add(clientHandler);
                clientHandler.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void broadcast(String message) {
        for (ClientHandler client : clients) {
            client.sendMessage(message);
        }
    }

    public static void broadcast(String message, MessageGroup group) {
        for (ClientHandler client : group.getMembers()) {
            client.sendMessage(message);
        }
    }

    public static void broadcast(Post message) {
        for (ClientHandler client : clients) {
            client.sendMessage(message);
        }
    }

    public static void broadcast(Post message, MessageGroup group) {
        for (ClientHandler client : group.getMembers()) {
            client.sendMessage(message);
        }
    }

    public static void removeClient(ClientHandler client) {
        clients.remove(client);
    }
}

// class MessageGroup { // Added
// private String id;
// private String name;
// private List<ClientHandler> members;

// public List<ClientHandler> getMembers() { // Changed return type to
// List<ClientHandler>
// return members;
// }

// // constructor, getters, setters, and other methods...
// }

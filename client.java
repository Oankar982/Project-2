import java.io.*;
import java.net.*;
import java.util.*;

// ChatMessage class to represent individual messages
class ChatMessage implements Serializable {
    private static final long serialVersionUID = 1L;
    private int messageId;
    private String sender;
    private Date postDate;
    private String subject;

    public ChatMessage(int messageId, String sender, Date postDate, String subject) {
        this.messageId = messageId;
        this.sender = sender;
        this.postDate = postDate;
        this.subject = subject;
    }

    // Getters for message attributes
    // (You can add more methods as needed)

    @Override
    public String toString() {
        return messageId + ", " + sender + ", " + postDate + ", " + subject;
    }
}

// Server class to manage the message board
class Server {
    private List<String> userList; // List of users
    private List<ChatMessage> messageBoard; // List of messages
    private DatagramSocket datagramSocket;

    public Server(int port) throws SocketException {
        userList = new ArrayList<>();
        userList.addAll(Arrays.asList("oankar", "seimas", "chris")); // Initial users
        messageBoard = new ArrayList<>();
        datagramSocket = new DatagramSocket(port);
    }

    // Method to start the server
    public void startServer() throws IOException {
        System.out.println("Server started...");
        byte[] receiveData = new byte[1024];
        while (true) {
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            datagramSocket.receive(receivePacket);
            InetAddress clientAddress = receivePacket.getAddress();
            int clientPort = receivePacket.getPort();
            String username = new String(receivePacket.getData()).trim();
            addUser(username, clientAddress, clientPort);
            displayLastTwoMessages(clientAddress, clientPort);
        }
    }

    // Method to add a new user to the group
    public void addUser(String username, InetAddress clientAddress, int clientPort) throws IOException {
        userList.add(username);
        notifyUsers(username + " joined the group.", clientAddress, clientPort);
        displayUserList(clientAddress, clientPort);
    }

    // Method to remove a user from the group
    public void removeUser(String username, InetAddress clientAddress, int clientPort) throws IOException {
        userList.remove(username);
        notifyUsers(username + " left the group.", clientAddress, clientPort);
        displayUserList(clientAddress, clientPort);
    }

    // Method to post a new message
    public void postMessage(ChatMessage message) throws IOException {
        messageBoard.add(message);
        byte[] sendData = serializeMessage(message);
        for (String username : userList) {
            InetAddress clientAddress = InetAddress.getByName("localhost"); // Assuming all clients are on localhost
            int clientPort = 12346; // Assuming all clients are listening on port 12346
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, clientAddress, clientPort);
            datagramSocket.send(sendPacket);
        }
    }

    // Method to display the last two messages for a new user
    public void displayLastTwoMessages(InetAddress clientAddress, int clientPort) throws IOException {
        int startIndex = Math.max(0, messageBoard.size() - 2);
        for (int i = startIndex; i < messageBoard.size(); i++) {
            ChatMessage message = messageBoard.get(i);
            sendMessage(clientAddress, clientPort, message.toString());
        }
    }

    // Method to display the list of users
    public void displayUserList(InetAddress clientAddress, int clientPort) throws IOException {
        StringBuilder userListString = new StringBuilder();
        for (String user : userList) {
            userListString.append(user).append(", ");
        }
        sendMessage(clientAddress, clientPort, "Users in the group: " + userListString.toString());
    }

    // Method to notify all users about an event
    private void notifyUsers(String message, InetAddress clientAddress, int clientPort) throws IOException {
        sendMessage(clientAddress, clientPort, message);
    }

    // Method to serialize a message object to byte array
    private byte[] serializeMessage(ChatMessage message) throws IOException {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        ObjectOutputStream objectStream = new ObjectOutputStream(byteStream);
        objectStream.writeObject(message);
        return byteStream.toByteArray();
    }

    // Method to send a message to a specific client
    private void sendMessage(InetAddress clientAddress, int clientPort, String message) throws IOException {
        byte[] sendData = message.getBytes();
        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, clientAddress, clientPort);
        datagramSocket.send(sendPacket);
    }
}

// Client class representing a user interacting with the message board
class Client {
    private String username;

    public Client(String username) {
        this.username = username;
    }

    // Method to join the group
    public void joinGroup(String serverAddress, int serverPort) throws IOException {
        DatagramSocket socket = new DatagramSocket();
        InetAddress serverInetAddress = InetAddress.getByName(serverAddress);
        byte[] sendData = username.getBytes();
        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, serverInetAddress, serverPort);
        socket.send(sendPacket);
        byte[] receiveData = new byte[1024];
        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
        socket.receive(receivePacket);
        String userList = new String(receivePacket.getData()).trim();
        System.out.println("Received user list: " + userList);
        socket.close();
    }
}

public class client {
    public static void main(String[] args) {
        try {
            Server server = new Server(12345);
            new Thread(() -> {
                try {
                    server.startServer();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();

            // Start two clients
            Client client1 = new Client("Alice");
            client1.joinGroup("localhost", 12345);

            Client client2 = new Client("Bob");
            client2.joinGroup("localhost", 12345);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

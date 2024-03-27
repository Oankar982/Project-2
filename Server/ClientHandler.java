import java.io.*;
import java.net.*;
import Classes.Post;

public class ClientHandler extends Thread {
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    public ClientHandler(Socket socket) {
        this.socket = socket;
        try {
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            String username = in.readLine();
            System.out.println("New user joined: " + username);

            // Send list of users to the new user
            StringBuilder userList = new StringBuilder();
            for (ClientHandler client : Server.clients) {
                userList.append(client.getUsername()).append(", ");
            }
            sendMessage("Users: " + userList.toString());
            
            if(Server.messageList.size() >= 2)
            {
                out.println(Server.messageList.size() - 2);
                out.println(Server.messageList.size() - 1);
            }
            else if(Server.messageList.size() == 1)
            {
                out.println(Server.messageList.size() - 1);
            }

            // Notify other clients about the new user
            Server.broadcast(username + " joined the group.");

            // Handle client messages
            String[] inputs = new String[2];
            String message;
            while ((message = in.readLine()) != null) {
                inputs = message.split("~");
                Post userPost = new Post(username, inputs[0], inputs[1]);
                userPost.setId(Server.messageList.size());
                Server.messageList.add(userPost);
            }

            // Handle client leaving
            socket.close();
            Server.removeClient(this);
            Server.broadcast(username + " left the group.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String message) {
        out.println(message);
    }

    public String getUsername() {
        return socket.getInetAddress().getHostName();
    }
}

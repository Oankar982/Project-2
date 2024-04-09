import java.io.*;
import java.net.*;
import Classes.Post;
import Classes.Group;

public class ClientHandler extends Thread {
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private String username;

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
            username = in.readLine();
            System.out.println("New user joined: " + username);

            // Send list of users to the new user
            StringBuilder userList = new StringBuilder();
            for (ClientHandler client : Server.clients) {
                userList.append(client.getUsername()).append(", ");
            }
            sendMessage("Users: " + userList.toString());

            if (Server.messageList.size() >= 2) {
                out.println(Server.messageList.get(Server.messageList.size() - 2).toString());
                out.println(Server.messageList.get(Server.messageList.size() - 1).toString());
            } else if (Server.messageList.size() == 1) {
                out.println(Server.messageList.get(Server.messageList.size() - 1).toString());
            }

            // Notify other clients about the new user
            Server.broadcast(username + " joined the group.");

            // Handle client messages
            String[] inputs = new String[2];
            String message;
            while ((message = in.readLine()) != null) {
                String command = message.split("")[0];
                message = message.replace(command, "");

                switch (command) {
                    case "%post":
                        inputs = message.split("~");
                        Post userPost = new Post(username, inputs[0], inputs[1]);
                        userPost.setId(Server.messageList.size());
                        Server.messageList.add(userPost);
                        Server.broadcast(userPost);
                        break;
                    case "%users":
                        sendMessage("Users: " + userList.toString());
                        break;
                    case "%exit":
                        socket.close();
                        Server.removeClient(this);
                        Server.broadcast(username + " left the group.");
                        break;
                    case "%message":
                        out.println(getMessage(Integer.parseInt(message)).toString());
                    //chris' part 2
                    case "%groupjoin":
                        Group userGroup = new Group(List<String> groupName);
                        groupName.addUser(username);
                    
                    case "%groupleave":
                        groupName.removeUser(username);
                    
                    case "groupmessage":
                        //broadcast message to everyone in the groupUser list.
                }

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

    public void sendMessage(Post message) {
        out.println(message.toString());
    }

    public String getUsername() {
        return username;
    }

    private Post getMessage(int msgId)
    {
        return Server.messageList.get(msgId);
    }
}

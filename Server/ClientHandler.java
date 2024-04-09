import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

import Classes.Post;

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
            Server.broadcast(username + " joined the server.");

            // Handle client messages
            String[] inputs = new String[2];
            String message;
            while ((message = in.readLine()) != null) {
                if (message.isEmpty()) {
                    continue;
                }
                String command = message.split(" ")[0];
                message = message.replace(command + " ", "");

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
                        Server.broadcast(username + " left the server.");
                        break;
                    case "%message":
                        out.println(getMessage(Integer.parseInt(message)).toString());
                        break;
                    //group 2
                    case "%groups":
                        
                    case "%groupusers":
                        
                    case "%groupjoin":

                    case "%groupleave":

                    case "%groupmessage":
                        String groupId = message.split(" ")[0];
                        message = message.replace(groupId + " ", "");
                        inputs = message.split("~");
                        Post userGroupPost = new Post(username, inputs[0], inputs[1]);
                        userGroupPost.setId(Server.messageList.size());
                        Server.messageList.add(userGroupPost); //change to group messagelist
                        //if(int.tryParse(groupId))
                        Server.broadcast(userGroupPost, Server.groups.get(groupId));
                        break;
                }
            }

            // Handle client leaving
            socket.close();
            Server.removeClient(this);
            Server.broadcast(username + " left the group.", null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private MessageGroup getGroup(String groupName)
    {
        for (MessageGroup group : Server.groups){
            if (group.getName().equals(groupName)){
                return group;
            }
            else{
                try{
                    if(Integer.parseInt(groupName) == group.getId())
                        return group;

                }catch(NumberFormatException e){
                    return null;
                }
                
            }

        }
        return new MessageGroup();
    }

    public synchronized void sendMessage(String message) {
        out.println(message);
    }

    public synchronized void sendMessage(String message, List<MessageGroup> groups)
    {
        for (MessageGroup group : groups) {
            if (group.getMembers().contains(this)) {
                sendMessage(message.toString());
            }
        }
    }

    public synchronized void sendMessage(Post message)
    {
        out.println(message.toString());
    }

    public synchronized void sendMessage(Post message, List<MessageGroup> groups) {
        for (MessageGroup group : groups) {
            if (group.getMembers().contains(this)) {
                sendMessage(message);
            }
        }
    }

    public String getUsername() {
        return username;
    }

    public synchronized void joinGroup(int id) { // Added
        Server.groups.get(id).addMember(this);
    }

    public synchronized void joinGroup(String groupName) { // Added
        for(MessageGroup group : Server.groups)
        {
            if(group.getName().equals(groupName))
                group.addMember(this);
        }
    }

    public synchronized void leaveGroup(int id) { // Added
        Server.groups.get(id).removeMember(this);
    }

    public synchronized void leaveGroup(String groupName) { // Added
        for(MessageGroup group : Server.groups)
        {
            if(group.getName().equals(groupName))
                group.removeMember(this);
        }
    }

    private Post getMessage(int msgId)
    {
        return Server.messageList.get(msgId);
    }
}

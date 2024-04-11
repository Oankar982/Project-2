import java.io.*;
import java.net.*;
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
            for(MessageGroup group : Server.groups){
                sendMessage(group.toString());
            }

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
                String groupId = "";

                if(command.contains("group")) //Commands that have a groupId/name
                {
                    try{
                        if(!command.equals("%groups"))
                        {
                            groupId = message.split(" ")[0];
                            if(groupId.contains("~"))
                                throw new ArrayIndexOutOfBoundsException("Missing GroupId");
                        }
                    }
                    catch(ArrayIndexOutOfBoundsException e)
                    {
                        e.printStackTrace();
                        break;
                    }
                    switch (command) {
                        case "%groupusers":
                            out.print(getGroup(groupId).getName() + "Users: ");
                            for(ClientHandler client: getGroup(groupId).getMembers())
                                out.println(client.username + ", ");
                            break;
                        case "%groupjoin":
                            getGroup(groupId).addMember(this);
                            break;
                        case "%groupleave":
                            getGroup(groupId).removeMember(this);
                            break;
                        case "%grouppost":
                            if(!message.contains("~")){
                                out.println("Please use the correct ~ post format");
                            }
                            else{
                                message = message.replace(groupId + " ", "");
                                inputs = message.split("~");
                                Post userGroupPost = new Post(username, inputs[0], inputs[1]);
                                userGroupPost.setGroupId(getGroup(groupId).getId());
                                userGroupPost.setId(getGroup(groupId).messages.size());
                                getGroup(groupId).messages.add(userGroupPost);
                                Server.broadcast(userGroupPost, getGroup(groupId));
                            }
                            break;
                        case "%groups":
                            if(message.equals("") || message.equals("%groups")){
                                for(MessageGroup group : Server.groups){
                                    out.println(group.toString());
                                }
                            }
                            else
                                out.println("%groups is a stand alone command not followed by anything");
                            break;
                            case "%groupmessage":
                        if(message.contains(".")){
                            message = message.replace(groupId + " ", "");
                            inputs = message.split("\\.");
                            try{
                                out.println(getGroup(inputs[0]).getMessage(Integer.parseInt(inputs[1])).toString());
                            }
                            catch(NumberFormatException e)
                            {
                                out.println("Please use a number for the Message Id.");
                            }
                        }
                        else
                        {
                            out.println("Please use the proper formatting for %groupmessage.");
                        }
                            break;
                        default:
                            out.println(command + " is not a valid command!");
                        
    
                    }
                }
                else //commands that don't have a groupId/name
                {
                    switch (command) {
                        case "%post":
                            if(!message.contains("~")){
                                out.println("Please use the correct ~ post format");
                            }
                            else{
                                inputs = message.split("~");
                                Post userPost = new Post(username, inputs[0], inputs[1]);
                                userPost.setId(Server.messageList.size());
                                Server.messageList.add(userPost);
                                Server.broadcast(userPost);
                            }
                            break;
                        case "%users":
                            if(message.equals(""))
                                sendMessage("Users: " + userList.toString());
                            else
                                out.println("%users is a stand alone command followed by nothing");
                            break;
                        case "%exit":
                            if(message.equals("")){
                                socket.close();
                                Server.removeClient(this);
                                Server.broadcast(username + " left the server.");
                            }
                            else
                                out.println("%exit is a stand alone command followed by nothing");
                            break;
                        case "%message":
                            try{
                                out.println(getMessage(Integer.parseInt(message)).toString());
                            }catch(NumberFormatException e){
                                out.println("Message ID was not an int");
                            }
                            
                            break;
                        default:
                            out.println(command + " is not a valid command!");
                    }
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
                }
                
            }
        }
        return null;
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

    private Post getMessage(int msgId)
    {
        return Server.messageList.get(msgId);
    }
}

import java.util.ArrayList;
import java.util.List;
import Classes.Post;

public class MessageGroup {
   private int id;
   private String name;
   private List<ClientHandler> members = new ArrayList<ClientHandler>();
   public List<Post> messages = new ArrayList<>();

   public MessageGroup(int id, String name)
   {
      this.id = id;
      this.name = name;
   }

   public synchronized List<ClientHandler> getMembers() {
      return this.members;
   }

   public synchronized void addMember(ClientHandler member) {
      Server.broadcast(member.getUsername() + " joined " + name, this);
      this.members.add(member);
   }

   public synchronized void removeMember(ClientHandler member) {
      Server.broadcast(member.getUsername() + " left " + name, this);
      this.members.remove(member);
   }

   public Post getMessage(int msgId)
    {
        return messages.get(msgId);
    }

   // Getters and setters for id and name
   public int getId() {
      return id;
   }

   public String getName() {
      return name;
   }

   public String toString()
   {
      return "-----\nId: " + id + "\nName: " + name + "\n-----\n";
   }
}
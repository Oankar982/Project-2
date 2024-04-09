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
      this.members.add(member);
      Server.broadcast(member.getUsername() + " joined " + name, this);
   }

   public synchronized void removeMember(ClientHandler member) {
      this.members.remove(member);
      Server.broadcast(member.getUsername() + " left " + name, this);
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

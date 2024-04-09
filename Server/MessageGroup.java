import java.util.ArrayList;
import java.util.List;

public class MessageGroup {
   private int id;
   private String name;
   private List<ClientHandler> members = new ArrayList<ClientHandler>();

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
   }

   public synchronized void removeMember(ClientHandler member) {
      this.members.remove(member);
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

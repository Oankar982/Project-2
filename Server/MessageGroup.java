import java.util.ArrayList;
import java.util.List;

public class MessageGroup {
   private String id;
   private String name;
   private List<ClientHandler> members = new ArrayList<ClientHandler>();
   private List<MessageGroup> clientGroups = new ArrayList<>();

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
   public String getId() {
      return id;
   }

   public void setId(String id) {
      this.id = id;
   }

   public String getName() {
      return name;
   }

   public void setName(String name) {
      this.name = name;
   }
}

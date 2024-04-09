package Classes;
import java.util.ArrayList;
import java.util.List;


//Okay what I'm thinking for this class is, we have 5 different groups, each with a different groupName
// %groupJoin <groupName> adds the user to the list of groupUsers
// %groupPost <groupName> broadcasts the following message to all users within the list groupUsers
// %groupLeave <groupName> removes the user from the list of groupUsers


public class Group {
    //private String userName;
    private String groupName;
    private List<String> groupUsers;

    public Group(String groupName){
        this.groupName = groupName;
        //
    }

    public void addUser(){
        groupUsers.add();
    }

    public void removeUsers(){
        groupUsers.remove();
    }
}

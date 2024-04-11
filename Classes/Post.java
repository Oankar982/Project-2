package Classes;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Post {
    private int msgId, groupId = -1; //num in array?
    private String senderName; //username
    private String postDate; //date time
    private String msgSub; //message Subject 
    private String msgParam;
    
    public Post(String senderName, String msgSub, String msgParam) {
        this.senderName = senderName;
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        this.postDate = dtf.format(LocalDate.now());
        this.msgSub = msgSub;
        this.msgParam = msgParam;
    }

    public String toString(){
        
        if(groupId >= 0)
            return "-----\nMsg ID: " + groupId + "." + msgId + "\n" + "Username: " + senderName + "\n" + "Date: " + postDate + "\n" + "Subject: " + msgSub + "\n" + "Message: " + msgParam + "\n-----";
        
        return "-----\nMsg ID: " + msgId + "\n" + "Username: " + senderName + "\n" + "Date: " + postDate + "\n" + "Subject: " + msgSub + "\n" + "Message: " + msgParam + "\n-----";
    }

    public void setId(int id)
    {
        msgId = id;
    }

    public void setGroupId(int groupId)
    {
        this.groupId = groupId;
    }
}
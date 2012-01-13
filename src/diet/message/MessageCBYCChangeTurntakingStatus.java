package diet.message;
import java.io.Serializable;

import diet.server.CbyC.DocInsert;

public class MessageCBYCChangeTurntakingStatus extends Message implements Serializable{

   private int newStatus;
   private DocInsert prefix;
   private long id=-1;
    
    public MessageCBYCChangeTurntakingStatus(String email,String username,int newStatus, DocInsert prefix) {
        super(email,username);
        this.setNewStatus(newStatus);
        this.prefix=prefix;
    }
    
     public MessageCBYCChangeTurntakingStatus(String email,String username,int newStatus, DocInsert prefix,long id) {
        super(email,username);
        this.setNewStatus(newStatus);
        this.prefix=prefix;
        this.id=id;
    }
   
    public int getNewStatus(){
        return newStatus;
    }
     
     public String getMessageClass(){
         return "CBYCChangeTurntakingStatus";
     }
     
     public DocInsert getPrefix(){
         return prefix;
     }

    public void setNewStatus(int newStatus) {
        this.newStatus = newStatus;
    }

    public long getID(){
        return id;
    }
   
}

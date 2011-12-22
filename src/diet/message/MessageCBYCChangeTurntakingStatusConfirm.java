package diet.message;
import java.io.Serializable;

public class MessageCBYCChangeTurntakingStatusConfirm extends Message implements Serializable{

   private int newStatus;
   private int oldStatus;
   private long id;
    
    public MessageCBYCChangeTurntakingStatusConfirm(String email,String username,int oldStatus,int newStatus, long id) {
        super(email,username);
        this.setNewStatus(newStatus);
        this.oldStatus=oldStatus;
        this.id=id;
    }
   
    public int getNewStatus(){
        return newStatus;
    }
     
     public String getMessageClass(){
         return "CBYCChangeTurntakingStatusConfirm";
     }
     

    public void setNewStatus(int newStatus) {
        this.newStatus = newStatus;
    }

    public long getId() {
        return id;
    }

    public int getOldStatus() {
        return oldStatus;
    }

   
}

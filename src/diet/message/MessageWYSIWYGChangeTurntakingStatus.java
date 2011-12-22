package diet.message;
import java.io.Serializable;

public class MessageWYSIWYGChangeTurntakingStatus extends Message implements Serializable{

   private int newStatus;
   private String msg;
    
    public MessageWYSIWYGChangeTurntakingStatus(String email,String username,int newStatus, String msg) {
        super(email,username);
        this.setNewStatus(newStatus);
        this.setMsg(msg);
    }
   
    public int getNewStatus(){
        return newStatus;
    }
     
     public String getMessageClass(){
         return "MessageWYSIWYGChangeInterceptionStatus";
     }
     
     public String getMsg(){
         return msg;
     }

    public void setNewStatus(int newStatus) {
        this.newStatus = newStatus;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}

package diet.message;
import java.io.Serializable;

public class MessageWYSIWYGTypingUnhinderedRequest extends Message implements Serializable{

    private int offsetFrmRight;
    private String txt;
    
    public MessageWYSIWYGTypingUnhinderedRequest(String email,String username,int offsetFrmRight,String txt) {
        super(email,username);
        this.setOffsetFrmRight(offsetFrmRight);
        this.setTxt(txt);
    }
   
     public int getOffsetFrmRight(){
         return offsetFrmRight;
     }
     public String getText(){
         return getTxt();
     }
     
     public String getMessageClass(){
         return "MessageWYSIWYGTypingUnhinderedRequest";
     }  

    public void setOffsetFrmRight(int offsetFrmRight) {
        this.offsetFrmRight = offsetFrmRight;
    }

    public String getTxt() {
        return txt;
    }

    public void setTxt(String txt) {
        this.txt = txt;
    }
}

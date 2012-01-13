package diet.message;
import java.io.Serializable;

public class MessageCBYCTypingUnhinderedRequest extends Message implements Serializable{

    private int offsetFrmRight;
    private String txt;
    
    private String elementString;
    private int elementStart;
    private int elementFinish;
    
    public MessageCBYCTypingUnhinderedRequest(String email,String username,int offsetFrmRight,String txtTyped,String elementText,int elementStart,int elementFinish) {
        super(email,username);
        this.setOffsetFrmRight(offsetFrmRight);
        this.setTxt(txtTyped);
        this.elementString=elementText;
        this.elementStart=elementStart;
        this.elementFinish=elementFinish;
    }

    public int getElementFinish() {
        return elementFinish;
    }

    public int getElementStart() {
        return elementStart;
    }

    public String getElementString() {
        return elementString;
    }
   
     public int getOffsetFrmRight(){
         return offsetFrmRight;
     }
     public String getText(){
         return getTxt();
     }
     
     public String getMessageClass(){
         return "CBYCTypingUnhinderedRequest";
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

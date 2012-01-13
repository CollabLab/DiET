package diet.message;

import java.io.Serializable;

import diet.server.CbyC.DocChange;

public class MessageCBYCDocChangeToClient extends Message implements Serializable{

    private int windowNumber;
    private DocChange dc;

    
    
    
    public MessageCBYCDocChangeToClient(String email, String username, int windowNumber, DocChange dc) {
        super(email, username);
        this.setWindowNumber(windowNumber);
        this.dc =dc;
    }
 
   
   public DocChange getDocChange(){
       return dc;
   } 
   
    public int getWindowNumber(){
        return windowNumber;
    }
    
    public String getMessageClass(){
       return "CBYCDocChangeToClient";
    }

    public void setWindowNumber(int windowNumber) {
        this.windowNumber = windowNumber;
    }

   

}

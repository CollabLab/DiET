package diet.message;

import java.io.Serializable;
import java.util.Date;

import diet.server.CbyC.DocChange;

public class MessageCBYCDocChangeFromClient extends Message implements Serializable{

    private int windowNumber;
    private DocChange dc;

    
    
    
    public MessageCBYCDocChangeFromClient(String email, String username, int windowNumber, DocChange dc) {
        super(email, username);
        this.setWindowNumber(windowNumber);
        this.dc =dc;
    }
 
   
    
   
    public int getWindowNumber(){
        return windowNumber;
    }
    
    public String getMessageClass(){
       return "CBYCDocChangeFromClient";
    }

    public void setWindowNumber(int windowNumber) {
        this.windowNumber = windowNumber;
    }

    public DocChange getDocChange(){
        return dc;
    }

    @Override
    public void timeStamp() {
        super.timeStamp();
        dc.setTimestamp(new Date().getTime());
    }
    

}

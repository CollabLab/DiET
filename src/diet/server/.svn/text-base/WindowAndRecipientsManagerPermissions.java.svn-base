package diet.server;

/**
 *
 * Stores for each Participant in which window they see text from another Participant. Also saves in which window they
 * see their own text.
 * 
 * @author user
 */
public class WindowAndRecipientsManagerPermissions {
    

    
    private boolean sendingEnabled;
    private int ownWindowNumber;
   
    public WindowAndRecipientsManagerPermissions(boolean sendingEnabled, int ownWindowNumber) {
      this.sendingEnabled=sendingEnabled;
      this.ownWindowNumber = ownWindowNumber;
    }
    

    public void setWindowNo(int ownWindowNumber){
        this.ownWindowNumber=ownWindowNumber;
    }
    
    public void setSendingEnabled(boolean sendingEnabled){
        this.sendingEnabled = sendingEnabled;
    }
    
    public int getWindowNo(){
        return ownWindowNumber;
    }
    
    public boolean getSendingEnabled(){
        return this.sendingEnabled;
    }
}


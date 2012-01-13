package diet.message;
import diet.debug.Debug;
import java.io.Serializable;
import java.util.Date;
import java.util.Vector;

import diet.parameters.IntParameter;
import diet.parameters.LongParameter;
import diet.parameters.StringParameter;

public class MessageChatTextFromClient extends Message implements Serializable {

  private Vector keyPresses;
  private boolean processed =false;
  private long startOfTyping;
  private long endOfTyping;
  private String text;
  private boolean hasBeenBlocked;

  
  
  public MessageChatTextFromClient(String email,String username,String t, long startOfTyping, boolean currentTurnBeingConstructedHasBeenBlocked, Vector keyPresses) {
    super(email,username);
    setText(t);
    this.setKeyPresses(keyPresses);
    this.setStartOfTyping(startOfTyping);
    setEndOfTyping(new Date().getTime());
    if(startOfTyping <0) this.setStartOfTyping(getEndOfTyping());
    //System.out.println("client created message with "+startOfTyping+" "+getEndOfTyping());
    //System.out.println("Text is: "+t);
    //System.out.println("hasbeenblocked:"+currentTurnBeingConstructedHasBeenBlocked);
    this.setHasBeenBlocked(currentTurnBeingConstructedHasBeenBlocked);
  }

public void timeStamp(){
  super.timeStamp();
  setStartOfTyping(super.getTimeStamp().getTime()  - (getEndOfTyping() - getStartOfTyping()));
  setEndOfTyping(super.getTimeStamp().getTime());
  if(Debug.verboseOUTPUT)System.out.println("MESSAGETIMESTAMPEDWITH "+getStartOfTyping()+" : "+getEndOfTyping());
}

  public String getText() {
    return text;
  }

  public Vector getKeypresses(){
     return keyPresses;
  }

  public int getNoOfDeletes(){
    //if(2<5)return 5;
    //8 or 127
    int delcount = 0;
    try{
    for (int i =0 ; i < getKeyPresses().size();i++){
       Keypress keyp = (Keypress)getKeyPresses().elementAt(i);
       if(keyp.isDel()){
          delcount = delcount+1;
       }
    }
    }catch(Exception e){}
    return delcount;
  }
  
  public boolean hasBeenBlocked(){
      return this.isHasBeenBlocked();
  }
  public long getTypingOnset(){
    return getStartOfTyping();
  }
  public long getEndOfTyping(){
    return endOfTyping;
  }

  public String getMessageClass(){
      return "ChatTextFromClient";
  }

  public Vector getAllParameters(){
    StringParameter sp1 = new StringParameter("Text", getText()); 
    StringParameter sp2 = new StringParameter("Keypresses",getKeyPresses().toString());
    IntParameter sp3 = new IntParameter("NoOfDels",getNoOfDeletes());
    StringParameter sp4 = new StringParameter("HasBeenBlocked",""+hasBeenBlocked());
    LongParameter sp5 = new LongParameter("TypingOnset", getStartOfTyping());
    LongParameter sp6 = new LongParameter("TypingOnset", getEndOfTyping());    
    Vector v = super.getAllParameters();
    v.addElement(sp1);
    v.addElement(sp2);
    v.addElement(sp3);   
    v.addElement(sp4);   
    v.addElement(sp5);   
    v.addElement(sp6);   
    return v; 
 } 

  

   

 

    public Vector getKeyPresses() {
        return keyPresses;
    }

    public void setKeyPresses(Vector keyPresses) {
        this.keyPresses = keyPresses;
    }

    public boolean isProcessed() {
        return processed;
    }

    public void setProcessed(boolean processed) {
        this.processed = processed;
    }

    public long getStartOfTyping() {
        return startOfTyping;
    }

    public void setStartOfTyping(long startOfTyping) {
        this.startOfTyping = startOfTyping;
    }

    public void setEndOfTyping(long endOfTyping) {
        this.endOfTyping = endOfTyping;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isHasBeenBlocked() {
        return hasBeenBlocked;
    }

    public void setHasBeenBlocked(boolean hasBeenBlocked) {
        this.hasBeenBlocked = hasBeenBlocked;
    }
  
  
}

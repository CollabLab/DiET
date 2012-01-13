

package diet.server.conversationhistory;
import java.io.Serializable;
import java.util.Vector;

import diet.server.conversationhistory.turn.Turn;



public class WordPartOfSpeechOccurrenceCounterSender implements Serializable{
   
  private Conversant sender;  
  private LexiconEntry le;
  private int counter;
  private Turn turnOfLastUse;
  private Vector recipients = new Vector();

public WordPartOfSpeechOccurrenceCounterSender(Turn t, LexiconEntry le) {
    this.sender = t.getSender();
    this.le=le;
    this.turnOfLastUse=t;
    counter=1;
    this.recipients=(Vector)t.getRecipients().clone();
}

public LexiconEntry getLexEntry(){
    return le;
}
public void update(Turn t){
    counter++;
    turnOfLastUse = t;
    Vector v = t.getRecipients(); 
    for(int i=0;i<v.size();i++){
        if(!recipients.contains(v.elementAt(i))){
            recipients.addElement(v.elementAt(i));
        }
    }
    
}

}

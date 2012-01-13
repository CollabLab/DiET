

package diet.server.conversationhistory;
import java.io.Serializable;
import java.util.Vector;

import diet.server.conversationhistory.turn.Turn;


public class WordPartOfSpeechOccurrenceCounterRecipient implements Serializable{
   
  private Conversant recipient;  
  private LexiconEntry le;
  private int counter;
  private Turn turnOfLastReceipt;
  private Vector senders=new Vector();

public WordPartOfSpeechOccurrenceCounterRecipient(Turn t, LexiconEntry le,Conversant c) {
    this.recipient = c;
    this.le=le;
    this.turnOfLastReceipt=t;
    counter=1;
    this.senders.addElement(t.getSender());
}

public LexiconEntry getLexEntry(){
    return le;
}
public void update(Turn t){
    counter++;
    turnOfLastReceipt=t;
    if(!senders.contains(t.getSender()))senders.addElement(t.getSender());
}

}
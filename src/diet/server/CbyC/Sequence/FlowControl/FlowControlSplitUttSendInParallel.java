/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package diet.server.CbyC.Sequence.FlowControl;

import java.util.Date;
import java.util.Vector;

import diet.message.MessageCBYCDocChangeToClient;
import diet.server.Conversation;
import diet.server.Participant;
import diet.server.CbyC.DocChange;
import diet.server.CbyC.DocInsert;
import diet.server.CbyC.FIFOBuffer;
import diet.server.CbyC.Sequence.Sequence;


/**
 *
 * @author Greg
 */
public class FlowControlSplitUttSendInParallel extends FlowControl implements Runnable{

    public FIFOBuffer fb;
    String recipient;
    String apparentSender;
    
    public FlowControlSplitUttSendInParallel(Sequence seq, String recipient,String apparentSender,Vector docChanges) {
        super(seq);
        this.recipient = recipient;
        this.apparentSender = apparentSender;
        fb = new FIFOBuffer(seq,seq.getSS().getcC(),0);   
        for(int i=0;i<docChanges.size();i++){
            this.addDocChange_ButNotToDocChangesBySender((DocChange)docChanges.elementAt(i));
        }
    }
   
    public synchronized void addDocChange_ButNotToDocChangesBySender(DocChange dc) {
               Conversation.printWSln("FLOWCONTROL", "A");
               DocChange dcCopy = dc.returnCopy();
               dcCopy.recipient= recipient;
               Participant appOrigin = seq.getSS().getcC().getC().getParticipants().findParticipantWithUsername(apparentSender);
               
               
               if(dc instanceof DocInsert){
                  DocInsert di = (DocInsert)dcCopy;
                  Participant p = seq.getSS().getcC().getC().getParticipants().findParticipantWithUsername(recipient);
                  int unique = seq.getSS().getStyleManager().getUniqueIntForRecipient(p, appOrigin);  
                  di.a = "n"+unique;
               }
               fb.enqueue(dcCopy);
    
    }
    
    public void setCompleted(){
        fb.setInputCompleted();
    }
    
    public void main(){
        Conversation.printWSln("FLOWCONTROL", "1"); 
        while(fb.getSize()>0||!fb.isInputCompleted()){
             Conversation.printWSln("FLOWCONTROL", "2");
            try{
                long oTimestamp=-99999;
                Object o = fb.getNextBlockingObeyingTrickle();
                DocChange dc = (DocChange)o;
                Participant recipientP = seq.getSS().getcC().getC().getParticipants().findParticipantWithUsername(dc.getRecipient());
                MessageCBYCDocChangeToClient mct = new MessageCBYCDocChangeToClient(dc.sender,dc.apparentSender,0,dc); 
                long timeStampSend = new Date().getTime();
                dc.setTimeStampOfSend(timeStampSend);
                recipientP.sendMessage(mct);
                seq.updateAllRecipients(recipientP.getUsername(), dc);
                oTimestamp=dc.getTimestamp();           
                Object oNext = fb.peekNonBlocking();
                if(oNext !=null && oNext instanceof DocChange &&oTimestamp>0){
                       DocChange dcNext = (DocChange)oNext;
                       long timeTosleep = dcNext.getTimestamp()-oTimestamp;
                       if(timeTosleep>0){
                              Thread.sleep(timeTosleep);
                        }  
                                  
                }
 
            }catch (Exception e){
                
                System.err.println("INSIDEERROR");
                e.printStackTrace();
            }
        }
           
        
    }
    
    public void run(){
        main();       
    }
    
    public void controlFlow(){
        Thread r = new Thread(this);
        r.start();
    }
}

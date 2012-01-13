/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package diet.server.CbyC.Sequence;

import java.util.Date;
import java.util.Vector;

import diet.message.MessageCBYCDocChangeToClient;
import diet.message.MessageCBYCTypingUnhinderedRequest;
import diet.server.Participant;
import diet.server.CbyC.DocChange;
import diet.server.CbyC.DocInsert;
import diet.server.CbyC.Sequences;
import diet.server.ConversationController.CCCBYCDefaultController;

/**
 *
 * @author Greg
 */
public class SequencePlaysPrerecordedSequence extends Sequence{
    
   

   

    public SequencePlaysPrerecordedSequence(Sequences sS, CCCBYCDefaultController cC, String sender, Date startTime, String elementText, int eStartPos, int eFinishPos,Vector prerecorded) {
        super(sS, cC, sender, startTime, elementText, eStartPos, eFinishPos);
        createHeader();
        for(int i=0;i<prerecorded.size();i++){
            DocChange dc = (DocChange)prerecorded.elementAt(i);
            this.addDocChangeFromPrior(dc);
        }
        
    }

    public SequencePlaysPrerecordedSequence(Sequences sS, CCCBYCDefaultController cC, String sender, MessageCBYCTypingUnhinderedRequest mCTUR) {
        super(sS, cC, sender, mCTUR);
    }
   
   
    private synchronized Sequence addDocChangeFromPrior(DocChange dc){
        Vector v = new Vector();
        Participant senderP = cC.getC().getParticipants().findParticipantWithUsername(dc.sender);
        Vector recipients = cC.getC().getParticipants().getAllOtherParticipants(cC.getC().getParticipants().findParticipantWithUsername(dc.getSender()));
        for(int i=0;i<recipients.size();i++){
            Participant p = (Participant)recipients.elementAt(i);
            DocChange dcCopy = dc.returnCopy();
            dcCopy.recipient=p.getUsername();
            if(dc instanceof DocInsert){
                DocInsert di = (DocInsert)dcCopy;
                int unique = sS.getStyleManager().getUniqueIntForRecipient(p, senderP);  
                di.a = "n"+unique;
            }
            v.addElement(dcCopy);
        }
        fb.enqueue(v);        
       
        return null;
    }
    
    
    
    
    @Override
    public void createHeader(){
        super.createHeader();
    }
    

    @Override
    public String getType(){
        return "Replayed sequence";
        
    }

    @Override
    public boolean hasBeenModified() {
        return true;
    }
    
   
    
    public void main(){
        
       
         
        
        while(!fb.isInputCompleted()||fb.getSize()>0){
           
            try{
                Object o = fb.getNextBlockingObeyingTrickle();
                long oTimestamp=-99999;
                if(o!=null|| o instanceof Vector){
                   
                    Vector v = (Vector)o;
                    for(int i=0;i<v.size();i++){                   
                        DocChange dc = (DocChange)v.elementAt(i);
                        Participant recipient = cC.getC().getParticipants().findParticipantWithUsername(dc.getRecipient());
                        MessageCBYCDocChangeToClient mct = new MessageCBYCDocChangeToClient(dc.sender,dc.apparentSender,0,dc);
                        this.timeOfLastSend=new Date().getTime();
                        if(!this.timeOfFirstSendBeenRecorded){
                            timeOfFirstSendBeenRecorded = true;
                            timeOfFirstSend = new Date().getTime();
                        }
                        recipient.sendMessage(mct);
                        updateAllRecipients(recipient.getUsername(),dc);
                        oTimestamp=dc.getTimestamp();
                    }
                    Object oNext = fb.peekNonBlocking();
                    if(oNext!=null&&oTimestamp>0){
                        Vector vNext = (Vector)oNext;
                        if(vNext.size()>0){
                            DocChange dcNext = (DocChange)vNext.elementAt(0);
                            long timeTosleep = dcNext.getTimestamp()-oTimestamp;
                            if(timeTosleep>0){
                              sS.sleep(timeTosleep*3);
                            }  
                        }
                        
                        
                    }
                    
                }
                
            }catch (Exception e){
                System.err.println("INSIDEERROR");
                e.printStackTrace();
            }
        }
        
        System.err.println("EXITING FROM "+this.getClass().toString()+this.getFinalText());
        
        
        return;
        
    }
    
    
    
}

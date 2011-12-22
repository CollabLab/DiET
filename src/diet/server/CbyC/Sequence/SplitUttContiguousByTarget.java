/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package diet.server.CbyC.Sequence;

import java.util.Date;
import java.util.Hashtable;
import java.util.Vector;

import diet.message.MessageCBYCDocChangeToClient;
import diet.server.Conversation;
import diet.server.Participant;
import diet.server.CbyC.DocChange;
import diet.server.CbyC.DocInsert;
import diet.server.CbyC.DocInsertHeader;
import diet.server.CbyC.Sequences;
import diet.server.ConversationController.CCCBYCDefaultController;

/**
 *
 * @author Greg
 */
public class SplitUttContiguousByTarget extends EditSequence{

    Sequence priorSeq;
    
      
    public Hashtable contigTurnsAppOriginForEachParticipant = new Hashtable(); //This is swapped to appOriginForEachParticipant on performing the split

    public SplitUttContiguousByTarget(SplitUttContiguousByTarget prior,Sequences sS, CCCBYCDefaultController cC, String sender, Date startTime, String elementText, int eStartPos, int eFinishPos) {
        super(sS, cC, sender, startTime, elementText, eStartPos, eFinishPos);
        this.priorSeq=prior;
        this.hasBeenModified=true;
        this.contigTurnsAppOriginForEachParticipant=prior.contigTurnsAppOriginForEachParticipant;
    }
    
    
    public SplitUttContiguousByTarget(SplitUtt prior,Sequences sS, CCCBYCDefaultController cC, String sender, Date startTime, String elementText, int eStartPos, int eFinishPos) {
        super(sS, cC, sender, startTime, elementText, eStartPos, eFinishPos);
        this.priorSeq=prior;
        this.hasBeenModified=true;
        this.contigTurnsAppOriginForEachParticipant=prior.secondHalfappOriginForEachParticipant;
    }

    public synchronized Sequence addDocChange(DocChange dc){  
            
            Vector v = new Vector();
            this.docChangesBySender.addElement(dc); 
            //Participant senderP = cC.getC().getParticipants().findParticipantWithUsername(dc.sender);
            Vector recipients = cC.getC().getParticipants().getAllOtherParticipants(cC.getC().getParticipants().findParticipantWithUsername(dc.getSender()));
            
            for(int i=0;i<recipients.size();i++){
               Participant p = (Participant)recipients.elementAt(i);
               DocChange dcCopy = dc.returnCopy();
               dcCopy.recipient=p.getUsername();
               if(dc instanceof DocInsert){
                  DocInsert di = (DocInsert)dcCopy;
                  Participant appOrigin = (Participant)this.contigTurnsAppOriginForEachParticipant.get(p);
                  int unique = sS.getStyleManager().getUniqueIntForRecipient(p, appOrigin);  
                  di.a = "n"+unique;
               }   
               v.addElement(dcCopy);
               
            }
          fb.enqueue(v);          
          return null;     
    }
    
    
    public String getApparentOriginForRecipient(String recipient) {
         Participant recp = (Participant)cC.getC().getParticipants().findParticipantWithUsername(recipient);
         if(recp==null)return "ERROR45a...no RECIPIENT FOR FIRST PART";    
         Participant p2 = (Participant)this.contigTurnsAppOriginForEachParticipant.get(recp);
         if(p2==null)return "ERROR46b....BAD HASHTABLE FOR FIRST PART";
         
         return p2.getUsername();            
    }
    
    
    
    
    
    public String getStatusWindowTextForRecipient(String recipient) {
         Participant recp = (Participant)cC.getC().getParticipants().findParticipantWithUsername(recipient);
         if(recp==null)return "ERROR45a...no RECIPIENT";
         Participant p = (Participant)this.contigTurnsAppOriginForEachParticipant.get(recp);
         if(p==null)return "ERROR46b....BAD HASHTABLE";
         return p.getUsername()+"...is typing";
    }
    
    public String getType(){
        return "CONTINUATION";
    }
   
    public void main(){
         while(!fb.isInputCompleted()||fb.getSize()>0){
           
            try{
                long oTimestamp=-99999;
                Object o = fb.getNextBlockingObeyingTrickle();
                 
                if(o!=null&& o instanceof Vector){
                    Vector v = (Vector)o;
                    this.informOthersOfTyping();
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
                        if(dc instanceof DocInsertHeader){
                            DocInsertHeader dih = (DocInsertHeader)dc;
                            Conversation.printWSln("HEADERS", "sending header "+dih.getStr()+" to "+recipient.getUsername());
                        }
                        long timeStampSend = new Date().getTime();
                        dc.setTimeStampOfSend(timeStampSend);
                        updateAllRecipients(recipient.getUsername(),dc);
                        oTimestamp=dc.getTimestamp();
                    }
                    Object oNext = fb.peekNonBlocking();
                    
                    if(oNext !=null && oNext instanceof Vector &&oTimestamp>0){
                        System.err.println("oNext IS1");
                        Vector vNext = (Vector)oNext;
                        if(vNext.size()>0){
                            DocChange dcNext = (DocChange)vNext.elementAt(0);
                            long timeTosleep = dcNext.getTimestamp()-oTimestamp;
                            if(timeTosleep>0){
                               Thread.sleep(timeTosleep);
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
        return ;
    }

 
    private void informOthersOfTyping(){
          Participant pSender = cC.getC().getParticipants().findParticipantWithUsername(sender);
          Vector v = cC.getC().getParticipants().getAllOtherParticipants(pSender);
          for(int i=0;i<v.size();i++){
              Participant p = (Participant)v.elementAt(i);
              String statusWindowDisplay = this.getStatusWindowTextForRecipient(p.getUsername());
              cC.getC().sendAndEnableLabelDisplayToALLStatusWindowsOfParticipant(p, statusWindowDisplay, true, true);
          }
    }
    
    
    
    
}

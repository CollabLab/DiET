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
import diet.server.CbyC.Sequence.FlowControl.FlowControl;
import diet.server.CbyC.Sequence.FlowControl.FlowControlDoSpoofResponsivityToTypingActivityOfParticipant;
import diet.server.CbyC.Sequence.FlowControl.FlowControlSplitUttDelayForSecondSplit;
import diet.server.CbyC.Sequence.FlowControl.FlowControlSplitUttNormalOperationResume;
import diet.server.CbyC.Sequence.FlowControl.FlowControlSplitUttSendInParallel;
import diet.server.ConversationController.CCCBYCDefaultController;


/**
 *
 * @author Greg
 */
public class SplitUtt extends Sequence{

    String recip1;
    String recip1AppOrigin;
    String recip2;
    String recip2AppOrigin;
    String recip3;
    String recip3AppOrigin;
    
    String secondHalfrecip1;
    String secondHalfrecip1AppOrigin;
    String secondHalfrecip2;
    String secondHalfrecip2AppOrigin;
    String secondHalfrecip3;
    String secondHalfrecip3AppOrigin;
    
    
    public Hashtable appOriginForEachParticipant = new Hashtable();
    public Hashtable secondHalfappOriginForEachParticipant = new Hashtable(); //This is swapped to appOriginForEachParticipant on performing the split
    //boolean targetCriteriaMet = false;
    //String actualSender;
    
   
    long delayBetweenFirstAndSecond = 4000;
    Vector vSecondHalf = new Vector();
    FlowControlDoSpoofResponsivityToTypingActivityOfParticipant fcspooflabel;
    FlowControlSplitUttSendInParallel fcu;
    
    public SplitUtt(Sequences sS, CCCBYCDefaultController cC, String sender, Date startTime, String elementText, int eStartPos, int eFinishPos) {
        super(sS, cC, sender, startTime, elementText, eStartPos, eFinishPos);
        
    }

    public SplitUtt(String actualSender,Sequences sS, Vector vFirstHalf,Vector vSecondHalf,String recip1, String recip1AppOrigin, String recip2, String recip2AppOrigin, String recip3, String recip3AppOrigin,String[] secondHalf) {
        super(sS, sS.getcC(), actualSender, new Date(), "", -99999, -99999);
        if(fcspooflabel==null)fcspooflabel = new FlowControlDoSpoofResponsivityToTypingActivityOfParticipant(this,sender,getSS().getcC().getIsTypingTimeOut());
      
        //this.actualSender=actualSender;
        this.recip1=recip1;
        this.recip2=recip2;
        this.recip3=recip3;
        this.recip1AppOrigin=recip1AppOrigin;
        this.recip2AppOrigin=recip2AppOrigin;
        this.recip3AppOrigin=recip3AppOrigin;
        
        
        this.secondHalfrecip1=secondHalf[0];
        this.secondHalfrecip1AppOrigin=secondHalf[1];
        this.secondHalfrecip2=secondHalf[2];
        this.secondHalfrecip2AppOrigin=secondHalf[3];
        this.secondHalfrecip3=secondHalf[4];
        this.secondHalfrecip3AppOrigin=secondHalf[5];
        
        
        Vector bothCombined = new Vector();
        for(int i=0;i<vFirstHalf.size();i++){
            bothCombined.addElement(vFirstHalf.elementAt(i));
        }
        for(int i=0;i<vSecondHalf.size();i++){
            bothCombined.addElement(vSecondHalf.elementAt(i));
        }
        fcu = new FlowControlSplitUttSendInParallel(this,recip3,recip3AppOrigin,bothCombined);
        
        
        createHeader();
        fb.enqueue(fcu);
        this.vSecondHalf=vSecondHalf;
        for(int i=0;i<vFirstHalf.size();i++){
            DocChange dc = (DocChange)vFirstHalf.elementAt(i);
            this.addDocChange_ButNotToDocChangesBySender(dc,this.appOriginForEachParticipant);    
        }
        this.informOthersOfTyping();
        
        FlowControlSplitUttDelayForSecondSplit fcd = new FlowControlSplitUttDelayForSecondSplit(this,delayBetweenFirstAndSecond);///CHANGE TO DYNAMICALLY RECORDED
        fb.enqueue(fcd);
        this.createHeader2ndHalf();
       
        for(int i=0;i<vSecondHalf.size();i++){
            DocChange dc = (DocChange)vSecondHalf.elementAt(i);
            this.addDocChange_ButNotToDocChangesBySender(dc,this.secondHalfappOriginForEachParticipant);    
        }
        fb.enqueue(fcspooflabel);
        
        FlowControlSplitUttNormalOperationResume fcnrm = new FlowControlSplitUttNormalOperationResume(this,actualSender);
        fb.enqueue(fcnrm);
        
        this.hasBeenModified=true;
    }
    
   
    

    public void createHeader(){
        Participant recipient1 = cC.getC().getParticipants().findParticipantWithUsername(recip1);
        Participant recipient2 = cC.getC().getParticipants().findParticipantWithUsername(recip2);
        Participant recipient3 = cC.getC().getParticipants().findParticipantWithUsername(recip3);
        
        Participant recipient1AppOrigin = cC.getC().getParticipants().findParticipantWithUsername(recip1AppOrigin);
        Participant recipient2AppOrigin = cC.getC().getParticipants().findParticipantWithUsername(recip2AppOrigin);
        Participant recipient3AppOrigin = cC.getC().getParticipants().findParticipantWithUsername(recip3AppOrigin);
        
        this.appOriginForEachParticipant.put(recipient1, recipient1AppOrigin);
        this.appOriginForEachParticipant.put(recipient2, recipient2AppOrigin);
        this.appOriginForEachParticipant.put(recipient3, recipient3AppOrigin);
        
        Conversation.printWSln("RECIPIENT", "FIRST HALF...TYPED BY: "+super.sender);
        Conversation.printWSln("RECIPIENT", recipient1.getUsername()+" "+recipient1AppOrigin.getUsername());
        Conversation.printWSln("RECIPIENT", recipient2.getUsername()+" "+recipient2AppOrigin.getUsername());
        Conversation.printWSln("RECIPIENT", recipient3.getUsername()+" "+recipient3AppOrigin.getUsername());
        
        DocInsertHeader dih1 = sS.getStyleManager().getPrefixForParticipant("",recipient1, recipient1AppOrigin);        
        DocInsertHeader dih2 = sS.getStyleManager().getPrefixForParticipant("",recipient2, recipient2AppOrigin);
        DocInsertHeader dih3 = sS.getStyleManager().getPrefixForParticipant("",recipient3, recipient3AppOrigin);
        
        //Conversation.printWSln("RECIPIENT","DOCHEADERS");
        //Conversation.printWSln("RECIPIENT",dih1.getRecipient()+ " will receive "+dih1.str);
        //Conversation.printWSln("RECIPIENT",dih2.getRecipient()+ " will receive "+dih2.str);
        //Conversation.printWSln("RECIPIENT",dih3.getRecipient()+ " will receive "+dih3.str);
        Vector v = new Vector();
        v.addElement(dih1);
        v.addElement(dih2);
        v.addElement(dih3);
        
        fb.enqueue(v);
    }

    private void createHeader2ndHalf(){
        
       
        
        Participant secondHalfrecipient1 = cC.getC().getParticipants().findParticipantWithUsername(secondHalfrecip1);
        Participant secondHalfrecipient2 = cC.getC().getParticipants().findParticipantWithUsername(secondHalfrecip2);
        Participant secondHalfrecipient3 = cC.getC().getParticipants().findParticipantWithUsername(secondHalfrecip3);
        
        Participant secondHalfrecipient1AppOrigin = cC.getC().getParticipants().findParticipantWithUsername(secondHalfrecip1AppOrigin);
        Participant secondHalfrecipient2AppOrigin = cC.getC().getParticipants().findParticipantWithUsername(secondHalfrecip2AppOrigin);
        Participant secondHalfrecipient3AppOrigin = cC.getC().getParticipants().findParticipantWithUsername(secondHalfrecip3AppOrigin);
        
        this.secondHalfappOriginForEachParticipant.put(secondHalfrecipient1, secondHalfrecipient1AppOrigin);
        this.secondHalfappOriginForEachParticipant.put(secondHalfrecipient2, secondHalfrecipient2AppOrigin);
        this.secondHalfappOriginForEachParticipant.put(secondHalfrecipient3, secondHalfrecipient3AppOrigin);
        
        Conversation.printWSln("RECIPIENT", "Sender "+super.sender);
        Conversation.printWSln("RECIPIENT", secondHalfrecipient1.getUsername()+" "+secondHalfrecipient1AppOrigin.getUsername());
        Conversation.printWSln("RECIPIENT", secondHalfrecipient2.getUsername()+" "+secondHalfrecipient2AppOrigin.getUsername());
        Conversation.printWSln("RECIPIENT", secondHalfrecipient3.getUsername()+" "+secondHalfrecipient3AppOrigin.getUsername());
        
        DocInsertHeader dih1 = sS.getStyleManager().getPrefixForParticipant("",secondHalfrecipient1, secondHalfrecipient1AppOrigin);
        DocInsertHeader dih2 = sS.getStyleManager().getPrefixForParticipant("",secondHalfrecipient2, secondHalfrecipient2AppOrigin);
        //DocInsertHeader dih3 = sS.getStyleManager().getPrefixForParticipant(secondHalfrecipient3, secondHalfrecipient3AppOrigin);
        
        
        //dih1.str=dih1.str+"....(Number2)";
        //dih2.str=dih2.str+"....(Number2)";
        Vector v = new Vector();
        v.addElement(dih1);
        v.addElement(dih2);
        //v.addElement(dih3);
        
        fb.enqueue(v);
    }
    
    
  
    synchronized Sequence addDocChange_ButNotToDocChangesBySender(DocChange dc,Hashtable appOrig) {
            Vector v = new Vector();
           
            Vector recipients = cC.getC().getParticipants().getAllOtherParticipants(cC.getC().getParticipants().findParticipantWithUsername(dc.getSender()));
            for(int i=0;i<recipients.size();i++){
               Participant p = (Participant)recipients.elementAt(i);
               if(!p.getUsername().equalsIgnoreCase(recip3)){
                   DocChange dcCopy = dc.returnCopy();
                   dcCopy.recipient=p.getUsername();
                   if(dc instanceof DocInsert){
                      DocInsert di = (DocInsert)dcCopy;
                      Participant appOrigin = (Participant)appOrig.get(p);
                      int unique = sS.getStyleManager().getUniqueIntForRecipient(p, appOrigin);  
                      di.a = "n"+unique;
                   }
                   v.addElement(dcCopy);
               }   
           }
               
        fb.enqueue(v);
        return null;
    
    }
    
    
    
    public synchronized Sequence addDocChange(DocChange dc){  
            this.fcspooflabel.sendFakeIsTypingMessage();
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
                  Participant appOrigin = (Participant)secondHalfappOriginForEachParticipant.get(p);
                  int unique = sS.getStyleManager().getUniqueIntForRecipient(p, appOrigin);  
                  di.a = "n"+unique;
               }
               if(dcCopy.recipient.equalsIgnoreCase(recip3)){
                  this.fcu.addDocChange_ButNotToDocChangesBySender(dcCopy);
                  
               }
               else{
                 v.addElement(dcCopy);
               }  
            }
          fb.enqueue(v);          
          return null;     
    }
    public synchronized boolean isSpeakerChangePermitted() {
        if(this.fb.getSize()>0)return false;
        if(this.fcu.fb.getSize()>0)return false;
        
        
        return super.isSpeakerChangePermitted();
    }

    @Override
    public String getApparentOriginForRecipient(String recipient) {
         Participant recp = (Participant)cC.getC().getParticipants().findParticipantWithUsername(recipient);
         if(recp==null)return "ERROR45a...no RECIPIENT FOR FIRST PART";
         Participant p = (Participant)this.appOriginForEachParticipant.get(recp);
         if(p==null)return "ERROR46b....BAD HASHTABLE FOR FIRST PART";
         
         
         Participant p2 = (Participant)this.secondHalfappOriginForEachParticipant.get(recp);
         if(p2==null)return "ERROR46b....BAD HASHTABLE FOR FIRST PART";
         
         return p.getUsername()+"-"+p2.getUsername();            
    }

    @Override
    public String getStatusWindowTextForRecipient(String recipient) {
        Participant recp = (Participant)cC.getC().getParticipants().findParticipantWithUsername(recipient);
         if(recp==null)return "ERROR45a...no RECIPIENT";
         Participant p = (Participant)this.appOriginForEachParticipant.get(recp);
         if(p==null)return "ERROR46b....BAD HASHTABLE";
         return p.getUsername()+"...is typing";
    }
    
    
 
   public String getType(){
        return "SPLIT1";
    }   
    
    public String getType(String recipientName){
        if(recipientName.equalsIgnoreCase(sender)){
            return "split.sender";
        }
        if(recipientName.equalsIgnoreCase(this.recip3)){
            return "split.recipient3";
        }
        return "split";
    }
   
    public synchronized void performBlock(){
        //second half = the new one
                       
        //set so that anything that comes in to here is part of the new one
    }
   
     public void main(){
         while(!fb.isInputCompleted()||fb.getSize()>0){
           
            try{
                long oTimestamp=-99999;
                Object o = fb.getNextBlockingObeyingTrickle();
                if(o instanceof FlowControl){
                    FlowControl fc = (FlowControl)o;
                    fc.controlFlow();
                }
                   
                else if(o!=null&& o instanceof Vector){
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

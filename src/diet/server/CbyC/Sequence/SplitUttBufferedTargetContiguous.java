/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package diet.server.CbyC.Sequence;

import java.util.Date;
import java.util.Vector;

import diet.debug.Debug;
import diet.message.MessageCBYCDocChangeToClient;
import diet.server.Conversation;
import diet.server.Participant;
import diet.server.CbyC.DocChange;
import diet.server.CbyC.DocInsert;
import diet.server.CbyC.Sequences;
import diet.server.CbyC.Sequence.FlowControl.FlowControlDoSpoofResponsivityToTypingActivityOfParticipant;
import diet.server.ConversationController.CCCBYCSplitUtterance;
import diet.textmanipulationmodules.SplitUtterance.UtteranceSplitter;

/**
 *
 * @author Greg
 */
public class SplitUttBufferedTargetContiguous extends Sequence {

    /*
     * Can be in 3 states....
     * 1. Buffering input...waiting for criteria to split / abort
     * 2. CREATED SPLIT
     * 3. ABORTED
     * 
     * 
     */
    
    CCCBYCSplitUtterance cC;
    
    String debugOfAbort = ".";
    
   
    boolean targetCriteriaMet = false;
    boolean hasAborted = false;
    boolean runCheckingThread=true;
    FlowControlDoSpoofResponsivityToTypingActivityOfParticipant fcspooflabel;
    
   
    public SplitUttBufferedTargetContiguous(Vector bufferedDocChanges,CCCBYCSplitUtterance cC,String actualSender,Sequences sS) {
        super(sS, sS.getcC(), actualSender, new Date(), "", -99999, -99999);
        //cC.fh.setNormalOperation(false);
        //Participant p = cC.getC().getParticipants().findParticipantWithUsername(actualSender);
        //cC.fh.giveFloorToParticipantBlockOthersWithNetworkError(p);
        for(int i=0;i<bufferedDocChanges.size();i++){
            super.addDocChange_ButNotToDocChangesBySender((DocChange)bufferedDocChanges.elementAt(i));    
        }
        this.cC=cC;
        this.start();
        this.hasBeenModified=true;
    }
    
  
    
    
    
     
 
    
     public synchronized Sequence addDocChange(DocChange dc){  
      if(!this.runCheckingThread)return super.addDocChange(dc);
      cC.fh.setAutomaticallyAllowOpenFloorAfterIsTypingTimeout(false);
      if(fcspooflabel==null)fcspooflabel = new FlowControlDoSpoofResponsivityToTypingActivityOfParticipant(this,sender,getSS().getcC().getIsTypingTimeOut());
      this.fcspooflabel.sendFakeIsTypingMessage();
      this.docChangesBySender.addElement(dc);
      if(!this.hasAborted&&this.haveCriteriaBeenMetForSplit()){
          //this.setInputClosed();
          CCCBYCSplitUtterance cbyc = (CCCBYCSplitUtterance)cC;
          
          String[][] ab = cbyc.getAppOriginsForIntervention_BothHalves(this, sender);  
          String[] a = ab[0];
          String[] b = ab[1];
         
          Vector[] utteranceSplit = UtteranceSplitter.splitUtterance(docChangesBySender);
          fcspooflabel.controlFlow();
          if(Debug.trackTypeError)debugOfAbort=debugOfAbort+"ADDDOCCHANGE1";
          return new SplitUtt(sender, sS,utteranceSplit[0], utteranceSplit[1],a[0], a[1],a[2],a[3],a[4],a[5],b);
          
      }
       
      return null;
    }
 
    
     
    
    public void createHeader(){
        
    }
   
    public synchronized boolean haveCriteriaBeenMetForSplit(){
        if(this.hasAborted)return false;
        String textSoFar = " "+super.getFinalText()+" ";
        if(this.docChangesBySender.size()>10){
            DocChange dc = (DocChange)docChangesBySender.lastElement();
            if(dc instanceof DocInsert){
                DocInsert di = (DocInsert)dc;
                if(!di.getStr().endsWith(" "))return false;
            }
            this.runCheckingThread=false;
            targetCriteriaMet=true;
            
            Conversation.printWSln("SPLITorABORT", "SPLIT: "+ textSoFar);
            return true;
        }
        return false;
    }
    
    public synchronized boolean haveCriteriaBeenMetForAbort(){
        if(this.targetCriteriaMet)return false;
        if(this.docChangesBySender.size()>0){
                  if(Debug.trackTypeError)debugOfAbort=debugOfAbort+"CRIT1_";
                  DocChange dcLast = (DocChange)this.docChangesBySender.lastElement();
                  long currTime = new Date().getTime();
                  String textSoFar = " "+super.getFinalText()+" ";
                  if(Debug.trackTypeError)debugOfAbort=debugOfAbort+"CRIT2_";
                  if(currTime-dcLast.getTimestamp()>10000 || textSoFar.indexOf(" me ")>0){
                      runCheckingThread=false;
                      Conversation.printWSln("SPLITorABORT", "ABORT: "+textSoFar+"..CurrTime="+currTime+" DcLast="+dcLast.getTimestamp()
                              +" Subtracted= ");
                      
                      super.createHeader();
                      if(Debug.trackTypeError)debugOfAbort=debugOfAbort+"CRIT3_";
                      for(int i=0;i<docChangesBySender.size();i++){
                           if(Debug.trackTypeError)debugOfAbort=debugOfAbort+"CRIT41_";
                           DocChange dc = (DocChange)docChangesBySender.elementAt(i);
                           super.addDocChange_ButNotToDocChangesBySender(dc);
                      }
                      if(Debug.trackTypeError)debugOfAbort=debugOfAbort+"CRIT5_";
                      fcspooflabel.controlFlow();
                      cC.fh.forceInformOthersOfTyping(cC.getC().getParticipants().findParticipantWithUsername(sender));
                      cC.fh.setAutomaticallyAllowOpenFloorAfterIsTypingTimeout(true);
                      if(Debug.trackTypeError)debugOfAbort=debugOfAbort+"CRIT6_";
                      cC.processBufferedTargetAbort();
                      return true;
                  }
                      
        }
        if(Debug.trackTypeError)debugOfAbort=debugOfAbort+"CRITB_";
        return false;
    }
    
    
    public void run(){
        //This method checks to see if the conditions are not met after a certain amount of time...
       while(runCheckingThread){
          try{
              if(this.haveCriteriaBeenMetForAbort()){
                   if(Debug.trackTypeError)debugOfAbort=debugOfAbort+"RUN_ABORTCRITERIAMET_";
              }  
              Thread.sleep(400);
         }catch (Exception e){
             this.cC.getC().saveErrorLog("ERRORCBYC1");
             if(Debug.trackTypeError)debugOfAbort=debugOfAbort+"CBYCERROR1_";
             this.cC.getC().saveErrorLog(e);
             Conversation.printWSln("Main", "CBYCERROR "+e.getMessage().toString());
         }  
      }    
    }
    
    
   
    
    public synchronized boolean isSpeakerChangePermitted() {
        if(targetCriteriaMet){
            return false;
        }
        else if(runCheckingThread){
            return false;
        }
        else{
            boolean speakerChangeIsPermitted = super.isSpeakerChangePermitted();
            if(speakerChangeIsPermitted){
                if(Debug.trackTypeError)debugOfAbort=debugOfAbort+"PERMITTEDSPEAKERCHANGE(TARGETCRITERIANOTMET)  ";
            }
            else{
                if(Debug.trackTypeError)debugOfAbort=debugOfAbort+"DIDNOTPERMITSPEAKERCHANGE(TARGETCRITERIANOTMET)";        
            }
            return speakerChangeIsPermitted;
        }    
        
    }

    @Override
    public String getApparentOriginForRecipient(String recipient) {
        return sender;
    }

    @Override
    public String getStatusWindowTextForRecipient(String recipient) {
        if(this.runCheckingThread)return "Network error...please wait";
        return super.getStatusWindowTextForRecipient(recipient);
    }
     
 
   public String getType(){
        if(this.targetCriteriaMet)return "Target CONTIG...split generated "+debugOfAbort;
        return "Target CONTIG...ABORTED"+debugOfAbort; 
    }

    @Override
    public boolean hasBeenModified() {
        return true;
    }

   
   
   
   public void main(){
                if(Debug.trackTypeError)debugOfAbort=debugOfAbort+"MAIN0_";
        while(!fb.isInputCompleted()||fb.getSize()>0){
           
            try{
                if(Debug.trackTypeError)debugOfAbort=debugOfAbort+"MAIN1_";
                Object o = fb.getNextBlockingObeyingTrickle();
                long oTimestamp=-99999;
                if(o!=null&& o instanceof Vector){
                    if(Debug.trackTypeError)debugOfAbort=debugOfAbort+"MAIN2_";
                    Vector v = (Vector)o;
                    for(int i=0;i<v.size();i++){
                        if(Debug.trackTypeError)debugOfAbort=debugOfAbort+"MAIN3_";
                        DocChange dc = (DocChange)v.elementAt(i);
                        Participant recipient = cC.getC().getParticipants().findParticipantWithUsername(dc.getRecipient());
                        MessageCBYCDocChangeToClient mct = new MessageCBYCDocChangeToClient(dc.sender,dc.apparentSender,0,dc);
                        this.timeOfLastSend=new Date().getTime();
                        if(!this.timeOfFirstSendBeenRecorded){
                            timeOfFirstSendBeenRecorded = true;
                            timeOfFirstSend = new Date().getTime();
                        }
                        recipient.sendMessage(mct);
                        long timeStampSend = new Date().getTime();
                        dc.setTimeStampOfSend(timeStampSend);
                        updateAllRecipients(recipient.getUsername(),dc);
                        oTimestamp=dc.getTimestamp();
                        if(Debug.trackTypeError)debugOfAbort=debugOfAbort+"MAIN4_";
                    }
                    Object oNext = fb.peekNonBlocking();
                    if(oNext!=null&&oTimestamp>0){
                        if(Debug.trackTypeError)debugOfAbort=debugOfAbort+"MAIN5_";
                        Vector vNext = (Vector)oNext;
                        if(Debug.trackTypeError)debugOfAbort=debugOfAbort+"MAIN6_";
                        if(vNext.size()>0){
                            if(Debug.trackTypeError)debugOfAbort=debugOfAbort+"MAIN7_";
                            DocChange dcNext = (DocChange)vNext.elementAt(0);
                            long timeTosleep = dcNext.getTimestamp()-oTimestamp;
                            if(timeTosleep>0){
                                if(Debug.trackTypeError)debugOfAbort=debugOfAbort+"MAIN8_";
                              sS.sleep(timeTosleep);
                            }  
                        }
                        
                        
                    }
                    
                }
                
            }catch (Exception e){
                 if(Debug.trackTypeError)debugOfAbort=debugOfAbort+"MAIN9_";
                 this.cC.getC().saveErrorLog("ERRORCBYC3");
                 this.cC.getC().saveErrorLog(e);
                 Conversation.printWSln("Main", "CBYCERROR "+e.getMessage().toString());
                 System.err.println("INSIDEERROR54");
                 e.printStackTrace();
            }
        }
        
        System.err.println("EXITING FROM "+this.getClass().toString()+this.getFinalText());
        
        if(Debug.trackTypeError)debugOfAbort=debugOfAbort+"MAIN10_";
        return ;
        
    }
   
   
}

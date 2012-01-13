/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package diet.server.CbyC.Sequence;

import java.util.Arrays;
import java.util.Date;
import java.util.Hashtable;
import java.util.Vector;

import diet.debug.Debug;
import diet.message.MessageCBYCDocChangeToClient;
import diet.message.MessageCBYCTypingUnhinderedRequest;
import diet.server.Conversation;
import diet.server.Participant;
import diet.server.StringOfDocChangeInserts;
import diet.server.CbyC.DocChange;
import diet.server.CbyC.DocInsert;
import diet.server.CbyC.FIFOBuffer;
import diet.server.CbyC.Sequences;
import diet.server.CbyC.Sequence.FlowControl.FlowControl;
import diet.server.ConversationController.CCCBYCDefaultController;

/**
 *
 * @author Greg
 */
public class Sequence extends Thread{
	
	/* 
	 * 
	 * 
	 * 
	 * */

    String sender;
    CCCBYCDefaultController cC;
    Sequences sS;
    FIFOBuffer fb;
    Vector docChangesBySender = new Vector();
    //Sequence next;
    
    Hashtable allRecipientsDocChanges = new Hashtable();
    boolean hasBeenModified = false;
     Date startTime;
    
    String elementText="";   // These three are now discontinued, however this data is still stored
    int elementStartPos;     // The main reason is that this variable is meant to be empty if the turn
    int elementFinishPos;    // is a normal speaker change. If it's an edit, it's meant to contain the text
                                     // of the turn being edited.
                                     // (1) When doing a "normal" speaker change, and this Sequence is constructed,
                                     //     the first elementString text will include the prior turn by the other speaker. 
                                     //     This is because of the slightly peculiar way in which the first String from
                                     //     each turn is enqueued, to be displayed only if the client gains the floor.
                                     // (2) If you look at how edit sequences are constructed by same speaker, then
                                     //     it will also work if the first doc edit is used
                                     // (3) Same for edit sequences by next speaker

    public boolean giveFloorRequestorTheFloor = true; //This overrides giving the floor to whoever typed first.
    private String type = "_default";
    
    public Date getStartTime() {
        return startTime;
    }
    
    boolean timeOfFirstSendBeenRecorded = false;
    long timeOfLastSend = -9999;
    long timeOfFirstSend = -99999;
   
    public Sequence(Sequences sS,CCCBYCDefaultController cC, String sender, MessageCBYCTypingUnhinderedRequest mCTUR){
        this.cC=cC;
        this.sS=sS;
        this.sender=sender;
        this.fb = new FIFOBuffer(this,cC,0);
        startTime = mCTUR.getTimeStamp(); 
        elementText = mCTUR.getElementString();
        elementStartPos = mCTUR.getElementStart();
        elementFinishPos = mCTUR.getElementFinish();       
    }
    public Sequence(Sequences sS,CCCBYCDefaultController cC, String sender) {
    	this.cC=cC;
    	this.sS=sS;
    	this.sender=sender;
    	this.fb=new FIFOBuffer(this, cC, 0);
    	startTime=new Date();
    	elementText="";   		
    }

    
     public Sequence(Sequences sS,CCCBYCDefaultController cC,String sender,Date startTime,String elementText,int eStartPos,int eFinishPos){
        this.cC=cC;
        this.sS=sS;
        this.sender=sender;
        this.fb = new FIFOBuffer(this,cC,0);
        this.startTime = startTime;
        this.elementText = elementText;
        elementStartPos = eStartPos;
        elementFinishPos = eFinishPos;
        
        if(this instanceof EditSequence){
           // cC.getC().printWln("CREATING", "MAKING NEW EDITSEQUENCE");
        }
        else{
          //  cC.getC().printWln("CREATING", "MAKING NEW SEQUENCE");
        }
       
    }
    
    
    public void createHeader(){
        Vector v = new Vector();
        Participant pSender = cC.getC().getParticipants().findParticipantWithUsername(sender);
        Vector recipients = cC.getC().getParticipants().getAllOtherParticipants(pSender);
        for(int i=0;i<recipients.size();i++){
            Participant p = (Participant)recipients.elementAt(i);
            DocInsert di = sS.getStyleManager().getPrefixForParticipant("s",p, pSender);
            System.err.println("SEQUENCEHEADERATTRIBUTE IS "+di.getAttrSet().toString());
            di.recipient=p.getUsername();
            v.addElement(di);
        }
        fb.insert(v, 0);
    }
    
    
   boolean firstDCSeen=false;
    public synchronized Sequence addDocChange_ButNotToDocChangesBySender(DocChange dc){
        try{  	
        	
          if(Debug.trackSeqADDDOCCHANGE)Conversation.printWSln("Main", "Sequence5a");   
          Vector v = new Vector();
          if(Debug.trackSeqADDDOCCHANGE)Conversation.printWSln("Main", "Sequence5b");   
          Participant senderP = cC.getC().getParticipants().findParticipantWithUsername(dc.sender);
          if(Debug.trackSeqADDDOCCHANGE)Conversation.printWSln("Main", "Sequence5c");   
          Vector recipients = cC.getC().getParticipants().getAllOtherParticipants(cC.getC().getParticipants().findParticipantWithUsername(dc.getSender()));
          if(Debug.trackSeqADDDOCCHANGE)Conversation.printWSln("Main", "Sequence5d "+recipients.size());
          for(int i=0;i<recipients.size();i++){
              Participant p = (Participant)recipients.elementAt(i);
              if(Debug.trackSeqADDDOCCHANGE)Conversation.printWSln("Main", "SequenceE");   
              DocChange dcCopy = dc.returnCopy();
              dcCopy.recipient=p.getUsername();
              if(dc instanceof DocInsert){
                DocInsert di = (DocInsert)dcCopy;
                int unique = sS.getStyleManager().getUniqueIntForRecipient(p, senderP);  
                di.a = "n"+unique;
                if(Debug.trackSeqADDDOCCHANGE)Conversation.printWSln("Main", "SequenceEa");   
            }
            if(Debug.trackSeqADDDOCCHANGE)Conversation.printWSln("Main", "Sequence5f");     
            v.addElement(dcCopy);
        }
        fb.enqueue(v);
        if(Debug.trackSeqADDDOCCHANGE)Conversation.printWSln("Main", "Sequence5g");
        
        if(Debug.trackSeqADDDOCCHANGE)Conversation.printWSln("Main", "Sequence5h");
        return null;
        }catch(Exception e){
            this.cC.getC().saveErrorLog("ERRORINADDOCCHANGEBUTNOTTODOCCHANGESBYSENDER");
            this.cC.getC().saveErrorLog(e);
            if(Debug.trackSeqADDDOCCHANGE)Conversation.printWSln("Main", "Sequence5i");
            return null;
        }
    }
    
    
    public int getRemainingOutputDocChanges()
    {
    	return fb.getSize();
    }
    
	public synchronized Sequence addDocChange(DocChange dc){
		
    	
    	
		if(!dc.sender.equalsIgnoreCase(sender)){
            if(dc instanceof DocInsert){
                DocInsert di = (DocInsert)dc;
                Conversation.printWSln("ERROR", "New DocChange from: "+dc.getSender()+", "+di.str);
            }    
            Conversation.printWSln("ERROR", "Existing Sequence: "+sender+", "+this.getFinalText());
            Conversation.printWSln("ERROR", "______________________________________________________");
        }
        Vector v = new Vector();
        this.docChangesBySender.addElement(dc); 
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
    
    //This method is called by FIFO after it has enqueued the DocChanges....
    //Use this method to modify the buffered text..
    public void postFIFOEnqueing(){
        
    }
    
    
    public String getFinalText(){
        

        if(this.docChangesBySender.size()>0){
            DocChange d1 = (DocChange)docChangesBySender.lastElement();
            DocChange dc = (DocChange)d1.returnCopy();
            String s = dc.elementString;
            dc.offs=-1+dc.offs-(dc.docSize-dc.elementFinish);
            s = s.replaceAll ("\\n", "");
            Vector textAsIns = StringOfDocChangeInserts.getInsEquivalentOfString(s);
            textAsIns.addElement(dc);
            StringOfDocChangeInserts sdi = new StringOfDocChangeInserts(textAsIns);
            return sdi.getString();
        }
        return "";
    }
    
    
    
    public long getTimeOfLastEnter(){
        if(docChangesBySender.size()>0){
           DocChange dc = (DocChange)docChangesBySender.lastElement();
           return dc.getTimestamp();
        }
        return this.startTime.getTime();
           
    }
    
    public long getTimeOfFirstSend(){
        return this.timeOfFirstSend;
    }
    public long getTimeOfLastSend(){
        return this.timeOfLastSend;
    }

    public void setType(String t){
        this.type=t;
    }

    public String getType(){
        if(this.hasBeenModified) {
            return type+"modified";
        }
        else{
            return type+"unmodified";
        }     
    }
    
    public Sequences getSS(){
        return sS;
    }
    
    public String getApparentOriginForRecipient(String recipient){
        return sender;  
    }
    
    public String getStatusWindowTextForRecipient(String recipient){
    	return sender+"...is typing";
    }
    
    
    public String getSender(){
        return sender;
    }
    
    public DocChange getFirstDocChangeBySender(){
        return (DocChange)docChangesBySender.elementAt(0);
    }
    
    public synchronized void setInputClosed(){
        fb.setInputCompleted();
    }
    
    public synchronized void setInputClosedEditOfOwnTurn(){
        setInputClosed();
    }
    public synchronized void setInputClosedEditOfOthersTurn(){
        setInputClosed();
    }
    public synchronized void setInputClosedSpeakerChange(){
        setInputClosed();
    }
    
    public boolean isSpeakerChangePermitted(){
        if(fb.isInputCompleted())return true;
        if(fb.getSize()==0)return true;
        return false;

  
    }
    
    public synchronized boolean isInputAndOutputFinished(){
        
        if(!fb.isInputCompleted())return false;
        if(fb.getSize()==0){
            return true;
        }
        return false;
    }
    
    
   
    //This is prior to the first elementStartPos as returned in the getElementStartPos() method
    //This is used solely to determine whether or not any subsequent edits are editing the same turn.
    //It needs to be initialized prior to the first DocChange being added.
    
    public int getStartPosSmearedIntoPrior(){
        return this.elementStartPos;
    }
    
    
    public String getElementText(){
        if(this.docChangesBySender.size()==0){
            return "";
        }
         DocChange dc = (DocChange)this.docChangesBySender.firstElement();
        return dc.elementString;
    }
    
    public int getElementStartPos(){
        if(this.docChangesBySender.size()==0){
            return -99999;
        }
        DocChange dc = (DocChange)this.docChangesBySender.firstElement();
        return dc.elementStart;
    }
    public int getElementFinishPos(){
        if(this.docChangesBySender.size()==0){
            return -99999;
        }
         DocChange dc = (DocChange)this.docChangesBySender.firstElement();
        return dc.elementFinish;
    }
    
    public Sequence returnNextForcedSequence(){
        return null;
    }
    
    

    public void main(){
        
        while(!fb.isInputCompleted()||fb.getSize()>0){
                Conversation.printWSln("SeqMain", "inside While Loop");
        	System.err.println("outputting sequence. current size is:"+fb.getSize());
            try{
                //sS.sleep(400);
                System.err.println("INSIDE2b");
                Object o = fb.getNextBlockingObeyingTrickle();
                if(o!=null&&Debug.showErrorsCalculatingDocInsertsSent){
                    
                    Conversation.printWSln("DOCINSERTSOUTOFFB", o.toString());
                }
                System.err.println("INSIDE3");
                if(o instanceof FlowControl){
                    System.err.println("INSIDE3b");
                    FlowControl fc = (FlowControl)o;
                    fc.controlFlow();
                }
                if(o!=null&& o instanceof Vector){
                    System.err.println("INSIDE4");
                    Vector v = (Vector)o;
                    for(int i=0;i<v.size();i++){
                        System.err.println("INSIDE5");
                        DocChange dc = (DocChange)v.elementAt(i);
                        System.err.println("INSIDE6");
                        Participant recipient = cC.getC().getParticipants().findParticipantWithUsername(dc.getRecipient());
                        System.err.print("INSIDE7:"+recipient.getUsername());
                        if(dc instanceof DocInsert){
                            DocInsert dci = (DocInsert)dc;
                            System.err.print("...."+dci.getStr()+"....."+dci.elementString);
                        }
                        System.err.println("");
                        MessageCBYCDocChangeToClient mct = new MessageCBYCDocChangeToClient(dc.sender,dc.apparentSender,0,dc);
                        System.err.println("INSIDE8");
                        this.timeOfLastSend=new Date().getTime();
                        if(!this.timeOfFirstSendBeenRecorded){
                            timeOfFirstSendBeenRecorded = true;
                            timeOfFirstSend = new Date().getTime();
                        }
                        long timeStampSend = new Date().getTime();
                        recipient.sendMessage(mct);
                        dc.setTimeStampOfSend(timeStampSend);
                        this.updateAllRecipients(recipient.getUsername(),dc);
                        System.err.println("INSIDE9");
                    }
                    
                    
                }
                
            }catch (Exception e){
                System.err.println("INSIDEERROR");
                e.printStackTrace();
            }
        }
        
        System.err.println("EXITING FROM "+this.getClass().toString()+this.getFinalText());
        
        
        return;
        //if has found clarification target then return a new Sequence of CR question (The CR question would return null from main(),
        //but would use the methods above.
        
        //The rule will be:
        //If you want to send something immediately to the sender, use the main() method
        //If you want to capture a response to a fake turn and NOT relay it, can use either (I think)
        //If you want to capture a response to a fake turn and relay it
        //If you want something to be carried out after how ever many edits, use a trojan version of EditSequence
        //If you want something to be carried out after how ever many turns, each time increasing the information, use a trojan version
        //If you want to split the turn
        //    If there's no buffer.....at a certain point, use main() to return a new second part sequence....
        //       --Will have a main() method that immediately returns
        
        
        /*The problem with this design is that it only suites probe CRs that are local to their
         * antecedents. this needs to change . . 
         * 
         *  Arash
         */
        
    }
    
    private void setHasBeenModified(boolean modified){
        this.hasBeenModified=modified;
    }
    
    public boolean hasBeenModified(){
        return hasBeenModified;
    }
    
    public Vector getAllRecipients(){
        Vector v = new Vector(Arrays.asList(this.allRecipientsDocChanges.keySet().toArray()));
        return v;
    }
    
    public Vector getAllDocChangesFromSender(){
        return this.docChangesBySender;
    }
    
    public void updateAllRecipients(String recipient,DocChange dc){
        
        if(!allRecipientsDocChanges.containsKey(recipient)){
            Vector docChanges = new Vector();
            docChanges.addElement(dc);
            allRecipientsDocChanges.put(recipient, docChanges);
        }
        else{
            Vector docChanges = (Vector)allRecipientsDocChanges.get(recipient);
            docChanges.addElement(dc);
            allRecipientsDocChanges.put(recipient, docChanges);
        }
        return; 
    }
    
    public Vector getRecipientsDocChanges(String recipient){
        Vector v = (Vector)this.allRecipientsDocChanges.get(recipient);
        if(v.size()==0)v=new Vector();
        return v;
    }
    
    public boolean getGiveFloorRequestorTheFloorOnStart(){
        return this.giveFloorRequestorTheFloor;
    }


}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package diet.server.CbyC.Sequence;

import java.util.Date;
import java.util.Random;
import java.util.Vector;

import diet.debug.Debug;
import diet.message.MessageCBYCDocChangeToClient;
import diet.message.MessageCBYCTypingUnhinderedRequest;
import diet.server.Conversation;
import diet.server.Participant;
import diet.server.CbyC.DocChange;
import diet.server.CbyC.DocInsert;
import diet.server.CbyC.DocRemove;
import diet.server.CbyC.DocReplace;
import diet.server.CbyC.Sequences;
import diet.server.CbyC.Sequence.FlowControl.FlowControl;
import diet.server.CbyC.Sequence.FlowControl.FlowControlFakeEditDelay;
import diet.server.ConversationController.CCCBYCDefaultController;
import diet.server.ConversationController.CCCBYCMazeGameFakeTypo;
import diet.textmanipulationmodules.SynonymDetector.DynamicSynonymModificationsForDyads;
import diet.textmanipulationmodules.TypoGenerator.TypoGenerator;

/**
 *
 * @author Greg
 */
public class MazeGameBufferedTypoReplacement extends MazeGameDefaultSequence{

    Random r = new Random();
    DynamicSynonymModificationsForDyads dsmfd;
    
    TypoGenerator tg = new TypoGenerator();
    
    CCCBYCMazeGameFakeTypo cCTypo;
    public boolean performedTransformation = false;
    String target = "NULL";
    String resultOfTransform = "NULL";

    public MazeGameBufferedTypoReplacement(Sequences sS, CCCBYCDefaultController cC, String sender, Date startTime, String elementText, int eStartPos, int eFinishPos,DynamicSynonymModificationsForDyads dsmfd) {
        super(sS, cC, sender, startTime, elementText, eStartPos, eFinishPos);
        cCTypo = (CCCBYCMazeGameFakeTypo)cC;
        this.dsmfd=dsmfd;  
    }
    
    
    
    

    public MazeGameBufferedTypoReplacement(Sequences sS, CCCBYCDefaultController cC, String sender, MessageCBYCTypingUnhinderedRequest mCTUR,DynamicSynonymModificationsForDyads dsmfd) {
        super(sS, cC, sender, mCTUR);
        cCTypo = (CCCBYCMazeGameFakeTypo)cC;
        this.dsmfd=dsmfd;   
    }

    @Override
    public synchronized Sequence addDocChange(DocChange dc) {
        fb.enqueue(new FlowControlFakeEditDelay(this,cCTypo.baselineDelay.getValue()+r.nextInt(cCTypo.variationInDelay.getValue())));
        return super.addDocChange(dc);
    }

    
    
    
    
    
    @Override
    public void postFIFOEnqueing() {
        if(this.performedTransformation)return;
        Vector v = fb.getAllContents();
        Conversation.printWSln("Main", "postFIFO "+v.size());
        boolean performReplacement = false;
        int lastIndexOfWord = 0;
        for(int i = v.size()-1;i>=0;i--){
            Conversation.printWSln("Main", " InsideForloop "+i);
            Object o = v.elementAt(i);
            if(o instanceof Vector){
                Conversation.printWSln("Main", " ISVECTOR "+i);
                 Vector vCmpl = (Vector)v.elementAt(i);
                 DocChange dc = (DocChange)vCmpl.elementAt(0);
                 lastIndexOfWord = i;
                 if(dc instanceof DocInsert){
                     DocInsert di = (DocInsert)dc;
                     if(di.str.startsWith(" ") || di.str.startsWith("\n")) {
                         performReplacement=true;
                         Conversation.printWSln("Main", "performing "+lastIndexOfWord);
                         break;
                     }
                 }
                 else if(dc instanceof DocRemove){
                     Conversation.printWSln("Main", "aborting1");
                     return;
                 }
                 else if(dc instanceof DocReplace){
                     Conversation.printWSln("Main", "aborting2");
                     return;
                 }
                 else{
                     Conversation.printWSln("Main", "NOTYET"+i);
                 }
            }
            
        }
        if(!performReplacement)return;
        
        performReplacement=false;
        
        int firstIndexOfWord = 0;
        for(int i = lastIndexOfWord-1;i>=0;i--){
            Conversation.printWSln("Main", " (2)InsideForloop "+i);
            Object o = v.elementAt(i);
            if(o instanceof Vector){
                Conversation.printWSln("Main", "(2) ISVECTOR "+i);
                 Vector vCmpl = (Vector)v.elementAt(i);
                 DocChange dc = (DocChange)vCmpl.elementAt(0);
                 firstIndexOfWord = i;
                 if(dc instanceof DocInsert){
                     DocInsert di = (DocInsert)dc;
                     if(di.str.startsWith(" ") || di.str.startsWith("\n")) {
                         performReplacement=true;
                         Conversation.printWSln("Main", "(2)performing "+firstIndexOfWord);
                         break;
                     }
                 }
                 else if(dc instanceof DocRemove){
                     Conversation.printWSln("Main", "(2)aborting1");
                     return;
                 }
                 else if(dc instanceof DocReplace){
                     Conversation.printWSln("Main", "(2)aborting2");
                     return;
                 }
                 else{
                     Conversation.printWSln("Main", "(2)NOTYET"+i);
                 }
            }
            
        }
        
        if(!performReplacement)return;
        
        
        Vector word = new Vector();
        for(int i=firstIndexOfWord;i<lastIndexOfWord;i++){
            Object o = v.elementAt(i);
            if(o instanceof Vector)word.addElement(o);
        }
        if(word.size()==0)return;
        word.removeElementAt(0);
        String enqWord ="";
        for(int i=0;i<word.size();i++){
            Vector vcmpl = (Vector)word.elementAt(i);
            DocInsert di  = (DocInsert)vcmpl.elementAt(0);
            enqWord = enqWord + di.str;
        }
        Conversation.printWSln("Main", "The word is "+enqWord+"....");
        
        Vector vParticipants = cC.getC().getParticipants().getAllParticipants();
        Participant p = cC.getC().getParticipants().findParticipantWithUsername(sender);
        if(p==null){Conversation.saveErr("Couldn't find participant");}
        int index = -1;
        index = vParticipants.indexOf(p);
        String synonym = null;
        if(index==0){
            synonym=this.dsmfd.lookupSynonymsAndRemoveParticipant1(enqWord);
        }
        else if(index==1){
            synonym=this.dsmfd.lookupSynonymsAndRemoveParticipant2(enqWord);
        }
        else{
            Conversation.saveErr("Couldn't find participant (error2)");
        }
        if(synonym==null)return;
        
        
        
        Vector vcmplOriginal = (Vector)word.elementAt(0);
        DocInsert diOriginal  = (DocInsert)vcmplOriginal.elementAt(0);
        
        int lastIndexForAddingRemoves= firstIndexOfWord+1;
        
        DocInsert di = diOriginal.returnCopy();
        Vector changes  = new Vector();
        changes= this.tg.createTypoForFakeEdit(this, enqWord, diOriginal.apparentSender, diOriginal.recipient, di.getAttrSet(),cCTypo.baselineDelay.getValue() , cCTypo.variationInDelay.getValue());
        
        this.resultOfTransform = tg.getString(changes);
        
        int lengthOfTypo =0;
        
        for(int i=0;i<changes.size();i++){
            Object o = changes.elementAt(i);
            if(o instanceof DocChange){
                Vector vN = new Vector();
                vN.addElement(o);
                fb.insertNoCallBack(vN, lastIndexForAddingRemoves);
                lengthOfTypo++;
            }
            else{
                fb.insertNoCallBack(o, lastIndexForAddingRemoves);
            }
            lastIndexForAddingRemoves++; 

            
        }
        
        
        //lastIndexForAddingRemoves = lastIndexForAddingRemoves+1;
       
        Conversation.printWSln("Main", "Performing replace");
        for(int i=0;i<lengthOfTypo;i++){
            
            Vector nwVcmpl = new Vector();
            DocRemove dr = new DocRemove("server",diOriginal.apparentSender,diOriginal.recipient,1,1);
            nwVcmpl.addElement(dr);
            
            fb.insertNoCallBack(new FlowControlFakeEditDelay(this,cCTypo.baselineDelay.getValue()+r.nextInt(cCTypo.variationInDelay.getValue())),lastIndexForAddingRemoves);
            lastIndexForAddingRemoves++;
            fb.insertNoCallBack(nwVcmpl,lastIndexForAddingRemoves);
            lastIndexForAddingRemoves++;
        }
            
        performedTransformation = true;
        this.target=enqWord;
        
        
    }

    @Override
    public String getType() {
        if(this.performedTransformation){
            return "transform "+ this.target + ">" + this.resultOfTransform;
        }   
        return "Target not detected";
    }

    @Override
    public boolean hasBeenModified() {
        return true;
    }

    

    
    
    
 
    
    
    
    
    
    public void main(){
        System.err.println("INSIDE1");
        while(!fb.isInputCompleted()||fb.getSize()>0){
            System.err.println("INSIDE2");
            try{
                //sS.sleep(400);
                System.err.println("INSIDE2b");
                Object o = fb.getNextBlockingObeyingTrickle();
                System.err.println("INSIDE3");
                if(o instanceof FlowControl){
                    FlowControl fc = (FlowControl)o;
                    fc.controlFlow();
                }
                if(o!=null&& o instanceof Vector){
                    System.err.println("INSIDE4");
                    Vector v = (Vector)o;
                    long oTimestamp=-99999;
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
                        oTimestamp=dc.getTimestamp();
                    }
                    Object oNext = fb.peekNonBlocking();
                    if(oNext!=null&& oNext instanceof Vector && oTimestamp>0){                     
                        Vector vNext = (Vector)oNext;
                        if(vNext.size()>0){   
                            DocChange dcNext = (DocChange)vNext.elementAt(0);
                            long timeTosleep = dcNext.getTimestamp()-oTimestamp;
                            if(timeTosleep>0){
                                 Conversation.printWSln("SLEEPINGBUFFER", "SLEEPING..."+timeTosleep);   
                                 //sS.sleep(timeTosleep*2);
                                    
                            }
                            else if(Debug.trackCBYCDyadError){
                                 Conversation.printWSln("SLEEPINGBUFFER", "NOT SLEEPING..."+timeTosleep);
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
        
        
        //Time of previous send (recorded)
        //Current time
        
        //
        
        
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
        
    }
    
    
}

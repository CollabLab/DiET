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
import diet.server.CbyC.Sequences;
import diet.server.CbyC.Sequence.FlowControl.FlowControl;
import diet.server.CbyC.Sequence.FlowControl.FlowControlFakeEditDelay;
import diet.server.ConversationController.CCCBYCDefaultController;
import diet.server.ConversationController.CCCBYCMazeGameFakeSynonymReplace;
import diet.server.ConversationController.CCCBYCMazeGameFakeTypo;
import diet.textmanipulationmodules.SynonymDetector.DynamicSynonymModificationsForDyads;

/**
 *
 * @author Greg
 */
public class MazeGameBuffered extends MazeGameDefaultSequence{

    Random r = new Random();
    DynamicSynonymModificationsForDyads dsmfd;

    public MazeGameBuffered(Sequences sS, CCCBYCDefaultController cC, String sender, Date startTime, String elementText, int eStartPos, int eFinishPos) {
        super(sS, cC, sender, startTime, elementText, eStartPos, eFinishPos);
    }
    
    
    
    
    
   

    public MazeGameBuffered(Sequences sS, CCCBYCDefaultController cC, String sender, MessageCBYCTypingUnhinderedRequest mCTUR, DynamicSynonymModificationsForDyads dsmfd) {
        super(sS, cC, sender, mCTUR);
        this.dsmfd=dsmfd;
    }

    @Override
    public synchronized Sequence addDocChange(DocChange dc) {
        if(cC instanceof CCCBYCMazeGameFakeSynonymReplace){
            CCCBYCMazeGameFakeSynonymReplace cCC = (CCCBYCMazeGameFakeSynonymReplace)cC;
            fb.enqueue(new FlowControlFakeEditDelay(this,cCC.baselineDelay.getValue()+r.nextInt(cCC.variationInDelay.getValue())));
        
        }
        else if (cC instanceof CCCBYCMazeGameFakeTypo){
            CCCBYCMazeGameFakeTypo cCC = (CCCBYCMazeGameFakeTypo)cC;
            fb.enqueue(new FlowControlFakeEditDelay(this,cCC.baselineDelay.getValue()+r.nextInt(cCC.variationInDelay.getValue())));
        
        }
        return super.addDocChange(dc);
    }

    @Override
    public void postFIFOEnqueing() {
        //Conversation.printWSln("POSTFIFO", "1");
    }

    

    @Override
    public String getType() {
        return "RandomBuffer";
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
        
        
        return;
        
        
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

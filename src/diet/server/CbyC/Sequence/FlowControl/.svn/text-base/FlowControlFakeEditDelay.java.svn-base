/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package diet.server.CbyC.Sequence.FlowControl;

import java.util.Date;
import java.util.Random;

import diet.server.Conversation;
import diet.server.Participant;
import diet.server.CbyC.Sequence.MazeGameBufferedSynonymReplacement;
import diet.server.CbyC.Sequence.MazeGameBufferedTypoReplacement;
import diet.server.CbyC.Sequence.Sequence;
import diet.server.ConversationController.CCCBYCMazeGameFakeEdit;

/**
 *
 * @author Greg
 */
public class FlowControlFakeEditDelay extends FlowControl{

    Random r = new Random();
    long delay=0;
    CCCBYCMazeGameFakeEdit cfk;
    
    public FlowControlFakeEditDelay(Sequence seq,long delay){
        super(seq);
        this.delay=delay;
        cfk = (CCCBYCMazeGameFakeEdit)seq.getSS().getcC();
        
    }

    @Override
    public void controlFlow() {
      Participant p = seq.getSS().getcC().getC().getParticipants().findParticipantWithUsername(seq.getSender());
      
      boolean shortenDelay = false;
      if(seq instanceof MazeGameBufferedSynonymReplacement){
          MazeGameBufferedSynonymReplacement seqM = (MazeGameBufferedSynonymReplacement)seq;
          if(!seqM.performedTransformation&&!p.isTyping(cfk.isTypingTimeOut.getValue())){
              shortenDelay = true;
          }
      }
      if(seq instanceof MazeGameBufferedTypoReplacement){
          MazeGameBufferedTypoReplacement seqM = (MazeGameBufferedTypoReplacement)seq;
          if(!seqM.performedTransformation&&!p.isTyping(cfk.isTypingTimeOut.getValue())){
              shortenDelay = true;
          }
      }
        long currTime = new Date().getTime();
        long typingPause = currTime-p.getConnection().getTimeOfLastTyping();
        long typingPauseSeconds = typingPause/1000;
        if(typingPauseSeconds<1)typingPauseSeconds=1;
        long div = (long)(Math.sqrt(typingPauseSeconds));
        
        try{ 
        if(delay>0){
           if(shortenDelay){
               Conversation.printWSln("Main","Shortening the delay time....");
               Thread.sleep(delay/div);
               
               return;
           }
           Thread.sleep(delay);
        }
       }catch(Exception e){
           
       }
    }
    
    
    
    public long getDelay(){
        return delay;
    }
}

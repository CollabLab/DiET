/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package diet.server.CbyC.Sequence;


import diet.message.MessageCBYCTypingUnhinderedRequest;
import diet.server.CbyC.FloorHolderAdvanced;
import diet.server.CbyC.Sequence.FlowControl.FlowControlDelay;
import diet.server.CbyC.Sequence.FlowControl.FlowControlSetInputClosedAndOpenFloorPostDummyDelay;
import diet.server.Participant;
import diet.server.CbyC.Sequences;
import diet.server.ConversationController.CCCBYCDefaultController;
import java.util.Date;
import java.util.Random;

/**
 *
 * @author Greg
 */
public class MazeGameDummyBlockingOnTextEntry extends MazeGameDefaultSequence{

    Random r = new Random();

    Participant blockedOnKeyPressP;
    Participant otherP;

    String clarificationRequestText = "";
    FloorHolderAdvanced fh2 ;

    public MazeGameDummyBlockingOnTextEntry(Sequences sS, CCCBYCDefaultController cC, String other, String blockedOnKeyPress, MessageCBYCTypingUnhinderedRequest mCTUR, long blockTime) {
        super(sS, cC, "server", mCTUR);
        super.giveFloorRequestorTheFloor=false;
        fh2= (FloorHolderAdvanced)cC.fh;
        fh2.fullyBlock();
        cC.fh.setAutomaticallyAllowOpenFloorAfterIsTypingTimeout(false);
        ((FloorHolderAdvanced)cC.fh).blockAllIncomingFloorRequests(true);
        blockedOnKeyPressP = cC.getC().getParticipants().findParticipantWithUsername(blockedOnKeyPress);
        this.otherP = cC.getC().getParticipants().findParticipantWithUsername(other);
        this.timeOfFirstSend= new Date().getTime();
        this.startTime = new Date();
    }

    



    public void createHeader(){    
            fb.enqueue(new FlowControlDelay(this, r.nextInt(8000)));
            FlowControlSetInputClosedAndOpenFloorPostDummyDelay fc = new FlowControlSetInputClosedAndOpenFloorPostDummyDelay(this);
            fb.enqueue(fc);
    }


    public void blockTimedOUT(){
        this.timeOfLastSend= new Date().getTime();
        
    }

    public long getTimeOfLastEnter(){
        return this.timeOfLastSend;
    }



    @Override
    public boolean getGiveFloorRequestorTheFloorOnStart() {
        return false;
    }
    
  

   

    @Override
    public String getType() {
        return "MG_DUMMY_BLOCK";
    }

    @Override
    public boolean hasBeenModified() {
        return false;
    }
    

 

}

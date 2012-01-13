/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package diet.server.CbyC.Sequence;

import java.util.Date;

import client.MazeGameController2WAY;
import diet.message.MessageCBYCTypingUnhinderedRequest;
import diet.server.Participant;
import diet.server.CbyC.Sequences;
import diet.server.ConversationController.CCCBYCDefaultController;
import diet.task.TaskController;

/**
 *
 * @author Greg
 */
public class MazeGameDefaultSequence extends Sequence{

    public int mazeNo=0;
    public int moveNoATSTART=0;
    public int moveNoATEND=0;

    public int senderMoveNoATSTART;
    public int senderMoveNoATEND;

    public boolean senderIsOnSwitchAtBegin = false;
    public int senderSwitchTraversalCountAtBegin=0;
    public int recipientMoveNoATSTART=0;
    public boolean recipientIsOnSwitchAtBegin = false;
    public int recipientSwitchTraversalCountAtBegin=0;



    public boolean senderIsOnSwitchAtEnd = false;
    public int senderSwitchTraversalCountAtEnd=0;
    public int recipientMoveNoATEND=0;
    public boolean recipientIsOnSwitchAtEnd = false;
    public int recipientSwitchTraversalCountAtEnd=0;


    
    private void getMazeGameDataStart(){
        TaskController tc = cC.getC().getTaskController();
        if(sender.equalsIgnoreCase("server"))return;
        if(tc==null)return;
        if(tc instanceof MazeGameController2WAY){
            MazeGameController2WAY tcm = (MazeGameController2WAY)tc;
            mazeNo =tcm.getMazeNo();
            moveNoATSTART = tcm.getMoveNo();

            String participantDataToUse = sender; //check that it's not server...otherwise there is problem


            Participant ps = (Participant)cC.getC().getParticipants().findParticipantWithUsername(sender);
            senderMoveNoATSTART = tcm.getParticipantsMoveNo(ps);
            senderIsOnSwitchAtBegin = tcm.isAonOthersSwitch(sender);
            senderSwitchTraversalCountAtBegin=tcm.getSwitchTraversalCount(sender);
            recipientMoveNoATSTART = tcm.getOthersMoveNo(sender);
            recipientIsOnSwitchAtBegin = tcm.isOtherSOnAswitch(sender);
            recipientSwitchTraversalCountAtBegin=tcm.getOthersSwitchTraversalCount(sender);
        }  
    }
    
    private void getMazeGameDataEND(){
        if(sender.equalsIgnoreCase("server"))return;
        TaskController tc = cC.getC().getTaskController();
        if(tc==null)return;
        if(tc instanceof MazeGameController2WAY){
            MazeGameController2WAY tcm = (MazeGameController2WAY)tc;
            moveNoATEND = tcm.getMoveNo();
            Participant ps = (Participant)cC.getC().getParticipants().findParticipantWithUsername(sender);
            senderMoveNoATEND = tcm.getParticipantsMoveNo(ps);
            senderIsOnSwitchAtEnd = tcm.isAonOthersSwitch(sender);;
            senderSwitchTraversalCountAtEnd = tcm.getSwitchTraversalCount(sender);
            recipientMoveNoATEND = tcm.getOthersMoveNo(sender);
            recipientIsOnSwitchAtEnd = tcm.isOtherSOnAswitch(sender);
            recipientSwitchTraversalCountAtEnd=tcm.getOthersSwitchTraversalCount(sender);

        }  
    }

    @Override
    synchronized public void setInputClosed() {
        super.setInputClosed();
        getMazeGameDataEND();
    }

    @Override
    public synchronized void setInputClosedEditOfOthersTurn() {
        super.setInputClosedEditOfOthersTurn();
        getMazeGameDataEND();
    }

    @Override
    public synchronized void setInputClosedEditOfOwnTurn() {
        super.setInputClosedEditOfOwnTurn();
        getMazeGameDataEND();
    }

    @Override
    public synchronized void setInputClosedSpeakerChange() {
        super.setInputClosedSpeakerChange();
        getMazeGameDataEND();
    }

    

    @Override
    public boolean hasBeenModified() {
        return false;
    }

    public MazeGameDefaultSequence(Sequences sS, CCCBYCDefaultController cC, String sender) {
        super(sS,cC,sender);
    }
    
    
    
    
    
    public MazeGameDefaultSequence(Sequences sS, CCCBYCDefaultController cC, String sender, Date startTime, String elementText, int eStartPos, int eFinishPos) {
        super(sS, cC, sender, startTime, elementText, eStartPos, eFinishPos);
        this.getMazeGameDataStart();
    }

    public MazeGameDefaultSequence(Sequences sS, CCCBYCDefaultController cC, String sender, MessageCBYCTypingUnhinderedRequest mCTUR) {
        super(sS, cC, sender, mCTUR);
        this.getMazeGameDataStart();
    }

}

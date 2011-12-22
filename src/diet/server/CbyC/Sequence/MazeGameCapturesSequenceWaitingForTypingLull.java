/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package diet.server.CbyC.Sequence;

import diet.debug.Debug;
import diet.message.MessageCBYCTypingUnhinderedRequest;
import diet.server.CbyC.DocChange;
import diet.server.CbyC.Sequence.FlowControl.FlowControlDoSpoofResponsivityToTypingActivityOfParticipant;
import diet.server.CbyC.Sequences;
import diet.server.Conversation;
import diet.server.ConversationController.CCCBYCDefaultController;
import diet.server.Participant;
import java.util.Date;

/**
 *
 * @author sre
 */
public class MazeGameCapturesSequenceWaitingForTypingLull extends MazeGameDefaultSequence{

     FlowControlDoSpoofResponsivityToTypingActivityOfParticipant fcd;



    public MazeGameCapturesSequenceWaitingForTypingLull(Sequences sS, CCCBYCDefaultController cC, String sender, Date startTime, String elementText, int eStartPos, int eFinishPos) {
        super(sS, cC, sender, startTime, elementText, eStartPos, eFinishPos);

    }

    public MazeGameCapturesSequenceWaitingForTypingLull(Sequences sS, CCCBYCDefaultController cC, String sender, MessageCBYCTypingUnhinderedRequest mCTUR) {
        super(sS, cC, sender, mCTUR);
        this.startTime=new Date();
    }






    ///Server set state of client to 2
    ///Client IS at 2 -
    ///Requests floor for the response
    ///Is automatically granted it (but as a continuation)
    ///Sends a docchange....and the


    boolean participantHASSTARTEDTYPING = false;
    int dncnt =0;

    @Override
    public synchronized Sequence addDocChange(DocChange dc) {
        if(dncnt==0) {
            fcd = new FlowControlDoSpoofResponsivityToTypingActivityOfParticipant(this, this.sender, this.getSS().getcC().getIsTypingTimeOut());
            this.startTime=new Date();
        }
        fcd.sendFakeIsTypingMessage();
        this.docChangesBySender.add(dc);

        this.participantHASSTARTEDTYPING=true;
        dncnt++;
        String dbgms ="";  if(Debug.showEOFCRSTATES)dbgms="(MAZEGAMECAPTURING RESPONSE...ADDING CHAR )"+dncnt;
        Participant p = getSS().getcC().getC().getParticipants().findParticipantWithUsername(sender);
        //getSS().getcC().getC().sendLabelDisplayAndEnableToParticipantInOwnStatusWindow(p, "Please type"+dbgms, false, true);

        //super.addDocChange(dc);
        return null;
    }


    public synchronized void lullDetected(){
         if(fcd!=null) fcd.stopSpoofIsTypingMessages();
        super.setInputClosedSpeakerChange();

    }

    @Override
    public void createHeader() {
        //super.createHeader();
    }

    @Override
    public boolean getGiveFloorRequestorTheFloorOnStart() {
        return super.getGiveFloorRequestorTheFloorOnStart();
    }


     public boolean hasStartedTyping(){
         return this.participantHASSTARTEDTYPING;
     }

    @Override
    public synchronized void setInputClosed() {
        Conversation.printWSln("Main", "CLOSING1");
        if(fcd!=null) fcd.stopSpoofIsTypingMessages();
         Conversation.printWSln("Main", "CLOSING2");
        super.setInputClosed();
    }

    @Override
    public synchronized void setInputClosedSpeakerChange() {
         Conversation.printWSln("Main", "CLOSING1A");
        if(fcd!=null) fcd.stopSpoofIsTypingMessages();
          Conversation.printWSln("Main", "CLOSING2B");
        super.setInputClosedSpeakerChange();

    }

    @Override
    public synchronized void setInputClosedEditOfOthersTurn() {
        if(fcd!=null) fcd.stopSpoofIsTypingMessages();
        super.setInputClosed();
    }

    @Override
    public synchronized void setInputClosedEditOfOwnTurn() {
       if(fcd!=null) fcd.stopSpoofIsTypingMessages();
        super.setInputClosed();
    }



}

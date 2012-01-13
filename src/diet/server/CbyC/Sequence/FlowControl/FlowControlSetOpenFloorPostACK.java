/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package diet.server.CbyC.Sequence.FlowControl;

import diet.client.CBYCDocumentWithEnforcedTurntaking;
import diet.debug.Debug;
import diet.server.CbyC.FloorHolderAdvanced;
import diet.server.CbyC.Sequence.MazeGameAcknowledgement;
import diet.server.CbyC.Sequence.Sequence;
import diet.server.ConversationController.CCCBYCMazeGameFakeEndOfTurnCRGenerator;
import diet.server.Participant;

/**
 *
 * @author Greg
 */
public class FlowControlSetOpenFloorPostACK extends FlowControl{

    public FlowControlSetOpenFloorPostACK(Sequence seq) {
        super(seq);
    }

    @Override
    public void controlFlow() {
        FloorHolderAdvanced fh2 = (FloorHolderAdvanced)seq.getSS().getcC().fh;
        String recipName = ((MazeGameAcknowledgement)seq).recipientOfACK;
        Participant pRecipient = seq.getSS().getcC().getC().getParticipants().findParticipantWithUsername(recipName);
        String dbgms ="";  if(Debug.showEOFCRSTATES)dbgms="(DEBUG:ACKSENT..SHOULD BE OPENED)";
        seq.getSS().getcC().getC().sendLabelDisplayAndEnableToParticipantInOwnStatusWindow(pRecipient, "Please type"+dbgms, false, true);
        seq.setInputClosedEditOfOthersTurn();
        CCCBYCMazeGameFakeEndOfTurnCRGenerator ccMG = (CCCBYCMazeGameFakeEndOfTurnCRGenerator)seq.getSS().getcC();
        ccMG.setState(0);

        fh2.resumeNormalOperation();

        //fh2.setInformOthersOfTyping(true);
        //fh2.allowAllIncomingFloorRequests(true);
        //fh2.changeFloorStatusOfParticipantsNoPrefix(seq.getSS().getcC().getC().getParticipants().getAllParticipants(), 2);
        //fh2.setAutomaticallyAllowOpenFloorAfterIsTypingTimeout(true);

        //ccMG.

    }

}

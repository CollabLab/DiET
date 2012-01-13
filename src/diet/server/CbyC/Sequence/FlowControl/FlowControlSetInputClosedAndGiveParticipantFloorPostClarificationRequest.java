/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package diet.server.CbyC.Sequence.FlowControl;

import diet.client.CBYCDocumentWithEnforcedTurntaking;
import diet.debug.Debug;
import diet.server.CbyC.FloorHolderAdvanced;
import diet.server.CbyC.Sequence.MazeGameClarificationRequest;
import diet.server.CbyC.Sequence.Sequence;
import diet.server.ConversationController.CCCBYCMazeGameFakeEndOfTurnCRGenerator;
import diet.server.Participant;

/**
 *
 * @author Greg
 */
public class FlowControlSetInputClosedAndGiveParticipantFloorPostClarificationRequest extends FlowControl{

    public FlowControlSetInputClosedAndGiveParticipantFloorPostClarificationRequest(Sequence seq) {
        super(seq);
    }

    @Override
    public void controlFlow() {
        FloorHolderAdvanced fh2 = (FloorHolderAdvanced)seq.getSS().getcC().fh;
        String recipName = ((MazeGameClarificationRequest)seq).recipientOfCR;
        Participant pRecipient = seq.getSS().getcC().getC().getParticipants().findParticipantWithUsername(recipName);
        fh2.onlyAllowParticipantToRequestFloor(pRecipient,true);
        fh2.changeFloorStatusOfParticipantNoPrefix(pRecipient, CBYCDocumentWithEnforcedTurntaking.nooneelsetyping);
        String dbgms ="";  if(Debug.showEOFCRSTATES)dbgms="(DEBUG:CRSENT_WAITINGFORRESPONSE)";
        seq.getSS().getcC().getC().sendLabelDisplayAndEnableToParticipantInOwnStatusWindow(pRecipient, "Please type"+dbgms, false, true);
        //seq.getSS().getcC().fh.setAutomaticallyAllowOpenFloorAfterIsTypingTimeout(true);
        seq.setInputClosedEditOfOthersTurn();
        CCCBYCMazeGameFakeEndOfTurnCRGenerator ccMG = (CCCBYCMazeGameFakeEndOfTurnCRGenerator)seq.getSS().getcC();
        ccMG.setState(2);
        //ccMG.

    }

}

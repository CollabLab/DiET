/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package diet.server.CbyC.Sequence.FlowControl;

import diet.server.CbyC.FloorHolderAdvanced;
import diet.server.CbyC.Sequence.MazeGameDummyBlockingOnTextEntry;
import diet.server.ConversationController.CCCBYCMazeGameFakeEndOfTurnCRGenerator;

/**
 *
 * @author Greg
 */
public class FlowControlSetInputClosedAndOpenFloorPostDummyDelay extends FlowControl{

    MazeGameDummyBlockingOnTextEntry mgblock;

    public FlowControlSetInputClosedAndOpenFloorPostDummyDelay(MazeGameDummyBlockingOnTextEntry seq) {
        super(seq);
        this.mgblock=seq;
    }

    @Override
    public void controlFlow() {
        FloorHolderAdvanced fh2 = (FloorHolderAdvanced)seq.getSS().getcC().fh;
        //fh2.allowAllIncomingFloorRequests(true);
        fh2.resumeNormalOperation();
        CCCBYCMazeGameFakeEndOfTurnCRGenerator ccMG = (CCCBYCMazeGameFakeEndOfTurnCRGenerator)seq.getSS().getcC();
        ccMG.setState(0);
        seq.setInputClosed();
        mgblock.blockTimedOUT();

    }

}

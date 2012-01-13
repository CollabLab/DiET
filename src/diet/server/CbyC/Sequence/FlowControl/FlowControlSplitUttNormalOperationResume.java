/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package diet.server.CbyC.Sequence.FlowControl;

import diet.server.CbyC.Sequence.Sequence;

/**
 *
 * @author Greg
 */
public class FlowControlSplitUttNormalOperationResume extends FlowControlNormalOperationResume{

    String originOfSplitUtt;
    
    public FlowControlSplitUttNormalOperationResume(Sequence seq,String s) {
        super(seq);
    }

    @Override
    public void controlFlow() {
        super.controlFlow();
        //Participant p = seq.getSS().getcC().getC().getParticipants().findParticipantWithUsername(originOfSplitUtt);
        
    }

}

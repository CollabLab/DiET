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
public class FlowControlNormalOperationResume extends FlowControl{

    public FlowControlNormalOperationResume(Sequence seq) {
        super(seq);
    }

    @Override
    public void controlFlow() {
        seq.getSS().getcC().fh.setAutomaticallyAllowOpenFloorAfterIsTypingTimeout(true);
    }

}

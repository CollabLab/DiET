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
public class FlowControlNormalOperationStop extends FlowControl{

    public FlowControlNormalOperationStop(Sequence seq) {
        super(seq);
    }

    @Override
    public void controlFlow() {
        seq.getSS().getcC().fh.setAutomaticallyAllowOpenFloorAfterIsTypingTimeout(false);
    }

}

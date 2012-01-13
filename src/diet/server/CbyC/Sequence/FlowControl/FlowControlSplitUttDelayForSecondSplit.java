/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package diet.server.CbyC.Sequence.FlowControl;

import diet.server.CbyC.Sequence.Sequence;
import diet.server.CbyC.Sequence.SplitUtt;

/**
 *
 * @author Greg
 */
public class FlowControlSplitUttDelayForSecondSplit extends FlowControlDelay{

    
    
    public FlowControlSplitUttDelayForSecondSplit(Sequence seq,long delay){
        super(seq,delay);
    }

    @Override
    public void controlFlow() {
       super.controlFlow();
       SplitUtt spu = (SplitUtt)seq;
       spu.appOriginForEachParticipant = spu.secondHalfappOriginForEachParticipant;
       
    }
    
    
    
   
}

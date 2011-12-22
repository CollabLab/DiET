/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package diet.server.CbyC.Sequence.FlowControl;

import diet.server.Conversation;
import diet.server.CbyC.Sequence.Sequence;

/**
 *
 * @author Greg
 */
public class FlowControlDelay extends FlowControl{

    long delay;
    
    public FlowControlDelay(Sequence seq, long delay){
        super(seq);
        Conversation.printWSln("FC", "Creating delay. Delay is:"+delay);
        this.delay=delay;
        
    }
    
    @Override
    public void controlFlow() {
        try{ 
        	Conversation.printWSln("FC", "Controling flow. Delay is:"+delay);
        if(delay>0){
           
           Thread.sleep(delay);
        }
       }catch(Exception e){
           
       }
    }
    
    
    
    public long getDelay(){
        return delay;
    }
    
    
}

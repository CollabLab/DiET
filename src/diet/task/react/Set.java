/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package diet.task.react;

/**
 *
 * @author sre
 */
public abstract class Set {
    
    ReactTaskController rtc;
    
    public Set(ReactTaskController rtc){
        this.rtc=rtc;
    }
    
    abstract public void setSize(int i);
    
    abstract public Move[] getSet(int i);
    
  
    
}

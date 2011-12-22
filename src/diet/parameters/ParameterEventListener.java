/*
 * ParameterEventListener.java
 *
 * Created on 14 January 2008, 01:32
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package diet.parameters;
import java.util.EventListener;

/**
 *
 * @author user
 */
public interface ParameterEventListener extends EventListener{
    
    /** Creates a new instance of ParameterEventListener */
    public void parameterChanged(ParameterEvent pe);
    
}

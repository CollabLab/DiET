/*
 * ParameterEvent.java
 *
 * Created on 14 January 2008, 01:30
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package diet.parameters;
import java.util.EventObject;

/**
 *
 * @author user
 */
public class ParameterEvent extends EventObject{
    
    /** Creates a new instance of ParameterEvent */
    public ParameterEvent(Object parameterSource) {
        super(parameterSource);
    }
    
}

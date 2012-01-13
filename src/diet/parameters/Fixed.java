/*
 * Fixed.java
 *
 * Created on 13 January 2008, 14:57
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package diet.parameters;
import java.util.Vector;

/**
 *
 * @author user
 */
public interface Fixed {
    
    /** Creates a new instance of Fixed */
    
    
    public Vector getPermittedValues();
   // public int getSelectedIndex();
    public int indexOfPermittedValue(Object o);
    public void addToPermittedValues(Object o);
    
}

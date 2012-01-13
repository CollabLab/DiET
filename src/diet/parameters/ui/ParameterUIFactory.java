/*
 * ParameterUIFactory.java
 *
 * Created on 13 January 2008, 15:09
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package diet.parameters.ui;
import java.awt.Component;

import javax.swing.JComboBox;
import javax.swing.JTextField;

import diet.parameters.Parameter;
import diet.parameters.StringParameterFixed;


/**
 *
 * @author user
 */
public class ParameterUIFactory {
    
    /** Creates a new instance of ParameterUIFactory */
    public ParameterUIFactory() {
    }
    
    static public Component getUIComponentForExperimentManagerNewParameterValue(Parameter p){
        if(p instanceof StringParameterFixed){
            StringParameterFixed spf = (StringParameterFixed)p;
            JComboBox jc = new JComboBox(spf.getPermittedValues());
            jc.setEditable(true);
            return jc;
        }
        return new JTextField("");
    }
    
    
}

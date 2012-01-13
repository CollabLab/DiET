/*
 * JExperimentParametersFrame.java
 *
 * Created on 13 January 2008, 22:48
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package diet.parameters.ui;
import javax.swing.JFrame;
import javax.swing.JScrollPane;

import diet.parameters.ExperimentSettings;

/**
 *
 * @author user
 */
public class JExperimentParametersFrame extends JFrame{
    
    JExperimentParameterTable jept;
    
    /** Creates a new instance of JExperimentParametersFrame */
    public JExperimentParametersFrame(ExperimentSettings expSettings) {
        super();
        JExperimentParameterTable jept = new JExperimentParameterTable(expSettings);
        JScrollPane js = new JScrollPane();
        js.getViewport().add(jept);
        this.getContentPane().add(js);
        this.pack();
        this.setVisible(true);
        this.setEnabled(true);
        
        
    }
    
}

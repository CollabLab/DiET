/*
 * JExperimentSetParameterButton.java
 *
 * Created on 13 January 2008, 15:17
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package diet.parameters.ui;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import diet.parameters.Parameter;



/**
 *
 * @author user
 */
public class JExperimentSetParameterButton extends JButton implements ActionListener{
    
    /**
     * Creates a new instance of JExperimentSetParameterButton
     */
    
    JExperimentParametersTableModel jeptm;
    Parameter p;
    
    public JExperimentSetParameterButton(JExperimentParametersTableModel jeptm,Parameter p,String text) {
        super(text);
        this.jeptm = jeptm;
        this.addActionListener(this);
        this.p=p;
        super.setEnabled(true);
       
    }
    
    public void actionPerformed(ActionEvent e) { 
       jeptm.setValueOfParameter(p,this);
       System.out.println("SETTING");
    }
    
}    
/*
 * ParameterUITesting.java
 *
 * Created on 13 January 2008, 14:08
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package diet.parameters.ui;
import diet.parameters.ExperimentSettings;
/**
 *
 * @author user
 */
public class ParameterUITesting {
    
    /** Creates a new instance of ParameterUITesting */
    public ParameterUITesting() {
        ExperimentSettings em = new ExperimentSettings();
        em.populateWithTestData();
        JExperimentParametersFrame jepf = new JExperimentParametersFrame(em);
        //JFrame jf = new JFrame();
        //JScrollPane js = new JScrollPane();
        //js.getViewport().add(new JExperimentParameterTable(em));
        //jf.getContentPane().add(js);
        //jf.pack();
        //jf.setVisible(true);
    }
    
    public static void main(String[] args){
        ParameterUITesting pi = new ParameterUITesting();
    }
    
}

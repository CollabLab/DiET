/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package diet.textmanipulationmodules.CyclicRandomTextGenerators;

import java.io.File;
import java.util.Vector;

import diet.parameters.ui.SavedExperimentsAndSettingsFile;
/**
 *
 * @author Greg
 */
public class CyclicRandomCRClarifyingIntentions2 extends CyclicRandomCRClarifyingIntentions{

    public CyclicRandomCRClarifyingIntentions2(Vector v) {
        super(v);
    }

    public CyclicRandomCRClarifyingIntentions2() {
         File f = new File(System.getProperty("user.dir")+File.separator+"crintention"+File.separator+"crintention.txt");
         Vector v2 =SavedExperimentsAndSettingsFile.readVectorOfStringsFromTextFile(f);

         Vector v = new Vector();
         
         v.addElement("umm why?");
         v.addElement("why?");
         v.addElement("cld u explain why?");
         v.addElement("sry why?");
         v.addElement("srry why that?");
         v.addElement("why that?");

         
        // v.addElement("wht for?");
        // v.addElement("could u xplain mre?");
        // v.addElement("what for?");
        // v.addElement("sory..um wht u mean by that?");
        // v.addElement("sory..what for?");
        // v.addElement("sory..because of?");



       


         
        super.setPossibleWords(v);
    }

    
}

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
public class CyclicRandomCRClarifyingIntentions extends CyclicRandomTextGenerator{

    public CyclicRandomCRClarifyingIntentions(Vector v) {
        super(v);
    }

    public CyclicRandomCRClarifyingIntentions() {
         File f = new File(System.getProperty("user.dir")+File.separator+"crintention"+File.separator+"crintention.txt");
         Vector v2 =SavedExperimentsAndSettingsFile.readVectorOfStringsFromTextFile(f);

         Vector v = new Vector();

         /*
          *
          *
          v.addElement("umm why?");
          v.addElement("why?");
          v.addElement("cld u explain more?");
          v.addElement("sry why?");
          v.addElement("srry why that?");
          v.addElement("why that?");
          *
          * 
          *
          */

         //could you say more?
         //

         v.addElement("sory..um wot u mean by that?");
         v.addElement("erm why?");
         v.addElement("y?");
         v.addElement("sory erm why that?");
         v.addElement("cld u xplain why?");
         v.addElement("wot u ment by that?");
         v.addElement("sry why?");
         v.addElement("sorry..y?");
         v.addElement("cld u xplain mre?");
         v.addElement("why that?");
         v.addElement("erm..for wht?");
         
         
        super.setPossibleWords(v2);
    }

    
}

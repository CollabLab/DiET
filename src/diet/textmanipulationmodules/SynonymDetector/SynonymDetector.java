/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package diet.textmanipulationmodules.SynonymDetector;

import java.io.File;
import java.util.Vector;

import diet.parameters.StringListParameter;
import diet.parameters.ui.SavedExperimentsAndSettingsFile;

/**
 *
 * @author Greg
 */
public class SynonymDetector {
     
    File f = new File(System.getProperty("user.dir")+File.separator+"synonymsets");
    Vector synonymSets = new Vector();
    
    public SynonymDetector(){
        String [] names = f.list();
        for(int i=0;i<names.length;i++){
            String sname = names[i];
            if(sname.endsWith("syn")){
                File fsyn = new File(names[i]);
                Vector v = SavedExperimentsAndSettingsFile.readVectorOfStringsFromTextFile(fsyn);
                String rootName = (String)v.elementAt(0);
                StringListParameter slp = new StringListParameter(rootName,v);
                synonymSets.addElement(slp);
            }
            
        }
        
    } 
     
    public StringListParameter lookupSynonyms(String needle){
        for(int i =0;i<synonymSets.size();i++){
            StringListParameter slp = (StringListParameter)synonymSets.elementAt(i);
            Vector v = slp.getValue();
            for(int j=0;j<v.size();j++){
                String s = (String)v.elementAt(j);
                if(s.equalsIgnoreCase(needle)){
                    return slp;
                }
            }
        }
        return null;
    }
    
    
    
}

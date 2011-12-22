/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package diet.textmanipulationmodules.SynonymDetector;

import java.io.File;
import java.util.Random;
import java.util.Vector;

import diet.parameters.StringListParameter;
import diet.parameters.ui.SavedExperimentsAndSettingsFile;

/**
 *
 * This class is used by a specific experimental intervention (spoof self-repair). 
 * 
 * @author Greg
 */
public class DynamicSynonymModificationsForDyads  {
     
    Random r = new Random();
    File f = new File(System.getProperty("user.dir")+File.separator+"synonymsets");
    Vector synonymSetsParticipant1 = new Vector();
    Vector synonymSetsParticipant2 = new Vector();
    
    
    public static void main(String[] args){             
        DynamicSynonymModificationsForDyads dsmfd = new DynamicSynonymModificationsForDyads();
        System.out.println(dsmfd.addSynonym("row","hrz"));
        String slp = dsmfd.lookupSynonymsAndRemoveParticipant1("hrz");
        System.out.println(slp);
        String slp2 = dsmfd.lookupSynonymsAndRemoveParticipant1("DESTNATION");
        System.out.println(slp2);
    }
    
    
    
    public DynamicSynonymModificationsForDyads(){
        String [] names = f.list();
        for(int i=0;i<names.length;i++){
            String sname = names[i];
            System.out.println(sname);
            if(sname.endsWith("xml")){
                File fsyn = new File(f,names[i]);
                Vector v1=SavedExperimentsAndSettingsFile.readParameterObjects(fsyn);   
                Vector v2=SavedExperimentsAndSettingsFile.readParameterObjects(fsyn);
                synonymSetsParticipant1.addElement(v1);               
                synonymSetsParticipant2.addElement(v2);
            }
            
        }
        
    } 
     
    public String lookupSynonymsAndRemoveParticipant1(String needle){
        for(int i =0;i<synonymSetsParticipant1.size();i++){   
            Vector v = (Vector) synonymSetsParticipant1.elementAt(i);
            for(int j=0;j<v.size();j++){
                StringListParameter slp = (StringListParameter)v.elementAt(j);
                Vector v2 = slp.getValue();
                for(int k=0;k<v2.size();k++){
                    String s = (String)v2.elementAt(k);
                    if(s.equalsIgnoreCase(needle)){
                        synonymSetsParticipant1.remove(v);
                        v.remove(slp);
                        int rnd = r.nextInt(v.size());
                        StringListParameter slpSYNONYM = (StringListParameter)v.elementAt(rnd);
                        return slpSYNONYM.getID();
                    }
                }
            }
        }
        return null;
    }
    
    public String lookupSynonymsAndRemoveParticipant2(String needle){
        for(int i =0;i<synonymSetsParticipant2.size();i++){   
            Vector v = (Vector) synonymSetsParticipant2.elementAt(i);
            for(int j=0;j<v.size();j++){
                StringListParameter slp = (StringListParameter)v.elementAt(j);
                Vector v2 = slp.getValue();
                for(int k=0;k<v2.size();k++){
                    String s = (String)v2.elementAt(k);
                    if(s.equalsIgnoreCase(needle)){
                        synonymSetsParticipant2.remove(v);
                        v.remove(slp);
                        int rnd = r.nextInt(v.size());
                        StringListParameter slpSYNONYM = (StringListParameter)v.elementAt(rnd);
                        return slpSYNONYM.getID();
                    }
                }
            }
        }
        return null;
    }
    
    public String addSynonym(String name1,String name2){
        
        String res= "";
        res="(A1)";
        boolean success1 = this.addSyn(name1, name2, synonymSetsParticipant1);
        if(!success1){
            res="(A2)";
            success1 = this.addSyn(name2, name1, synonymSetsParticipant1);
        }
        boolean success2 = this.addSyn(name1, name2, synonymSetsParticipant2);
        if(!success2){
            res="(B)";
            success2 = this.addSyn(name2, name1, synonymSetsParticipant2);
        }
        
        
        if(success1) res = res+"Added to first participant";
        if(success1&&success2) res = res + "and second participant";
        if(success2&&!success1) res = res+"Added to second participant only";
        return res;
    }
    
    private boolean addSyn(String newString,String oldString,Vector synonymSetsParticipant){
        for(int i=0;i<synonymSetsParticipant.size();i++){
            Vector v = (Vector)synonymSetsParticipant.elementAt(i);
            for(int j=0;j<v.size();j++){
                StringListParameter slp = (StringListParameter)v.elementAt(j);
                if(slp.getID().equalsIgnoreCase(oldString)){
                    //System.out.println("ADDING(A1) "+newString+" to parametername: "+slp.getID()+" size is: "+slp.getValue().size());
                    slp.addNewString(newString, false);
                    //System.out.println("ADDING(A2) "+newString+" to parametername: "+slp.getID()+" size is: "+slp.getValue().size());
                    return true; 
                }
                for(int k=0;k<slp.getValue().size();k++){
                    String s2 = (String)slp.getValue().elementAt(k);
                    if(s2.equalsIgnoreCase(oldString)){
                        //System.out.println("ADDING(B1) "+newString+" to parametername: "+slp.getID()+" size is: "+slp.getValue().size());
                        slp.addNewString(newString, false);
                        //System.out.println("ADDING(B2) "+newString+" to parametername: "+slp.getID()+" size is: "+slp.getValue().size());
                        return true;
                    }
                    
                }
            }
        }
        return false;
    }
    
}

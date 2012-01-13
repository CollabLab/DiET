/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package diet.textmanipulationmodules.CRDetector;

import java.io.File;
import java.util.Vector;

import diet.parameters.ExperimentSettings;
import diet.parameters.StringListParameter;
import diet.parameters.ui.SavedExperimentsAndSettingsFile;

/**
 *
 * @author Greg
 */
public class CRDetectorIOPreprocessing {

    
    File homeDir = new File(System.getProperty("user.dir")+File.separator+"crdetection");
    File synonymsFile = new File(homeDir,"synonyms.xml");
   
    public Vector listOfWildcards ;
    public Vector listOfWildcardsMustBeQuestion;
    public Vector listOfPrefixMustBeQuestion;
    public Vector listOfSuffixMustBeQuestion;
    public Vector listOfPrefix;
    public Vector listOfSuffix;   
    public Vector listOfAbsoluteMustBeQuestion;
     public Vector listOfAbsolute;
 
    public CRDetectorIOPreprocessing(CRDetector crd) {
        doSetup();             
    }
    
    public void doSetup(){
        Vector v=SavedExperimentsAndSettingsFile.readParameterObjects(synonymsFile);
        ExperimentSettings synonymParam = new ExperimentSettings(v);
        
        Vector wildcardsRaw = SavedExperimentsAndSettingsFile.readVectorOfStringsFromTextFile(new File(homeDir,"wildcards.xml"));
        listOfWildcards = processWildCards(wildcardsRaw,synonymParam);
        
        Vector wildcardsRawMustBeQuestion = SavedExperimentsAndSettingsFile.readVectorOfStringsFromTextFile(new File(homeDir,"wildcards_mustbequestion.xml"));
        listOfWildcardsMustBeQuestion = processWildCards(wildcardsRawMustBeQuestion,synonymParam);
        
        Vector prefixRawMustBeQuestion = SavedExperimentsAndSettingsFile.readVectorOfStringsFromTextFile(new File(homeDir,"prefix_mustbequestion.xml"));
        listOfPrefixMustBeQuestion = processWildCards(prefixRawMustBeQuestion,synonymParam);
        
        Vector suffixRawMustBeQuestion = SavedExperimentsAndSettingsFile.readVectorOfStringsFromTextFile(new File(homeDir,"suffix_mustbequestion.xml"));
        listOfSuffixMustBeQuestion = processWildCards(suffixRawMustBeQuestion,synonymParam);
        
         Vector prefixRaw = SavedExperimentsAndSettingsFile.readVectorOfStringsFromTextFile(new File(homeDir,"prefix.xml"));
         listOfPrefix = processWildCards(prefixRaw,synonymParam);
        
        Vector suffixRaw = SavedExperimentsAndSettingsFile.readVectorOfStringsFromTextFile(new File(homeDir,"suffix.xml"));
        listOfSuffix = processWildCards(suffixRaw,synonymParam);
        
         Vector absoluteRawMustBeQuestion = SavedExperimentsAndSettingsFile.readVectorOfStringsFromTextFile(new File(homeDir,"absolute_mustbequestion.xml"));
         listOfAbsoluteMustBeQuestion = processWildCards(absoluteRawMustBeQuestion,synonymParam);
         
         Vector absoluteRaw = SavedExperimentsAndSettingsFile.readVectorOfStringsFromTextFile(new File(homeDir,"absolute.xml"));
         listOfAbsolute = processWildCards(absoluteRaw,synonymParam);
        
    }

    public Vector processWildCards(Vector stringsToBePermuted,ExperimentSettings synonymParam){ 
        Vector wildCardsAsVVV = new Vector();
        for(int i=0;i<stringsToBePermuted.size();i++){
            Vector vofV = new Vector();    
            String s = (String)stringsToBePermuted.elementAt(i);
            String[] sWords = s.split(" ");
            for(int j=0;j<sWords.length;j++){
                String wrd = sWords[j];
                StringListParameter p = (StringListParameter)synonymParam.getParameter(wrd);   
                if(p==null){
                    Vector stem = new Vector();
                    stem.addElement(wrd);
                    vofV.addElement(stem);
                }
                else{
                    Vector stem0 = p.getValue();
                    Vector stems = (Vector)stem0.clone();
                    stems.insertElementAt(wrd, 0);
                    vofV.addElement(stems);
                    
                }    
            }
            wildCardsAsVVV.addElement(vofV);  
        }
    
        
        Vector listOfStrings = new Vector();
        for(int i=0;i< wildCardsAsVVV.size();i++){
            Vector stringFrag = (Vector)wildCardsAsVVV.elementAt(i);
            for(int j=0;j<stringFrag.size();j++){
                Vector stem = (Vector)stringFrag.elementAt(j);
                System.out.print("(");
                for(int k=0;k<stem.size();k++){
                    String s = (String)stem.elementAt(k);
                    System.out.print(s+",");
                }
                System.out.print(")");
            }
            System.out.println("");
             Vector listOfStringsForFrag = new Vector();
            processStringFrag(stringFrag,"",listOfStringsForFrag);
            //System.out.println("A "+listOfStringsForFrag.size());
            listOfStrings.addAll(listOfStringsForFrag);
        }
        return listOfStrings;
    }
    
    
    public void processStringFrag(Vector vString,String soFar, Vector result){
        //Car           man           steel    by
        //Vehicle       person                 of
        //bike        

        if(vString.size()==0){
            result.addElement(soFar);
            //System.out.println(soFar+result.size());
        }
        else{
        Vector head = (Vector)vString.firstElement();
        for(int i=0;i<head.size();i++){
            String s = (String)head.elementAt(i);
            //System.err.println("Cloning "+s);
            Vector vStringclone = (Vector)vString.clone();
            vStringclone.removeElementAt(0);
            processStringFrag(vStringclone,soFar+" "+s,result);
        }
        
} 
        
        
        
        
        
        
        
        
        
    }
    
}

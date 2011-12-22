/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package diet.textmanipulationmodules.CRDetector;

import diet.server.ConversationController.DefaultConversationController;
import diet.utils.stringsimilarity.StringSimilarityMeasure;

/**
 *
 * @author Greg
 */
public class CRDetector {

    public DefaultConversationController cC;
    CRDetectorIOPreprocessing crdIO;
    
    public static void main(String[] args){
        CRDetector crd = new CRDetector();
        String immediatelyPriorByOther = "I am so in a shop";
        String immediatelyPriorBySame = "";
        String currentText ="and so are you?";
        boolean isCR = crd.isCR(immediatelyPriorByOther, immediatelyPriorBySame, currentText);
        if(isCR){
            System.out.println("IT IS CR");
        }
        else{
            System.out.println("IT IS NOT CR");
        }
    }
    
    public CRDetector(){
        crdIO = new CRDetectorIOPreprocessing(this);
    }
    
    public CRDetector(DefaultConversationController cC){
        this.cC = cC;
        crdIO = new CRDetectorIOPreprocessing(this);
    } 
    
    public boolean isCR(String immediatelyPriorTextByOther, String immediatelyPriorBySame, String currentText){
        String fullText = ""+immediatelyPriorBySame + " "+currentText;
        fullText.trim();
        fullText =fullText.replaceAll("[\\W&&[\\S]]", "");
        fullText = fullText.replaceAll(" +", " ");
        fullText = fullText.trim();
        System.out.println("FULL TEXT IS"+fullText);
        
        fullText = fullText.toLowerCase();
        immediatelyPriorTextByOther = immediatelyPriorTextByOther.toLowerCase();
        
        if(currentText.indexOf("?")>=0){
            for(int i=0;i<crdIO.listOfPrefixMustBeQuestion.size();i++){
                String sPREF = (String)crdIO.listOfPrefixMustBeQuestion.elementAt(i);
                if(fullText.startsWith(sPREF)) {
                    System.out.println("FOUND "+fullText+" as questionpref");
                    return true;
                }
            }
            for(int i=0;i<crdIO.listOfSuffixMustBeQuestion.size();i++){
                String sPREF = (String)crdIO.listOfSuffixMustBeQuestion.elementAt(i);
                if(fullText.endsWith(sPREF)){
                    System.out.println("FOUND "+fullText+" as questionsuff");
                    return true;
                }
            }
            for(int i=0;i<crdIO.listOfWildcardsMustBeQuestion.size();i++){
                String sPREF = (String)crdIO.listOfWildcardsMustBeQuestion.elementAt(i);
                if(fullText.indexOf(sPREF)>0){
                    System.out.println("FOUND "+fullText+" as questionwild");
                    return true;
                }
            }
            for(int i=0;i<crdIO.listOfAbsoluteMustBeQuestion.size();i++){
                String sPREF = (String)crdIO.listOfAbsoluteMustBeQuestion.elementAt(i);
                if(fullText.equalsIgnoreCase(sPREF)){
                    System.out.println("FOUND "+fullText+" as questionabs");
                    return true;
                }
            }
            
         }
        
           for(int i=0;i<crdIO.listOfPrefix.size();i++){
                String sPREF = (String)crdIO.listOfPrefix.elementAt(i);
                if(fullText.startsWith(sPREF)) {
                    System.out.println("FOUND "+fullText+" as pref");
                    return true;
                }
            }
            
            for(int i=0;i<crdIO.listOfSuffix.size();i++){
                String sPREF = (String)crdIO.listOfSuffix.elementAt(i);
                if(fullText.endsWith(sPREF)){
                    System.out.println("FOUND "+fullText+" as suff");
                    return true;
                }
            }
            for(int i=0;i<crdIO.listOfWildcards.size();i++){
                String sPREF = (String)crdIO.listOfWildcards.elementAt(i);
                if(fullText.indexOf(sPREF)>0){
                    System.out.println("FOUND "+fullText+" as wild");
                    return true;
                }
            }
            for(int i=0;i<crdIO.listOfAbsolute.size();i++){
                String sPREF = (String)crdIO.listOfAbsolute.elementAt(i);
                if(fullText.equalsIgnoreCase(sPREF)){
                    System.out.println("FOUND "+fullText+" as abs");
                    return true;
                }
            }
            
            if(currentText.indexOf("?")>=0){
                //Looks to see if it is a question that starts with where and has an it
                System.out.println("AFULLTEXT IS"+fullText);
                String fullTextSpace = fullText+" ";
                if(fullTextSpace.indexOf(" it ")>0||fullTextSpace.indexOf(" that ")>0||fullTextSpace.indexOf(" tht ")>0||
                fullTextSpace.indexOf(" dat ")>0){       
                    if(fullText.startsWith("where")||fullText.startsWith("what")||fullText.startsWith("which")||
                       fullText.startsWith("wher")||fullText.startsWith("whre")||fullText.startsWith("whch")
                       ||fullText.startsWith("wats")){
                          System.out.println("FOUND "+fullText+" is a question that starts with wh- and has an it/that in it"); 
                          return true; 
                    }
                }
                
                //Looks to see if first word is repeat of last word
                String[] allWordsFullText = fullText.split("[\\W]");
                String[] allWordsImmPrior = immediatelyPriorTextByOther.split("[\\W]");
                try{
                    if(allWordsFullText[0].equalsIgnoreCase(allWordsImmPrior[allWordsImmPrior.length-1])){
                        System.out.println("FOUND "+fullText+" first word is a repeat of the last");
                        return true;
                    }
                }catch (Exception e){
                    System.err.println("Little error in trying to detect CR "+fullText);
                }
                
                //Looks to see if the last N words are repeated at the beginning
                try{
                    String compositeWordsImmPrior="";
                    for(int i=allWordsImmPrior.length-1;i>=0;i--){
                         compositeWordsImmPrior = allWordsImmPrior[i]+" "+compositeWordsImmPrior;
                         compositeWordsImmPrior = compositeWordsImmPrior.trim();
                         System.out.println(i+" composite Word is_"+compositeWordsImmPrior);
                         if(fullText.startsWith(compositeWordsImmPrior)){
                             System.out.println("FOUND "+fullText+" first "+i+" words are a repeat of the last");
                             return true;
                         }
                    }
                    
                    if(allWordsFullText[0].equalsIgnoreCase(allWordsImmPrior[allWordsImmPrior.length-1])){
                         System.out.println("FOUND "+fullText+" first word is a repeat of the last");
                        return true;
                    }
                }catch (Exception e){
                    System.err.println("Little error in trying to detect CR "+fullText);
                }
                
                
                //Looks to see if question is followed by question
                if(immediatelyPriorTextByOther.endsWith("?")){
                    System.out.println("FOUND "+fullText+" question is followed by question");
                    return true;
                }
                //Looks to see if surface form is that of a FRAG
                if(immediatelyPriorTextByOther.indexOf(fullText)>=0){
                    System.out.println("FOUND "+fullText+" is a surface FRAG");
                    return true;
                }
                //Looks for string similarity measure:
                float[] matching =StringSimilarityMeasure.getProportionOfWordsOfAInBandBinA(immediatelyPriorTextByOther, fullText);
                System.out.println("Matching: "+matching[0]+","+matching[1]);
                if(allWordsFullText.length>3&&matching[1]>0.75){
                    System.out.println("FOUND "+fullText+" is a FRAG found by matching");
                }
                
                
            }
        
        
        return false;
    }
    
}

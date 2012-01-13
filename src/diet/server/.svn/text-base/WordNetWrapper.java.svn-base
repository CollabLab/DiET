/*
 * WordNetWrapper.java
 *
 * Created on 26 October 2007, 12:42
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package diet.server;
import java.util.Random;
import java.util.Vector;

import edu.gwu.wordnet.DictionaryDatabase;
import edu.gwu.wordnet.FileBackedDictionary;
import edu.gwu.wordnet.IndexWord;
import edu.gwu.wordnet.POS;
import edu.gwu.wordnet.Pointer;
import edu.gwu.wordnet.PointerTarget;
import edu.gwu.wordnet.PointerType;
import edu.gwu.wordnet.Synset;
import edu.gwu.wordnet.Word;

/**
 * WordNet wrapper 
 * 
 * @author user
 */
public class WordNetWrapper {
    
    Random r = new Random();
    DictionaryDatabase dictionary; //= new FileBackedDictionary("C:\\DiET\\Libraries\\jwordnet\\wordnetdata");
    
    public WordNetWrapper(String wordnetdirectory) {
        dictionary = new FileBackedDictionary(wordnetdirectory);
    }
    
    
    /**
     * Attempts to find alternative words (synonym,hypernym,hyponym...) for words tagged by the Stanford Parser
     * @param posText Part of speech from Stanford Parser
     * @param text word to search for
     * @param pointType synonym|hyponym|hypernym
     * @return Alternative word from WordNet
     */
    public Vector getReplacementWord(String posText, String text, PointerType pointType){
        
     Vector replacementWords = new Vector();
     
     try{
        POS pos;
        if(posText.equalsIgnoreCase("nn")){
            pos = POS.NOUN;
        }
        else if(posText.equalsIgnoreCase("vb")){
            pos = POS.VERB;
        }
        else if(posText.equalsIgnoreCase("adv")){
            pos = POS.ADV;
        }
        else if(posText.equalsIgnoreCase(("JJ"))){
            pos = POS.ADJ;
        }
        else {
            return replacementWords;
        }
        
        IndexWord word = dictionary.lookupIndexWord(pos, text);
        Synset[] senses = word.getSenses();
       
        int taggedCount = word.getTaggedSenseCount();
        for (int i=0; i < senses.length; i++) {   
            Synset sense = senses[i];
            
            PointerTarget[] targets = sense.getTargets(pointType);
            //System.out.println("WORDNETWORDNETWORDNETWOEDNET------------"+);
            for(int j=0;j<targets.length;j++){
               Pointer[] pAnt = targets[j].getPointers();
               for(int k=0;k<pAnt.length;k++){
                  String possReplacement = pAnt[k].getTarget().getDescription();
                  if(!possReplacement.equalsIgnoreCase(text)){
                      replacementWords.addElement(possReplacement);
                  }
                  System.err.println("WORDNET SENSE--"+text+": "+sense.getPOS()+":"+sense.getWords()+":"+sense.getWord(0));
                  //System.err.println("WORDNET SENSE--"+text+":"+sense.getDescription());
                  //System.err.println("WORDNET SENSE--"+text+":"+sense.getLongDescription());
                  // System.err.println("WORDNET SENSE--"+text+":"+sense.getGloss());
                  System.err.println("WORDNET--"+text+":"+pAnt[k].getTarget().getDescription());
                  //System.err.println("WORDNET--"+text+":"+pAnt[k].getTarget().getLongDescription());
                  //System.err.println("WORDNET--"+text+":"+word.getLemma()+":POSTEXT: "+posText);
                  
            }
        }
        //System.out.println("WORDNET---------------");
            
            
            
        } 
        
        return replacementWords;
     }catch (Exception e){
         System.out.println("Error in WordNet: "+text);
         return replacementWords;
     }   
        
    }
    
    
    public String getSingleWordFromList(Vector v){
        int r2 = r.nextInt(v.size());
        String s = (String)v.elementAt(r2);
        String[] split = s.split("[,] ");
        int r3 = r.nextInt(split.length);
        for(int i=0;i<split.length;i++){
            System.out.println("regex found: "+split[i]);
        }
        return split[r3];
        
        
    }
    
    
    
    
    
    /**
     * Test implementation for WordNet wrapper
     */
    public void getInfo2(){
      IndexWord word = dictionary.lookupIndexWord(POS.NOUN, "house");
      Synset[] senses = word.getSenses();
      int taggedCount = word.getTaggedSenseCount();
     
      // Explore related words. 
      for (int i=0; i < senses.length; i++) {   

        Synset sense = senses[i];
        PointerTarget[] antonyms = sense.getTargets(PointerType.HYPONYM);
       
        System.out.println("------------");
        for(int j=0;j<antonyms.length;j++){
            Pointer[] pAnt = antonyms[j].getPointers();
            for(int k=0;k<pAnt.length;k++){
                System.out.println("--"+pAnt[k].getTarget().getDescription());
            }
        }
        System.out.println("------------");
        
        // Print Synset Description 
        System.out.println((i+1) + ". " + sense.getLongDescription());

        // Print words in Synset 
        Word[] printWords = sense.getWords(); 
        for (int j=0; j < printWords.length; j++) {
          System.out.println (printWords[j].getLemma());
        }
        System.out.println("");

      }  
    }
    }
    
    


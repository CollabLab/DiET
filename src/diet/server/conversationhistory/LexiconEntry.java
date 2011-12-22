package diet.server.conversationhistory;
import java.io.Serializable;
import java.util.Vector;

import diet.server.conversationhistory.turn.Turn;

public class LexiconEntry implements Serializable{

    private String wordText;
    private String partOfSpeech;
    private Vector turnsContainingWord = new Vector();
    private Vector conversantsWhoTypedWord = new Vector();
    private Vector conversantsWhoReceivedWord = new Vector();
    private int occurrences =0;
    
    
    
    
   
    public LexiconEntry(String wordText,String partOfSpeech) {
        this.wordText = wordText;
        this.partOfSpeech = partOfSpeech;
    }
    
    public void addNewOccurrenceOfWord(Turn t, Conversant convWhoTyped,Vector convsWhoReceived){
       //Check that the word doesn't occur more than once
        occurrences = occurrences+1;
        if(!turnsContainingWord.contains(t)){
            turnsContainingWord.addElement(t);
        }
        else{
            //System.out.println("Turn contains more than one occurrence of "+wordText);
        }
        if(!conversantsWhoTypedWord.contains(convWhoTyped)){
            conversantsWhoTypedWord.addElement(convWhoTyped);
        }
        else{
           // System.out.println("Participant has already typed this word before "+wordText);            
        }
        for(int i=0;i<convsWhoReceived.size();i++){
            Conversant c = (Conversant)convsWhoReceived.elementAt(i);
            if(!conversantsWhoReceivedWord.contains(c)){
                conversantsWhoReceivedWord.addElement(c);
            }
            else {
               // System.out.println("Participant has already received this word before: "+wordText);        
            }
        }
        
        
        
    }
    
    
    public String getWord(){
        return wordText;
    }
    
    public String getPartOfSpeech(){
        return partOfSpeech;
    }
    
    public int getOccurrences(){
        return occurrences;
    }
    
    public Vector getTurnsContainingWord(){
        return turnsContainingWord;
    }
    
    public Conversant getConversantWhoIntroducedWord(){
        Vector v = this.getConversantsWhoTypedWord();
        return (Conversant)v.elementAt(0);
    }
    
    public Vector getConversantsWhoTypedWord(){
        return this.conversantsWhoTypedWord;
    }
    
    public Vector getConversantsWhoReceivedWord(){
        return this.conversantsWhoReceivedWord;
    }
    
    public String getWPT(){
        String s = this.wordText+","+this.partOfSpeech+" contained in "+this.turnsContainingWord.size();
        return s;
        
    }
    
    
}

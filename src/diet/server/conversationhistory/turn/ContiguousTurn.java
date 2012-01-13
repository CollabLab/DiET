

package diet.server.conversationhistory.turn;
import java.io.Serializable;
import java.util.Vector;

import diet.server.conversationhistory.Conversant;

/**
 * This represents a turn that is constructed out of multiple turns in the chat text. The ConversationHistory
 * class creates these automatically whenever a participant types a sequence of turns that are uninterrupted by
 * turns from other participants.
 * 
 * @author user
 */
public class ContiguousTurn implements Serializable{

    public Vector turns = new Vector();
    //private Tree parseTreeForAll;
    //private TaggedTextForAll;
    

    public ContiguousTurn(Turn t) {
        turns.addElement(t);
    }
    
    
  
     public Vector getWordsAsLexicalEntries(){
         Vector v = new Vector();
         for(int i=0;i<turns.size();i++){
             Turn t = (Turn)turns.elementAt(i);
             v.addAll(t.getWordsAsLexicalEntries());
         }
         
         return v;
    }
     
    public Vector getStrings(){
         Vector v = new Vector();
         for(int i=0;i<turns.size();i++){
             Turn t = (Turn)turns.elementAt(i);
             v.addAll(t.getWordsAsStrings_NOPARTSOFSPEECH());
         }
         
         return v;
        
    }
    
    public boolean getTurnsHaveSameRecipients(){
        if(2<5)return true;
        Vector v = ((Turn)turns.elementAt(0)).getRecipients();
         for(int i=0;i<turns.size();i++){
             Turn t = (Turn)turns.elementAt(i);
             if(!v.containsAll(t.getRecipients()))return false;
         }
        return true;
    }
    
    public boolean getTurnsHaveSameApparentOrigin(){
        if(2<5)return true;
        Conversant c = ((Turn)turns.elementAt(0)).getApparentSender();
        for(int i=0;i<turns.size();i++){
             Turn t = (Turn)turns.elementAt(i);
             if(!c.equals(t.getApparentSender()))return false;
         }
        return true;
    }
    
    public Vector getRecipients(){
        Vector v = new Vector();
         for(int i=0;i<turns.size();i++){
             Turn t = (Turn)turns.elementAt(i);
             for(int j=0;j<t.getRecipients().size();j++){
                Conversant c = (Conversant)t.getRecipients().elementAt(j);
                if(!v.contains(c))v.addElement(c);
             }
         }
         return v;
    }
    
    public Conversant getSender(){
        return ((Turn)turns.elementAt(0)).getSender();
    }
    public long getTypingOnset(){
        return ((Turn)turns.elementAt(0)).getTypingOnsetNormalized();
    }
    
    public long getTypingReturnPressed(){
        return ((Turn)turns.elementAt(turns.size()-1)).getTypingReturnPressedNormalized();
    }
    public Conversant getApparentSender(){
        return ((Turn)turns.elementAt(0)).getApparentSender();
    }
    public String getTextString(){
        String s = new String();
         for(int i=0;i<turns.size();i++){
             Turn t = (Turn)turns.elementAt(i);
             s=s+t.getTextString()+" ";
         }
         return s;
    }
    public boolean getTypingWasBlockedDuringTyping(){
         for(int i=0;i<turns.size();i++){
             Turn t = (Turn)turns.elementAt(i);
             if(t.getTypingWasBlockedDuringTyping())return true;
         }
         return false;
    }
    
    
    public int getDocDeletes(){
         int noOfDocDeletes = 0;
         for(int i=0;i<turns.size();i++){
             Turn t = (Turn)turns.elementAt(i);
             noOfDocDeletes = noOfDocDeletes + t.getDocDeletes();
         }
         return noOfDocDeletes;
    }
    
    public int getDocInsertsBeforeTerminal(){
        int noOfDocInserts = 0;
         for(int i=0;i<turns.size();i++){
             Turn t = (Turn)turns.elementAt(i);
             noOfDocInserts = noOfDocInserts + t.getDocInsertsBeforeTerminal();
         }
         return noOfDocInserts;
    }
    public int getDocEditScore(){
         int totalDocEditScore = 0;
         for(int i=0;i<turns.size();i++){
             Turn t = (Turn)turns.elementAt(i);
             totalDocEditScore = totalDocEditScore + t.getDocInsertsBeforeTerminal();
         }
         return totalDocEditScore;
    }
    
    
    public int getKeypressDeletes(){
         int noOfDeletes = 0;
         for(int i=0;i<turns.size();i++){
             Turn t = (Turn)turns.elementAt(i);
             noOfDeletes = noOfDeletes + t.getKeypressDeletes();
         }
         return noOfDeletes;
    }
    
    
    
    
    
    
    public int getNumberOfTurns(){
        return turns.size();
    }
    
    public void addTurn(Turn t){
        turns.addElement(t);
    }
    
    public long getTypingSpeedForTurnCharactersPerSecond(){
        
        long cOnset = ((Turn)turns.elementAt(0)).getTypingOnsetNormalized();
        long cReturn = ((Turn)turns.elementAt(turns.size()-1)).getTypingReturnPressedNormalized();
        long denominator = cReturn - cOnset;
        if(denominator==0)return 0;
        return this.getTextString().length()*1000/denominator;
        
        
    }
    
    public Vector getTurns(){
        return turns;
    }
    public Turn getLastTurn(){
        if(turns.size()>0){
            return (Turn)turns.elementAt(turns.size()-1);
        }
        return null;
    }
    public Turn getFirstTurn(){
        if(turns.size()>0){
            return (Turn)turns.elementAt(0);
        }
        return null;
    }
}

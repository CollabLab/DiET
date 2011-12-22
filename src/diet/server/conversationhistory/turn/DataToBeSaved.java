package diet.server.conversationhistory.turn;
import java.io.Serializable;
import java.util.Vector;

import diet.message.Keypress;
import diet.server.DocChangesIncomingSequenceFIFO;
import diet.server.conversationhistory.Conversant;
import diet.server.conversationhistory.ConversationHistory;
import diet.server.conversationhistory.LexiconEntry;
import edu.stanford.nlp.trees.Tree;



/**
 * This is the representation of individual turns in the conversation. It stores the origin, the recipients,
 * the keypresses, the individual words used and their associated patterns of usage, the timing data and the 
 * parse tree (if stored).
 * @author user
 */
public class DataToBeSaved extends Turn implements Serializable{
  private String senderID;
  private String sender;
  private String apparentSender;
  private Vector recipients = new Vector();
  private String text="";
  private Vector keyPresses = new Vector();
  private Vector documentUpdates = new Vector();
  private Vector wordsAsLexicalEntries = new Vector();
  private boolean wasBlockedDuringSending;
  private long startTimeOfExperiment; //Duplicated from ConversationHistory to ensure that start time is saved and can be retrieved
  private long typingOnset;
  private long typingReturnPressed;
  private long turnNo;
  private ContiguousTurn contiguousTurn;
  private Tree parseTree;
  private transient ConversationHistory cH;
  private String type = "default";
  private Vector additionalValues;
  
  
   public DataToBeSaved(){
   }

    
    public DataToBeSaved (ConversationHistory cH,String type, long onset, long enter,String senderID, String sender, String apparentSender,String text,
          Vector recipients,boolean wasBlocked,Vector keyPresses,Vector documentUpdates,Vector parsedWords,Tree parseTree,long turnNo, Vector additionalStrings){
          this.parseTree=parseTree;
          this.typingOnset = onset;
          this.typingReturnPressed = enter;
          this.senderID=senderID;
          this.sender=sender;
          this.apparentSender = apparentSender;
          this.text=text;
          this.recipients = recipients;
          this.wasBlockedDuringSending=wasBlocked;
          this.keyPresses = keyPresses;
          this.documentUpdates=documentUpdates;
          this.turnNo=turnNo;
          this.cH=cH;
          this.startTimeOfExperiment=cH.getStartTime();
          this.type=type;
          this.additionalValues=additionalStrings;
          //Calculate typing onset and enter of the turn
          
          
    }
    /**
     * sets the words contained in the turn as a Vector of {@link LexiconEntry}.
     * @param v
     */
    public void setWordsAsLexicalEntries(Vector v){
        wordsAsLexicalEntries = v;
    }
    
    /**
     * sets the words contained in the turn as a Vector of {@link LexiconEntry}.
     * @return a vector containing all the lexical entries
     */
    public Vector getWordsAsLexicalEntries(){
        return wordsAsLexicalEntries;
    }
    
    public long getTurnNo(){
        try{
          return cH.getTurns().indexOf(this);
        }catch(Exception e){
           try{ 
              return turnNo;
           }catch(Exception e2){
               return 0;
           }   
        }  
    }
    
    /**
     * Returns all the recipients of the turn as a vector of {@link Conversant}  
     * @return
     */
    public Vector getRecipients(){
        return recipients;
    }
    public String getSenderID(){
        return this.senderID;
    }

    public String getSenderName(){
        return sender;
    }
    public long getTypingOnsetNormalized(){
        return typingOnset-this.startTimeOfExperiment;
    }
    
    public long getTypingReturnPressedNormalized(){
        return typingReturnPressed-this.startTimeOfExperiment;
    }
    public String getApparentSenderName(){
        return apparentSender;
    }
    public String getTextString(){
        return text;
    }
    public boolean getTypingWasBlockedDuringTyping(){
        return this.wasBlockedDuringSending;
    }
    
    public int getDocDeletes(){
        if(documentUpdates==null)return 0;
        return DocChangesIncomingSequenceFIFO.getTotalCharsDeleted(documentUpdates);
    }
    
    public int getDocInsertsBeforeTerminal(){
        if(documentUpdates==null)return 0;
        return DocChangesIncomingSequenceFIFO.getTotalCharsInsertedOutsideTurnTerminal(documentUpdates);
    }
    public int getDocEditScore(){
        if(documentUpdates==null)return 0;
        return DocChangesIncomingSequenceFIFO.getEditScore(documentUpdates);
    }
    
    public int getDocDelsScore(){
        if(documentUpdates==null)return 0;
        return DocChangesIncomingSequenceFIFO.getDelsScore(documentUpdates);
    }
    
    public int getDocInsScore(){
        if(documentUpdates==null)return 0;
        return DocChangesIncomingSequenceFIFO.getInsScore(documentUpdates);
    }
    
    public int getKeypressDeletes(){ 
        if(keyPresses == null)return 0;
        int delCount=0;
        for (int i=0;i<keyPresses.size();i++){
            Keypress kp = (Keypress)keyPresses.elementAt(i);
            if (kp.isDel())delCount++;
        }
        return delCount;
    }
    
  
      public ConversationHistory getCH() {
        return cH;
    }

    public void setCH(ConversationHistory cH) {
        this.cH = cH;
    }

    public Vector getDocumentUpdates() {
        return documentUpdates;
    }

    public void setDocumentUpdates(Vector documentUpdates) {
        this.documentUpdates = documentUpdates;
    }

    public Vector getKeyPresses() {
        return keyPresses;
    }

    public void setKeyPresses(Vector keyPresses) {
        this.keyPresses = keyPresses;
    }

    public long getStartTimeOfExperiment() {
        return startTimeOfExperiment;
    }

    public void setStartTimeOfExperiment(long startTimeOfExperiment) {
        this.startTimeOfExperiment = startTimeOfExperiment;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public long getTypingOnset() {
        return typingOnset;
    }

    public void setTypingOnset(long typingOnset) {
        this.typingOnset = typingOnset;
    }

    public long getTypingReturnPressed() {
        return typingReturnPressed;
    }

    public void setTypingReturnPressed(long typingReturnPressed) {
        this.typingReturnPressed = typingReturnPressed;
    }

    public boolean isWasBlockedDuringSending() {
        return wasBlockedDuringSending;
    }

    public void setWasBlockedDuringSending(boolean wasBlockedDuringSending) {
        this.wasBlockedDuringSending = wasBlockedDuringSending;
    }
    
    public void setContiguousTurn(ContiguousTurn contiguousTurn){
        this.contiguousTurn = contiguousTurn;
    }
    
    public ContiguousTurn getContiguousTurn(){
        return this.contiguousTurn;
    }
    public Tree getParseTree(){
        return parseTree;
    }
    public Vector getWordsAsStrings_NOPARTSOFSPEECH(){
        Vector words = new Vector();
        for(int i=0;i<wordsAsLexicalEntries.size();i++){
            LexiconEntry le = (LexiconEntry)wordsAsLexicalEntries.elementAt(i);
            words.addElement(le.getWord());
        }
        return words;
    } 
            
    public long getTypingSpeedForTurnCharactersPerSecond(){
        long denominator = typingReturnPressed - typingOnset;
        if(denominator==0)return 0;
        return text.length()*1000/denominator;
    }
    
   
    public String getIOAdditionalHeaders(){
        return "";
    }
    
    public String getIOAdditionalValues(){
        if(this.additionalValues==null)return"";
        String returnval = "";
        try{
            for(int i=0;i<this.additionalValues.size();i++){
                Object o = additionalValues.elementAt(i);
                if(o instanceof Integer) o = ""+(Integer)o;
                returnval = returnval + "|"+ (String)o;
            }
        }catch (Exception e){
             e.printStackTrace();
            return "|error";
        }
        return returnval;
    }

    public String getType(){
        return this.type;
    }
}

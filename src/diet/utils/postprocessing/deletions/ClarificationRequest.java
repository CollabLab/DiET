/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package diet.utils.postprocessing.deletions;

import java.util.Vector;

import diet.server.conversationhistory.Conversant;
import diet.server.conversationhistory.ConversationHistory;
import diet.server.conversationhistory.LexiconEntry;
import diet.server.conversationhistory.turn.ContiguousTurn;
import diet.server.conversationhistory.turn.TurnMazeGame;
import diet.utils.stringsimilarity.StringSimilarityMeasure;

/**
 *
 * @author Greg
 */
public class ClarificationRequest {
     
    
    public ConversationHistory ch;
    public ContiguousTurn tst;
    public TurnMazeGame cr;
    public ContiguousTurn responsePriorToAck;
    public TurnMazeGame ack;
    public ContiguousTurn nextTurnAfterAck;
    public boolean isVALID=false;
    public String wotFRAG="";
    boolean respContainsCR=false;
    boolean nextContainsCR=false;
    
    
    public ClarificationRequest(ConversationHistory ch){
        this.ch=ch;
        
    }
    /*
     * Get number of words shared by TST and response
     * Get number of words shared by TST and nextTurn
     * Get number of words deleted from TST in response
     * Get number of words deleted from TST in nextTurn
     * Get number of words shared by response and next turn
     * Get number of words deleted from response in next turn
     * Get number of words shared by TST response and next turn
     *  
     * Get longest common substring in both the TST and the response
     * Get longest common substring in the TST and the next Turn
     * Get longest common substring in the response and the next turn
     * 
     * for TST calculate the proportion of words introduced by recipient/nonrecipient
     * for response calculate the proportion of words introduced by recipient/nonrecipient 
     * 
     * 
     * 
     */
     public Vector wordsSharedByTSTAndResponse = new Vector();
     public Vector wordsSharedByTSTAndNextTurn = new Vector();
     public Vector wordsSharedByResponseAndNextTurn = new Vector();
     public Vector wordsSharedByAll3 = new Vector();
     
     public Vector wordsInTSTButNotResponse =new Vector();
     public Vector wordsInTSTButNotNextTurn =new Vector();
     public Vector wordsInResponseButNotNextTurn = new Vector();
     public Vector wordsInTSTButNotInResponseAndNotInNextTurn = new Vector();
     
     public Vector wordsInResponseButNotTST =new Vector();
     public Vector wordsInNextTurnButNotTST =new Vector();
     public Vector wordsInNextTurnButNotResponse =new Vector();
     public Vector wordsInNextTurnButNotInTSTSndNotInResponse=new Vector();
     
     public String longestCommonSubSequenceTSTAndResponse ="";
     public String longestCommonSubSequenceTSTAndNextTurn="";
     public String longestCommonSubSequenceResponseAndNextTurn="";
     
     public int longestCommonSubSequenceTSTAndResponseLENGTH =0;
     public int longestCommonSubSequenceTSTAndNextTurnLENGTH=0;
     public int longestCommonSubSequenceResponseAndNextTurnLENGTH=0;
     
     public Vector tstWordsIntroducedByCRRecipient = new Vector();
     public Vector tstWordsIntroducedByOther = new Vector();
     
     public Vector respWordsIntroducedByCRRecipient = new Vector();
     public Vector respWordsIntroducedByOther = new Vector();
     
     public Vector nextWordsIntroducedByCRRecipient = new Vector();
     public Vector nextWordsIntroducedByOther = new Vector();
     
     
     public void calculateNumberOfWordsSharedAndDeleted(){
         wordsSharedByTSTAndResponse=StringSimilarityMeasure.getAllSharedWords(this.tst.getStrings(), this.responsePriorToAck.getStrings());
         wordsSharedByTSTAndNextTurn = StringSimilarityMeasure.getAllSharedWords(this.tst.getStrings(), this.nextTurnAfterAck.getStrings());
         wordsSharedByResponseAndNextTurn = StringSimilarityMeasure.getAllSharedWords( this.responsePriorToAck.getStrings(),this.nextTurnAfterAck.getStrings());
         this.wordsSharedByAll3 = StringSimilarityMeasure.getAllSharedWords( this.wordsSharedByTSTAndResponse,this.wordsSharedByTSTAndNextTurn);
     
         this.wordsInTSTButNotResponse=StringSimilarityMeasure.getAllWordsDeletedFromA(tst.getStrings(), this.responsePriorToAck.getStrings());
         this.wordsInTSTButNotNextTurn=StringSimilarityMeasure.getAllWordsDeletedFromA(tst.getStrings(), this.nextTurnAfterAck.getStrings());
         wordsInResponseButNotNextTurn=StringSimilarityMeasure.getAllWordsDeletedFromA(this.responsePriorToAck.getStrings(),this.nextTurnAfterAck.getStrings());
         wordsInTSTButNotInResponseAndNotInNextTurn=StringSimilarityMeasure.getAllWordsDeletedFromAThatAreNotInBorC(tst.getStrings(), this.responsePriorToAck.getStrings(),this.nextTurnAfterAck.getStrings());
         
         wordsInResponseButNotTST = StringSimilarityMeasure.getAllWordsDeletedFromA(this.responsePriorToAck.getStrings(),tst.getStrings());
         wordsInNextTurnButNotTST = StringSimilarityMeasure.getAllWordsDeletedFromA(this.nextTurnAfterAck.getStrings(), tst.getStrings());
         wordsInNextTurnButNotResponse = StringSimilarityMeasure.getAllWordsDeletedFromA(this.nextTurnAfterAck.getStrings(), responsePriorToAck.getStrings());
         wordsInNextTurnButNotInTSTSndNotInResponse=StringSimilarityMeasure.getAllWordsDeletedFromAThatAreNotInBorC(this.nextTurnAfterAck.getStrings(), tst.getStrings(),responsePriorToAck.getStrings());
         
         longestCommonSubSequenceTSTAndResponseLENGTH = StringSimilarityMeasure.longestSubstr(tst.getTextString(), this.responsePriorToAck.getTextString());
         longestCommonSubSequenceTSTAndNextTurnLENGTH = StringSimilarityMeasure.longestSubstr(tst.getTextString(), this.nextTurnAfterAck.getTextString());
         longestCommonSubSequenceResponseAndNextTurnLENGTH=StringSimilarityMeasure.longestSubstr(this.responsePriorToAck.getTextString(), this.nextTurnAfterAck.getTextString());
     
         //System.out.println("LCR of "+tst.getTextString()+" and "+this.responsePriorToAck.getTextString()+" is: "+longestCommonSubSequenceTSTAndResponse);
     
     }
    
     public void calulateRatios(){
         Conversant crRecip = this.tst.getSender();
         Conversant crOther = this.cr.getApparentSender();
         
         System.err.println("CROTHERRECIP: "+crRecip+":"+crOther);
         //System.exit(-1231231);
         
         Vector allTSTLexemes = tst.getWordsAsLexicalEntries();
         for(int i=0;i<allTSTLexemes.size();i++){
             LexiconEntry le = (LexiconEntry)allTSTLexemes.elementAt(i);
             Conversant cWhoIntroduced =le.getConversantWhoIntroducedWord();
             
             if(cWhoIntroduced==crRecip){
                 tstWordsIntroducedByCRRecipient.addElement(le);
             }
             else if(cWhoIntroduced==crOther){
                 tstWordsIntroducedByOther.addElement(le);
             }    
         }
         tstWordsIntroducedByCRRecipient = StringSimilarityMeasure.removeDuplicatesLexE(tstWordsIntroducedByCRRecipient);
         tstWordsIntroducedByOther=StringSimilarityMeasure.removeDuplicatesLexE(tstWordsIntroducedByOther);
         
         
         
         Vector allRespLexemes = this.responsePriorToAck.getWordsAsLexicalEntries();
         for(int i=0;i<allRespLexemes.size();i++){
             LexiconEntry le = (LexiconEntry)allRespLexemes.elementAt(i);
             Conversant cWhoIntroduced =le.getConversantWhoIntroducedWord();
             
             if(cWhoIntroduced==crRecip){
                 respWordsIntroducedByCRRecipient.addElement(le);
             }
             else if(cWhoIntroduced==crOther){
                 respWordsIntroducedByOther.addElement(le);
             }    
         }
         respWordsIntroducedByCRRecipient = StringSimilarityMeasure.removeDuplicatesLexE(respWordsIntroducedByCRRecipient);
         respWordsIntroducedByOther=StringSimilarityMeasure.removeDuplicatesLexE(respWordsIntroducedByOther);
         
         
         Vector allNextLexemes = this.nextTurnAfterAck.getWordsAsLexicalEntries();
         for(int i=0;i<allNextLexemes.size();i++){
             LexiconEntry le = (LexiconEntry)allNextLexemes.elementAt(i);
             Conversant cWhoIntroduced =le.getConversantWhoIntroducedWord();
             
             if(cWhoIntroduced==crRecip){
                 nextWordsIntroducedByCRRecipient.addElement(le);
             }
             else if(cWhoIntroduced==crOther){
                 nextWordsIntroducedByOther.addElement(le);
             }    
         }
         nextWordsIntroducedByCRRecipient = StringSimilarityMeasure.removeDuplicatesLexE(nextWordsIntroducedByCRRecipient);
         nextWordsIntroducedByOther=StringSimilarityMeasure.removeDuplicatesLexE(nextWordsIntroducedByOther);       
     }
     
     
     public void determineWOTFRAG(){
         if(cr.getTextString().indexOf("ehh")>-1||
                 cr.getTextString().indexOf("err")>-1||
                 cr.getTextString().indexOf("uhh")>-1||
                 cr.getTextString().indexOf("huh")>-1||
                 cr.getTextString().indexOf("sorry")>-1||
                 cr.getTextString().indexOf("sory")>-1||
                 cr.getTextString().indexOf("what")>-1||
                 cr.getTextString().indexOf("What")>-1||
                 cr.getTextString().indexOf("wot")>-1){
                    wotFRAG="WOT";
         }
         else{
             wotFRAG="FRAG";
             calculateIFResponseContainsCR();
         }
     }
     public void calculateIFResponseContainsCR(){
         
         Vector vCRResp =StringSimilarityMeasure.getAllSharedWords(cr.getWordsAsStrings_NOPARTSOFSPEECH(), this.responsePriorToAck.getStrings());
         if(vCRResp.size()>0){
             this.respContainsCR=true;
         }
         Vector vCRNext =StringSimilarityMeasure.getAllSharedWords(cr.getWordsAsStrings_NOPARTSOFSPEECH(), this.nextTurnAfterAck.getStrings());
         if(vCRNext.size()>0){
             this.nextContainsCR=true;
         }
     }
}

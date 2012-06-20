/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package diet.server.conversationhistory.turn;

import java.util.Vector;

import diet.server.conversationhistory.Conversant;
import diet.server.conversationhistory.ConversationHistory;
import edu.stanford.nlp.trees.Tree;

/**
 *
 * @author Noah
 */
public class CCCUSTOM1Turn extends Turn{
    public String finalText;
    public String text;

    public CCCUSTOM1Turn(){

    }

    public CCCUSTOM1Turn(ConversationHistory cH,String type, long onset, long enter,Conversant sender, Conversant apparentSender,String text,
          Vector recipients,boolean wasBlocked,Vector keyPresses,Vector documentUpdates,Vector parsedWords,Tree parseTree,long turnNo, Vector additionalStrings, String finalText) {
        super(cH, "CCCUSTOM1Turn", onset, enter, sender, apparentSender, text, recipients, wasBlocked, keyPresses, documentUpdates, parsedWords, parseTree, turnNo, new Vector());
        this.finalText = finalText;
    }

    // let's see if this works to add log data to turns.txt
    @Override
    public String getIOAdditionalHeaders(){
        return  "|EnteredText|SentText";
    }
    @Override
    public String getIOAdditionalValues(){
        return "|" + text + "|" + finalText;
    }
   
}

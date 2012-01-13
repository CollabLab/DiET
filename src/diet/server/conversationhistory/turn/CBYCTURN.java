/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package diet.server.conversationhistory.turn;

import java.util.Vector;

import diet.server.Conversation;
import diet.server.conversationhistory.Conversant;
import diet.server.conversationhistory.ConversationHistory;
import edu.stanford.nlp.trees.Tree;

/**
 *
 * @author Greg
 */
public class CBYCTURN extends Turn{

    
    public String eString="";
    public int eStart=-99999;
    public int eFinish=-99999;
    private String type="NOT SET";
    public long timeOfFirstSend=-99999;
    public long timeOfLastSend=-99999;
    public String finalText;
    
    
    public CBYCTURN(){
        String type = "CONSTRUCTEDTHRUEMPTYHEADER";
    }
    
    public CBYCTURN(ConversationHistory cH, long onset, long enter, Conversant sender, Conversant apparentSender, String text, Vector recipients, boolean wasBlocked, Vector keyPresses, Vector documentUpdates, Vector parsedWords, Tree parseTree, int turnNo,
            String eString, int eStart,int eFinish,String type,long timeOfFirstSend,long timeOfLastSend,String finalText) {
        super(cH,"CBYCTURN", onset, enter, sender, apparentSender, text.replaceAll ("\\n", " "), recipients, wasBlocked, keyPresses, documentUpdates, parsedWords, parseTree, turnNo, new Vector());
        this.eString=eString.replaceAll ("\\n", "");
        this.eStart=eStart;
        this.eFinish=eFinish;
        this.type=type;
        this.timeOfFirstSend=timeOfFirstSend;
        this.timeOfLastSend=timeOfLastSend;
        this.finalText=finalText.replaceAll ("\\n", "");
    
    }

    @Override
    public String getIOAdditionalHeaders(){
        return  "|"+ "CBYCestring"+"|"+"CBYCestart"+"|"+"CBYCefinish"+"|"+"CBYCtype"+"|"+"CBYCfirstsend"+"|"+"CBYClastsend1"+"|"+"CBYCFinalText";
    }
    @Override
    public String getIOAdditionalValues(){
        return "|" + eString+"|"+eStart+"|"+eFinish+"|"+type+"|"+timeOfFirstSend+"|"+timeOfLastSend+"|"+finalText;
    }
    public long getTypingSpeedForTurnCharactersPerSecond()
    {
    	if (this.timeOfLastSend-this.timeOfFirstSend==0) return 3;
    	long speed=this.finalText.length()*1000/(this.timeOfLastSend-this.timeOfFirstSend);
    	
    	Conversation.printWSln("CBYCTurn", "Typing speed is: "+speed);
    	return speed;
    }
   
}

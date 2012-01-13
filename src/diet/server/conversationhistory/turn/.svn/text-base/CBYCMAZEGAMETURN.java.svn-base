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
 * @author Greg
 */
public class CBYCMAZEGAMETURN extends Turn{

    
    public String eString="";
    public int eStart=-99999;
    public int eFinish=-99999;
    private String type="NOT SET";
    public long timeOfFirstSend=-99999;
    public long timeOfLastSend=-99999;
    public String finalText;
    public int mazeNo;
    public int moveNoATSTART;
    public int senderMoveNoATSTART;
    public int moveNoATEND;
    public int senderMoveNoATEND;
        
    public boolean senderIsOnSwitchAtBegin = false;
    public int senderSwitchTraversalCountAtBegin=0;
    public int recipientMoveNoATSTART =0 ;
    public boolean recipientIsOnSwitchAtBegin = false;
    public int recipientSwitchTraversalCountAtBegin=0;



    public boolean senderIsOnSwitchAtEnd = false;
    public int senderSwitchTraversalCountAtEnd=0;
    public int recipientMoveNoATEND=0;
    public boolean recipientIsOnSwitchAtEnd = false;
    public int recipientSwitchTraversalCountAtEnd=0;

    
    
    public CBYCMAZEGAMETURN(){
        String type = "CONSTRUCTEDTHRUEMPTYHEADER";
    }


public CBYCMAZEGAMETURN(ConversationHistory cH, long onset, long enter, Conversant sender, Conversant apparentSender, String text, Vector recipients, boolean wasBlocked, Vector keyPresses, Vector documentUpdates, Vector parsedWords, Tree parseTree, int turnNo,
      String eString, int eStart,int eFinish,String type,long timeOfFirstSend,long timeOfLastSend,String finalText,
      int mazeNo,int moveNoATSTART,int indivMoveNoATSTART,int moveNoATEND,int indivMoveNoATEND,   
      boolean senderIsOnSwitchAtBegin,int senderSwitchTraversalCountAtBegin, int recipientMoveNoATSTART, boolean recipientIsOnSwitchAtBegin, int recipientSwitchTraversalCountAtBegin,
      boolean senderIsOnSwitchAtEnd, int senderSwitchTraversalCountAtEnd, int recipientMoveNoATEND, boolean recipientIsOnSwitchAtEnd, int recipientSwitchTraversalCountAtEnd)
      {
           this(cH, onset, enter,  sender,  apparentSender, text, recipients, wasBlocked, keyPresses, documentUpdates, parsedWords, parseTree, turnNo,
             eString, eStart, eFinish, type, timeOfFirstSend, timeOfLastSend, finalText, mazeNo,moveNoATSTART,indivMoveNoATSTART, moveNoATEND, indivMoveNoATEND);

    this.senderIsOnSwitchAtBegin = false;
    this.senderSwitchTraversalCountAtBegin=senderSwitchTraversalCountAtBegin;
    this.recipientMoveNoATSTART =recipientMoveNoATSTART ;
    this.recipientIsOnSwitchAtBegin = recipientIsOnSwitchAtBegin;
    this.recipientSwitchTraversalCountAtBegin=recipientSwitchTraversalCountAtBegin;



    this.senderIsOnSwitchAtEnd = senderIsOnSwitchAtEnd;
    this.senderSwitchTraversalCountAtEnd=senderSwitchTraversalCountAtEnd;
    this.recipientMoveNoATEND=recipientMoveNoATEND;
    this.recipientIsOnSwitchAtEnd = recipientIsOnSwitchAtEnd;
    this.recipientSwitchTraversalCountAtEnd=recipientSwitchTraversalCountAtEnd;

     }

    public CBYCMAZEGAMETURN(ConversationHistory cH, long onset, long enter, Conversant sender, Conversant apparentSender, String text, Vector recipients, boolean wasBlocked, Vector keyPresses, Vector documentUpdates, Vector parsedWords, Tree parseTree, int turnNo,
            String eString, int eStart,int eFinish,String type,long timeOfFirstSend,long timeOfLastSend,String finalText,
            int mazeNo,int moveNoATSTART,int indivMoveNoATSTART,int moveNoATEND,int indivMoveNoATEND) {
        super(cH, "CBYCMAZEGAMETURN",onset, enter, sender, apparentSender, text.replaceAll ("\\n", " "), recipients, wasBlocked, keyPresses, documentUpdates, parsedWords, parseTree, turnNo,new Vector());
        this.eString=eString.replaceAll ("\\n", "");
        this.eStart=eStart;
        this.eFinish=eFinish;
        this.type=type;
        this.timeOfFirstSend=timeOfFirstSend;
        this.timeOfLastSend=timeOfLastSend;
        this.finalText=finalText.replaceAll ("\\n", "");
        this.mazeNo=mazeNo;
        this.moveNoATSTART=moveNoATSTART;
        this.senderMoveNoATSTART=indivMoveNoATSTART;
        this.moveNoATEND=moveNoATEND;
        this.senderMoveNoATEND=indivMoveNoATEND;
    
    }

    @Override
    public String getIOAdditionalHeaders(){
        return  "|"+ "CBYCestring"+"|"+"CBYCestart"+"|"+"CBYCefinish"+"|"+"CBYCtype"+"|"+"CBYCfirstsend"+"|"+"CBYClastsend1"+"|"+"CBYCFinalText"+"|"+"MazeNo"+"|"+"AbsMoveNoATSTART"+"|"+"IndivMoveNoATSTART"+"|"+"AbsMoveNoATEND"+"|"+"IndivMoveNoATEND";
    }
    @Override
    public String getIOAdditionalValues(){
        return "|" + eString+"|"+eStart+"|"+eFinish+"|"+type+"|"+timeOfFirstSend+"|"+timeOfLastSend+"|"+finalText+"|"+mazeNo+"|"+moveNoATSTART +"|"+senderMoveNoATSTART +"|"+moveNoATEND +"|"+ senderMoveNoATEND+"|"+

                senderIsOnSwitchAtBegin + "|"+
                senderSwitchTraversalCountAtBegin + "|"+
                recipientMoveNoATSTART + "|"+
                recipientIsOnSwitchAtBegin + "|"+
                recipientSwitchTraversalCountAtBegin + "|"+



                senderIsOnSwitchAtEnd + "|"+
                senderSwitchTraversalCountAtEnd+ "|"+
                recipientMoveNoATEND+ "|"+
                recipientIsOnSwitchAtEnd + "|"+
                recipientSwitchTraversalCountAtEnd;


    }
   
}

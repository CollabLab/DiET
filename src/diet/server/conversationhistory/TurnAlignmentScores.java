/*
 * AlignmentScoreSinglePreviousTurn.java
 *
 * Created on 06 October 2007, 21:42
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package diet.server.conversationhistory;
import java.io.Serializable;
import java.util.Vector;

import diet.server.conversationhistory.turn.Turn;

public class TurnAlignmentScores implements Serializable{
    
     private Turn priorByPrevious;
     private Turn nextTurn;
    // private AlignmentScore alignmentScoreWithImmediatelyPriorTurn;
     //private AlignmentScore alignmentScoreWithImmediatelyPriorTurnOnlyText;
     private int alignmentScoreWithImmediatelyPriorContiguousTurn;
     private int alignmentScoreWithImmediatelyPriorContiguousTurnOnlyText;
    
     
     
    
    public TurnAlignmentScores(Turn priorByPrevious,Turn nextTurn) {
        this.priorByPrevious = priorByPrevious;
        this.nextTurn = nextTurn;
        Vector v = priorByPrevious.getWordsAsLexicalEntries();
       //for(int )
    
    }
    
}

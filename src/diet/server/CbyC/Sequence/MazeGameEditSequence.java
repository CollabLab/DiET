/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package diet.server.CbyC.Sequence;

import java.util.Date;

import diet.message.MessageCBYCTypingUnhinderedRequest;
import diet.server.CbyC.Sequences;
import diet.server.ConversationController.CCCBYCDefaultController;

/**
 *
 * @author Greg
 */
public class MazeGameEditSequence extends MazeGameDefaultSequence{

    public MazeGameEditSequence(Sequences sS, CCCBYCDefaultController cC, String sender, MessageCBYCTypingUnhinderedRequest mCTUR) {
        super(sS, cC, sender, mCTUR);
    }

    public MazeGameEditSequence(Sequences sS, CCCBYCDefaultController cC, String sender, Date startTime, String elementText, int eStartPos, int eFinishPos) {
        super(sS, cC, sender, startTime, elementText, eStartPos, eFinishPos);
    }

    @Override
    public String getType() {
        return "MAZEGAMEEDIT"+super.getType();
    }

    
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package diet.server.CbyC.Sequence;


import diet.message.MessageCBYCTypingUnhinderedRequest;
import diet.server.CbyC.DocInsert;
import diet.server.CbyC.FloorHolderAdvanced;
import diet.server.CbyC.Sequence.FlowControl.FlowControlDelay;
import diet.server.CbyC.Sequence.FlowControl.FlowControlSetInputClosedAndGiveParticipantFloorPostClarificationRequest;
import diet.server.Participant;
import diet.server.CbyC.Sequences;
import diet.server.Conversation;
import diet.server.ConversationController.CCCBYCDefaultController;
import diet.server.StringOfDocChangeInserts;
import java.util.Date;
import java.util.Random;
import java.util.Vector;

/**
 *
 * @author Greg
 */
public class MazeGameClarificationRequest extends MazeGameDefaultSequence{

    Random r = new Random();
    

    String apparentSender;
    public String recipientOfCR;

    Participant apparentSenderP;
    Participant recipientP;

    String clarificationRequestText = "";
    int crDelayMEAN;
    int crDELAYPLUSMINUS;

    public MazeGameClarificationRequest(Sequences sS, CCCBYCDefaultController cC, String recipientOfCR, String participantBlocked, MessageCBYCTypingUnhinderedRequest mCTUR, String clarificationRequest, int crDelayMEAN, int crDELAYPLUSMINUS) {
        super(sS, cC, "server", mCTUR);
        super.giveFloorRequestorTheFloor=false;
        this.crDelayMEAN = crDelayMEAN;
        this.crDELAYPLUSMINUS = crDELAYPLUSMINUS;


        Conversation.printWSln("Main", "Should generate CR");
        cC.fh.setAutomaticallyAllowOpenFloorAfterIsTypingTimeout(false);
        this.apparentSender = participantBlocked;
        this.recipientOfCR = recipientOfCR;

        apparentSenderP = cC.getC().getParticipants().findParticipantWithUsername(participantBlocked);
        this.recipientP = cC.getC().getParticipants().findParticipantWithUsername(recipientOfCR);

        Conversation.printWSln("MMMMMM1", recipientP.getUsername());
        Conversation.printWSln("MMMMMM2", apparentSenderP.getUsername());

        this.clarificationRequestText = clarificationRequest;

        ((FloorHolderAdvanced)cC.fh).blockAllIncomingFloorRequests(true);
    }

    public String getClarificationText(){
        return this.clarificationRequestText;
    }

    public long getTimeOfLastEnter(){
        return this.timeOfLastSend;
    }

    


    public void createHeader(){
        Vector v = new Vector();
        Vector recipients = cC.getC().getParticipants().getAllOtherParticipants(apparentSenderP);
        this.startTime = new Date();
        for(int i=0;i<recipients.size();i++){
            
            Participant p = (Participant)recipients.elementAt(i);
            Conversation.printWSln("MMMMMM3", apparentSenderP.getUsername()+p.getUsername());
            DocInsert di = sS.getStyleManager().getPrefixForParticipant("s",p, apparentSenderP);
            System.err.println("SEQUENCEHEADERATTRIBUTE IS "+di.getAttrSet().toString());
            di.recipient=p.getUsername();
            v.addElement(di);
        }
        fb.enqueue(v);

        int unique = sS.getStyleManager().getUniqueIntForRecipient(recipientP,apparentSenderP);

        
        Vector vCR = StringOfDocChangeInserts.getInsEquivalentOfString(this.clarificationRequestText);
        for(int i=0;i<vCR.size();i++){
            DocInsert di = (DocInsert)vCR.elementAt(i);
            di.a="n"+unique;
            di.recipient=this.recipientOfCR;
            Vector nw= new Vector();
            nw.addElement(di);
            this.docChangesBySender.addElement(di);
            fb.enqueue(nw);
            fb.enqueue(new FlowControlDelay(this,this.crDelayMEAN+r.nextInt(this.crDELAYPLUSMINUS)));
        }

         cC.getC().getParticipants().sendLabelDisplayToParticipant(recipientP, apparentSenderP.getUsername()+"...is Typing", true);

         FlowControlSetInputClosedAndGiveParticipantFloorPostClarificationRequest fc = new FlowControlSetInputClosedAndGiveParticipantFloorPostClarificationRequest(this);
         fb.enqueue(fc);
  
         cC.getC().getCHistoryUIM().updateChatToolTextEntryFieldsUI("Server", this.clarificationRequestText);


    }





    @Override
    public boolean getGiveFloorRequestorTheFloorOnStart() {
        Participant p = cC.getC().getParticipants().findParticipantWithUsername(apparentSender);
        cC.getC().getParticipants().sendAndEnableLabelDisplayToParticipant(p, "Network or Game Error...recalculating index..please wait", true, false);
        
        return false;
    }
    
  

   

    @Override
    public String getType() {
        return "CR: "+this.clarificationRequestText;
    }

    @Override
    public boolean hasBeenModified() {
        return false;
    }
    

 

}

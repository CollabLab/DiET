/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package diet.server.CbyC.Sequence;


import diet.message.MessageCBYCTypingUnhinderedRequest;
import diet.server.CbyC.DocInsert;
import diet.server.CbyC.FloorHolderAdvanced;
import diet.server.CbyC.Sequence.FlowControl.FlowControlDelay;
import diet.server.CbyC.Sequence.FlowControl.FlowControlSetOpenFloorPostACK;
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
public class MazeGameAcknowledgement extends MazeGameDefaultSequence{

    Random r = new Random();
    public int mazeNo;
    public int moveNoATSTART;
    public int indivMoveNoATSTART;
    public int moveNoATEND;
    public int indivMoveNoATEND;

    String apparentSender;
    public String recipientOfACK;

    Participant apparentSenderP;
    Participant recipientP;

    String ackText = "";

    int delayMEAN;
    int delayPLUSMINUS;

    

    public MazeGameAcknowledgement(Sequences sS, CCCBYCDefaultController cC, String recipientOfACK, String participantBLOCKED, String ackText, int delayMEAN, int delayPLUSMINUS) {
        super(sS, cC, "server");

        this.ackText=ackText;
        this.delayMEAN=delayMEAN;
        this.delayPLUSMINUS=delayPLUSMINUS;

        super.giveFloorRequestorTheFloor=false;
        Conversation.printWSln("Main", "Should be generating ACK");


        cC.fh.setAutomaticallyAllowOpenFloorAfterIsTypingTimeout(false);
        this.apparentSender = participantBLOCKED;
        this.recipientOfACK = recipientOfACK;

        apparentSenderP = cC.getC().getParticipants().findParticipantWithUsername(participantBLOCKED);
        this.recipientP = cC.getC().getParticipants().findParticipantWithUsername(recipientOfACK);

        Conversation.printWSln("MMMMMM1", recipientP.getUsername());
        Conversation.printWSln("MMMMMM2", apparentSenderP.getUsername());

        this.ackText = ackText;

        ((FloorHolderAdvanced)cC.fh).blockAllIncomingFloorRequests(true);
         createHeader();

         cC.getC().getParticipants().sendLabelDisplayToParticipant(recipientP, apparentSenderP.getUsername()+"...is Typing", true);
         cC.getC().getCHistoryUIM().updateChatToolTextEntryFieldsUI("Server", this.ackText);
    }

    

    


    public void createHeader(){
        this.startTime=new Date();
        Vector v = new Vector();
        Vector recipients = cC.getC().getParticipants().getAllOtherParticipants(apparentSenderP);
        for(int i=0;i<recipients.size();i++){
            
            Participant p = (Participant)recipients.elementAt(i);
            Conversation.printWSln("MMMMMM3b", apparentSenderP.getUsername()+p.getUsername());
            DocInsert di = sS.getStyleManager().getPrefixForParticipant("s",p, apparentSenderP);
            System.err.println("SEQUENCEHEADERATTRIBUTE IS "+di.getAttrSet().toString());
            di.recipient=p.getUsername();
            v.addElement(di);
        }
        fb.enqueue(v);

        int unique = sS.getStyleManager().getUniqueIntForRecipient(recipientP,apparentSenderP);

        
        Vector vCR = StringOfDocChangeInserts.getInsEquivalentOfString(this.ackText);
        for(int i=0;i<vCR.size();i++){
            DocInsert di = (DocInsert)vCR.elementAt(i);
            di.a="n"+unique;
            di.recipient=this.recipientOfACK;
            Vector nw= new Vector();
            nw.addElement(di);
            this.docChangesBySender.add(di);
            fb.enqueue(nw);
            fb.enqueue(new FlowControlDelay(this,delayMEAN+r.nextInt(delayPLUSMINUS)));
        }


         FlowControlSetOpenFloorPostACK fc = new FlowControlSetOpenFloorPostACK(this);
         fb.enqueue(fc);
  

    }





    @Override
    public boolean getGiveFloorRequestorTheFloorOnStart() {
        Participant p = cC.getC().getParticipants().findParticipantWithUsername(apparentSender);
        cC.getC().getParticipants().sendAndEnableLabelDisplayToParticipant(p, "Network Error...please wait", true, false);
        
        return false;
    }
    
    

    
   

    @Override
    public String getType() {
        return "MAZEGAMEACK";
    }

    @Override
    public boolean hasBeenModified() {
        return false;
    }

    @Override
    public boolean isSpeakerChangePermitted() {
        return super.isSpeakerChangePermitted();
    }
    

 

}

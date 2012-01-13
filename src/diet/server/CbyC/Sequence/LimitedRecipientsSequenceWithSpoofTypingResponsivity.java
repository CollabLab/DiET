package diet.server.CbyC.Sequence;

import java.util.Date;
import java.util.Vector;

import diet.server.Conversation;
import diet.server.Participant;
import diet.server.CbyC.DocChange;
import diet.server.CbyC.DocInsert;
import diet.server.CbyC.Sequences;
import diet.server.CbyC.Sequence.FlowControl.FlowControlDelay;
import diet.server.CbyC.Sequence.FlowControl.FlowControlDoSpoofResponsivityToTypingActivityOfParticipant;
import diet.server.ConversationController.CCCBYCDefaultController;

public class LimitedRecipientsSequenceWithSpoofTypingResponsivity extends
		LimitedRecipientsSequence {	
	
	FlowControlDoSpoofResponsivityToTypingActivityOfParticipant spoofTyping=null;
	public LimitedRecipientsSequenceWithSpoofTypingResponsivity(Sequences sS, CCCBYCDefaultController cC,
			String sender, Vector recipients, Participant apparentOrigin) {
		super(sS, cC, sender, recipients,apparentOrigin);
		
		
	
	}
	public LimitedRecipientsSequenceWithSpoofTypingResponsivity(Sequences sS, CCCBYCDefaultController cC,
			String sender, Vector recipients, Participant apparentOrigin, String condition) {
		super(sS, cC, sender,recipients,apparentOrigin,condition);
		
		
	
	}
	
	
	public synchronized Sequence addDocChange(DocChange dc) {
		if(this.spoofTyping==null)
			this.spoofTyping=new FlowControlDoSpoofResponsivityToTypingActivityOfParticipant(this, this.sender, this.getSS().getcC().getIsTypingTimeOut());
		
		this.spoofTyping.sendFakeIsTypingMessage();
		
		
		
		if (!dc.sender.equalsIgnoreCase(sender)) {
			if (dc instanceof DocInsert) {
				DocInsert di = (DocInsert) dc;
				Conversation.printWSln("ERROR", "New DocChange from: "
						+ dc.getSender() + ", " + di.str);
			}
			Conversation.printWSln("ERROR", "Existing Sequence: " + sender
					+ ", " + this.getFinalText());
			Conversation.printWSln("ERROR",
					"______________________________________________________");
		}
		Vector v = new Vector();
		this.docChangesBySender.addElement(dc);
		Participant senderP = cC.getC().getParticipants()
				.findParticipantWithUsername(dc.sender);
		
		for (int i = 0; i < recipients.size(); i++) {
			Participant p = (Participant) recipients.elementAt(i);
			DocChange dcCopy = dc.returnCopy();
			dcCopy.recipient = p.getUsername();
			if (dc instanceof DocInsert) {
				DocInsert di = (DocInsert) dcCopy;
				int unique = sS.getStyleManager().getUniqueIntForRecipient(p,
						senderP);
				di.a = "n" + unique;
			}
			v.addElement(dcCopy);
		}
		fb.enqueue(v);
		return null;
	}
	
	@Override
    public synchronized void setInputClosed() {
         if(spoofTyping!=null) spoofTyping.stopSpoofIsTypingMessages();
        super.setInputClosed();
    }

    @Override
    public synchronized void setInputClosedSpeakerChange() {
        if(spoofTyping!=null) spoofTyping.stopSpoofIsTypingMessages();
        super.setInputClosedSpeakerChange();
        
    }

    @Override
    public synchronized void setInputClosedEditOfOthersTurn() {
        if(spoofTyping!=null) spoofTyping.stopSpoofIsTypingMessages();
        super.setInputClosed();
    }

    @Override
    public synchronized void setInputClosedEditOfOwnTurn() {
       if(spoofTyping!=null) spoofTyping.stopSpoofIsTypingMessages();
        super.setInputClosed();
    }
    
    public void createHeader(){
        Vector v = new Vector();
        Participant pSender = cC.getC().getParticipants().findParticipantWithUsername(sender);
        
        for(int i=0;i<recipients.size();i++){
            Participant p = (Participant)recipients.elementAt(i);
            DocInsert di = sS.getStyleManager().getPrefixForParticipant("s",p, pSender);
            System.err.println("SEQUENCEHEADERATTRIBUTE IS "+di.getAttrSet().toString());
            di.recipient=p.getUsername();
            v.addElement(di);
        }
        
        fb.enqueue(v);
    }

}

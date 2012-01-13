package diet.server.CbyC.Sequence;

import java.util.Vector;

import diet.debug.Debug;
import diet.server.Conversation;
import diet.server.Participant;
import diet.server.StringOfDocChangeInserts;
import diet.server.CbyC.DocChange;
import diet.server.CbyC.DocInsert;
import diet.server.CbyC.Sequences;
import diet.server.ConversationController.CCCBYCDefaultController;

public class LimitedRecipientsSequence extends Sequence {

	
	Vector recipients;
	Participant apparentOrigin;
	String condition;

	public LimitedRecipientsSequence(Sequences sS, CCCBYCDefaultController cC,
			String sender, Vector recipients, Participant apparentOrigin) {
		super(sS, cC, sender);
		this.recipients = recipients;
		this.apparentOrigin=apparentOrigin;
		this.condition="";
	
	}
	public LimitedRecipientsSequence(Sequences sS, CCCBYCDefaultController cC,
			String sender, Vector recipients, Participant apparentOrigin, String condition) {
		super(sS, cC, sender);
		this.recipients = recipients;
		this.apparentOrigin=apparentOrigin;
		this.condition=condition;
	
	}
	public String getType()
	{
		if (!this.condition.equals("")) return "LimitedRecipientsSequence("+this.condition+")";
		else return "LimitedRecipientsSequence";
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

	public synchronized Sequence addDocChange_ButNotToDocChangesBySender(
			DocChange dc) {
		try {
			if (Debug.trackSeqADDDOCCHANGE)
				Conversation.printWSln("Main", "Sequence5a");
			Vector v = new Vector();
			if (Debug.trackSeqADDDOCCHANGE)
				Conversation.printWSln("Main", "Sequence5b");
			Participant senderP = cC.getC().getParticipants()
					.findParticipantWithUsername(dc.sender);
			if (Debug.trackSeqADDDOCCHANGE)
				Conversation.printWSln("Main", "Sequence5c");

			if (Debug.trackSeqADDDOCCHANGE)
				Conversation.printWSln("Main", "Sequence5d");
			for (int i = 0; i < recipients.size(); i++) {
				Participant p = (Participant) recipients.elementAt(i);
				if (Debug.trackSeqADDDOCCHANGE)
					Conversation.printWSln("Main", "SequenceE");
				DocChange dcCopy = dc.returnCopy();
				dcCopy.recipient = p.getUsername();
				if (dc instanceof DocInsert) {
					DocInsert di = (DocInsert) dcCopy;
					int unique = sS.getStyleManager().getUniqueIntForRecipient(
							p, senderP);
					di.a = "n" + unique;
					if (Debug.trackSeqADDDOCCHANGE)
						Conversation.printWSln("Main", "SequenceEa");
				}
				if (Debug.trackSeqADDDOCCHANGE)
					Conversation.printWSln("Main", "Sequence5f");
				v.addElement(dcCopy);
			}
			fb.enqueue(v);
			if (Debug.trackSeqADDDOCCHANGE)
				Conversation.printWSln("Main", "Sequence5g");
			StringOfDocChangeInserts sdi = new StringOfDocChangeInserts(
					docChangesBySender);
			if (Debug.trackSeqADDDOCCHANGE)
				Conversation.printWSln("Main", "Sequence5h");
			return null;
		} catch (Exception e) {
			this.cC.getC().saveErrorLog(
					"ERRORINADDOCCHANGEBUTNOTTODOCCHANGESBYSENDER");
			this.cC.getC().saveErrorLog(e);
			if (Debug.trackSeqADDDOCCHANGE)
				Conversation.printWSln("Main", "Sequence5i");
			return null;
		}
	}

	public synchronized Sequence addDocChange(DocChange dc) {
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

}

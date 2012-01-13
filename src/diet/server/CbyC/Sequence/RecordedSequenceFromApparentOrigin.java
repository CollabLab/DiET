package diet.server.CbyC.Sequence;

import java.util.Date;
import java.util.Random;
import java.util.Vector;

import diet.server.Conversation;
import diet.server.Participant;
import diet.server.StringOfDocChangeInserts;
import diet.server.CbyC.DocChange;
import diet.server.CbyC.DocInsert;
import diet.server.CbyC.DocInsertHeader;
import diet.server.CbyC.Sequences;
import diet.server.CbyC.Sequence.FlowControl.FlowControlDelay;
import diet.server.ConversationController.CCCBYCAbstractProbeCR;
import diet.server.ConversationController.CCCBYCDefaultController;
import diet.server.ConversationController.CCCBYCElicitSplitUtt;
import diet.server.ConversationController.UseOfPrerecordedFakeTurn;

public class RecordedSequenceFromApparentOrigin extends Sequence {

	private double typingSpeed;
	private Vector recipients;
	private String header;
	private Random rand = new Random();
	private String condition;

	public RecordedSequenceFromApparentOrigin(Sequences sS,
			CCCBYCDefaultController cC, String recording, double typingSpeed,
			Vector recipients, Participant apparentOrigin) {
		super(sS, cC, "server");
		this.recipients = recipients;
		// super.createHeader();

		this.typingSpeed = typingSpeed;

		this.header = apparentOrigin.getUsername();

		System.out.println("Recipients vector in recorded Seq "
				+ recipients.size());
		this.record(recording);
		this.setInputClosed();
	}

	public RecordedSequenceFromApparentOrigin(Sequences sS,
			CCCBYCDefaultController cC, String recording, double typingSpeed,
			Vector recipients, Participant apparentOrigin, String condition) {
		super(sS, cC, "server");
		this.recipients = recipients;
		this.typingSpeed = typingSpeed;
		this.header = apparentOrigin.getUsername();
		this.condition = condition;
		this.record(recording);
		this.setInputClosed();
	}

	public RecordedSequenceFromApparentOrigin(Sequences sS,
			CCCBYCDefaultController cC, String recording, double typingSpeed,
			Vector recipients, String headerText, String condition) {
		super(sS, cC, "server");
		this.recipients = recipients;
		this.typingSpeed = typingSpeed;
		this.header = headerText;
		this.condition = condition;
		this.record(recording);
		this.setInputClosed();

	}

	

	public String getApparentSender() {
		return this.header;
	}

	public String getType() {
		if (!this.condition.equals(""))
			return "RecordedSequenceFromApparentOrigin(" + this.condition + ")";
		else
			return "RecordedSequenceFromApparentOrigin";

	}

	public synchronized Sequence addDocChange(DocChange dc) {

		System.out
				.println("Adding DocChange to Recorded Sequence: ERROR: this shouldn't happen!");
		return null;
	}

	public String getFinalText() {
		StringOfDocChangeInserts sdi = new StringOfDocChangeInserts(
				getAllDocChangesFromSender());
		return sdi.getString();
	}

	public void record(String s) {

		//createHeader();

		for (int i = 0; i < s.length(); i++) {
			String ch = s.substring(i, i + 1);
			Vector v1 = new Vector();
			boolean addedToDocBySender = false;
			for (int j = 0; j < this.recipients.size(); j++) {
				Participant recipient = (Participant) this.recipients
						.elementAt(j);
				int uniqueInt;
				Participant apparentOrigin = this.cC.getC().getParticipants()
						.findParticipantWithUsername(header);
				if (apparentOrigin == null)
					uniqueInt = this.sS.getStyleManager()
							.getUniqueIntForRecipientNoSender(recipient);
				else
					uniqueInt = this.sS
							.getStyleManager()
							.getUniqueIntForRecipient(apparentOrigin, recipient);
				String style = "n" + uniqueInt;
				DocInsert di = new DocInsert("server", "server", recipient
						.getUsername(), 0, ch, style);
				if (!addedToDocBySender) {
					this.docChangesBySender.add(di);
					addedToDocBySender = true;
				}
				v1.addElement(di);

			}
			System.err.println("enqueing to recorded:" + ch);
			fb.enqueue(v1);
			Double delayF = new Double(1 / this.typingSpeed) * 1000;

			long delay = delayF.longValue();
			if (delay == 0)
				delay = 50;
			long randomDelay = Math.abs(rand.nextLong()) % (delay * 3 / 2);

			delay += randomDelay;

			System.err.println("delay is:" + delay);
			FlowControlDelay fl = new FlowControlDelay(this, delay);
			fb.enqueue(fl);

			// DocInsert di=new DocInsert
		}

	}

	public long getTimeOfLastEnter() {
		return this.timeOfLastEnter;

	}

	public String getStatusWindowTextForRecipient(String recipient) {

		return this.header + "...is typing";
	}

	public void createHeader() {
		Vector v = new Vector();

		for (int i = 0; i < recipients.size(); i++) {

			Participant recipient = (Participant) recipients.elementAt(i);
			int unique;
			Participant apparentSender = this.cC.getC().getParticipants()
					.findParticipantWithUsername(this.header);
			if (apparentSender != null)
				unique = this.sS.getStyleManager().getUniqueIntForRecipient(
						recipient, apparentSender);
			else
				unique = this.sS.getStyleManager()
						.getUniqueIntForRecipientNoSender(recipient);

			DocInsert di = new DocInsertHeader("server", this.header, recipient
					.getUsername(), 0, "\n" + this.header + "\n", "h" + unique);

			// sS.getStyleManager().getPrefixForParticipant("s",recipient,
			// apparentOrigin);
			// new DocInsertHeader("server",
			// "Q-BOT",p.getUsername(),0,"\n"+"Q-BOT"+"\n","h2");

			di.recipient = recipient.getUsername();
			v.addElement(di);
		}
		fb.insert(v, 0);
	}

	long timeOfLastEnter = new Date().getTime();

	public void main() {

		super.main();
		this.timeOfLastEnter = new Date().getTime();

		System.err.println("EXITING FROM " + this.getClass().toString()
				+ this.getFinalText());
		if (this.cC instanceof UseOfPrerecordedFakeTurn)
			((UseOfPrerecordedFakeTurn) this.cC)
			.finishedSendingFakeTurn(this.timeOfLastSend);
		else Conversation.printWSln("ERROR", "Wrong controller type in recordedseqfromapparentorig. Should implement useofprerecordedfaketurn");
		
		return;

	}

}

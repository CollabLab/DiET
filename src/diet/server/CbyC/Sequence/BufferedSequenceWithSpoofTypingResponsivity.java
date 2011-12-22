package diet.server.CbyC.Sequence;

import java.util.Date;
import java.util.Vector;

import diet.debug.Debug;
import diet.message.MessageCBYCDocChangeToClient;
import diet.message.MessageCBYCTypingUnhinderedRequest;
import diet.server.Conversation;
import diet.server.Participant;
import diet.server.CbyC.DocChange;
import diet.server.CbyC.DocInsert;
import diet.server.CbyC.Sequences;
import diet.server.CbyC.Sequence.FlowControl.FlowControl;
import diet.server.CbyC.Sequence.FlowControl.FlowControlDelay;
import diet.server.CbyC.Sequence.FlowControl.FlowControlDoSpoofResponsivityToTypingActivityOfParticipant;
import diet.server.ConversationController.CCCBYCDefaultController;
import diet.server.ConversationController.CCCBYCElicitSplitUtt;

public class BufferedSequenceWithSpoofTypingResponsivity extends Sequence {
	
	/**
	 * Sequence only works with CCCBYCElicitSplitUtt. So it's ad-hoc.
	 * Should make things more generic . . . OO-design is getting quite scrappy in the chat tool.
	 * Has lead to unnecessary duplication of effort.	 
	 *  
	 * Sequence responds to typing activity of owner sending spoof "is typing" messages
	 * Sequence buffers the input adding FlowControlDelay between DocChanges for later
	 * playback (whenever added to Sequences). When buffer is emptied, sequence stops spoofing
	 * and acts as a normal sequence would.
	 * 
	 */
	FlowControlDoSpoofResponsivityToTypingActivityOfParticipant spoofTyping;
	String condition="";
	public BufferedSequenceWithSpoofTypingResponsivity(Sequences sS,
			CCCBYCElicitSplitUtt cC, String sender,
			MessageCBYCTypingUnhinderedRequest mCTUR, String condition) {
		super(sS, cC, sender, mCTUR);
		this.timeOfLastDocChange = -1;
		this.condition=condition;

	}
	public BufferedSequenceWithSpoofTypingResponsivity(Sequences sS,
		CCCBYCElicitSplitUtt cC, String sender,
		MessageCBYCTypingUnhinderedRequest mCTUR) {
	super(sS, cC, sender, mCTUR);
	this.timeOfLastDocChange = -1;
	this.condition="";
	}

	public BufferedSequenceWithSpoofTypingResponsivity(Sequences sS,
			CCCBYCElicitSplitUtt cC, String sender) {
		super(sS, cC, sender);
		this.timeOfLastDocChange = -1;
		this.condition="";
	}
	public BufferedSequenceWithSpoofTypingResponsivity(Sequences sS,
		CCCBYCElicitSplitUtt cC, String sender, String condition) {
	    super(sS, cC, sender);
	    this.timeOfLastDocChange = -1;
	    this.condition=condition;
	}

	long timeOfLastDocChange;
	private boolean buffering = true;
	private boolean bufferEmptied = false;

	public synchronized Sequence addDocChange(DocChange dc) {
		if (buffering) {
			if (this.spoofTyping == null)
				this.spoofTyping = new FlowControlDoSpoofResponsivityToTypingActivityOfParticipant(
						this, this.sender, this.getSS().getcC()
								.getIsTypingTimeOut());

			this.spoofTyping.sendFakeIsTypingMessage();

			if (this.timeOfLastDocChange > 0) {
				fb.enqueue(new FlowControlDelay(this, new Date().getTime()
						- this.timeOfLastDocChange));
				timeOfLastDocChange = new Date().getTime();
			} else
				timeOfLastDocChange = new Date().getTime();
		}
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
		Vector recipients = cC.getC().getParticipants()
				.getAllOtherParticipants(
						cC.getC().getParticipants()
								.findParticipantWithUsername(dc.getSender()));
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
		if (spoofTyping != null)
			spoofTyping.stopSpoofIsTypingMessages();
		super.setInputClosed();
	}

	@Override
	public synchronized void setInputClosedSpeakerChange() {
		if (spoofTyping != null)
			spoofTyping.stopSpoofIsTypingMessages();
		super.setInputClosedSpeakerChange();

	}

	@Override
	public synchronized void setInputClosedEditOfOthersTurn() {
		if (spoofTyping != null)
			spoofTyping.stopSpoofIsTypingMessages();
		super.setInputClosed();
	}

	@Override
	public synchronized void setInputClosedEditOfOwnTurn() {
		if (spoofTyping != null)
			spoofTyping.stopSpoofIsTypingMessages();
		super.setInputClosed();
	}

	public void stopSpoofResponsivity() {
		if (spoofTyping != null)
			spoofTyping.stopSpoofIsTypingMessages();
	}

	// for this to work, the owner of the sequence must be blocked from typing
	// until the initial buffer's flushed completly
	// the sequence will then stop buffering and behave like a normal sequence,
	// sending chars out as they come in without
	// recording delays in between.

	public void main() {
		
		if (fb.getSize() == 0 && !bufferEmptied)

		{
			CCCBYCElicitSplitUtt cc;
			if (this.cC instanceof CCCBYCElicitSplitUtt)
				((CCCBYCElicitSplitUtt) this.cC).resetToNormal();
			else {
				Conversation
						.printWSln(
								"ERROR",
								"Error in BufferedSequenceWithSpoofResponsivity"
										+ "\nConversation Controller should be of type CCCBYCElicitSplitUtt, but isn't");
				return;
			}
			bufferEmptied = true;
		}
		
		while (!fb.isInputCompleted() || fb.getSize() > 0) {

			Conversation.printWSln("SeqMain", "inside While Loop");
			System.err.println("outputting sequence. current size is:"
					+ fb.getSize());
			try {
				// sS.sleep(400);
				System.err.println("INSIDE2b");
				Object o = fb.getNextBlockingObeyingTrickle();
				/*
				 * if (fb.getSize()==0)
				 * 
				 * { ((CCCBYCElicitSplitUtt) this.cC)
				 * .finishedFlushingBuffer(this.timeOfLastSend);
				 * this.stopRecordingDelaysAndSpoofResponsivity(); }
				 */
				if (o != null && Debug.showErrorsCalculatingDocInsertsSent) {

					Conversation.printWSln("DOCINSERTSOUTOFFB", o.toString());
				}
				System.err.println("INSIDE3");
				if (o instanceof FlowControl) {
					System.err.println("INSIDE3b");
					FlowControl fc = (FlowControl) o;
					fc.controlFlow();

				}
				if (o != null && o instanceof Vector) {

					System.err.println("INSIDE4");
					Vector v = (Vector) o;
					for (int i = 0; i < v.size(); i++) {
						System.err.println("INSIDE5");
						DocChange dc = (DocChange) v.elementAt(i);
						System.err.println("INSIDE6");
						Participant recipient = cC.getC().getParticipants()
								.findParticipantWithUsername(dc.getRecipient());
						System.err.print("INSIDE7:" + recipient.getUsername());
						if (dc instanceof DocInsert) {
							DocInsert dci = (DocInsert) dc;
							System.err.print("...." + dci.getStr() + "....."
									+ dci.elementString);
						}
						System.err.println("");
						MessageCBYCDocChangeToClient mct = new MessageCBYCDocChangeToClient(
								dc.sender, dc.apparentSender, 0, dc);
						System.err.println("INSIDE8");
						this.timeOfLastSend = new Date().getTime();
						if (!this.timeOfFirstSendBeenRecorded) {
							timeOfFirstSendBeenRecorded = true;
							timeOfFirstSend = new Date().getTime();
						}
						long timeStampSend = new Date().getTime();
						recipient.sendMessage(mct);
						dc.setTimeStampOfSend(timeStampSend);
						this.updateAllRecipients(recipient.getUsername(), dc);
						System.err.println("INSIDE9");
					}

				}

			} catch (Exception e) {
				System.err.println("INSIDEERROR");
				e.printStackTrace();
			}
			if (fb.getSize() == 0 && !bufferEmptied)

			{
				CCCBYCElicitSplitUtt cc;
				if (this.cC instanceof CCCBYCElicitSplitUtt)
					((CCCBYCElicitSplitUtt) this.cC).resetToNormal();
				else {
					Conversation
							.printWSln(
									"ERROR",
									"Error in BufferedSequenceWithSpoofResponsivity"
											+ "\nConversation Controller should be of type CCCBYCElicitSplitUtt, but isn't");
					return;
				}
				bufferEmptied = true;
			}

		}

		System.err.println("EXITING FROM " + this.getClass().toString()
				+ this.getFinalText());

		return;

	}

	public String getType() {
	    return (condition.isEmpty()?"BufferedSequence":"BufferedSequence("+this.condition+")");
		
	}

	public void stopRecordingDelaysAndSpoofResponsivity() {
		this.stopSpoofResponsivity();
		this.buffering = false;
		// TODO Auto-generated method stub

	}

}

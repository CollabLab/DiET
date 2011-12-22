/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package diet.server.CbyC;

import diet.client.StyleManager;
import java.util.HashMap;
import java.util.Vector;

import diet.message.MessageCBYCDocChangeFromClient;
import diet.message.MessageCBYCTypingUnhinderedRequest;
import diet.server.Conversation;
import diet.server.Participant;
import diet.server.CbyC.Sequence.Sequence;
import diet.server.ConversationController.CCCBYCDefaultController;

/**
 * 
 * @author Greg
 */
public class Sequences extends Thread {

	CCCBYCDefaultController cC;
	StyleManager sm;
	Vector sequences = new Vector();
	HashMap<Participant, Long> typingSpeeds;
	// Sequence currentIn;
	private DocChange dcLastIncoming;

	public Sequences(CCCBYCDefaultController cC) {
		this.cC = cC;
		sm = cC.getStyleManager();
		typingSpeeds = new HashMap<Participant, Long>();
		start();
	}

	public StyleManager getStyleManager() {
		return sm;
	}

	public synchronized void setNextSequenceIsSpeakerChange() {
		this.dcLastIncoming = null;
	}

	public DocChange getDCLastIncoming() {
		return this.getDCLastIncoming();
	}

	public synchronized boolean canFloorBeRelinquished() {
		if (sequences.size() == 0)
			return true;
		for (int i = sequences.size() - 1; i >= 0; i--) {
			Sequence seq = (Sequence) sequences.elementAt(i);
			if (seq.isInputAndOutputFinished())
				break;
			if (!seq.isSpeakerChangePermitted())
				return false;
		}
		return true;
	}

	public synchronized boolean isSpeakerChangePermitted(Participant p,
			MessageCBYCTypingUnhinderedRequest mCTUR) {
		if (sequences.size() == 0)
			return true;
		Sequence seq = (Sequence) this.sequences.lastElement();
		return seq.isSpeakerChangePermitted();
	}

	private synchronized boolean isSpeakerChangePermitted() {
		if (sequences.size() == 0)
			return true;
		Sequence seq = (Sequence) this.sequences.lastElement();
		return seq.isSpeakerChangePermitted();
	}

	public CCCBYCDefaultController getcC() {
		return cC;
	}

	/**
	 * 
	 * This method is called by the FloorHolder, once it has determined that it
	 * would be OK for a speaker change. At this point, it can be customized
	 * what happens when a request comes in. It returns true if speaker change
	 * permitted
	 */
	public synchronized boolean processSpeakerChangeRequest_SetNewSequence(
			Participant p, MessageCBYCTypingUnhinderedRequest mCTUR) {
		if (!isSpeakerChangePermitted(p, mCTUR))
			return false;
		Sequence sPrev = null;
		if (sequences.size() > 0) {
			sPrev = (Sequence) sequences.elementAt(sequences.size() - 1);
			sPrev.setInputClosedSpeakerChange();
		}
		int posFrmRight = mCTUR.getOffsetFrmRight();
		if (posFrmRight == 0) {
			Conversation.printWSln("CREATING", "2");
			Sequence seq = cC.getNextSequence_Speaker_Change(sPrev, sequences
					.size(), this, p, mCTUR);
			if (seq==null) return false;
			Conversation.printWSln("sequences", "Creating header for new sequence:"+seq.getClass());
			seq.createHeader();
			sequences.add(seq);
			notifyAll();
			return seq.getGiveFloorRequestorTheFloorOnStart();

		} else {
			Conversation.printWSln("CREATING", "1");
			Sequence seq = cC.getNextSequence_Edit_By_Different_Speaker(sPrev,
					sequences.size(), this, p, mCTUR);
			sequences.add(seq);
		}
		notifyAll();
		return true;
	}

	/**
	 * This method adds a recorded sequence to the top of all the sequences. it
	 * should only be called if all further input to the last/top sequence (last
	 * turn) in sequences has been blocked, e.g. by sending network error/floor
	 * status change. Note that method waits for all docchanges already in the
	 * last sequence to be flushed/outputted first and only then adds the
	 * recorded sequence.
	 * 
	 * @param takes
	 *            an already instantiated RecordedSequenceFromApparentOrigin
	 * @return returns true if the Recorded Sequence has been successfully added
	 * 
	 * 
	 */

	public synchronized void addSequence(Sequence s) {
		Sequence sPrev = null;
		if (sequences.size() > 0) {
			sPrev = (Sequence) sequences.elementAt(sequences.size() - 1);
			sPrev.setInputClosed();
			synchronized (sPrev) {
				while (!sPrev.isInputAndOutputFinished()) {
					try {
						Conversation.printWSln("Main", "Trying to close "
								+ sPrev.getType());
						wait(50);
						sPrev.setInputClosed();
					} catch (InterruptedException e) {
						Conversation.saveErr(e);

					}
				}
			}
		}
		
		Conversation.printWSln("Sequences", "Managed to close " + sPrev.getType()
				+ "...so now adding" + s.getType());
		sequences.add(s);
		try {
			this.dcLastIncoming = (DocChange) s.getAllDocChangesFromSender()
					.lastElement();
		} catch (Exception e) {
			dcLastIncoming = null;
		}
		Conversation.printWSln("Sequences", "Exited addSequence()");
		notifyAll();
	}

        //This is simply to help it compile...will be changed, possibly removerd//merged on later update
        public synchronized void addRecordedSequenceFromApparentOrigin(Sequence s) {
           Sequence sPrev = null;
           Conversation.printWSln("Main", "Trying to close (0).."+s);
	   if (sequences.size() > 0) {
	       sPrev = (Sequence) sequences.elementAt(sequences.size() - 1);
	       Conversation.printWSln("Main", "Trying to close (1).."+sPrev.getType());
               sPrev.setInputClosedSpeakerChange();
               Conversation.printWSln("Main", "...Trying to close (2).."+sPrev.getType());
	       synchronized(sPrev){
	           while (!sPrev.isInputAndOutputFinished()){
			try{
			   Conversation.printWSln("Main", "Trying to (3)..close "+sPrev.getType());
         		   wait(50);
			   sPrev.setInputClosedSpeakerChange();
                        }
			catch(InterruptedException e){
			   Conversation.saveErr(e);

		        }
                   }
		}
		}
                Conversation.printWSln("Main", "Managed to close "+sPrev.getType()+"...so now adding" +s.getType());
	        sequences.add(s);
                try{
		   this.dcLastIncoming = (DocChange) s.getAllDocChangesFromSender().lastElement();
                }catch (Exception e){dcLastIncoming = null;};
                notifyAll();
	}






	public synchronized void addDocChange(MessageCBYCDocChangeFromClient mCDC) {
		
		DocChange dc = mCDC.getDocChange();

		Sequence seqToPop = null;

		Conversation.printWSln("ADDINGDOCCHANGE:", dc.elementString);
		if (dcLastIncoming == null) {
			Sequence seq = (Sequence) this.sequences.lastElement();
			seqToPop = seq.addDocChange(dc);
		} else if (!dcLastIncoming.sender.equalsIgnoreCase(dc.sender)) {
			Sequence current = (Sequence) sequences.lastElement();
			if (current != null && !current.getSender().equals(dc.getSender())) {
				Conversation.printWSln("Sequences",
						"POSSIBLE INTERLEAVING ERROR....SHOULDN'T HAPPEN..."
								+ dc.getSender());
				// return;
			}
			Sequence seq = (Sequence) this.sequences.lastElement();
			// Conversation.printWSln("CREATING",
			// "DIFFERENT SPEAKER...ADDING "+dc.getClass().toString());
			seqToPop = seq.addDocChange(dc);
		} else if (dc.elementStart != dcLastIncoming.elementStart) {
			// Can be due to participant typing a new line,
			// or due to participant having moved cursor to another turn
			// or something more complex if there's an intervention that came
			// beforehand
			// and interfered with it..

			Sequence seq = (Sequence) this.sequences.lastElement();
			seq.setInputClosedEditOfOwnTurn();
			// Conversation.printWSln("CREATING",
			// "ABOUT TO MAKE NEW EDITSEQUENCE SAME SPEAKER "+dc.getClass().toString());
			// Conversation.printWSln("CREATING",
			// "DCelementStart: "+dc.elementStart+"..DcLastIncoming: "+dcLastIncoming.elementStart);
			// Conversation.printWSln("CREATING", );
			// Sequence seq2 = new
			// EditSequence(this,cC,dc.getSender(),mCDC.getTimeStamp(),dc.elementString,dc.elementStart,dc.elementFinish);

			Sequence seq2;
			if (dc.offs == 0) {
				seq2 = cC.getNextSequence_NewLine_By_Same_Speaker(seq,
						sequences.size(), this, dc.getSender(), mCDC);

			} else {
				seq2 = cC.getNextSequence_Edit_By_Same_Speaker(seq, sequences
						.size(), this, dc.getSender(), mCDC);

			}
			if (seq2==null) return;
			
			Conversation.printWSln("CREATING", "3a"
					+ dc.positionWithinRowFromRight);
			Conversation.printWSln("CREATING", "3b" + dc.elementStart);
			Conversation.printWSln("CREATING", "3c" + dc.elementFinish);
			Conversation.printWSln("CREATING", "3d" + dc.offs);
			Conversation.printWSln("CREATING", "3e" + dc.rowNumber);
			Conversation.printWSln("CREATING",
					"------------------------------------------------");

			if (seq != seq2)
				sequences.addElement(seq2);
			seqToPop = seq2.addDocChange(dc);
		} else {
			Sequence seq = (Sequence) this.sequences.lastElement();
			seqToPop = seq.addDocChange(dc);
		}
		dcLastIncoming = dc;
		if (seqToPop != null)
			sequences.addElement(seqToPop);
		notifyAll();
		// Verify if it is editing the same element.
	}

	public synchronized DocChange getLastIncoming() {
		return this.dcLastIncoming;
	}

	public synchronized Sequence getCurrentSequence() {
		if (sequences.size() > 0)
			return (Sequence) sequences.lastElement();
		else
			return null;

	}

	public String getStatusWindowTextForRecipient(String recipient) {
		if (sequences.size() == 0)
			return "initializing";
		Sequence seq = (Sequence) sequences.lastElement();
		return seq.getStatusWindowTextForRecipient(recipient);
	}

	int currentOut = 0;
	public boolean conversationIsActive = true;

	public synchronized Sequence getNextSequenceForOutput() {
		while (currentOut + 1 > sequences.size()) {
			try {
				System.err.println("WAKINGS0");
				wait();
				System.err.println("WAKINGS1");
			} catch (Exception e) {

			}
		}
		currentOut++;
		return (Sequence) this.sequences.elementAt(currentOut - 1);
	}

	public void run() {

		while (conversationIsActive) {
			try {
				System.err.println("WAKINGSA");
				Sequence s = getNextSequenceForOutput();
				Conversation.printWSln("sequences", "Sequence Class:"
						+ s.getClass().getName());
				System.err.println("WAKINGSB " + s.getClass().toString());
				
				s.main();
				
				System.err.println("WAKINGSC");
				cC.getC().getHistory().saveCBYCSequence(s);
				/*
				synchronized (this) {
					if (s2 != null && currentOut + 1 >= sequences.size()) {
						sequences.addElement(s2);

					} else {

					}
				}
				*/

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}

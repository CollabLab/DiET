package diet.server.CbyC.Sequence;

import java.util.Date;
import java.util.Map;
import java.util.Vector;

import diet.message.MessageCBYCTypingUnhinderedRequest;
import diet.server.Conversation;
import diet.server.CbyC.Sequences;
import diet.server.ConversationController.CCCBYCDefaultController;
import diet.utils.Dictionary;
import diet.utils.POSTagRegex;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;

public class POSTagFilterSequenceNonTriggering extends POSTagFilterSequence {

	private String curFrag = null;

	public POSTagFilterSequenceNonTriggering(Sequences sS,
			CCCBYCDefaultController cC, String sender, Date timeStamp,
			String elementString, int elementStart, int elementFinish,
			MaxentTagger tagger, Vector<POSTagRegex> regexes, Dictionary d,
			Map<String, String> misspellings) {
		super(sS, cC, sender, timeStamp, elementString, elementStart,
				elementFinish, tagger, regexes, d, misspellings);
		// TODO Auto-generated constructor stub
	}

	public POSTagFilterSequenceNonTriggering(Sequences sS,
			CCCBYCDefaultController cC, String sender,
			MessageCBYCTypingUnhinderedRequest mCTUR, MaxentTagger tagger,
			Vector<POSTagRegex> r, Dictionary d,
			Map<String, String> misspellings) {
		super(sS, cC, sender, mCTUR, tagger, r, d, misspellings);
		// TODO Auto-generated constructor stub
	}

	public POSTagFilterSequenceNonTriggering(Sequences sS,
			CCCBYCDefaultController cC, String sender, Date timeStamp,
			String elementString, int elementStart, int elementFinish,
			MaxentTagger tagger, Vector<POSTagRegex> regexes, Dictionary dict,
			Map<String, String> misspellings, String condition) {
		super(sS, cC, sender, timeStamp, elementString, elementStart,
				elementFinish, tagger, regexes, dict, misspellings, condition);

	}

	public String getCurrentFrag() {
		return curFrag;
	}

	private boolean seenEndOfSent = false;

	public void run() {

		while (!this.isInputFinished()) {
			synchronized (this) {
				try {

					long beforeWait = new Date().getTime();
					wait(this.waitingTimeBeforeInterventionCheckPoint);
					Conversation.printWSln("POSTagFilterSeq",
							"Current Frag is:" + this.curFrag);
					if (this.turnSoFar == null || this.turnSoFar.equals("")) {
						continue;
					}
					if (this.isInputFinished())
						break;

					long afterWait = new Date().getTime();
					if (afterWait - beforeWait >= this.waitingTimeBeforeInterventionCheckPoint
							|| turnSoFar.endsWith(" ")) {

						if (this.curFrag != null && fragRepeated())
						{
							curFrag = null;
							wait();
						}

						String frag = this.getFragForIntervention();
						if (frag != null) {
							
							this.curFrag = frag;
							
						}

					}
				}

				/*
				 * uncomment if you want intervention to cancel at end of
				 * sentence. . . } else if
				 * (turnSoFar.endsWith(".")||turnSoFar.endsWith("?")
				 * ||turnSoFar.endsWith("!")) { seenEndOfSent=true; } else if
				 * (seenEndOfSent&&turnSoFar.substring(turnSoFar.length()-1,
				 * turnSoFar.length()).matches("([a-z]|[A-Z])")) {
				 * this.curFrag=null; seenEndOfSent=false; }
				 */

				catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

	}

	// assuming lastChar in turn so far is " ".
	private boolean fragRepeated() {
		Conversation.printWSln("POSTagFilterSeq", "Checking for repetition.");
		if (this.curFrag == null || turnSoFar == null || turnSoFar.isEmpty())
			return false;

		Conversation.printWSln("POSTagFilterSeq", "Frag is:" + this.curFrag
				+ " and the turn is:" + this.turnSoFar);
		String[] fragWords = curFrag.split(" ");
		String[] turnWords = this.turnSoFar.split(" ");
		if (fragWords.length == 0 || turnWords.length == 0)
			return false;

		String lastFragWord = fragWords[fragWords.length - 1];
		String lastTurnWord = turnWords[turnWords.length - 1];
		Conversation.printWSln("POSTagFilterSeq", "Last turn word is:"
				+ lastTurnWord + " and last frag word is:" + lastFragWord);
		if (lastTurnWord.equalsIgnoreCase(lastFragWord)) {
			return true;

		} else {
			String[] fixedSpellingWords = this.fixSpelling().split(" ");
			String last = fixedSpellingWords[fixedSpellingWords.length - 1];
			if (last.equalsIgnoreCase(lastFragWord))
				return true;
		}

		return false;

	}

}

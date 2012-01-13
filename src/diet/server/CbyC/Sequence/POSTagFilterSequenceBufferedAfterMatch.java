package diet.server.CbyC.Sequence;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;

import diet.message.MessageCBYCTypingUnhinderedRequest;
import diet.server.Conversation;
import diet.server.Participant;
import diet.server.CbyC.DocChange;
import diet.server.CbyC.DocInsert;
import diet.server.CbyC.Sequences;
import diet.server.ConversationController.CCCBYCAbstractProbeCR;
import diet.server.ConversationController.CCCBYCDefaultController;
import diet.server.ConversationController.CCCBYCElicitSplitUtt;
import diet.utils.Dictionary;
import diet.utils.POSTagRegex;
import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.ling.TaggedWord;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;

public class POSTagFilterSequenceBufferedAfterMatch extends
	POSTagFilterSequence {

    Map<String, String> tagGroupsForIntervention;

    // intervention is triggered when the last incoming tag is one of these.

    public POSTagFilterSequenceBufferedAfterMatch(Sequences sS,
	    CCCBYCAbstractProbeCR cC, String sender, Date timeStamp,
	    String elementString, int elementStart, int elementFinish,
	    MaxentTagger tagger, Vector<POSTagRegex> regexes, Dictionary d,
	    Map<String, String> misspellings,
	    Map<String, String> tagGroupsIntervention) {
	super(sS, cC, sender, timeStamp, elementString, elementStart,
		elementFinish, tagger, regexes, d, misspellings);
	this.tagGroupsForIntervention = tagGroupsIntervention;

    }

    public POSTagFilterSequenceBufferedAfterMatch(Sequences sS,
	    CCCBYCDefaultController cC, String sender,
	    MessageCBYCTypingUnhinderedRequest mCTUR, MaxentTagger tagger,
	    Vector<POSTagRegex> r, Dictionary d,
	    Map<String, String> misspellings,
	    Map<String, String> tagGroupsIntervention) {
	super(sS, cC, sender, mCTUR, tagger, r, d, misspellings);
	this.tagGroupsForIntervention = tagGroupsIntervention;

    }

    public POSTagFilterSequenceBufferedAfterMatch(Sequences sS,
	    CCCBYCDefaultController cC, String sender,
	    MessageCBYCTypingUnhinderedRequest mCTUR, MaxentTagger tagger,
	    Vector<POSTagRegex> r, Dictionary d,
	    Map<String, String> misspellings, String condition,
	    Map<String, String> tagGroupsIntervention) {
	super(sS, cC, sender, mCTUR, tagger, r, d, misspellings, condition);
	this.tagGroupsForIntervention = tagGroupsIntervention;
    }

    public POSTagFilterSequenceBufferedAfterMatch(Sequences sS,
	    CCCBYCDefaultController cC, String sender, Date timeStamp,
	    String elementString, int elementStart, int elementFinish,
	    MaxentTagger tagger, Vector<POSTagRegex> regexes, Dictionary dict,
	    Map<String, String> misspellings, String condition,
	    Map<String, String> tagGroupsIntervention) {
	super(sS, cC, sender, timeStamp, elementString, elementStart,
		elementFinish, tagger, regexes, dict, misspellings, condition);
	this.tagGroupsForIntervention = tagGroupsIntervention;
    }

    public POSTagFilterSequenceBufferedAfterMatch(Sequences sS,
	    CCCBYCDefaultController cC, String sender, Date timeStamp,
	    String elementString, int elementStart, int elementFinish,
	    MaxentTagger tagger, Vector<POSTagRegex> regexes, Dictionary dict,
	    Map<String, String> misspellings,
	    Map<String, String> tagGroupsIntervention) {
	super(sS, cC, sender, timeStamp, elementString, elementStart,
		elementFinish, tagger, regexes, dict, misspellings);
	this.tagGroupsForIntervention = tagGroupsIntervention;
    }

    public POSTagFilterSequenceBufferedAfterMatch(Sequences sS,
	    CCCBYCDefaultController cC, String sender, Date timeStamp,
	    String elementString, int elementStart, int elementFinish,
	    MaxentTagger tagger, Vector<POSTagRegex> regexes, Dictionary dict,
	    TreeMap<String, String> misspellings, String condition,
	    Map<String, String> tagGroupsIntervention) {
	super(sS, cC, sender, new Date(), elementString, elementStart,
		elementFinish, tagger, regexes, dict, misspellings, condition);
	this.tagGroupsForIntervention = tagGroupsIntervention;
    }

    public synchronized Sequence addDocChange(DocChange dc) {
	lastDocChangeTimeStamp = new Date().getTime();
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

	if (dc instanceof DocInsert) {
	    DocInsert di = (DocInsert) dc;
	    String insert = di.getStr();
	    // if
	    // (insert.equals(" ")||insert.equals("!")||insert.equals("?")||insert.e)
	    this.turnSoFar += insert;
	    notify();
	} else
	    Conversation
		    .printWSln("ERROR",
			    "DocRemove in POSTagFilterSequence. Edit and deletes should be disabled.");

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

    public void run() {
	while (!this.isInputFinished()) {

	    long beforeWait = new Date().getTime();
	    synchronized (this) {
		try {
		    wait(this.waitingTimeBeforeInterventionCheckPoint);
		} catch (InterruptedException e) {
		    e.printStackTrace();
		}
	    }
	    if (this.turnSoFar == null || this.turnSoFar.equals("")) {
		continue;
	    }
	    if (this.isInputFinished())
		break;

	    long afterWait = new Date().getTime();
	    if (afterWait - beforeWait >= this.waitingTimeBeforeInterventionCheckPoint
		    || turnSoFar.endsWith(" ")) {
		String lastTag = this.lastTagMatched();
		if (lastTag != null) {

		    if (this.cC instanceof CCCBYCElicitSplitUtt) {
			CCCBYCElicitSplitUtt cont = (CCCBYCElicitSplitUtt) this.cC;
			int turnLengthSoFar = getFinalText().split("\\s+").length;
			if (turnLengthSoFar < cont.minimumTurnLengthBeforeIntervention
				.getValue()) {
			    Conversation.printWSln("POSTagFilterSequence",
				    "Turn too short");
			    continue;
			}
			if (cont.attemptIntervention(this.sender, lastTag)) {
			    this.condition = lastTag
				    + "="
				    + this.tagGroupsForIntervention
					    .get(lastTag);
			    this.setInputClosed();
			    break;
			} else {

			}

		    } else {
			Conversation
				.printWSln("ERROR",
					"Wrong conversation controller class. Should be CCCBYCElicitSplitUtt");
		    }
		}

	    }

	}
    }

    protected String lastTagMatched() {

	String turnSpellingCorrected = this.fixSpelling();
	Conversation.printWSln("POSTagFilterSeq",
		"Tagging Text (after spell check):\n" + turnSpellingCorrected);
	List<ArrayList<? extends HasWord>> sentences = MaxentTagger
		.tokenizeText(new StringReader(turnSpellingCorrected));
	if (sentences.isEmpty())
	    return null;
	ArrayList<? extends HasWord> lastArrayList = sentences.get(sentences
		.size() - 1);
	ArrayList<TaggedWord> lastTaggedArrayList = tagger
		.tagSentence(lastArrayList);
	TaggedWord last = lastTaggedArrayList.get(lastTaggedArrayList.size() - 1);

	if (this.tagGroupsForIntervention.containsKey(last.tag())) {

	    return last.tag();

	} else
	    return null;

    }

}
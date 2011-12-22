package diet.server.ConversationController;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;

import diet.client.WYSIWYGDocumentWithEnforcedTurntaking;
import diet.message.MessageCBYCChangeTurntakingStatusConfirm;
import diet.message.MessageCBYCDocChangeFromClient;
import diet.message.MessageCBYCTypingUnhinderedRequest;
import diet.message.MessageKeypressed;
import diet.parameters.ExperimentSettings;
import diet.parameters.IntParameter;
import diet.parameters.Parameter;
import diet.parameters.StringParameter;
import diet.server.Conversation;
import diet.server.Participant;
import diet.server.CbyC.DocChange;
import diet.server.CbyC.Sequences;
import diet.server.CbyC.Sequence.BufferedSequenceWithSpoofTypingResponsivity;
import diet.server.CbyC.Sequence.EditSequence;
import diet.server.CbyC.Sequence.LimitedRecipientsSequenceWithSpoofTypingResponsivity;
import diet.server.CbyC.Sequence.POSTagFilterSequence;
import diet.server.CbyC.Sequence.POSTagFilterSequenceBufferedAfterMatch;
import diet.server.CbyC.Sequence.RecordedSequenceFromApparentOrigin;
import diet.server.CbyC.Sequence.Sequence;
import diet.utils.Dictionary;
import diet.utils.POSTagRegex;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;

public class CCCBYCElicitSplitUtt extends CCCBYCDefaultController implements
	UseOfPrerecordedFakeTurn {

    private boolean interventionTriggered = false;

    IntParameter minimumSpeakerChangesBetweenInterventions;
    public IntParameter minimumTurnLengthBeforeIntervention;
    IntParameter timeToWaitForResponse = new IntParameter("Response Time Out",
	    12);
    IntParameter typingTimeOutDeterminesEndOfTurn = new IntParameter(
	    "Typing time out: Determines end of turn during intervention", 3);
    private int speakerChangesSinceLastIntervention;
    String[] fillerConditions = { "filler", "nonFiller" };
    String[] fillers;
    // { "err ", "errrm ", "errr ", "errm ", "er ",
    // "erm ", ". . . ", "uhh ", "uuh ", "umm " };
    Map<String, Vector<POSTagRegex>> regexes = new HashMap<String, Vector<POSTagRegex>>();
    File withinConstRegexFile = new File(System.getProperty("user.dir")
	    + File.separator + "fragmentFilters" + File.separator
	    + "withinConstAfterSubjectAndVerbRegexes.txt");
    File constBoundaryRegexFile = new File(System.getProperty("user.dir")
	    + File.separator + "fragmentFilters" + File.separator
	    + "constBoundaryRegexes.txt");
    File misspellingsFile = new File(System.getProperty("user.dir")
	    + File.separator + "fragmentFilters" + File.separator
	    + "misspellings.txt");
    String taggerFileName = System.getProperty("user.dir") + File.separator
	    + "utils" + File.separator
	    + "bidirectional-distsim-wsj-0-18.tagger";
    File tagsForInterventionFile = new File(System.getProperty("user.dir")
	    + File.separator + "fragmentFilters" + File.separator
	    + "tagsForIntervention.txt");
    Parameter fillerCondition;
    MaxentTagger tagger;
    Dictionary dict;
    String antecedentOwnerUserName = null;
    Participant antecedentOwner = null;
    String curFillerCondition;
    // this will contain the number of interventions in each tag group
    // however it will not contain any numbers for the groups which
    // are always good (low frequency ones)
    Map<String, Integer> tagGroupCount = new HashMap<String, Integer>();

    HashMap<String, String> tagGroups;

    public void initialize(Conversation c, ExperimentSettings expS) {
	super.initialize(c, expS);
	super.setIsTypingTimeOut(200);
	super.setProcessLoopSleepTime(80);
	this.fillerCondition = this.expSettings
		.getParameter("Filler Condition");
	this.minimumSpeakerChangesBetweenInterventions = (IntParameter) this.expSettings
		.getParameter("Speaker Changes Between Interventions");
	this.expSettings.addParameter(this.timeToWaitForResponse);
	this.expSettings.addParameter(this.typingTimeOutDeterminesEndOfTurn);
	this.minimumTurnLengthBeforeIntervention = (IntParameter) this.expSettings
		.getParameter("Minimum Words Before Intervention");
	this.expSettings.addParameter(this.sendingNetErrors);
	this.minMaxTagDiff = (IntParameter) this.expSettings
		.getParameter("Maximum Min Max Difference (tags)");
	this.minMaxUserDiff = (IntParameter) this.expSettings
	.getParameter("Maximum Min Max Difference (users)");
	
	this.speakerChangesSinceLastIntervention = 0;
	StringParameter fillerString = (StringParameter) this.expSettings
		.getParameter("Fillers");

	String[] fill = fillerString.getValue().split(",");
	fillers = new String[fill.length];
	Conversation.printWSln("Main", "The fillers are:");
	for (int i = 0; i < fill.length; i++) {
	    fillers[i] = fill[i].trim();
	    fillers[i] += " ";
	    Conversation.printWSln("Main", fillers[i]);
	}

	if (((String) this.fillerCondition.getValue())
		.equalsIgnoreCase("random"))
	    this.curFillerCondition = this.fillerConditions[r
		    .nextInt(this.fillerConditions.length)];
	else
	    this.curFillerCondition = (String) this.fillerCondition.getValue();

	Conversation
		.printWSln("Main", "Initialising Stanford POS-Tagger . . .");
	try {

	    this.tagger = new MaxentTagger(this.taggerFileName);
	    Conversation
		    .printWSln("Main", "Initialisation ended successfully.");
	    Conversation.printWSln("Main", "Loading Regexes");
	    this.loadRegexes();
	    Conversation.printWSln("Main", "Loaded Regexes");
	    Conversation.printWSln("Main", "Loading Dictionary");
	    this.dict = new Dictionary(System.getProperty("user.dir")
		    + File.separator + "utils" + File.separator
		    + "fulldictionary.txt");
	    Conversation.printWSln("Main", "Loaded Dictionary");
	    Conversation.printWSln("Main", "Loading Misspellings Map");
	    this.loadMisspellings();
	    Conversation.printWSln("Main", "Loaded Misspellings Map");
	    this.loadAlwaysGoodTagGroups();
	    Conversation.printWSln("Main", "Loading Tags for Intervention");
	    this.loadTagsForIntervention();
	    Conversation.printWSln("Main", "Loaded Tags for Intervention");

	} catch (Exception e) {
	    e.printStackTrace();

	}
    }
    File alwaysGoodTagGroupsFile = new File(System.getProperty("user.dir")
	    + File.separator + "fragmentFilters" + File.separator
	    + "alwaysGoodTagGroups.txt");
    private void loadAlwaysGoodTagGroups() throws IOException{
	this.alwaysGoodTagGroups=new ArrayList<String>();
	BufferedReader in = new BufferedReader(new FileReader(
		this.alwaysGoodTagGroupsFile));
	String line;
	while ((line = in.readLine()) != null) {
	    if (line.isEmpty())
		continue;
	   
	    this.alwaysGoodTagGroups.add(line.trim());
	    
	}
	
	
    }

    private void loadTagsForIntervention() throws IOException {
	this.tagGroups = new HashMap<String, String>();

	BufferedReader in = new BufferedReader(new FileReader(
		this.tagsForInterventionFile));
	String line;
	while ((line = in.readLine()) != null) {
	    if (line.isEmpty())
		continue;

	    String[] values = line.split(":");
	    String[] tagsInGroup = values[1].trim().split("\\s+");
	    for (String tag : tagsInGroup) {
		this.tagGroups.put(tag.trim(), values[0]);
	    }
	    if (!alwaysGoodTagGroups.contains(values[0].trim())) {
		this.tagGroupCount.put(values[0].trim(), 0);
	    }
	}
	
    }

    TreeMap<String, String> misspellings = new TreeMap<String, String>();

    private synchronized void loadMisspellings() throws IOException {
	misspellings = new TreeMap<String, String>();

	BufferedReader in = new BufferedReader(new FileReader(
		this.misspellingsFile));
	String line;
	while ((line = in.readLine()) != null) {
	    String[] halves = getHalves(line);
	    misspellings.put(halves[0], halves[1]);
	}

    }

    private String[] getHalves(String whole) {
	String[] result = new String[2];
	for (int i = 0; i < whole.length(); i++) {
	    if (whole.substring(i, i + 1).matches("\\s+")) {
		result[0] = whole.substring(0, i);
		result[1] = whole.substring(i + 1, whole.length());
		return result;
	    }
	}
	return null;
    }

    private void loadRegexes() throws Exception {
	BufferedReader reader = new BufferedReader(new FileReader(
		this.withinConstRegexFile));
	Vector<POSTagRegex> withinConst = new Vector<POSTagRegex>();
	String regex;
	while ((regex = reader.readLine()) != null) {
	    if (regex.startsWith("%"))
		continue;
	    String[] split = regex.split(" ");
	    withinConst.add(new POSTagRegex(split[2], split[0], split[1]));
	}
	reader.close();
	regexes.put("withinConst", withinConst);
	Vector<POSTagRegex> constBoundary = new Vector<POSTagRegex>();
	reader = new BufferedReader(new FileReader(this.constBoundaryRegexFile));
	while ((regex = reader.readLine()) != null) {
	    if (regex.startsWith("%"))
		continue;
	    String[] split = regex.split(" ");
	    constBoundary.add(new POSTagRegex(split[2], split[0], split[1]));
	}
	reader.close();
	regexes.put("constBoundary", constBoundary);

    }
    public void participantJoinedConversation(Participant p){
	this.interPerUser.put(p.getUsername(), 0);
    }

    // _______________________________________________________________

    long interventionTime = -1;
    BufferedSequenceWithSpoofTypingResponsivity secondHalfSeq;
    boolean waitingToTriggerIntervention = false;
    String lastTag="";
    HashMap<String, Integer> interPerUser=new HashMap<String, Integer>();
    public boolean attemptIntervention(String turnOwner, String lastTag) {
	Conversation.printWSln("CCCbyCElicitSplitUtt",
		"Intervention Triggered.");
	int turnLengthSoFar = sS.getCurrentSequence().getFinalText().split(
		"\\s+").length;
	Conversation.printWSln("CCCbyCElicitSplitUtt", "Turn Length So Far:"
		+ turnLengthSoFar);
	Conversation.printWSln("CCCbyCElicitSplitUtt", "The Turn:"
		+ sS.getCurrentSequence().getFinalText());

	if (this.speakerChangesSinceLastIntervention < this.minimumSpeakerChangesBetweenInterventions
		.getValue()) {
	    Conversation
		    .printWSln("CCCbyCElicitSplitUtt",
			    "Intervention aborted: Not enough turns since the previous.");
	    return false;
	} else if (this.waitingOnNetworkError) {
	    Conversation.printWSln("CCCbyCElicitSplitUtt",
		    "Intervention aborted: Net Error in progress");
	    return false;
	} else if (turnLengthSoFar < this.minimumTurnLengthBeforeIntervention
		.getValue()) {
	    Conversation.printWSln("CCCbyCElicitSplitUtt",
		    "Intervention aborted: Turn too short");
	    return false;

	} else if (this.disturbsTagBalance(lastTag)) {

	    Conversation.printWSln("CCCbyCElicitSplitUtt",
		    "Intervention aborted: would unbalance tag numbers. Tag was "
			    + lastTag + " in group "
			    + this.tagGroups.get(lastTag));
	    return false;

	} else if (this.disturbsTurnOwnerBalance(turnOwner)) {

	    Conversation.printWSln("CCCbyCElicitSplitUtt",
		    "Intervention aborted: would unbalance participant numbers. Participant was "
			    + turnOwner);
	    return false;

	}

	
	
	interPerUser.put(turnOwner, interPerUser.get(turnOwner)+1);
	
	String lastTagGroup = tagGroups.get(lastTag);
	this.lastTag=lastTag;
	Sequence lastSeq=sS.getCurrentSequence();
	if (lastSeq instanceof POSTagFilterSequence)
	    ((POSTagFilterSequence)lastSeq).setCondition(lastTag+"="+lastTagGroup);
	
	if (!alwaysGoodTagGroups.contains(lastTagGroup)) {
	    this.tagGroupCount.put(lastTagGroup, this.tagGroupCount
		    .get(lastTagGroup) + 1);
	}
	this.secondHalfSeq = new BufferedSequenceWithSpoofTypingResponsivity(
		sS, this, turnOwner, this.lastTag+"="+this.tagGroups.get(lastTag));
	this.speakerChangesSinceLastIntervention = 0;
	this.antecedentOwnerUserName = turnOwner;
	this.antecedentOwner = getC().getParticipants()
		.findParticipantWithUsername(turnOwner);
	interventionTriggered = true;
	// will try to handle concurrent modification and inconsistent states
	// manualy.
	// letting java do it leads to thread deadlock
	
	if (this.processLoopRunning) {
	    waitingToTriggerIntervention = true;
	    synchronized (this) {
		try {
		    wait();
		} catch (InterruptedException e) {
		    e.printStackTrace();
		}
	    }
	    waitingToTriggerIntervention = false;
	}

	fh.setAutomaticallyAllowOpenFloorAfterIsTypingTimeout(false);
	Conversation
		.printWSln("InterV Balance Check", "Successfully triggered");

	this.responder = (Participant) getC().getParticipants()
		.getAllOtherParticipants(antecedentOwner).elementAt(0);

	// fh.changeFloorStatusOfParticipantNoPrefix(getC().getParticipants().findParticipantWithUsername(turnOwner),
	// WYSIWYGDocumentWithEnforcedTurntaking.nooneelsetyping);
	if (((String) this.fillerCondition.getValue())
		.equalsIgnoreCase("random"))
	    this.curFillerCondition = this.fillerConditions[r
		    .nextInt(this.fillerConditions.length)];
	else
	    this.curFillerCondition = (String) this.fillerCondition.getValue();

	Conversation.printWSln("CCCBYCElicitSplitUtt", "Filler condition is: "
		+ this.curFillerCondition);
	if (this.curFillerCondition.equalsIgnoreCase("filler")) {

	    Vector recip = new Vector();
	    recip.add(responder);
	    String filler;
	    String turnSoFar = ((POSTagFilterSequence) sS.getCurrentSequence())
		    .getTurnSofar();
	    if (turnSoFar.endsWith(" "))
		filler = this.fillers[r.nextInt(this.fillers.length)];
	    else
		filler = " " + this.fillers[r.nextInt(this.fillers.length)];
	    RecordedSequenceFromApparentOrigin rs = new RecordedSequenceFromApparentOrigin(
		    sS, this, filler, 8, recip, antecedentOwner,
		    this.lastTag+"="+this.tagGroups.get(lastTag));
	    try {

		Thread.sleep(1000);

		sS.addSequence(rs);
		synchronized (this) {
		    wait();
		}

	    } catch (Exception e) {
		e.printStackTrace();
	    }
	}
	fh.changeFloorStatusOfParticipantNoPrefix(responder,
		WYSIWYGDocumentWithEnforcedTurntaking.nooneelsetyping);
	getC().sendAndEnableLabelDisplayToALLStatusWindowsOfParticipant(
		responder, "Please type", false, true);
	interventionTime = new Date().getTime();
	return true;
    }

    boolean antecedentOwnerFinishedTyping = false;
    private Participant responder;
    private boolean responseGiven = false;
    private boolean sendingRest = false;

    private IntParameter sendingNetErrors = new IntParameter(
	    "Send Rand Net Error", 1);
    private int i = 0;
    boolean processLoopRunning = false;
    
    public void processLoop() {
	processLoopRunning = true;
	if (sendingNetErrors.getValue() == 1)
	    this.sendRandNetworkError();
	if (!interventionTriggered) {

	    Conversation.printWSln("ProcessLoop",
		    "ProcessLoop: Intervention Not Triggered." + i);
	    i++;
	    if (i == 10)
		i = 0;
	    fh.openFloorAfterTimeOut(super.getIsTypingTimeOut());
	    Conversation.printWSln("ProcessLoop", "Open Floor returned");
	} else {
	    Conversation.printWSln("ProcessLoop",
		    "ProcessLoop: Intervention Triggered." + i);
	    i++;
	    if (i == 10)
		i = 0;

	    if (waitingToTriggerIntervention) {
		processLoopRunning = false;
		synchronized (this) {
		    notify();
		}
		return;
	    }
	    if (!antecedentOwnerFinishedTyping
		    && !this.antecedentOwner
			    .isTyping(1000 * this.typingTimeOutDeterminesEndOfTurn
				    .getValue())) {

		antecedentOwnerFinishedTyping = true;
		Conversation
			.printWSln("CCCBYCElicitSplitUtt",
				"Antecendent owner finished typing. Now blocking him with network error");

		this.secondHalfSeq.stopRecordingDelaysAndSpoofResponsivity();
		getC()
			.sendAndEnableLabelDisplayToALLStatusWindowsOfParticipant(
				antecedentOwner, "Network Error. Please wait.",
				true, false);

		// this.secondHalfSeq.setInputClosed();

	    }
	    if (this.receivingResponse) {
		if (!sendingRest
			&& !responder.isTyping(typingTimeOutDeterminesEndOfTurn
				.getValue() * 1000)) {
		    fh.changeFloorStatusOfParticipantNoPrefix(responder,
			    WYSIWYGDocumentWithEnforcedTurntaking.othertyping);
		    // fh.informOthersOfTyping(antecedentOwner);
		    getC()
			    .sendAndEnableLabelDisplayToALLStatusWindowsOfParticipant(
				    responder,
				    antecedentOwnerUserName + "...is typing",
				    true, true);
		    Conversation.printWSln("CCCbyCElicitSplitUtt",
			    "Responder finished response");
		    Conversation.printWSln("CCCbyCElicitSplitUtt",
			    "Sending rest of turn.");
		    this.secondHalfSeq.createHeader();
		    sS.addSequence(this.secondHalfSeq);
		    this.receivingResponse = false;
		    responseGiven = true;
		    sendingRest = true;

		}

	    } else if (this.interventionTime > 0
		    && !responseGiven
		    && !sendingRest
		    && new Date().getTime() - this.interventionTime > this.timeToWaitForResponse
			    .getValue() * 1000) {
		fh.changeFloorStatusOfParticipantNoPrefix(responder,
			WYSIWYGDocumentWithEnforcedTurntaking.othertyping);
		fh.forceInformOthersOfTyping(antecedentOwner);
		// this.secondHalfSeq.createHeader();
		Conversation.printWSln("CCCbyCElicitSplitUtt",
			"waited long enough. No response given.");
		Conversation.printWSln("CCCbyCElicitSplitUtt",
			"Sending rest of turn.");
		sendingRest = true;
		sS.addSequence(this.secondHalfSeq);

	    }
	}

	processLoopRunning = false;

	if (waitingToTriggerIntervention)
	    synchronized (this) {
		notify();
	    }

    }

    public boolean isProcessLoopRunning() {
	return this.processLoopRunning;
    }

    public void processCBYCTypingUnhinderedRequest(Participant sender,
	    MessageCBYCTypingUnhinderedRequest mWTUR) {
	if (c.getParticipants().getAllParticipants().size() != 2
		|| this.waitingOnNetworkError)
	    return;
	Conversation.printWSln("FloorHolder",
		"Received and processing floor request from: "
			+ sender.getUsername());
	// Conversation.printWSln("FloorHolder",
	// "Element: "+mWTUR.getElementString()+"|ES:"+mWTUR.getElementStart()+"|EF:"+mWTUR.getElementFinish()+"|Text:"+mWTUR.getText()+"|OffsetFrmR:"+mWTUR.getOffsetFrmRight());
	Conversation.printWSln("KeyPress", "FloorRequest");
	if (!interventionTriggered) {
	    Conversation.printWSln("FloorHolder",
		    "Passing request to floorholder " + sender.getUsername());

	    fh.processFloorRequest(sender, mWTUR);
	} else {
	    if (sender.equals(antecedentOwner)) {
		fh.changeFloorStatusOfParticipantNoPrefix(sender,
			WYSIWYGDocumentWithEnforcedTurntaking.typingunhindered);
		Conversation
			.printWSln(
				"FloorHolder",
				"sender is antecedentOwner. Not passing to floor holder. Just giving him typing unhindered "
					+ sender.getUsername());
	    } else {
		Conversation
			.printWSln(
				"FloorHolder",
				"sender is responder. Not passing to floor holder. Just giving him typing unhindered "
					+ sender.getUsername());
		receivingResponse = true;
		fh.changeFloorStatusOfParticipantPrefix(sender,
			WYSIWYGDocumentWithEnforcedTurntaking.typingunhindered);
		if (!sS.processSpeakerChangeRequest_SetNewSequence(sender,
			mWTUR))
		    Conversation
			    .printWSln("ERROR",
				    "process speaker change returned false. This shouldn't happen");

	    }

	}
    }

    public void processCBYCChangeTurnTakingStatusConfirm(Participant sender,
	    MessageCBYCChangeTurntakingStatusConfirm mConf) {
	fh.processStateChangeConfirm(sender, mConf);
    }

    public void processCBYCDocChange(Participant sender,
	    MessageCBYCDocChangeFromClient mCDC) {
	if (c.getParticipants().getAllParticipants().size() != 2
		|| this.waitingOnNetworkError)
	    return;

	if (!interventionTriggered) {
	    Conversation.printWSln("DocChange",
		    "DocChage received. Handling normally"
			    + sender.getUsername());
	    sS.addDocChange(mCDC);

	} else {
	    if (sender.equals(this.antecedentOwner)) {
		Conversation.printWSln("DocChange",
			"DocChage received from sender. Intervention active. Adding to secondHalfSeq."
				+ sender.getUsername());
		this.secondHalfSeq.addDocChange(mCDC.getDocChange());
	    } else {
		Conversation
			.printWSln(
				"DocChange",
				"DocChage received from responder. Intervention Active, still passing to sequences for processing."
					+ sender.getUsername());
		sS.addDocChange(mCDC);
	    }
	}
    }

    public ArrayList<String> alwaysGoodTagGroups;
    private boolean receivingResponse = false;

    // ________________________GET NEXT SEQUENCE METHODS____________

    public Sequence getNextSequence_Speaker_Change(Sequence prior,
	    int sequenceNo, Sequences ss, Participant p,
	    MessageCBYCTypingUnhinderedRequest mCTUR) {
	if (!interventionTriggered) {
	    Conversation.printWSln("getNextSequence",
		    "Speaker Change. Sender is:" + p.getUsername());
	    Conversation.printWSln("getNextSequence",
		    "Returning postagfilterseq");

	    this.speakerChangesSinceLastIntervention++;
	    return new POSTagFilterSequenceBufferedAfterMatch(ss, this, p
		    .getUsername(), mCTUR, tagger, this.regexes
		    .get("withinConst"), this.dict, this.misspellings,
		    this.tagGroups);
	} else {
	    // Should return limited recipient sequence (with dyads there are no
	    // recipients of the others' response
	    Conversation.printWSln("getNextSequence",
		    "Speaker Change. Intervention is active. Sender is:"
			    + p.getUsername());
	    Conversation
		    .printWSln("getNextSequence", "Returning LimitedRecSeq");
	    receivingResponse = true;
	    return new LimitedRecipientsSequenceWithSpoofTypingResponsivity(ss,
		    this, p.getUsername(), new Vector(), p,
		    this.lastTag+"="+this.tagGroups.get(lastTag));
	}
    }

    public Sequence getNextSequence_Edit_By_Different_Speaker(Sequence prior,
	    int sequenceNo, Sequences ss, Participant p,
	    MessageCBYCTypingUnhinderedRequest mCTUR) {
	Conversation.printWSln("ERROR", "No edits allowed. Something is wrong");
	return new EditSequence(sS, this, p.getUsername(), mCTUR);
    }

    public Sequence getNextSequence_NewLine_By_Same_Speaker(Sequence prior,
	    int sequenceNo, Sequences sS, String sender,
	    MessageCBYCDocChangeFromClient mCDC) {
	if (!interventionTriggered) {
	    Conversation.printWSln("getNextSequence", "New Line. Sender is:"
		    + sender);
	    Conversation.printWSln("getNextSequence",
		    "Returning postagfilterseq");
	    DocChange dc = mCDC.getDocChange();
	    return new POSTagFilterSequenceBufferedAfterMatch(sS, this, sender,
		    mCDC.getTimeStamp(), dc.elementString, dc.elementStart,
		    dc.elementFinish, this.tagger, this.regexes
			    .get("withinConst"), this.dict, this.misspellings,
		    this.tagGroups);
	} else {
	    // Should return limited recipient sequence (with dyads there are no
	    // recipients of the others' response
	    Conversation.printWSln("getNextSequence",
		    "New Line. Intervention is active. Sender is:" + sender);
	    Conversation.printWSln("getNextSequence",
		    "Returning limitedRecSeq.");
	    receivingResponse = true;
	    return new LimitedRecipientsSequenceWithSpoofTypingResponsivity(sS,
		    this, sender, new Vector(), getC().getParticipants()
			    .findParticipantWithUsername(sender),
		    this.lastTag+"="+this.tagGroups.get(lastTag));
	}
    }

    public Sequence getNextSequence_Edit_By_Same_Speaker(Sequence prior,
	    int sequenceNo, Sequences sS, String sender,
	    MessageCBYCDocChangeFromClient mCDC) {
	Conversation.printWSln("ERROR", "No edits allowed. Something is wrong");
	DocChange dc = mCDC.getDocChange();
	EditSequence seq2 = new EditSequence(sS, this, sender, mCDC
		.getTimeStamp(), dc.elementString, dc.elementStart,
		dc.elementFinish);
	return seq2;

    }

    public void processKeyPress(Participant sender, MessageKeypressed mkp) {

    }

    public void resetToNormal() {
	Conversation.printWSln("CCCBYCElicitSplitUtt", "resetting to normal");
	this.lastTag="";
	this.interventionTime = -1;
	this.interventionTriggered = false;
	this.antecedentOwner = null;
	this.antecedentOwnerUserName = null;
	this.antecedentOwnerFinishedTyping = false;
	this.responder = null;
	this.receivingResponse = false;
	this.responseGiven = false;
	this.sendingRest = false;
	this.forceOpenFloor();

    }

    // ___________________________________Dealing with random network errors:

    long networkErrorInterval = 120000;
    long lastNetworkErrorTime = new Date().getTime();
    boolean waitingOnNetworkError = false;

    public void sendRandNetworkError() {

	if (!this.interventionTriggered && !this.waitingToTriggerIntervention) {

	    // send error if time has come
	    if (new Date().getTime() - lastNetworkErrorTime > networkErrorInterval) {

		Vector participants = c.getParticipants()
			.getAllActiveParticipants();
		for (Object p : participants)
		    c.sendAndEnableLabelDisplayToALLStatusWindowsOfParticipant(
			    (Participant) p, "Network Error. Please wait..",
			    true, false);
		lastNetworkErrorTime = new Date().getTime();
		networkErrorInterval = 540000 + r.nextInt(60000);
		waitingOnNetworkError = true;
		Conversation.printWSln("CCCBYCElicitSplitUtt",
			"Blocking participants with network error");
		try {
		    Thread.sleep(4000 + r.nextInt(2000));
		} catch (Exception e) {
		    e.printStackTrace();
		}
		for (Object p : participants) {
		    c.sendAndEnableLabelDisplayToALLStatusWindowsOfParticipant(
			    (Participant) p, "Status: OK", false, true);

		}
		fh.openFloorAfterTimeOut(super.getIsTypingTimeOut());
		waitingOnNetworkError = false;
		Conversation.printWSln("CCCBYCElicitSplitUtt",
			"Released participants from network error");

	    } else
		return;
	} else {
	    // don't send error. wait until next time(reduced interval).
	    lastNetworkErrorTime = new Date().getTime();
	    networkErrorInterval = 20000 + r.nextInt(20000);

	}

    }

    @Override
    public synchronized void finishedSendingFakeTurn(long timeOfLastSend) {

	notifyAll();
    }

    public boolean waitingOnNetError() {
	System.out.println(this.waitingOnNetworkError);
	return this.waitingOnNetworkError;
    }

    public boolean interventionTriggered() {
	System.out.println(this.interventionTriggered);
	return this.interventionTriggered;
    }

    public Participant getAntecedentOwner() {
	System.out.println(this.antecedentOwner.getUsername());
	return this.antecedentOwner;
    }

    public Participant getResponder() {
	System.out.println(this.responder.getUsername());
	return this.responder;
    }

    public boolean receivingResponse() {
	System.out.println(this.receivingResponse);
	return this.receivingResponse;
    }

    public synchronized void setWaitingOnNetError(boolean a) {
	this.waitingOnNetworkError = a;
    }

    public synchronized void forceOpenFloor() {
	fh.setAutomaticallyAllowOpenFloorAfterIsTypingTimeout(true);
	Vector ps = getC().getParticipants().getAllActiveParticipants();
	for (Object p : ps) {
	    Participant par = (Participant) p;
	    fh.changeFloorStatusOfParticipantNoPrefix(par,
		    WYSIWYGDocumentWithEnforcedTurntaking.nooneelsetyping);
	    getC().sendAndEnableLabelDisplayToALLStatusWindowsOfParticipant(
		    par, "Please type", false, true);
	}
    }

    IntParameter minMaxTagDiff;
    IntParameter minMaxUserDiff;
    public boolean disturbsTurnOwnerBalance(String owner)
    {
	int[] minMax = findMinMaxInterPerUserAfterIntervention(owner);
	int min = minMax[0];
	int max = minMax[1];
	Conversation.printWSln("InterV Per Part Balance Check",
		    "minMax:"+min+" "+max);
	if (max - min > minMaxUserDiff.getValue()) {
	    Conversation.printWSln("InterV Per Part Balance Check",
		    "balance disturbed. Turn Owner:"
			    + owner);
	    Conversation.printWSln("InterV Per Part Balance Check",
		    "Inter Per Part counts were:\n" + interPerUser.toString());
	    return true;
	} else {
	    Conversation.printWSln("InterV Per Part Balance Check",
		    "balance not disturbed. Turn Owner:"
			    + owner);
	    Conversation.printWSln("InterV Per Part Balance Check",
		    "Inter Per Part counts were:\n" + interPerUser.toString());
	    return false;

	}
    }
    public boolean disturbsTagBalance(String lastTag) {

	
	if (alwaysGoodTagGroups.contains(tagGroups.get(lastTag))) {
	    Conversation.printWSln("InterV Balance Check",
		    "balance not disturbed. Tag Always Good:"
			    + tagGroups.get(lastTag));
	    return false;
	}
	int[] minMax = findMinMaxAfterIntervention(lastTag);
	int min = minMax[0];
	int max = minMax[1];
	if (max - min > minMaxTagDiff.getValue()) {
	    Conversation.printWSln("InterV Balance Check",
		    "balance disturbed. Last Tag Group:"
			    + tagGroups.get(lastTag));
	    Conversation.printWSln("InterV Balance Check",
		    "group counts were:\n" + tagGroupCount.toString());
	    return true;
	} else {
	    Conversation.printWSln("InterV Balance Check",
		    "balance not disturbed. Last Tag Group:"
			    + tagGroups.get(lastTag));
	    Conversation.printWSln("InterV Balance Check",
		    "group counts were:\n" + tagGroupCount.toString());
	    return false;

	}
    }
    private int[] findMinMaxInterPerUserAfterIntervention(String owner)
    {
	int[] r = new int[2];
	r[0] = 9999;
	r[1] = 0;
	for (String userName : this.interPerUser.keySet()) {
	    Integer cur;
	    if (userName.equals(owner))
		cur = this.interPerUser.get(userName) + 1;
	    else
		cur = this.interPerUser.get(userName);

	    if (cur < r[0])
		r[0] = cur;
	    if (cur > r[1])
		r[1] = cur;
	}
	return r;
	
    }
    private int[] findMinMaxAfterIntervention(String lastTag) {
	int[] r = new int[2];
	r[0] = 9999;
	r[1] = 0;
	for (String tagGroup : this.tagGroupCount.keySet()) {
	    Integer cur;
	    if (tagGroups.get(lastTag).equals(tagGroup))
		cur = this.tagGroupCount.get(tagGroup) + 1;
	    else
		cur = this.tagGroupCount.get(tagGroup);

	    if (cur < r[0])
		r[0] = cur;
	    if (cur > r[1])
		r[1] = cur;
	}
	return r;
    }
}

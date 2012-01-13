/**
 * 
 */
package diet.server.ConversationController;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;
import java.util.Vector;

import diet.client.WYSIWYGDocumentWithEnforcedTurntaking;
import diet.message.MessageCBYCChangeTurntakingStatusConfirm;
import diet.message.MessageCBYCDocChangeFromClient;
import diet.message.MessageCBYCTypingUnhinderedRequest;
import diet.message.MessageKeypressed;
import diet.parameters.ExperimentSettings;
import diet.parameters.Parameter;
import diet.server.Conversation;
import diet.server.Participant;
import diet.server.CbyC.DocChange;
import diet.server.CbyC.Sequences;
import diet.server.CbyC.Sequence.EditSequence;
import diet.server.CbyC.Sequence.LimitedRecipientsSequence;
import diet.server.CbyC.Sequence.POSTagFilterSequence;
import diet.server.CbyC.Sequence.RecordedSequenceFromApparentOrigin;
import diet.server.CbyC.Sequence.Sequence;
import diet.utils.Dictionary;
import diet.utils.POSTagRegex;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;

/**
 * @author Arash
 * 
 */
public class CCCBYCProbeCRWithinVSBetweenConstituent extends
		CCCBYCAbstractProbeCR {

	String[] conditions = { "withinConst", "constBoundary" };
	
	Map<String, Vector<POSTagRegex>> regexes = new HashMap<String, Vector<POSTagRegex>>();
	
	
	
	File withinConstRegexFile = new File(System.getProperty("user.dir")
			+ File.separator + "fragmentFilters" + File.separator
			+ "withinConstRegexes2.txt");
	File constBoundaryRegexFile = new File(System.getProperty("user.dir")
			+ File.separator + "fragmentFilters" + File.separator
			+ "constBoundaryRegexes.txt");
	File misspellingsFile = new File(System.getProperty("user.dir")
			+ File.separator + "fragmentFilters" + File.separator
			+ "misspellings.txt");
	String taggerFileName = System.getProperty("user.dir") + File.separator
			+ "utils" + File.separator
			+ "bidirectional-distsim-wsj-0-18.tagger";
	String condition = "constBoundary";
	MaxentTagger tagger;
	Dictionary dict;
	

	public void initialize(Conversation c, ExperimentSettings expS) {
		super.initialize(c, expS);
		
		Parameter condition = this.expSettings.getParameter("Condition");
		if (condition != null)
			this.condition = (String) condition.getValue();

		if (this.condition.equalsIgnoreCase("random"))
			this.probeCondition = conditions[r.nextInt(conditions.length)];
		else
			this.probeCondition = this.condition;
		String qbot=(String)this.expSettings.getParameter("Q-BOT").getValue();
		if (qbot.equalsIgnoreCase("off")) this.qbot=false;
		else this.qbot=true;
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

		} catch (Exception e) {
			e.printStackTrace();

		}
	}

	TreeMap<String, String> misspellings = new TreeMap<String, String>();

	public synchronized void loadMisspellings() throws IOException {
		misspellings = new TreeMap<String, String>();
		Conversation.printWSln("Main", "loading misspellings");
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
			if (whole.substring(i, i+1).matches("\\s+")) {
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

	// ___________________________________________________________

	

	

	

	

	private Participant chooseRandomParticipant(Vector participants) {

		Random r = new Random(new Date().getTime());

		int pIndex = r.nextInt(participants.size());
		Participant p = (Participant) participants.elementAt(pIndex);
		return p;
	}

	

	

	
	
	

	public Sequence getNextSequence_Speaker_Change(Sequence prior,
			int sequenceNo, Sequences ss, Participant p,
			MessageCBYCTypingUnhinderedRequest mCTUR) {

		if (this.finishTimeOfFragSending > 0 && fragAppOrigin != null) {
			System.out.println("Returning limited rec sequence");
			Vector v = getC().getParticipants().getAllOtherParticipants(
					fragAppOrigin);
			Vector v2 = (Vector) v.clone();
			v2.remove(p);
			receivingResponse = true;
			return new LimitedRecipientsSequence(ss, this, p.getUsername(), v2,
					p, this.probeCondition);
		} else {
			if (this.condition.equalsIgnoreCase("random"))
				this.probeCondition = conditions[r.nextInt(conditions.length)];
			else
				this.probeCondition = condition;

			Conversation.printWSln("CCCBYCProbe", "Set condition to:"
					+ this.probeCondition);
			this.speakerChangesSinceLastIntervention++;
			return new POSTagFilterSequence(ss, this, p.getUsername(), mCTUR,
					tagger, this.regexes.get(this.probeCondition), this.dict,
					this.misspellings, this.probeCondition);
		}
	}

	public Sequence getNextSequence_Edit_By_Different_Speaker(Sequence prior,
			int sequenceNo, Sequences ss, Participant p,
			MessageCBYCTypingUnhinderedRequest mCTUR) {
		Conversation
				.printWSln(
						"ERROR",
						"getNextSequence_Edit_By_Different_Speaker: This should not happen. Edits should be disabled");
		return new EditSequence(sS, this, p.getUsername(), mCTUR);
	}

	public Sequence getNextSequence_NewLine_By_Same_Speaker(Sequence prior,
			int sequenceNo, Sequences sS, String sender,
			MessageCBYCDocChangeFromClient mCDC) {
		Conversation.printWSln("CCCbyCProbe",
				"getNextSequence_NewLine_By_Same_Speaker");
		Participant p = this.getC().getParticipants()
				.findParticipantWithUsername(sender);
		if (this.finishTimeOfFragSending > 0 && fragAppOrigin != null) {
			System.out.println("Returning limited rec sequence");
			Vector v = getC().getParticipants().getAllOtherParticipants(
					fragAppOrigin);
			Vector v2 = (Vector) v.clone();
			v2.remove(p);
			receivingResponse = true;
			return new LimitedRecipientsSequence(sS, this, p.getUsername(), v2,
					p, this.probeCondition);
		} else {

			if (this.condition.equalsIgnoreCase("random"))
				this.probeCondition = conditions[r.nextInt(conditions.length)];
			else
				this.probeCondition = condition;
			Conversation.printWSln("CCCBYCProbe", "Set condition to:"
					+ this.probeCondition);
			DocChange dc = mCDC.getDocChange();
			return new POSTagFilterSequence(sS, this, sender, mCDC
					.getTimeStamp(), dc.elementString, dc.elementStart,
					dc.elementFinish, this.tagger, this.regexes
							.get(this.probeCondition), this.dict,
					this.misspellings, this.probeCondition);
		}
	}

	public Sequence getNextSequence_Edit_By_Same_Speaker(Sequence prior,
			int sequenceNo, Sequences sS, String sender,
			MessageCBYCDocChangeFromClient mCDC) {
		Conversation
				.printWSln(
						"ERROR",
						"getNextSequence_Edit_By_Same_Speaker: This should not happen. Edits should be disabled");

		DocChange dc = mCDC.getDocChange();
		EditSequence seq2 = new EditSequence(sS, this, sender, mCDC
				.getTimeStamp(), dc.elementString, dc.elementStart,
				dc.elementFinish);
		return seq2;
	}

	public void processKeyPress(Participant sender, MessageKeypressed mkp) {

	}

	@Override
	protected Participant chooseFragApparentOrigin(String antecedentOwner) {
		Participant antecedentOwnerP = getC().getParticipants()
		.findParticipantWithUsername(antecedentOwner);
		return this.chooseRandomParticipant(getC()
				.getParticipants().getAllOtherParticipants(antecedentOwnerP));
		
	}
	
	
	
	

}

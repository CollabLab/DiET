package diet.server.conversationhistory;

import java.io.Serializable;
import java.util.Vector;

import diet.debug.Debug;
import diet.parameters.IntParameter;
import diet.parameters.StringParameterFixed;
import diet.server.CbyC.Sequence.MazeGameAcknowledgement;
import diet.server.CbyC.Sequence.MazeGameClarificationRequest;
import diet.server.Conversation;
import diet.server.ConversationUIManager;
import diet.server.ParserWrapper;
import diet.server.StringOfDocChangeInserts;
import diet.server.CbyC.Sequence.MazeGameDefaultSequence;
import diet.server.CbyC.Sequence.RecordedSequenceFromApparentOrigin;
import diet.server.CbyC.Sequence.Sequence;
import diet.server.CbyC.Sequence.SplitUtt;
import diet.server.CbyC.Sequence.SplitUttBufferedTarget;
import diet.server.CbyC.Sequence.SplitUttContiguousByTarget;
import diet.server.StringOfDocChangeInsertsCBYC;
import diet.server.conversationhistory.turn.CBYCMAZEGAMETURN;
import diet.server.conversationhistory.turn.CBYCTURN;
import diet.server.conversationhistory.turn.ContiguousTurn;
import diet.server.conversationhistory.turn.DataToBeSaved;
import diet.server.conversationhistory.turn.MAZEGAMETURN;
import diet.server.conversationhistory.turn.Turn;
import diet.server.conversationhistory.turn.TurnMazeGame;
import diet.server.io.ConversationIO;
import diet.utils.stringsimilarity.StringSimilarityMeasure;
import edu.stanford.nlp.trees.Tree;

/**
 * This and other classes of this package store all the turns from the
 * conversation. Each turn is indexed by its and origin and recipient(s). On
 * saving a turn, all the words are stored in a lexicon, which is also
 * associated with origin/recipients.
 * 
 * <p>
 * The data stored in ConversationHistory abstracts the data from individual
 * messages to Turns. It is designed to function without being associated with a
 * {@link diet.server.Conversation} object, in order to be able to replay saved
 * turns.
 * 
 * 
 * @author user
 */
public class ConversationHistory implements Serializable {

	private Vector turns = new Vector();
	private Vector contiguousTurns = new Vector();
	private PartOfSpeechLexicon plex = new PartOfSpeechLexicon();
	private Vector interventions = new Vector();
	private String abcaStructure = "";
	private ParserWrapper pw;// = new ParserWrapper();
	private Vector conversants = new Vector();
	private ConversationUIManager convUIManager;
	private String conversationName;
	private long startTime = -999999999;
	private boolean setStartTimeOnFirstMessage = true;
	private ConversationIO convIO;
	private Conversation c;
	private StringParameterFixed parseConversationHistory;
	private IntParameter parseConversationHistoryTimeout;

	public ConversationHistory(Conversation c, String nameOfConversation,
			String parserFileLocation, ConversationIO cIO) {
		this.convIO = cIO;

		conversationName = nameOfConversation;
		this.c = c;
		try {

			parseConversationHistory = (StringParameterFixed) c
					.getExperimentSettings().getParameter(
							"Transcript parser enabled");
			parseConversationHistoryTimeout = (IntParameter) c
					.getExperimentSettings().getParameter(
							"Transcript parser timeout");
			if (parseConversationHistory.getValue().equalsIgnoreCase("enabled")) {
				if (parserFileLocation != null)
					pw = new ParserWrapper(parserFileLocation);
			}
		} catch (Exception e) {
			System.err
					.println("TRYING TO LOAD TRANSCRIPT PARSER PARAMETERS: ERROR");
			// System.exit(-2345);
		}

	}

	public void setConversationUIManager(ConversationUIManager ci) {
		this.convUIManager = ci;
	}

	/**
	 * Sets the start time of the conversation. This is so that the first turn
	 * in the conversation will have a turn onset of 0 millisecs.
	 * 
	 * @param startTime
	 */
	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public long getStartTime() {
		return startTime;
	}

	public void setStartTimeOnFirstMessage(boolean setStartFirstMessage) {
		this.setStartTimeOnFirstMessage = setStartFirstMessage;
	}

	public synchronized void saveCBYCSequence(Sequence seq){
       




        if(seq instanceof MazeGameClarificationRequest || seq instanceof MazeGameAcknowledgement)
            {
            StringOfDocChangeInserts sdi = new StringOfDocChangeInserts(seq.getAllDocChangesFromSender());
            MazeGameDefaultSequence seqMG = (MazeGameDefaultSequence)seq;
            this.saveIndividualCBYCSequenceMazeGame(seq.getStartTime().getTime(),seq.getTimeOfLastEnter(),seq.getSender(),seq.getSender(),
                sdi.getString(),seq.getAllRecipients(),false,new Vector(),seq.getAllDocChangesFromSender(),0,
                    seq.getElementText(),seq.getElementStartPos(),seq.getElementFinishPos(),seq.getType(),seq.getTimeOfFirstSend(),seq.getTimeOfLastSend(),seq.getFinalText(),
                    seqMG.mazeNo,seqMG.moveNoATSTART,seqMG.senderMoveNoATSTART,seqMG.moveNoATEND,seqMG.senderMoveNoATEND,
                    seqMG.senderIsOnSwitchAtBegin,seqMG.senderSwitchTraversalCountAtBegin, seqMG.recipientMoveNoATSTART, seqMG.recipientIsOnSwitchAtBegin, seqMG.recipientSwitchTraversalCountAtBegin,
                    seqMG.senderIsOnSwitchAtEnd, seqMG.senderSwitchTraversalCountAtEnd, seqMG.recipientMoveNoATEND, seqMG.recipientIsOnSwitchAtEnd, seqMG.recipientSwitchTraversalCountAtEnd);
        }
        else if(!seq.hasBeenModified() && seq instanceof MazeGameDefaultSequence)
            {
            StringOfDocChangeInserts sdi = new StringOfDocChangeInserts(seq.getAllDocChangesFromSender());
            MazeGameDefaultSequence seqMG = (MazeGameDefaultSequence)seq;
            
            this.saveIndividualCBYCSequenceMazeGame(seq.getStartTime().getTime(),seq.getTimeOfLastEnter(),seq.getSender(),seq.getSender(),
                sdi.getString(),seq.getAllRecipients(),false,new Vector(),seq.getAllDocChangesFromSender(),0,
                    seq.getElementText(),seq.getElementStartPos(),seq.getElementFinishPos(),seq.getType()+"(ATM1)",seq.getTimeOfFirstSend(),seq.getTimeOfLastSend(),seq.getFinalText(),
                    seqMG.mazeNo,seqMG.moveNoATSTART,seqMG.senderMoveNoATSTART,seqMG.moveNoATEND,seqMG.senderMoveNoATEND,
                    seqMG.senderIsOnSwitchAtBegin,seqMG.senderSwitchTraversalCountAtBegin, seqMG.recipientMoveNoATSTART, seqMG.recipientIsOnSwitchAtBegin, seqMG.recipientSwitchTraversalCountAtBegin,
                    seqMG.senderIsOnSwitchAtEnd, seqMG.senderSwitchTraversalCountAtEnd, seqMG.recipientMoveNoATEND, seqMG.recipientIsOnSwitchAtEnd, seqMG.recipientSwitchTraversalCountAtEnd);
            
        }
        else if(seq.hasBeenModified()&&seq instanceof MazeGameDefaultSequence){
            StringOfDocChangeInserts sdi = new StringOfDocChangeInserts(seq.getAllDocChangesFromSender());
            MazeGameDefaultSequence seqMG = (MazeGameDefaultSequence)seq;
            
            this.saveIndividualCBYCSequenceMazeGame(seq.getStartTime().getTime(),seq.getTimeOfLastEnter(),seq.getSender(),seq.getSender(),
                sdi.getString(),seq.getAllRecipients(),false,new Vector(),seq.getAllDocChangesFromSender(),0,
                    seq.getElementText(),seq.getElementStartPos(),seq.getElementFinishPos(),seq.getType()+"(ATM2)",seq.getTimeOfFirstSend(),seq.getTimeOfLastSend(),seq.getFinalText(),
                    seqMG.mazeNo,seqMG.moveNoATSTART,seqMG.senderMoveNoATSTART,seqMG.moveNoATEND,seqMG.senderMoveNoATEND,
                    seqMG.senderIsOnSwitchAtBegin,seqMG.senderSwitchTraversalCountAtBegin, seqMG.recipientMoveNoATSTART, seqMG.recipientIsOnSwitchAtBegin, seqMG.recipientSwitchTraversalCountAtBegin,
                    seqMG.senderIsOnSwitchAtEnd, seqMG.senderSwitchTraversalCountAtEnd, seqMG.recipientMoveNoATEND, seqMG.recipientIsOnSwitchAtEnd, seqMG.recipientSwitchTraversalCountAtEnd);

            
                Vector v = seq.getAllRecipients();
                for(int i=0;i<v.size();i++){
                   String recipientName = (String)v.elementAt(i);
                   StringOfDocChangeInserts sdiReceived = new StringOfDocChangeInserts(seq.getRecipientsDocChanges(recipientName));
                   Vector recipients = new Vector();
                   recipients.addElement(recipientName);
                   this.saveIndividualCBYCSequenceMazeGame(seq.getStartTime().getTime(),seq.getTimeOfLastEnter(),"server",seq.getApparentOriginForRecipient(recipientName),
                   sdiReceived.getString(),recipients,false,new Vector(),seq.getRecipientsDocChanges(recipientName),0,
                   seq.getElementText(),seq.getElementStartPos(),seq.getElementFinishPos(),seq.getType()+"(ATM3)",seq.getTimeOfFirstSend(),seq.getTimeOfLastSend(),seq.getFinalText(),
                   seqMG.mazeNo,seqMG.moveNoATSTART,seqMG.senderMoveNoATSTART,seqMG.moveNoATEND,seqMG.senderMoveNoATEND,
                   seqMG.senderIsOnSwitchAtBegin,seqMG.senderSwitchTraversalCountAtBegin, seqMG.recipientMoveNoATSTART, seqMG.recipientIsOnSwitchAtBegin, seqMG.recipientSwitchTraversalCountAtBegin,
                   seqMG.senderIsOnSwitchAtEnd, seqMG.senderSwitchTraversalCountAtEnd, seqMG.recipientMoveNoATEND, seqMG.recipientIsOnSwitchAtEnd, seqMG.recipientSwitchTraversalCountAtEnd);

                }
        
         
        
        
        }
        else if(seq instanceof RecordedSequenceFromApparentOrigin)
        {
        	StringOfDocChangeInserts sdi = new StringOfDocChangeInserts(seq.getAllDocChangesFromSender());
        	RecordedSequenceFromApparentOrigin rs=(RecordedSequenceFromApparentOrigin)seq;
        	
            this.saveIndividualCBYCSequence(rs.getStartTime().getTime(),rs.getTimeOfLastEnter(),rs.getSender(),rs.getApparentSender(),
                sdi.getString(),rs.getAllRecipients(),false,new Vector(),seq.getAllDocChangesFromSender(),0,
                    seq.getElementText(),seq.getElementStartPos(),seq.getElementFinishPos(),seq.getType()+"(AT)",seq.getTimeOfFirstSend(),seq.getTimeOfLastSend(),seq.getFinalText()   );
        	
        }
        
        else if(!seq.hasBeenModified()){
            StringOfDocChangeInserts sdi = new StringOfDocChangeInserts(seq.getAllDocChangesFromSender());
            
            this.saveIndividualCBYCSequence(seq.getStartTime().getTime(),seq.getTimeOfLastEnter(),seq.getSender(),seq.getSender(),
                sdi.getString(),seq.getAllRecipients(),false,new Vector(),seq.getAllDocChangesFromSender(),0,
                    seq.getElementText(),seq.getElementStartPos(),seq.getElementFinishPos(),seq.getType()+"(AT)",seq.getTimeOfFirstSend(),seq.getTimeOfLastSend(),seq.getFinalText()   );
            
        }
        else if(seq instanceof SplitUttBufferedTarget){
           StringOfDocChangeInserts sdi = new StringOfDocChangeInserts(seq.getAllDocChangesFromSender());
            
           this.saveIndividualCBYCSequence(seq.getStartTime().getTime(),seq.getTimeOfLastEnter(),seq.getSender(),seq.getSender(),
                sdi.getString(),new Vector(),false,new Vector(),seq.getAllDocChangesFromSender(),0,
                    seq.getElementText(),seq.getElementStartPos(),seq.getElementFinishPos(),seq.getType()+"(AT1)",seq.getTimeOfFirstSend(),seq.getTimeOfLastSend(),seq.getFinalText()   );
            
                Vector v = seq.getAllRecipients();
                for(int i=0;i<v.size();i++){
                   String recipientName = (String)v.elementAt(i);
                   StringOfDocChangeInserts sdiReceived = new StringOfDocChangeInserts(seq.getRecipientsDocChanges(recipientName));
                   Vector recipients = new Vector();
                   recipients.addElement(recipientName);
                   this.saveIndividualCBYCSequence(seq.getStartTime().getTime(),seq.getTimeOfLastEnter(),"server",seq.getApparentOriginForRecipient(recipientName),
                   sdiReceived.getString(),recipients,false,new Vector(),seq.getRecipientsDocChanges(recipientName),0,
                   seq.getElementText(),seq.getElementStartPos(),seq.getElementFinishPos(),seq.getType()+"(AT3)",seq.getTimeOfFirstSend(),seq.getTimeOfLastSend() ,seq.getFinalText()   );
                }
        
        } 
        else if(seq instanceof SplitUtt){
           SplitUtt seqSp = (SplitUtt)seq;
           StringOfDocChangeInserts sdi = new StringOfDocChangeInserts(seq.getAllDocChangesFromSender());
            
           this.saveIndividualCBYCSequence(seq.getStartTime().getTime(),seq.getTimeOfLastEnter(),seq.getSender(),seq.getSender(),
                sdi.getString(),new Vector(),false,new Vector(),seq.getAllDocChangesFromSender(),0,
                    seq.getElementText(),seq.getElementStartPos(),seq.getElementFinishPos(),seqSp.getType(seq.getSender())+"(AT4)",seq.getTimeOfFirstSend(),seq.getTimeOfLastSend(),seq.getFinalText()   );
            
            
            Vector v = seq.getAllRecipients();
            for(int i=0;i<v.size();i++){
                String recipientName = (String)v.elementAt(i);
                StringOfDocChangeInsertsCBYC sdiReceived = new StringOfDocChangeInsertsCBYC(seq.getRecipientsDocChanges(recipientName));
                Vector recipients = new Vector();
                recipients.addElement(recipientName);
                this.saveIndividualCBYCSequence(seq.getStartTime().getTime(),seq.getTimeOfLastEnter(),"server",seq.getApparentOriginForRecipient(recipientName),
                sdiReceived.getString(),recipients,false,new Vector(),seq.getRecipientsDocChanges(recipientName),0,
                    seq.getElementText(),seq.getElementStartPos(),seq.getElementFinishPos(),seqSp.getType(recipientName)+"(AT5)",sdiReceived.getSendStart(),sdiReceived.getSendFinish() ,seq.getFinalText()   );
            }
            /*long onset, long enter, String senderUsername,
			String apparentSenderUsername, String text, Vector recipientsNames,
			boolean wasBlocked, Vector keyPresses, Vector documentUpdates,
			int turnNumber, String eString, int eStart, int eFinish,
			String type, long timeOfFirstSend, long timeOfLastSend,
			String finalText, int mazeNo, int moveNoATSTART,
			int indivMoveNoATSTART, int moveNoATEND, int indivMoveNoATEND) */
        }
        else if(seq instanceof SplitUttContiguousByTarget){
           SplitUttContiguousByTarget seqSp = (SplitUttContiguousByTarget)seq;
           StringOfDocChangeInserts sdi = new StringOfDocChangeInserts(seq.getAllDocChangesFromSender());
            
           this.saveIndividualCBYCSequence(seq.getStartTime().getTime(),seq.getTimeOfLastEnter(),seq.getSender(),seq.getSender(),
                sdi.getString(),new Vector(),false,new Vector(),seq.getAllDocChangesFromSender(),0,
                    seq.getElementText(),seq.getElementStartPos(),seq.getElementFinishPos(),seqSp.getType()+"(AT4b)",seq.getTimeOfFirstSend(),seq.getTimeOfLastSend(),seq.getFinalText()   );
            
            
            Vector v = seq.getAllRecipients();
            for(int i=0;i<v.size();i++){
                String recipientName = (String)v.elementAt(i);
                StringOfDocChangeInsertsCBYC sdiReceived = new StringOfDocChangeInsertsCBYC(seq.getRecipientsDocChanges(recipientName));
                Vector recipients = new Vector();
                recipients.addElement(recipientName);
                this.saveIndividualCBYCSequence(seq.getStartTime().getTime(),seq.getTimeOfLastEnter(),"server",seq.getApparentOriginForRecipient(recipientName),
                sdiReceived.getString(),recipients,false,new Vector(),seq.getRecipientsDocChanges(recipientName),0,
                    seq.getElementText(),seq.getElementStartPos(),seq.getElementFinishPos(),seqSp.getType()+"(AT5b)",sdiReceived.getSendStart(),sdiReceived.getSendFinish() ,seq.getFinalText()   );
            }
        }
        
        
        
    }

	private synchronized void saveIndividualCBYCSequence(

	long onset, long enter, String senderUsername,
			String apparentSenderUsername, String text, Vector recipientsNames,
			boolean wasBlocked, Vector keyPresses, Vector documentUpdates,
			int turnNumber, String eString, int eStart, int eFinish,
			String type, long timeOfFirstSend, long timeOfLastSend,
			String finalText) {

		if (turns.size() == 0 & setStartTimeOnFirstMessage & this.startTime <-999999998) {
			this.startTime = onset;
		}

		Conversant sender = getConversantAddingIfNecessary(senderUsername);
		Conversant apparentSender = getConversantAddingIfNecessary(apparentSenderUsername);
		Vector recipients = getConversantsAddingIfNecessary(recipientsNames);
		int recipConversantsSize = recipients.size();

		Vector parseTreeAndWords = new Vector();
		Vector parsedWords = new Vector();
		Tree parseTree = null;

		try {
			String parseConv = (String) this.parseConversationHistory
					.getValue();
			if (5 < 2) {
				int parseTimeout = (Integer) this.parseConversationHistoryTimeout
						.getValue();
				parseTreeAndWords = pw.parseTextTimeOut(text, 9000);// parseTimeout);
				parsedWords = (Vector) parseTreeAndWords.elementAt(0);
				parseTree = (Tree) parseTreeAndWords.elementAt(1);
				// System.exit(-242);
			}
		} catch (Exception e) {
			System.out
					.println("ERROR GETTING PARAMETERS FOR TRANSCRIPT PARSER");
			System.exit(-453);
		}

		// ----------------------------------------------------------------------------

		Turn t = new CBYCTURN(this, onset, enter, sender, apparentSender, text,
				recipients, wasBlocked, keyPresses, documentUpdates,
				parsedWords, parseTree, turnNumber, eString, eStart, eFinish,
				type, timeOfFirstSend, timeOfLastSend, finalText);

		if (Debug.trackTypeError) {
			if (type == null) {
				System.exit(-312312312);
			}
			if (type.equalsIgnoreCase("")) {
				System.exit(-312312315);
			}
		}

		plex.addWordsToLexicon(t, parsedWords, sender, recipients);
		sender.addTurnProduced(t);
		for (int i = 0; i < recipients.size(); i++) {
			Conversant c2 = (Conversant) conversants.elementAt(i);
			c2.addTurnReceived(t);
		}

		updateContiguousTurns(t);
		turns.addElement(t);// Has to be after updating contiguous turns

		if (t.getContiguousTurn() == null) {
			System.err.println("ERROR with contiguous turns: "
					+ t.getSender().getUsername());
		}
		if (this.convUIManager != null)
			convUIManager.updateHistoryUI(t);
		this.convIO.saveTurn(t);

	}











        private synchronized void saveIndividualCBYCSequenceMazeGame(long onset, long enter, String senderUsername,
			String apparentSenderUsername, String text, Vector recipientsNames,
			boolean wasBlocked, Vector keyPresses, Vector documentUpdates,
			int turnNumber, String eString, int eStart, int eFinish,
			String type, long timeOfFirstSend, long timeOfLastSend,
			String finalText, int mazeNo, int moveNoATSTART,
			int indivMoveNoATSTART, int moveNoATEND, int indivMoveNoATEND,
                        boolean senderIsOnSwitchAtBegin,int senderSwitchTraversalCountAtBegin, int recipientMoveNoATSTART, boolean recipientIsOnSwitchAtBegin, int recipientSwitchTraversalCountAtBegin,
                        boolean senderIsOnSwitchAtEnd, int senderSwitchTraversalCountAtEnd, int recipientMoveNoATEND, boolean recipientIsOnSwitchAtEnd, int recipientSwitchTraversalCountAtEnd
                        ) {

		if (turns.size() == 0 & setStartTimeOnFirstMessage) {
			this.startTime = onset;
		}

		Conversant sender = getConversantAddingIfNecessary(senderUsername);
		Conversant apparentSender = getConversantAddingIfNecessary(apparentSenderUsername);
		Vector recipients = getConversantsAddingIfNecessary(recipientsNames);
		int recipConversantsSize = recipients.size();

		Vector parseTreeAndWords = new Vector();
		Vector parsedWords = new Vector();
		Tree parseTree = null;

		try {
			String parseConv = (String) this.parseConversationHistory
					.getValue();
			if (5 < 2) {
				int parseTimeout = (Integer) this.parseConversationHistoryTimeout
						.getValue();
				parseTreeAndWords = pw.parseTextTimeOut(text, 9000);// parseTimeout);
				parsedWords = (Vector) parseTreeAndWords.elementAt(0);
				parseTree = (Tree) parseTreeAndWords.elementAt(1);
				// System.exit(-242);
			}
		} catch (Exception e) {
			System.out
					.println("ERROR GETTING PARAMETERS FOR TRANSCRIPT PARSER");
			System.exit(-453);
		}

		// ----------------------------------------------------------------------------

		Turn t = new CBYCMAZEGAMETURN(this, onset, enter, sender,
				apparentSender, text, recipients, wasBlocked, keyPresses,
				documentUpdates, parsedWords, parseTree, turnNumber, eString,
				eStart, eFinish, type, timeOfFirstSend, timeOfLastSend,
				finalText, mazeNo, moveNoATSTART, indivMoveNoATSTART,
				moveNoATEND, indivMoveNoATEND,
                                senderIsOnSwitchAtBegin,senderSwitchTraversalCountAtBegin, recipientMoveNoATSTART, recipientIsOnSwitchAtBegin, recipientSwitchTraversalCountAtBegin,
                                senderIsOnSwitchAtEnd, senderSwitchTraversalCountAtEnd, recipientMoveNoATEND, recipientIsOnSwitchAtEnd, recipientSwitchTraversalCountAtEnd);





		if (Debug.trackTypeError) {
			if (type == null) {
				System.exit(-312312312);
			}
			if (type.equalsIgnoreCase("")) {
				System.exit(-312312315);
			}
		}

		plex.addWordsToLexicon(t, parsedWords, sender, recipients);
		sender.addTurnProduced(t);
		for (int i = 0; i < recipients.size(); i++) {
			Conversant c2 = (Conversant) conversants.elementAt(i);
			c2.addTurnReceived(t);
		}

		updateContiguousTurns(t);
		turns.addElement(t);// Has to be after updating contiguous turns

		if (t.getContiguousTurn() == null) {
			System.err.println("ERROR with contiguous turns: "
					+ t.getSender().getUsername());
		}
		if (this.convUIManager != null)
			convUIManager.updateHistoryUI(t);
		this.convIO.saveTurn(t);

	}




	private synchronized void saveIndividualCBYCSequenceMazeGameDEPRECATED_SEE_OTHER_METHOD_THAT_SAVES_MORE(

	long onset, long enter, String senderUsername,
			String apparentSenderUsername, String text, Vector recipientsNames,
			boolean wasBlocked, Vector keyPresses, Vector documentUpdates,
			int turnNumber, String eString, int eStart, int eFinish,
			String type, long timeOfFirstSend, long timeOfLastSend,
			String finalText, int mazeNo, int moveNoATSTART,
			int indivMoveNoATSTART, int moveNoATEND, int indivMoveNoATEND) {

		if (turns.size() == 0 & setStartTimeOnFirstMessage& this.startTime <-999999998) {
			this.startTime = onset;
		}

		Conversant sender = getConversantAddingIfNecessary(senderUsername);
		Conversant apparentSender = getConversantAddingIfNecessary(apparentSenderUsername);
		Vector recipients = getConversantsAddingIfNecessary(recipientsNames);
		int recipConversantsSize = recipients.size();

		Vector parseTreeAndWords = new Vector();
		Vector parsedWords = new Vector();
		Tree parseTree = null;

		try {
			String parseConv = (String) this.parseConversationHistory
					.getValue();
			if (5 < 2) {
				int parseTimeout = (Integer) this.parseConversationHistoryTimeout
						.getValue();
				parseTreeAndWords = pw.parseTextTimeOut(text, 9000);// parseTimeout);
				parsedWords = (Vector) parseTreeAndWords.elementAt(0);
				parseTree = (Tree) parseTreeAndWords.elementAt(1);
				// System.exit(-242);
			}
		} catch (Exception e) {
			System.out
					.println("ERROR GETTING PARAMETERS FOR TRANSCRIPT PARSER");
			System.exit(-453);
		}

		// ----------------------------------------------------------------------------

		Turn t = new CBYCMAZEGAMETURN(this, onset, enter, sender,
				apparentSender, text, recipients, wasBlocked, keyPresses,
				documentUpdates, parsedWords, parseTree, turnNumber, eString,
				eStart, eFinish, type, timeOfFirstSend, timeOfLastSend,
				finalText, mazeNo, moveNoATSTART, indivMoveNoATSTART,
				moveNoATEND, indivMoveNoATEND);





		if (Debug.trackTypeError) {
			if (type == null) {
				System.exit(-312312312);
			}
			if (type.equalsIgnoreCase("")) {
				System.exit(-312312315);
			}
		}

		plex.addWordsToLexicon(t, parsedWords, sender, recipients);
		sender.addTurnProduced(t);
		for (int i = 0; i < recipients.size(); i++) {
			Conversant c2 = (Conversant) conversants.elementAt(i);
			c2.addTurnReceived(t);
		}

		updateContiguousTurns(t);
		turns.addElement(t);// Has to be after updating contiguous turns

		if (t.getContiguousTurn() == null) {
			System.err.println("ERROR with contiguous turns: "
					+ t.getSender().getUsername());
		}
		if (this.convUIManager != null)
			convUIManager.updateHistoryUI(t);
		this.convIO.saveTurn(t);

	}

	/**
	 * 
	 * This saves text from the conversation, associating it with an origin and
	 * recipients, as well as timing data. In addition, this method will append
	 * the single to a contiguous turn (if appropriate) and store the individual
	 * words associated with the turn.
	 * 
	 * <p>
	 * This method when called from the Conversation class must always be called
	 * AFTER sending the message to the participants. This is because the
	 * participant.send() method carries out the timestamping.
	 * 
	 * @param onset
	 * @param enter
	 * @param senderUsername
	 * @param apparentSenderUsername
	 * @param text
	 * @param recipientsNames
	 * @param wasBlocked
	 * @param keyPresses
	 * @param documentUpdates
	 * @param turnNumber
	 */
	public synchronized void saveMessage(String dtype, long onset, long enter,String senderUsername, String apparentSenderUsername, String text,
			Vector recipientsNames, boolean wasBlocked, Vector keyPresses, Vector documentUpdates, int turnNumber) {
		if (turns.size() == 0 & setStartTimeOnFirstMessage& this.startTime <-999999998) {
			this.startTime = onset;
		}

		Conversant sender = getConversantAddingIfNecessary(senderUsername);
		Conversant apparentSender = getConversantAddingIfNecessary(apparentSenderUsername);
		Vector recipients = getConversantsAddingIfNecessary(recipientsNames);
		int recipConversantsSize = recipients.size();

		Vector parseTreeAndWords = new Vector();
		Vector parsedWords = new Vector();
		Tree parseTree = null;

		try {
			String parseConv = (String) this.parseConversationHistory
					.getValue();
			if (parseConv.equalsIgnoreCase("ENABLED")) {
				int parseTimeout = (Integer) this.parseConversationHistoryTimeout
						.getValue();
				parseTreeAndWords = pw.parseTextTimeOut(text, 9000);// parseTimeout);
				parsedWords = (Vector) parseTreeAndWords.elementAt(0);
				parseTree = (Tree) parseTreeAndWords.elementAt(1);
				// System.exit(-242);
			}
		} catch (Exception e) {
			System.out
					.println("ERROR GETTING PARAMETERS FOR TRANSCRIPT PARSER");
			System.exit(-453);
		}

		// ----------------------------------------------------------------------------

		Turn t = new Turn(this, dtype,onset, enter, sender, apparentSender, text,
				recipients, wasBlocked, keyPresses, documentUpdates,
				parsedWords, parseTree, turnNumber,new Vector());

		plex.addWordsToLexicon(t, parsedWords, sender, recipients);
		sender.addTurnProduced(t);
		for (int i = 0; i < recipients.size(); i++) {
			Conversant c2 = (Conversant) conversants.elementAt(i);
			c2.addTurnReceived(t);
		}

		updateContiguousTurns(t);
		turns.addElement(t);// Has to be after updating contiguous turns

		if (t.getContiguousTurn() == null) {
			System.err.println("ERROR with contiguous turns: "
					+ t.getSender().getUsername());
		}
		if (this.convUIManager != null)
			convUIManager.updateHistoryUI(t);
		this.convIO.saveTurn(t);

	}

	public void saveMazeGameMessage(long onset, long enter,
			String senderUsername, String apparentSenderUsername, String text,
			Vector recipientsNames, boolean wasBlocked, Vector keyPresses,
			Vector documentUpdates, int turnNumber, int mazeNo, int moveNo,
			int indivMveNo) {
		if (turns.size() == 0 & setStartTimeOnFirstMessage& this.startTime <-999999998) {
			this.startTime = onset;
		}

		Conversant sender = getConversantAddingIfNecessary(senderUsername);
		Conversant apparentSender = getConversantAddingIfNecessary(apparentSenderUsername);
		Vector recipients = getConversantsAddingIfNecessary(recipientsNames);
		int recipConversantsSize = recipients.size();

		Vector parseTreeAndWords = new Vector();
		Vector parsedWords = new Vector();
		Tree parseTree = null;

		try {
			String parseConv = (String) this.parseConversationHistory
					.getValue();
			if (parseConv.equalsIgnoreCase("ENABLED")) {
				int parseTimeout = (Integer) this.parseConversationHistoryTimeout
						.getValue();
				parseTreeAndWords = pw.parseTextTimeOut(text, 9000);// parseTimeout);
				parsedWords = (Vector) parseTreeAndWords.elementAt(0);
				parseTree = (Tree) parseTreeAndWords.elementAt(1);
				// System.exit(-242);
			}
		} catch (Exception e) {
			System.out
					.println("ERROR GETTING PARAMETERS FOR TRANSCRIPT PARSER");
			System.exit(-453);
		}

		// ----------------------------------------------------------------------------

		MAZEGAMETURN t = new MAZEGAMETURN(this, onset, enter, sender,
				apparentSender, text, recipients, wasBlocked, keyPresses,
				documentUpdates, parsedWords, parseTree, turnNumber, mazeNo,
				moveNo, indivMveNo);

		plex.addWordsToLexicon(t, parsedWords, sender, recipients);
		sender.addTurnProduced(t);
		for (int i = 0; i < recipients.size(); i++) {
			Conversant c2 = (Conversant) conversants.elementAt(i);
			c2.addTurnReceived(t);
		}

		updateContiguousTurns(t);
		turns.addElement(t);// Has to be after updating contiguous turns

		if (t.getContiguousTurn() == null) {
			System.err.println("ERROR with contiguous turns: "
					+ t.getSender().getUsername());
		}
		if (this.convUIManager != null)
			convUIManager.updateHistoryUI(t);
		this.convIO.saveTurn(t);

	}

	public TurnMazeGame saveMessageFromCRMazeGame(long onset, long enter,
			String senderUsername, String apparentSenderUsername, String text,
			Vector recipientsNames, boolean wasBlocked, Vector keyPresses,
			Vector documentUpdates, int turnNumber, String descType,
			String ackCROther) {
		if (turns.size() == 0 & setStartTimeOnFirstMessage&& this.startTime <-999999998) {
			this.startTime = onset;
		}

		Conversant sender = getConversantAddingIfNecessary(senderUsername);
		Conversant apparentSender = getConversantAddingIfNecessary(apparentSenderUsername);
		Vector recipients = getConversantsAddingIfNecessary(recipientsNames);
		int recipConversantsSize = recipients.size();

		Vector parseTreeAndWords = new Vector();
		Vector parsedWords = new Vector();
		Tree parseTree = null;

		try {
			String parseConv = (String) this.parseConversationHistory
					.getValue();
			if (parseConv.equalsIgnoreCase("ENABLED")) {
				int parseTimeout = (Integer) this.parseConversationHistoryTimeout
						.getValue();
				parseTreeAndWords = pw.parseTextTimeOut(text, 9000);// parseTimeout);
				parsedWords = (Vector) parseTreeAndWords.elementAt(0);
				parseTree = (Tree) parseTreeAndWords.elementAt(1);
				// System.exit(-242);
			}
		} catch (Exception e) {
			// System.out.println("ERROR GETTING PARAMETERS FOR TRANSCRIPT PARSER");
			// System.exit(-453);
		}

		// ----------------------------------------------------------------------------

		TurnMazeGame t = new TurnMazeGame(this, onset, enter, sender,
				apparentSender, text, recipients, wasBlocked, keyPresses,
				documentUpdates, parsedWords, parseTree, turnNumber, descType,
				ackCROther);

		parsedWords = StringSimilarityMeasure.splitIntoWords(text);

		plex.addStringsToLexicon(t, parsedWords, sender, recipients);
		// System.out.println("LEXICONBEGIN");
		// plex.printLexiconDummy();
		// System.out.println("LEXICONEND");
		sender.addTurnProduced(t);
		for (int i = 0; i < recipients.size(); i++) {
			Conversant c2 = (Conversant) conversants.elementAt(i);
			c2.addTurnReceived(t);
		}

		updateContiguousTurns(t);
		turns.addElement(t);// Has to be after updating contiguous turns

		if (t.getContiguousTurn() == null) {
			System.err.println("ERROR with contiguous turns: "
					+ t.getSender().getUsername());
			System.exit(-12233456);
		}
		if (this.convUIManager != null)
			convUIManager.updateHistoryUI(t);
		if (convIO != null) {
			this.convIO.saveTurn(t);
		}

		return t;
	}

	/**
	 * This verifies whether the most recent turn is of the same origin. If so,
	 * it appends it to the contiguous turns
	 * 
	 * @param t
	 */
	private void updateContiguousTurns(Turn t) {

		ContiguousTurn ct;
		if (contiguousTurns.size() == 0) {
			ct = new ContiguousTurn(t);
			contiguousTurns.addElement(ct);
			t.setContiguousTurn(ct);
			return;
		} else {
			ct = (ContiguousTurn) contiguousTurns.elementAt(contiguousTurns
					.size() - 1);
		}
		if (t.getSender().equals(ct.getSender())) {
			ct.addTurn(t);
			t.setContiguousTurn(ct);

		} else {
			ContiguousTurn contiguousT = new ContiguousTurn(t);
			t.setContiguousTurn(contiguousT);
			contiguousTurns.addElement(contiguousT);
		}
	}

	/**
	 * 
	 * @param usernames
	 *            Vector containing Strings of the names of Conversants
	 * @return Vector containing conversants
	 */
	private Vector getConversantsAddingIfNecessary(Vector usernames) {
		Vector v = new Vector();
		for (int i = 0; i < usernames.size(); i++) {
			String username = (String) usernames.elementAt(i);
			v.addElement(getConversantAddingIfNecessary(username));
		}
		return v;
	}

	/**
	 * Searches for conversant. If conversant not found, creates and returns a
	 * new Conversant with specified username
	 * 
	 * @param username
	 *            username to search for
	 * @return conversant with username
	 */
	private Conversant getConversantAddingIfNecessary(String username) {
		for (int i = 0; i < conversants.size(); i++) {
			Conversant conv = (Conversant) conversants.elementAt(i);
			if (conv.getUsername().equalsIgnoreCase(username)) {
				return conv;
			}
		}
		System.out.println("Adding new conversant with username " + username);
		Conversant c2 = new Conversant(username);
		conversants.addElement(c2);

		return c2;
	}

	public void mergeUsernames(String username1, String username2) {
		// Have this as option on UI
	}

	public ParserWrapper getParserWrapper() {
		return pw;
	}

	public PartOfSpeechLexicon getLexicon() {
		return this.plex;
	}

	/**
	 * Returns all the turns of the conversation
	 * 
	 * @return Vector containing all turns of conversation
	 */
	public Vector getTurns() {
		return this.turns;
	}

	public Vector getContiguousTurns() {
		return contiguousTurns;
	}

	public Vector getConversants() {
		return conversants;
	}

	public String getConversationIdentifier() {
		return this.conversationName;
	}

	private Conversant getConversant(String s) {
		for (int i = 0; i < conversants.size(); i++) {
			Conversant c2 = (Conversant) conversants.elementAt(i);
			if (c2.getUsername().equals(s))
				return c2;
		}
		return null;
	}

	/**
	 * Returns the last N turns produced by a conersant
	 * 
	 * @param name
	 *            name of conversant
	 * @param numberOfTurns
	 *            number of turns to look back in conversation history
	 * @return Vector containing the last N turns
	 */
	private Vector getLastNTurnsProduced(String name, int numberOfTurns) {
		Conversant c2 = getConversant(name);
		Vector vAllTurnsProduced = c2.getTurnsProduced();
		Vector v = new Vector();
		for (int i = vAllTurnsProduced.size() - 1 - numberOfTurns; i < vAllTurnsProduced
				.size(); i++) {
			v.addElement(vAllTurnsProduced.elementAt(i));
		}
		return v;
	}

	public ContiguousTurn getMostRecentContiguousTurnOfConversant(String name) {
		Conversant c2 = getConversant(name);
		for (int i = contiguousTurns.size() - 1; i >= 0; i--) {
			ContiguousTurn ct = (ContiguousTurn) contiguousTurns.elementAt(i);
			if (ct.getSender() == c2) {
				return ct;
			}
		}
		return null;
	}

	public String getNameOfMostRecentSpeakerIgnoringInterventions() {
		Conversant lastSpeakerIgnoringInterventions;
		for (int i = turns.size() - 1; i >= 0; i--) {
			Turn t = (Turn) turns.elementAt(i);
			if (!t.getSender().getUsername().equalsIgnoreCase("server")) {
				lastSpeakerIgnoringInterventions = t.getSender();
				return lastSpeakerIgnoringInterventions.getUsername();
			}
		}
		return null;
	}

	public String getNameOfMostRecentSpeaker() {
		try {
			Turn t = (Turn) turns.elementAt(turns.size() - 1);
			return t.getApparentSender().getUsername();
		} catch (Exception e) {
			return null;
		}
	}

	public ContiguousTurn getMostRecentContiguousTurn() {
		if (contiguousTurns.size() == 0)
			return null;
		ContiguousTurn ct = (ContiguousTurn) this.contiguousTurns
				.elementAt(contiguousTurns.size() - 1);
		return ct;
	}

	public String getNameOfImmediatelyPriorApparentSpeaker() {
		Turn lastTurn = (Turn) turns.elementAt(turns.size() - 1);
		Conversant lastApparentSpeaker = lastTurn.getApparentSender();
		for (int i = turns.size() - 2; i >= 0; i--) {
			Turn t = (Turn) turns.elementAt(i);
			if (t.getApparentSender() != lastApparentSpeaker) {
				return t.getApparentSender().getUsername();
			}
		}
		return null;
	}

	public String getNameOfImmediatelyPriorSpeakerIgnoringInterventions() {
		Conversant lastSpeakerIgnoringInterventions;
		for (int i = turns.size() - 1; i >= 0; i--) {
			Turn t = (Turn) turns.elementAt(i);
			if (!t.getSender().getUsername().equalsIgnoreCase("server")) {
				lastSpeakerIgnoringInterventions = t.getSender();
				String tUsername = lastSpeakerIgnoringInterventions
						.getUsername();
				for (int j = i; j >= 0; j--) {
					Turn t2 = (Turn) turns.elementAt(j);
					String t2Username = t2.getSender().getUsername();
					if (!t2Username.equalsIgnoreCase("server")
							&& !t2Username.equalsIgnoreCase(tUsername)) {
						return t2Username;
					}
				}
				return null;
			}
		}
		return null;
	}

	/**
	 * Returns the average typing speed of the last N turns produced by a
	 * conversant
	 * 
	 * @param name
	 *            name of conversant
	 * @param numberOfTurns
	 *            number of turns to look back
	 * @return the average typing speed per turn (msecs)
	 */
	public long getAverageTypingSpeedForLastNTurns(String name,
			int numberOfTurns) {
		Conversant c2 = getConversant(name);
		if (c == null)
			return 0;
		Vector vAllTurnsProduced = c2.getTurnsProduced();
		Vector v = new Vector();
		long totalTime = 0;
		long numberOfTurnsFound = 0;
		for (int i = vAllTurnsProduced.size() - 1 - numberOfTurns; i < vAllTurnsProduced
				.size()
				&& i >= 0; i++) {
			v.addElement(vAllTurnsProduced.elementAt(i));
			Turn t = (Turn) vAllTurnsProduced.elementAt(i);
			totalTime = totalTime + t.getTypingReturnPressedNormalized()
					- t.getTypingOnsetNormalized();
			numberOfTurnsFound++;
		}
		if (numberOfTurnsFound == 0)
			return 0;
		return (totalTime / numberOfTurnsFound) * 1000;
	}
	
	/**
	 * Returns the average typing speed of conversant. This is to be used
	 * with the CByC interface. . . the return value represents the average
	 * number of characters, typed per second by the conversant
	 * @param name
	 *            name of conversant
	 * @param n
	 *            number of turns to look back
	 * @return the average typing speed in characters per second
	 */
	public long getAverageTypingSpeedCharsPerSecond(String name, int n)
	{
		Conversant c=getConversant(name);
		if (c==null) 
			{
				System.err.println("returning 3 for average typing speed. Conversant null");
				return 3;
			}
		
		Vector allTurns=c.getTurnsProduced();
		System.err.println("trying to calculate average typing speed. Turns:"+allTurns.size());
		int m=n;
		while(m>allTurns.size())
		{
			m--;
		}
		if (m==0) return 5;
		long totalSpeed=0;
		
		for(int i=allTurns.size()-1; i>allTurns.size()-m-1; i--)
		{
			Turn t=(Turn)allTurns.get(i);
			totalSpeed+=t.getTypingSpeedForTurnCharactersPerSecond();
		}
		Conversation.printWSln("Typing speed", "returning:"+totalSpeed/m);
		
		long charsPerSec=totalSpeed/m;
		if (charsPerSec<5) {Conversation.printWSln("Typing speed", "Too low. returning: 5");return 5;}
		else {Conversation.printWSln("Typing speed", "returning:"+totalSpeed/m);return charsPerSec;}
	}

	public ContiguousTurn getEnclosingContiguousTurn(Turn t) {
		for (int i = 0; i < contiguousTurns.size(); i++) {
			ContiguousTurn ct = (ContiguousTurn) contiguousTurns.elementAt(i);
			Vector ctturns = ct.getTurns();
			for (int j = 0; j < ctturns.size(); j++) {
				Turn ctt = (Turn) ctturns.elementAt(j);
				if (ctt == t) {
					return ct;
				}
			}
		}
		return null;
	}

	public void closeDown() {
		try {
			turns = null;
			contiguousTurns = null;
			pw = null;
		} catch (Exception e) {

		}
	}

	public ContiguousTurn getPriorContiguousTurnOfConversant(
			String nameOfConversant, Turn t) {
		ContiguousTurn ct = this.getEnclosingContiguousTurn(t);
		int idx = contiguousTurns.indexOf(ct);
		for (int i = idx; i >= 0; i--) {
			ContiguousTurn ct2 = (ContiguousTurn) contiguousTurns.elementAt(i);
			if (ct2.getSender().getUsername()
					.equalsIgnoreCase(nameOfConversant)) {
				return ct2;
			}
		}
		return null;
	}









        

        public synchronized void saveDataToFile(String type, long onset, long enter,
			String senderID, String senderUsername, String apparentSenderUsername, String text,
			Vector recipientsNames, boolean wasBlocked, Vector keyPresses,
			Vector documentUpdates, int turnNumber, Vector stringData) {
	       if( setStartTimeOnFirstMessage&& this.startTime <-999999998){
                   this.startTime = onset;
               }
              

		
		Vector recipients = getConversantsAddingIfNecessary(recipientsNames);
		int recipConversantsSize = recipients.size();

		// ----------------------------------------------------------------------------

                
                Turn t = new DataToBeSaved(this, type,onset, enter, senderID, senderUsername, apparentSenderUsername, text,
				recipients, wasBlocked, keyPresses, documentUpdates,
				null, null, turnNumber, stringData);

                
                this.convIO.saveTurn(t);
                
	}





}

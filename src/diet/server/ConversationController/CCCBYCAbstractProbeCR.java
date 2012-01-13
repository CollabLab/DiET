package diet.server.ConversationController;

import java.util.Date;
import java.util.Vector;

import diet.client.WYSIWYGDocumentWithEnforcedTurntaking;
import diet.message.MessageCBYCChangeTurntakingStatusConfirm;
import diet.message.MessageCBYCDocChangeFromClient;
import diet.message.MessageCBYCTypingUnhinderedRequest;
import diet.message.MessageKeypressed;
import diet.parameters.ExperimentSettings;
import diet.parameters.IntParameter;
import diet.server.Conversation;
import diet.server.Participant;
import diet.server.CbyC.FloorHolderCRProcessing;
import diet.server.CbyC.Sequence.RecordedSequenceFromApparentOrigin;

public abstract class CCCBYCAbstractProbeCR extends CCCBYCDefaultController implements UseOfPrerecordedFakeTurn{
	
	int numberOfParticipants=2;
	long startTimeOfFragSending = -1;
	long finishTimeOfFragSending = -1;
	int speakerChangesSinceLastIntervention = 0;
	String frag = null;
	String probeCondition;
	
	final String[] acks = { "ok", "okay", "right", "alright", "oh ok", "uhuh",
	"uhu" };
	final String[] qbotAcks={"thanks!", "ok", "thank you", "okay"};
	boolean qbot=false;
	boolean interventionTriggered = false;
	boolean receivingResponse = false;
	FloorHolderCRProcessing fhc;
	Participant fragAppOrigin = null;
	IntParameter timeToWaitForResponse = new IntParameter(
			"Frag Response Time Out", 7);// in seconds
	IntParameter typingTimeOutBeforeSendingAck=new IntParameter("timeOutBeforeAck", 3);
	IntParameter minimumSpeakerChangesBetweenInterventions=new IntParameter("speakerChangesBetweenInterventions", 2);
	long finishTimeOfFakeTurnSending = -1;
	public void initialize(Conversation c, ExperimentSettings expS) {
		super.initialize(c, expS);
		super.setIsTypingTimeOut(200);
		super.setProcessLoopSleepTime(80);
		this.expSettings.addParameter(this.timeToWaitForResponse);
		this.expSettings.addParameter(this.typingTimeOutBeforeSendingAck);
		this.expSettings.addParameter(this.minimumSpeakerChangesBetweenInterventions);
		IntParameter np=(IntParameter)this.expSettings.getParameter("Number of participants per conversation");
		this.probeCondition="";
		this.numberOfParticipants=np.getValue();
		super.fh=null;
		fhc = new FloorHolderCRProcessing(this);
		fhc.sS = this.sS;
	}
	
	
	//this should be called by any pre-recorded fake sequence/turn to notify this conversation controller
	//that the sending off of fake turn was finished, so it can be dealt with according to whatever intervention
	//is being programmed

	public synchronized void finishedSendingFakeTurn(long finishTime) {
		this.finishTimeOfFakeTurnSending = finishTime;
		Conversation.printWSln("CCCbyCProbe",
				"finish time of fake turn is set to:" + finishTime);
		notifyAll();
	}
	
	public synchronized boolean triggerFragSending(String antecedentOwner,
			String frag) {
		Conversation.printWSln("CCCbyCProbe", "Intervention Triggered");
		if (this.speakerChangesSinceLastIntervention < this.minimumSpeakerChangesBetweenInterventions
				.getValue()) {
			Conversation
					.printWSln("CCCbyCProbe",
							"Intervention aborted: Not enough turns since the previous.");
			return false;
		}
		interventionTriggered = true;
		this.speakerChangesSinceLastIntervention = 0;
		
		setFragAppOrig(this.chooseFragApparentOrigin(antecedentOwner));
		fhc.sendingFrag(true);
		this.frag = frag;
		Conversation.printWSln("CCCbyCProbe", "frag is:"+frag);
		return true;
		

	}
	
	protected void setFragAppOrig(Participant appOrig) {
		this.fragAppOrigin = appOrig;
		this.fhc.fragAppOrig = appOrig;
	}
	long networkErrorInterval=120000;
    long lastNetworkErrorTime=new Date().getTime();
    boolean waitingOnNetworkError=false;
    private boolean captureResponse=true;
    protected boolean sendRandNetError=true;
    protected void sendRandNetworkError()
    {
        if (this.fragAppOrigin==null)
        {
            
            //send error if time has come
            if (new Date().getTime()-lastNetworkErrorTime>networkErrorInterval)
                     {
                     
                        
                        Vector participants=c.getParticipants().getAllActiveParticipants();
                        for(Object p:participants)
                        	c.sendAndEnableLabelDisplayToALLStatusWindowsOfParticipant((Participant)p, "Network Error. Please wait..", true, false);
                        lastNetworkErrorTime=new Date().getTime();
                        networkErrorInterval=240000+r.nextInt(60000);
                        waitingOnNetworkError=true;
                     
                        try{Thread.sleep(4000+r.nextInt(2000));}catch (Exception e){e.printStackTrace();}
                        for(Object p: participants)
                        {
                        	c.sendAndEnableLabelDisplayToALLStatusWindowsOfParticipant((Participant)p, "Status: OK", false, true);
                        	
                        }
                        this.resetToNormal();//fhc.openFloorAfterTimeOut(super.getIsTypingTimeOut());
                        waitingOnNetworkError=false;
     
                        
                    }
            else return;
        }
        else
        {
            //don't send error. wait until next time(reduced interval).
            lastNetworkErrorTime=new Date().getTime();
            networkErrorInterval=20000+r.nextInt(20000);
    
        }
        
    }
    public void processCBYCChangeTurnTakingStatusConfirm(Participant sender,
			MessageCBYCChangeTurntakingStatusConfirm mConf) {
		if (getC().getParticipants().getAllParticipants().size() != this.numberOfParticipants)
			return;

		fhc.processStateChangeConfirm(sender, mConf);
	}
    protected abstract Participant chooseFragApparentOrigin(String antecedentOwner);
    
    public void processCBYCTypingUnhinderedRequest(Participant sender,
			MessageCBYCTypingUnhinderedRequest mWTUR) {
		if (getC().getParticipants().getAllParticipants().size() != this.numberOfParticipants)
			return;

		Conversation.printWSln("FloorHolder", "Processing Floor request from"
				+ sender.getUsername());
		
		if (this.waitingOnNetworkError)
			return;

		fhc.processFloorRequest(sender, mWTUR);
	}
    
    public void processCBYCDocChange(Participant sender,
			MessageCBYCDocChangeFromClient mCDC) {
		if (getC().getParticipants().getAllParticipants().size() != 2
				|| this.waitingOnNetworkError)
			return;

		if (!interventionTriggered || !(this.finishTimeOfFragSending < 0)) {
			sS.addDocChange(mCDC);
		}
	}
    
    protected void resetToNormal(){
	
	fhc.openFloorResetAllToNormal();
	this.receivingResponse = false;
	startTimeOfFragSending = -1;
	finishTimeOfFragSending = -1;
	this.interventionTriggered = false;
	this.setFragAppOrig(null);
	
    }
    protected void sendFakeAckAndResetToNormalWhenDone() {
		fhc.sendingFrag(true);
		String ack;
		if (!this.qbot) ack= this.acks[r.nextInt(acks.length)];
		else ack=this.qbotAcks[r.nextInt(this.qbotAcks.length)];
		
		fhc.changeFloorStatusOfParticipantsNoPrefix(this.getC()
				.getParticipants().getAllOtherParticipants(fragAppOrigin),
				WYSIWYGDocumentWithEnforcedTurntaking.othertyping);
		long typingSpeed = this.getC().getHistory()
				.getAverageTypingSpeedCharsPerSecond(
						fragAppOrigin.getUsername(), 10);
		RecordedSequenceFromApparentOrigin rs;
		if (!this.qbot)
		{
				rs = new RecordedSequenceFromApparentOrigin(
				sS, this, ack, typingSpeed, this.getC().getParticipants()
						.getAllOtherParticipants(fragAppOrigin), fragAppOrigin,
				this.probeCondition);
				rs.createHeader();
		}
		else
		{
			rs = new RecordedSequenceFromApparentOrigin(
					sS, this, ack, 10, this.getC().getParticipants()
							.getAllOtherParticipants(fragAppOrigin), "Q-BOT",
					this.probeCondition);
			rs.createHeader();
			
		}

		sS.addSequence(rs);
		fhc.forceInformOthersOfTyping(fragAppOrigin);

		try {

			synchronized (this) {

				Conversation.printWSln("CCCbyCProbe","starting to wait for ack output");
				wait();

				Conversation.printWSln("CCCbyCProbe",
						"Finished waiting for ack to be sent");

			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		// reset to normal:
		fhc.openFloorResetAllToNormal();
		this.receivingResponse = false;
		startTimeOfFragSending = -1;
		finishTimeOfFragSending = -1;
		this.interventionTriggered = false;
		this.setFragAppOrig(null);

	}
    public void setCaptureResponse(boolean b)
    {
	this.captureResponse=b;
    }
    public void processLoop() {

		if (getC().getParticipants().getAllParticipants().size() != 2)
			return;
		long now = new Date().getTime();
		if (!this.interventionTriggered) {
			fhc.openFloorAfterTimeOut(super.getIsTypingTimeOut());// normal
			// operation
		} else {
			if (this.startTimeOfFragSending < 0) {
				intervene();
			} else {
				if (this.finishTimeOfFakeTurnSending > 0) {
					if (receivingResponse) {
						if (fhc.nobodyTypingAndFloorIsOpen(this.typingTimeOutBeforeSendingAck
										.getValue() * 1000)) {
							sendFakeAckAndResetToNormalWhenDone();
						} else {
							fhc.openFloorAfterTimeOut(super
									.getIsTypingTimeOut());
						}

					} else if (now - this.finishTimeOfFragSending > 1000 * this.timeToWaitForResponse
							.getValue()
							&& fhc
									.nobodyTypingAndFloorIsOpen(this.typingTimeOutBeforeSendingAck
											.getValue() * 1000)) {
						// nothing received after frag was sent and the timeout
						// has
						// expired
						fhc.openFloorResetAllToNormal();
						startTimeOfFragSending = -1;
						finishTimeOfFragSending = -1;
						this.receivingResponse = false;
						this.interventionTriggered = false;
						this.setFragAppOrig(null);
						Conversation.printWSln("CCCbyCProbe",
								"opening floor . . waited long enough");

					} else {
						fhc.openFloorAfterTimeOut(super.getIsTypingTimeOut());
					}

				}

			}
		}
		if (this.sendRandNetError) this.sendRandNetworkError();
	}
 // this is part of the processLoop Thread
	public void intervene() {

		fhc.changeFloorStatusOfParticipantsNoPrefix(this.getC()
				.getParticipants().getAllOtherParticipants(fragAppOrigin),
				WYSIWYGDocumentWithEnforcedTurntaking.othertyping);
		this.getC().sendAndEnableLabelDisplayToALLStatusWindowsOfParticipant(
				fragAppOrigin, "Network Error...please wait", true, false);
		long typingSpeed = getC().getHistory()
				.getAverageTypingSpeedCharsPerSecond(
						fragAppOrigin.getUsername(), 5);
		String recording;
		if (this.captureResponse)
		    recording = this.frag + "?";
		else
		    recording = this.frag;
		    
		RecordedSequenceFromApparentOrigin rs;
		if (!this.qbot)
		{
			rs = new RecordedSequenceFromApparentOrigin(
				sS, this, recording, typingSpeed, this.getC().getParticipants()
						.getAllOtherParticipants(fragAppOrigin), fragAppOrigin,
				this.probeCondition);
			rs.createHeader();
		}
		else
		{
			rs = new RecordedSequenceFromApparentOrigin(
					sS, this, recording, 10, this.getC().getParticipants()
							.getAllOtherParticipants(fragAppOrigin), "Q-BOT",
					this.probeCondition);
			rs.createHeader();
		}
		
		sS.addSequence(rs);
		
		fhc.forceInformOthersOfTyping(fragAppOrigin);

		Conversation.printWSln("CCCByCProbe", "Setting frag app orig to:"
				+ fragAppOrigin.getUsername());

		try {

			synchronized (this) {
				this.startTimeOfFragSending = new Date().getTime();
				Conversation.printWSln("CCCByCProbe",
						"starting to wait for frag output");
				wait();
				this.finishTimeOfFragSending = this.finishTimeOfFakeTurnSending;
				Conversation.printWSln("CCCByCProbe",
						"Finished waiting for frag to be sent");

			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		if (this.captureResponse)
		    fhc.openFloorAfterFragSending();
		else
		{
		    this.resetToNormal();
		}

	}
	
	

	

}

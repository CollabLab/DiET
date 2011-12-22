/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package diet.server.CbyC;

import java.util.Random;
import java.util.Vector;

import diet.client.WYSIWYGDocumentWithEnforcedTurntaking;
import diet.debug.Debug;
import diet.message.MessageCBYCChangeTurntakingStatus;
import diet.message.MessageCBYCChangeTurntakingStatusConfirm;
import diet.message.MessageCBYCTypingUnhinderedRequest;
import diet.server.Conversation;
import diet.server.Participant;
import diet.server.ConversationController.CCCBYCDefaultController;
import diet.client.StyleManager;

/**
 * 
 * @author Greg
 */
public class FloorHolder {

	CCCBYCDefaultController cC;
	Conversation c;
	Participant floorHolder;
	public Sequences sS;
	StyleManager sm;
	boolean automaticallyAllowOpenFloorOnIsTypingTimeout = true;
	Random r = new Random();
	long oCounter = 0;
	long cCounter = 0;
        boolean informOthersOfTyping = true;


	public FloorHolder(CCCBYCDefaultController cC) {
		this.cC = cC;
		this.c = cC.getC();
		sm = cC.getStyleManager();

	}

	public long getTurnCount() {
		return oCounter;
	}

	public long getOpenFloorCount() {
		return cCounter;
	}
	public boolean isTheSpeakerChanging(Participant sender,MessageCBYCTypingUnhinderedRequest mCBYCTUR) {
		if (sender == floorHolder) {
			//Conversation.printWSln("FL", "Sender of Floor Request same as current FloorHolder");
			return false;
		}
		DocChange dc = (DocChange) sS.getLastIncoming();
		if (dc == null) {
			return false;
		}
		String lastSender = dc.getSender();
		if (lastSender.equals(sender.getUsername())) {
			if (floorHolder != null && !floorHolder.getUsername().equals(lastSender)) {
				cC.getC().printWln("FL","Definite error.."+ lastSender	+ " should not be able to request the floor while other is typing");
		        }
			return false;
                }
		else	{
			if (sS.isSpeakerChangePermitted(sender, mCBYCTUR)) return true;	
		}	
		return false;
	}

	boolean timeOfFirstFloorRequestRecorded=false;
	long timeOfFirstFloorRequest;
	public synchronized void processFloorRequest(Participant sender, MessageCBYCTypingUnhinderedRequest mCBYCTUR) {
		if (sender == floorHolder) {
			Conversation.printWSln("FL", "Sender of Floor Request same as current FloorHolder");
			if(Debug.showEOFCRSTATES)Conversation.printWSln("Main","Requestor "+sender.getUsername() +"  same as flooholder "+floorHolder.getUsername()+ " "+" so no prefix");
          
                        this.giveFloorToParticipantNoPrefix(sender);
			return;
		}
		DocChange dc = (DocChange) sS.getLastIncoming();
		if (dc == null) {
                        if(Debug.showEOFCRSTATES)Conversation.printWSln("Main","FHSIMPLE1: FloorHolder is null");

			boolean speakerChangePermittedAndPerformed = sS.processSpeakerChangeRequest_SetNewSequence(sender, mCBYCTUR);
                        if(Debug.showEOFCRSTATES)Conversation.printWSln("Main","FHSIMPLE2: Speakerchange= "+speakerChangePermittedAndPerformed);
			// cC.getC().printWln("FloorHolder", "Trying2");
			if (speakerChangePermittedAndPerformed) {
				giveFloorToParticipantPrefix(sender);
				cC.getC().printWln("FloorHolder", "Trying3");
                                if(Debug.showEOFCRSTATES)Conversation.printWSln("Main","FHSIMPLE3 Performed it");
			}
			return;
		}
		String lastSender = dc.getSender();
		cC.getC().printWln("FL", "Last Doc Change was from:"+lastSender);
		if (lastSender.equals(sender.getUsername())) {
			if (floorHolder != null) {
				cC.getC().printWln("FL","Possible error...is yielding floor to "+ sender.getUsername()	+ " this shouldn't be really happening");
			}
			if (floorHolder != null && !floorHolder.getUsername().equals(lastSender)) {
				cC.getC().printWln("FL","Definite error.."+ lastSender	+ " should not be able to request the floor while other is typing");
				return;
			}
			giveFloorToParticipantNoPrefix(sender);
		} else {
			boolean speakerChangePermittedAndPerformed = sS.processSpeakerChangeRequest_SetNewSequence(sender, mCBYCTUR);
			if (speakerChangePermittedAndPerformed) {
				if (mCBYCTUR.getOffsetFrmRight() == 0) {
                                    giveFloorToParticipantPrefix(sender);
				} else {
				    giveFloorToParticipantNoPrefix(sender);
				}

			}
		}

	}

	public void processStateChangeConfirm(Participant sender, MessageCBYCChangeTurntakingStatusConfirm mConf) {

	}
	public synchronized void changeFloorStatusOfParticipantPrefix(Participant p, int newStatus) {
		DocInsert prefix;
		if (diet.debug.Debug.showPrefixBeforeCBYCTurn) {
			prefix = sm.getPrefixForParticipant("" + oCounter + "fh", p,p);
		} else {
			prefix = sm.getPrefixForParticipant("", p, p);
		}
		diet.message.MessageCBYCChangeTurntakingStatus mcbyc = new MessageCBYCChangeTurntakingStatus("server", "server", newStatus, prefix);
		p.sendMessage(mcbyc);
	}
	
	protected synchronized void giveFloorToParticipantPrefix(Participant sender) {
		Vector v = cC.getC().getParticipants().getAllOtherParticipants(sender);
		for (int i = 0; i < v.size(); i++) {
			Participant p = (Participant) v.elementAt(i);
			diet.message.MessageCBYCChangeTurntakingStatus mcbyc = new MessageCBYCChangeTurntakingStatus(
					"server", "server",WYSIWYGDocumentWithEnforcedTurntaking.othertyping,
					DocInsert.createEmpty());
			p.sendMessage(mcbyc);
		}
		DocInsert prefix;
		
		if (diet.debug.Debug.showPrefixBeforeCBYCTurn) {
			prefix = sm.getPrefixForParticipant("" + oCounter + "fh", sender,sender);
		} else {
			prefix = sm.getPrefixForParticipant("", sender, sender);
		}
		MessageCBYCChangeTurntakingStatus mcbyc = new MessageCBYCChangeTurntakingStatus("server", "server",WYSIWYGDocumentWithEnforcedTurntaking.typingunhindered, prefix);
		sender.sendMessage(mcbyc);
		
		if (Debug.showCounterAfterCBYCStatusMessage) {
			cC.getC().sendAndEnableLabelDisplayToALLStatusWindowsOfParticipant(sender, "You are typing " + oCounter+"B", true, true);
		} else {
			cC.getC().sendAndEnableLabelDisplayToALLStatusWindowsOfParticipant(sender, "You are typing", true, true);
		}
		// /cC.getC().sendLabelDisplayToAllowedParticipantsFromApparentOrigin(sender,sender.getUsername()+"....is typing",true);
		if(this.informOthersOfTyping)this.forceInformOthersOfTyping(sender);
		//Conversation.printWSln("FL", "not informing others of typing");
		oCounter++;
		floorHolder = sender;
	}

	int infCounter = 0;

        public void setInformOthersOfTyping(boolean setInform){
            this.informOthersOfTyping=setInform;
        }

       

	public void forceInformOthersOfTyping(Participant actualTypist) {
		//if(!this.informOthersOfTyping) return;//This check has been moved into giveFloorToParticipantPrefix and NoPrefix
		infCounter++;
		Vector v = cC.getC().getParticipants().getAllOtherParticipants(actualTypist);
		for (int i = 0; i < v.size(); i++) {
			Participant p = (Participant) v.elementAt(i);
			String statusWindowDisplay = sS.getStatusWindowTextForRecipient(p.getUsername());
			if (Debug.showCounterAfterCBYCStatusMessage) {
				cC.getC().sendAndEnableLabelDisplayToALLStatusWindowsOfParticipant(p, statusWindowDisplay + infCounter+"C", true, true);
			} else {
				cC.getC().sendAndEnableLabelDisplayToALLStatusWindowsOfParticipant(p, statusWindowDisplay, true, true);
			}
		}
	}

	public synchronized void changeFloorStatusOfParticipantsNoPrefix(Vector participants, int newStatus) {
		for (int i = 0; i < participants.size(); i++) {
			Participant p = (Participant) participants.elementAt(i);
			diet.message.MessageCBYCChangeTurntakingStatus mcbyc = new MessageCBYCChangeTurntakingStatus("server", "server", newStatus, DocInsert.createEmpty());
			p.sendMessage(mcbyc);
		}
	}

	public synchronized void changeFloorStatusOfParticipantNoPrefix(Participant p, int newStatus) {
		diet.message.MessageCBYCChangeTurntakingStatus mcbyc = new MessageCBYCChangeTurntakingStatus("server", "server", newStatus, DocInsert.createEmpty());
		p.sendMessage(mcbyc);
	}

	protected synchronized void giveFloorToParticipantNoPrefix(Participant sender) {
                //THIS METHOD ASSUMES THAT ALL OF THE CLIENTS ARE IN NOONEELSETYPING MODE AND ONE OF THE PARTICIPANTS
                // HAS WON THE BATTLE FOR THE FLOOR.

		Vector v = cC.getC().getParticipants().getAllOtherParticipants(sender);
		for (int i = 0; i < v.size(); i++) {
			Participant p = (Participant) v.elementAt(i);
			diet.message.MessageCBYCChangeTurntakingStatus mcbyc = new MessageCBYCChangeTurntakingStatus(
					"server", "server",
					WYSIWYGDocumentWithEnforcedTurntaking.othertyping,
					DocInsert.createEmpty());
			p.sendMessage(mcbyc);
		}

		DocInsert prefix = sm.getPrefixForParticipant("", sender, sender);
		MessageCBYCChangeTurntakingStatus mcbyc = new MessageCBYCChangeTurntakingStatus(
				"server", "server",
				WYSIWYGDocumentWithEnforcedTurntaking.typingunhindered,
				DocInsert.createEmpty());
		sender.sendMessage(mcbyc);
		if (Debug.showCounterAfterCBYCStatusMessage) {
			cC.getC().sendAndEnableLabelDisplayToALLStatusWindowsOfParticipant(sender, "You are typing" + oCounter+"D", true, true);
		} else {
			cC.getC().sendAndEnableLabelDisplayToALLStatusWindowsOfParticipant(sender, "You are typing", true, true);

		}
		// cC.getC().sendLabelDisplayToAllowedParticipantsFromApparentOrigin(sender,sender.getUsername()+"....is typing",true);
		if(this.informOthersOfTyping)this.forceInformOthersOfTyping(sender);
		//Conversation.printWSln("FL", "not informing others of typing");
		oCounter++;
		floorHolder = sender;
	}






	public void setAutomaticallyAllowOpenFloorAfterIsTypingTimeout(boolean normalOperation) {
		this.automaticallyAllowOpenFloorOnIsTypingTimeout = normalOperation;
	}

	public synchronized void openFloorAfterTimeOut(long timeout) {
		if (!automaticallyAllowOpenFloorOnIsTypingTimeout)
			return;
		if (floorHolder != null) {

			if (floorHolder.isTyping(timeout)) {
				return;
			}

			if (!sS.canFloorBeRelinquished())
				return;

			// System.err.println("TIME OF LAST TYPING: "+floorHolder.getConnection().getTimeOfLastTyping()+
			// "..."+(new
			// Date().getTime()-floorHolder.getConnection().getTimeOfLastTyping()));

			Vector prts = cC.getC().getParticipants().getAllParticipants();
			for (int i = 0; i < prts.size(); i++) {
				Participant p = (Participant) prts.elementAt(i);
				Conversation.printWSln("Open Floor","sending turntaking message to:" + p.getUsername());
				MessageCBYCChangeTurntakingStatus mcbycOther = new MessageCBYCChangeTurntakingStatus(
						"server", "server", WYSIWYGDocumentWithEnforcedTurntaking.nooneelsetyping,null);
				p.sendMessage(mcbycOther);
				if (Debug.showCounterAfterCBYCStatusMessage) {
					cC.getC().sendLabelDisplayToAllParticipantsInTheirOwnStatusWindow("Please type" + cCounter+"D", false);
				} else {
					cC.getC().sendLabelDisplayToAllParticipantsInTheirOwnStatusWindow("Please type", false);
					
				}
			}
			cCounter++;
			floorHolder = null;
		}

	}

	public boolean floorIsOpen() {
		return floorHolder == null;
	}

	

	

}

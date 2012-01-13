/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package diet.server.CbyC;

import diet.client.CBYCDocumentWithEnforcedTurntaking;
import java.util.Vector;

import diet.debug.Debug;
import diet.message.MessageCBYCChangeTurntakingStatus;
import diet.message.MessageCBYCChangeTurntakingStatusConfirm;
import diet.message.MessageCBYCTypingUnhinderedRequest;
import diet.server.Conversation;
import diet.server.Participant;
import diet.server.ConversationController.CCCBYCDefaultController;
import java.util.Date;

/**
 *
 * @author Greg
 */
public class FloorHolderAdvanced extends FloorHolder{

    
    
    public FloorHolderAdvanced(CCCBYCDefaultController cC) {
        super(cC);
    }
    
    

    
    
    
    

    

    @Override
    public void forceInformOthersOfTyping(Participant actualTypist) {
        super.forceInformOthersOfTyping(actualTypist);
    }


    public synchronized void forciblyOpenFloor(){
        if(floorHolder!=null){
            this.openFloorAfterTimeOut(0);
        }
        else{
            Vector prts = cC.getC().getParticipants().getAllParticipantsNewCopy();
            super.changeFloorStatusOfParticipantsNoPrefix(prts, CBYCDocumentWithEnforcedTurntaking.nooneelsetyping);
        }
    }


    public synchronized void processStateChangeConfirm(Participant sender,MessageCBYCChangeTurntakingStatusConfirm mConf){
        System.err.println("RECEIVED "+mConf.getId()+" "+sender.getUsername());
        Conversation.printWSln("FL","RECEIVED "+mConf.getId()+" "+sender.getUsername());
        
        if(mConf.getNewStatus()== CBYCDocumentWithEnforcedTurntaking.nooneelsetyping &&  sender==floorHolder){
             //
             this.floorOpeningReceivedConfID=mConf.getId();
             notifyAll();
         }
        else{
           System.err.println("RECEIVED WAS NOT THE RIGHT ONE: "+mConf.getId()+" "+sender.getUsername()); 
           Conversation.printWSln("FL","RECEIVED WAS NOT THE RIGHT ONE: "+mConf.getId()+" "+sender.getUsername());
        
        }
         
    }



    


    boolean floorOpeningAbortedDueToIncomingFloorRequest=false;
    boolean floorOpeningReceivedConf = false;
    long floorOpeningReceivedConfID = 0;


    //This is pretty much exactly the same as openFloorAfterTimeOut
    public synchronized void forceOpenFloorAfterTimeOut(long timeout){
        boolean oldState = this.automaticallyAllowOpenFloorOnIsTypingTimeout;
        this.automaticallyAllowOpenFloorOnIsTypingTimeout=true;
        openFloorAfterTimeOut(timeout);
        this.automaticallyAllowOpenFloorOnIsTypingTimeout=oldState;
    }





    @Override
    public synchronized void openFloorAfterTimeOut(long timeout) {
        if(!automaticallyAllowOpenFloorOnIsTypingTimeout) {
        	if(Debug.showFloorHolderAdvancedInfo)Conversation.printWSln("Open Floor", "Not opening floor due to not automatically open floor");
        	return;
        }
        if(floorHolder!=null)  {
			if (floorHolder.isTyping(timeout)) {
				if(Debug.showFloorHolderAdvancedInfo)Conversation.printWSln("Open Floor", "Not opening floor due floor holder still typing");
				return;
			}
			if (!sS.canFloorBeRelinquished())
			
			{
				if(Debug.showFloorHolderAdvancedInfo)Conversation.printWSln("Open Floor", "Not opening floor due to SS saying floor can't be relinquished");
				return;
			}
			long id = new Date().getTime();
			MessageCBYCChangeTurntakingStatus mcbyc = new MessageCBYCChangeTurntakingStatus("server","server",diet.client.CBYCDocumentWithEnforcedTurntaking.nooneelsetyping,null, id);
			floorHolder.sendMessage(mcbyc);
			floorOpeningReceivedConf = false;
			while (!floorOpeningReceivedConf && !floorOpeningAbortedDueToIncomingFloorRequest && automaticallyAllowOpenFloorOnIsTypingTimeout) {
				try {
					System.err.println("WAITINGFOR " + id + " "+ floorHolder.getUsername());
					Conversation.printWSln("FL", "WAITINGFOR " + id + " "	+ floorHolder.getUsername());
					wait();
					if (this.floorOpeningReceivedConfID == id) {
						System.err.println("WAITINGFORFOUND " + id + " "+ floorHolder.getUsername());
						Conversation.printWSln("FL", "WAITINGFORFOUND " + id + " " + floorHolder.getUsername());
						floorOpeningReceivedConf = true;
					}
				} catch (Exception e) {
					e.printStackTrace();
					this.c.saveErrorLog(e);

				}

			}
			if (floorOpeningAbortedDueToIncomingFloorRequest) {
				floorOpeningAbortedDueToIncomingFloorRequest = false;
				return;
			}

			System.err.println("WAITINGFORFOUNDCONFIRMEXIT " + id + " " + floorHolder.getUsername());
			Conversation.printWSln("FL", "WAITINGFORFOUNDCONFIRMEXIT " + id+ " " + floorHolder.getUsername());
			Vector prts = cC.getC().getParticipants().getAllOtherParticipants(floorHolder);
			for (int i = 0; i < prts.size(); i++) {
				Participant p = (Participant) prts.elementAt(i);
				if(Debug.showFloorHolderAdvancedInfo)Conversation.printWSln("Open Floor","sending turntaking message to:" + p.getUsername());
				MessageCBYCChangeTurntakingStatus mcbycOthers = new MessageCBYCChangeTurntakingStatus(
						"server","server",diet.client.CBYCDocumentWithEnforcedTurntaking.nooneelsetyping,null);
				p.sendMessage(mcbycOthers);
				if (Debug.showCounterAfterCBYCStatusMessage) {
					cC.getC().sendLabelDisplayToAllParticipantsInTheirOwnStatusWindow("Please type" + cCounter +"A", false);
				} else {
					cC.getC().sendLabelDisplayToAllParticipantsInTheirOwnStatusWindow("Please type", false);
				}
			}
			floorHolder = null;
			cCounter++;       
         }         
    }


    boolean isProcessing=false;
    @Override
    public synchronized void processFloorRequest(Participant sender, MessageCBYCTypingUnhinderedRequest mCBYCTUR) {
        if(!this.mayParticipantRequestFloor(sender))return;
        Conversation.printWSln("FL","processingfloorrequest from "+sender.getUsername());
        if(Debug.showEOFCRSTATES)Conversation.printWSln("Main","processingfloorrequest from "+sender.getUsername());
        isProcessing=true;
        if(sender==floorHolder){
            floorOpeningAbortedDueToIncomingFloorRequest=true;
            Conversation.printWSln("FL","INITIATED ABORT OF FLOOR CHANGE TO NOONE.."+sender.getUsername());   
            Conversation.printWSln("Main","Previous FloorHolder has retaken the floor..."+sender.getUsername());
            super.changeFloorStatusOfParticipantsNoPrefix(c.getParticipants().getAllOtherParticipants(sender),diet.client.CBYCDocumentWithEnforcedTurntaking.othertyping);
            super.changeFloorStatusOfParticipantNoPrefix(sender, diet.client.CBYCDocumentWithEnforcedTurntaking.typingunhindered);
            return;
        }           
        else if(floorHolder!=null){
            //If floorHolder has the floor and someone requests it - deny the request and tell them to stop asking
            if(Debug.showEOFCRSTATES)Conversation.printWSln("Main","+sender.getUsername() "+" requested floor, but not permitted because "+floorHolder.getUsername()+ " "+" has floor");
            super.changeFloorStatusOfParticipantNoPrefix(sender, diet.client.CBYCDocumentWithEnforcedTurntaking.othertyping);
            return;
        }
        //Nobody currently has the floor, and the floor is up for grabs...
        if(Debug.showEOFCRSTATES){
            if(floorHolder==null){
                   Conversation.printWSln("Main","FLOORHOLDER IS NULL");
            }else{
                  Conversation.printWSln("Main", "FLOORHOLDER IS NOT NULL..."+floorHolder.getUsername());
            }
        }
        

        super.processFloorRequest(sender, mCBYCTUR);
        isProcessing=false;
    }
    
    public boolean isTheSpeakerChanging(Participant sender, MessageCBYCTypingUnhinderedRequest mCBYCTUR)
    {
    	if (sender==this.floorHolder) return false;
    	else if (this.floorHolder!=null) return false;
    	
    	return super.isTheSpeakerChanging(sender, mCBYCTUR);
    	
    }
    public boolean isProcessing()
    {
    	return this.isProcessing;
    }

    @Override
    public void setAutomaticallyAllowOpenFloorAfterIsTypingTimeout(boolean normalOperation) {
        super.setAutomaticallyAllowOpenFloorAfterIsTypingTimeout(normalOperation);
    }





    Vector blackListOfFloorRequestors = new Vector();

    private boolean mayParticipantRequestFloor(Participant p){
        if (!this.blackListOfFloorRequestors.contains(p))return true;
        return false;
    }

    public synchronized void allowAllIncomingFloorRequests(boolean withReset){
        
        this.blackListOfFloorRequestors= new Vector();
        this.openFloorAfterTimeOut(0);

        super.changeFloorStatusOfParticipantsNoPrefix(cC.getC().getParticipants().getAllParticipants(), CBYCDocumentWithEnforcedTurntaking.nooneelsetyping);
        if(withReset)this.reset();


    }

    private void reset(){
        sS.setNextSequenceIsSpeakerChange();
        floorHolder = null;
    }
    
    //This will only work properly if the floor is already relinquished
    //...otherwise this method might do some strange stuff
    public synchronized void blockAllIncomingFloorRequests(boolean withReset){
         this.blackListOfFloorRequestors = cC.getC().getParticipants().getAllParticipantsNewCopy();
         this.openFloorAfterTimeOut(0);
         if(withReset){
            this.reset();
         }
         
    }

    
    public synchronized void onlyAllowParticipantToRequestFloor(Participant p, boolean withReset){
        this.openFloorAfterTimeOut(0);
        Vector vParts = cC.getC().getParticipants().getAllParticipantsNewCopy();
        vParts.removeElement(p);
        this.blackListOfFloorRequestors=vParts;
        if(withReset){
            this.reset();
         }
    }

    
    public void addParticipantToBlackList(Participant p){
        this.blackListOfFloorRequestors.addElement(p);
    }
    public void removeParticipantFromBlackList(Participant p){
        this.blackListOfFloorRequestors.removeElement(p);
    }

    public synchronized void fullyBlock(){
        fullyBlock("Network or Game Error...recalculating index..please wait");
    }

    public synchronized void fullyBlock(String s){
        Vector v = this.c.getParticipants().getAllParticipants();
        this.setInformOthersOfTyping(false);
        this.blockAllIncomingFloorRequests(isProcessing);
        for(int i = 0;i<v.size();i++){
            Participant p = (Participant)v.elementAt(i);
            String suffx = "";
            if(Debug.showCounterAfterCBYCStatusMessage)suffx="NOTCOUNTING-RESUME";
            c.getParticipants().sendAndEnableLabelDisplayToParticipant(p, s+suffx, true, false);
            this.changeFloorStatusOfParticipantNoPrefix(p, CBYCDocumentWithEnforcedTurntaking.nooneelsetyping);
        }

    }


    public synchronized void resumeNormalOperation(){
        this.openFloorAfterTimeOut(0);
        reset();
        this.allowAllIncomingFloorRequests(true);
        this.setInformOthersOfTyping(true);
        this.setAutomaticallyAllowOpenFloorAfterIsTypingTimeout(true);
        Vector v = this.c.getParticipants().getAllParticipants();
        for(int i = 0;i<v.size();i++){
            Participant p = (Participant)v.elementAt(i);
            String suffx = "";
            if(Debug.showCounterAfterCBYCStatusMessage)suffx="NOTCOUNTING-RESUME";
            c.getParticipants().sendAndEnableLabelDisplayToParticipant(p, "Please type"+suffx, false, true);
            this.changeFloorStatusOfParticipantNoPrefix(p, CBYCDocumentWithEnforcedTurntaking.nooneelsetyping);
        }

    }
  

}

package diet.server.ConversationController;
import java.util.Date;
import java.util.Vector;

import diet.message.MessageChatTextFromClient;
import diet.message.MessageKeypressed;
import diet.message.MessageWYSIWYGDocumentSyncFromClientInsert;
import diet.message.MessageWYSIWYGDocumentSyncFromClientRemove;
import diet.message.MessageWYSIWYGTextSelectionFromClient;
import diet.parameters.ExperimentSettings;
import diet.parameters.StringListParameter;
import diet.server.Conversation;
import diet.server.Participant;
import edu.stanford.nlp.ling.TaggedWord;
import edu.stanford.nlp.trees.Tree;




public class CCFRAG extends DefaultConversationController{
    
    public StringListParameter acknowledgments;
  
    
    public CCFRAG(){      
    }
    
    public void initialize(Conversation c, ExperimentSettings expSettings){
        super.initialize(c, expSettings);
        acknowledgments =  (StringListParameter)expSettings.getParameter("Acknowledgments");
    }
    
   
    
    @Override
    public void processLoop(){
        if(!processingCRSubdialogue){
             c.resetFromIsTypingtoNormalChatAllAllowedParticipants(super.getIsTypingTimeOut());      
        }
        else if (processingCRSubdialogue&&new Date().getTime()-timeOfCRSending>35000&&recipientOfCR!=null){
            if(!recipientOfCR.isTyping(15000)){           
              c.sendAndEnableLabelDisplayToAllowedParticipantsFromApparentOrigin(recipientOfCR, "Status: OK",false,true);
              processingCRSubdialogue=false;
            }  
        }      
    }
    
   
    boolean processingCRSubdialogue = false;
    Participant recipientOfCR;
    Participant apparentOrigin;
    long timeOfCRSending =0;
    
    @Override
    public void processChatText(Participant sender,MessageChatTextFromClient mct){    
        
       
        String nounToFrag = "";
        boolean nounFoundToFrag = false;


        //if(mct.getText().e)
        




        //nounFoundToFrag = false;
        //processingCRSubdialogue =false;
        if(nounFoundToFrag & !processingCRSubdialogue){
            c.relayTurnToAllOtherParticipants(sender,mct);
            processingCRSubdialogue = true;    
            long avgTypingSpeed = c.getHistory().getAverageTypingSpeedForLastNTurns(sender.getUsername(),5);
            if(avgTypingSpeed<=0)avgTypingSpeed = 3;
            Vector allOtherParticipants = c.getParticipants().getAllOtherParticipants(sender);
            apparentOrigin = (Participant)allOtherParticipants.elementAt(0);
            try{Thread.sleep(1000+ r.nextInt(200));}catch (Exception e){}
            c.informParticipantBthatParticipantAIsTyping(apparentOrigin,sender);
            c.sendAndEnableLabelDisplayToAllowedParticipantsFromApparentOrigin(sender, "Network error..please wait",true,false);         
            //c.sendLabelDisplayToAllowedParticipantsFromApparentOrigin(sender,sender.getUsername()+"Network error..please wait",true);
            try{Thread.sleep(2500+ 1000*(nounToFrag.length()/avgTypingSpeed));}catch (Exception e){}
            //c.setNewTurnBeingConstructedNotRelayingOldTurnAddingOldTurnToHistory(sender, mct);
            c.sendArtificialTurnFromApparentOriginToRecipient(apparentOrigin,sender,nounToFrag+"?");
            timeOfCRSending = new Date().getTime();
            recipientOfCR = sender;
            c.informParticipantBthatParticipantAIsNotTypingAndStatusIsOK(apparentOrigin,sender);
        }
        else if (processingCRSubdialogue && this.recipientOfCR == sender){
            //Receiving response to CR
            
            int ackType = r.nextInt(acknowledgments.getValue().size());
            String ack = (String) ((Vector) acknowledgments.getValue()).elementAt(ackType);
            long avgTypingSpeed = c.getHistory().getAverageTypingSpeedForLastNTurns(sender.getUsername(),5);
            try{Thread.sleep(1000+ r.nextInt(200));}catch (Exception e){}
            c.informParticipantBthatParticipantAIsTyping(apparentOrigin,sender);
            if(avgTypingSpeed<=0)avgTypingSpeed = 3;
            try{Thread.sleep(1000+ 1000*(nounToFrag.length()/avgTypingSpeed));}catch (Exception e){}
            c.setNewTurnBeingConstructedNotRelayingOldTurnAddingOldTurnToHistory(sender, mct);
            c.sendArtificialTurnFromApparentOriginToRecipient(apparentOrigin,sender,ack);   
            c.sendAndEnableLabelDisplayToAllowedParticipantsFromApparentOrigin(sender, "Status: OK",false,true);
            c.informParticipantBthatParticipantAIsNotTypingAndStatusIsOK(apparentOrigin,sender);
            processingCRSubdialogue=false;
            recipientOfCR=null;
            //
        } 
        else{
           c.relayTurnToAllOtherParticipants(sender,mct);
        }   
           //c.sendLabelDisplayToAllowedParticipantsFromApparentOrigin(sender,"Status: OK",false);
       
       
    }
    
    @Override
    public void processKeyPress(Participant sender,MessageKeypressed mkp){
        //c.informIsTypingToAllowedParticipants(sender);//,mkp);
    }

    
    @Override
    public void processWYSIWYGTextInserted(Participant sender,MessageWYSIWYGDocumentSyncFromClientInsert mWYSIWYGkp){
        if(this.processingCRSubdialogue && this.recipientOfCR==sender){
             //c.informIsTypingToAllowedParticipants(sender);
        }
        else if(!processingCRSubdialogue){
             c.informIsTypingToAllowedParticipants(sender);
        }
           
           
 
    }
    
    @Override
    public void processWYSIWYGTextRemoved(Participant sender,MessageWYSIWYGDocumentSyncFromClientRemove mWYSIWYGkp){
        //c.relayWYSIWYGTextRemovedToAllowedParticipants(sender,mWYSIWYGkp);
        //turnBeingConstructed.remove(mWYSIWYGkp.getOffset(),mWYSIWYGkp.getLength(),mWYSIWYGkp.getTimeStamp().getTime()); 
        //chOut.addMessage(sender,mWYSIWYGkp);
    }
    
    @Override
    public void processWYSIWYGSelectionChanged(Participant sender, MessageWYSIWYGTextSelectionFromClient mWYSIWYGSel){
        c.relayWYSIWYGSelectionChangedToAllowedParticipants(sender,mWYSIWYGSel);
    }
}

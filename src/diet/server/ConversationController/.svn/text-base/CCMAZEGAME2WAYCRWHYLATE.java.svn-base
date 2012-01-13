package diet.server.ConversationController;
import java.util.Date;
import java.util.Vector;

import client.MazeGameController2WAY;
import diet.message.MessageChatTextFromClient;
import diet.message.MessageKeypressed;
import diet.message.MessageWYSIWYGDocumentSyncFromClientInsert;
import diet.message.MessageWYSIWYGDocumentSyncFromClientRemove;
import diet.message.MessageWYSIWYGTextSelectionFromClient;
import diet.parameters.ExperimentSettings;
import diet.parameters.IntParameter;
import diet.server.Conversation;
import diet.server.Participant;
import diet.server.conversationhistory.turn.MAZEGAMETURN;
import diet.server.conversationhistory.turn.Turn;
import diet.textmanipulationmodules.CyclicRandomTextGenerators.CyclicRandomAcknowledgmentGenerator;
import diet.textmanipulationmodules.CyclicRandomTextGenerators.CyclicRandomCRClarifyingIntentions;
import diet.textmanipulationmodules.mazegame_location_description_detection.PhraseFilter;




public class CCMAZEGAME2WAYCRWHYLATE  extends DefaultConversationController{
    
   
    boolean processingCRSubdialogue = false;
    Participant recipientOfCR;
    Participant apparentOrigin;
    long timeOfCRSending =0;
    CyclicRandomCRClarifyingIntentions cycCRINT;
    CyclicRandomAcknowledgmentGenerator cycACK = new  CyclicRandomAcknowledgmentGenerator();
    PhraseFilter pf = new PhraseFilter();
    
    
    IntParameter lowerBoundForInter = new IntParameter("Lower bound for intervention (different participant)",12);
    IntParameter lowerBoundForIntra = new IntParameter("Lower bound for intervention (same participant)",24);
    IntParameter turnsSinceLastIntervention = new IntParameter("Turns elapsed since last intervention",5);
    
    Participant lastOriginOfCRDowngrade=null;
   
    
    
    
   public void initialize(Conversation c,ExperimentSettings expSett){
       super.initialize(c, expSett);
       cycCRINT = new CyclicRandomCRClarifyingIntentions();
       expSettings.addParameter(lowerBoundForIntra);
       expSettings.addParameter(lowerBoundForInter);
       expSettings.addParameter(this.turnsSinceLastIntervention);
      
   }
    
   long timeOfNextRandomNetworkError = r.nextInt(20000)+new Date().getTime();
   boolean carryingOutFakeError = false;
   
   public void processLoop(){
        if(!processingCRSubdialogue&&c.getParticipants().getAllParticipants().size()>1){
             c.resetFromIsTypingtoNormalChatAllAllowedParticipants(super.getIsTypingTimeOut());
             long currTime = new Date().getTime();
             if(currTime>this.timeOfNextRandomNetworkError){
                 Vector v = c.getParticipants().getAllParticipants();
                 Participant pRecipientError = (Participant)v.elementAt(r.nextInt(v.size()));
                 if(!pRecipientError.isTyping(500)){
                        carryingOutFakeError = true;
                        if(!processingCRSubdialogue){
                              c.sendAndEnableLabelDisplayToALLStatusWindowsOfParticipant(pRecipientError, "Network error..please wait", true, false);
                              try{
                                  Thread.sleep(500+r.nextInt(1500));    
                              }catch (Exception e){ }
                              c.sendAndEnableLabelDisplayToALLStatusWindowsOfParticipant(pRecipientError, "Status: OK", false, true);
                              timeOfNextRandomNetworkError = 10000+r.nextInt(50000)+new Date().getTime();
                        }
                        carryingOutFakeError=false;
                 }
             }    
        }
        else if (processingCRSubdialogue&&new Date().getTime()-timeOfCRSending>35000&&recipientOfCR!=null){
            if(!recipientOfCR.isTyping(15000)){ 
              c.sendAndEnableLabelDisplayToAllowedParticipantsFromApparentOrigin(recipientOfCR, "Status: OK",false,true);
              processingCRSubdialogue=false;
              turnsSinceLastIntervention.setValue(0);
            }  
        }
        
        
    }
    
   
 
 
boolean processingChatText = false;
   
@Override
public void processChatText(Participant sender,MessageChatTextFromClient mct){    
  processingChatText = true;
  pTurnsElapsed.setValue(((Integer)pTurnsElapsed.getValue())+1);
  super.expSettings.generateParameterEvent(pTurnsElapsed);
        
  MazeGameController2WAY mgc = (MazeGameController2WAY)c.getTaskController();
  int mazeNo = mgc.getMazeNo();
  int moveNo= mgc.getMoveNo();
  int indivMveNo = mgc.getParticipantsMoveNo(sender);
        
        
  if(  mazeNo>5 &&
          
       !this.carryingOutFakeError&&!processingCRSubdialogue&&
    ((  lastOriginOfCRDowngrade==null&&pTurnsElapsed.getValue()>10)
    ||(lastOriginOfCRDowngrade==sender&&lowerBoundForIntra.getValue()<turnsSinceLastIntervention.getValue())
    ||(lastOriginOfCRDowngrade!=sender && turnsSinceLastIntervention.getValue()>lowerBoundForInter.getValue()))){
                
                //String s = pf.getAClarificationFastest(mct.getText());  
                //boolean nounFoundToFrag = false;
                boolean nounFoundToFrag = true;
                String crText = this.cycCRINT.getNext(sender);
                //c.relayTurnToAllOtherParticipants(sender,mct);
                c.relayMazeGameTurnToAllOtherParticipants(sender, mct, mazeNo, moveNo, indivMveNo);
                if(nounFoundToFrag){
                     processingCRSubdialogue = true;    
                     long avgTypingSpeed = c.getHistory().getAverageTypingSpeedForLastNTurns(sender.getUsername(),5);
                     if(avgTypingSpeed<=0)avgTypingSpeed = 3;
                     Vector allOtherParticipants = c.getParticipants().getAllOtherParticipants(sender);
                     apparentOrigin = (Participant)allOtherParticipants.elementAt(0);
                     try{c.sleep(1000+ r.nextInt(200));}catch (Exception e){e.printStackTrace();}
                     c.informParticipantBthatParticipantAIsTyping(apparentOrigin,sender);
                     
                        c.sendAndEnableLabelDisplayToAllowedParticipantsFromApparentOrigin(sender, "Network error..please wait",true,false);         
                        //c.sendLabelDisplayToAllowedParticipantsFromApparentOrigin(sender,sender.getUsername()+"Network error..please wait",true);
                        try{Thread.sleep(1500+ 1000*(crText.length()/avgTypingSpeed));}catch (Exception e){}
                        
                        if(!sender.isTyping(1400)){
                     
                        //c.setNewTurnBeingConstructedNotRelayingOldTurnAddingOldTurnToHistory(sender, mct);
                        c.sendArtificialTurnFromApparentOriginToRecipient(apparentOrigin,sender,crText+"?");
                        timeOfCRSending = new Date().getTime();
                        recipientOfCR = sender;
                        lastOriginOfCRDowngrade = sender;
                        c.informParticipantBthatParticipantAIsNotTypingAndStatusIsOK(apparentOrigin,sender);
                     }
                     else{
                         processingChatText=false;
                         c.sendAndEnableLabelDisplayToAllowedParticipantsFromApparentOrigin(sender, "Status: OK",false,true);
                         c.sendAndEnableLabelDisplayToALLStatusWindowsOfParticipant(sender, "Status: OK",false,true);
                         c.sendAndEnableLabelDisplayToAllowedParticipantsFromApparentOrigin(sender, "Status: OK",false,true);
                         this.processingCRSubdialogue=false;
                         return;
                     }
                 }
                 processingChatText = false;
                return;
        }
        else if(this.processingCRSubdialogue){
            
            String ack = cycACK.getNext(sender);
            long avgTypingSpeed = c.getHistory().getAverageTypingSpeedForLastNTurns(sender.getUsername(),5);
            try{Thread.sleep(700+ r.nextInt(200));}catch (Exception e){}
            c.informParticipantBthatParticipantAIsTyping(apparentOrigin,sender);
            if(avgTypingSpeed<=0)avgTypingSpeed = 3;
            try{c.sleep(1200+ 1000*(ack.length()/avgTypingSpeed));}catch (Exception e){e.printStackTrace();}
            c.setNewMazeGameTurnBeingConstructedNotRelayingOldTurnAddingOldTurnToHistory(sender, mct, mazeNo, moveNo, indivMveNo);
            c.sendArtificialTurnFromApparentOriginToRecipient(apparentOrigin,sender,ack);   
            c.sendAndEnableLabelDisplayToAllowedParticipantsFromApparentOrigin(sender, "Status: OK",false,true);
            c.informParticipantBthatParticipantAIsNotTypingAndStatusIsOK(apparentOrigin,sender);
            processingCRSubdialogue=false;
            turnsSinceLastIntervention.setValue(0);
            recipientOfCR=null;
            processingChatText = false;
            return;
        }
        c.relayMazeGameTurnToAllOtherParticipants(sender, mct, mazeNo, moveNo, indivMveNo);
        turnsSinceLastIntervention.setValue(turnsSinceLastIntervention.getValue()+1);
        super.expSettings.generateParameterEvent(turnsSinceLastIntervention);
        processingChatText = false;
           
           
           
           
           
   
          
    }
    
    @Override
    public void processKeyPress(Participant sender,MessageKeypressed mkp){
        //c.informIsTypingToAllowedParticipants(sender);//,mkp);
    }

    
    @Override
    public void processWYSIWYGTextInserted(Participant sender,MessageWYSIWYGDocumentSyncFromClientInsert mWYSIWYGkp){
         if(!this.processingCRSubdialogue){ 
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
        //c.relayWYSIWYGSelectionChangedToAllowedParticipants(sender,mWYSIWYGSel);
    }

    @Override
    public Turn getTurnTypeForIO() {
        return new MAZEGAMETURN();
    }
    
   
   public void reload(){
       cycCRINT = new CyclicRandomCRClarifyingIntentions();
   }

   
    
    
   

   

}

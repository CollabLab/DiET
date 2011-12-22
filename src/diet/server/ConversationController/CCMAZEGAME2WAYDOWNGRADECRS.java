package diet.server.ConversationController;
import java.util.Random;
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
import diet.server.conversationhistory.turn.ContiguousTurn;
import diet.server.conversationhistory.turn.MAZEGAMETURN;
import diet.server.conversationhistory.turn.Turn;
import diet.textmanipulationmodules.CRDetector.CRDetector;
import diet.textmanipulationmodules.CyclicRandomTextGenerators.CyclicRandomCROpenClassGenerator;




public class CCMAZEGAME2WAYDOWNGRADECRS  extends DefaultConversationController{
    
    
    boolean isCompleted;
    Random r = new Random();
   
      
    
    //IntParameter lowerBoundForIntra = new IntParameter("Lower bound for intervention (same participant)",24);
    //IntParameter lowerBoundForInter = new IntParameter("Lower bound for intervention (different participant)",12);
    
    IntParameter lowerBoundForIntra = new IntParameter("Lower bound for intervention (same participant)",2);
    IntParameter lowerBoundForInter = new IntParameter("Lower bound for intervention (different participant)",2);
    
    IntParameter turnsSinceLastIntervention = new IntParameter("Turns elapsed since last intervention",0);
    
    Participant lastOriginOfCRDowngrade=null;
    CRDetector crd = new CRDetector(this);
    CyclicRandomCROpenClassGenerator crgt = new CyclicRandomCROpenClassGenerator();
    
    
    
    
    public void initialize(Conversation c,ExperimentSettings expSettings){
        super.initialize(c, expSettings);
        expSettings.addParameter(this.lowerBoundForIntra);
        expSettings.addParameter(lowerBoundForInter);
        expSettings.addParameter(this.turnsSinceLastIntervention);
    }
    
    @Override
    public void processLoop(){
             c.resetFromIsTypingtoNormalChatAllAllowedParticipants(super.getIsTypingTimeOut());      
    }
    
   
    
    @Override
    public void processChatText(Participant sender,MessageChatTextFromClient mct){    
       
        MazeGameController2WAY mgc = (MazeGameController2WAY)c.getTaskController();
        int mazeNo = mgc.getMazeNo();
        int moveNo= mgc.getMoveNo();
        int indivMveNo = mgc.getParticipantsMoveNo(sender);
        
        pTurnsElapsed.setValue(((Integer)pTurnsElapsed.getValue())+1);
        super.expSettings.generateParameterEvent(pTurnsElapsed);
           
        
        if(     (lastOriginOfCRDowngrade==null && lowerBoundForInter.getValue()<turnsSinceLastIntervention.getValue()) 
              ||(lastOriginOfCRDowngrade==sender&&lowerBoundForIntra.getValue()<turnsSinceLastIntervention.getValue())
              || (lastOriginOfCRDowngrade!=null&&lastOriginOfCRDowngrade!=sender && lowerBoundForInter.getValue() < turnsSinceLastIntervention.getValue())){
                        
                ContiguousTurn ct = c.getHistory().getMostRecentContiguousTurn();
                String ctApparentsender=ct.getApparentSender().getUsername();
                boolean identifiedCR = false;
                if(ctApparentsender.equalsIgnoreCase(sender.getUsername())){
                    //It is a contiguous turn continuation
                    String otherSpeaker = c.getHistory().getNameOfImmediatelyPriorApparentSpeaker();
                    ContiguousTurn ctPrior = c.getHistory().getMostRecentContiguousTurnOfConversant(otherSpeaker);
                    if(ctPrior==null){
                        identifiedCR=crd.isCR("", ct.getTextString(), mct.getText());
                    }
                    else{
                        identifiedCR=crd.isCR(ctPrior.getTextString(), ct.getTextString(), mct.getText());
                    }
                }
                else{
                    //It is a new turn being constructed by a new speaker
                    identifiedCR=crd.isCR(ct.getTextString(), "", mct.getText());
                }
                if(identifiedCR){
                   //c.setNewTurnBeingConstructedNotRelayingOldTurnAddingOldTurnToHistory(sender, mct);
                   c.setNewMazeGameTurnBeingConstructedNotRelayingOldTurnAddingOldTurnToHistory(sender, mct, mazeNo, moveNo, indivMveNo);
                   Vector otherParticipants = c.getParticipants().getAllOtherParticipants(sender);
                   String textReplaced = crgt.getNext(sender);
                   c.sendArtificialTurnFromApparentOriginToRecipients(sender, otherParticipants, textReplaced);  
                   turnsSinceLastIntervention.setValue(0);
                   this.lastOriginOfCRDowngrade=sender;
                   Conversation.printWSln("Main", "CR Downgrade of turn from "+this.lastOriginOfCRDowngrade.getUsername()+": "+mct.getText()+" to "+textReplaced);
                   return;
                }
                
                
        }
        
        
        turnsSinceLastIntervention.setValue(turnsSinceLastIntervention.getValue()+1);
        super.expSettings.generateParameterEvent(turnsSinceLastIntervention);
        c.relayMazeGameTurnToAllOtherParticipants(sender,mct,mazeNo,moveNo,indivMveNo);
        c.sendLabelDisplayToAllowedParticipantsFromApparentOrigin(sender,"Status: OK",false);
           
       
       
    }
    
    @Override
    public void processKeyPress(Participant sender,MessageKeypressed mkp){
        //c.informIsTypingToAllowedParticipants(sender);//,mkp);
    }

    
    @Override
    public void processWYSIWYGTextInserted(Participant sender,MessageWYSIWYGDocumentSyncFromClientInsert mWYSIWYGkp){
          c.informIsTypingToAllowedParticipants(sender);
       
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
    
   
    
   
    
   

   

}

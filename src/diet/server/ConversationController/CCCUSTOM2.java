package diet.server.ConversationController;
import diet.message.MessageChatTextFromClient;
import diet.message.MessageKeypressed;
import diet.message.MessageWYSIWYGDocumentSyncFromClientInsert;
import diet.message.MessageWYSIWYGDocumentSyncFromClientRemove;
import diet.message.MessageWYSIWYGTextSelectionFromClient;
import diet.server.Participant;




public class CCCUSTOM2 extends DefaultConversationController{
   
    
        
    @Override
    public void processLoop(){
            
             c.resetFromIsTypingtoNormalChatAllAllowedParticipants(super.getIsTypingTimeOut());      
              
    }
    
   
  
    
    
    @Override
    public void processChatText(Participant sender,MessageChatTextFromClient mct){    
       
           pTurnsElapsed.setValue(((Integer)pTurnsElapsed.getValue())+1);
           super.expSettings.generateParameterEvent(pTurnsElapsed);
     
           c.relayTurnToAllOtherParticipants(sender,mct);
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
    
   
   


    
    
   

   

}

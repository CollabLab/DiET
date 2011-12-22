package diet.server.ConversationController;
import diet.message.MessageChatTextFromClient;
import diet.message.MessageKeypressed;
import diet.message.MessageWYSIWYGDocumentSyncFromClientInsert;
import diet.message.MessageWYSIWYGDocumentSyncFromClientRemove;
import diet.message.MessageWYSIWYGTextSelectionFromClient;
import diet.server.Participant;




public class CCCustom extends DefaultConversationController{
    
    
    
    
    //final int MAX_NUMBER_OF_PARTICIPANTS = 20;
    //final int OWN_AND_OTHER_WINDOW_NUMBERS = PermissionsManager.ONEWINDOWENABLED;
    
    
  
    
    public void processLoop(){
        c.resetFromIsTypingtoNormalChatAllAllowedParticipants(6000);
        
    }
    
    
    public void processChatText(Participant sender,MessageChatTextFromClient mct){
       c.relayTurnToAllOtherParticipants(sender,mct);
       if(mct.getText().equalsIgnoreCase("me")){
          c.sendArtificialTurnFromApparentOriginToRecipient(sender,sender,"what do you mean me?");
       }   
    }
    
    public void processKeyPress(Participant sender,MessageKeypressed mkp){
        c.informIsTypingToAllowedParticipants(sender);
    }
    
    
    Participant pTyping =null;
    
    public void processWYSIWYGTextInserted(Participant sender,MessageWYSIWYGDocumentSyncFromClientInsert mWYSIWYGkp){
        c.informIsTypingToAllowedParticipants(sender);
        c.relayWYSIWYGTextInsertedToAllowedParticipants(sender,mWYSIWYGkp);
    
    }

    
    public void processWYSIWYGTextRemoved(Participant sender,MessageWYSIWYGDocumentSyncFromClientRemove mWYSIWYGkp){
        c.relayWYSIWYGTextRemovedToAllowedParticipants(sender,mWYSIWYGkp);       
    }
    
    
    public void processWYSIWYGSelectionChanged(Participant sender, MessageWYSIWYGTextSelectionFromClient mWYSIWYGSel){
        c.relayWYSIWYGSelectionChangedToAllowedParticipants(sender,mWYSIWYGSel);
    }
    
    





    







}

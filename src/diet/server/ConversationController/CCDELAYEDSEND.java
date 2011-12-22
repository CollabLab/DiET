package diet.server.ConversationController;
import diet.message.MessageChatTextFromClient;
import diet.message.MessageKeypressed;
import diet.message.MessageWYSIWYGDocumentSyncFromClientInsert;
import diet.message.MessageWYSIWYGDocumentSyncFromClientRemove;
import diet.message.MessageWYSIWYGTextSelectionFromClient;
import diet.parameters.ExperimentSettings;
import diet.parameters.IntParameter;
import diet.server.Conversation;
import diet.server.Participant;




public class CCDELAYEDSEND extends DefaultConversationController{
    
   
    IntParameter p2;// = new IntParameter("DelayInMillisecs",3000,3000);
        
    
    
    public CCDELAYEDSEND(){
    }
    
    @Override
    public void initialize(Conversation c,ExperimentSettings expSettings){
        super.initialize(c, expSettings);
        
        p2 = (IntParameter)expSettings.getParameter("Typing status timeout (msecs)");
        //If you want to have more complex "istyping" behaviour, comment out the line above
        //And uncomment the two lines below
        //p2 = new IntParameter("DelayInMillisecs",3000,3000);
        //expSettings.addParameter(p2);        
        
    }
    
    
   
    
    
    
    @Override
    public void processLoop(){
          //This method runs continuously on a separate Thread, constantly checking whether 
          //any participant has not typed in the last p2.getValue() milliseconds
          //This is the same number of milliseconds as the delay in the processChatText method.
          c.resetFromIsTypingtoNormalChatAllAllowedParticipants(p2.getValue());                    
    }
    
   
  
    
    
    @Override
    public void processChatText(Participant sender,MessageChatTextFromClient mct){    
           //This method determines what is done with the incoming chat text messages from the client.
           //It is currently set up so that it treats the turn produced as separate from the turn that is
           //sent as a delay.
        
           c.setNewTurnBeingConstructedNotRelayingOldTurnAddingOldTurnToHistory(sender, mct);
           c.sendDelayedTextToAllOtherParticipants(sender, mct.getText(), p2.getValue());
               
    }
    
    @Override
    public void processKeyPress(Participant sender,MessageKeypressed mkp){
        //c.informIsTypingToAllowedParticipants(sender);//,mkp);
    }

    
    @Override
    public void processWYSIWYGTextInserted(Participant sender,MessageWYSIWYGDocumentSyncFromClientInsert mWYSIWYGkp){
        //This simply sends the "is typing" message to the other clients.  
        c.informIsTypingToAllowedParticipants(sender);   
    }
    
    @Override
    public void processWYSIWYGTextRemoved(Participant sender,MessageWYSIWYGDocumentSyncFromClientRemove mWYSIWYGkp){
        
    }
    
    @Override
    public void processWYSIWYGSelectionChanged(Participant sender, MessageWYSIWYGTextSelectionFromClient mWYSIWYGSel){
        
    }
    
   
   


    
    
   

   

}

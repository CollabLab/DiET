package diet.server.ConversationController;
import bsh.Interpreter;
import diet.message.MessageChatTextFromClient;
import diet.message.MessageKeypressed;
import diet.message.MessageWYSIWYGDocumentSyncFromClientInsert;
import diet.message.MessageWYSIWYGDocumentSyncFromClientRemove;
import diet.message.MessageWYSIWYGTextSelectionFromClient;
import diet.parameters.ExperimentSettings;
import diet.server.Conversation;
import diet.server.Participant;

/*It's not really recommended programming this class...
 */



public class CCBeanShell extends DefaultConversationController{
    
    
    
    
    Interpreter i;
   
    

    public CCBeanShell(Conversation c,ExperimentSettings expSettings,Interpreter i) {
        super();
        super.initialize(c,expSettings);
        this.c = c;
        this.i=i;
        
    }  
    
    public CCBeanShell(){
        
    }
    
    
    
    @Override
    public void processLoop(){
             try{     
                   i.eval("processLoop();");
             }catch(Exception e){
                 System.err.println("ERROR IN PROCESSLOOP: "+e.getMessage().toString());
             }
             //c.resetFromIsTypingtoNormalChatAllAllowedParticipants(super.getIsTypingTimeOut());      
              
    }
    
   
    
    @Override
    public void processChatText(Participant sender,MessageChatTextFromClient mct){   
          try{
             i.set("sender",sender);
             i.set("mct",mct);
           
             i.eval("processChatText(sender,mct);");
             
             
          }catch(Exception e){
              System.err.println("ERROR PROCESSING CHATTEXT: "+e.getMessage().toString());
          }
          //p.setValue(((Integer)p.getValue())+1);
          //super.expSettings.generateParameterEvent(p);
     
          //c.relayTurnToAllowedParticipants(sender,mct);
          //c.sendLabelDisplayToAllowedParticipantsFromApparentOrigin(sender,"Status: OK",false);
       
    }
    @Override
    public void processKeyPress(Participant sender,MessageKeypressed mkp){
        try{
            i.set("sender",sender);
            i.set("mkp",mkp);
            i.eval("processKeyPress(sender,mkp)");
            
        }catch(Exception e){
            System.err.println("Error processing keypress: "+e.getMessage().toString());
        }
        
        
        //c.informIsTypingToAllowedParticipants(sender);//,mkp);
    }

    
    @Override
    public void processWYSIWYGTextInserted(Participant sender,MessageWYSIWYGDocumentSyncFromClientInsert mWYSIWYGkp){
         try{
             i.set("sender",sender);
             i.set("mWYSIWYGkp",mWYSIWYGkp);
             i.eval("processWYSIWYGTextInserted(sender,mWYSIWYGkp);");
         }catch (Exception e){
             System.err.println("ERROR PROCESSING WYSIWYGTEXTINSERTED: "+e.getMessage());
         }
        //  c.informIsTypingToAllowedParticipants(sender);
       
    }
    
    @Override
    public void processWYSIWYGTextRemoved(Participant sender,MessageWYSIWYGDocumentSyncFromClientRemove mWYSIWYGkp){
        //c.relayWYSIWYGTextRemovedToAllowedParticipants(sender,mWYSIWYGkp);
        //turnBeingConstructed.remove(mWYSIWYGkp.getOffset(),mWYSIWYGkp.getLength(),mWYSIWYGkp.getTimeStamp().getTime()); 
        //chOut.addMessage(sender,mWYSIWYGkp);
          try{
             i.set("sender",sender);
             i.set("mWYSIWYGkp",mWYSIWYGkp);
             i.eval("processWYSIWYGTextRemoved(sender,mWYSIWYGkp);");
         }catch (Exception e){
             System.err.println("ERROR PROCESSING WYSIWYGTEXTINSERTED: "+e.getMessage());
         }
        //  c.informIsTypingToAllowedParticipants(sender);
       
        
    }
    
    @Override
    public void processWYSIWYGSelectionChanged(Participant sender, MessageWYSIWYGTextSelectionFromClient mWYSIWYGSel){
         try{
             i.set("sender",sender);
             i.set("mWYSIWYGSel",mWYSIWYGSel);
             i.eval("processWYSIWYGSelectionChanged(sender,mWYSIWYGkp");
         }catch (Exception e){
             System.err.println("ERROR PROCESSING WYSIWYGTEXTINSERTED");
         }
        
        //c.relayWYSIWYGSelectionChangedToAllowedParticipants(sender,mWYSIWYGSel);
    }
    
   
   


    
    
   

   

}

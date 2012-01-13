package diet.server.ConversationController;
import diet.debug.Debug;
import diet.message.MessageChatTextFromClient;
import diet.message.MessageKeypressed;
import diet.message.MessageWYSIWYGDocumentSyncFromClientInsert;
import diet.message.MessageWYSIWYGDocumentSyncFromClientRemove;
import diet.message.MessageWYSIWYGTextSelectionFromClient;
import diet.parameters.ExperimentSettings;
import diet.server.Conversation;
import diet.server.Participant;
import diet.task.react.Game;
import diet.task.react.ReactTaskController;
import java.util.Vector;




public class CCREACT extends DefaultConversationController{

    ReactTaskController rtc= new ReactTaskController(this);
   


    


    @Override
    public void initialize(Conversation c, ExperimentSettings expSettings) {
        super.initialize(c, expSettings);
        c.setTaskController(rtc);
        //rtc = new ReactTaskController(this);
        try{
         Vector v = new Vector(); v.addElement("Buttonname"); v.addElement("X");v.addElement("Y");    v.addElement("LocalTimeOfPress");v.addElement("LocalTimeOfRELEASE");v.addElement("PriorPressBySelf");            v.addElement("PriorreleaseBYSELF");v.addElement("PRIORSELECTBYOTHER");v.addElement("PRIORRELEASEBYOTHER"); v.addElement("evaluation");
         //this.getC().saveDataToFile("MOUSEPRESSFROMCLIENT", "PARTICIPANTID", "USERNAME", new Date().getTime(), new Date().getTime(),"", v);
       }catch (Exception e){
           e.printStackTrace();
       }
      

    }
    
        
    @Override
    public void processLoop(){
             c.resetFromIsTypingtoNormalChatAllAllowedParticipants(super.getIsTypingTimeOut());      
    }

    @Override
    public void participantJoinedConversation(Participant p) {
           //g=rtc.getGame();
          //rtc.participantJoinedConversation(p);
    }



   public void nextSet(){
       this.rtc.nextSet();
   }

   public void gotoSetNo(int i){
       this.rtc.getGame().gotoSetNo(i);
   }
    
   public void setSize(int i){
      this.rtc.setSize(i);     
    }


    public void swap(String id, int size){
      this.rtc.swap(id, size);
    }


    @Override
    public void processChatText(Participant sender,MessageChatTextFromClient mct){    
       
           pTurnsElapsed.setValue(((Integer)pTurnsElapsed.getValue())+1);
           super.expSettings.generateParameterEvent(pTurnsElapsed);
     
           //mct.setText(mct.getText()+"....was wicked");
          

           c.relayTurnToAllOtherParticipants(sender,mct);
           c.sendLabelDisplayToAllowedParticipantsFromApparentOrigin(sender,"Status: OK",false);


           if(Debug.allowCHATCLIENTTOSENDDEBUGCOMMANDS){
               if(mct.getText().startsWith("/n")){
                   this.nextSet();
               }
           }

       
                   
    }
    
    @Override
    public void processKeyPress(Participant sender,MessageKeypressed mkp){
        //c.informIsTypingToAllowedParticipants(sender);//,mkp);
        c.saveKeypressToFile(sender, mkp);

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

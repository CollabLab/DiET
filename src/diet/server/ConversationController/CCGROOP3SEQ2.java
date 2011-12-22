package diet.server.ConversationController;
import diet.message.MessageChatTextFromClient;
import diet.message.MessageKeypressed;
import diet.message.MessageWYSIWYGDocumentSyncFromClientInsert;
import diet.message.MessageWYSIWYGDocumentSyncFromClientRemove;
import diet.message.MessageWYSIWYGTextSelectionFromClient;
import diet.parameters.ExperimentSettings;
import diet.server.Conversation;
import diet.server.Participant;
import diet.task.collabMinitaskProceduralComms.AlphabeticalTask;
import diet.task.collabMinitaskProceduralComms.Move;
import diet.task.collabMinitaskProceduralComms.MoveAND;
import java.util.Vector;




public class CCGROOP3SEQ2 extends DefaultConversationController{
   
    AlphabeticalTask at = new AlphabeticalTask(this);

    @Override
    public void initialize(Conversation c, ExperimentSettings expSettings) {
        super.initialize(c, expSettings);

    }

    @Override
    public void participantJoinedConversation(Participant p) {
        super.participantJoinedConversation(p);
        c.displayNEWWebpage(p, "ID1", "Instructions", "", 500, 300, false,false);
        c.changeWebpageTextAndColour(p, "ID1", "Please wait for other participant to log in", "white", "black");
        if(c.getParticipants().getAllParticipants().size()>1){
            at.startTask ((Participant)c.getParticipants().getAllParticipants().elementAt(0),(Participant)c.getParticipants().getAllParticipants().elementAt(1));

           
        }
    }

    @Override
    public void participantRejoinedConversation(Participant p) {
        super.participantRejoinedConversation(p);
        c.displayNEWWebpage(p, "ID1", "Instructions", "", 300, 300, false,false);
    }


   



    @Override
    public void processLoop(){
            // c.resetFromIsTypingtoNormalChatAllAllowedParticipants(super.getIsTypingTimeOut());
    }





    boolean queryaction = true;
    
    @Override
    public void processChatText(Participant sender,MessageChatTextFromClient mct){            
            pTurnsElapsed.setValue(((Integer)pTurnsElapsed.getValue())+1);
           super.expSettings.generateParameterEvent(pTurnsElapsed);   
           if(mct.getText().startsWith("/")){
                System.err.println("SETTING MCT TO "+mct.getText().substring(1));
                System.exit(-5); 
                at.processSelection(sender, mct.getText());
                c.setNewTurnBeingConstructedNotRelayingOldTurnAddingOldTurnToHistory(sender, mct);
                
                
           }
           Move mostRecent=null;
           if(this.targetRecipient!=null&&isinCRSubdialogue){
              c.setNewTurnBeingConstructedNotRelayingOldTurnAddingOldTurnToHistory(sender, mct);
              return;
           }
           String clarification ="";
           if(this.targetRecipient != null) {
                Object[] allMatches = at.getMostRecentAndAllMovesForString(mct.getText());
                mostRecent = (Move)allMatches[0];
                Vector allmentionedMoves = (Vector)allMatches[1];
                for(int i=0;i<allmentionedMoves.size();i++){
                    Move mv = (Move)allmentionedMoves.elementAt(i);
                    Conversation.printWSln("Main", "IDENTIFIED: "+ i+"): "+mv.getName());
                }
           }


           if(this.targetRecipient != null & sender == this.targetRecipient ) {
                Object[] allMatches = at.getMostRecentAndAllMovesForString(mct.getText());
                mostRecent = (Move)allMatches[0];
                Vector allmentionedMoves = (Vector)allMatches[1];
                for(int i=0;i<allmentionedMoves.size();i++){
                    Move mv = (Move)allmentionedMoves.elementAt(i);
                    Conversation.printWSln("Main", "IDENTIFIED: "+ i+"): "+mv.getName());
                }
                if(onlyForMostRecent && mostRecent != null){
                    clarification = mostRecent.getName()+"?";
                    this.displayInterventionInfo("MOSTRECENT MOVE FOUND -WAITING FOR TIMEOUT");
                }
                else if (!onlyForMostRecent & allmentionedMoves.size()>0){
                    Move m = (Move)allmentionedMoves.elementAt(r.nextInt(allmentionedMoves.size()));
                    clarification = m.getName()+"?";
                }

           }
           if(clarification!=null& !clarification.equalsIgnoreCase("")){
                  c.relayTurnToAllOtherParticipants(sender,mct);
                  final String interventiontext = ""+clarification;
                  clarification ="";
                  Vector v = c.getParticipants().getAllOtherParticipants(sender);
                  Participant pAppOrig = (Participant)v.elementAt(0);
                  c.sendAndEnableLabelDisplayToAllowedParticipantsFromApparentOrigin(sender, "Network problem..recalculating", true, false);
                  this.displayInterventionInfo("MOSTRECENT MOVE FOUND -WAITING FOR TIMEOUT");
                  try{
                     Thread.sleep(2000);
                  }catch (Exception e){}
                  if(sender.isTyping(1500)){
                      this.displayInterventionInfo("ABORTED - THEY TYPED");
                      c.sendAndEnableLabelDisplayToAllowedParticipantsFromApparentOrigin(sender, "Network OK", false, true);
                      return;
                  }

                  Conversation.printWSln("Main", "INTERVENTION IS:"+interventiontext);
                  final Participant senderCopy =sender;
                  Thread t = new Thread (){ public void run(){ try{ displayInterventionInfo("5: "+interventiontext); Thread.sleep(1000);displayInterventionInfo("4: "+interventiontext);
                  Thread.sleep(1000);displayInterventionInfo("3: "+interventiontext);Thread.sleep(1000);displayInterventionInfo("2: "+interventiontext);Thread.sleep(1000);
                  displayInterventionInfo("1: "+interventiontext);Thread.sleep(1000); displayInterventionInfo("0-REENABLED");
                  c.sendAndEnableLabelDisplayToAllowedParticipantsFromApparentOrigin(senderCopy, "Normal text", false, true);
                  isinCRSubdialogue=false;
                  targetRecipient=null;
                  at.jcrs.interventionOVER(); } catch (Exception e) {} } };
                  isinCRSubdialogue=true;
                  c.sendArtificialTurnFromApparentOriginToRecipient(pAppOrig, targetRecipient, interventiontext);
                  t.start();
                  return;
           }
           c.relayTurnToAllOtherParticipants(sender,mct);
           c.sendLabelDisplayToAllowedParticipantsFromApparentOrigin(sender,"Status: OK",false);   
          
           //System.exit(-3);
    }





   // public boolean performIntervention = false;
    public boolean isinCRSubdialogue = false;
    public boolean onlyForMostRecent = false;
    public Participant targetRecipient = null;

    public void setOnlyForMostRecent(boolean onlyForMostRecent) {
        this.onlyForMostRecent = onlyForMostRecent;
    }

    //public void setPerformIntervention(boolean performIntervention) {
      //  this.performIntervention = performIntervention;
    //}

    public void setTargetRecipient(Participant targetRecipient) {
        this.targetRecipient = targetRecipient;
    }

    public void displayInterventionInfo(String interventioninfo){
        try{
            this.at.jcrs.displayInterventionInfo("REMAINING: "+interventioninfo);
        }catch (Exception e){
            System.err.println("nullinterface");
            e.printStackTrace();
        }
    }


    @Override
    public void processKeyPress(Participant sender,MessageKeypressed mkp){
        //c.informIsTypingToAllowedParticipants(sender);//,mkp);
        c.saveKeypressToFile(sender, mkp);
        
    }

  
    @Override
    public void processWYSIWYGTextInserted(Participant sender,MessageWYSIWYGDocumentSyncFromClientInsert mWYSIWYGkp){
          //c.informIsTypingToAllowedParticipants(sender);

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
    
   
   
    public void changeTime(long andTime){
        MoveAND.maxTimeBetweenPerformances=andTime;
    }

    
    
   

   

}

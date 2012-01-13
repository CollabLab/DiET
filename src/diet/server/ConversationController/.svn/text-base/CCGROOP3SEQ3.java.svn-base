package diet.server.ConversationController;
import diet.message.MessageChatTextFromClient;
import diet.message.MessageKeypressed;
import diet.message.MessageWYSIWYGDocumentSyncFromClientInsert;
import diet.message.MessageWYSIWYGDocumentSyncFromClientRemove;
import diet.message.MessageWYSIWYGTextSelectionFromClient;
import diet.parameters.ExperimentSettings;
import diet.server.Conversation;
import diet.server.Participant;
import diet.task.collabMinitaskProceduralComms.Move;
import diet.task.collabMinitaskProceduralComms.MoveAND;
import diet.task.collabMinitaskProceduralComms.ProcLangDetector;
import diet.task.collabMinitaskProceduralComms3.AlphabeticalTask3WAY;
import java.util.Date;
import java.util.Hashtable;
import java.util.Vector;




public class CCGROOP3SEQ3 extends DefaultConversationController{
   
    AlphabeticalTask3WAY at = new AlphabeticalTask3WAY(this);

    @Override
    public void initialize(Conversation c, ExperimentSettings expSettings) {
        super.initialize(c, expSettings);

    }

    @Override
    public void participantJoinedConversation(Participant p) {
        super.participantJoinedConversation(p);
        c.displayNEWWebpage(p, "ID1", "Instructions", "", 430, 540, false,false);
        c.changeWebpageTextAndColour(p, "ID1", "Please wait for other participant to log in", "white", "black");
        if(c.getParticipants().getAllParticipants().size()>2){
            at.startTask ((Participant)c.getParticipants().getAllParticipants().elementAt(0),(Participant)c.getParticipants().getAllParticipants().elementAt(1),(Participant)c.getParticipants().getAllParticipants().elementAt(2));
        }
    }

    int numberOfFragsSent =0;
    int numberOfDelaysPreFragSending =0;


    @Override
    public void participantRejoinedConversation(Participant p) {
        super.participantRejoinedConversation(p);
        c.displayNEWWebpage(p, "ID1", "Instructions", "", 300, 300, false,false);
    }

    public long timeout_beforeonset =1500;
   
    public void setTO(long timeout_beforeonset){
        this.timeout_beforeonset = timeout_beforeonset;
    }

    public void setT(long timeout_whiletyping){
        this.timeout_whiletyping=timeout_whiletyping;
    }
    public long timeout_whiletyping =10000;


    @Override
    public void processLoop(){
            // c.resetFromIsTypingtoNormalChatAllAllowedParticipants(super.getIsTypingTimeOut());
    }

    public void setInterventionStage(String s){
        this.interventionstage=s;
    }
    public synchronized void setQueryTurns(boolean b){
        this.queryturns =b;
    }
    public synchronized void setQueryaction(boolean b){
        this.queryaction=b;
    }


    String interventionstage=istageBOTH;

    public static String istageEARLY = "EARLY";
    public static String istageLATE =  "LATE";
    public static String istageBOTH =  "BOTH";

   /// boolean clarifyproc = false;
    ProcLangDetector pld = new ProcLangDetector();
    public boolean queryaction = false;//false;
   // String actionquery ="";

    public boolean queryturns = true;




    @Override
    public synchronized void processChatText(Participant sender,MessageChatTextFromClient mct){
            pTurnsElapsed.setValue(((Integer)pTurnsElapsed.getValue())+1);
           super.expSettings.generateParameterEvent(pTurnsElapsed);   
           if(mct.getText().startsWith("/")){
                //actionquery = "";
                at.processSelection(sender, mct.getText());
                c.setNewTurnBeingConstructedNotRelayingOldTurnAddingOldTurnToHistory(sender, mct);
                if(!queryaction)return;
 
           }
           

           Move mostRecent=null;
           String clarification ="";
           String haystack = mct.getText();
           if(mct.getText().startsWith("/"))haystack=mct.getText().substring(1);
           //if(mct.getText().startsWith(haystack));

           //System.err.println(haystack);
           //System.exit(-4);


           //search for clarification
           if((queryturns|(mct.getText().startsWith("/")&queryaction)) &this.targetRecipient != null & sender == this.targetRecipient & (
                   (  this.interventionstage==this.istageEARLY & at.getLastSel()==0 ) |
                   (  this.interventionstage==this.istageLATE & at.getLastSel()>0) |
                   (  this.interventionstage==this.istageBOTH) )
                   ) {
               //System.exit(-5);
               Object[] allMatches = at.getMostRecentAndAllMovesForString(haystack);
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
                     //int moveindex = 0;
                     Move m = (Move)allmentionedMoves.elementAt(0);
                    // Move m = (Move)allmentionedMoves.elementAt(r.nextInt(allmentionedMoves.size()));
                    clarification = m.getName()+"?";
                }
           }
           if(clarification!=null& !clarification.equalsIgnoreCase("")){
                  if(!mct.getText().startsWith("/"))c.relayTurnToAllOtherParticipants(sender,mct);
                  if(this.isinCRSubdialogue)return;
                  final String interventiontext = ""+clarification;
                  clarification ="";
                  Vector v = c.getParticipants().getAllOtherParticipants(sender);
                  Participant pAppOrig;
                  if(this.targetAppOrigIsOverhearer){
                      pAppOrig = at.getOverhearer(sender);
                  }
                  else{
                      pAppOrig = at.getOther(sender);
                  }


                  //c.sendAndEnableLabelDisplayToAllowedParticipantsFromApparentOrigin(sender, "Network problem..recalculating", true, false);
                  c.sendAndEnableLabelDisplayToAllOtherParticipantsFromApparentOriginInOwnStatusWindow(sender, "Network problem..recalculating", true, false);
                  //c.sendArtificialTurnToAllOtherParticipants(sender, "THISSHOULD...");
                  //c.sendAndEnableLabelDisplayToAllowedParticipantsFromApparentOrigin(sender, "Network problem..recalculating", true, false);
                  //c.sendLabelDisplayToParticipantInOwnStatusWindow(c, interventiontext, isCompleted);
                  this.numberOfDelaysPreFragSending++;
                  this.displayInterventionInfo("MOSTRECENT MOVE FOUND -WAITING FOR TIMEOUT");
                  try{
                     Thread.sleep(this.timeout_beforeonset);
                  }catch (Exception e){}
                  if(sender.isTyping(this.timeout_beforeonset-500)){
                      this.displayInterventionInfo("ABORTED - THEY TYPED");
                      c.sendAndEnableLabelDisplayToAllowedParticipantsFromApparentOrigin(sender, "Network OK", false, true);
                      return;
                  }
                  this.numberOfFragsSent++;
                  this.setRecordFragSent(sender.getUsername(), at.getLastSel());
                  Conversation.printWSln("Main", "INTERVENTION IS:"+interventiontext);
                  Conversation.printWSln("Main", this.getRecordFragsSent());

                  final Participant senderCopy =sender;
                  
                  Thread t = new Thread (){ public void run(){
                      try{
                          long starttime = new Date().getTime();
                          int seconds =0;
                          while(starttime+timeout_whiletyping  > new Date().getTime()){
                              Thread.sleep(1000);
                              seconds = seconds + 1;
                              int timeremaining =(int) ((starttime+timeout_whiletyping)-(new Date().getTime()))/1000;
                              displayInterventionInfo(timeremaining+": "+interventiontext);
                          }

                      }catch(Exception ee){}


                      try{
                         // displayInterventionInfo("5: "+interventiontext); Thread.sleep(1500);displayInterventionInfo("4: "+interventiontext);
                         // Thread.sleep(1500);displayInterventionInfo("3: "+interventiontext);Thread.sleep(1500);displayInterventionInfo("2: "+interventiontext);Thread.sleep(1500);
                          //displayInterventionInfo("1: "+interventiontext);Thread.sleep(1500); displayInterventionInfo("0-REENABLED");
                         c.sendAndEnableLabelDisplayToAllowedParticipantsFromApparentOrigin(senderCopy, "Normal text", false, true);
                         isinCRSubdialogue=false;
                         targetRecipient=null;
                         at.jcrs.interventionOVER(); }
                     catch (Exception e) {} } };

                       isinCRSubdialogue=true;
                       c.sendArtificialTurnFromApparentOriginToRecipient(pAppOrig, targetRecipient, interventiontext);
                       t.start();
                       return;
           }
           //cnt++;
           if(!mct.getText().startsWith("/")){
              c.relayTurnToAllOtherParticipants(sender,mct);
              c.sendLabelDisplayToAllowedParticipantsFromApparentOrigin(sender,"Status: OK",false);
           }
           //c.sendAndEnableLabelDisplayToAllOtherParticipantsFromApparentOriginInOwnStatusWindow(sender, "Network problem..recalculating"+cnt, false, false);
                  

           //System.exit(-3);
    }
//int cnt =0;


    //private long

   // public boolean performIntervention = false;
    public boolean isinCRSubdialogue = false;
    public boolean onlyForMostRecent = false;
    public Participant targetRecipient = null;
    //public Participant targetAppOrig = null;
    boolean targetAppOrigIsOverhearer = false;
    Hashtable htRecord = new Hashtable();

    public void setRecordFragSent(String recipient, int stge){
       try{
        String stage = this.istageLATE;
        if (stge==0) stage = this.istageEARLY;
        Object o = htRecord.get(recipient+stage);
        if(o==null){
            o = new Integer(0);
            htRecord.put(recipient+stage,o);
        }
        Integer i = ((Integer)o)+1;
        htRecord.put(recipient+stage,i);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public String getRecordFragsSent(){
        String allOfThem ="ALLTHEINTERVENTIONS:\n";
        try{
        Vector v = c.getParticipants().getAllParticipants();
        for(int i=0;i<v.size();i++){
            Participant p = (Participant)v.elementAt(i);
            Object o1 = htRecord.get(p.getUsername()+this.istageEARLY);
            Object o2 = htRecord.get(p.getUsername()+this.istageLATE);
            if(o1==null){
                o1 =0;
                htRecord.put(p.getUsername()+this.istageEARLY, new Integer(0));
            }
            if(o2==null){
                o2=0;
                htRecord.put(p.getUsername()+this.istageLATE, new Integer(0));
            }
            allOfThem = allOfThem+ "--"+p.getUsername()+"  EARLY: "+((Integer)o1)+"....LATE: "+((Integer)o2)+"\n";

        }
        }catch (Exception e){
            e.printStackTrace();
        }
        return allOfThem;

    }

    public void setOnlyForMostRecent(boolean onlyForMostRecent) {
        this.onlyForMostRecent = onlyForMostRecent;
    }

    //public void setPerformIntervention(boolean performIntervention) {
      //  this.performIntervention = performIntervention;
    //}

    public void setTargetRecipient(Participant targetRecipient) {
        this.targetRecipient = targetRecipient;
    }

    public void setApparentOriginIsOverhearer(boolean b){
       this.targetAppOrigIsOverhearer=b;
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

    
    
   public void swapBWithC(){
       at.swapBWithC();
   }
   public void swapAWithB(){
       at.swapAWithC();
   }



   public void blockC(){
       c.sendLabelDisplayAndEnableToParticipantInOwnStatusWindow(at.getC(), "Network problem...recalculating", true, false);
   }

   public void unblockC(){
      c.sendLabelDisplayAndEnableToParticipantInOwnStatusWindow(at.getC(), "Network OK", false, true);

   }



}

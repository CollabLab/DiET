package diet.server.ConversationController;
import client.MazeGameController2WAY;
import diet.debug.Debug;
import diet.message.MessageCBYCDocChangeFromClient;
import diet.message.MessageCBYCTypingUnhinderedRequest;
import diet.parameters.ExperimentSettings;
import diet.parameters.IntParameter;
import diet.server.Conversation;
import diet.server.Participant;
import diet.server.CbyC.DocChange;
import diet.server.CbyC.FloorHolderAdvanced;
import diet.server.CbyC.Sequence.MazeGameAcknowledgement;
import diet.server.CbyC.Sequence.MazeGameCapturesSequenceWaitingForTypingLull;
import diet.server.CbyC.Sequences;
import diet.server.CbyC.Sequence.MazeGameClarificationRequest;
import diet.server.CbyC.Sequence.MazeGameDefaultSequence;
import diet.server.CbyC.Sequence.MazeGameDummyBlockingOnTextEntry;
import diet.server.CbyC.Sequence.Sequence;
import diet.server.conversationhistory.turn.CBYCMAZEGAMETURN;
import diet.server.conversationhistory.turn.Turn;
import diet.textmanipulationmodules.CyclicRandomTextGenerators.CyclicRandomAcknowledgmentGenerator;
import diet.textmanipulationmodules.CyclicRandomTextGenerators.CyclicRandomCRClarifyingIntentions;
import java.util.Date;
import java.util.Hashtable;



public class CCCBYCMazeGameFakeEndOfTurnCRGeneratorEARLYLATE extends CCCBYCMazeGameFakeEdit{


    IntParameter lowerBoundForInter = new IntParameter("Lower bound for intervention (different participant)",12);
    IntParameter lowerBoundForIntra = new IntParameter("Lower bound for intervention (same participant)",24);
    IntParameter turnsSinceLastIntervention = new IntParameter("Turns elapsed since last intervention",5);

    Participant lastRecipientOfCR=null;



    CyclicRandomCRClarifyingIntentions cycCRIN_participant1 = new CyclicRandomCRClarifyingIntentions();
    //CyclicRandomCRClarifyingIntentions cycCRIN_participant2 = new CyclicRandomCRClarifyingIntentions();

    CyclicRandomAcknowledgmentGenerator cycACK = new  CyclicRandomAcknowledgmentGenerator();

    FloorHolderAdvanced fh2 = (FloorHolderAdvanced)fh;

    @Override
    public void initialize(Conversation c, ExperimentSettings expSettings) {
        super.initialize(c, expSettings);
        super.setIsTypingTimeOut(250);
        super.setProcessLoopSleepTime(80);
        //expSettings.addParameter(lowerBoundForIntra);
        //expSettings.addParameter(lowerBoundForInter);
        expSettings.addParameter(this.turnsSinceLastIntervention);

    }





    @Override
    public void processCBYCTypingUnhinderedRequest(Participant sender, MessageCBYCTypingUnhinderedRequest mWTUR) {
        if(c.getParticipants().getAllParticipants().size()<2)return;
        synchronized(this){
            if(state==1)return;
        }
        super.processCBYCTypingUnhinderedRequest(sender, mWTUR);
    }

    int maxInterventionsPerMaze = 3;

    Hashtable htLastTraversalCount = new Hashtable();
    MazeGameController2WAY mgc  = (MazeGameController2WAY)this.getC().getTaskController();
    int[] participant1mazeInterventions = new int[12];
    int[] participant2mazeInterventions = new int[12];


    public boolean isCR_OK(Participant p){
        MazeGameController2WAY mgc  = (MazeGameController2WAY)this.getC().getTaskController();
        int currentTraversalCount = mgc.getSwitchTraversalCount(p.getUsername());
        Integer oldTraversalCount = (Integer)this.htLastTraversalCount.get(p);
        if(oldTraversalCount==null)return true;
        if(oldTraversalCount>=currentTraversalCount)return false;

        int idx = this.getC().getParticipants().getAllParticipants().indexOf(p);
        if(idx==0){
            if (this.participant1mazeInterventions[mgc.getMazeNo()]>=maxInterventionsPerMaze) return false;
            return true;
        }
        else if(idx==1){
            if(this.participant2mazeInterventions[mgc.getMazeNo()]>=maxInterventionsPerMaze) return false;
            return true;
        }
       return true;
    }


    public void performedIntervention(Participant p){
        htLastTraversalCount.put(p,mgc.getSwitchTraversalCount(p.getUsername()));
        int idx = this.getC().getParticipants().getAllParticipants().indexOf(p);
        if(idx==0){
            participant1mazeInterventions[mgc.getMazeNo()]++;
            return;
        }
        else if(idx==1){
           participant2mazeInterventions[mgc.getMazeNo()]++;
           return;
        }


    }











    //int hasNOTTYPED =0;
    public void setState(int i){
        this.state=i;
        timeOfLastStateChange = new Date().getTime();
    }

    private int state = 0 ;
    long timeOfLastStateChange;


    //0 = normal
    //1= sending off intervention..
    //2 = has sent off CR, waiting for first key press of response (MUST BE SENT BY SEQUENCE - AFTER FIRST KEYPRESS -
    //3 = is capturing response, waiting for delay
    //4 = delay triggered ...sending acknowledgment....
    //5 = Should be done /// not necessary

    //This must be tested by making sure it works if they press ENTER.

    Participant apparentOrigin;
    MazeGameCapturesSequenceWaitingForTypingLull mgcSWFT ;
    public Participant mgcSWFTREcipient;


    long timeoutCR_RESP = 10000;
    long timeoutRESP_ACK = 4000;
    long timeOfLastState3DocChange = new Date().getTime();

    int ackDelayMEAN = 100;
    int ackDelayPLUSMINUS = 30;

    int crDelayMEAN = 100;
    int crDelayPLUSMINUS = 30;

    int createDummyIntervention = 40;
    long dummyInterventionMaxLength = 3000;

    @Override
    public void processLoop(){
        FloorHolderAdvanced fh2 = (FloorHolderAdvanced)fh;
        long currTime = new Date().getTime();
        if(state==0){
            fh.openFloorAfterTimeOut(super.getIsTypingTimeOut());
        }
        else if(state == 1)
        {
            ///Server is sendinding intervention
        }
        else if (state==2){
            ///Server is waiting for first keypress of response
            ///This is the gap between sending of intervention AND waiting for first keypress by participant, which should be the response
            ///We need to ensure that if participant doesn't respond for whatever reason, it will open floor to other participant after timeout..
            System.err.println("COUNTING DOWN TO RESPONSE" + ((currTime-this.timeOfLastStateChange)-this.timeoutCR_RESP));
            synchronized(this){
                ///
                if(currTime-this.timeOfLastStateChange>this.timeoutCR_RESP){
                    //fh2.allowAllIncomingFloorRequests(true);
                    //fh2.setAutomaticallyAllowOpenFloorAfterIsTypingTimeout(true);
                    //h2.forciblyOpenFloor();
                    fh2.resumeNormalOperation();
                    String addn = "-";
                    if(Debug.showEOFCRSTATES)addn=" ((NORESPONSE: ABORTED-SERVERSTATE FROM 2 to 0))";
                    getC().getParticipants().sendAndEnableLabelDisplayToParticipant(apparentOrigin, "Please type: "+addn, false, true);
                    getC().getParticipants().sendAndEnableLabelDisplayToParticipant(this.mgcSWFTREcipient, "Please type: "+addn, false, true);
                    setState(0);
                }
            }
        }
        else if(state==3) { // Server is receiving the response, waiting for a lull so that it can send an acknowledgment
            System.err.println("COUNTING DOWN TO ACK" + ((currTime-this.timeOfLastState3DocChange)-this.timeoutRESP_ACK));
            System.err.println("COUNTING DOWN:"+(currTime-timeOfLastState3DocChange));


            synchronized(mgcSWFT){
                       //first docchange has come through....and there has been a pause....
                       if(timeOfLastState3DocChange>0&&(currTime-this.timeOfLastState3DocChange>this.timeoutRESP_ACK)){
                           fh2.blockAllIncomingFloorRequests(true);
                           fh2.forciblyOpenFloor();
                           Conversation.printWSln("Main", "COUNTED DOWN FROM RESPONSE TO ACK");
                           MazeGameAcknowledgement mga = new MazeGameAcknowledgement( sS, this, this.mgcSWFTREcipient.getUsername(), this.apparentOrigin.getUsername(), this.cycACK.getNext(this.mgcSWFTREcipient), this.ackDelayMEAN, this.ackDelayPLUSMINUS);
                           mga.setType("ACK");
                           sS.addRecordedSequenceFromApparentOrigin(mga);
                           setState(4);

                }
            }

            //MUST ADD A RESET SO THAT THE PARTICIPANT DCAN REQUEST THE FLOOR...


        }

        //
    }

    @Override
    public Turn getTurnTypeForIO(){
        return new CBYCMAZEGAMETURN();
    }

    @Override
    public void processCBYCDocChange(Participant sender, MessageCBYCDocChangeFromClient mCDC) {
        super.processCBYCDocChange(sender, mCDC);
        if(state==3)this.timeOfLastState3DocChange = new Date().getTime();
        System.err.println("The state on the server is: "+state);
    }



    boolean forceIntervention = true;

    @Override
    public synchronized Sequence getNextSequence_Speaker_Change(Sequence prior,int sequenceNo, Sequences ss, Participant sender, MessageCBYCTypingUnhinderedRequest mCTUR) {
           //return new Sequence(sS,this,p.getUsername(),mCTUR);

        if(Debug.showEOFCRSTATES)Conversation.printWSlnLog("Main", sender.getUsername()+" is requesting turnSPEAKERCHANGE, the state is: "+this.state);
        if(state==2){
            mgcSWFT = new MazeGameCapturesSequenceWaitingForTypingLull(sS,this, sender.getUsername(),mCTUR);
            mgcSWFT.setType("RESPONSE_NEXTSEQUENCESPEAKERCHANGE");
            this.mgcSWFTREcipient = sender;
            setState(3);
            this.timeOfLastState3DocChange = new Date().getTime();
            Conversation.printWSln("MODOS","(B)MazeGameCapturesSequenceWaitingForTypingLull:"+sender.getUsername());
            return mgcSWFT;
        }





         if(r.nextInt(this.createDummyIntervention)==1) {
            Participant pRecipient = (Participant)getC().getParticipants().getAllOtherParticipants(sender).elementAt(0);
            Conversation.printWSln("MODOS","(A2)MazeGameClarificationRequest:"+sender.getUsername());
            return new MazeGameDummyBlockingOnTextEntry( sS, this, pRecipient.getUsername(), sender.getUsername(), mCTUR,this.dummyInterventionMaxLength) ;


         }
         if(this.getC().getParticipants().getAllParticipants().size()>1){

               int rnd = r.nextInt(super.interventionEvery.getValue());
               Conversation.printWSln("Main", "Trying for intervention");

               Participant pRecipient = (Participant)getC().getParticipants().getAllOtherParticipants(sender).elementAt(0);

               if( !forceIntervention||((rnd==2&&lastRecipientOfCR!=pRecipient) || (lastRecipientOfCR==pRecipient&&rnd==2&&turnsSinceLastIntervention.getValue()>2))){
                   lastRecipientOfCR=pRecipient;
                   this.turnsSinceLastIntervention.setValue(0);
                   String cr = this.cycCRIN_participant1.getNext(pRecipient);
                   this.setState(1);
                   apparentOrigin=sender;
                   this.mgcSWFTREcipient=pRecipient;
                   fh.setInformOthersOfTyping(false);
                   Conversation.printWSln("MODOS","(A)MAZEGAMECR recipient"+pRecipient.getUsername()+" Sender: "+sender.getUsername());
                   MazeGameClarificationRequest mgcr = new MazeGameClarificationRequest(sS,this,pRecipient.getUsername(), sender.getUsername(),mCTUR, cr, this.crDelayMEAN,this.crDelayPLUSMINUS);
                   mgcr.setType("CLARIFICATION_NEXTSEQUENCESPEAKERCHANGE");
                   return mgcr;
               }
               else{
                   Conversation.printWSln("Main", "Random number was: "+rnd+" should be 8 for intervention");
               }
           }

           this.turnsSinceLastIntervention.setValue(turnsSinceLastIntervention.getValue()+1);
           expSettings.generateParameterEvent(turnsSinceLastIntervention);
           Conversation.printWSln("Main", "Normal turn");
           Conversation.printWSln("MODOS","(C)MazeGameDefaultSequence"+sender.getUsername());
           MazeGameDefaultSequence mgds = new  MazeGameDefaultSequence(sS,this,sender.getUsername(),mCTUR);
           mgds.setType("DEFAULTNORMAL_NEXTSEQUENCESPEAKERCHANGE");
           return mgds;

         ///Remember to reset the state for all other possible kinsd of Sequence....including edits, and also for the dummy errors
         //

    }


    //REMEMBER, HAVE ADDED THE NEW SEQUENCE THING IN FLOORHOLDER, THAT DOESNT SEEM TO WORK PROPERLY

    @Override
    public Sequence getNextSequence_NewLine_By_Same_Speaker(Sequence prior, int sequenceNo, Sequences sS, String sender, MessageCBYCDocChangeFromClient mCDC) {

         DocChange dc = mCDC.getDocChange();
         String sendername = prior.getSender();
         Participant p = this.c.getParticipants().findParticipantWithUsername(sendername);

         if(state==2||state==3){
            mgcSWFT = new MazeGameCapturesSequenceWaitingForTypingLull(sS,this, sender, mCDC.getTimeStamp(),  dc.elementString, dc.elementStart, dc.elementFinish) ;
            mgcSWFT.setType("RESPONSE_NEWLINE");
            this.mgcSWFTREcipient = p;
            this.timeOfLastState3DocChange = new Date().getTime();
            setState(3);
            Conversation.printWSln("MODOS","(B)MazeGameCapturesSequenceWaitingForTypingLull:"+sender);
            return mgcSWFT;
        }


        if(Debug.showEOFCRSTATES)Conversation.printWSlnLog("Main", sender+" is requesting NEWLINETURN, the state is: "+this.state+"B");



           Conversation.printWSln("Main", "Normal turn");
           this.turnsSinceLastIntervention.setValue(turnsSinceLastIntervention.getValue()+1);
           expSettings.generateParameterEvent(turnsSinceLastIntervention);
           Conversation.printWSln("MODOS","(B)MazeGameDefaultSequence:"+sender);
            MazeGameDefaultSequence mgds = new MazeGameDefaultSequence(sS, this ,sender, mCDC.getTimeStamp(), dc.elementString, dc.elementStart, dc.elementFinish);
            mgds.setType("default_NEWLINE");
            return mgds;
    }





   public Sequence getNextSequence_Edit_By_Different_Speaker(Sequence prior,int sequenceNo,Sequences ss,Participant p,MessageCBYCTypingUnhinderedRequest mCTUR){
        String sendername = prior.getSender();
        if(state==2||state==3){
            mgcSWFT = new MazeGameCapturesSequenceWaitingForTypingLull(sS,this, p.getUsername(), mCTUR.getTimeStamp(),  mCTUR.getElementString(), mCTUR.getElementStart(), mCTUR.getElementFinish()) ;
            mgcSWFT.setType("RESPONSE_EDITBYDIFFERENT");
            this.mgcSWFTREcipient = p;
            this.timeOfLastState3DocChange = new Date().getTime();
            setState(3);
            Conversation.printWSln("MODOS","(C)MazeGameCapturesSequenceWaitingForTypingLull:"+sendername);
            return mgcSWFT;
        }


       if(Debug.showEOFCRSTATES)Conversation.printWSlnLog("Main", p.getUsername()+" is requesting EDITBYDIFFERENT, the state is: "+this.state);
       Conversation.printWSln("MODOS","EDITBYDIFFERENT");
       MazeGameDefaultSequence mgds = new  MazeGameDefaultSequence(sS,this,p.getUsername(),mCTUR);
       mgds.setType("DEFAULTNORMAL_NEXTSEQUENCE_EDITBYDIFFERENT");
       return mgds;
    }


    public Sequence getNextSequence_Edit_By_Same_Speaker(Sequence prior,int sequenceNo,Sequences sS,String sender,MessageCBYCDocChangeFromClient mCDC){
         DocChange dc = mCDC.getDocChange();
         String sendername = prior.getSender();
         Participant p = this.c.getParticipants().findParticipantWithUsername(sendername);
        if(state==2||state==3){
            mgcSWFT = new MazeGameCapturesSequenceWaitingForTypingLull(sS,this, sender, mCDC.getTimeStamp(),  dc.elementString, dc.elementStart, dc.elementFinish) ;
            mgcSWFT.setType("RESPONSE_EDITBYSAME");
            this.mgcSWFTREcipient = p;
            this.timeOfLastState3DocChange = new Date().getTime();
            setState(3);
            Conversation.printWSln("MODOS","(B)MazeGameCapturesSequenceWaitingForTypingLull:"+sender);
            return mgcSWFT;
        }
        if(Debug.showEOFCRSTATES)Conversation.printWSlnLog("Main", sender+" is requesting EDITBYSAME, the state is: "+this.state);
        Conversation.printWSln("MODOS","EDITBYSAME");
        //MazeGameEditSequence seq2 = new MazeGameEditSequence(sS,this,sender,mCDC.getTimeStamp(),dc.elementString,dc.elementStart,dc.elementFinish);
        MazeGameDefaultSequence mgds = new MazeGameDefaultSequence(sS, this ,sender, mCDC.getTimeStamp(), dc.elementString, dc.elementStart, dc.elementFinish);
        mgds.setType("default_EDITBYSAME");
        return mgds;
    }






}
package diet.server.ConversationController;
import diet.message.MessageCBYCDocChangeFromClient;
import diet.message.MessageCBYCTypingUnhinderedRequest;
import diet.parameters.ExperimentSettings;
import diet.parameters.IntParameter;
import diet.server.Conversation;
import diet.server.Participant;
import diet.server.CbyC.DocChange;
import diet.server.CbyC.Sequences;
import diet.server.CbyC.Sequence.MazeGameBuffered;
import diet.server.CbyC.Sequence.MazeGameBufferedSynonymReplacement;
import diet.server.CbyC.Sequence.MazeGameDefaultSequence;
import diet.server.CbyC.Sequence.MazeGameEditSequence;
import diet.server.CbyC.Sequence.Sequence;
import diet.server.conversationhistory.turn.CBYCMAZEGAMETURN;
import diet.server.conversationhistory.turn.Turn;
import diet.textmanipulationmodules.SynonymDetector.DynamicSynonymModificationsForDyads;



public class CCCBYCMazeGame extends CCCBYCDefaultController{
       
    
    
    
    
    DynamicSynonymModificationsForDyads dsmfd = new DynamicSynonymModificationsForDyads();
    
    
    @Override
    public void initialize(Conversation c, ExperimentSettings expSettings) {
        super.initialize(c, expSettings);
         super.setIsTypingTimeOut(250);
        super.setProcessLoopSleepTime(80);
        //expSettings.addParameter(lowerBoundForIntra);
        //expSettings.addParameter(lowerBoundForInter);


    }

    


    @Override
    public Conversation getC() {
        return super.getC();
    }


    @Override
    public void processCBYCTypingUnhinderedRequest(Participant sender, MessageCBYCTypingUnhinderedRequest mWTUR) {
        if(c.getParticipants().getAllParticipants().size()<2)return;
        super.processCBYCTypingUnhinderedRequest(sender, mWTUR);
    }
    
    
    
    @Override
    public void processLoop(){
        fh.openFloorAfterTimeOut(super.getIsTypingTimeOut());
    }

    @Override
    public Turn getTurnTypeForIO(){
        return new CBYCMAZEGAMETURN();
    }    

    
    
    @Override
    public Sequence getNextSequence_Speaker_Change(Sequence prior,int sequenceNo, Sequences ss, Participant p, MessageCBYCTypingUnhinderedRequest mCTUR) {     
           //return new Sequence(sS,this,p.getUsername(),mCTUR);
           Conversation.printWSln("Main", "Normal turn");
           return new MazeGameDefaultSequence(sS,this,p.getUsername(),mCTUR);
          
           
      
    }

    @Override
    public Sequence getNextSequence_NewLine_By_Same_Speaker(Sequence prior, int sequenceNo, Sequences sS, String sender, MessageCBYCDocChangeFromClient mCDC) {
        String sendername = prior.getSender();
        Participant p = this.c.getParticipants().findParticipantWithUsername(sendername);
        DocChange dc = mCDC.getDocChange();
        
        
        
          Conversation.printWSln("Main", "Normal turn");
          return new MazeGameDefaultSequence(sS, this ,sender, mCDC.getTimeStamp(), dc.elementString, dc.elementStart, dc.elementFinish);         
    }

    
    
    
    public Sequence getNextSequence_Edit_By_Different_Speaker(Sequence prior,int sequenceNo,Sequences ss,Participant p,MessageCBYCTypingUnhinderedRequest mCTUR){
        return new MazeGameEditSequence(sS,this,p.getUsername(),mCTUR);
    }
    public Sequence getNextSequence_Edit_By_Same_Speaker(Sequence prior,int sequenceNo,Sequences sS,String sender,MessageCBYCDocChangeFromClient mCDC){
        DocChange dc = mCDC.getDocChange();
        MazeGameEditSequence seq2 = new MazeGameEditSequence(sS,this,sender,mCDC.getTimeStamp(),dc.elementString,dc.elementStart,dc.elementFinish);
        return seq2;
    }

    
    public String addSyn(String s1,String s2){
        return this.dsmfd.addSynonym(s1, s2);
    }
    
    
    
}
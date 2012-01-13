package diet.server.ConversationController;
import java.util.Random;
import java.util.Vector;

import diet.debug.Debug;
import diet.message.MessageCBYCDocChangeFromClient;
import diet.message.MessageCBYCTypingUnhinderedRequest;
import diet.parameters.ExperimentSettings;
import diet.server.Conversation;
import diet.server.Participant;
import diet.server.CbyC.DocChange;
import diet.server.CbyC.Sequences;
import diet.server.CbyC.Sequence.Sequence;
import diet.server.CbyC.Sequence.SplitUtt;
import diet.server.CbyC.Sequence.SplitUttBufferedTarget;
import diet.server.CbyC.Sequence.SplitUttContiguousByTarget;
import diet.server.conversationhistory.turn.CBYCTURN;
import diet.server.conversationhistory.turn.Turn;



public class CCCBYCSplitUtterance extends CCCBYCDefaultController{
       
    
    @Override
    public void initialize(Conversation c, ExperimentSettings expSettings) {
        super.initialize(c, expSettings);
        super.setIsTypingTimeOut(250);
        super.setProcessLoopSleepTime(80);
    }

    

    @Override
    public void processCBYCTypingUnhinderedRequest(Participant sender, MessageCBYCTypingUnhinderedRequest mWTUR) {
        if(c.getParticipants().getAllParticipants().size()!=4)return;
        super.processCBYCTypingUnhinderedRequest(sender, mWTUR);
    }
    
    
    
    @Override
    public void processLoop(){
        fh.openFloorAfterTimeOut(super.getIsTypingTimeOut());
    }

    @Override
    public Turn getTurnTypeForIO(){
        return new CBYCTURN();
    }    

    private String[] getAppOriginsForIntervention_Random(Participant sender, Participant otherRandom, int WhichHalf, String IntType, String IntType2){
            Random r = new Random();
            Random r1 = new Random();
            Vector allOtherParticipants = getC().getParticipants().getAllOtherParticipants(sender);
            Vector allParticipants = getC().getParticipants().getAllParticipants();
             
            String[] appOrig = new String[8];
            Participant otherA = (Participant)allOtherParticipants.elementAt(0);
            Participant otherB = (Participant)allOtherParticipants.elementAt(1);
            Participant otherC = (Participant)allOtherParticipants.elementAt(2);
            int i;
            int j = 0;
            int k = 0;
            
            if(otherA == otherRandom){
                appOrig[0] = otherB.getUsername();
                for(i=0;i<=3;i++){
                    if(otherB == allParticipants.elementAt(i)){
                    j=i;
                    }
                }
            } else {
                appOrig[0] = otherA.getUsername();
                for(i=0;i<=3;i++){
                    if(otherA == allParticipants.elementAt(i)){
                    j=i;
                    }
                }
            }
            
            if(otherC == otherRandom){
                appOrig[2] = otherB.getUsername();
                for(i=0;i<=3;i++){
                    if(otherB == allParticipants.elementAt(i)){
                    k=i;
                    }
                }
            } else {
                appOrig[2] = otherC.getUsername();
                for(i=0;i<=3;i++){
                    if(otherC == allParticipants.elementAt(i)){
                    k=i;
                    }
                }
            }
            
            int ChooseInt = r.nextInt(100);
            int ChooseInt2 = r1.nextInt(100);
            String Int1 = "";
            String Int2 = "";
            String Int3 = "";
            String Int4 = "";
            if(WhichHalf==1){
            if(ChooseInt<=personOdds[j][0]){
                //String Intervention = "AA";
                Int1 = "A";
                Int2 = "A";
                personOdds[j][0]=personOdds[j][0]-8;
            }else if(ChooseInt<=personOdds[j][0]+personOdds[j][1]){
                //String Intervention = "AB";
                Int1 = "A";
                Int2 = "B";
                personOdds[j][1]=personOdds[j][1]-8;
            }else if(ChooseInt<=personOdds[j][0]+personOdds[j][1]+personOdds[j][2]){
                //String Intervention = "BA";
                Int1 = "B";
                Int2 = "A";
                personOdds[j][2]=personOdds[j][2]-8;
            }else{
                //String Intervention = "BB";
                Int1 = "B";
                Int2 = "B";
                personOdds[j][3]=personOdds[j][3]-8;
            }
            for(i=0;i<=3;i++){
                personOdds[j][i]=personOdds[j][i]+2;
            }
            if(ChooseInt2<=personOdds[k][0]){
                //String Intervention = "AA";
                Int3 = "A";
                Int4 = "A";
                personOdds[k][0]=personOdds[k][0]-8;
            }else if(ChooseInt2<=personOdds[k][0]+personOdds[k][1]){
                //String Intervention = "AB";
                Int3 = "A";
                Int4 = "B";
                personOdds[k][1]=personOdds[k][1]-8;
            }else if(ChooseInt2<=personOdds[k][0]+personOdds[k][1]+personOdds[k][2]){
                //String Intervention = "BA";
                Int3 = "B";
                Int4 = "A";
                personOdds[k][2]=personOdds[k][2]-8;
            }else{
                //String Intervention = "BB";
                Int3 = "B";
                Int4 = "B";
                personOdds[k][3]=personOdds[k][3]-8;
            }
            for(i=0;i<=3;i++){
                personOdds[k][i]=personOdds[k][i]+2;
            }
            IntType = Int1;
            IntType2 = Int3;
            }

            
            if(IntType == "A"){
                appOrig[1] = sender.getUsername();
            }else{
                appOrig[1] = otherRandom.getUsername();
            }
            
            if(IntType2 == "A"){
                appOrig[3] = sender.getUsername();
            }else{
                appOrig[3] = otherRandom.getUsername();
            }
    
            appOrig[4] = otherRandom.getUsername();
            appOrig[5] = sender.getUsername();
            appOrig[6] = Int2;
            appOrig[7] = Int4;
            
            return appOrig;
    }
    

    public String[] getAppOriginsForIntervention_SecondHalf(Sequence su1, Participant otherRandom, String SecondHalfOfSplitType1, String SecondHalfOfSplitType2){
        
        Participant sender = (Participant)this.getC().getParticipants().findParticipantWithUsername(su1.getSender());
 
        String[] a = this.getAppOriginsForIntervention_Random(sender, otherRandom, 2, SecondHalfOfSplitType1, SecondHalfOfSplitType2);
        return a;
    }
    
    public String PreviousA = "null";
    public String PreviousB = "null";
    int[][] personOdds = {{25,25,25,25},{25,25,25,25},{25,25,25,25},{25,25,25,25}};

 
    public String[][] getAppOriginsForIntervention_BothHalves(Sequence spuBuff,String sender){
        Participant senderP = (Participant)this.getC().getParticipants().findParticipantWithUsername(sender);

        Random r = new Random();
        Vector allOtherParticipants = getC().getParticipants().getAllOtherParticipants(senderP);
        Participant otherRandom = (Participant)allOtherParticipants.elementAt(r.nextInt(allOtherParticipants.size()));
        if(otherRandom.getUsername()==PreviousA && sender == PreviousB){
            allOtherParticipants.removeElement(otherRandom);
            otherRandom = (Participant)allOtherParticipants.elementAt(r.nextInt(allOtherParticipants.size()));
        }
        
        String[][] ab = new String[8][2];
        ab[0]= this.getAppOriginsForIntervention_Random(senderP, otherRandom, 1, "", "");
        String SecondHalfOfSplitType1=ab[0][6];
        String SecondHalfOfSplitType2=ab[0][7];
        ab[1]= this.getAppOriginsForIntervention_SecondHalf(spuBuff, otherRandom, SecondHalfOfSplitType1, SecondHalfOfSplitType2);

        Conversation.printWSln("SPLITUTT", "______________________________________________");
        String s1 = ab[0][0]+","+ab[0][1]+"\n"+
                ab[0][2]+","+ab[0][3]+"\n"+
                ab[0][4]+","+ab[0][5]+"\n";
        String s2 = ab[1][0]+","+ab[1][1]+"\n"+
                ab[1][2]+","+ab[1][3]+"\n"+
                ab[1][4]+","+ab[1][5]+"\n"+
                personOdds[0][0]+","+personOdds[0][1]+","+personOdds[0][2]+","+personOdds[0][3]+"\n"+
                personOdds[1][0]+","+personOdds[1][1]+","+personOdds[1][2]+","+personOdds[1][3]+"\n"+
                personOdds[2][0]+","+personOdds[2][1]+","+personOdds[2][2]+","+personOdds[2][3]+"\n"+
                personOdds[3][0]+","+personOdds[3][1]+","+personOdds[3][2]+","+personOdds[3][3];
        
        PreviousA = ab[1][5];
        PreviousB = ab[1][4];
        Conversation.printWSln("SPLITUTT", s1);
        Conversation.printWSln("SPLITUTT", "AND THE SECOND HALF IS");
        Conversation.printWSln("SPLITUTT", s2);
        return ab;
    }
    
    public void processBufferedTargetAbort(){
        Random r = new Random();
        count = r.nextInt(2);
    }
    
    
    private int count=0;

    
    
    @Override
    public Sequence getNextSequence_Speaker_Change(Sequence prior,int sequenceNo, Sequences ss, Participant p, MessageCBYCTypingUnhinderedRequest mCTUR) {
        
        //return new Sequence(sS,this,p.getUsername(),mCTUR);
        if(Debug.generateOnlySplitUttCBYCTurns) return new SplitUttBufferedTarget(this,p.getUsername(),sS);
        
        //if(p.getUsername()==PreviousA){
        if(p.getUsername().equalsIgnoreCase(PreviousA)){
            return new Sequence(sS,this,p.getUsername(),mCTUR);
        }
                
        //if(count>=8){
        if(count>=4){    
            count = 0;
            return new SplitUttBufferedTarget(this,p.getUsername(),sS);
        }else{
            count++;
            return new Sequence(sS,this,p.getUsername(),mCTUR);
        }             

        
        
    }

    
    public Sequence getNextSequence_Edit_By_Same_Speaker(Sequence prior, int sequenceNo, Sequences sS, String sender, MessageCBYCDocChangeFromClient mCDC) {
        DocChange dc = mCDC.getDocChange();
        
        if(prior instanceof SplitUttBufferedTarget){
            SplitUttBufferedTarget spuBuff = (SplitUttBufferedTarget)prior;
            spuBuff.abortDueToNewLine();
            return spuBuff;
        }
        else if(prior instanceof SplitUtt){
            return new SplitUttContiguousByTarget((SplitUtt)prior,sS,this,sender,mCDC.getTimeStamp(),mCDC.getDocChange().elementString,dc.elementStart,dc.elementFinish);
        }
        else if(prior instanceof SplitUttContiguousByTarget){
            return new SplitUttContiguousByTarget((SplitUttContiguousByTarget)prior,sS,this,sender,mCDC.getTimeStamp(),mCDC.getDocChange().elementString,dc.elementStart,dc.elementFinish);
        
        }
        else{
            return super.getNextSequence_Edit_By_Same_Speaker(prior, sequenceNo, sS, sender, mCDC);    
        }
        
        
        
    }

    
    
    
    
    
}
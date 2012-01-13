package diet.server.ConversationController;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.Random;
import java.util.Vector;

import diet.message.MessageChatTextFromClient;
import diet.message.MessageKeypressed;
import diet.message.MessageWYSIWYGDocumentSyncFromClientInsert;
import diet.message.MessageWYSIWYGDocumentSyncFromClientRemove;
import diet.message.MessageWYSIWYGTextSelectionFromClient;
import diet.parameters.ExperimentSettings;
import diet.parameters.IntParameter;
import diet.parameters.Parameter;
import diet.server.Conversation;
import diet.server.Participant;
import diet.task.tangram2D1M.MessageBuffered;
import diet.task.tangram2D1M.ParticipantCircularVector;
import diet.task.tangram2D1M.SlotUtterance;
import diet.task.tangram2D1M.TangramGameController;
import diet.task.tangram2D1M.TangramSequence;
import diet.utils.treekernel.Fragment;
import diet.utils.treekernel.FragmentIdentifierThread;


public class CCTANGRAMGAMEDISTANTFRAG extends DefaultConversationController{
    
    //private JTextAreaDebugWindow js = new JTextAreaDebugWindow();
    
    Parameter p;
        
    static final String[] fakeAcks={"ok done", "done", "done that", "ok got it", "got that"};
    static final String[] acks={"ok", "right", "okay", "got it", "uhhu", "alright"};
    static final String[] hesitations={"sorry errm  ","sorry err ", "err . . ", "uuuuh sorry ", "err sorry ", "sorry uuuh ", "errm . ."}; 
    static final long responseTimeOut=20000;
    FragmentIdentifierThread fi;
    TangramGameController gc;
    int curSlotUD=0;
    BufferedWriter slotChangeFile;
           
    TangramSequence curSeq;
    Participant pDirectorA;
    Participant pDirectorB;
    Participant pMatcher;
    boolean participantsSet=false;
    Vector<String> interventionTriggers=new Vector<String>();
    
    ParticipantCircularVector last5TalkSeq;
    long timeOfLastUtterance;
    long timeOfFragSending;
    
    public CCTANGRAMGAMEDISTANTFRAG() {
        
    }
    
    public void initialize(Conversation c, ExperimentSettings expSettings)
    {
        super.initialize(c, expSettings);
        
        System.out.println("Initializing TangramGameDistantFrag");
        p = new IntParameter("NumberOfTurns",0,0);
        expSettings.addParameter(p);
            
    }
    
    
    private void initializeSlotChangeFile()
    {
        try {
            String expDir=c.getConvIO().getFileNameContainingConversationData().getName();;
            this.slotChangeFile= new BufferedWriter(new FileWriter(System.getProperty("user.dir")+File.separator+"data"+File.separator+"Saved Experimental data"+File.separator+expDir+File.separator+"slotChange.txt"));
            slotChangeFile.write("TimeStamp|Sequence|SlotID|Intervention|FragOwner");
            
            slotChangeFile.newLine();
            slotChangeFile.write(new Date().getTime()+"|"+gc.getCurSequence()+"|0|No|NotSent");
            
        
        }
        catch(IOException e)
        {
            System.out.println("trouble writing to slot change file");
            e.printStackTrace();
            System.exit(1);
            
        
        }
    }
    private void doOtherInitialization()
    {
        gc=(TangramGameController)c.getTaskController();
        curSlotUD=gc.getCurSlotID();
        initializeSlotChangeFile();
        fi=new FragmentIdentifierThread(c.getHistory().getParserWrapper());
        fi.start();
        curSeq=gc.getCurSequence();
        
    }
    
    private String getDirectorUserNamesAtSlot(int i)
    {
        String result="";
        if (curSeq.whichDirectors(i).indexOf('A')>=0
                &&
                
                curSeq.whichDirectors(i).indexOf('B')>=0) result=pDirectorA.getUsername()+"&"+pDirectorB.getUsername();
        else if (curSeq.whichDirectors(i).indexOf('B')>=0) result=pDirectorB.getUsername();
        else result=pDirectorA.getUsername();
        return result;
    }
    
 
    private void intervene()
    {

        directorBuffer=new Vector<MessageBuffered>();
        matcherBuffer=new Vector<MessageBuffered>();

        System.out.println("trying to do intervention. Sequence:"+curSeq.getSubsequenceAsString(curSlotUD-2, curSlotUD));
        curSeq.advanceInterventionPointer();
        Fragment frag = fi.chooseBestRandFragFromSlot(curSlotUD-2, 4);
        if (frag==null)
        {
            c.resetFromIsTypingtoNormalChatAllAllowedParticipants(super.getIsTypingTimeOut());
            try{slotChangeFile.write("|"+curSeq.getSubsequenceAsString(curSlotUD-2, curSlotUD)+"_NoFrags"+"|"+"NotSent");
                slotChangeFile.flush();
            }catch(IOException e){}
            System.out.println("no suitable fragments found. skipping opportunity:(");

            return;
        }
        
        this.processingCRSubdialogue=true;
        this.ackSent=false;
        
        try{
            slotChangeFile.write("|"+curSeq.getSubsequenceAsString(curSlotUD-2, curSlotUD)+"|"+this.getDirectorUserNamesAtSlot(curSlotUD-2));
            slotChangeFile.flush();
        }
        catch(IOException e){e.printStackTrace();}
        
        
        long avgTypingSpeed=c.getHistory().getAverageTypingSpeedForLastNTurns(pMatcher.getUsername(), 5);
        if(avgTypingSpeed<=0) avgTypingSpeed = 3;
        
            
        String intervention=hesitations[new Random().nextInt(hesitations.length)]+frag.content.trim()+"?";
        String fakeAck=fakeAcks[r.nextInt(fakeAcks.length)];
        c.informParticipantBthatParticipantAIsTyping(pMatcher,pDirectorA);
        c.informParticipantBthatParticipantAIsTyping(pMatcher,pDirectorB);
         
        
        long randomWaitTimeAck=1000+1000*(fakeAck.length()/avgTypingSpeed);
        long randomWaitTimeIntervention=500 + 1000*(intervention.length()/avgTypingSpeed);
        
        timeOfFragSending=new Date().getTime()+randomWaitTimeAck+randomWaitTimeIntervention;
        c.sendAndEnableLabelDisplayToALLStatusWindowsOfParticipant(pMatcher, "Network error.please wait..",true,false);
        try{Thread.sleep(randomWaitTimeAck);}catch (Exception e){}
            
        c.sendArtificialTurnFromApparentOriginToRecipient(pMatcher, pDirectorA, fakeAck);
        c.sendArtificialTurnFromApparentOriginToRecipient(pMatcher, pDirectorB, fakeAck);
        
        
        try{Thread.sleep(randomWaitTimeIntervention);}catch (Exception e){}
        
        c.sendArtificialTurnFromApparentOriginToRecipient(pMatcher, pDirectorA, intervention);
        c.sendArtificialTurnFromApparentOriginToRecipient(pMatcher, pDirectorB, intervention);
            
        c.informParticipantBthatParticipantAIsNotTypingAndStatusIsOK(pMatcher,pDirectorA);
        c.informParticipantBthatParticipantAIsNotTypingAndStatusIsOK(pMatcher,pDirectorB);
            
            
        
        
    }
    
   
   
     
    boolean processingCRSubdialogue=false;
    boolean slotJustChanged=false;
    long networkErrorInterval=180000;
    long lastNetworkErrorTime=new Date().getTime();
    private void sendRandNetworkError()
    {
        if (!processingCRSubdialogue)
        {
            
            //send error if time has come
            if (new Date().getTime()-lastNetworkErrorTime>networkErrorInterval)
                     {
                     
                        
                        Vector participants=c.getParticipants().getAllActiveParticipants();
                        Participant to=(Participant)participants.elementAt(r.nextInt(participants.size()));
                        c.sendAndEnableLabelDisplayToALLStatusWindowsOfParticipant(to, "Network Error. Please wait..", true, false);
                        lastNetworkErrorTime=new Date().getTime();
                        networkErrorInterval=120000+r.nextInt(60000);
                        errorSentTo=to;
                     
                        try{Thread.sleep(3000+r.nextInt(1000));}catch (Exception e){}
                        
                        c.sendAndEnableLabelDisplayToALLStatusWindowsOfParticipant(to, "Status: OK", false, true);
                        errorSentTo=null; 
                        c.resetFromIsTypingtoNormalChatAllAllowedParticipants(super.getIsTypingTimeOut());
                    }
            else return;
        }
        else
        {
            //don't send error. wait until next time(reduced interval).
            lastNetworkErrorTime=new Date().getTime();
            networkErrorInterval=20000+r.nextInt(20000);
            errorSentTo=null;
        }
        
    }
    @Override
    public void processLoop(){
        if (!participantsSet) return;    
        
        if (curSlotUD!=gc.getCurSlotID()) 
        {
             
            
            curSlotUD=gc.getCurSlotID();
            System.out.println("SlotUD changed to:"+curSlotUD);
            
            try
            {
                    System.out.println("writing to slot change file");
                    
                    slotChangeFile.newLine();
                    slotChangeFile.write(new Date().getTime()+"|"+curSeq+"|"+curSlotUD);
                    slotChangeFile.flush();
                    
            
            }
            catch(IOException e)
            {
                    System.out.println("cannot write to slot change file");
                    e.printStackTrace();
            }
            if (curSlotUD==0)
            {
                System.out.println("resetting triggers and fragmentIdent. Slot is 0.");
                curSeq=gc.getCurSequence();
                fi.reset();
                 try{slotChangeFile.write("|"+"No"+"|"+"NotSent");
                            slotChangeFile.flush();
                         }catch(Exception e){}
               
                slotJustChanged=false;
                
            }
            else 
            {
                slotJustChanged=true;
               
            }
                
        }
        
        
        if (!processingCRSubdialogue){
            
           
             if (slotJustChanged)
             {
                 //slot has been placed by matcher. either he has alreadey acked or will do soon.
                 //wait a while if matcher hasn't acked.
                 System.out.println("slot just changed");
                 if (curSeq.interventionTime(curSlotUD))
                 {
                     
                     slotJustChanged=false;
                     
                     try{Thread.sleep(2000+r.nextInt(1000));}catch (Exception e){}
                     intervene();              
                     
                 }
                 else
                 {
                     c.resetFromIsTypingtoNormalChatAllAllowedParticipants(super.getIsTypingTimeOut());
                     try{slotChangeFile.write("|"+"No"+"|"+"NotSent");
                            slotChangeFile.flush();
                         }catch(Exception e){}
                     
                     
                     slotJustChanged=false;
                     
                 }
             }
             else
             {
                 
                    c.resetFromIsTypingtoNormalChatAllAllowedParticipants(super.getIsTypingTimeOut());
                 
                 
             }
             
             
             
        }
        
        else if (new Date().getTime()-this.timeOfFragSending>responseTimeOut)
        {
            //timeout has passed. check if directors have been inactive.
            if(!pDirectorA.isTyping(2000)&&!pDirectorB.isTyping(2000)){
                //no response has been given from directors :(. resetting to normal.
              processingCRSubdialogue=false;
              c.sendAndEnableLabelDisplayToALLStatusWindowsOfParticipant(pMatcher, "Status: OK", false, true);
              
              //previousline is NO GOOD
              c.resetFromIsTypingtoNormalChatAllAllowedParticipants(super.getIsTypingTimeOut());
              
              
              //flushDirectorBufferToMatcher();
            }  
            
        }
        else
        {
            if (!pDirectorA.isTyping(super.getIsTypingTimeOut()))
            {
                c.informParticipantBthatParticipantAIsNotTypingAndStatusIsOK(pDirectorA, pDirectorB);
            }            
            if (!pDirectorB.isTyping(super.getIsTypingTimeOut()))
            {
                c.informParticipantBthatParticipantAIsNotTypingAndStatusIsOK(pDirectorB, pDirectorA);
                
            }
            
                
        }
        //Try sending random network error
        sendRandNetworkError();
        
       
    }
    private void flushDirectorBufferToMatcher()
    {
        for(MessageBuffered m:directorBuffer)
        {
            c.sendArtificialTurnFromApparentOriginToRecipient(m.sender, pMatcher, m.mct.getText());
        }
        directorBuffer=new Vector<MessageBuffered>();
    }
    
   
    Vector<MessageBuffered> directorBuffer=new Vector<MessageBuffered>();
    Vector<MessageBuffered> matcherBuffer=new Vector<MessageBuffered>();
    
    boolean ackSent;
    @Override
    public void processChatText(Participant sender, MessageChatTextFromClient mct){    
       
            //initialise
           if (!setParticipants()) return;
           if (mct.getText().length()==0) return;
           timeOfLastUtterance=new Date().getTime();
           //last5TalkSeq.addToTalkSeq(sender);
           
           p.setValue(((Integer)p.getValue())+1);
           super.expSettings.generateParameterEvent(p);
           
           //send utterance to fragmentIdentifier          
           String curUtterance=mct.getText().trim();
           if (sender!=pMatcher&&!processingCRSubdialogue&&fi!=null)
           {
               //System.out.println("is this ever run?");
               fi.enqueueSlotUtterance(new SlotUtterance(curUtterance, this.curSlotUD));
           }
          
            
           if (processingCRSubdialogue)
           {
                
                if (sender==pDirectorA||sender==pDirectorB)
                {
                    Participant otherDirector;
                    if (sender==pDirectorA) otherDirector=pDirectorB;
                    else otherDirector=pDirectorA;
                    
                    if (mct.getEndOfTyping()<timeOfFragSending+3000)
                    
                    {
                        
                        //directorBuffer.add(new MessageBuffered(sender,mct));
                        //director turn recieved before frag was sent. possibly buffer and send to matcher afterwards
                        System.out.println("message recieved before 3 seconds after the frag");
                        c.setNewTurnBeingConstructedNotRelayingOldTurnAddingOldTurnToHistory(sender, mct);
                        c.sendArtificialTurnFromApparentOriginToRecipient(sender, otherDirector, mct.getText());
                        
                        return;
                    }
                    
                    String ack = acks[new Random().nextInt(acks.length)];
                    long avgTypingSpeed = c.getHistory().getAverageTypingSpeedForLastNTurns(pMatcher.getUsername(),5);
                    try{Thread.sleep(1000+ r.nextInt(200));}catch (Exception e){}
                    c.sendArtificialTurnFromApparentOriginToRecipient(sender, otherDirector, mct.getText());
                    c.setNewTurnBeingConstructedNotRelayingOldTurnAddingOldTurnToHistory(sender, mct);
                    try{Thread.sleep(1000+ r.nextInt(200));}catch (Exception e){}
                    if (!ackSent){
                        ackSent=true;
                        c.informParticipantBthatParticipantAIsTyping(pMatcher, pDirectorA);
                        c.informParticipantBthatParticipantAIsTyping(pMatcher, pDirectorB);
                        if(avgTypingSpeed<=0) avgTypingSpeed = 3;
                        try{Thread.sleep(1000+ 1000*(ack.length()/avgTypingSpeed));}catch (Exception e){}
                        //c.setNewTurnBeingConstructedNotRelayingOldTurnAddingOldTurnToHistory(sender, mct);
                    
                        c.sendArtificialTurnFromApparentOriginToRecipient(pMatcher, pDirectorA, ack);
                        c.sendArtificialTurnFromApparentOriginToRecipient(pMatcher, pDirectorB, ack);
                    
                        //c.sendAndEnableLabelDisplayToALLStatusWindowsOfParticipant(pMatcher, "Status: OK",false,true);
                        c.informParticipantBthatParticipantAIsNotTypingAndStatusIsOK(pMatcher,pDirectorA);
                        c.informParticipantBthatParticipantAIsNotTypingAndStatusIsOK(pMatcher,pDirectorB);
                        if(!otherDirector.isTyping(4000)){
                            //no response has been given from directors :(. resetting to normal.
                            processingCRSubdialogue=false;
                            c.sendAndEnableLabelDisplayToALLStatusWindowsOfParticipant(pMatcher, "Status: OK", false, true);
              
                            //previousline is NO GOOD
                            c.resetFromIsTypingtoNormalChatAllAllowedParticipants(super.getIsTypingTimeOut());
              
              
                            //flushDirectorBufferToMatcher();
                        }  
                        
                    }
                    else 
                    {//have seen  2 turns from directors after the frag. resetting
                        processingCRSubdialogue=false;
                        
                        
                        c.sendAndEnableLabelDisplayToALLStatusWindowsOfParticipant(pMatcher, "Status: OK", false, true);
                        c.resetFromIsTypingtoNormalChatAllAllowedParticipants(super.getIsTypingTimeOut());
                        //flushDirectorBufferToMatcher();
                    }
                }
                else
                {
                    matcherBuffer.add(new MessageBuffered(sender, mct));
                }
            
            //
        } 
        else{
           c.relayTurnToAllOtherParticipants(sender,mct);
           //sending random network errors
           c.sendLabelDisplayToAllowedParticipantsFromApparentOrigin(sender,"Status: OK",false);
        
           }   
        
    }
    
   
   
    
 
    Participant errorSentTo=null;
    @Override
    public void processKeyPress(Participant sender, MessageKeypressed mkp){
        
        if (!participantsSet) return;
        if(this.processingCRSubdialogue){
            Participant otherDirector;
            if (sender==pDirectorA) otherDirector=pDirectorB;
            else otherDirector=pDirectorA;
            c.informParticipantBthatParticipantAIsTyping(sender, otherDirector);
        }
        else if (errorSentTo==null){
             c.informIsTypingToAllowedParticipants(sender);
        }
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
    
   
    private boolean setParticipants()
    {
        if (participantsSet) return true;
        Vector<Participant> participants=c.getParticipants().getAllParticipants();
        
        if (participants==null) return false;
        if (participants.size()<3) return false;
       
        int matcherIndex=0;
        for(Participant pa: participants)
        {
            if (pa.getParticipantID().equalsIgnoreCase("Matcher") 
                   || 
                   pa.getParticipantID().startsWith("match")
                   ||
                   pa.getParticipantID().startsWith("Match"))
            {
                this.pMatcher=pa;
                break;
            }
            matcherIndex++;
        }
        if (matcherIndex==0){
            
        
            pDirectorA=participants.elementAt(1);
            pDirectorB=participants.elementAt(2);
            
        }
        else if(matcherIndex==1)
        {
            pDirectorA=participants.elementAt(0);
            pDirectorB=participants.elementAt(2);
            
        }
        else if(matcherIndex==2)
        {
            pDirectorA=participants.elementAt(0);
            pDirectorB=participants.elementAt(1);
            
        }
        
        doOtherInitialization();
        
        participantsSet=true;
        return true;
    }
   
}

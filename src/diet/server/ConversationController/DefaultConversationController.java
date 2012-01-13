package diet.server.ConversationController;
import diet.debug.Debug;
import java.util.Date;
import java.util.Random;

import diet.message.MessageCBYCChangeTurntakingStatusConfirm;
import diet.message.MessageCBYCDocChangeFromClient;
import diet.message.MessageCBYCTypingUnhinderedRequest;
import diet.message.MessageChatTextFromClient;
import diet.message.MessageKeypressed;
import diet.message.MessageWYSIWYGDocumentSyncFromClientInsert;
import diet.message.MessageWYSIWYGDocumentSyncFromClientRemove;
import diet.message.MessageWYSIWYGTextSelectionFromClient;
import diet.message.MessageWYSIWYGTypingUnhinderedRequest;
import diet.message.MessageWithinTurnDocumentSyncFromClientInsert;
import diet.message.MessageWithinTurnDocumentSyncFromClientRemove;
import diet.message.MessageWithinTurnDocumentSyncFromClientReplace;
import diet.message.MessageWithinTurnTextSelectionFromClient;
import diet.parameters.ExperimentSettings;
import diet.parameters.IntParameter;
import diet.server.Conversation;
import diet.server.DocChangesOutgoingSequenceFIFO;
import diet.server.Participant;
import diet.client.StyleManager;
import diet.message.MessagePopup;
import diet.server.conversationhistory.turn.Turn;
import diet.textmanipulationmodules.TypoGenerator.TypoGenerator;
import java.util.Vector;




/**
 * This is the main (preferably only) class that should be changed when creating a new experimental
 * design. Every message sent from the clients passes through the methods provided by this class. This
 * includes each keypress, each turn typed and sent.
 * <p>On receiving a message, this class determines what is to be done with the message. In normal operation
 * it relays the messages to the other participants. However, to create interventions this behaviour can be
 * replaced with commands to modify the turn or create artificial turns.
 * 
 *<p> Most of the methods of this class are called by {@link diet.server.Conversation}. It is expected that the
 * methods of this class will do necessary detection of targets, transforming of turns, and on this basis
 * call methods in {@link diet.server.Conversation} to send the artificially created messages to the participants.
 *
 *  
 * @author user
 */
public abstract class DefaultConversationController extends Thread {
    
    //private JTextAreaDebugWindow js = new JTextAreaDebugWindow();
    Conversation c;

    public Conversation getC() {
        return c;
    }
    boolean isCompleted;
    private TypoGenerator tg = new TypoGenerator();
    Random r = new Random(new Date().getTime());
    private DefaultConversationController cC;
    private long processLoopSleepTime = 50;
    
    public ExperimentSettings expSettings;
    public IntParameter isTypingTimeOut;
    public IntParameter pTurnsElapsed;

     StyleManager sm = new StyleManager(this);

    public StyleManager getStyleManager(){
        return sm;
    }

    public int getIsTypingTimeOut() {
        return (Integer)isTypingTimeOut.getValue();
    }
    
     public IntParameter getIsTypingTimeOutParameter() {
        return isTypingTimeOut;
    }
 
    public void setIsTypingTimeOut(int n){
        isTypingTimeOut.setValue(n);
        if(expSettings!=null){
            expSettings.generateParameterEvent(isTypingTimeOut);
        }
    }
    
    public DocChangesOutgoingSequenceFIFO chOut = new DocChangesOutgoingSequenceFIFO(null,10,new Date().getTime()); //THIS WILL BE RESET ON FIRST RECEIPT OF TURN
    
    
    public DefaultConversationController(){ 
    }

    public void initialize(Conversation c,ExperimentSettings expSettings){
        this.c=c;
        this.expSettings=expSettings;
        IntParameter ip = (IntParameter)expSettings.getParameter("Typing status timeout (msecs)");
        if(ip==null){
            isTypingTimeOut=new IntParameter("Typing status timeout",500,500);
            expSettings.addParameter(isTypingTimeOut);
        }
        else{
            isTypingTimeOut=ip;
        }
        IntParameter pTurnsTemp = (IntParameter)expSettings.getParameter("NumberOfTurns");
        if(pTurnsTemp==null){
            pTurnsElapsed = new IntParameter("NumberOfTurns",0,0);
            expSettings.addParameter(pTurnsElapsed);
        }
        else{
            pTurnsElapsed = pTurnsTemp; 
        }
        
    }
    //public DefaultConversationController(Conversation c, ExperimentSettings expSettings) {
    //    super();
    //     initialize(c,expSettings);
    //    //this.c = c;
    //    //this.expSettings = expSettings;
    //    //isTypingTimeOut=(IntParameter)expSettings.getParameter("Typing status timeout (msecs)");
    //}  
    
    
    /**
     * This method must be called once the ConversationController subclass is instantiated. This method
     * starts a thread that periodically calls the processLoop() method of the subclass. This is needed to
     * deal with the "is typing" messages that appear in participants' status windows. It is also designed
     * for interventions that are triggered by timing information (As opposed to incoming turns).
     * 
     * @param cC
     */
    public void setControllerAndStartIntercept(DefaultConversationController cC){
        this.cC=cC;
        this.start();
    }
    
    @Override
    public void run(){
        //if(Debug.debugGROOP)System.err.println("----PROCESSLOOP1");
        while(c.isConversationIsActive()){
             //if(Debug.debugGROOP)System.err.println("----PROCESSLOOP2");
             try{
                 if(this.getProcessLoopSleepTime()>0)sleep(getProcessLoopSleepTime());
                 //if(Debug.debugGROOP)System.err.println("----PROCESSLOOP3");
                 //System.out.println("Sleeping");
             }catch(Exception e){
                 //if(Debug.debugGROOP)System.err.println("----PROCESSLOOP4");
                 if(c!=null){
                     c.getConvIO().saveErrorLog(e);
                 }
             }
           try{

          
           //  if(Debug.debugGROOP)System.err.println("----PROCESSLOOP5");
            cC.processLoop();
           // if(Debug.debugGROOP)System.err.println("----PROCESSLOOP6");
           }catch(Throwable t){
               t.printStackTrace();
               Conversation.printWSln("Main", c.getConversationIdentifier()+": error in processLoop "+t.getMessage());
               Conversation.printWSln("Main","Check the ERRORLOG.TXT file in the directory containing");
               Conversation.printWSln("Main","the saved experimental data");
               c.getConvIO().saveErrorLog(t);
           } 
        }
    }
    
    
    /**
     * Returns the collection of parameters constituting the experiment settings associated with this particular
     * experimental setup.
     * @return
     */
    public ExperimentSettings getExpSettings(){
        return expSettings;
    }
    
    public void processLoop(){
       
    }

    
    /**
     * This method is invoked by {@link diet.server.Conversation} whenever a participant has typed a message
     * The default behaviour is to relay the message to the other participants. This is the main locus for
     * programming interventions in the chat tool.
     * 
     * @param sender the participant who typed the turn
     * @param mct the message typed by the participant
     */
    public void processChatText(Participant sender,MessageChatTextFromClient mct){        
           c.relayTurnToAllOtherParticipants(sender,mct);
           c.sendLabelDisplayToAllowedParticipantsFromApparentOrigin(sender,"Status: OK",false);
           if(Debug.allowCHATCLIENTTOSENDDEBUGCOMMANDS)cmnd(sender,mct.getText());
    }
    
    public void processChatText_DoNOTSendIsTypingInfo(Participant sender,MessageChatTextFromClient mct){        
           c.relayTurnToAllOtherParticipants(sender,mct);
           //c.sendLabelDisplayToAllowedParticipantsFromApparentOrigin(sender,"Status: OK",false);
           if(Debug.allowCHATCLIENTTOSENDDEBUGCOMMANDS)cmnd(sender,mct.getText());
    }
    
    
    /**
     * 
     * This method is invoked by {@link diet.server.Conversation} whenever a participant presses
     * a key while typing text in their chat window.
     * The default behaviour is to inform the other participants that the participant is typing.
     * 
     * @param sender Participant who has pressed a key
     * @param mkp message containing the keypress information
     */
    public void processKeyPress(Participant sender,MessageKeypressed mkp){
    	Conversation.printWSln("KeyPress", "KeyPressed");
        c.informIsTypingToAllowedParticipants(sender);//,mkp);
    }

    
    /**
     * This method is invoked by {@link diet.server.Conversation} whenever the text in a participant's text
     * entry window changes by having one or more characters inserted. 
     * 
     * @param sender participant who inserted text
     * @param mWYSIWYGkp message containing information about the text inserted
     */
    public void processWYSIWYGTextInserted(Participant sender,MessageWYSIWYGDocumentSyncFromClientInsert mWYSIWYGkp){

    }
    
    /**
     * This method is invoked by {@link diet.server.Conversation} whenever the text in a participant's text
     * entry window changes by having one or more characters deleted. This is separate from Keypresses (a user might delete a whole segment of text by
     * highlighting the text and pressing delete once).
     * 
     * @param sender participant who deleted text
     * @param mWYSIWYGkp message containing information about the text deleted
     */
    public void processWYSIWYGTextRemoved(Participant sender,MessageWYSIWYGDocumentSyncFromClientRemove mWYSIWYGkp){
       
    }
    
    /**
     * This method is invoked by {@link diet.server.Conversation} whenever the cursor position and/or the
     * highlighted text in a participant's text entry window changes .
     * 
     * @param sender Participant who changed cursor position/highlighted text
     * @param mWYSIWYGkp message containing information about cursor change / change of highlighted text
     */
    public void processWYSIWYGSelectionChanged(Participant sender, MessageWYSIWYGTextSelectionFromClient mWYSIWYGSel){
        
    }
        
    /**
     * This method is invoked by {@link diet.server.Conversation} whenever a participant using the 
     * single window WYSIWYG interface attempts to gain the "conversational floor".
     * @param sender Participant attempting to gain the conversational floor
     * @param mWTUR message describing the attempt to gain floor, including character typed in attempt
     */
    public void processWYSIWYGTypingUnhinderedRequest(Participant sender,MessageWYSIWYGTypingUnhinderedRequest mWTUR){
       
    }

    public void processCBYCTypingUnhinderedRequest(Participant sender,MessageCBYCTypingUnhinderedRequest mWTUR){
       
    }
    
    public void processCBYCChangeTurnTakingStatusConfirm(Participant sender,MessageCBYCChangeTurntakingStatusConfirm mConf){
        
    }
    
    public void processCBYCDocChange(Participant sender, MessageCBYCDocChangeFromClient mCDC){
       
    }
    
    
    public void processWithinTurnTextInserted(Participant sender, MessageWithinTurnDocumentSyncFromClientInsert mws){
        
    }
    public void processWithinTurnTextReplaced(Participant sender, MessageWithinTurnDocumentSyncFromClientReplace mws){
        
    }
    public void processWithinTurnTextRemoved(Participant sender, MessageWithinTurnDocumentSyncFromClientRemove mws){
        
    }
    public void processWithinTurnTextSelectionChanged(Participant sender, MessageWithinTurnTextSelectionFromClient mws){
        
    }

    public boolean requestParticipantJoinConversation(String participantID){
        return true;
    }

    public void participantJoinedConversation(Participant p){
        
    }
    
    
    public void participantRejoinedConversation(Participant p){

    }
    
    
     public void processPopupResponse(Participant origin, MessagePopup mp){
         
     }
    
    
    /**
     * 
     * @return how long the thread sleeps inbetween calling processLoop() in the subclass
     */
    public long getProcessLoopSleepTime() {
        return processLoopSleepTime;
    }

    /**
     * Sets how long the thread sleeps inbetween calling processLoop() in the subclass.
     * @param processLoopSleepTime time in in millisecs
     */
    public void setProcessLoopSleepTime(long processLoopSleepTime) {
        this.processLoopSleepTime = processLoopSleepTime;
    }

    public Turn getTurnTypeForIO(){
        return new Turn();
    }

    public void cmnd(String command){
        if (command.equalsIgnoreCase("////d")){
            Vector v = c.getParticipants().getAllParticipants();
            Participant p = (Participant)v.elementAt(0);
            p.getConnection().dispose();
        }
    }
    public void cmnd(Participant p,String command){
        if (command.equalsIgnoreCase("////d")){
            if(p!=null)p.getConnection().dispose();
        }
    }

}

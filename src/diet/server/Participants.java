package diet.server;

import diet.debug.Debug;
import java.util.Vector;

import diet.message.Message;
import diet.message.MessageChatTextToClient;
import diet.message.MessageClientCloseDown;
import diet.message.MessageDisplayChangeJProgressBar;
import diet.message.MessageDisplayChangeWebpage;
import diet.message.MessageDisplayCloseWindow;
import diet.message.MessageDisplayNEWWebpage;
import diet.message.MessageStatusLabelDisplay;
import diet.message.MessageStatusLabelDisplayAndBlocked;
import diet.message.MessageWYSIWYGChangeTurntakingStatus;
import diet.message.MessageWYSIWYGDocumentSyncToClientInsert;
import diet.message.MessageWYSIWYGDocumentSyncToClientRemove;
import diet.message.MessageWYSIWYGTextSelectionToClient;
import java.awt.Color;

/**
 * Contains all the participants of a Conversation. It constructs messages to be sent to the Participants
 * and also polls the individual particpants for incoming messages, placing them in a queue.
 * <p>Please avoid invoking these methods directly...use the methods supplied in {@link Conversation} class
 * as they ensure that the messages are saved correctly to the user interface.
 * 
 * @author user
 */
public class Participants {
    
    
    private Vector participants = new Vector();
    private Conversation c;
   // private Vector incomingMessages = new Vector();
    private QueueBlockingForRecipientSortsIncomingMessages incomingMessageQueue= new QueueBlockingForRecipientSortsIncomingMessages();
   
    private boolean enqueingMessages = true;
    LabelManager lm;
    
    
    public Participants(Conversation c) {
      this.c =c;
      lm=new LabelManager(c);
    }
    
    
    /**
     * Returns next message from the queue of incoming messages. Blocks if no message available.
     * @return next mesage from participants
     */
    public Message getNextMessage(){
        if(Debug.debugBLOCKAGE){System.out.println("MCT66600");System.out.flush();}
        return getNextMessageBlocking();
    }
    
    /**
     * 
     * Returns next message from the queue of incoming messages. Blocks if no message available.
     * 
     * @return next message from participants
     */
    private Message getNextMessageBlocking(){
         return this.incomingMessageQueue.getNextMessageBlocking();
    }
   
    
    
    /**
     * Adds message to queue of incoming messages
     * @param m Message from participant
     */
    public void addMessageToIncomingQueue(Message m){
        if(2>5&&Debug.debugGROOP)System.err.println("GRP(1)-ADDING");
        this.incomingMessageQueue.addMessageToIncomingQueue(m);
    }
    
    /**
     * Loop which polls all participants for incoming messages
     * 
     */
   
    
    /**
     * 
     * @return Vector containing all participants in the experiment (including inactive participants)
     */
    public Vector getAllParticipants(){
        return participants;
    }
    
    public Vector getAllParticipantsNewCopy(){
        Vector v = new Vector();
        for(int i=0;i<participants.size();i++){
            v.addElement(participants.elementAt(i));
        }
        return v;
    }
    
    /**
     * 
     * @return Vector only containing participants that have active connections.
     */
    public Vector getAllActiveParticipants(){
        Vector v = new Vector();
        for(int i=0;i<participants.size();i++){
            Participant p = (Participant)participants.elementAt(i);
            if(p.isConnected()){
                v.addElement(p);
            }
        }
        return v;      
    }
    
    /**
     * Adds participant to the Vector containing all the participants that are polled for incoming messages
     * and that are sent messages.
     * 
     * 
     * @param Participant that has just joined the Conversation and completed login
     */
    public void addNewParticipant(Participant p){
        participants.addElement(p);
        
        //c.getPermissionsController().addConnectingToAllInTheirNextFreeWindow(p,ownWindowNumber,permissionsEnabled);
    }
    
    /**
     * Searches for a participant by email (case insensitive)
     * @param email to search for
     * @return null | Participant
     */
    public Participant findParticipantWithEmail(String participantemail){
        for (int i=0;i<participants.size();i++){


            Participant p = (Participant)participants.elementAt(i);
            if(2>5&&Debug.debugGROOP)System.err.println("TRYING TO FIND PARTICIPANTWITHEMAIL"+participantemail+"--"+p.getParticipantID()+"--"+p.getUsername());
            if (p.getParticipantID().equalsIgnoreCase(participantemail))return p;
        }
         if(2>5&&Debug.debugGROOP)System.err.println("RETURNING NULL FOR "+participantemail);
        return null;
    }
    
     /**
     * Searches for a participant by username (case insensitive)
     * @param email to search for
     * @return null | Participant
     */
    public Participant findParticipantWithUsername(String participantusername){
        for (int i=0;i<participants.size();i++){
            Participant p = (Participant)participants.elementAt(i);
            if (p.getUsername().equalsIgnoreCase(participantusername))return p;
        }
        return null;
    }
    
    /**
     * Sends message to participants
     * @param recipients
     * @param m
     */
    public void sendMessageToParticipantsDeprecated(Vector recipients,Message m){
        for(int i=0;i<recipients.size();i++){
            Participant p = (Participant)recipients.elementAt(i);
            m.timeStamp();
            p.sendMessage(m);
        }
    }
    
    /**
     * Sends a turn to the specified recipients. The turn can either be sent by another participant or by the
     * "server", in which case it is an intervention.
     * 
     * 
     * @param recipients Vector containing all the Participants who are to receive the intervention
     * @param senderEmail String describing the email of the sender. If it is an intervention, the email will be server
     * @param senderUsername String describing the username of the sender. If it is an intervention, the username will be "sender".
     * @param type NOT USED
     * @param text text to be displayed in the chat window
     * @param windowNumbers Vector containing info for each client in which chat text is to appear. In default mode this should be 0.
     * @param prefixUsername String which is prefixed at the beginning of the turn (apparent origin)
     */
    public void sendChatTextToParticipantsDEPRECATED(Vector recipients,  String senderEmail, String senderUsername,int type, String text, Vector windowNumbers,boolean prefixUsername){
        for(int i=0;i<recipients.size();i++){
            Participant p  = (Participant)recipients.elementAt(i);
            int windowNumber = (Integer)windowNumbers.elementAt(i);
            //MessageChatTextToClient msccttc = new MessageChatTextToClient(senderEmail,senderUsername,0,text,windowNumber,prefixUsername);
           // msccttc.timeStamp();
            //p.sendMessage(msccttc);
        } 
    }
    
    /**
     * 
     * Updates the String displayed in the status bar of the clients. A message is only sent if the message
     * is different from what is currently being displayed.
     * 
     * @param recipients Vector containing all the Participants who are to have their status bars updated
     * @param text text to display in the status window
     * @param windowNumbers Vector containing info for each client in which chat text is to appear. In default mode this should be 0.
     * @param labelIsRed whether label is red/black
     */
    public void sendLabelDisplayToParticipants(Vector recipients, String text, Vector windowNumbers,boolean labelIsRed){
        for(int i=0;i<recipients.size();i++){
            Participant p  = (Participant)recipients.elementAt(i);
            int windowNumber = 0;
            try{  windowNumber = (Integer)windowNumbers.elementAt(i);} catch (Exception e){
                   windowNumber = 0;
            }
            MessageStatusLabelDisplay mld = new MessageStatusLabelDisplay("server","server",windowNumber,text,labelIsRed);
            mld.timeStamp();
            //
            boolean hasAlreadyBeenSent = lm.isLabelAlreadySetToValue_AndIfNotUpdateIt_IgnoreEnableDisableStatus(p,windowNumber,text);
            if(!hasAlreadyBeenSent){
                p.sendMessage(mld);
            }
            else{
                //System.out.println("--------------- "+text+" has already been sent to "+p.getUsername()+"windowno "+windowNumber);
            }
            //
            //p.sendMessage(mld);
        } 
    }

     public void sendAndEnableLabelDisplayToParticipant(Participant p, String text, boolean labelIsRed,boolean isEnabled){
         Vector v = new Vector();
         v.addElement(p);
         Vector vWindows = new Vector();
         vWindows.addElement(new Vector(new Integer(0)));
         this.sendAndEnableLabelDisplayToParticipants(v, text, vWindows, labelIsRed, isEnabled);
    }

     public void sendLabelDisplayToParticipant(Participant p, String text, boolean labelIsRed){
         Vector v = new Vector();
         v.addElement(p);
         Vector vWindows = new Vector();
         vWindows.addElement(new Vector(new Integer(0)));
         this.sendLabelDisplayToParticipants(v, text, vWindows, labelIsRed);
    }



    /**
     * Updates the String displayed in the status bar of the clients. It also specifies whether text entry is to be blocked or enabled.
     * Should be used sparingly (for interventions) and not for sending "is typing" updates, as this will quickly 
     * lead to very large data files being stored on the disc. This is because this method does NOT check whether the message
     * has already been sent. see {@link sendLabelDisplayToParticipants()}
     * 
     * 
     * 
     *  
     * @param recipients Vector containing all the Participants who are to have their status bars updated
     * @param text text to display in the status window
     * @param windowNumbers Vector containing info for each client in which chat text is to appear. In default mode this should be 0.
     * @param labelIsRed whether label is red/black
     * @param isEnabled w
     */
    public void sendAndEnableLabelDisplayToParticipants(Vector recipients, String text, Vector windowNumbers,boolean labelIsRed,boolean isEnabled){
          for(int i=0;i<recipients.size();i++){
            Participant p  = (Participant)recipients.elementAt(i);
            int windowNumber=0;
            try{  windowNumber = (Integer)windowNumbers.elementAt(i);} catch (Exception e){
                   windowNumber = 0;
            }
            MessageStatusLabelDisplayAndBlocked mld = new MessageStatusLabelDisplayAndBlocked("server","server",windowNumber,text,labelIsRed,isEnabled);
            mld.timeStamp();
            boolean hasAlreadyBeenSent = lm.isLabelAlreadySetToValueAndEnabledDisabledAndColour_AndIfNotUpdateIt(p,windowNumber,text,labelIsRed,isEnabled);
                if(!hasAlreadyBeenSent){
                  System.err.println("SENDING TO---"+p.getUsername());
                  p.sendMessage(mld);
                  //Conversation.printWln("DEBUG", "SENDING MESSAGELABELPARTICIPANTS: "+text+ "to "+p.getUsername());
                  //return;
            }
            //Conversation.printWln("DEBUG", "NOT SENDING MESSAGELABELPARTICIPANTS: "+text+ "to "+p.getUsername());
            //System.exit(-1231231231);
        }
        //  System.exit(-33);
     }         
        
        
    /**
     * 
     * Sends instructions to the clients to insert specified text at a specific position.
     * 
     * @param recipients Participants whose displays are to be updated
     * @param sender the origin of the insert (either other Participant or server)
     * @param windowNumbers window in which insert is to be performed
     * @param text text to be inserted
     * @param offset offset (from beiginning of text | from end of text)
     * @param length
     */
    public void sendWYSIWYGTextInsertedToParticipants(Vector recipients, String sender,Vector windowNumbers,String text,int offset,int length){
     for(int i=0;i<recipients.size();i++){
            
            Participant p  = (Participant)recipients.elementAt(i);
            //System.out.println("SERVER SENDING INSERT TO: "+p.getUsername());
            int windowNumber = (Integer)windowNumbers.elementAt(i);
            MessageWYSIWYGDocumentSyncToClientInsert mWYSIWYGTC = new MessageWYSIWYGDocumentSyncToClientInsert("server","server",windowNumber,text,offset, length);
            mWYSIWYGTC.timeStamp();
            p.sendMessage(mWYSIWYGTC);
            //String email, String username, String textAppended, int offset)
        }     
     }
     
    /**
     * 
     * Sends instructions to the clients to remove text at a specific position.
     * 
     * @param recipients Participants whose displays are to be updated
     * @param sender the origin of the remove (either other Participant or server)
     * @param windowNumbers window in which remove is to be performed
     * @param offset offset (from beiginning of text | from end of text)
     * @param length
     */
    public void sendWYSIWYGTextRemovedToParticipants(Vector recipients, String sender,Vector windowNumbers,int offset,int length){
     for(int i=0;i<recipients.size();i++){
            Participant p  = (Participant)recipients.elementAt(i);
            int windowNumber = (Integer)windowNumbers.elementAt(i);
            MessageWYSIWYGDocumentSyncToClientRemove mWYSIWYGTC = new MessageWYSIWYGDocumentSyncToClientRemove("server","server",windowNumber,offset, length);
            mWYSIWYGTC.timeStamp();
            p.sendMessage(mWYSIWYGTC);
            //String email, String username, String textAppended, int offset)
        }     
     }
    
    /**
     * Sends instructions to update the cursor position / highlighted text in the chatwindow
     * 
     * @param recipients Participants whose displays are to be updated
     * @param sender the origin of the selection change (either other Participant or server)
     * @param windowNumbers window in which change is to be performed
     * @param startIndex offset of start of highlighted area
     * @param finishIndex  offset of end of highlighted area
     */
    public void sendWYSIWYGSelectionChangedToParticipants(Vector recipients,String sender, Vector windowNumbers,int startIndex,int finishIndex){
          for(int i=0;i<recipients.size();i++){
            Participant p  = (Participant)recipients.elementAt(i);
            int windowNumber = (Integer)windowNumbers.elementAt(i);
            MessageWYSIWYGTextSelectionToClient mWYSIWYGSelChanged = new MessageWYSIWYGTextSelectionToClient("server", "server",windowNumber,startIndex,finishIndex);
            mWYSIWYGSelChanged.timeStamp();
            p.sendMessage(mWYSIWYGSelChanged);             
          }
    }
     
    /**
     * Sends status message indicating their status on the "conversational floor", which determines whether
     * the participant is allowed to type or not.
     * 
     * @param recipients Participants whose conversational statuses are to be updated
     * @param sender the origin of the change (this is handled by the server)
     * @param newStatus new conversational status
     * @param msgPrefix String prefixed to the message (for example to prefix a new turn with participant's name)
     */
    public void sendWYSIWYGChangeInterceptionStatusToParticipants(Vector recipients,String sender,int newStatus,String msgPrefix){
        for(int i=0;i<recipients.size();i++){
            Participant p  = (Participant)recipients.elementAt(i);
            MessageWYSIWYGChangeTurntakingStatus mWYSIWYGIS = new MessageWYSIWYGChangeTurntakingStatus("server","server",newStatus, msgPrefix);
            mWYSIWYGIS.timeStamp();
            p.sendMessage(mWYSIWYGIS);
        }    
    }
    
    /**
     * Sends message to individual participant
     * @param p recipient
     * @param m sender
     */
    public void sendMessageToParticipant(Participant p, Message m){
        //System.out.println("SINL"+p.getUsername());
        m.timeStamp();
         //System.out.println("SINM"+p.getUsername());
        p.sendMessage(m);
        //System.out.println("SINN"+p.getUsername());
    }
    
    /**
     * Returns a Vector containing all other participants taking part in the Conversation 
     * 
     * @param p participant to search for
     * @return Vector containing all the other participants
     */
    public Vector getAllOtherParticipants(Participant p){
        Vector v = new Vector();
        v.addAll(participants);
        v.remove(p);
        return v;
    }
    
    
    
    /**
     * Attempts to close down the Thread which loops through all the participants, polling them for incoming messages and also attempts
     * to close down the individual participants' incoming and outgoing connections.
     * 
     * @param sendCloseToClients
     */
    public void closeDown(boolean sendCloseToClients){
        for(int i=0;i<participants.size();i++){
            Participant p = (Participant)participants.elementAt(i);
            try{
                if(sendCloseToClients)p.sendMessage(new MessageClientCloseDown("server","server"));
                p.getConnection().setConnected(false);             
            }catch (Exception e){}
        }
        enqueingMessages=false;
        participants = null;
        c =null;
        incomingMessageQueue = null;
    }
    
    public void displayNEWWebpage(Participant p, String id, String header,String url, int width, int height,boolean vScrollBar,boolean forceCourierFont){
        MessageDisplayNEWWebpage mdnw = new MessageDisplayNEWWebpage(id,header,url,width,height,vScrollBar,false);
        this.sendMessageToParticipant(p, mdnw);
    }
    
    public void changeWebpage(Participant p, String id,String url){
        MessageDisplayChangeWebpage mdcw = new MessageDisplayChangeWebpage(id,url);
        this.sendMessageToParticipant(p, mdcw);   
    }

    public void changeWebpage(Participant p,String id, String url, String prepend, String append) {
        MessageDisplayChangeWebpage mdcw = new MessageDisplayChangeWebpage(id,url,prepend,append);
        this.sendMessageToParticipant(p, mdcw);
    }
    public void changeJProgressBar(Participant p,String id, String text, Color foreCol, int value) {
        //System.out.println("SINI");
        MessageDisplayChangeJProgressBar mdcjpb = new MessageDisplayChangeJProgressBar(id,text,foreCol,value);
        // System.out.println("SINJ");
        this.sendMessageToParticipant(p, mdcjpb);
         //System.out.println("SINK");
    }
    
     public void closeWebpageWindow(Participant p, String id){
        MessageDisplayCloseWindow mdcw = new MessageDisplayCloseWindow(id);
        this.sendMessageToParticipant(p, mdcw);
    } 


     public synchronized void changeParticipants(Vector newList){
         if(newList.size()!=this.participants.size()){
             System.err.println("NOT GOOD - SORTEDPARTICIPANTS DOESNT HAVE SAME SIZE "+newList.size()+" vs. "+participants.size());
             Conversation.printWSln("Main", "NOT GOOD - SORTEDPARTICIPANTS DOESNT HAVE SAME SIZE "+newList.size()+" vs. "+participants.size());

         }
         this.participants = newList;
     }

}

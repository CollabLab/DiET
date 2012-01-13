
package diet.server;

import java.io.File;
import java.util.Date;
import java.util.Vector;

import bsh.Interpreter;
import client.MazeGameController2WAY;
import client.MazeGameController3WAY;
import diet.debug.Debug;
import diet.message.Message;
import diet.message.MessageCBYCChangeTurntakingStatusConfirm;
import diet.message.MessageCBYCDocChangeFromClient;
import diet.message.MessageCBYCTypingUnhinderedRequest;
import diet.message.MessageChangeClientInterfaceProperties;
import diet.message.MessageChatTextFromClient;
import diet.message.MessageChatTextToClient;
import diet.message.MessageClientSetupParameters;
import diet.message.MessageErrorFromClient;
import diet.message.MessageKeypressed;
import diet.message.MessagePopup;
import diet.message.MessageTask;
import diet.message.MessageWYSIWYGChangeTurntakingStatus;
import diet.message.MessageWYSIWYGDocumentSyncFromClientInsert;
import diet.message.MessageWYSIWYGDocumentSyncFromClientRemove;
import diet.message.MessageWYSIWYGTextSelectionFromClient;
import diet.message.MessageWYSIWYGTypingUnhinderedRequest;
import diet.message.MessageWithinTurnDocumentSyncFromClientInsert;
import diet.message.MessageWithinTurnDocumentSyncFromClientRemove;
import diet.message.MessageWithinTurnDocumentSyncFromClientReplace;
import diet.message.MessageWithinTurnTextSelectionFromClient;
import diet.parameters.ExperimentSettings;
import diet.parameters.ui.SavedExperimentsAndSettingsFile;
import diet.server.CbyC.DocChange;
import diet.server.CbyC.DocInsert;
import diet.server.CbyC.DocRemove;
import diet.server.ConversationController.CCBeanShell;
import diet.server.ConversationController.DefaultConversationController;
import diet.server.conversationhistory.ConversationHistory;
import diet.server.experimentmanager.EMUI;
import diet.server.io.ConversationIO;
import diet.task.TaskController;
import diet.task.tangram2D1M.TangramGameController;
import java.awt.Color;


/**
 * This is the main server class. Each experiment has an associated Conversation object. The Conversation object
 * acts as the intermediary between the clients. It constantly checks for incoming messages, and relays them to a
 * ConversationController object that determines whether the message should be transformed.
 *
 * The Conversation object contains methods that should be used to send messages to clients.
 *
 * @author user
 */


public class Conversation extends Thread{

    private ConversationHistory cH;
    private DefaultConversationController cC;
    private ConversationUIManager cHistoryUIM;
    private WindowAndRecipientsManager permissionsController;   //See also Permissions.java
    private Participants ps;
    private boolean conversationIsActive = true;
    private boolean conversationThreadHasTerminated=false;
    private String conversationIdentifier;
    private DocChangesIncoming turnsconstructed = new DocChangesIncoming(this);
    private WordNetWrapper wnw;// = new WordNetWrapper();
    private ConversationIO convIO;
    private ExperimentSettings expSettings;
    private ExperimentManager expManager;
    private TaskController tc; //= new MazeGameController(this);

    static Conversation statC;



    /**
     * Constructor for interventions programmed with BeanShell. Using this is not really recommended
     * @param expM
     * @param i
     */
    public Conversation(ExperimentManager expM, Interpreter i){
        statC=this;
        try{
          this.expManager=expM;
          expSettings = (ExperimentSettings)i.eval("getExperimentParameters()");

          i.set("cH", cH);
          i.set("c",this);
          i.set("ps", ps);
          i.set("expSett",expSettings);

         doBasicSetup();
         cC = new CCBeanShell(this,expSettings,i);
         i.set("cC", cC);
         cC.setControllerAndStartIntercept(cC);

         this.expManager.emui.println("Main", "Experiment: "+this.getConversationIdentifier()+" setup successfully. Waiting for Participants to login");
       }catch(Exception e){
          System.err.println("ERROR INITIALIZING CONVERSATION: "+e.getMessage().toString());
      }

  }



  /**
   * Preferred constructor
   * @param expM ExperimentManager
   * @param expSettings settings of experiment
   */
  public Conversation(ExperimentManager expM,ExperimentSettings expSettings){
       statC=this;
       this.expSettings=expSettings;
        this.expManager=expM;
        doBasicSetup();
        if(cC==null){
            System.err.println("NO CONTROLLER");
            //System.exit(-200);
            if(this.expManager.emui!=null){
               this.expManager.emui.println("Main", "Experiment: "+this.getConversationIdentifier()+" could not start. Could not find ConversationController");
            }
        }
        if(this.expManager.emui!=null){
           this.expManager.emui.println("Main", "Experiment: "+this.getConversationIdentifier()+" setup done. Waiting for Participants to login");
        }
     }

    /**
     *
     * This method retrieves the individual settings for the Conversation by extracting
     * the {@link diet.parameters.Parameter} objects from {@link diet.parameters.ExperimentSettings}.
     * It instantiates the correct {@link ConversationController} using dynamic class loading if necessary, {@link TaskController} (if any), ensures the data will be saved
     * in a particular directory.
     *
     * <p>After initializing the components, it starts the ConversationController Thread.
     *
     * <p>Editing this method has to be done carefully. The sequence of the objects that are initialized is very
     * important as there are many complex interdependencies.
     *
     */
    public void doBasicSetup(){
        //Editing this method has to be done carefully
        //The sequence of the objects that are initialized
        //Is very important..

        conversationIdentifier = (String)expSettings.getV("Experiment ID");


        wnw = new WordNetWrapper(System.getProperty("user.dir")+File.separator+"utils");

        //String chattoolinterface = (String)expSettings.getV("Chat tool interface");
        String cCType = (String)expSettings.getV("Conversation Controller");
        try{
            cCType = cCType.trim();
            Class c = Class.forName( "diet.server.ConversationController."+cCType);
            DefaultConversationController dcc = (DefaultConversationController)c.newInstance();
            dcc.initialize(this, expSettings);
            cC=dcc;
           }catch(Exception e){
                  System.err.println("COULD NOT FIND AND DYNAMICALLY LOAD CONVERSATION CONTROLLER");
                  e.printStackTrace();
                  if(this.expManager.emui!=null){
                    this.expManager.emui.print("Main","Could not dynamically load "+cCType);
                    e.printStackTrace();

                  }else{
                      System.err.println("Could not dynamically load "+cCType);
                  }
           }
        convIO = new ConversationIO(this,System.getProperty("user.dir")+File.separator+"data"+File.separator+"Saved experimental data",conversationIdentifier,cC.getTurnTypeForIO());
        SavedExperimentsAndSettingsFile.writeAllParameterObjects(new File(convIO.getFileNameContainingConversationData(),"expparameters.xml"), expSettings.getParameters());
        ////String parserFileLocation = (String)expSettings.getV("Parser file location");
        String parserFileLocation = System.getProperty("user.dir")+File.separator+"utils"+File.separator+"englishPCFG.ser.gz";
        cH= new ConversationHistory(this,conversationIdentifier,  parserFileLocation, convIO);
        cHistoryUIM = new ConversationUIManager(cH,this);

        cH.setConversationUIManager(cHistoryUIM);
        ps = new Participants(this);

        int maxNumberOfParticipants = (Integer)expSettings.getV("Number of participants per conversation");
        String windowNumberingPolicy = (String)expSettings.getV("Window numbering policy");

        permissionsController = new WindowAndRecipientsManager(this,ps,maxNumberOfParticipants,windowNumberingPolicy);
        //ps.start();
        if(cC!=null) {
            cC.setControllerAndStartIntercept(cC);
        }
        String taskC = (String)expSettings.getV("TaskController");
        if(taskC!=null){
          if(taskC.equalsIgnoreCase("Maze2"))this.tc=new MazeGameController2WAY(this);
          else if(taskC.equalsIgnoreCase("Maze3"))this.tc=new MazeGameController3WAY(this);
          else if(taskC.equalsIgnoreCase("Tangram2D1M"))
          {
              String experimentID=(String)expSettings.getV("Experiment ID");
              System.out.println("experiment id is:"+experimentID);

                  this.tc=new TangramGameController(this, experimentID);
          }

        }
        this.expManager.connectUIWithExperimentManager(this,cHistoryUIM);

    }

    /**
     * Returns the collection of tables that are associated with this Conversation
     * @return ConversationUIManager
     */
    public ConversationUIManager getCHistoryUIM() {
        return cHistoryUIM;
    }



    public synchronized boolean requestPermissionForNewParticipantToBeAddedToConversation(String participantID){
        return cC.requestParticipantJoinConversation(participantID);
    }




    /**
     * Adds participant to the Conversation and sends the participant a MessageClientSetupParameters message with
     * the necessary window and client settings.
     *
     * @param p Participant to be added to the Conversation
     */
    public synchronized boolean addNewParticipant(Participant p){


        //Possible thread error if this thread sleeps inbetween adding participant and sending the default message
        //As the conversation might send a message before the participant has received the chat window setup.


        //Get the information from the Permissions
        //create the send client setup parameters

        //Ensure that the permissions are set up properly
        //There also has to be a script detailing how any new participant is
        //Dealt with, whether the participant that is newly added will be enabled
        //And who the participant can receive from
        //
        //Ensure that participant being added doesn't have the same name'
      try{
        ps.addNewParticipant(p);
        //Permission perm = ps.getPermissions(Participant p);
        int ownWindowNumber = permissionsController.getWindownumberInWhichASendsToB(p,p);
        MessageClientSetupParameters mcsp = diet.message.MessageClientSetupParameterFactory.generateClientChatToolSetupMessage(ownWindowNumber,expSettings);
        mcsp.setNewEmailAndUsername(p.getParticipantID(),p.getUsername());
        p.sendMessage(mcsp);
        System.out.println("added new participant "+p.getParticipantID());
        if(this.expManager.emui!=null){
           this.expManager.emui.println("Main", "Participant with email: "+p.getParticipantID()+" and with username: "+p.getUsername()+" has logged in to "+this.getConversationIdentifier()+" there are "+ps.getAllParticipants().size());
        }
      }catch (Exception e){
         System.err.println("Problem adding new participant");
      }
      if(tc!=null){
          tc.participantJoinedConversation(p);
      }
      cC.participantJoinedConversation(p);
      return true;
      }

    /**
     * This method still needs to be implemented and verified.
     * @param p
     */
    public void reactivateParticipant(Participant p){

       try{
        Conversation.printWSln("Main", "LOGGINGA");
        int ownWindowNumber = permissionsController.getWindownumberInWhichASendsToB(p,p);
        Conversation.printWSln("Main", "LOGGINGB");
        MessageClientSetupParameters mcsp = diet.message.MessageClientSetupParameterFactory.generateClientChatToolSetupMessage(ownWindowNumber,expSettings);
        Conversation.printWSln("Main", "LOGGINGC");
        mcsp.setNewEmailAndUsername(p.getParticipantID(),p.getUsername());
        Conversation.printWSln("Main", "LOGGINGD");
        p.sendMessage(mcsp);
        Conversation.printWSln("Main", "LOGGINGE");
        cC.participantRejoinedConversation(p);
        Conversation.printWSln("Main", "LOGGINGF");
        Conversation.printWSln("Main", "Participant "+p.getParticipantID()+" reactivated ");
      }catch (Exception e){
          System.err.println("Problem reactivating participant");
          e.printStackTrace();
      }
   }







    /**
     * Loop that polls {@link Participants} for any incoming messages. On receiving a message it calls the corresponding
     * methods in {@link diet.server.ConversationController}.
     *
     */
    @Override
    public void run(){
        System.out.println("Starting conversationcontroller");
        while (isConversationIsActive()){
          try{
             if(Debug.debugBLOCKAGE){System.out.println("MCT4");System.out.flush();}
             Message m = (Message) ps.getNextMessage();
             
             
             
              if(Debug.debugBLOCKAGE){System.out.println("MCT66601");System.out.flush();}
             if(Debug.debugBLOCKAGE)System.err.println("AWAKE1");

             if (m!=null){
               if(Debug.debugBLOCKAGE){System.out.println("MCT66602");System.out.flush();}
               cHistoryUIM.updateControlPanel(m);
               if(Debug.debugBLOCKAGE){System.out.println("MCT66603");System.out.flush();}
               convIO.saveMessage(m);
               if(Debug.debugBLOCKAGE){System.out.println("MCT66607777m");System.out.flush();}
               if(Debug.trackCBYCDyadError){
                   Conversation.printWSln("MESSAGEIN", "Saving Mesage:"+ m.getClass().toString());
               }
                if(Debug.debugBLOCKAGE){System.out.println("MCT6660888888mm");System.out.flush();}
               Participant origin = ps.findParticipantWithEmail(m.getEmail());
                if(Debug.debugBLOCKAGE){System.out.println("MCT66609999999m");System.out.flush();}
               //System.out.println("UPDATINGCONTROLPANEL");

                if (m instanceof MessageChatTextFromClient) {
                    MessageChatTextFromClient msctfc = (MessageChatTextFromClient)m;
                    //MessageChatTextToClient msccttc = new MessageChatTextToClient(msctfc.getEmail(),msctfc.getUsername(),0,msctfc.getText(),1);
                    //cHistoryUIM.updateChatToolTextEntryFieldsUI(msctfc);
                    //System.out.println("Received message onset"+msctfc.getTypingOnset()+":"+msctfc.getEndOfTyping());
                   // System.out.println("MCT1");
                    if(Debug.debugBLOCKAGE){System.out.println("MCT66609999999m22222");System.out.flush();}
                    cC.processChatText(origin,msctfc);
                    if(Debug.debugBLOCKAGE){System.out.println("MCT2");System.out.flush();}
                    //sendMessageToAllOtherParticipants(p, msccttc);
                }
                else if (m instanceof MessageKeypressed){
                    //System.out.println("Received message keypress");
                    MessageKeypressed mkp = (MessageKeypressed)m;

                    //this.cHistoryUIM.updateChatToolTextEntryFieldsUI(mkp);

                    String txtEntered = mkp.getContentsOfTextEntryWindow();
                    if(txtEntered!=null){
                        if(txtEntered.length()>0){
                           char txtEnteredLastChar = txtEntered.charAt(txtEntered.length()-1);
                          // if(Character.isWhitespace(txtEnteredLastChar))this.cHistoryUIM.parseTreeAndDisplayOfParticipant(origin,txtEntered);
                        }
                    }
                    System.out.println(origin.getParticipantID());
                    System.out.println(mkp.getKeypressed());
                    cC.processKeyPress(origin,mkp);

                    //MessageStatusLabelDisplayAndBlocked sld = new MessageStatusLabelDisplayAndBlocked("server", "server",1,m.getUsername()+"... is Typing",false,true);
                    //sendMessageToAllOtherParticipants(p,sld);
                }
                else if (m instanceof MessageWYSIWYGDocumentSyncFromClientInsert){
                    
                    MessageWYSIWYGDocumentSyncFromClientInsert mWYSIWYGkp = (MessageWYSIWYGDocumentSyncFromClientInsert)m;
                    getDocChangesIncoming().addInsert(origin,mWYSIWYGkp.getTextToAppendToWindow(),mWYSIWYGkp.getOffset(),mWYSIWYGkp.getTimeStamp().getTime());
                    cC.processWYSIWYGTextInserted(origin,mWYSIWYGkp);

                    char txtToInsert = mWYSIWYGkp.getTextToAppendToWindow().charAt(mWYSIWYGkp.getTextToAppendToWindow().length()-1);
                    if(Character.isWhitespace(txtToInsert)){
                    String txtEntered = getDocChangesIncoming().getTurnBeingConstructed(origin).getParsedText();
                    if(txtEntered!=null){
                        if(txtEntered.length()>0){
                           char txtEnteredLastChar = txtEntered.charAt(txtEntered.length()-1);
                           if(Character.isWhitespace(txtEnteredLastChar))this.cHistoryUIM.parseTreeAndDisplayOfParticipant(origin,txtEntered);
                        }
                      }
                    }
                    this.cHistoryUIM.updateChatToolTextEntryFieldsUI(origin);
                    
                }
                else if (m instanceof MessageWYSIWYGDocumentSyncFromClientRemove){
                    MessageWYSIWYGDocumentSyncFromClientRemove mWYSIWYGkp = (MessageWYSIWYGDocumentSyncFromClientRemove)m;
                    getDocChangesIncoming().addRemove(origin,mWYSIWYGkp.getOffset(),mWYSIWYGkp.getLength(),mWYSIWYGkp.getTimeStamp().getTime());
                    cC.processWYSIWYGTextRemoved(origin,mWYSIWYGkp);

                    String txtEntered = getDocChangesIncoming().getTurnBeingConstructed(origin).getParsedText();
                    if(txtEntered!=null){
                        if(txtEntered.length()>0){
                           char txtEnteredLastChar = txtEntered.charAt(txtEntered.length()-1);
                           if(Character.isWhitespace(txtEnteredLastChar))this.cHistoryUIM.parseTreeAndDisplayOfParticipant(origin,txtEntered);
                        }
                    }
                    this.cHistoryUIM.updateChatToolTextEntryFieldsUI(origin);
                }

                else if (m instanceof MessageWYSIWYGTextSelectionFromClient){
                    MessageWYSIWYGTextSelectionFromClient mwysts = (MessageWYSIWYGTextSelectionFromClient) m;
                    cC.processWYSIWYGSelectionChanged(origin,mwysts);
                    //MessageWYSIWYGTextSelectionToClient mwstsend = new MessageWYSIWYGTextSelectionToClient(1, mwysts.getEmail(),
                    //        mwysts.getUsername(), mwysts.getCorrectedStartIndex(), mwysts.getCorrectedFinishIndex());
                    //sendMessageToAllOtherParticipants(p, mwstsend);
                }
                else if (m instanceof MessageWYSIWYGTypingUnhinderedRequest){
                   MessageWYSIWYGTypingUnhinderedRequest mwTUR = (MessageWYSIWYGTypingUnhinderedRequest)m;
                   cC.processWYSIWYGTypingUnhinderedRequest(origin, mwTUR);
                }

               else if (m instanceof MessageCBYCTypingUnhinderedRequest){
                   MessageCBYCTypingUnhinderedRequest mCTUR = (MessageCBYCTypingUnhinderedRequest)m;
                   cC.processCBYCTypingUnhinderedRequest(origin, mCTUR);
                   this.cHistoryUIM.updateChatToolTextEntryFieldsUI(origin, mCTUR);
                   if(Debug.showErrorsForMacIntegration)Conversation.printWSln("INC", "TYPINGUNHINDEREDREQUESTFROM from "+m.getUsername());
                   if(Debug.trackCBYCDyadError){
                     Conversation.printWSln("INC", "CALLEDCCC");
                   }
               }
               else if(m instanceof MessageCBYCDocChangeFromClient){
                   MessageCBYCDocChangeFromClient mCDC = (MessageCBYCDocChangeFromClient)m;
                   cC.processCBYCDocChange(origin,  mCDC);
                   this.cHistoryUIM.updateChatToolTextEntryFieldsUI(origin, mCDC);
                   if(Debug.showErrorsForMacIntegration)Conversation.printWSln("INC", "DOCCHANGEINCOMINGFROM " +m.getUsername());
                   if(Debug.trackCBYCDyadError){
                     Conversation.printWSln("MESSAGEIN", "CALLEDCCC2"+m.getUsername());
                   }
               }
               else if (m instanceof MessageCBYCChangeTurntakingStatusConfirm){

                   MessageCBYCChangeTurntakingStatusConfirm mConf = (MessageCBYCChangeTurntakingStatusConfirm)m;
                   if(Debug.showErrorsForMacIntegration)Conversation.printWSln("INC", "RECEIVEDSTATECHANGECONFIRM from" +m.getUsername()+" "+mConf.getNewStatus());
                   cC.processCBYCChangeTurnTakingStatusConfirm(origin, mConf);
                   this.cHistoryUIM.updateChatToolTextEntryFieldsUI(origin, mConf);
               }



                else if (m instanceof MessageWithinTurnDocumentSyncFromClientInsert){
                   MessageWithinTurnDocumentSyncFromClientInsert mws= (MessageWithinTurnDocumentSyncFromClientInsert)m;
                   cC.processWithinTurnTextInserted(origin, mws);
                }
                else if (m instanceof MessageWithinTurnDocumentSyncFromClientRemove){
                   MessageWithinTurnDocumentSyncFromClientRemove mws= (MessageWithinTurnDocumentSyncFromClientRemove)m;
                   cC.processWithinTurnTextRemoved(origin, mws);
                }
                else if (m instanceof MessageWithinTurnDocumentSyncFromClientReplace){
                    MessageWithinTurnDocumentSyncFromClientReplace mws= (MessageWithinTurnDocumentSyncFromClientReplace)m;
                    cC.processWithinTurnTextReplaced(origin, mws);
                }
               else if(m instanceof MessageWithinTurnTextSelectionFromClient){
                   MessageWithinTurnTextSelectionFromClient mws= (MessageWithinTurnTextSelectionFromClient)m;
                   cC.processWithinTurnTextSelectionChanged(origin, mws);
               }

                else if (m instanceof MessageErrorFromClient){
                    MessageErrorFromClient mefc = (MessageErrorFromClient)m;
                    this.printWln("Main", "ERROR IN CLIENT: "+m.getEmail()+" "+m.getUsername()+"\n"+mefc.getErrorMessage());
                    Throwable t = mefc.getThrowable();


                }
                else if (m instanceof MessagePopup){
                    try{
                        MessagePopup mp = (MessagePopup)m;
                       String[] options = mp.getOptions();
                       String question = mp.getQuestion();
                       String title = mp.getTitle();
                       String optionsFLATTENED = "";
                       for(int l=0;l<options.length;l++){
                           optionsFLATTENED = optionsFLATTENED+options[l];
                       }
                       
                       
                        String id = "Not yet set";
                        String username = "Not yet set";
                        id = mp.getEmail();
                        username = mp.getUsername();//origin.getUsername();
                        
                      
                       //System.out.println(id+username+"---------------------------------------------------");
                       //System.exit(-4);
                       
                       String s4 = ""+mp.getTimeStamp().getTime();
                       String s5 = ""+mp.getTimeStamp();
                       String s7 = optionsFLATTENED;
                       
                       
                       
                        this.saveDataToFile("POPUPRECEIVED", id,username, username, mp.getTimeStamp().getTime(), mp.getTimeStamp().getTime(),(title+"/"+question+"/"+optionsFLATTENED).replaceAll("\n","(NEWLINE)"), null);                        
                      
                        cC.processPopupResponse(origin, (MessagePopup)m);
                        
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    
                }
                
                
                
                
                else if(m instanceof MessageTask){
                    if(tc!=null){
                        tc.processTaskMove((MessageTask)m,origin);
                    }else{
                        //System.exit(-2345);
                    }
                }
                else{
                   //System.exit(-23456);
                }
         }

          if(Debug.debugBLOCKAGE){System.out.println("MCT44");System.out.flush();}
          }catch (Exception e){
               System.err.println(this.getConversationIdentifier()+ ": ERROR SOMEWHERE IN THE CONVERSATION CONTROLLER");
               printWln("Main","There is an ERROR in the Conversation Controller");
               printWln("Main","Check the ERRORLOG.TXT file in the directory containing");
               printWln("Main","the saved experimental data");
               printWln("Main",e.getMessage());
               convIO.saveErrorLog(e);
          }
       if(Debug.debugBLOCKAGE){System.out.println("MCT45");System.out.flush();}
      }
         if(Debug.debugBLOCKAGE){System.out.println("MCT46");System.out.flush();}
          System.err.println("THREAD TERMINATED");
          System.err.flush();
          conversationThreadHasTerminated=true;
    }







    /**
     * Sends a turn from a spoofed origin to a single Participant. It ensures that the turn is sent to the correct window
     * on the chat client and also saves the message to file.
     *
     * @param apparentOrigin "spoof" origin of turn
     * @param recipient recipient of turn
     * @param text text to appear
     */
    public void sendArtificialTurnFromApparentOriginToRecipient(Participant apparentOrigin,Participant recipient, String text){
        int windowNo = permissionsController.getWindownumberInWhichASendsToB(apparentOrigin, recipient);
        int styleno = cC.getStyleManager().getUniqueIntForRecipient(recipient, apparentOrigin);
        MessageChatTextToClient mctc = new MessageChatTextToClient("server",apparentOrigin.getUsername(),0,text,windowNo,true,styleno);
        //mctc.timeStamp();
        Vector recipientNames = new Vector();
        recipientNames.addElement(recipient.getUsername());
        Vector recipients = new Vector();
        recipients.addElement(recipient);
        ps.sendMessageToParticipant(recipient,mctc);
        cH.saveMessage("ARTIFICIALTURN",mctc.getTimeStamp().getTime(),mctc.getTimeStamp().getTime(),"server",apparentOrigin.getUsername(),text,recipientNames,false,null,null,0);
    }





     public void sendDelayedTextToAllOtherParticipants(final Participant sender,final String text,final long millisecdelay){
        //MessageChatTextToClient msccttc = new MessageChatTextToClient(mct.getEmail(),mct.getUsername(),0,mct.getText(),1);
        DocChangesIncomingSequenceFIFO ds = getDocChangesIncoming().setNewTurnAndReturnPreviousTurn(sender,new Date().getTime());
        Vector recipientsOfChatText = permissionsController.getPermittedChatTextRecipients(sender);
        final Vector pRecipients = (Vector)recipientsOfChatText.elementAt(0);
        final Vector pUsernames = (Vector)recipientsOfChatText.elementAt(2);
        //Vector pChatWindows = (Vector)recipientsOfChatText.elementAt(3);
        //ps.sendChatTextToParticipants(pRecipients,mct.getEmail(),mct.getUsername(),0,mct.getText(),pChatWindows,true);
        //cH.saveMessage(mct.getTypingOnset(),mct.getEndOfTyping(),mct.getUsername(),mct.getUsername(),mct.getText(),pUsernames,mct.hasBeenBlocked(),mct.getKeypresses(),ds.getAllInsertsAndRemoves(),0);
        //sendDelayedArtificialTurnFromApparentOriginToRecipients(sender, pRecipients, text,millisecdelay);
        //cH.saveMessage(new Date().getTime(),new Date().getTime(),"server",sender.getUsername(),mct.getText(),pRecipients,false,null,null,0);
        //cH.saveMessage(turnNotRelayed.getTypingOnset(),turnNotRelayed.getEndOfTyping(),p.getUsername(),p.getUsername(),ds.getParsedText(),new Vector(),false,null,ds.getAllInsertsAndRemoves(),0);
        Thread t = new Thread(){
            public void run(){
                try{
                    sleep(millisecdelay);
                    sendArtificialTurnFromApparentOriginToRecipients(sender, pRecipients, text);
                    //cH.saveMessage(new Date().getTime(),new Date().getTime(),"server",sender.getUsername(),text,pUsernames,false,null,null,0);

                }catch(Exception e){
                    printWln("Main","ERROR: Could not send the following delayed text: "+text);
                    printWln("Main","The error is: "+e.getMessage());
                }
            }
        };
        t.start();
     }





    /**
     * Sends a turn from a spoofed origin to a multiple Participants. It ensures that the turn is sent to the correct window
     * on the chat client and also saves the message to file.
     *
     * @param apparentOrigin "spoof" origin of turn
     * @param recipients Vector containing all the recipients of the turn.
     * @param text text to appear
     */
    public void sendArtificialTurnFromApparentOriginToRecipients(Participant apparentOrigin, Vector recipients, String text){
        Vector windowNumbers  = new Vector();
        Vector recipientNames = new Vector();
        for(int i=0;i<recipients.size();i++){
            Participant recipient = (Participant)recipients.elementAt(i);
            recipientNames.addElement(recipient.getUsername());
            int windowNo = permissionsController.getWindownumberInWhichASendsToB(apparentOrigin,recipient);
            windowNumbers.addElement(windowNo);
            this.sendArtificialTurnFromApparentOriginToRecipient(apparentOrigin, recipient, text);
        }
         cH.saveMessage("ARTIFICIALTURN",new Date().getTime(),new Date().getTime(),"server",apparentOrigin.getUsername(),text,recipientNames,false,null,null,0);
    }

    /**
     * Sends a turn to a single Participant. This method requires the programmer to specify which window the
     *
     * It ensures that the turn is sent to the correct window
     * on the chat client and also saves the message to file.
     *
     * @param windowNo
     * @param recipient Recipient of the turn.
     * @param text text to appear
     */
    public void sendArtificialTurnToRecipient(Participant recipient,String text, int windowNo){
        int styleno = cC.getStyleManager().getUniqueIntForRecipientNoSender(recipient);
        MessageChatTextToClient mctc = new MessageChatTextToClient("server","server",0,text,windowNo,false,styleno);
         //mctc.timeStamp();
         Vector recipientNames = new Vector();
         recipientNames.addElement(recipient.getUsername());
         Vector recipients = new Vector();
         recipients.addElement(recipient);
         ps.sendMessageToParticipant(recipient,mctc);
         cH.saveMessage("ARTIFICIALTURN",mctc.getTimeStamp().getTime(),mctc.getTimeStamp().getTime(),"server","server",text,recipientNames,false,null,null,0);
    }


    public void showPopupOnClientQueryInfo(Participant recipient, String question, String[] options, String title, int selection){
        MessagePopup mp = new MessagePopup ("server", "server", question, options, title, selection);
        ps.sendMessageToParticipant(recipient,mp);
        
        String questionCleaned = question.replaceAll("\n", "(NEWLINE)");
        String titleCleaned = title.replaceAll("\n", "(NEWLINE)");
        this.saveDataToFile("POPUPSENT", recipient.getParticipantID(), recipient.getUsername(), recipient.getUsername(), new Date().getTime(), new Date().getTime(),titleCleaned+"-"+questionCleaned, null);
        //this.saveDataToFile(title, title, question, selection, selection, title, null);
    }
    
    
    public void saveDataToConversationHistory(String data){
        MessageChatTextToClient mctc = new MessageChatTextToClient("server","server",0,data,0,false,-444);
        mctc.timeStamp();
        //mctc.timeStamp();
         String sender = "server";
         Vector recipients = new Vector();
         //if(cH==null)System.exit(-4);
         cH.saveMessage("DATA",mctc.getTimeStamp().getTime(),mctc.getTimeStamp().getTime(),"server","server",mctc.getText(),recipients,false,null,null,0);
    }

    
    public void saveDataToFile(String datatype, String senderID, String senderUsername, String recipient, long onset, long enter, String text, Vector additionalData){
        try{
            
           cH.saveDataToFile(datatype, onset, enter,senderID, senderUsername, recipient,text.replaceAll("\n", "(NEWLINE)"),new Vector(),false,null,null,0,additionalData);
             }catch (Exception e){
            Conversation.printWSln("Main", "Error saving data");
        }
    }
    
    public void saveDataToFile(String datatype, String senderID, String senderUsername, long onset, long enter, String text, Vector additionalData){
        try{
            
           cH.saveDataToFile(datatype, onset, enter,senderID, senderUsername,senderUsername,text.replaceAll("\n", "(NEWLINE)"),new Vector(),false,null,null,0,additionalData);
             }catch (Exception e){
            Conversation.printWSln("Main", "Error saving data");
        }
    }

    public void saveKeypressToFile(Participant sender, MessageKeypressed mkp){
        this.saveDataToFile("KEYPRESS", sender.getParticipantID(),sender.getUsername(), mkp.getTimeStamp().getTime(), new Date().getTime(), mkp.getContentsOfTextEntryWindow(),null);
    }

    public void saveKeypressDocumentchangeToFile(Participant sender, long timestamp, String contentsOfWindow){
        this.saveDataToFile("KEYPRESSDOCCHANGE", sender.getParticipantID(),sender.getUsername(), timestamp, timestamp, contentsOfWindow,null);
    }

    /**
     *
     * Checks the last time of typing of all participants. If most recent keypress exceeds the threshold, this method
     * sends a message to all permitted clients that participant is no longer typing.
     *
     * @param timeout time threshold of no typing activity. If threshold is exceeded,
     */
    public void resetFromIsTypingtoNormalChatAllAllowedParticipants(long timeout){
       Vector v = ps.getAllParticipants();
       Vector participantsWhoAreReceivingIsTyping = new Vector();
       for(int i=0;i<v.size();i++){
            Participant p = (Participant)v.elementAt(i);
            if(p.isTyping(timeout)){
               Vector recipientsOfChatText = permissionsController.getPermittedChatTextRecipients(p);
               Vector pRecipients = (Vector)recipientsOfChatText.elementAt(0);
               for(int j=0;j<pRecipients.size();j++){
                   Participant pRecp = (Participant)pRecipients.elementAt(j);
                   if (!participantsWhoAreReceivingIsTyping.contains(pRecp)){
                       participantsWhoAreReceivingIsTyping.addElement(pRecp);
                   }
               }
            }
        }

        Vector participantsToSendStatusOKMessageTo = new Vector();
        Vector participantsToSendStatusOKMessageToChatWindows = new Vector();

        for(int i=0;i<v.size();i++){
            Participant p = (Participant)v.elementAt(i);
            if(!p.isTyping(timeout)){
               Vector recipientsOfChatText = permissionsController.getPermittedChatTextRecipients(p);
               Vector pRecipients = (Vector)recipientsOfChatText.elementAt(0);
               Vector pUsernames = (Vector)recipientsOfChatText.elementAt(2);
               Vector pChatWindows = (Vector)recipientsOfChatText.elementAt(3);

               for(int j=0;j<pRecipients.size();j++){
                   Participant candidate = (Participant)pRecipients.elementAt(j);
                   Integer candidateChatWindow = (Integer)pChatWindows.elementAt(j);
                   if(!participantsWhoAreReceivingIsTyping.contains(candidate)){
                       participantsToSendStatusOKMessageTo.addElement(candidate);
                       participantsToSendStatusOKMessageToChatWindows.addElement(candidateChatWindow);
                   }
               }

               ps.sendLabelDisplayToParticipants(participantsToSendStatusOKMessageTo,"Status: OK",participantsToSendStatusOKMessageToChatWindows,false);
            }
        }

    }


    public void sendArtificialTurnToAllOtherParticipants(Participant sender, String text){
        Vector recipientsOfChatText = permissionsController.getPermittedChatTextRecipients(sender);
        Vector pRecipients = (Vector)recipientsOfChatText.elementAt(0);
        //Vector pUsernames = (Vector)recipientsOfChatText.elementAt(2);
        //Vector pChatWindows = (Vector)recipientsOfChatText.elementAt(3);
        this.sendArtificialTurnFromApparentOriginToRecipients(sender, pRecipients, text);
        //ps.sendChatTextToParticipants(pRecipients,sender.getParticipantID(),sender.getUsername(),0,text,pChatWindows,true);
        //cH.saveMessage(new Date().getTime(),new Date().getTime(),"server",sender.getUsername(),text,pUsernames,false,null,null,0);

    }



    public void relayTurnToParticipant(Participant sender, Participant recipient, MessageChatTextFromClient mct){
        //
        DocChangesIncomingSequenceFIFO ds = getDocChangesIncoming().setNewTurnAndReturnPreviousTurn(sender,new Date().getTime());
        int windowNo =0;
        int styleno = cC.getStyleManager().getUniqueIntForRecipient(recipient, sender);
        MessageChatTextToClient mctc = new MessageChatTextToClient(sender.getParticipantID(),sender.getUsername(),0,mct.getText(),windowNo,true,styleno);
        //mctc.timeStamp();
        Vector recipientNames = new Vector();
        recipientNames.addElement(recipient.getUsername());
        Vector recipients = new Vector();
        recipients.addElement(recipient);
        ps.sendMessageToParticipant(recipient,mctc);
        cH.saveMessage("Default",mct.getStartOfTyping(),mct.getEndOfTyping(),sender.getUsername(),sender.getUsername(),mct.getText(),recipientNames,false,mct.getKeypresses(),ds.getAllInsertsAndRemoves(),0);
   }



    /**
     * Relays turn from clients to all permitted participants. This should be the default method invoked to relay turns that are
     * not modified / blocked. It ensures that the message is stored and displayed properly in the UI.
     *
     * @param sender
     * @param mct
     */
    public void relayTurnToAllOtherParticipants(Participant sender,MessageChatTextFromClient mct){

        

        //MessageChatTextToClient msccttc = new MessageChatTextToClient(mct.getEmail(),mct.getUsername(),0,mct.getText(),1);
        DocChangesIncomingSequenceFIFO ds = getDocChangesIncoming().setNewTurnAndReturnPreviousTurn(sender,new Date().getTime());
        Vector recipientsOfChatText = permissionsController.getPermittedChatTextRecipients(sender);
        Vector pRecipients = (Vector)recipientsOfChatText.elementAt(0);
        Vector pUsernames = (Vector)recipientsOfChatText.elementAt(2);
        Vector pChatWindows = (Vector)recipientsOfChatText.elementAt(3);
        for(int i=0;i<pRecipients.size();i++){
            Participant recipient = (Participant)pRecipients.elementAt(i);
            int windowNo = (Integer)pChatWindows.elementAt(i);
            int styleno = cC.getStyleManager().getUniqueIntForRecipient(recipient, sender);
            MessageChatTextToClient mctc = new MessageChatTextToClient(sender.getParticipantID(),sender.getUsername(),0,mct.getText(),windowNo,true,styleno);
            ps.sendMessageToParticipant(recipient, mctc);
        }
        cH.saveMessage("Default",mct.getTypingOnset(),mct.getEndOfTyping(),mct.getUsername(),mct.getUsername(),mct.getText(),pUsernames,mct.hasBeenBlocked(),mct.getKeypresses(),ds.getAllInsertsAndRemoves(),0);
       }

    /**
     * Relays turn from clients to all permitted participants. This should be the default method invoked to relay turns that are
     * not modified / blocked. It ensures that the message is stored and displayed properly in the UI. Be very careful using this method
     * as it "breaks" the metaphor used for representing turns and experimental manipulation of turns. The basic metaphor is that normal
     * turns that appear "as is", i.e. from the origin, displayed with the proper name etc. are treated as basic. Any turn that is changed
     * in ANY way is treated as not being the "same" turn...leading to the original turn being saved as not transmitted to anyone, and a second
     * turn that originates from the server is recorded. This method turns this metaphor on its head, and should only be used if
     * a participant's name is consistently changed throughout the experiment.
     *
     *
     * @param sender
     * @param mct
     */
    public void relayTurnToAllOtherParticipantsWithAlteredOriginName(Participant sender,String alteredName,MessageChatTextFromClient mct){
        //MessageChatTextToClient msccttc = new MessageChatTextToClient(mct.getEmail(),mct.getUsername(),0,mct.getText(),1);
        DocChangesIncomingSequenceFIFO ds = getDocChangesIncoming().setNewTurnAndReturnPreviousTurn(sender,new Date().getTime());
        Vector recipientsOfChatText = permissionsController.getPermittedChatTextRecipients(sender);
        Vector pRecipients = (Vector)recipientsOfChatText.elementAt(0);
        Vector pUsernames = (Vector)recipientsOfChatText.elementAt(2);
        Vector pChatWindows = (Vector)recipientsOfChatText.elementAt(3);

        for(int i=0;i<pRecipients.size();i++){
            Participant recipient = (Participant)pRecipients.elementAt(i);
            int windowNo = (Integer)pChatWindows.elementAt(i);
            int styleno = cC.getStyleManager().getUniqueIntForRecipient(recipient, sender);
            MessageChatTextToClient mctc = new MessageChatTextToClient(sender.getParticipantID(),sender.getUsername(),0,mct.getText(),windowNo,true,styleno);
            ps.sendMessageToParticipant(recipient, mctc);
        }
        cH.saveMessage("AlteredOrigin",mct.getTypingOnset(),mct.getEndOfTyping(),sender.getUsername(),alteredName,mct.getText(),pUsernames,mct.hasBeenBlocked(),mct.getKeypresses(),ds.getAllInsertsAndRemoves(),0);

    }






    /**
     * Informs all other participants that are permitted to receive turns from the participant that participant is typing..
     * @param sender
     */
    public void informIsTypingToAllowedParticipants(Participant sender){//,Message m){
        Vector recipientsOfChatText = permissionsController.getPermittedChatTextRecipients(sender);
        Vector pRecipients = (Vector)recipientsOfChatText.elementAt(0);
        Vector pUsernames = (Vector)recipientsOfChatText.elementAt(2);
        Vector pChatWindows = (Vector)recipientsOfChatText.elementAt(3);
        ps.sendLabelDisplayToParticipants(pRecipients,sender.getUsername()+"...is typing",pChatWindows,false);
      }

    /**
     * Informs all other participants that are permitted to receive turns from the participant that participant is typing,
     * using a altered participant name (supplied)
     * @param sender
     * @param alteredName
     */
    public void informIsTypingToAllowedParticipantswithAlteredOriginName(Participant sender, String alteredName){//,Message m){
        Vector recipientsOfChatText = permissionsController.getPermittedChatTextRecipients(sender);
        Vector pRecipients = (Vector)recipientsOfChatText.elementAt(0);
        Vector pUsernames = (Vector)recipientsOfChatText.elementAt(2);
        Vector pChatWindows = (Vector)recipientsOfChatText.elementAt(3);
        ps.sendLabelDisplayToParticipants(pRecipients,alteredName+"...is typing",pChatWindows,false);
      }

    /**
     * Informs a participant that another participant IS typing
     *
     * @param sender Participant that appears to be typing
     * @param recipient Participant that receives the message that the other is typing
     */
    public void informParticipantBthatParticipantAIsTyping(Participant sender, Participant recipient){
        Vector recipients = new Vector();
        recipients.addElement(recipient);
        int chatWindow = permissionsController.getWindownumberInWhichASendsToB(sender,recipient);
        Vector chatWindows = new Vector();
        chatWindows.addElement(chatWindow);
        ps.sendLabelDisplayToParticipants(recipients,sender.getUsername()+"...is typing", chatWindows,false);
    }

    /**
     * Informs a participant that another participant is NOT typing
     *
     * @param sender Participant that appears to be not typing
     * @param recipient Participant that receives the message that the other is not typing
     */
    public void informParticipantBthatParticipantAIsNotTypingAndStatusIsOK(Participant sender, Participant recipient){
        Vector recipients = new Vector();
        recipients.addElement(recipient);
        int chatWindow = permissionsController.getWindownumberInWhichASendsToB(sender,recipient);
        Vector chatWindows = new Vector();
        chatWindows.addElement(chatWindow);
        ps.sendLabelDisplayToParticipants(recipients,"Status: OK", chatWindows,false);
    }


    public void sendAndEnableLabelDisplayToALLStatusWindowsOfParticipant(Participant recipient,String text,boolean isRed,boolean isEnabled){
        Vector otherParticipants = (Vector)permissionsController.getPermittedChatTextRecipients(recipient).elementAt(0);
        Vector recipients = new Vector();
        recipients.addElement(recipient);
        Vector recipientsWindows = new Vector();
        for(int i=0;i<otherParticipants.size();i++){
            Participant otherParticipant = (Participant)otherParticipants.elementAt(i);
            int windowNumber = permissionsController.getWindownumberInWhichASendsToB(otherParticipant, recipient);
            boolean foundWindow = false;
            for(int j=0;j<recipientsWindows.size();j++){
                Integer recipientWindow = (Integer)recipientsWindows.elementAt(j);
                if(recipientWindow.equals(windowNumber)){
                    foundWindow = true;
                    break;
                }
            }
            if(!foundWindow)recipientsWindows.addElement(windowNumber);
        }
        ps.sendAndEnableLabelDisplayToParticipants(recipients, text, recipientsWindows, isRed, isEnabled);

      }

    /**
     * Instructs client of particular participant to display a status message (in black/red) in the status bar asssociated with a particular Participant.
     * @param sender
     * @param text
     * @param isRed
     */



    /**
     * Instructs clients to display a status message (in black/red) in the status bar asssociated with a particular Participant.
     * @param sender
     * @param text
     * @param isRed
     */
    public void sendLabelDisplayToAllowedParticipantsFromApparentOrigin(Participant sender,String text,boolean isRed){
        Vector recipientsOfChatText = permissionsController.getPermittedChatTextRecipients(sender);
        Vector pRecipients = (Vector)recipientsOfChatText.elementAt(0);
        Vector pUsernames = (Vector)recipientsOfChatText.elementAt(2);
        Vector pChatWindows = (Vector)recipientsOfChatText.elementAt(3);
        ps.sendLabelDisplayToParticipants(pRecipients,text,pChatWindows,isRed);
      }

    /**
     * Instructs client of particular participant to display a status message (in black/red) in the status bar asssociated with a particular Participant.
     * @param sender
     * @param text
     * @param isRed
     */
    public void sendAndEnableLabelDisplayToAllowedParticipantsFromApparentOrigin(Participant sender, String text, boolean isRed, boolean isEnabled){
         Vector recipientsOfChatText = permissionsController.getPermittedChatTextRecipients(sender);
         Vector pRecipients = (Vector)recipientsOfChatText.elementAt(0);
         //Vector pUsernames = (Vector)recipientsOfChatText.elementAt(2);
         Vector pChatWindows = (Vector)recipientsOfChatText.elementAt(3);
         ps.sendAndEnableLabelDisplayToParticipants(pRecipients,text,pChatWindows,isRed,isEnabled);
    }

    public void sendAndEnableLabelDisplayToAllOtherParticipantsFromApparentOriginInOwnStatusWindow(Participant sender, String text, boolean isRed, boolean isEnabled){
         Vector recipients = ps.getAllOtherParticipants(sender);
         Vector windows = new Vector();
         for(int i=0;i<recipients.size();i++){
             windows.addElement(0);
             System.err.println(((Participant)recipients.elementAt(i)).getUsername());
         }
         //System.exit(-5);
         //Vector pRecipients = (Vector)recipientsOfChatText.elementAt(0);
         ps.sendAndEnableLabelDisplayToParticipants(recipients,text,windows,isRed,isEnabled);
    }


    public void sendLabelDisplayToParticipantInOwnStatusWindow(Participant p, String text, boolean labelIsRed){
         Vector v = new Vector();
         Vector windows = new Vector();
         v.addElement(p);
         windows.addElement(0);
         ps.sendLabelDisplayToParticipants(v, text, windows, labelIsRed);
    }

    /**
     * Sends a String to all clients that is displayed in the status window in which they see their own text.
     * @param text
     * @param labelIsRed
     */
    public void sendLabelDisplayToAllParticipantsInTheirOwnStatusWindow(String text,boolean labelIsRed){
         Vector v = ps.getAllParticipants();
         Vector windows = new Vector();
         for(int i=0;i<v.size();i++){
             Participant p = (Participant)v.elementAt(i);
             int windowNumber = permissionsController.getWindownumberInWhichASendsToB(p,p);
             windows.addElement(windowNumber);
         }
         ps.sendLabelDisplayToParticipants(v, text, windows, labelIsRed);
    }



    /**
     * Sends a String to a client that is displayed in the status window in which they see their own text.
     * @param text
     * @param labelIsRed
     * @param enable
     */
    public void sendLabelDisplayAndEnableToParticipantInOwnStatusWindow(Participant recipient,String text,boolean labelIsRed,boolean enable){
         int windowNumber = permissionsController.getWindownumberInWhichASendsToB(recipient,recipient);
         Vector windowNumbers = new Vector();
         windowNumbers.addElement(windowNumber);
         Vector v = new Vector();
         v.addElement(recipient);
         ps.sendAndEnableLabelDisplayToParticipants(v, text, windowNumbers, labelIsRed, enable);
    }

    
    public void changeClientInterface_allowENTERSEND(Participant recipient, boolean allowENTERSEND){
        if(allowENTERSEND){
           MessageChangeClientInterfaceProperties mccip= new MessageChangeClientInterfaceProperties(0,MessageChangeClientInterfaceProperties.enableRETURNANDSEND_enableTEXTENTRY);
           ps.sendMessageToParticipant(recipient, mccip);
        }
        else{
           MessageChangeClientInterfaceProperties mccip= new MessageChangeClientInterfaceProperties(0,MessageChangeClientInterfaceProperties.disableRETURNANDSEND_enableTEXTENTRY);
           ps.sendMessageToParticipant(recipient, mccip);
        }
    }

     public void changeClientInterface_clearTextEntryField(Participant recipient){
           MessageChangeClientInterfaceProperties mccip= new MessageChangeClientInterfaceProperties(0,MessageChangeClientInterfaceProperties.clearTextEntryField);
           ps.sendMessageToParticipant(recipient, mccip);
       
    }

    public void changeClientInterface_clearMaintextEntryWindow(Participant recipient){
           MessageChangeClientInterfaceProperties mccip= new MessageChangeClientInterfaceProperties(0,MessageChangeClientInterfaceProperties.clearMainTextWindow);
           ps.sendMessageToParticipant(recipient, mccip);
    }
    
    public void changeClientInterface_enableTextEntry(Participant recipient){
           MessageChangeClientInterfaceProperties mccip= new MessageChangeClientInterfaceProperties(0,MessageChangeClientInterfaceProperties.enableTextEntry);
           ps.sendMessageToParticipant(recipient, mccip);
    }
    
    public void changeClientInterface_disableTextEntry(Participant recipient){
           MessageChangeClientInterfaceProperties mccip= new MessageChangeClientInterfaceProperties(0,MessageChangeClientInterfaceProperties.disableTextEntry);
           ps.sendMessageToParticipant(recipient, mccip);
    }
    
    /**
     * Relays changes in the WYSIWYG window of client to other clients
     * @param sender
     * @param mWYSIWYG
     */
    public void relayWYSIWYGTextInsertedToAllowedParticipants(Participant sender,diet.message.MessageWYSIWYGDocumentSyncFromClientInsert mWYSIWYG){
           Vector recipientsOfChatText = permissionsController.getPermittedChatTextRecipients(sender);
           Vector pRecipients = (Vector)recipientsOfChatText.elementAt(0);
           Vector pUsernames = (Vector)recipientsOfChatText.elementAt(2);
           Vector pChatWindows = (Vector)recipientsOfChatText.elementAt(3);
           ps.sendWYSIWYGTextInsertedToParticipants(pRecipients,sender.getUsername(),pChatWindows,mWYSIWYG.getTextToAppendToWindow(),mWYSIWYG.getOffset(),mWYSIWYG.getLength());
    }

    /**
     * Relays changes in the WYSIWYG window of client to other clients
     * @param sender
     * @param mWYSIWYG
     */
    public void relayWYSIWYGTextRemovedToAllowedParticipants(Participant sender,diet.message.MessageWYSIWYGDocumentSyncFromClientRemove mWYSIWYG){
           Vector recipientsOfChatText = permissionsController.getPermittedChatTextRecipients(sender);
           Vector pRecipients = (Vector)recipientsOfChatText.elementAt(0);
           Vector pUsernames = (Vector)recipientsOfChatText.elementAt(2);
           Vector pChatWindows = (Vector)recipientsOfChatText.elementAt(3);
           ps.sendWYSIWYGTextRemovedToParticipants(pRecipients,sender.getUsername(),pChatWindows,mWYSIWYG.getOffset(),mWYSIWYG.getLength());
    }

    /**
     * Relays changes to the selection / cursor in the WYSIWYG window of client to other clients
     * @param sender
     * @param mWYSIWYGSel
     */
    public void relayWYSIWYGSelectionChangedToAllowedParticipants(Participant sender,MessageWYSIWYGTextSelectionFromClient mWYSIWYGSel){
          Vector recipientsOfChatText = permissionsController.getPermittedChatTextRecipients(sender);
          Vector pRecipients = (Vector)recipientsOfChatText.elementAt(0);
          Vector pUsernames = (Vector)recipientsOfChatText.elementAt(2);
          Vector pChatWindows = (Vector)recipientsOfChatText.elementAt(3);
          ps.sendWYSIWYGSelectionChangedToParticipants(pRecipients,sender.getUsername(),pChatWindows,mWYSIWYGSel.getCorrectedStartIndex(),mWYSIWYGSel.getCorrectedFinishIndex());
                    //MessageWYSIWYGTextSelectionToClient mwstsend = new MessageWYSIWYGTextSelectionToClient(1, mwysts.getEmail(),
                    //        mwysts.getUsername(), mwysts.getCorrectedStartIndex(), mwysts.getCorrectedFinishIndex());
    }

    /**
     * Sends message to WYSIWYG clients setting their rights on the conversational floor. The recipients are the participants
     * who are permitted to receive text from the sender Participant.
     *
     *
     * @param sender
     * @param newState
     * @param msgPrefix
     */
    public void sendWYSIWYGChangeInterceptionStatusToAllowedParticipants(Participant sender, int newState,String msgPrefix){
        Vector recipientsOfChatText = permissionsController.getPermittedChatTextRecipients(sender);
        Vector pRecipients = (Vector)recipientsOfChatText.elementAt(0);
        Vector pUsernames = (Vector)recipientsOfChatText.elementAt(2);
        ps.sendWYSIWYGChangeInterceptionStatusToParticipants(pRecipients,sender.getUsername(),newState, msgPrefix);
    }

    /**
     *
     * Sends message to WYSIWYG client setting its rights on the conversational floor (whether text entry is permitted)
     * @param recipient
     * @param newState
     * @param msgPrefix
     */
    public void sendWYSIWYGChangeInterceptionStatusToSingleParticipant(Participant recipient, int newState,String msgPrefix){
        MessageWYSIWYGChangeTurntakingStatus mWYSIWYGIS= new MessageWYSIWYGChangeTurntakingStatus("server","server",newState, msgPrefix);
        Vector recipients = new Vector();
        recipients.addElement(recipient);
        ps.sendMessageToParticipant(recipient,mWYSIWYGIS);
    }


     public void setNewDocChangesIncomingAddingOldDocChangesToHistoryDeprecated(Participant p,long timestamp){
        DocChangesIncomingSequenceFIFO ds = getDocChangesIncoming().setNewTurnAndReturnPreviousTurn(p,timestamp);
        Vector recipientsOfChatText = permissionsController.getPermittedChatTextRecipients(p);
        Vector pRecipients = (Vector)recipientsOfChatText.elementAt(0);
        Vector pUsernames = (Vector)recipientsOfChatText.elementAt(2);
        Vector pChatWindows = (Vector)recipientsOfChatText.elementAt(3);
        cH.saveMessage("DEPRECATED",ds.getStartTime(),ds.getEndOfTyping(),p.getUsername(),p.getUsername(),ds.getParsedText(),pUsernames,false,null,ds.getAllInsertsAndRemoves(),0);
    }

     /**
      *
      * Saves the turn that is not being relayed to the log files. It also resets the queue of incoming messages associated with the participant.
      *
      * @param p
      * @param turnNotRelayed
      */
     public void setNewTurnBeingConstructedNotRelayingOldTurnAddingOldTurnToHistory(Participant p,MessageChatTextFromClient turnNotRelayed){
        DocChangesIncomingSequenceFIFO ds = getDocChangesIncoming().setNewTurnAndReturnPreviousTurn(p,new Date().getTime());
        cH.saveMessage("NOTRELAYED",turnNotRelayed.getTypingOnset(),turnNotRelayed.getEndOfTyping(),p.getUsername(),p.getUsername(),turnNotRelayed.getText(),new Vector(),false,null,ds.getAllInsertsAndRemoves(),0);
    }

    public void saveInfoAsFakeTurnFromServerWithAddresseesAsRecipients(String s,Participant pAddressee){
         Vector addressees = new Vector();
         addressees.addElement(pAddressee.getUsername());      
         try{
         cH.saveMessage("INFOASFAKETURNFROMSERVER",new Date().getTime(), new Date().getTime(), "server", "server", s, addressees, false, null, new Vector(), 0);
         }catch(Exception e){
             e.printStackTrace();
         }
    }


    public void saveInfoFromParticipantsTextEntryWindow(Participant sender, MessageKeypressed mkp){
        String message = mkp.getContentsOfTextEntryWindow();
        Vector addressees = new Vector();
        addressees.addElement("KEYPRESS");
        Date messageTimestamp =  mkp.getTimeStamp();
         try{
         cH.saveMessage("TEXTENTRYWINDOW",messageTimestamp.getTime(), new Date().getTime(), "sender", "sender", message, addressees, false, null, new Vector(), 0);
         }catch(Exception e){
             e.printStackTrace();
         }

    }

    public void setNewTurnBeingConstructedNotRelayingOldTurnAddingOldTurnToHistory_WithAddresseeSavedAsRecipient(Participant p,MessageChatTextFromClient turnNotRelayed, Participant pAddressee){
        DocChangesIncomingSequenceFIFO ds = getDocChangesIncoming().setNewTurnAndReturnPreviousTurn(p,new Date().getTime());
        Vector addressees = new Vector();
        if(pAddressee!=null)addressees.addElement(pAddressee.getUsername());
        cH.saveMessage("NEWTURNBEINGCONSTRUCTED",turnNotRelayed.getTypingOnset(),turnNotRelayed.getEndOfTyping(),p.getUsername(),p.getUsername(),turnNotRelayed.getText(),addressees,false,null,ds.getAllInsertsAndRemoves(),0);
    }

     public void setNewMazeGameTurnBeingConstructedNotRelayingOldTurnAddingOldTurnToHistory(Participant p,MessageChatTextFromClient mct,int mazeNo,int moveNo,int indivMveNo){
         DocChangesIncomingSequenceFIFO ds = getDocChangesIncoming().setNewTurnAndReturnPreviousTurn(p,new Date().getTime());
         cH.saveMazeGameMessage(mct.getTypingOnset(),mct.getEndOfTyping(),mct.getUsername(),mct.getUsername(),mct.getText(),new Vector(),mct.hasBeenBlocked(),mct.getKeypresses(),ds.getAllInsertsAndRemoves(),0,mazeNo,moveNo,indivMveNo);
     }


     /**
      *
      *
      *
      * Demarcates a position in the stream of DocChanges from a Participant that determines the end of a turn. It resets the queue
      * of incoming DocChanges and saves the most recent turn to the logfiles. It resets and polls the outgoing queue of DocChangesOutgoingSequence
      * to check if any interventions were performed. If yes, a new turn is saved to the log files.
      *
      * <p>This method needs to be changed if it is to be used generally for WYSIWYG interfaces.
      *
      * @param p
      * @param timestamp
      * @param chOut
      */
     public void wYSIWYGSetNewDocChangesIncomingSavingBothChangesTypedAndModifiedOutgoingQueue(Participant p,long timestamp,DocChangesOutgoingSequenceFIFO chOut){
        DocChangesIncomingSequenceFIFO ds = getDocChangesIncoming().setNewTurnAndReturnPreviousTurn(p,timestamp);
        Vector recipientsOfChatText = permissionsController.getPermittedChatTextRecipients(p);
        Vector pRecipients = (Vector)recipientsOfChatText.elementAt(0);
        Vector pUsernames = (Vector)recipientsOfChatText.elementAt(2);
        Vector pChatWindows = (Vector)recipientsOfChatText.elementAt(3);
        cH.saveMessage("WYSIWYGNewDocChangesIncoming(A)",ds.getStartTime(),ds.getEndOfTyping(),p.getUsername(),p.getUsername(),ds.getParsedText(),pUsernames,false,null,ds.getAllInsertsAndRemoves(),0);
        if(chOut.hasBeenChangedArtificially()){
            cH.saveMessage("WYSIWYGNewDocChangesIncoming(B)",chOut.getStartTime(),chOut.getFinishTime(),"server",p.getUsername(),chOut.getStringOfDocChangeInserts().getString(),
                    pUsernames,false,null,chOut.getAllInsertsAndRemoves(),0);
        }
    }


     public void sendDocChangeToAllowedParticipants(Participant sender,DocChange dc){
           Vector recipientsOfChatText = permissionsController.getPermittedChatTextRecipients(sender);
           Vector pRecipients = (Vector)recipientsOfChatText.elementAt(0);
           Vector pUsernames = (Vector)recipientsOfChatText.elementAt(2);
           Vector pChatWindows = (Vector)recipientsOfChatText.elementAt(3);
           if(dc instanceof DocInsert){
               DocInsert di = (DocInsert)dc;
               ps.sendWYSIWYGTextInsertedToParticipants(pRecipients,sender.getUsername(),pChatWindows,di.getStr(),di.getOffs(),di.getStr().length());
           }
           else if(dc instanceof DocRemove){
               DocRemove dr = (DocRemove)dc;
               ps.sendWYSIWYGTextRemovedToParticipants(pRecipients,sender.getUsername(),pChatWindows,dr.getOffs(),dr.getLen());
           }
    }


    public void relayMazeGameTurnToAllOtherParticipants(Participant sender,MessageChatTextFromClient mct,int mazeNo,int moveNo,int indivMveNo){
        //MessageChatTextToClient msccttc = new MessageChatTextToClient(mct.getEmail(),mct.getUsername(),0,mct.getText(),1);
        DocChangesIncomingSequenceFIFO ds = getDocChangesIncoming().setNewTurnAndReturnPreviousTurn(sender,new Date().getTime());
        Vector recipientsOfChatText = permissionsController.getPermittedChatTextRecipients(sender);
        Vector pRecipients = (Vector)recipientsOfChatText.elementAt(0);
        Vector pUsernames = (Vector)recipientsOfChatText.elementAt(2);
        Vector pChatWindows = (Vector)recipientsOfChatText.elementAt(3);


        for(int i=0;i<pRecipients.size();i++){
            Participant recipient = (Participant)pRecipients.elementAt(i);
            int windowNo = (Integer)pChatWindows.elementAt(i);
            int styleno = cC.getStyleManager().getUniqueIntForRecipient(recipient, sender);
            MessageChatTextToClient mctc = new MessageChatTextToClient(sender.getParticipantID(),sender.getUsername(),0,mct.getText(),windowNo,true,styleno);
            ps.sendMessageToParticipant(recipient, mctc);
        }

        cH.saveMazeGameMessage(mct.getTypingOnset(),mct.getEndOfTyping(),mct.getUsername(),mct.getUsername(),mct.getText(),pUsernames,mct.hasBeenBlocked(),mct.getKeypresses(),ds.getAllInsertsAndRemoves(),0,mazeNo,moveNo,indivMveNo);
    }





     /**
      * Sends task move (e.g. move in the maze game) to a participant.
      * @param p
      * @param mt
      */
     public void sendTaskMoveToParticipant(Participant p, MessageTask mt){
        ps.sendMessageToParticipant(p, mt);

    }

     /**
      * Returns the TaskController (e.g. MazeGameController) associated with the Conversation.
      * @return TaskController
      */
     public TaskController getTaskController(){
        if(tc!=null)return tc;
        return null;
    }


    public void setTaskController(TaskController tc){
       this.tc=tc;
    }


     /**
      * Searches for a participant by their email
      * @param participantlogin
      * @return Participant
      */
     public Participant findParticipantWithEmail(String participantlogin){
        return ps.findParticipantWithEmail(participantlogin);
    }

     /**
      * Returns the location on the local file system where the data from the experiment is saved
      * @return File representing the enclosing folder/directory containing the data from the experiment
      */
     public File getDirectoryNameContainingAllSavedExperimentalData(){
        return convIO.getFileNameContainingConversationData();
    }

    public ConversationHistory getHistory(){
        return cH;
    }
    public DefaultConversationController getController(){
        return cC;
    }
    public String getConversationIdentifier(){
        return this.conversationIdentifier;
    }

    public WindowAndRecipientsManager getPermissionsController(){
        return this.permissionsController;
    }



    public Participants getParticipants(){
        return this.ps;
    }
    public DocChangesIncoming getDocChangesIncoming(){
        return this.turnsconstructed;
    }
    public DocChangesIncomingSequenceFIFO getTurnBeingConstructed(Participant p){
        return this.turnsconstructed.getTurnBeingConstructed(p);
    }
    public WordNetWrapper getWordNetWrapper(){
        return wnw;
    }

    public ConversationIO getConvIO(){
        return convIO;
    }

    public ExperimentSettings getExperimentSettings(){
        return this.expSettings;
    }
    static public boolean isRunningLocally(){
        if(Conversation.statC==null)return true;
        return false;
    }

    /**
     * Attempts to close down the Conversation and associated resources.
     * @param sendCloseToClients
     */
    public void closeDown(boolean sendCloseToClients){
        try{
            this.conversationIsActive=false;
            this.cC.stop();
            if(this.tc!=null){
                tc.closeDown();
            }
            this.cHistoryUIM.closedown();
            this.cH.closeDown();
            this.stop();
            this.ps.closeDown(sendCloseToClients);
            //this.ps.stop();
            this.ps = null;

            //this.conversationIsActive=false;
            this.convIO.shutThreadDownAndCloseFiles();
        } catch (Exception e){

        }
    }


    public void displayNEWWebpage(Participant p, String id, String header,String url, int width, int height,boolean vScrollBar,boolean displayCOURIERFONT){
        ps.displayNEWWebpage(p, id, header, url, width, height, vScrollBar,displayCOURIERFONT);
    }


    public void changeWebpageTextAndColour(Participant p, String id,String text, String colourBackground, String colourText){
        String textToBeSent = "<html><head><style type=\"text/css\">body {color:"+colourText+"; background: "+colourBackground+";}div { font-size: 120%;}</style></head><body><div>"+ text+"</div></body></html>";
        ps.changeWebpage(p, id, textToBeSent);
    }
//public String redbackground = "<html><head><style type=\"text/css\">body {color: white; background: red;}div { font-size: 200%;}</style></head><body><div>INCORRECT </div></body></html>";

    public void changeWebpageImage_OnServer(Participant p, String id,  String imageaddressOnServerWebserver){
        //imageaddressOnServerWebserver needs to include a backslash as first character.
        String url = "<html><img src='http://%%SERVERIPADDRESS%%"+          imageaddressOnServerWebserver+"'></img>";
       
       
        ps.changeWebpage(p, id, url);
    }

    public void changeWebpage(Participant p, String id,String url){
        ps.changeWebpage(p, id, url);
    }



    public void changeWebpage(Participant p, String id,String url, String prepend, String append){
        ps.changeWebpage(p, id, url, prepend, append);
    }

    public void changeJProgressBar(Participant p,String id, String text, Color foreCol, int value){
        ps.changeJProgressBar(p, id, text, foreCol, value);
    }
    public void changeJProgressBarsOfAllParticipants(String id, String text, Color foreCol,int value){
        if(Debug.debugBLOCKAGE){System.out.println("SINH1");;System.out.flush();}
        Vector v = ps.getAllParticipants();
         if(Debug.debugBLOCKAGE){ System.out.println("SINH2");;System.out.flush();}
        for(int i=0;i<v.size();i++){
            
            Participant p = (Participant)v.elementAt(i);
            if(Debug.debugBLOCKAGE){ System.out.println("SINH4 "+p.getUsername());;System.out.flush();}
             ps.changeJProgressBar(p, id, text, foreCol, value);
             if(Debug.debugBLOCKAGE){System.out.println("SINH5 "+p.getUsername());System.out.flush();}
        }
        if(Debug.debugBLOCKAGE){System.out.println("SINH6 ");System.out.flush();}
    }

    public void chageWebpageOfAllParticipants(String id, String text){
        Vector v = ps.getAllParticipants();
        for(int i=0;i<v.size();i++){
            Participant p = (Participant)v.elementAt(i);
            ps.changeWebpage(p, id, "", text, "");
        }
    }

     public void closeWebpageWindow(Participant p, String id){
        ps.closeWebpageWindow(p, id);
    }



    public ExperimentManager getExpManager() {
        return expManager;
    }

    public void printWln(String windowName, String text){
        EMUI em = this.getExpManager().getEMUI();
        if(em!=null){
            em.println(windowName, text);
        }

    }

    public static void printWSln(String windowName, String text){
        if(statC!=null){
           statC.printWln(windowName, text);
        }
    }
    public static void printWSlnLog(String windowName, String text)
    {
    	if(statC!=null)
    		statC.printWlnLog(windowName, text);

    }
    public void printWlnLog(String windowName, String text)
    {
    	EMUI em = this.getExpManager().getEMUI();
        if(em!=null){
            em.println(windowName, text);
        }
        this.convIO.saveWindowTextToLog(windowName, text);


    }

    public static void saveErr(Throwable t){
        if(statC!=null){
           statC.saveErrorLog(t);
        }
    }

    public static void saveErr(String s){
        if(statC!=null){
           statC.saveErrorLog(s);
        }
    }

    public void saveErrorLog(Throwable t){
        System.err.println(this.getConversationIdentifier()+ ": ERROR SOMEWHERE IN THE CONVERSATION CONTROLLER");
        printWln("Main","There is an ERROR in the Conversation Controller");
        printWln("Main","Check the ERRORLOG.TXT file in the directory containing");
        printWln("Main","the saved experimental data");
        printWln("Main",t.getMessage());
        getConvIO().saveErrorLog(t);
    }
    public void saveErrorLog(String s){
        System.err.println(this.getConversationIdentifier()+ ": ERROR SOMEWHERE IN THE CONVERSATION CONTROLLER");
        printWln("Main","There is an ERROR in the Conversation Controller");
        printWln("Main","Check the ERRORLOG.TXT file in the directory containing");
        printWln("Main","the saved experimental data");
        printWln("Main",s);
        getConvIO().saveErrorLog(s);
    }


    public boolean isConversationIsActive() {
        return conversationIsActive;
    }

    public void setConversationIsActive(boolean conversationIsActive) {
        this.conversationIsActive = conversationIsActive;
    }


    public void printAllP(){
        System.err.println("----------LISTING PARTICIPANTS------------------------");
        for(int i=0;i<this.ps.getAllParticipants().size();i++){
            Participant p = (Participant) ps.getAllParticipants().elementAt(i);
            System.err.println("PARTICIPANT:"+p.getParticipantID()+" "+p.getUsername());
        }
    }
}

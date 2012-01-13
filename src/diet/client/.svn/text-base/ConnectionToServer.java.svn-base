package diet.client;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Vector;

import javax.swing.JOptionPane;

import client.ClientMazeGameComms;
import diet.debug.Debug;
import diet.message.Keypress;
import diet.message.Message;
import diet.message.MessageCBYCChangeTurntakingStatus;
import diet.message.MessageCBYCChangeTurntakingStatusConfirm;
import diet.message.MessageCBYCDocChangeFromClient;
import diet.message.MessageCBYCDocChangeToClient;
import diet.message.MessageCBYCTypingUnhinderedRequest;
import diet.message.MessageChangeClientInterfaceProperties;
import diet.message.MessageChatTextFromClient;
import diet.message.MessageChatTextToClient;
import diet.message.MessageClientCloseDown;
import diet.message.MessageClientLogon;
import diet.message.MessageClientSetupParameters;
import diet.message.MessageClientSetupParametersCBYCOneWindowKeypressIntercepted;
import diet.message.MessageClientSetupParametersWYSIWYG;
import diet.message.MessageClientSetupParametersWYSIWYGOneWindow;
import diet.message.MessageClientSetupParametersWYSIWYGOneWindowKeypressIntercepted;
import diet.message.MessageClientSetupParametersWithSendButtonAndTextEntry;
import diet.message.MessageClientSetupParametersWithSendButtonAndTextEntryOneTurnAtATime;
import diet.message.MessageClientSetupParametersWithSendButtonAndTextEntryWidthByHeight;
import diet.message.MessageClientSetupParametersWithinTurn;
import diet.message.MessageClientSetupSuccessful;
import diet.message.MessageDisplayChangeJProgressBar;
import diet.message.MessageDisplayChangeWebpage;
import diet.message.MessageDisplayCloseWindow;
import diet.message.MessageDisplayNEWWebpage;
import diet.message.MessageDummy;
import diet.message.MessageErrorFromClient;
import diet.message.MessageKeypressed;
import diet.message.MessagePopup;
import diet.message.MessagePopupClientLogonEmailRequest;
import diet.message.MessagePopupClientLogonUsernameRequest;
import diet.message.MessageStatusLabelDisplay;
import diet.message.MessageStatusLabelDisplayAndBlocked;
import diet.message.MessageTask;
import diet.message.MessageWYSIWYGChangeCursorIsVisibleAndTextSelection;
import diet.message.MessageWYSIWYGChangeTurntakingStatus;
import diet.message.MessageWYSIWYGDocumentSyncFromClientInsert;
import diet.message.MessageWYSIWYGDocumentSyncFromClientRemove;
import diet.message.MessageWYSIWYGDocumentSyncToClientInsert;
import diet.message.MessageWYSIWYGDocumentSyncToClientRemove;
import diet.message.MessageWYSIWYGTextSelectionFromClient;
import diet.message.MessageWYSIWYGTextSelectionToClient;
import diet.message.MessageWYSIWYGTypingUnhinderedRequest;
import diet.message.MessageWithinTurnDocumentSyncFromClientInsert;
import diet.message.MessageWithinTurnDocumentSyncFromClientRemove;
import diet.message.MessageWithinTurnDocumentSyncFromClientReplace;
import diet.message.MessageWithinTurnDocumentSyncToClientInsert;
import diet.message.MessageWithinTurnDocumentSyncToClientRemove;
import diet.message.MessageWithinTurnTextSelectionFromClient;
import diet.message.MessageWithinTurnTextSelectionToClient;
import diet.server.CbyC.DocInsert;
import diet.server.CbyC.DocRemove;
import diet.server.Conversation;
import diet.task.ClientTaskEventHandler;
import diet.task.mazegame.message.MessageNewMazeGame;
import diet.task.react.MessageNewReactTask;
import diet.task.tangram2D1M.ClientTangramGameComms;
import diet.task.tangram2D1M.message.MessageNewTangrams;
import java.util.Random;
import javax.swing.text.AttributeSet;

/**
 * This deals with low-level network communication with server. It exchanges messages between server and client, 
 * unpacking messages from the server and calling associated methods in {@link ClientEventHandler}. Events from the
 * chat tool interface are assembled into messages and sent to the server.
 *  
 * @author user
 */
public class ConnectionToServer extends Thread {

    public ClientEventHandler cEventHandler;
    public ClientTaskEventHandler cteh;
    public String serverName;
    private Socket cSocket = null;
    private ObjectOutputStream out = null;
    private ObjectInputStream in = null;
    private String email;
    private String forcedParticipantID;
    private boolean forceParticipantID = false;
    private String username;
    boolean retryconnection =true;
    private int portNumber =4444;
    private WebpageAndImageDisplay wiad = new WebpageAndImageDisplay(this);
   
    int turnsBetweenResets = 30;
    int counter =0;

    public ConnectionToServer(String serverName) {
        super();
        this.serverName = serverName;
    }


    public ConnectionToServer(String serverName,int portNumber, String participantID ) {
        super();
        this.forcedParticipantID=participantID;
        this.forceParticipantID=true;
        this.portNumber = portNumber;
        this.serverName = serverName;
    }




    public ConnectionToServer(String serverName,int portNumber) {
        super();
        this.portNumber = portNumber;
        this.serverName = serverName;
    }

    /**
     * Attempts to set up network connection with server. On establishing a connection it prompts the participant 
     * for an email address and also a username.
     *
     * Return false if user decides to cancel logging in
     *
     * @throws java.lang.Exception
     */
    private boolean loginToServerAndInitializeChattool() throws Exception{
        String emailrequest = "";
        String usernamerequest= "";
        boolean loggedin = false;
         System.out.println("ClientSetup4b1");
        while (!loggedin){
           System.out.println("ClientSetup4b2");
            Message m = (Message)in.readObject();
            System.out.println("ClientSetup4b3");
            System.out.println("Not logged in yet2"+usernamerequest+" "+emailrequest);
             if (m instanceof MessagePopupClientLogonEmailRequest && this.forceParticipantID){
                 if(this.forcedParticipantID==null||this.forcedParticipantID.length()==0){
                     JOptionPane.showMessageDialog(null, "For some reason the program can't\n"
                             + "find your ID so we can't log you in\n"
                             + "Please contact the experimenter directly\n"
                             + "If you wish to take part\n");
                     System.exit(-1);
                 }
                 emailrequest=this.forcedParticipantID;
                 out.writeObject(new MessageClientLogon(emailrequest,""));
             }

            else if(m instanceof MessagePopupClientLogonEmailRequest){
                System.out.println("ClientSetup4b4A");
                 MessagePopupClientLogonEmailRequest m2 = (MessagePopupClientLogonEmailRequest)m;
                 if(Debug.doAUTOLOGIN){
                     emailrequest=Debug.groopLOGIN.getNext();
                     //emailrequest = ""+new Random().nextInt(10000000);
                     usernamerequest=usernamerequest+emailrequest;
                 }else{                 
                     emailrequest =  (String) JOptionPane.showInputDialog(m2.getPrompt()+" (At least "+m2.getMinLength()+" characters)",m2.getOptionText());
                 }
                 if(emailrequest==null)return false;
                 while(emailrequest.length()<m2.getMinLength()){
                     emailrequest =  (String) JOptionPane.showInputDialog(m2.getPrompt()+" (At least "+m2.getMinLength()+" characters)",m2.getOptionText());
                     if(emailrequest==null)return false;
                 }
                 System.out.println("Sending EmailRequest");
                 out.writeObject(new MessageClientLogon(emailrequest,""));
            }
            else if (m instanceof MessagePopupClientLogonUsernameRequest){
                System.out.println("ClientSetup4b4B");
               MessagePopupClientLogonUsernameRequest m2 = (MessagePopupClientLogonUsernameRequest)m;
                if(Debug.doAUTOLOGIN){
                    usernamerequest = m2.getOptionText();
                    if(m2.getOptionText()==null||usernamerequest.length()==0){
                        System.exit(-62);
                    }
                    out.writeObject(new MessageClientLogon(emailrequest,usernamerequest));
                }
                
                else if(m2.getAllowAlternatives())
                 {
                     System.out.println("ClientSetup4b4B1");
                     usernamerequest =  (String) JOptionPane.showInputDialog(m2.getPrompt(),m2.getOptionText());
                     if(usernamerequest==null)return false;
                     out.writeObject(new MessageClientLogon(emailrequest,usernamerequest));
                     System.out.println("Sending UsernameRequest:"+usernamerequest);
                     
                }
                else{
                    System.out.println("ClientSetup4b4B2");
                    usernamerequest = m2.getOptionText();
                    Object[] options = {"Log me in again NOW", "Cancel"};
                    int n = JOptionPane.showOptionDialog(null, "It looks as if you were logged on before\n\n"
                            + "USERNAME: "+m2.getOptionText() +"\n"
                            + "\n\n"
                            + "Do you want to log back in again and continue?\n"
                            + "(Any old windows from previous login will stop working\n",
                            "Previous login detected",
                    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options,  options[0]); //default button title
                    if(n==1)return false;
                    else{
                        System.out.println("ClientSetup4b4B21");
                        out.writeObject(new MessageClientLogon(emailrequest,usernamerequest));
                    }


                }
            }
            else if (m instanceof MessageClientSetupParametersWYSIWYG
                    || m instanceof MessageClientSetupParametersWithSendButtonAndTextEntry
                     || m instanceof MessageClientSetupParametersWithSendButtonAndTextEntryWidthByHeight
                    || m instanceof MessageClientSetupParametersWYSIWYGOneWindow
                    || m instanceof MessageClientSetupParametersWYSIWYGOneWindowKeypressIntercepted
                    || m instanceof MessageClientSetupParametersWithSendButtonAndTextEntryOneTurnAtATime
                    || m instanceof MessageClientSetupParametersWithinTurn
                    || m instanceof MessageClientSetupParametersCBYCOneWindowKeypressIntercepted){
               // This IF section should be moved to the initialsetupofchattool() method 
               System.out.println("Logged  in...Received client setup parameters");
                MessageClientSetupParameters mcsp = (MessageClientSetupParameters)m;
                if(mcsp.getNewEmail()!=null){
                    email = mcsp.getNewEmail();
                }
                else{
                    email = emailrequest;
                }
                if(mcsp.getNewUsername()!=null){
                    username = mcsp.getNewUsername();
                }
                else{
                    username = usernamerequest;
                }      
                loggedin=true;
                this.cEventHandler = new ClientEventHandler();
                cEventHandler.setConnectionToServer(this);
                //EMUI.println(getUsername(),"Chat tool initialized...");
                initialsetupOfChatTool(m);

            }
        }
        
        return true;
    }

    /**
     * Determines which chat tool window is to be used and sets the chat window accordingly
     * @param m
     */
    public void initialsetupOfChatTool(Message m){
      System.err.println(m.toString());
     
      if(m  instanceof MessageClientSetupParametersWithSendButtonAndTextEntry){
            MessageClientSetupParametersWithSendButtonAndTextEntry mcsp = (MessageClientSetupParametersWithSendButtonAndTextEntry)m;
            JChatFrameMultipleWindowsWithSendButton jcfsw = new JChatFrameMultipleWindowsWithSendButton(cEventHandler,mcsp.getNoOfWindows(),mcsp.getNoOfRows(),mcsp.getNoOfCols(),mcsp.getAlignmentIsVertical(),mcsp.getParticipantHasStatusWindow(),mcsp.getParticipantsTextWindow(),mcsp.getRowsOfTextEntryArea());
            cEventHandler.setChatFrame(jcfsw);
            cEventHandler.setLabelAndTextEntryEnabled(mcsp.getParticipantsTextWindow(),mcsp.getStatusDisplay(),mcsp.getStatusIsInRed(),mcsp.getParticipantsWindowIsEnabled());
            cEventHandler.setChatFrame(jcfsw);
            MessageClientSetupSuccessful mcs = new MessageClientSetupSuccessful(email,username);
            //Needs to be set up by server
            cEventHandler.setLabelAndTextEntryEnabled(mcsp.getParticipantsTextWindow(),"Normal text",false,true);
        }
        else if(m instanceof MessageClientSetupParametersWithSendButtonAndTextEntryWidthByHeight)
        {
           
            MessageClientSetupParametersWithSendButtonAndTextEntryWidthByHeight mcspwbyh = (MessageClientSetupParametersWithSendButtonAndTextEntryWidthByHeight)m;


            JChatFrameMultipleWindowsWithSendButtonWidthByHeight jcfsw;
            jcfsw=null;
            try{
                 jcfsw = new JChatFrameMultipleWindowsWithSendButtonWidthByHeight(cEventHandler,mcspwbyh.getNoOfWindows(),mcspwbyh.getMainWindowWidth(),mcspwbyh.getMainWindowHeight(),mcspwbyh.getAlignmentIsVertical(),mcspwbyh.getParticipantHasStatusWindow(),mcspwbyh.getParticipantsTextWindow(),mcspwbyh.getTextEntryWidth(),mcspwbyh.getTextEntryHeight(), mcspwbyh.getMaxCharLength(), mcspwbyh.getWtsdsd());
                 //jcfsw = new JChatFrameMultipleWindowsWithSendButtonWidthByHeight(cEventHandler,mcspwbyh.getNoOfWindows(),mcspwbyh.getMainWindowWidth(),500,mcspwbyh.getAlignmentIsVertical(),mcspwbyh.getParticipantHasStatusWindow(),mcspwbyh.getParticipantsTextWindow(),mcspwbyh.getTextEntryWidth(),mcspwbyh.getTextEntryHeight(), mcspwbyh.getWtsdsd());


            }catch (Exception e){
                e.printStackTrace();
                System.exit(05);
            }
            cEventHandler.setChatFrame(jcfsw);
            cEventHandler.setLabelAndTextEntryEnabled(mcspwbyh.getParticipantsTextWindow(),mcspwbyh.getStatusDisplay(),mcspwbyh.getStatusIsInRed(),mcspwbyh.getParticipantsWindowIsEnabled());
            cEventHandler.setChatFrame(jcfsw);
            MessageClientSetupSuccessful mcs = new MessageClientSetupSuccessful(email,username);
            //Needs to be set up by server
            cEventHandler.setLabelAndTextEntryEnabled(mcspwbyh.getParticipantsTextWindow(),"Normal text",false,true);
        }
        else if(m instanceof MessageClientSetupParametersWYSIWYG){
            MessageClientSetupParametersWYSIWYG mcsp = (MessageClientSetupParametersWYSIWYG)m;
            JChatFrameMultipleWindowsWYSIWYG jcfwys = new JChatFrameMultipleWindowsWYSIWYG(cEventHandler,mcsp.getNoOfWindows(),mcsp.getNoOfRows(),mcsp.getNoOfCols(),mcsp.getAlignmentIsVertical(),mcsp.getParticipantHasStatusWindow(),mcsp.getParticipantsTextWindow());
            cEventHandler.setChatFrame(jcfwys);
            cEventHandler.setLabelAndTextEntryEnabled(mcsp.getParticipantsTextWindow(),mcsp.getStatusDisplay(),mcsp.getStatusIsInRed(),mcsp.getParticipantsWindowIsEnabled());
            cEventHandler.setChatFrame(jcfwys);
            //Needs to be set up by server
            cEventHandler.setLabelAndTextEntryEnabled(mcsp.getParticipantsTextWindow(),"Status ok",false,true);
            //The next line needs to read from the setup file
            cEventHandler.wWYSIWYGCsetCursorAndSelectionrAreDisplayed(0,true,true);
            cEventHandler.wWYSIWYGCsetCursorAndSelectionrAreDisplayed(1,true,true);
            cEventHandler.wWYSIWYGCsetCursorAndSelectionrAreDisplayed(2,true,true);
            
        }
        else if(m instanceof MessageClientSetupParametersWYSIWYGOneWindow){
            MessageClientSetupParametersWYSIWYGOneWindow mscp = (MessageClientSetupParametersWYSIWYGOneWindow)m;
            JChatFrameSingleWindowWYSIWYGDeprecated jcfwys = new JChatFrameSingleWindowWYSIWYGDeprecated(cEventHandler,mscp.getNoOfRows(),mscp.getNoOfCols(),mscp.getParticipantHasStatusWindow());
            cEventHandler.setChatFrame(jcfwys);
            cEventHandler.setLabelAndTextEntryEnabled(0,"Status ok", false,true);
            cEventHandler.wWYSIWYGCsetCursorAndSelectionrAreDisplayed(0,true,true);
        }
        else if(m instanceof MessageClientSetupParametersWYSIWYGOneWindowKeypressIntercepted){
            MessageClientSetupParametersWYSIWYGOneWindowKeypressIntercepted mscp = (MessageClientSetupParametersWYSIWYGOneWindowKeypressIntercepted)m;
            JChatFrameSingleWindowWYSIWYGWithEnforcedTurntaking jcfwys = new JChatFrameSingleWindowWYSIWYGWithEnforcedTurntaking(cEventHandler,mscp.getNoOfRows(),mscp.getNoOfCols(),mscp.getParticipantHasStatusWindow());
            cEventHandler.setChatFrame(jcfwys);
            cEventHandler.setLabelAndTextEntryEnabled(0,"Status ok", false,true);
            cEventHandler.wWYSIWYGCsetCursorAndSelectionrAreDisplayed(0,true,true);
        }
        else if(m instanceof MessageClientSetupParametersCBYCOneWindowKeypressIntercepted){
            System.err.println("CBYC11");
            Conversation.printWSln("DEBUG", "1");
            System.err.println("CBYC11");
            MessageClientSetupParametersCBYCOneWindowKeypressIntercepted mscp = (MessageClientSetupParametersCBYCOneWindowKeypressIntercepted)m;
             Conversation.printWSln("DEBUG", "2");
            StyledDocumentStyleSettings styles = mscp.getWtsdsd();
             Conversation.printWSln("DEBUG", "3");
            JChatFrameSingleWindowCBYCWithEnforcedTurntaking jcfc= new JChatFrameSingleWindowCBYCWithEnforcedTurntaking(cEventHandler,mscp.getNoOfRows(),mscp.getNoOfCols(),mscp.getParticipantHasStatusWindow(),styles);
             Conversation.printWSln("DEBUG", "4");
            cEventHandler.setChatFrame(jcfc);
             Conversation.printWSln("DEBUG", "5");
            cEventHandler.setLabelAndTextEntryEnabled(0,"Status ok", false,true);
            
            //cEventHandler.wWYSIWYGCsetCursorAndSelectionrAreDisplayed(0,true,true);
        }
        
        
        else if(m  instanceof MessageClientSetupParametersWithSendButtonAndTextEntryOneTurnAtATime){
            MessageClientSetupParametersWithSendButtonAndTextEntryOneTurnAtATime mcsp = (MessageClientSetupParametersWithSendButtonAndTextEntryOneTurnAtATime)m;
            JChatFrameMultipleWindowsWithSendButtonOneTurnAtATime jcfsw = new JChatFrameMultipleWindowsWithSendButtonOneTurnAtATime(cEventHandler,mcsp.getNoOfWindows(),mcsp.getNoOfRows(),mcsp.getNoOfCols(),mcsp.getAlignmentIsVertical(),mcsp.getParticipantHasStatusWindow(),mcsp.getParticipantsTextWindow(),mcsp.getRowsOfTextEntryArea(),mcsp.getNumberOfTurnsDisplayed(),mcsp.isScrollable());          
            cEventHandler.setChatFrame(jcfsw);
            cEventHandler.setLabelAndTextEntryEnabled(mcsp.getParticipantsTextWindow(),mcsp.getStatusDisplay(),mcsp.getStatusIsInRed(),mcsp.getParticipantsWindowIsEnabled());
            cEventHandler.setChatFrame(jcfsw);
            MessageClientSetupSuccessful mcs = new MessageClientSetupSuccessful(email,username);

            //Needs to be set up by server
            cEventHandler.setLabelAndTextEntryEnabled(mcsp.getParticipantsTextWindow(),"Normal text",false,true);
        }
        else if(m instanceof MessageClientSetupParametersWithinTurn){
            //Stub       
        }
    }

    
 
    long cntINCOMING =0;
    Random r = new Random();


    public void eat3000Messages(){
        for(int j=0;j<250;j++){
             try {
                 System.err.println("READING OBJECTS");System.err.flush();
                 Object o =  in.readObject();
                 

             }catch (Exception e){
                 e.printStackTrace();
             }
        }
        
    }

    /**
     * This is the main method of the client which constantly checks for incoming messages from the server.
     * On receiving a message, it extracts the information and calls the appropriate methods in {@link ClientEventHandler}
     * When changing this method, ensure that it retains the capacity to capture exceptions and send them to the server
     * @throws java.lang.Exception
     */
    public void checkForIncomingMessagesLoop() throws Exception{
        boolean gameInProgress = true;
        while (gameInProgress) {
          try{
          //System.out.println("checkingForincomingMessages");

          
              
          Message    m = (Message) in.readObject();
              
         
          
          cntINCOMING++;
          //System.err.println("________________________________________________"+cntINCOMING);
          if (m instanceof MessageChatTextToClient) {
            if(Debug.bypassCLIENTGUIPRINTOUT)return;
            // System.out.println("Appending");
             MessageChatTextToClient mct = (MessageChatTextToClient) m;
            
             if(mct.prefixUsername()){
                cEventHandler.appendWithCaretCheck(mct.getUsername(),mct.getText(),mct.getWindowNo(),mct.getStylenumber());
             }
             else{
                 cEventHandler.appendWithCaretCheck("",mct.getText(),mct.getWindowNo(),mct.getStylenumber());
             }
             //System.out.println("Appending: "+mct.getWindowNo()+mct.getText());
          }
          else if (m instanceof MessageStatusLabelDisplayAndBlocked){
              //System.out.println("Receiving MessageStatusLabelDisplayAndBlocked");
              MessageStatusLabelDisplayAndBlocked msld = (MessageStatusLabelDisplayAndBlocked)m;
              cEventHandler.setLabelAndTextEntryEnabled(msld.getWindowNumber(),msld.getTextToDisplay(),msld.isLabelRed(),msld.isScreenToBeEnabled());
          }
           else if (m instanceof MessageStatusLabelDisplay){
              //System.out.println("Receiving MessageStatusLabelDisplayAndBlocked");
              MessageStatusLabelDisplay msld = (MessageStatusLabelDisplay)m;              
              cEventHandler.setLabel(msld.getWindowNumber(),msld.getTextToDisplay(),msld.isLabelRed());
              
          }
           else if (m instanceof MessageChangeClientInterfaceProperties){
              //System.out.println("Receiving MessageStatusLabelDisplayAndBlocked");
              MessageChangeClientInterfaceProperties mccip = (MessageChangeClientInterfaceProperties)m;
              cEventHandler.changeInterfaceProperties(mccip.getInterfaceproperties());
              //cEventHandler.setLabel(msld.getWindowNumber(),msld.getTextToDisplay(),msld.isLabelRed());
              
          }
           
           
                   
                   
          else if (m instanceof MessageWYSIWYGDocumentSyncToClientInsert){
              //System.err.println("checkingForincoming Message WYSIWYG Insert----------------------------------------------------------------");
              MessageWYSIWYGDocumentSyncToClientInsert mwystc = (MessageWYSIWYGDocumentSyncToClientInsert)m;
              cEventHandler.wYSIWYGUpdateDocumentInsert(mwystc.getWindowNumber(),mwystc.getTextToAppendToWindow(),mwystc.getOffset(),mwystc.getLength()) ;
          }
          else if (m instanceof MessageWYSIWYGDocumentSyncToClientRemove){
              //System.err.println("checkingForincoming Messages WYSIWYG Remove----------------------------------------------------------------");
              MessageWYSIWYGDocumentSyncToClientRemove mwystc = (MessageWYSIWYGDocumentSyncToClientRemove)m;
              cEventHandler.wYSIWYGUpdateDocumentRemove(mwystc.getWindowNumber(),mwystc.getOffset(),mwystc.getLength()); 
          }
          
          else if (m instanceof MessageWYSIWYGTextSelectionToClient){
            //  System.out.println("Received TextSelectionUpdate");
              MessageWYSIWYGTextSelectionToClient mwysc = (MessageWYSIWYGTextSelectionToClient)m;
              cEventHandler.wWYSIWYGChangeCursorAndSelectionInWindow(mwysc.getWindowNumber(),mwysc.getStartIndex(),mwysc.getFinishIndex());
          }
          else if (m instanceof MessageWYSIWYGChangeCursorIsVisibleAndTextSelection){
             // System.out.println("Received Cursor and Selection settings");
              MessageWYSIWYGChangeCursorIsVisibleAndTextSelection mwciv = (MessageWYSIWYGChangeCursorIsVisibleAndTextSelection)m;
              cEventHandler.wWYSIWYGCsetCursorAndSelectionrAreDisplayed(mwciv.getWindowNumber(), mwciv.getCursorIsVisible(),mwciv.getTextSelectionIsEnabled());
              //wWYSIWYGChangeCursorAndSelectionInWindow(mwyciv.getWindowNumber(),mwysc.getStartIndex(),mwysc.getFinishIndex());
          }
          else if (m instanceof MessageWYSIWYGChangeTurntakingStatus){
             // System.out.println("Received Interception Status setting");
              MessageWYSIWYGChangeTurntakingStatus mWIS = (MessageWYSIWYGChangeTurntakingStatus)m;
              cEventHandler.wWYSIWYGChangeInterceptionStatus(mWIS.getNewStatus(),mWIS.getMsg());
          }
          else if (m instanceof MessageCBYCChangeTurntakingStatus){
             // System.out.println("Received Interception Status setting");
              MessageCBYCChangeTurntakingStatus mCIS = (MessageCBYCChangeTurntakingStatus)m;
              cEventHandler.cBYCChangeInterceptionStatus(mCIS.getNewStatus(),mCIS.getPrefix(),mCIS.getID());
          }
          else if (m instanceof MessageCBYCDocChangeToClient){
             // System.out.println("Received Interception Status setting");
              MessageCBYCDocChangeToClient mCD = (MessageCBYCDocChangeToClient)m;
              cEventHandler.cBYCDocChangeToClient(mCD.getDocChange());
          }       
          else if(m instanceof MessageTask){
              System.err.println(this.email+" receiving MessageTask "+m.getClass().toString());
              
              if(m instanceof MessageNewMazeGame&&cteh==null){
                 cteh = new ClientMazeGameComms(this,(MessageNewMazeGame)m);
              }
              else if(m instanceof MessageNewTangrams && cteh ==null){
                  System.out.println("client tangram game comms intitialise");
                  cteh = new ClientTangramGameComms(this,(MessageNewTangrams)m);
              }
              else if (m instanceof MessageNewReactTask && cteh ==null){
                  if(Debug.debugREACT)this.sendErrorMessage("NOTANERROR1");
                  cteh = new diet.task.react.ClientTaskReactEventHandler(this,(MessageNewReactTask)m);
                  if(Debug.debugREACT)this.sendErrorMessage("NOTANERROR2");
              }

              else{
                  MessageTask mt = (MessageTask)m;
                  cteh.processTaskMove(mt);
              }
              
          }
          else if (m instanceof MessageDisplayNEWWebpage){
              //System.exit(-05);
              MessageDisplayNEWWebpage mdw = (MessageDisplayNEWWebpage)m;
              try{
                  String tx = mdw.getUrl();
                  String tx2 = tx.replaceAll("%%SERVERIPADDRESS%%", this.serverName);
                  System.err.println("TRYING TO DISPLAY-"+tx+"-----------"+tx2);
                  if(Debug.debugPICTURELOADING){
                     // this.sendChatText("DEBUGCOMMAND:"+tx2, 0, false, new Vector());

                  }
                  

                  this.wiad.displayNEWWebpage(mdw.getId(), mdw.getHeader(), tx2, mdw.getWidth(), mdw.getHeight(), mdw.isVScrollBar(),mdw.isJProgressBar(), mdw.isForceCourier());
                  //this.wiad.changeJProgressBar(mdw.getId(), "THE TXT IS:" +tx2+"----ANDALSO-"+tx+"----", Color.red, 59);
              }catch(Exception e){
                  e.printStackTrace();
                  this.sendChatText("ERRORDISPLAYINGNEWWEBPAGE", 0, false, new Vector());
                  //System.exit(-123456);    
              }
          }
          else if (m instanceof MessageDisplayChangeWebpage){
              MessageDisplayChangeWebpage mdcw = (MessageDisplayChangeWebpage)m;
              try{
                  String tx = mdcw.getUrl();
                  String tx2 = tx.replaceAll("%%SERVERIPADDRESS%%", this.serverName);
                  System.err.println("TRYING TO DISPLAY-"+tx+"-----------"+tx2);
                  //this.wiad.changeJProgressBar(mdcw.getId(), "THE TXT IS:" +tx2+"----ANDALSO-"+tx+"----", Color.black, 59);
                  this.wiad.changeWebpage(mdcw.getId(), tx2, mdcw.getPrepend(),mdcw.getAppend());
              }catch(Exception e){
                  e.printStackTrace();
              }              
          }
          else if (m instanceof MessageDisplayChangeJProgressBar){
              MessageDisplayChangeJProgressBar mdcpb = (MessageDisplayChangeJProgressBar)m;
              try{
                  this.wiad.changeJProgressBar(mdcpb.getId(), mdcpb.gettext(), mdcpb.getColor(), mdcpb.getValue());
              }catch(Exception e){
                  e.printStackTrace();
              }
          }




          else if (m instanceof MessageDisplayCloseWindow){
              MessageDisplayCloseWindow mdcw = (MessageDisplayCloseWindow)m;
              wiad.destroyWebpage(mdcw.getID());
          }
          else if (m instanceof MessageWithinTurnDocumentSyncToClientInsert){
              //System.err.println("checkingForincoming Message WYSIWYG Insert----------------------------------------------------------------");
              MessageWithinTurnDocumentSyncToClientInsert mwystc = (MessageWithinTurnDocumentSyncToClientInsert)m;
              cEventHandler.withinTurnForceDocumentInsert(mwystc.getWindowNumber(),mwystc.getTextToAppendToWindow(),mwystc.getOffset(),mwystc.getLength(),mwystc.getAttr());
              
          }
          else if (m instanceof MessageWithinTurnDocumentSyncToClientRemove){
              //System.err.println("checkingForincoming Messages WYSIWYG Remove----------------------------------------------------------------");
              MessageWithinTurnDocumentSyncToClientRemove mwystc = (MessageWithinTurnDocumentSyncToClientRemove)m;
              cEventHandler.withinTurnForceDocumentRemove(mwystc.getWindowNumber(),mwystc.getOffset(),mwystc.getLength());
              
          }
          
          else if (m instanceof MessageWithinTurnTextSelectionToClient){
            //  System.out.println("Received TextSelectionUpdate");
              MessageWithinTurnTextSelectionToClient mwysc = (MessageWithinTurnTextSelectionToClient)m;
              cEventHandler.withinTurnChangeCursorAndSelectionInWindow(mwysc.getWindowNumber(),mwysc.getStartIndex(),mwysc.getFinishIndex());
          }
          else if (m instanceof MessagePopup){
              System.out.println("ClientSetup4b4B");
              MessagePopup mp = (MessagePopup)m;
              String[] options = mp.getOptions();
              String question = mp.getQuestion();
              String title = mp.getTitle();
              int n = JOptionPane.showOptionDialog(null, question, title, JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]); 
              
              String emailcopy = email; if(emailcopy==null)emailcopy="NOTSETONCLIENT";
              String usernamecopy = username; if(usernamecopy==null)usernamecopy="NOTSETONCLIENT";
              
              MessagePopup mpRETURN = new MessagePopup (emailcopy, usernamecopy, question, options, title, n);
              this.sendMessage(mpRETURN);
              
             // emailrequest =  (String) JOptionPane.showInputDialog(m2.getPrompt()+" (At least "+m2.getMinLength()+" characters)",m2.getOptionText());
             // out.writeObject(new MessageClientLogon(emailrequest,""));
                    
                    
                  
          }  
          else if(m instanceof MessageClientCloseDown){
              try{
                  System.err.println("CLIENT IS TRYING TO CLOSE DOWN");
                  gameInProgress=false;
                  System.err.println("CLIENT IS TRYING TO CLOSE DOWN1");
                  if(cteh!=null)cteh.closeDown();
                  System.err.println("CLIENT IS TRYING TO CLOSE DOWN2");
                  this.retryconnection=false;
                  System.err.println("CLIENT IS TRYING TO CLOSE DOWN3");
                  this.cEventHandler.closeDown();
                  System.err.println("CLIENT IS TRYING TO CLOSE DOWN4");
                  this.stop();
                  System.err.println("CLIENT IS TRYING TO CLOSE DOWN5");
                  this.cEventHandler=null;
                  System.err.println("CLIENT IS TRYING TO CLOSE DOWN6");
                  if(this.wiad!=null){
                      wiad.destroyAllWebpages();
                      wiad.displays = null;
                      wiad = null;
                  }
                  System.err.println("CLIENT IS TRYING TO CLOSE DOWN7");
                  out.close();
                  System.err.println("CLIENT IS TRYING TO CLOSE DOWN9");
                  in.close();
                  System.err.println("CLIENT IS TRYING TO CLOSE DOWN10");
                  this.cSocket.close();
                  System.err.println("CLIENT IS TRYING TO CLOSE DOWN11");
                   
                  
              }catch(Exception e){
                   this.stop();
                   System.gc();
              }
              if(!Conversation.isRunningLocally()){
                  System.err.println("Shut down by server...");
                  System.exit(-1);
              }
              
          }
          }catch (Exception e){
              e.printStackTrace();
              String possiblyNullusername = "not yet set";
              String possiblyNullemail = "Not yet set";
              if(this.email!=null)possiblyNullemail = this.email;
              if(this.username!=null)possiblyNullusername = this.username;
              diet.message.MessageErrorFromClient mefc= new MessageErrorFromClient(e.getMessage(),possiblyNullemail,possiblyNullusername);
              System.err.println("Some error in the client chat tool");
              boolean clientISCONNECTED = this.sendDummyMessageToTestConnection_getConnectionISOK();
              if(!clientISCONNECTED){
                  gameInProgress=false;
                  return;

              }
              this.sendMessage(mefc);
             
          }
       }
                 

    }


    
    /**
     * Initializes the client before calling checkForIncomingMessagesLoop()
     */
    public void run(){

      System.out.println("ClientSetup0");
      retryconnection = true;

       while (retryconnection){
           
           try{
                System.out.println("ClientSetup1");
                cSocket = new Socket(serverName, this.portNumber);
                System.out.println("ClientSetup2");
                in = new ObjectInputStream(cSocket.getInputStream());
                System.out.println("ClientSetup3");
                out = new ObjectOutputStream(cSocket.getOutputStream());
                System.out.println("ClientSetup4--");
                //EMUI.println("HELLO","ObjectInput and ObjectOutput streams created");
                retryconnection = false;
                System.out.println("ClientSetup5");
            }
             catch (Exception e){
                  e.printStackTrace();
                  System.err.println("Trying to connect");
                  String[] options = {"Reconnect","Cancel"};
                  int n2 =  JOptionPaneTimeOut.showTimeoutDialog(null, "\nThere was some connectivity problem"
                          + "trying to connect to "+serverName
                          + "...it could be\n\n"
               + "- Your internet connection isn't working\n"
               + "- Your firewall doesn't allow outgoing connections\n"
               + "- An error on the server\n"
               + "- The server isn't switched on yet\n\n"

               + "Trying to reconnect every 5 seconds\n\n"
                       + "If this goes on for too long, please choose Cancel"
                       + " and then try and log in again", "Disconnected", JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE,options, options[0]);
               if(n2==1) {
                   //System.exit(-5);
                   return;
               }
               System.err.println(n2);
            }
          }

          try{
                System.out.println("ClientSetup4b");
                boolean loginSuccessful = loginToServerAndInitializeChattool();
                if(!loginSuccessful)
                
                System.out.println("ClientSetup5");
                checkForIncomingMessagesLoop();
                System.out.println("ClientSetup6");
                
               }catch (Exception e){
               try{
                   System.out.println("ClientSetup7");
                    retryconnection=false;
                    out.close();
                    in.close();
                    cSocket.close();
                    System.out.println("ClientSetup8");
              }catch(Exception e2){
                 e2.printStackTrace();
                 System.out.println("ClientSetup9");
              }

              





            
        }

        System.err.println("EXITED FROM CLIENT");

    }



    /**
     * Sends an already constructed message to the server
     * @param m
     */
    public synchronized void sendMessage(Message m){
        try{
            if(Debug.showErrorsForMacIntegration)System.out.println("--SENDMESSAGE1");
            out.writeObject(m);
            if(Debug.showErrorsForMacIntegration)System.out.println("--SENDMESSAGE2");
            out.flush();
            if(Debug.showErrorsForMacIntegration)System.out.println("--SENDMESSAGE3");
            counter++;
            if(Debug.showErrorsForMacIntegration)System.out.println("--SENDMESSAGE4");
          if(counter>turnsBetweenResets){
              out.reset();
              if(Debug.showErrorsForMacIntegration)System.out.println("--SENDMESSAGE5");

              counter =0;
          }          
        }catch(Exception e){
          e.printStackTrace();
          System.err.println("Error sending "+e.getStackTrace()+" ");
           if(Debug.showErrorsForMacIntegration)System.exit(-1);
        }
    }


    public boolean isCONNECTED(){

        if(cSocket.isInputShutdown())return false;
        if(cSocket.isOutputShutdown())return false;
        if(!cSocket.isConnected()) return false;
        if(cSocket.isClosed()) return false;
        return true;
    }


    /**
     * Sends chat text and associated timing information to the server
     * @param text
     * @param startOfTyping
     * @param currentTurnBeingConstructedHasBeenBlocked
     * @param keyPresses All the keypresses associated with the construction of this turn
     */
    public synchronized void sendChatText(String text, long startOfTyping, boolean currentTurnBeingConstructedHasBeenBlocked,Vector keyPresses) {
      //if(Debug.debugBLOCKAGE&&text.equalsIgnoreCase("-"))this.eat3000Messages();
      // EMUI.println(getUsername(),"sendchattext: " + text);
     //if(Debug.debugPICTURELOADING)text = "DEBUGGING"+text;
      MessageChatTextFromClient msct = new MessageChatTextFromClient(email,username, text,startOfTyping,currentTurnBeingConstructedHasBeenBlocked,keyPresses);
      sendMessage(msct);
   }

//
    /**
     * Sends chat text and offset information to the server
     * 
     * @param textAtEndOfString
     * @param offset
     */
    public void sendChatTextWYSIWYG(String textAtEndOfString,int offset){
       try{
      // EMUI.println(getUsername(),"SendingWYSIWYG1---------------------------------------------------------------");
       MessageWYSIWYGDocumentSyncFromClientInsert mwysfc = new MessageWYSIWYGDocumentSyncFromClientInsert(email,username,textAtEndOfString,offset,0, "SENDCHATTEXTWYSIWYGDEPRECATED");
       //System.out.println("SendingWYSIWYG2");
       sendMessage(mwysfc);
       }catch (Exception e){
        //   EMUI.println(getUsername(),"ERROR SENDING WYSIWYG");
       }
   }

    public boolean sendDummyMessageToTestConnection_getConnectionISOK(){
        MessageDummy md = new MessageDummy(this.email,this.username);
        try{
             out.writeObject(md);
             out.flush();
             counter++;
          if(counter>turnsBetweenResets){
              out.reset();
              counter =0;
          }
        }catch(Exception e){
          e.printStackTrace();
          System.err.println("DUMMY MESSAGE NOT SENT "+e.getStackTrace()+" ");
          return false;
        }
        return true;
    }
    
    
    /**
     * Sends offset and length of text removed from WYSIWYG chat window interface
     * @param offset
     * @param length
     */
    public void sendWYSIWYGDocumentHasChangedRemove(int offset,int length){
          try{
          MessageWYSIWYGDocumentSyncFromClientRemove mwysfc = new MessageWYSIWYGDocumentSyncFromClientRemove(email,username,offset,length);
          sendMessage(mwysfc);
           //System.out.println("WYSIWYGIN CLEVENTHANDLER2");
          }catch (Exception e){
             // EMUI.println(getUsername(),"ERROR SENDING WYSIWYG REMOVE");
          }
    }
    
    /**
     * Sends offset and length of chat text inserted in WYSIWG chat window interface
     * @param text
     * @param offset
     * @param length
     */
    public void sendWYSIWYGDocumentHasChangedInsert(String textTyped,int offset,int length, String textInWindow){
          try{
          MessageWYSIWYGDocumentSyncFromClientInsert mwysfc = new MessageWYSIWYGDocumentSyncFromClientInsert(email,username,textTyped,offset,length, textInWindow);
          sendMessage(mwysfc); 
          }catch (Exception e){
             // EMUI.println(getUsername(),"ERROR SENDING WYSIWYG REMOVE");
          }
    }
    
    
    
    /**
     * Sends information to server of any changes made to the cursor position / text highlighting in WYSIWG chat window
     * @param startPos
     * @param finishPos
     */
    public void sendCursorAndSelectionUpdateWYSIWYG(int startPos,int finishPos){
        try{
        //System.out.println("Sending CursorAndSelectionUpdate");
        MessageWYSIWYGTextSelectionFromClient casWYSIWYG= new MessageWYSIWYGTextSelectionFromClient(email,username,startPos,finishPos);
        sendMessage(casWYSIWYG);
        //System.out.println("Sent CursorAndSelectionUpdate");
        }catch (Exception e){
           // EMUI.println(getUsername(),"ERROR SENDING CURSORANDSELECTIONUPDATE");
        }
    }
 
    /**
     * Sends message to server that participant has pressed a key
     * @param kp
     * @param contents
     */
    public void sendClientIsTyping(Keypress kp, String contents){
         //System.out.println("SENDING KEYPRESSED");
         MessageKeypressed mkp = new MessageKeypressed(email,username,kp, contents);
         sendMessage(mkp);
    }

    /**
     * Sends request to server to permit participant to gain conversational floor.
     * @param offsetFromRight
     * @param text
     */
    public void sendWYSIWYGTypingUnhinderedRequest(int offsetFromRight,String text){
          MessageWYSIWYGTypingUnhinderedRequest mWReq = new MessageWYSIWYGTypingUnhinderedRequest(email,username,offsetFromRight,text);
          sendMessage(mWReq);
          
    }
    
    public void sendCBYCTypingUnhinderedRequest(int offsetFromRight,String  textTyped,String eString,int eStart,int eFinish){
          MessageCBYCTypingUnhinderedRequest mCReq = new MessageCBYCTypingUnhinderedRequest(email,username,offsetFromRight,textTyped,eString,eStart,eFinish);
          sendMessage(mCReq);
          System.out.println("SENDING TYPING UNHINDERED REQUEST.............."+textTyped);
    }
    
    public void cBYCStateHasChanged(int oldState,int newState,long id){
          MessageCBYCChangeTurntakingStatusConfirm mcybcconf = new MessageCBYCChangeTurntakingStatusConfirm(email,username,oldState,newState,id);
          sendMessage(mcybcconf);
          System.out.println("Confirming change of turn-taking status");
   
    }
    
    public void sendCBYCDocumentHasChangedRemove(int offset, int length,String elementString,int elementStart,int elementFinish,int docSize) {
         DocRemove dr = new DocRemove(username,username,"server",offset,length,elementString,elementStart,elementFinish,docSize);
         MessageCBYCDocChangeFromClient mcdc = new MessageCBYCDocChangeFromClient(email,username,0,dr);
         sendMessage(mcdc);
         System.out.println("SENDING DOCUMENTHASCHANGEDREMOVE.............."+"offset:"+offset+"..length:"+length);
    }

    public void sendCBYCDocumentHasChangedInsert(String text, int offset, Object attr,String elementString,int elementStart,int elementFinish,int docSize) {
        DocInsert di = new DocInsert(username,username,"server",offset,text,attr,elementString,elementStart,elementFinish,docSize);
      
        MessageCBYCDocChangeFromClient mcdc = new MessageCBYCDocChangeFromClient(email,username,0,di);
        sendMessage(mcdc);
        System.out.println("SENDING INSERT.............."+text+"DOCOFFSET: "+di.offs);
    }
    
    
    
    
    
    public void withinTurnRequestInsert(int offset, String str,AttributeSet a){
        try{
          MessageWithinTurnDocumentSyncFromClientInsert mwtdsfc = new MessageWithinTurnDocumentSyncFromClientInsert(email,username,str,offset,str.length());
          sendMessage(mwtdsfc); 
          }catch (Exception e){
             // EMUI.println(getUsername(),"ERROR SENDING WYSIWYG REMOVE");
          }
    }
    public void withinTurnRequestRemove(int offset, int len){
        try{
          MessageWithinTurnDocumentSyncFromClientRemove mwtdsfc = new MessageWithinTurnDocumentSyncFromClientRemove(email,username,offset,len);
          sendMessage(mwtdsfc); 
          }catch (Exception e){
             // EMUI.println(getUsername(),"ERROR SENDING WYSIWYG REMOVE");
          }
    }
    public void withinTurnRequestReplace(int offset, String str,int len,AttributeSet a){
         try{
          MessageWithinTurnDocumentSyncFromClientReplace mwtdsfc = new MessageWithinTurnDocumentSyncFromClientReplace(email,username,str,offset,len,a);
          sendMessage(mwtdsfc); 
          }catch (Exception e){
             // EMUI.println(getUsername(),"ERROR SENDING WYSIWYG REMOVE");
          }
    }
    
    public void withinTurnSendCursorAndSelectionUpdate(int startPos, int finishPos){
        try{
         MessageWithinTurnTextSelectionFromClient mwttsfc = new MessageWithinTurnTextSelectionFromClient(email,username,startPos,finishPos);
         sendMessage(mwttsfc);
        }catch (Exception e){
            
        }
    }
    
    
    public void sendErrorMessage(Exception e){
        e.printStackTrace();
        StackTraceElement[] a = e.getStackTrace();
        String error = "";
        for(int i=0;i<a.length;i++){
            StackTraceElement ste = a[i];
            if(ste!=null){
                error = error+ste.toString();
            }
        }   
        String possiblyNullemail = this.email+"";
        String possiblyNullusername = this.username+"";       
        diet.message.MessageErrorFromClient mefc= new MessageErrorFromClient("This is the error message FROM CLIENT: "+e.getMessage()+"\n "+error,e,possiblyNullemail,possiblyNullusername);
        sendMessage(mefc);
    }
    
    public void sendErrorMessage(String s){
        String possiblyNullemail = this.email+"";
        String possiblyNullusername = this.username+"";   
        diet.message.MessageErrorFromClient mefc= new MessageErrorFromClient("Error message FROM CLIENT: "+s,null,possiblyNullemail,possiblyNullusername);
        sendMessage(mefc);
    }
    
    
    /**
     * Returns username associated with client
     * @return user name
     */
    public String getUsername() {
        return username;
    }
    /**
     * Returns String email of client
     * @return email
     */
    public String getEmail(){
        return email;
    }
    
}

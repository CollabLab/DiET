/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package diet.server.ConversationController;

import diet.message.MessageChatTextFromClient;
import diet.message.MessageKeypressed;
import diet.parameters.ExperimentSettings;
import diet.server.Conversation;
import diet.server.Participant;
import diet.task.collabMinitask.CollabMoonTask;

/**
 *
 * @author sre
 */
public class CCSEQUENCE extends DefaultConversationController{

    @Override
    public boolean requestParticipantJoinConversation(String participantID) {
        System.err.println("CHECKING PARTICIPANTID AGAIN");
        System.err.println("CHECKING PARTICIPANTID AGAIN");
        System.err.println("CHECKING PARTICIPANTID AGAIN");
        System.err.println("CHECKING PARTICIPANTID AGAIN");
        if(participantID.startsWith("-"))return false;
        return true;
    }

    




    public CollabMoonTask ct;
    static public String ipADDRESS = "10.32.11.11";
    int webserverport = 81;
    String ipADRESSWEB = "http://"+ipADDRESS+":"+webserverport+"/";


    @Override
    public void initialize(Conversation c, ExperimentSettings expSettings) {
        super.initialize(c, expSettings);
        ct = new CollabMoonTask(c,false);
    }


    @Override
    public void processKeyPress(Participant sender, MessageKeypressed mkp) {
         if(mkp.getKeypressed().getKeycode()==47){
             //Is typing a command..
         }
         else{
             //super.processKeyPress(sender, mkp);
         }
    }



    @Override
    public void participantJoinedConversation(Participant p) {    
       ct.participantJoinedConversation(p);
    }

    @Override
    public void participantRejoinedConversation(Participant p) {
        ct.participantRejoinedConversation(p);
    }

    
    @Override
    public void processLoop() {
        super.processLoop();
        c.resetFromIsTypingtoNormalChatAllAllowedParticipants(super.getIsTypingTimeOut());
    }


    @Override
    public void processChatText(Participant sender, MessageChatTextFromClient mct) {
          if(mct.getText().startsWith("/")){
              ct.processCommand(sender, mct);
          }
          else{
              super.processChatText(sender,mct);
          }
    }
}
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package diet.task;

import diet.message.MessageTask;
import diet.server.Participant;

/**
 *
 * @author user
 */
public class TaskController {

    
    public void initialize(){
        //This is called once max. number of participants has been reached.
    }
    
    public void participantJoinedConversation(Participant pJoined){
        
    }
    
    public void processTaskMove(MessageTask mtm, Participant origin){
     
    }
    
    /**
     * This method is called from Conversation.closeDown() Any threads instantiated in a custom
     * TaskController object should be stoppable using this method.
     */
    public void closeDown(){
        
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package diet.server.CbyC.Sequence.FlowControl;

import diet.server.Conversation;
import diet.server.Participant;
import diet.server.CbyC.Sequence.Sequence;


/**
 *
 * @author Greg
 */
public class FlowControlDoSpoofResponsivityToTypingActivityOfParticipant extends FlowControl implements Runnable{

    
    Participant recipientP;
    boolean carryOutSpoofing=true;
    long typingThreshold;
    Thread running;
    
    public FlowControlDoSpoofResponsivityToTypingActivityOfParticipant(Sequence seq, String recipient,long typingThreshold) {
        super(seq);
        this.typingThreshold=typingThreshold;
        recipientP =  seq.getSS().getcC().getC().getParticipants().findParticipantWithUsername(recipient);
        Thread r = new Thread(this);
        r.start();
        running=r;
    }
   
   
    
    public synchronized void sendFakeIsTypingMessage(){
        seq.getSS().getcC().getC().sendAndEnableLabelDisplayToALLStatusWindowsOfParticipant(recipientP, "You are typing", true, true);
        //Conversation.printWSln("spoofresponsivity", "SHOULD SAY"+recipientP.getUsername()+" "+" is typing");
     }
    
    public synchronized void sendFakePleaseTypeMessage(){
        seq.getSS().getcC().getC().sendAndEnableLabelDisplayToALLStatusWindowsOfParticipant(recipientP, "Please type", false, true);
    }
    
    public void run(){
        while(carryOutSpoofing){
            try{
              
              Thread.sleep(typingThreshold/3);
              Conversation.printWSln("spoofresponsivity", "EVERY300");
              
              if(!recipientP.isTyping(typingThreshold)&&carryOutSpoofing){
                  sendFakePleaseTypeMessage();
              }
             
            }catch(Exception e){
                
            }  
        }
        Conversation.printWSln("Main", "FLOWCONTROLISREALLY STOPPING SPOOFING");
    }

    public void stopSpoofIsTypingMessages(){
        this.carryOutSpoofing=false;
    }

    public void controlFlow(){
        carryOutSpoofing=false;
        Conversation.printWSln("Main", "TRYING TO JOIN...");
        //Conversation.printWSln("Main", "STOPPING SPOOFING");
        try{
          running.join();
        }catch(Exception e){
            Conversation.printWSln("Main", "Non-critical error attempting join of threads");
        }  
    }
}

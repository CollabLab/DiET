/*
 * QueeBlockingForRecipientSortsIncomingMessages.java
 *
 * Created on 21 January 2008, 12:57
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package diet.server;
/*
 * QueueBlockingForRecipientSortsIncomingMessages.java
 *
 * Created on 21 January 2008, 12:19
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */


import diet.debug.Debug;
import java.util.Date;
import java.util.Vector;

import diet.message.Message;
import diet.message.MessageChatTextFromClient;
import java.util.Random;

/**
 * This is an implementation of a blocking queue for incoming messages. When it adds a message it attempts
 * to insert the message in the correct position in the queue.
 * @author user
 */
public class QueueBlockingForRecipientSortsIncomingMessages {
    
    /**
     * Creates a new instance of QueueBlockingForRecipientSortsIncomingMessages
     */
    private Vector incomingMessages = new Vector();
    
    public QueueBlockingForRecipientSortsIncomingMessages() {
    }
    
  
        
        
    /**
     * Adds message to the incoming queue, scanning through the messages in the queue to insert it at the
     * correct index.
     * 
     * @param m Message to be added to the queue
     */
    public synchronized void addMessageToIncomingQueue(Message m){
         if(2>5&&Debug.debugGROOP)System.err.println("GRP(2)-ADDING");
  /*      for(int i=0;i<incomingMessages.size();i++){
            Message m2 = (Message)incomingMessages.elementAt(i);
            if(m.getTimeStamp().before(m2.getTimeStamp())){
                incomingMessages.insertElementAt(m,i);
                return;
            }
      }  */
        incomingMessages.addElement(m);
        //System.out.println("NOTIFYING "+new Date().getTime());
        notifyAll();
    }
    
    /**
     * Non-blocking
     * 
     * @return null if empty| first message in the queue
     */
    synchronized public Message getNextMessageNonBlocking(){
        if(incomingMessages.size()!=0){
            Message firstMessage= (Message) incomingMessages.elementAt(0);
            incomingMessages.remove(firstMessage);
            if(2>5&&Debug.debugGROOP){
           System.err.println("GRP(31)-RECEIVED MESSAGE ON PARTICIPANTTHREAD-NONBLOCKING-QUEUE IS"+ this.incomingMessages.size());
           if(firstMessage instanceof MessageChatTextFromClient){
               MessageChatTextFromClient mctfc = ( MessageChatTextFromClient)firstMessage;
               System.err.println("GRP(3A)-OTHER THREAD - READING ADDING "+mctfc.getText());
           }
       }


            return firstMessage;  
        }
        return null;
    }
    
    /**
     * Blocking 
     * @return first message in the queue
     */
    synchronized public Message getNextMessageBlocking(){
       if(Debug.debugBLOCKAGE){System.out.println("MCT4955");System.out.flush();}
       while(incomingMessages.size()==0){
           if(Debug.debugBLOCKAGE){System.out.println("MCT4955");System.out.flush();}
           try{
             if(true||Debug.verboseOUTPUT)System.out.println("GOING TO SLEEP "+new Date().getTime());System.out.flush();
             wait();
             if(true||Debug.verboseOUTPUT)System.out.println("WAKING FROM SLEEP "+new Date().getTime());System.out.flush();
           }catch(Exception e){
               System.out.println("QueueBlockingForRecipient WOKEN UP: "+new Date().getTime());
           }  
       }
       if(Debug.debugBLOCKAGE){System.out.println("MCT4956");System.out.flush();}

       Message firstObject= (Message)incomingMessages.elementAt(0);
       if(Debug.debugBLOCKAGE){System.out.println("MCT4957");System.out.flush();}
       incomingMessages.remove(firstObject);
       if(Debug.debugBLOCKAGE){System.out.println("MCT4958");System.out.flush();}
       if(Debug.debugGROOP&&r.nextInt(100)==0){
           System.err.println("GRP(31)-RECEIVED MESSAGE QUEUE IS"
                   + this.incomingMessages.size());
       }
       if(Debug.debugBLOCKAGE){System.out.println("MCT4959");System.out.flush();}
       return firstObject;  
    }

    Random r = new Random();
}

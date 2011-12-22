/*
 * ConversationIO.java
 *
 * Created on 17 November 2007, 16:28
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package diet.server.io;

import diet.debug.Debug;
import java.io.File;

import diet.message.Message;
import diet.server.Conversation;
import diet.server.QueueBlockingForRecipient;
import diet.server.conversationhistory.turn.Turn;


public class ConversationIO extends Thread{
    
    //Vector messages = new Vector();
    //Vector turns = new Vector();
    private boolean conversationInProgress = true;
    ConversationIOWriting convIO;
    //LinkedBlockingQueue lbq = new LinkedBlockingQueue();
    //Write own queue that has the desired features    
    QueueBlockingForRecipient qbfr = new QueueBlockingForRecipient();
    Conversation c;
    
    public ConversationIO(Conversation c,String parentDir,String suffix,Turn t){
       super();
       this.c=c;
       convIO = new  ConversationIOWriting(c,parentDir,suffix, t);
       this.start();
    }
    
    
    public void run(){
          while(conversationInProgress){
               Object o = qbfr.getNextBlocking();
               
               if(o instanceof Message){
                   convIO.saveMessage((Message)o);
                   
               }
               else if(o instanceof Turn){
                   convIO.saveTurn((Turn)o);
                   if(Debug.verboseOUTPUT)System.out.println("SAVING TURN");
               }
          } 
          convIO.closeAllFiles();
    }

    
    public void saveMessage(Message m){
      if(Debug.debugBLOCKAGE){System.out.println("MCT66604");System.out.flush();}
       qbfr.enqueue(m) ;
       if(Debug.debugBLOCKAGE){System.out.println("MCT66606b");System.out.flush();}

    }

    public void saveMessage2FORDEBUG(Message m){
      if(Debug.debugBLOCKAGE){System.out.println("SINDBG0000");System.out.flush();}
       qbfr.enqueueDEBUGSAVE(m) ;
       if(Debug.debugBLOCKAGE){System.out.println("SINDBG0002");System.out.flush();}

    }


    public void saveTurn(Turn t){
       qbfr.enqueue(t) ;
        
    }
        
    public void finalize(){
         
    }    
    
    public File getFileNameContainingConversationData(){
        return convIO.getFileNameContainingConversationData();
    }
    
    public void saveErrorLog(Throwable t){
        try{
           convIO.saveErrorLog(t);
        }catch (Exception e){
            
        }  
    }
    
    public void saveErrorLog(String s){
        try{
           convIO.saveErrorLog(s);
        }catch (Exception e){
            
        }  
    }
    public void saveWindowTextToLog(String windowName, String s)
    {
    	convIO.saveWindowTextToLog(windowName, s);
    	
    }
    
    public void shutThreadDownAndCloseFiles(){
        conversationInProgress=false;
    }
}

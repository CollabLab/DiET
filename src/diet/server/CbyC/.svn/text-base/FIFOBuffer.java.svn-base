/*
 * QueueBlockingForRecipient.java
 *
 * Created on 21 January 2008, 12:19
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package diet.server.CbyC;
import diet.debug.Debug;
import java.util.Vector;

import diet.server.Conversation;
import diet.server.CbyC.Sequence.Sequence;
import diet.server.ConversationController.DefaultConversationController;

/**
 *
 * This is the default implementation. It ignores cursor change as part of the Buffer
 * 
 * @author user
 */
public class FIFOBuffer {
    
    //Random r = new Random();
    private boolean inputIsCompleted = false;
    private Vector v = new Vector();
    private int maximumSize = 0; //DO NOT CHANGE ANY OF THESE
    DefaultConversationController cC;
    Sequence parent;
    private boolean blocked = false;
    
     /**
     * Creates a new instance of QueueBlockingForRecipient
     */
    public FIFOBuffer(Sequence parent,DefaultConversationController cC,int trickleSize) {
        this.cC=cC;
        this.parent=parent;
        maximumSize = trickleSize;
    }
    
    synchronized public void setTrickleSize(int trickleSize){
        maximumSize = trickleSize;
        notifyAll();
    }
    
    synchronized public void blockAllOutputForMillisecs(final long millisecs){
        Thread r = new Thread(){
            public void run(){
                 blocked = true;
                 try{
                     sleep(millisecs);    
                 }catch (Exception e){           
                 }
                 
            }  
        };
    }
    
    
    public int getTrickleSize(){
        return maximumSize;
    }
    
    synchronized public void setInputCompleted(){
        this.inputIsCompleted = true;
        notifyAll();
        System.err.println("..."+this.parent.getType());

    }
    synchronized public boolean isInputCompleted(){
        return this.inputIsCompleted;
    }
    
    
    public int getSize(){
        return this.v.size();
    }
    synchronized public void enqueue(Object o){
        if(Debug.debugFIFOBufferForceSystemExit&&o==null)System.exit(-10);
        //Conversation.printWSln("FIFOBUFFER", "ADDING TO QUEUE");
        v.addElement(o);
        parent.postFIFOEnqueing();
        notifyAll();
    }
   
    synchronized public void enqueueNoCallBack(Object o){
         if(Debug.debugFIFOBufferForceSystemExit&&o==null)System.exit(-9);
        //Conversation.printWSln("FIFOBUFFER", "ADDING TO QUEUE");
        v.addElement(o);
        notifyAll();
    }
    
    synchronized public void insertNoCallBack(Object o,int index){
         if(Debug.debugFIFOBufferForceSystemExit&&o==null)System.exit(-8);
        //Conversation.printWSln("FIFOBUFFER", "ADDING TO QUEUE");
        try{
          v.insertElementAt(o,index);
        }catch(Exception e){
            Conversation.saveErr("Error adding "+o.toString()+" to FIFOBUFFER");
            Conversation.saveErr(e);
        }
        notifyAll();
    }
    synchronized public void insert(Object o, int i)
    {
    	 if(o==null)System.exit(-7);
         //Conversation.printWSln("FIFOBUFFER", "ADDING TO QUEUE");
         try{
           v.insertElementAt(o, i);
         }catch(Exception e){

             Conversation.saveErr("Error adding "+o.toString()+" to FIFOBUFFER");
             Conversation.saveErr(e);
         }
         parent.postFIFOEnqueing();
         notifyAll();
    	
    }
   
    
    synchronized public Object peekNonBlocking(){
        if(v.size()!=0){
            return v.elementAt(0);
        }
        return null;
    }
    
    synchronized public Vector getAllContents(){
        return this.v;
                
    }
    
    synchronized public Object getNextBlockingObeyingTrickle(){
        while(!this.inputIsCompleted||v.size()>0){
            //Conversation.printWSln("FIFOBUFFER2", "ADDING TO QUERY1");
            try{
                if(v.size()>0&&!blocked){
                     Object firstObject= v.elementAt(0);
                     v.remove(firstObject);
                     return firstObject;
                }
                else{
                   
                   wait();    
                }
                
            }catch(Exception e){
                Conversation.printWSln("FIFOBUFFER", "ERROR..."+e.getMessage());
                e.printStackTrace();
            }           
        }
        Conversation.printWSln("FIFOBUFFER", "RETURNING NULL");
       return null;
    }
    
    
    
    public String getApproximationOfContents(){
        String s="";
        for(int i =0;i<v.size();i++){
            Vector v2 = (Vector)v.elementAt(i);
            DocChange dc = (DocChange)v2.elementAt(0);
            if(dc instanceof DocInsert){
                DocInsert di = (DocInsert)dc;
                s = s+di.getStr();
            }
        }
        return s;
    }
    
}

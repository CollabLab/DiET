/*
 * QueueBlockingForRecipient.java
 *
 * Created on 21 January 2008, 12:19
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package diet.server;
import diet.debug.Debug;
import java.util.Date;
import java.util.Vector;

/**
 *
 * @author user
 */
public class QueueBlockingForRecipient {
    
    /**
     * Creates a new instance of QueueBlockingForRecipient
     */
    private Vector v = new Vector();
    
    public QueueBlockingForRecipient() {
    }
    
    
    synchronized public void enqueue(Object o){
        if(Debug.debugBLOCKAGE){System.out.println("MCT66605");System.out.flush();}
        v.addElement(o);
         if(Debug.debugBLOCKAGE){System.out.println("MCT66605bb");System.out.flush();}
         if(Debug.debugBLOCKAGE){System.out.println("MCT66605ccc");System.out.flush();}
        notifyAll();
    }

    synchronized public void enqueueDEBUGSAVE(Object o){
        if(Debug.debugBLOCKAGE){System.out.println("SINSAVE1");System.out.flush();}
        v.addElement(o);
         if(Debug.debugBLOCKAGE){System.out.println("SINSAVE2");System.out.flush();}
         if(Debug.debugBLOCKAGE){System.out.println("SINSAVE3");System.out.flush();}
        notifyAll();
    }
    
   
    
    synchronized public Object getNextBlocking(){
       while(v.size()==0){
           try{
             wait();
           }catch(Exception e){
               System.out.println("QueueBlockingForRecipient WOKEN UP: "+new Date().getTime());
           }  
       } 
       Object firstObject= v.elementAt(0);
       v.remove(firstObject);
       return firstObject;  
    }
}

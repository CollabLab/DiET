/*
 * LabelManager.java
 *
 * Created on 15 December 2007, 22:27
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package diet.server;
import java.util.Hashtable;
/**
 * This stores the latest status window message sent to each participant.
 * This is to cut down network traffic and also reduce the size and improve the
 * legibility of the log files. 
 * 
 * 
 * @author user
 */
public class LabelManager {
    
    Conversation c;
    private Hashtable lastReceivedLabels = new Hashtable();
    
    
    
    /**
     * Creates a new instance of LabelManager
     */
    public LabelManager(Conversation c) {
        this.c=c;
    }
    
   
     
    /**
     * Checks whether the status bar of a participant is already displaying a particular String. If it isn't the
     * value is updated.
     * 
     * @param recipient
     * @param window
     * @param text
     * @return whether label is already set to text value
     */
    public boolean isLabelAlreadySetToValue_AndIfNotUpdateIt_IgnoreEnableDisableStatus(Participant recipient,int window,String text){
        //c.printWln("DEBUG", "LOOKING FOR IGNORING REDENABLE"+text);
        Hashtable pSender = (Hashtable)lastReceivedLabels.get(recipient);
         if(pSender==null){
             pSender=new Hashtable();
             lastReceivedLabels.put(recipient,pSender);
         }
         LabelManagerLabel labelCurrently = (LabelManagerLabel)pSender.get(window);
         if(labelCurrently==null){
             pSender.put(window,new LabelManagerLabel(text));
             //c.printWln("DEBUG", "LOOKING FOR IGNORING REDENABLE ..LABEL IS NULL FOR"+text);
             return false;
         }
         else if (!labelCurrently.equalsIgnoreLabelAndEnable(text)){
             pSender.put(window,new LabelManagerLabel(text));
             //c.printWln("DEBUG", "LOOKING FOR IGNORING REDENABLE ..LABEL IS NOT THE SAME"+text);
             return false;
         }
         else{
             //c.printWln("DEBUG", "LOOKING FOR IGNORING REDENABLE ..LABEL IS THE SAME"+text);
             return true;
         }
         
    }
    public boolean isLabelAlreadySetToValueAndEnabledDisabledAndColour_AndIfNotUpdateIt(Participant recipient,int window,String text,boolean isRed,boolean isEnabled){
        //c.printWln("DEBUG", "LOOKING FOR ENABLERED"+text); 
        Hashtable pSender = (Hashtable)lastReceivedLabels.get(recipient);
         if(pSender==null){
             pSender=new Hashtable();
             //c.printWln("DEBUG", "LOOKING FOR ENABLERED SENDER IS NULL "+text); 
             lastReceivedLabels.put(recipient,pSender);
         }
         LabelManagerLabel labelCurrently = (LabelManagerLabel)pSender.get(window);
         if(labelCurrently==null){
             pSender.put(window,new LabelManagerLabel(text,isRed,isEnabled));
             //c.printWln("DEBUG", "LOOKING FOR ENABLERED LABEL IS NULL "+text); 
             return false;
         }
         else if (!labelCurrently.equalsStringLabelAndEnable(text, isRed, isEnabled)){
             pSender.put(window,new LabelManagerLabel(text,isRed,isEnabled));
             //c.printWln("DEBUG", "LOOKING FOR ENABLERED LABEL IS NOT THE SAME "+text); 
             return false;
         }
         else{
             //c.printWln("DEBUG", "LOOKING FOR ENABLERED LABEL IS THE SAME"+text); 
             return true;
         }
         
    }
   public class LabelManagerLabel{
       public String label;
       public boolean isRed;
       public boolean isEnabled;
       public boolean hasBeenSetBeforeIgnoringIsRedAndEnableStatus=true;
       
       
       public LabelManagerLabel(String s){
           label=s;
           isRed=false;
           isEnabled=false;
           this.hasBeenSetBeforeIgnoringIsRedAndEnableStatus=true;
       }
        public LabelManagerLabel(String s,boolean red,boolean enabled){
           label=s;
           isRed=red;
           isEnabled=enabled;
           this.hasBeenSetBeforeIgnoringIsRedAndEnableStatus=false;
       }
       
       
       public boolean equalsIgnoreLabelAndEnable(String s){
           if(s.equals(label)){
               return true;
           }
           return false;       
       }
       public boolean equalsStringLabelAndEnable(String s,boolean red,boolean enabled){
           //c.printWln("DEBUG", "COMPARING PREVIOUS WHICH WAS "+label+Boolean.toString(isRed)+" and "+Boolean.toString(isEnabled)+" to "+s+ Boolean.toString(red)+Boolean.toString(enabled)); 
           if(hasBeenSetBeforeIgnoringIsRedAndEnableStatus){
               //c.printWln("DEBUG", "HASEVALUATED IT AS NEING SET INDEPENDENTLY BEFORE AND THEREFORE FALSE");
               return false;
           }
           if(label.equals(s)&&isRed==red&&isEnabled==enabled){
               //c.printWln("DEBUG", "HASEVALUATED THEM AS EQUAL AND THEREFORE TRUE");
               return true;
           }
           //c.printWln("DEBUG", "HASEVALUATED IT AS NEING SET INDEPENDENTLY BEFORE AND THEREFORE FALSE");
           return false;
       }
   } 
   
}


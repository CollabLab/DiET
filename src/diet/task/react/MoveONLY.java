/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package diet.task.react;

import diet.server.Participant;
import java.util.Vector;

/**
 *
 * @author sre
 */
public abstract class MoveONLY extends Move implements Cloneable{

   
    String name;
    public Participant p;
    long timestampOfReceipt = -99999;

    
    
    public String getName() {
        return name;
    }

    public Participant getP() {
        return p;
    }


     public MoveONLY(Game g,Participant p,String name){
         super(g);
         this.name=name;
         this.p=p;

     }


     
     
     
     public void saveState(Participant originOfMove){
         g.rtc.cC.getC().saveDataToFile("ONLY"+"|"+g.getSet()+"|"+ g.getConsecutiveCorrect(), originOfMove.getParticipantID(), originOfMove.getUsername(), this.timestampOfReceipt,  this.timestampOfReceipt,
                 "", null);
     }

     public boolean isMoveOK(Participant origin,SelectionState ss, long timeStampOfReceipt){
         if(origin==p){
             if(! ss.isOtherPressed(origin)){
                 if(ss.isPCurrentlySelectingName(origin, name)){
                      this.isComplete=true;
                      this.timestampOfReceipt=timeStampOfReceipt;
                      return true;
                 }else{
                      System.err.println("ISNOTCURRENTLYSELECTINGNAME");
                 }

             }else{
                 System.err.println("OTHERISPRESSED");
             }
         }
         else{
             System.err.println("NOTHEMOSTRECENT");
             System.err.println("NEEDS "+p.getUsername());
             System.err.println("....received from: "+origin.getUsername());
         }


        
         return false;
     }



     /*
      * Type:                   ONLY / AND1 vs. AND2
      * Participant:            Name
      * NameOfSel:              String
      *(AND) PARTICIPANT2:      Name
      *(AND) Name of sel2::     String
      * TimeOfReceiptOnServer
      *
      *
      */




     public String getInfo(){
         String s1 = "ONLY";
         String s2 = this.p.getUsername();
         String s3 = this.name;
         String s4 ="NotSelected"; if(this.isComplete) s4="Selected";
         return   "|"+s1+"|"+s2+"|"+s3+"|"+s4;
     }


     public Vector getInfoAsVector(){
         String s1 = "ONLY";
         String s2 = this.p.getUsername();
         String s3 = this.name;
         String s4 ="NotSelected"; if(this.isComplete) s4="Selected";
         Vector vInfo = new Vector();
         vInfo.addElement(s1);vInfo.addElement(s2);vInfo.addElement(s3);vInfo.addElement(s4);
         return vInfo;
         
         
         
     }
     

      public String getMoveForParticipant(Participant p){
        if(this.p==p)return this.name;
        return null;
    }
}

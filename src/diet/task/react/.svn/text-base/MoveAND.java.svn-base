/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package diet.task.react;

import diet.debug.Debug;
import diet.server.Participant;
import java.util.Date;
import java.util.Vector;

/**
 *
 * @author sre
 */
public class MoveAND extends Move implements Cloneable{

    
    String a;
    String b;

    Participant pA;
    Participant pB;

    long aSelectionTime =0;
    long bSelectionTime =0;




    public MoveAND(Game g,Participant pA,String a,Participant pB, String b){
        super(g);
        this.a=a;
        this.b=b;

        this.pA=pA;
        this.pB=pB;
    }


     public MoveAND getResetCopy(){
        MoveAND mva = new MoveAND(g,pA, a,pB,b);
        return mva;
    }

    public MoveAND getFullCopy(){
        MoveAND mva = new MoveAND(g,pA, a,pB,b);
        mva.a=this.a;
        mva.b=this.b;
        mva.pA=this.pA;
        mva.pB=this.pB;
        mva.aSelectionTime=this.aSelectionTime;
        mva.bSelectionTime=this.bSelectionTime;
        return mva;
    }


    public boolean isMoveOK(Participant origin,SelectionState ss, long timeOfReceipt){
        if(Debug.debugREACT){
          System.err.println("@SEARCHINGTHROUGHAND:");
          System.err.println("@MOSTRECENTPARTICIPANT:" +origin.getUsername());
          System.err.println("@PA:" +pA.getUsername());
          System.err.println("@PB:" +pB.getUsername());
          System.err.println("@MOSTRECENTSELECTIONA:" +ss.getpAMostRecentSelection());
          System.err.println("@MOSTRECENTSELECTIONB:" +ss.getpBMostRecentSelection());
          System.err.println("@ASELECTIONTIME: "+this.aSelectionTime);
          System.err.println("@BSELECTIONTIME: "+this.bSelectionTime);

        }

        if(aSelectionTime==0&&bSelectionTime==0){
            if(origin==pA
                    && ss.getpAMostRecentSelection().equalsIgnoreCase(a)
                    &&!ss.pBPressed){
                  aSelectionTime = timeOfReceipt;
                  return true;
            }
            if(origin==pB
                    && ss.getpBMostRecentSelection().equalsIgnoreCase(b)
                    &&!ss.pAPressed){
                 bSelectionTime = timeOfReceipt;
                 return true;
            }
        }
        else{
             if(aSelectionTime==0&&bSelectionTime!=0){
                 if(origin==pA){
                        if(ss.getpAMostRecentSelection().equalsIgnoreCase(a)){
                              if(ss.pBPressed){
                                   aSelectionTime = new Date().getTime();
                                   this.isComplete=true;
                                   return true;
                               }
                               else{
                                   System.err.println("###BISNOTPRESSED");
                        }}else{
                            System.err.println("###MOSTRECENTSELECTIONISNOT "+a+"...."+ss.getpAMostRecentSelection());
                 }}else{
                     System.err.println("###MOSTRECENTPARTICIPANTISNOT "+pA); 
                 }
             }
             else{
                 System.err.println("###ASELECTIONTIMEISNT "); 
             }

             if(bSelectionTime==0&&aSelectionTime!=0){
                 if(origin==pB){
                        if(ss.getpBMostRecentSelection().equalsIgnoreCase(b)){
                              if(ss.pAPressed){
                                   bSelectionTime = new Date().getTime();
                                   this.isComplete=true;
                                   return true;
                               }
                               else{
                                   System.err.println("$$###BISNOTPRESSED");
                        }}else{
                            System.err.println("$$$###MOSTRECENTSELECTIONISNOT "+a+"...."+ss.getpAMostRecentSelection());
                 }}else{
                     System.err.println("$$$###MOSTRECENTPARTICIPANTISNOT "+pA);
                 }
             }
             else{
                 System.err.println("$$###ASELECTIONTIMEISNT ");
             }

            
        }

     /*   else{    //Second half here...second half must be done while first half is pressed
            if(aSelectionTime==0&& ss.mostRecentParticipant==g.rtc.pA && ss.pAMostRecentSelection.equalsIgnoreCase(a)&&ss.pBPressed){
                  aSelectionTime = new Date().getTime();
                  this.isComplete=true;
                  return true;
            }
            if(bSelectionTime==0&& ss.mostRecentParticipant==g.rtc.pB && ss.pAMostRecentSelection.equalsIgnoreCase(b)&&ss.pAPressed){
                  bSelectionTime = new Date().getTime();
                  this.isComplete=true;
                  return true;
            }
        }*/
        //System.exit(-234);
        return false;
    }



    public void reset(){
        aSelectionTime=0;
        bSelectionTime=0;
        this.isComplete=false;
    }


    public String getInfo(){
         String s1 = "AND";
         if(aSelectionTime==0 & bSelectionTime==0){
             s1 = "AND0";
         }
         else if (aSelectionTime !=0 & bSelectionTime != 0){
             s1 = "AND2";
         }
         else{
             s1 = "AND1";
         }
         String s2 = pA.getUsername();
         String s3 = a;
         String s4 = "NOTSELECTED"; if(this.aSelectionTime>0)s4=""+this.aSelectionTime;
         String s5 = pB.getUsername();
         String s6 = b;
         String s7 = "NOTSELECTED"; if(this.bSelectionTime>0)s4=""+this.bSelectionTime;
         return    "|"+s1+"|"+s2+"|"+s3+"|"+s4+"|"+s5+"|"+s6+"|"+s7+"|";
     }



    public Vector getInfoAsVector(){
        Vector vInfo = new Vector();
        String s1 = "AND";
         if(aSelectionTime==0 & bSelectionTime==0){
             s1 = "AND0";
         }
         else if (aSelectionTime !=0 & bSelectionTime != 0){
             s1 = "AND2";
         }
         else{
             s1 = "AND1";
         }
         String s2 = pA.getUsername();
         String s3 = a;
         String s4 = "NOTSELECTED"; if(this.aSelectionTime>0)s4=""+this.aSelectionTime;
         String s5 = pB.getUsername();
         String s6 = b;
         String s7 = "NOTSELECTED"; if(this.bSelectionTime>0)s4=""+this.bSelectionTime;
         vInfo.addElement(s1);vInfo.addElement(s2);vInfo.addElement(s3);vInfo.addElement(s4);vInfo.addElement(s5);vInfo.addElement(s6);vInfo.addElement(s7);
         return vInfo;
    }
            
    
    
    

    public String getMoveForParticipant(Participant p){
        if(p==pA)return a;
        if(p==pB)return b;
        return null;
    }

}

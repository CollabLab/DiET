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
public class MoveONLYFirstHalf extends MoveONLY implements Cloneable{

    
    public MoveONLYFirstHalf(Game g, Participant p, String name) {
        super(g, p, name);
    }
    
    public MoveONLY getResetCopy(){
        MoveONLY mvo = new MoveONLYFirstHalf(g,p,name);
        return mvo;
    }
    
    public MoveONLY getFullCopy(){
         MoveONLY mvo = new MoveONLYFirstHalf(g,p,name);
         mvo.name=this.name;
         mvo.p=this.p;
         mvo.timestampOfReceipt=this.timestampOfReceipt;
         mvo.isComplete=this.isComplete;
         return mvo;
    }
    
    public String getInfo(){
         String s1 = "ONLY(FIRST)";
         String s2 = this.p.getUsername();
         String s3 = this.name;
         String s4 ="NotSelected"; if(this.isComplete) s4="Selected";
         return   "|"+s1+"|"+s2+"|"+s3+"|"+s4;
     }


     public Vector getInfoAsVector(){
         String s1 = "ONLY(FIRSTD)";
         String s2 = this.p.getUsername();
         String s3 = this.name;
         String s4 ="NotSelected"; if(this.isComplete) s4="Selected";
         Vector vInfo = new Vector();
         vInfo.addElement(s1);vInfo.addElement(s2);vInfo.addElement(s3);vInfo.addElement(s4);
         return vInfo;   
     }
    
    
}

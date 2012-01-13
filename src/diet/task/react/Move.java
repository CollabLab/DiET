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
public abstract class Move implements Cloneable{

    boolean isComplete = false;
    public Game g;

    public Move (Game g){
        this.g=g;
    }

    public boolean isComplete(){
        return isComplete;
    }

    abstract public Move getResetCopy();
     abstract public Move getFullCopy();
    
    public boolean isMoveOK(Participant origin,SelectionState ss, long timeOfReceipt){
       return false;
    }

    public void reset(){
       this.isComplete=false;
    }

    public String getInfo(){
        return "";
    }
    abstract public Vector getInfoAsVector();
    
    public String getMoveForParticipant(Participant p){
        return null;
    }

}

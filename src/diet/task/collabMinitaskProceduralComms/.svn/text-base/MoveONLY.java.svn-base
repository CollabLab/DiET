/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package diet.task.collabMinitaskProceduralComms;

import diet.debug.Debug;
import diet.server.Participant;

/**
 *
 * @author sre
 */
public class MoveONLY extends Move{

    Participant pWhoHasTo;
    String name;

    public MoveONLY(Participant p, String name){
        this.pWhoHasTo=p;
        this.name=name;
        //System.exit(-4);
        //Debug.printDBG(name);

    }

     public String getWordForParticipantAsHTML(Participant p){
         if(p==pWhoHasTo)return name;
         return null;
    }

     public String getWordForParticipant(Participant p){
         if(p==pWhoHasTo)return name;
         return null;
    }


    @Override
    public String getName() {
        return name;
    }
    public String evaluate(Participant p, String name, boolean isLastSelection){
        //System.exit(-5);
        
        if(!isLastSelection & p==pWhoHasTo && this.isSuccessful()){
            return Moves.correctWORDReselected;
        }
        else if(!isLastSelection & p==pWhoHasTo && !this.isSuccessful()){
            return Moves.errorInterface; //This shouldn't happen
        }
        else if(!isLastSelection & p!=pWhoHasTo && this.isSuccessful()){
            return Moves.cantGoBackToPriorState_TriedToSelectOthersWord;
        }
        else if(p == pWhoHasTo && !this.isSuccessful()) {
            this.setSuccessful();
            return Moves.correctWORD;
        }
        else if(p == pWhoHasTo && this.isSuccessful()) {
            this.setSuccessful();
            return Moves.errorWordIsAlreadySelectedByRequestor;
        }
        else{
            //this.setUnSuccessful();
            return Moves.errorTriedToSelectOther;
        }
    }

}

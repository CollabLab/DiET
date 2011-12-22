/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package diet.task.collabMinitaskProceduralComms;

import diet.server.Participant;

/**
 *
 * @author sre
 */
public class MoveXOR extends Move{

    Participant whoPerformedMove;
    String name;

    public MoveXOR(String name){
        this.name=name;
    }

     public String getWordForParticipantAsHTML(Participant p){
         return "<U>"+name+"</U>";
    }

     public String getWordForParticipant(Participant p){
         return name;
    }


   @Override
    public String getName() {
        return name;
    }

    public String evaluate(Participant p, String name,boolean isLastInSequence){

        if(!isLastInSequence & isSuccessful()){
            this.setUnSuccessful();
            whoPerformedMove =p;
            this.setSuccessful();
            return Moves.correctWORDReselected;
        }

        if(isLastInSequence & isSuccessful() & whoPerformedMove == p){
            return Moves.errorWordIsAlreadySelectedByRequestor;
        }
        else if (isLastInSequence & isSuccessful() & whoPerformedMove != p){
            return Moves.errorWordHasAlreadyBeenSelectedByOtherPerson;
        }
        else if (isLastInSequence){
            whoPerformedMove = p;
            this.setSuccessful();
            return Moves.correctWORD;
        }
        return null;

    }
}

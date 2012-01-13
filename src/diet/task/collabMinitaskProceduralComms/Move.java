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
public class Move {

    private boolean successful;

    public String getName(){
        return "";
    }

    public boolean hasAlreadyBeenCoordinatedOn(){
        return successful;
    }

    public void setSuccessful(){
        successful=true;
    }

    public boolean isSuccessful(){
       return successful;
    }

    public void setUnSuccessful(){
        successful=false;
    }

    public String getWordForParticipantAsHTML(Participant p){
        return null;
    }
    public String getWordForParticipant(Participant p){
        return null;
    }

    public String evaluate(Participant p, String name, boolean isLastInSequence){
       return "ERRORINSOFTWARE";
    }






    /*/ACTIONNAME
     *
     * Move    (NOT RIGHT NAME BUT NEXT ONE IS CORRECT)
     * Move    (NOT RIGHT NAME BUT NEXT ONE IS CORRECT)
     * Move     (NOT RIGHT NAME BUT NEXT ONE IS CORRECT)
     * Move     (NOT RIGHT NAME BUT NEXT ONE IS CORRECT)
     * Move     If the command matches the move name, then evaluate it.
     * Move
     * Move
     *
     *
     *
     *
     * Move    (NOT RIGHT NAME BUT NEXT ONE IS CORRECT)
     * Move    (NOT RIGHT NAME BUT NEXT ONE IS CORRECT)
     * Move    (NOT RIGHT NAME BUT NEXT ONE ISN'T CORRECT - THEN NEED TO STOP)
     * Move
     * Move
     * Move
     *
     *
     *
     *
     * Move    (NOT RIGHT NAME BUT NEXT ONE IS CORRECT)
     * Move    (NOT RIGHT NAME BUT NEXT ONE IS CORRECT)
     * Move    RIGHT NAME AND IT'S THE LAST ONE - IF GETS IT CORRECT THEN MOVES TO NEXT SET
     *
     *
     *
     *
     *
     *
     * Move    (NOT RIGHT NAME BUT NEXT ONE IS CORRECT)
     * Move    (NOT RIGHT NAME BUT NEXT ONE IS CORRECT)
     * Move    (NOT RIGHT NAME BUT NEXT ONE IS CORRECT)
     * Move    (NOT RIGHT NAME BUT NEXT ONE IS CORRECT)
     * Move     (NOT RIGHT NAME BUT NEXT ONE IS CORRECT)
     * Move    (NOT RIGHT NAME BUT NEXT ONE IS CORRECT BUT FINISHED ALL MOVES) THIS SHOULDN'T HAPPEN AS THEN IT SHOULD HAVE GONE TO NEXT ONE
     *
     *
     */



}

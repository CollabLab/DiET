/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package diet.task.react;

import diet.debug.Debug;
import diet.server.Participant;

/**
 *
 * @author sre
 */
public class SelectionState {

    
    ReactTaskController rtc;
    boolean pAPressed=false;
    boolean pBPressed=false;

    private String pAMostRecentSelection = null;
    private String pBMostRecentSelection = null;

    

    public SelectionState(ReactTaskController rtc){
        this.rtc=rtc;
    }

    public String getpAMostRecentSelection(){
        return this.pAMostRecentSelection;
    }
    
    public String getpBMostRecentSelection(){
        return this.pBMostRecentSelection;
    }
    
    public void updateSelectionPressed(Participant origin, String selection){
        //if(selection==null)System.exit(-4444);

        if(origin ==rtc.pA){
            pAPressed = true;
            pAMostRecentSelection=selection;
           
        }
        else if(origin ==rtc.pB){
            pBPressed = true;
            pBMostRecentSelection = selection;
           
        }
    }


    public void updateSelectionReleased(Participant origin, String selection){
        if(origin ==rtc.pA){
            pAPressed = false;
            pAMostRecentSelection=selection;
            
        }
        else if(origin ==rtc.pB){
            pBPressed = false;
            pBMostRecentSelection = selection;
           
        }
    }


    public boolean isOtherPressed(Participant p){
        if(p==rtc.pA) return this.pBPressed;
        if(p==rtc.pB) return this.pAPressed;
        return false;
    }


    

    public boolean isPCurrentlySelectingName(Participant p,String name){
        if(Debug.debugREACT) System.err.println("PC: looking for "+name+"...from "+p.getUsername());
        if(Debug.debugREACT) System.err.println("PC: MOSTRECENT: "+pAMostRecentSelection+"...."+pBMostRecentSelection);
        if(p==rtc.pA & this.pAPressed && pAMostRecentSelection!=null){
           
            if(pAMostRecentSelection.equalsIgnoreCase(name))return true;
        }
        else if(p == rtc.pB & this.pBPressed && pBMostRecentSelection != null) {
            
           if(pBMostRecentSelection.equalsIgnoreCase(name))return true;
        }
        return false;
    }

}

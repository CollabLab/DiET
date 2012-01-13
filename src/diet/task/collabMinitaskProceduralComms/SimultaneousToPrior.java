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
public class SimultaneousToPrior {

    Participant p1;
    Participant p2;
    String name;
    long timeOfLastPerformance;

    public SimultaneousToPrior(Participant p1,Participant p2, String name){
        this.p1=p1;
        this.p2=p2;
        this.name=name;
    }

    
}

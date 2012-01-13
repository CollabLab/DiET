/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package diet.task.react;

import java.io.Serializable;
import java.util.Vector;

/**
 *
 * @author sre
 */
public class Rules implements Serializable{

    Vector v ;


    public Rules(Vector v) {
        this.v=v;
    }

    public String getNextMoveBySelfAfterSELF(String priormoveBYSELF){
        for(int i=0;i<v.size();i++){
            String[] rule = (String[])v.elementAt(i);
            if(rule[0]!=null){
                  if(rule[0].equalsIgnoreCase(priormoveBYSELF)) return rule[2];
            }
        }
        return null;
    }

    
     public String getNextMoveBySelfAfterOTHER(String priormoveBYOTHER){
        for(int i=0;i<v.size();i++){
            String[] rule = (String[])v.elementAt(i);
            System.err.println("RULE "+i);
            if(rule!=null){
                System.err.println("----"+rule[0]+"----"+rule[1]+"-----"+rule[2]);
            }

            if(rule[1]!=null){

                if(rule[1].equalsIgnoreCase(priormoveBYOTHER)) return rule[2];
            }
        }
        return null;
    }



}

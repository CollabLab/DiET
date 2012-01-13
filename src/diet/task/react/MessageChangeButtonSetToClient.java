/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package diet.task.react;

import diet.message.MessageTask;
import java.io.Serializable;
import java.util.Random;

/**
 *
 * @author sre
 */
public class MessageChangeButtonSetToClient extends MessageTask implements Serializable{

    private String[][] names = new String[5][5];
    Rules rls;


    public MessageChangeButtonSetToClient(String email, String username, String[][] names, Rules rls) {
       super(email,username);
       this.names=names;
       if(names==null){
           fillRandomly(9);
       }
       this.rls=rls;



    }

    public String getName(int x,int y){
        return names[x][y];
    };
    public String[][] getNames(){
         return names;
    }

    public Rules getRules(){
        return rls;
    }


    public void setNames(String[][] names){
         this.names=names;
    }
    Random r = new Random();

    public void fillRandomly(int n){
         names = new String[5][5];
        for(int i=1;i<n+1;i++){
            boolean successfuladd = false;
            while(!successfuladd){
                int x = r.nextInt(5);
                int y = r.nextInt(5);
                String name = names[x][y];
                if(name==null){
                    names[x][y]=""+i;
                    successfuladd=true;
                    
                }
            }
        }
    }
}

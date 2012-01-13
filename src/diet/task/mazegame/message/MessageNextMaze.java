/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package diet.task.mazegame.message;

import java.io.Serializable;

import diet.message.MessageTask;

/**
 *
 * @author user
 */
public class MessageNextMaze extends MessageTask implements Serializable{

    int next;
    
    public MessageNextMaze(String email, String username,int next) {
      super(email,username);
      this.next=next;
    }
    
  public int getNext(){
    return next;
  }

}

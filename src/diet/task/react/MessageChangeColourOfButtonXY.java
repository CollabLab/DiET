/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package diet.task.react;

import diet.message.MessageTask;
import java.awt.Color;

/**
 *
 * @author sre
 */
public class MessageChangeColourOfButtonXY extends MessageTask {

    Color foreground;
    Color background;
    int x;
    int y;
    boolean ignoreALLANDSETALLXY = false;

    public MessageChangeColourOfButtonXY(String email, String username, int x,int y, Color foreground, Color background, boolean ignoreALLANDSETALLXY) {
        super(email,username);
      
        this.foreground=foreground;
        this.background=background;
        this.x=x;
        this.y=y;
        this.ignoreALLANDSETALLXY=ignoreALLANDSETALLXY;
    }


    public Color getBackground() {
        return background;
    }

    public Color getForeground() {
        return foreground;
    }

    public int getX(){
        return x;
    }


    public int getY(){
        return y;
    }

    public boolean getIgnoreALLANDSETALLXY(){
        return this.ignoreALLANDSETALLXY;
    }

}

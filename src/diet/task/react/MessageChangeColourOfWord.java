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
public class MessageChangeColourOfWord extends MessageTask {

    Color foreground;
    Color background;
    String word;


    public MessageChangeColourOfWord(String email, String username, String word, Color foreground, Color background) {
        super(email,username);
        this.word=word;
        this.foreground=foreground;
        this.background=background;
        
    }


    public Color getBackground() {
        return background;
    }

    public Color getForeground() {
        return foreground;
    }

    public String getWord(){
        return word;
    }

}

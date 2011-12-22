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
public class MessageChangeColourOfButton extends MessageTask{

    
    int x;
    int y;
   

    Color c;

    public static int foreground =1;
    public static int border =0;
    public static int background =2;
    
    int foregroundORbackgroundORborder=0;


    public static int senderHasSELECTED = 10;
    public static int senderHasDESELECTED = 11;
    public static int senderHASNEITHERSELECTEDNORDESELECTED = 12;

    int changeSELECTIONSTATUS = senderHASNEITHERSELECTEDNORDESELECTED;

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Color getColor(){
        return c;
    }


    public MessageChangeColourOfButton(String email, String username,int x, int y, Color c, int foregroundORbackgroundORborder,  int changeSELECTIONSTATUS ) {
        super(email,username);
        this.x=x;
        this.y=y;
        this.changeSELECTIONSTATUS=changeSELECTIONSTATUS;
        this.foregroundORbackgroundORborder=foregroundORbackgroundORborder;
        this.c=c;
    }

   

    public boolean isForeGround(){
        if(foregroundORbackgroundORborder==foreground)return true;
        return false;
    }

    public boolean isBorder(){
        if(foregroundORbackgroundORborder==border)return true;
        return false;
    }

    public boolean isBackground(){
        if(foregroundORbackgroundORborder==background)return true;
        return false;
    }

    public int getChangeSELECTIONSTATUS() {
        return changeSELECTIONSTATUS;
    }


}

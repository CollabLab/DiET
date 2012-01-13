/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package diet.task.react;

import diet.message.MessageTask;

/**
 *
 * @author sre
 */
public class MessageMousePressFromClient extends MessageTask{

    String buttonname;
    int x;
    int y;
    long localTimeOfPress;
    long priorPressBySelf;
    long priorReleaseBySelf;
    long priorSelectByOther;
    long priorreleaseByOther;
    
    String priorSelectedStringByOther ="";
    int priorSelectX =-1;
    int priorSelectY = -1;

    
    
    
    

    public long getPriorPressBySelf() {
        return priorPressBySelf;
    }

    public long getPriorReleaseBySelf() {
        return priorReleaseBySelf;
    }

    public long getPriorSelectByOther() {
        return priorSelectByOther;
    }

    public long getPriorreleaseByOther() {
        return priorreleaseByOther;
    }

    
    
    public int getPriorSelectByOtherX() {
        return priorSelectX;
    }

    public int getPriorSelectByOtherY() {
        return priorSelectY;
    }

    public String getPriorSelectedStringByOther(){
        return this.priorSelectedStringByOther;
    }

    
     public MessageMousePressFromClient(String email, String username, String buttonname, int x, int y, long localTimeOfPress, long priorPressBySelf, long priorReleaseBySelf, long priorSelectByOther, long priorreleaseByOther, String priorSelectedStringByOther, int priorSelectX, int priorSelectY) {
        super(email,username);
        this.buttonname=buttonname;
        this.x=x;
        this.y=y;
        this.localTimeOfPress=localTimeOfPress;
        this.priorPressBySelf=priorPressBySelf;
        this.priorReleaseBySelf=priorReleaseBySelf;
        this.priorSelectByOther=priorSelectByOther;
        this.priorreleaseByOther=priorreleaseByOther;
        this.priorSelectedStringByOther=priorSelectedStringByOther;
        this.priorSelectX=priorSelectX;
        this.priorSelectY=priorSelectY;
    }



    public String getButtonname() {
        return buttonname;
    }

    public long getLocalTimeOfPress() {
        return localTimeOfPress;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }



   


}

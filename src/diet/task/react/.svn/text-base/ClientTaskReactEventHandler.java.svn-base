/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package diet.task.react;

import diet.client.ConnectionToServer;
import diet.debug.Debug;
import diet.message.MessageTask;
import diet.task.ClientTaskEventHandler;
import java.awt.Dimension;
import java.util.Date;
import javax.swing.SwingUtilities;

/**
 *
 * @author sre
 */
public class ClientTaskReactEventHandler extends ClientTaskEventHandler {

    JReactTaskFrame jt;


    public ClientTaskReactEventHandler(ConnectionToServer cts, MessageNewReactTask m) {
       super(cts);
       final ConnectionToServer cts2 = cts;
       if(Debug.debugREACT)cts.sendErrorMessage("NOTANERROR3");
       final ClientTaskReactEventHandler ctreh =this;
        SwingUtilities.invokeLater( new Runnable(){public void run(){
               //if(Debug.debugREACT)cts2.sendErrorMessage("NOTANERROR3.5");
               //jt = new JReactTaskFrame(ctreh);
               //if(Debug.debugREACT)cts2.sendErrorMessage("NOTANERROR4");
            }
        });
       try{
       if(Debug.debugREACT)cts2.sendErrorMessage("NOTANERROR3.5");
       jt = new JReactTaskFrame(this);
        if(Debug.debugREACT)cts2.sendErrorMessage("NOTANERROR4");
        }catch (Exception e){
            cts2.sendErrorMessage("ERRORMESSAGE:"+e.getMessage());
            cts2.sendErrorMessage(e);
            cts2.sendErrorMessage("ERRORMESSAGE:"+e.getMessage());
        }
       //if(cts.getUsername().equalsIgnoreCase("Participant1")&&Debug.debugREACTDOAUTORESPONSEOFPARTICIPANT1)performIntercept=true;
    }

    public void closeDown(){

    }
    public void processTaskMove(MessageTask m){
        if(m instanceof diet.task.react.MessageChangeColourOfButton){
            MessageChangeColourOfButton msbctc = (MessageChangeColourOfButton)m;
            if(msbctc.isBorder())jt.setUniqueBorderSelected(msbctc.getX(),msbctc.getY(),msbctc.getColor(),msbctc.getChangeSELECTIONSTATUS());
            else if(msbctc.isForeGround()){
                //System.err.println("COLOR="+msbctc.getColor().toString());
                //System.exit(-4);
               // jt.setUniqueForegroundSelected(msbctc.getX(),msbctc.getY(),Color.red,msbctc.getChangeSELECTIONSTATUS());
               jt.setUniqueForegroundSelected(msbctc.getX(),msbctc.getY(),msbctc.getColor(),msbctc.getChangeSELECTIONSTATUS());
            }
            else if(msbctc.isBackground()){
               jt.setUniqueBackgroundSelected(msbctc.getX(),msbctc.getY(),msbctc.getColor(),msbctc.getChangeSELECTIONSTATUS());
            }
            //if(!msbctc.isSelect()&performIntercept)interceptIncoming(msbctc);

        }
        else if(m instanceof MessageChangeColourOfButtonXY){
            MessageChangeColourOfButtonXY mccXY = (MessageChangeColourOfButtonXY)m;
            jt.changeColourOfButtonXY(mccXY.getX(),mccXY.getY(),mccXY.getForeground(),mccXY.getBackground(),mccXY.getIgnoreALLANDSETALLXY());

        }
        else if (m instanceof diet.task.react.MessageChangeColourOfWord){
            MessageChangeColourOfWord mccof = (MessageChangeColourOfWord)m;
            jt.changeColourOfWord(mccof.getWord(), mccof.getForeground(), mccof.getBackground());

            
        }
        
        else if(m instanceof diet.task.react.MessageChangeButtonSetToClient) {
            MessageChangeButtonSetToClient mcbstc = (MessageChangeButtonSetToClient)m;
            if(mcbstc.getRules()!=null)this.setRules(mcbstc.getRules());
            jt.changeButtonSet(mcbstc.getNames());
            //System.exit(-5);
        }
        else if(m instanceof diet.task.react.MessageDisplayMessageOnReactTaskWindow) {
            MessageDisplayMessageOnReactTaskWindow mdmortw = (MessageDisplayMessageOnReactTaskWindow)m;
            if(mdmortw.getShowGrid()){
                jt.reEnableShowGrid();
            }
            else{
                jt.setDisableAndShowMessage(mdmortw.getText());
            }
        }
    }

   




    public void buttonSelected2(String buttonname, int x, int y, long localTimeOfPress, long priorPressBySelf, long priorReleaseBySelf, long priorSelectByOther, long priorreleaseByOther, String lastSelectByOTher, int lastSelectByOtherX,int lastSelectByOtherY) {
        //if(buttonname==null)System.exit(-5);
         MessageMousePressFromClient mmpfc = new MessageMousePressFromClient(cts.getEmail(),cts.getUsername(),buttonname, x,y,localTimeOfPress, priorPressBySelf, priorReleaseBySelf,priorSelectByOther,priorreleaseByOther, lastSelectByOTher, lastSelectByOtherX, lastSelectByOtherY);
         cts.sendMessage(mmpfc);
         //jt.setUniqueBorderSelected(x,y, true);
         //System.exit(-5);
         //if(buttonname==null)System.exit(-2342342);

    }

   

    public void buttonReleased(String buttonname, int x, int y, long localTimeOfPress, long priorPressBySelf, long priorReleaseBySelf, long priorSelectByOther, long priorreleaseByOther, String lastSelectByOTher, int lastSelectByOtherX,int lastSelectByOtherY) {
          MessageMouseReleaseFromClient mbrfc = new MessageMouseReleaseFromClient(cts.getEmail(),cts.getUsername(),buttonname, x,y, localTimeOfPress, priorPressBySelf, priorReleaseBySelf,priorSelectByOther,priorreleaseByOther, lastSelectByOTher, lastSelectByOtherX, lastSelectByOtherY);
          cts.sendMessage(mbrfc);
    }

    public void mouseEntered(String buttonname, int x, int y, long timeOfEntry){
        MessageMouseEntryFromClient mmefc = new MessageMouseEntryFromClient(cts.getEmail(),cts.getUsername(),buttonname,x,y,timeOfEntry);
        cts.sendMessage(mmefc);
    }



    
    Rules interceptRules;
    public void setRules(Rules r){
        this.interceptRules=r;
    }

    
  






  
}

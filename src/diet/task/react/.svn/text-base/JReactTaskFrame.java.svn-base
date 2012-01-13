/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package diet.task.react;

import diet.server.Participant;
import diet.task.react.ui.JInterfacePanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.HeadlessException;
import javax.swing.JFrame;

/**
 *
 * @author sre
 */
public class JReactTaskFrame extends JFrame{

    JInterfacePanel jip;
    Participant p;


    public JReactTaskFrame(ReactTaskController rtc, Participant p) throws HeadlessException {
      jip = new JInterfacePanel(rtc);
      this.p =p;
      this.add(jip);
      this.setTitle("EXPERIMENTER: "+p.getUsername());
      this.pack();
      this.setVisible(true);
    }



    public JReactTaskFrame(ClientTaskReactEventHandler ctreh) throws HeadlessException {
      jip = new JInterfacePanel(ctreh);
      this.add(jip);
      this.setTitle(ctreh.cts.getUsername());
      this.pack();
      this.setVisible(true);
    }



   public void initCardSpace(){
       jip.initCardSpace();
   }


    public void initButtons(){
        jip.initButtons();
    }




    public void setUniqueBorderSelected(final int x,final int y, final Color c, int selectionSTATUS){
        jip.setUniqueBorderSelected(x, y, c, selectionSTATUS);
    }

     public void setUniqueForegroundSelected(final int x,final int y, final Color c, int selectionSTATUS){
        jip.setUniqueForegroundSelected(x, y, c, selectionSTATUS);
    }

      public void setUniqueBackgroundSelected(final int x,final int y, final Color c, int selectionSTATUS){
        jip.setUniqueBackgroundSelected(x, y, c, selectionSTATUS);
    }


      public void changeColourOfButtonXY(int x, int y, Color foreground, Color background, boolean ignoreXYANDCHANGEALL){
          jip.changeColourOfButtonXY(x, y,  foreground,  background, ignoreXYANDCHANGEALL);

      }



    public void changeColourOfWord(String word, Color foreground, Color background){
        //if(word==null)System.exit(-5);
        jip.changeColourOfWord(word, foreground, background);
        //System.exit(-236);
    }


     public void setDisableAndShowMessage(final String message){
         jip.setDisableAndShowMessage(message);
     }


     public void reEnableShowGrid(){
          jip.reEnableShowGrid();
            }


     public String getButtonname(int x,int y){
         return jip.getButtonname(x, y);
              }


     public Dimension getDimensionForName(String s){
        return  jip.getDimensionForName(s);
      }


     public void changeButtonSet(final String[][] names){
         jip.changeButtonSet(names);
     }


    /** Creates new form JReactTaskFrame */




}

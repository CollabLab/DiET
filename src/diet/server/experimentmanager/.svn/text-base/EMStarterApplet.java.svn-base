/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package diet.server.experimentmanager;

import diet.client.ConnectionToServer;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.UIManager;
import netscape.javascript.JSObject;


/**
 *
 * @author sre
 */
public class EMStarterApplet  extends JApplet {

    
   
    String prefix = "prefix";
    String ip;

      JSObject window ;

    public void init() {

       //System.exit(-234234);

       window = JSObject.getWindow(this);
       
       window.eval("setHTMLInputText('"+"textset1"+"');");
       System.err.println("INITIAL");

        try {
                 UIManager.setLookAndFeel(
                 UIManager.getCrossPlatformLookAndFeelClassName());
             } catch (Exception e) { }

       
       final String participantId = getParameter("participantId");
       final String assignmentId =  getParameter("assignmentId");
       final String userentersvalues = getParameter("userentersvalues");
       final String ipADDRESSOFSERVER = getParameter("serveripaddress");
       final JButton button = new JButton("START the chat client");
       final JLabel label = new JLabel("Connect to: "+ipADDRESSOFSERVER);




        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
               ip = ipADDRESSOFSERVER;
               try{
                 button.setEnabled(false);
                 window.eval("setHTMLInputText('"+"textset2"+"');");
                 if(participantId==null||participantId.length()==0||userentersvalues.equalsIgnoreCase("yes")){
                     ConnectionToServer cts = new ConnectionToServer(ip,20000);
                     label.setText("Connecting to: "+ipADDRESSOFSERVER);
                     cts.start();
                 }else{
                    ConnectionToServer cts = new ConnectionToServer(ip, 20000,participantId);
                    label.setText("StatusMessage2-");
                    label.setText("Connecting to: "+ipADDRESSOFSERVER);
                    cts.start();
                    }
                
                 
                }catch (Exception error){
                    label.setText(e.toString());
                }
            }
        });
        getContentPane().setLayout(new FlowLayout());
       
        getContentPane().add(button);
        getContentPane().add(label);
        
    }

    
    public void updateFieldInSurroundingWebpage(String command){
        window.eval(command);
    }

}



/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package diet.debug;

import java.awt.Dimension;
import java.awt.Font;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;

/**
 *
 * @author sre
 */
public class DebugWindow {


    JFrame jf;
    JTextArea jt;
    JScrollPane jsp;


    public DebugWindow(){

        jf = new JFrame();
        jt = new JTextArea();
        jt.setPreferredSize(new Dimension(600,400));
        jsp = new JScrollPane();
        jsp.getViewport().add(jt);
        jsp.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        jf.getContentPane().add(jsp);
        jf.pack();
        jf.setVisible(true);


        Font font = new Font("Courier New", Font.BOLD, 10);
        jt.setFont(font);

    }


    public void append(final String s){
       SwingUtilities.invokeLater(new Runnable(){
                public void run(){
                     jt.append(s);
                }
            });
    }

    public void setText(final String s){
       SwingUtilities.invokeLater(new Runnable(){
                public void run(){
                     jt.setText(s);
                }
            });
    }

}

/*
 * JTextAreaDebugWindow.java
 *
 * Created on 27 October 2007, 18:13
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */


package diet.server.ui;

import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
/**
 *
 * @author user
 */
public class JTextAreaDebugWindow extends JFrame{
    
    JTextArea jta = new JTextArea();
    /** Creates a new instance of JTextAreaDebugWindow */
    public JTextAreaDebugWindow() {
        super();
        JScrollPane jsp = new JScrollPane();
        jsp.getViewport().add(jta);
        this.getContentPane().add(jsp);
        jta.setPreferredSize(new Dimension(300,150));
        this.setPreferredSize(new Dimension(300,150));
        jsp.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jsp.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        this.validate();
        this.pack();
        this.setVisible(true);
        
    }
    public void print(String s2){
        final String s = s2;
        SwingUtilities.invokeLater(new Runnable(){public void run(){     
          jta.append(s);
          jta.validate();
          jta.repaint();
         }  });
    }
     public void println(String s2){
       final String s = s2;
        SwingUtilities.invokeLater(new Runnable(){public void run(){     
          jta.append(s+"\n");
          jta.validate();
          jta.repaint();
         }  });
    }
    
}

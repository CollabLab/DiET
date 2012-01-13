/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package diet.server.experimentmanager;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Rectangle;

import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

/**
 *
 * @author user
 */
public class JOutput extends JPanel{
     
    JTextArea jta = new JTextArea();
    JScrollPane jsp = new JScrollPane();
    BorderLayout bl = new BorderLayout();
    JPanel southPanel = new JPanel();
    JCheckBox autoScroll = new JCheckBox("Scroll",true);
    JCheckBox autoSave = new JCheckBox("Save log to file",true);
    
    
    
    
    public JComponent getA(){
        return jta;
    }
    public JComponent getB(){
        return jsp;
    }
    public JComponent getC(){
        return southPanel;
    }
    
    
    public JOutput(){
        super();
        autoScroll.setHorizontalAlignment(SwingConstants.RIGHT);
        autoSave.setHorizontalAlignment(SwingConstants.RIGHT);
        this.setLayout(bl);
        jta = new JTextArea();
        System.out.println("HEREFONT1");
        Font originalFont = jta.getFont();
        jta.setFont(new Font(originalFont.getName(), originalFont.getStyle(), 10));
        System.out.println("HEREFONT2");
        jta.setRows(6);
        jta.setLineWrap(true);
        jta.setEditable(false);
        jsp.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jsp.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        jsp.getViewport().add(jta);     
        this.add(jsp,BorderLayout.CENTER);
        
        //autoScroll.setSize(autoScroll.WIDTH,Math.round(autoScroll.HEIGHT/2));
        Rectangle oldBounds = autoScroll.getBounds();
        //southPanel.setPreferredSize(new Dimension(500,20));
        BoxLayout bxl = new BoxLayout(southPanel,BoxLayout.X_AXIS);
        southPanel.add(autoScroll);
        southPanel.add(autoSave);
        this.add(southPanel,BorderLayout.SOUTH);
        
        
          System.out.println("HEREFONT3");
    }
    
    public String getText(){
        return jta.getText();
    }
    
    public void appendText(final String s){
        SwingUtilities.invokeLater(new Runnable(){
            public void run(){
                jta.append(s);
                if(autoScroll.isSelected()){
                    jsp.getVerticalScrollBar().setValue(jsp.getVerticalScrollBar().getMaximum());
                }
            }
        });
                
                
                
    }
    
}

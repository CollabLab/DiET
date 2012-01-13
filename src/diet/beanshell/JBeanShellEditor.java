/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package diet.beanshell;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import diet.parameters.ui.SavedExperimentsAndSettingsFile;
import diet.server.experimentmanager.FileTreeNode;
import diet.server.experimentmanager.FileTreeNodeAndRevisedDataProvider;
import diet.server.experimentmanager.JTemplateAndSetupRightTabbedPanel;

/**
 *This is a simple window allowing text to be displayed and edited. In this implementation it is primarily
 * associated with BeanShell scripting.
 * @author user
 */
public class JBeanShellEditor extends JPanel implements ActionListener, FileTreeNodeAndRevisedDataProvider, DocumentListener{

  
      
    
    
      BorderLayout bl = new BorderLayout();
      JScrollPane jScrollPane = new JScrollPane();
      JTextArea jta = new JTextArea();
      JCheckBox jcb = new JCheckBox("Wrap lines");
      FileTreeNode ftn=null;
      JTemplateAndSetupRightTabbedPanel jtasrtp;
      
      public JBeanShellEditor(FileTreeNode ftn){
          super();
          
          //this.jtasrtp=jtasrtp;
          this.ftn=ftn;
          File f = (File)ftn.getUserObject();
          String s = SavedExperimentsAndSettingsFile.readBSHFromFile(f);
          System.err.println("BSH IS: "+s);
           
          this.setLayout(bl);
                System.err.println("HERE0");
          jcb.setSelected(true);
                System.err.println("HERE1");
          jcb.setHorizontalAlignment(SwingConstants.LEFT);
                 System.err.println("HERE2");
          jcb.addActionListener(this);
                 System.err.println("HERE3");
          jta.setWrapStyleWord(true);
                 System.err.println("HERE4");
          jta.setLineWrap(true);
                 System.err.println("HERE5");
          jta.setText(s);
          jta.getDocument().addDocumentListener(this);
                System.err.println("HERE6");
         // if(s!=null)jta.setText(s);
                System.err.println("HERE7");
          jScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
                System.err.println("HERE8");
          jScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
               System.err.println("HERE9");
          jScrollPane.getViewport().add(jta);
               System.err.println("HERE10");
          this.add(jScrollPane,BorderLayout.CENTER);
               System.err.println("HERE11");
          this.add(jcb,BorderLayout.SOUTH);
               System.err.println("HERE12");
          
          this.setVisible(true);
          //System.exit(-235);
          
      }
      
    public void actionPerformed(ActionEvent e) {
          jta.setLineWrap(jcb.isSelected());
          jScrollPane.repaint();
    }

    public String getText(){
        return jta.getText();
    }
    
     public Object getData() {
        return jta.getText();
    }

    public FileTreeNode getTreeNode() {
        return ftn;
    }

    public String getSuffix() {
        String s = ftn.get_DIR_XML_BSH();
        return s;
    }

    
    public void addRecipientOfComponentChanges(JTemplateAndSetupRightTabbedPanel jtasrtp){
        this.jtasrtp = jtasrtp;
    }
    
     public void changedUpdate(DocumentEvent e) {
         if(jtasrtp!=null)jtasrtp.displayLabelInRed(this);
    }

    public void insertUpdate(DocumentEvent e) {
        jtasrtp.displayLabelInRed(this);
    }

    public void removeUpdate(DocumentEvent e) {
         jtasrtp.displayLabelInRed(this);
    }
    
    
    
    
}

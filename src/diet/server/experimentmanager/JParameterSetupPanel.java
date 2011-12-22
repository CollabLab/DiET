/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package diet.server.experimentmanager;

import java.awt.BorderLayout;
import java.awt.Font;
import java.io.File;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import diet.parameters.Parameter;

/**
 *
 * @author user
 */
public class JParameterSetupPanel extends JPanel implements ListSelectionListener, FileTreeNodeAndRevisedDataProvider{

    

    
    JParameterSetupTable jpst;
    JScrollPane jScrollPaneDescription;
    JTextArea   jDescription;
    JScrollPane jScrollPaneTable;
    BorderLayout bl = new BorderLayout();
    FileTreeNode ftn;
    JTemplateAndSetupRightTabbedPanel jtasrt;
    JPanel fillerPanel = new JPanel();
    
    public JParameterSetupPanel(FileTreeNode ftn){
        super();
        this.ftn = ftn;
        this.setLayout(bl);
        jpst=new JParameterSetupTable((File)ftn.getUserObject());
        
        
        jScrollPaneDescription = new javax.swing.JScrollPane();
        fillerPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Description"));
        jScrollPaneDescription.setBorder(javax.swing.BorderFactory.createTitledBorder("Description"));
        //jpst.setBorder(javax.swing.BorderFactory.createTitledBorder("Description"));
        jScrollPaneDescription.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        
        jDescription = new javax.swing.JTextArea();
        
        jDescription.setRows(5);
        jDescription.setLineWrap(true);
        jDescription.setWrapStyleWord(true);
        jDescription.setFont(new Font("Tahoma", 0, 12));
        
        jDescription.setBackground(this.getBackground());
        //jDescription.setText(jpst.getModel().getDescription());
        jScrollPaneDescription.getViewport().add(jDescription);
        this.add(jScrollPaneDescription,BorderLayout.NORTH);
        
        jScrollPaneTable = new javax.swing.JScrollPane();
        //jScrollPaneTable.setBorder(javax.swing.BorderFactory.createTitledBorder("Parameters"));
        jScrollPaneTable.getViewport().add(jpst);
        fillerPanel.setLayout(new BorderLayout());
        this.fillerPanel.add(jScrollPaneTable,BorderLayout.CENTER);
        
        this.add(fillerPanel,BorderLayout.CENTER);
        
        jpst.getSelectionModel().addListSelectionListener(this);
        
        setTextOfDescription(this.jpst.getModel().getDescription());
        
        Parameter p = jpst.getModel().getParameter(0);
        
        if(p!=null) {
            setTextOfDescription(p.getDescription());
        }
        jDescription.getDocument().addDocumentListener(jpst.getModel());
        this.validate();
        this.setVisible(true);
     

        
        jpst.addReferenceToEnclosingPanel(this);
        
    }
    
    
    
    public Object getData() {
        return this.jpst.jpstmp.expparameters;
    }

    public FileTreeNode getTreeNode() {
        return ftn;
    }
    public String getSuffix() {
        return "XML";
    }
    
    public void setTextOfDescription(final String s){
        SwingUtilities.invokeLater(new Runnable(){
            public void run(){
                jDescription.setText(s);
                jDescription.repaint();
            }
        });
    }

    
    public void addRecipientOfComponentChanges(JTemplateAndSetupRightTabbedPanel jtasrt){
        this.jtasrt = jtasrt;
    }
    public void informRecipientOfComponentChange(){
        if(jtasrt!=null){
          jtasrt.displayLabelInRed(this);
        }  
    }
    
    
    public void valueChanged(ListSelectionEvent e) {
        Parameter p = jpst.getModel().getParameter(jpst.getSelectedRow());
        System.out.println("----SELECTED "+e.getFirstIndex()+"----"+e.getLastIndex());
        if(p!=null) {
            setTextOfDescription(p.getDescription());
        }else{
            System.exit(-1);
        }
        
    }
    
    
    
    
    
}

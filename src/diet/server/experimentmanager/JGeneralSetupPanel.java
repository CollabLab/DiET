/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package diet.server.experimentmanager;

import java.awt.BorderLayout;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

/**
 *
 * @author user
 */
public class JGeneralSetupPanel extends JPanel implements FileTreeNodeAndRevisedDataProvider{

    
    
    
    FileTreeNode ftn;
    File f;
    BorderLayout bl = new BorderLayout();
    JTable jgsetup = new JTable();
    JScrollPane jscrollpanejgsetup = new JScrollPane();
    JGeneralSetupTableModel jsetupm;
    JTemplateAndSetupRightTabbedPanel jtasrtp;
    
    
    public JGeneralSetupPanel(FileTreeNode ftn){
        super();
        this.ftn=ftn;
        this.f = (File)ftn.getUserObject();
        jscrollpanejgsetup.setBorder(BorderFactory.createTitledBorder("General Setup"));
        this.jscrollpanejgsetup.getViewport().add(jgsetup);
        jsetupm = new JGeneralSetupTableModel(this,jgsetup,f);
        jgsetup.setModel(jsetupm);
        this.add(this.jscrollpanejgsetup);
        
        this.setVisible(true);
        this.validate();
   
    }
    
    public void addRecipientOfComponentChanges(JTemplateAndSetupRightTabbedPanel jtasrtp){
        this.jtasrtp=jtasrtp;
    }
    public void informRecipientOfComponentChange(){
        if(jtasrtp!=null){
          jtasrtp.displayLabelInRed(this);
        }  
    }
    
    public Object getData() {
        return this.jsetupm.parameters;
    }

    public FileTreeNode getTreeNode() {
        return ftn;
    }
    public String getSuffix() {
        return "xml";
    }

}

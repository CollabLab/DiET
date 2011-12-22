/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package diet.server.experimentmanager;
import java.awt.Color;
import java.awt.Component;
import java.io.File;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.wonderly.swing.tabs.CloseableTabbedPane;
import org.wonderly.swing.tabs.TabCloseEvent;
import org.wonderly.swing.tabs.TabCloseListener;

import diet.beanshell.JBeanShellEditor;
import diet.parameters.ui.SavedExperimentsAndSettingsFile;
/**
 *
 * @author user
 */
public class JTemplateAndSetupRightTabbedPanel extends CloseableTabbedPane {
    
    Hashtable ht = new Hashtable();
    Vector vChangedTreenodes = new Vector();
    
    public JTemplateAndSetupRightTabbedPanel(){
        super();
        
        addTabCloseListener(new TabCloseListener() {
            public void tabClosed(TabCloseEvent tce) {
                int i = tce.getClosedTab();
                
                Component c = getComponentAt(i);
                FileTreeNodeAndRevisedDataProvider ftnprovider = (FileTreeNodeAndRevisedDataProvider)c;
                FileTreeNode ftn = ftnprovider.getTreeNode();
                Object data = ftnprovider.getData();
                
                if(getForegroundAt(i).equals(Color.RED)){
                    JFrame jf = new JFrame();
                    jf.setVisible(false);
                    int n = JOptionPane.showConfirmDialog(jf,"Do you want to save "+ftn.getNameWithoutSuffix(),"Save file?",JOptionPane.YES_NO_CANCEL_OPTION);
                    //Generate popup window
                    if(n==0){
                        if(data instanceof Vector){
                            SavedExperimentsAndSettingsFile.saveParameterObjects((File)ftn.getUserObject(), (Vector)data);
                        }
                        else if(data instanceof Vector){
                            SavedExperimentsAndSettingsFile.writeBSHToFile((File)ftn.getUserObject(), (String)data);
                        }
                        ht.remove(ftn);
                        System.out.println("EXITED SAVING " + i);
                        removeTabAt(i);
                    }else if(n==1){
                        ht.remove(ftn);
                        System.out.println("EXITED WITHOUT SAVING " + i);
                        removeTabAt(i);
                    }                  
              }else{
                     ht.remove(ftn);
                     System.out.println("EXITED DID NOT NEED TO SAVE " + i);
                     removeTabAt(i);
              }
                   
                
                
            }
        });
       
        //setFont(new Font(oldFont.getName(),oldFont.getStyle(),50));
    }
    
    public void setTextToBlack(FileTreeNode ftn){
        try{
            JComponent jc = (JComponent)ht.get(ftn);
            int i = this.indexOfComponent(jc);
            this.setForegroundAt(i, Color.BLACK);
        }catch (Exception e){
            System.err.println("JTemplateAndSetupRightTabbedPanel could not find component");
        }
    }
    
    public void displayLabelInRed(JComponent jc){
        
       try{  
         int i = this.indexOfComponent(jc);
         
         this.setForegroundAt(i, Color.RED);
       }catch(Exception e){
           System.err.println("JTemplateAndSetupRightTabbedPanel could not find component");
       }  
    }
    
    public void displayComponentOfTreeNode(FileTreeNode ftn){
        JComponent jc = (JComponent)ht.get(ftn);       
        if(jc==null){
              createNewComponentForFileTreeNode(ftn);
        }
        else {
            
            super.setSelectedComponent(jc);
        }
    }
    
    public void createNewComponentForFileTreeNode(FileTreeNode ftn){
      try{
        
        JComponent created = null;
        String sType = ftn.get_DIR_XML_BSH();
        if(sType.equalsIgnoreCase("xml")){
            if(ftn.getNameWithoutSuffix().equalsIgnoreCase("General settings")){
                 JGeneralSetupPanel jgsp = new JGeneralSetupPanel(ftn);
                 jgsp.addRecipientOfComponentChanges(this);
                 ht.put(ftn, jgsp);
                 super.addTab(ftn.getNameWithoutSuffix(), jgsp);
                 super.setSelectedComponent(jgsp);
            }
            else{
                JParameterSetupPanel jpsp = new JParameterSetupPanel(ftn);
                jpsp.addRecipientOfComponentChanges(this);
                ht.put(ftn,jpsp);
                super.addTab(ftn.getNameWithoutSuffix(), jpsp);
                super.setSelectedComponent(jpsp);
            }
        }
        else if(sType.equalsIgnoreCase("bsh")){       
               JBeanShellEditor jbs = new JBeanShellEditor(ftn);
               jbs.addRecipientOfComponentChanges(this);
               ht.put(ftn,jbs);
               super.addTab(ftn.getNameWithoutSuffix(), jbs);
               super.setSelectedComponent(jbs);
        }
         else if(sType.equalsIgnoreCase("txt")){
               JBeanShellEditor jbs = new JBeanShellEditor(ftn);
               jbs.addRecipientOfComponentChanges(this);
               ht.put(ftn,jbs);
               super.addTab(ftn.getNameWithoutSuffix(), jbs);
               super.setSelectedComponent(jbs);
        }
         else if(sType.equalsIgnoreCase("dat")){
               String filename = ((File)ftn.getUserObject()).getName();
               if(filename.endsWith("turns.dat")||filename.endsWith("TURNS.DAT")||filename.endsWith("Turns.dat")){
                    JExperimentTurnsPanel jetp = new JExperimentTurnsPanel(ftn);
                    ht.put(ftn, jetp);
                    super.addTab(ftn.getNameWithoutSuffix(), jetp);
                    super.setSelectedComponent(jetp);
               }
               else if (filename.endsWith("messages.dat")||filename.endsWith("MESSAGES.DAT")||filename.endsWith("Messages.dat")){
                    
                    JExperimentMessagesPanel jemp = new JExperimentMessagesPanel(ftn);
                    ht.put(ftn, jemp);
                    super.addTab(ftn.getNameWithoutSuffix(), jemp);
                    super.setSelectedComponent(jemp);
               }
              
         }
        
    }catch(Exception e){
        System.out.println("There was an error opening: "+ftn.getNameWithoutSuffix());
        e.printStackTrace();
        
    }
    
    }

}

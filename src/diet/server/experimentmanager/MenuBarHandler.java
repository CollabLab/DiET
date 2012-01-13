/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package diet.server.experimentmanager;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Vector;

import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;

import diet.parameters.ui.SavedExperimentsAndSettingsFile;

/**
 *
 * @author user
 */
public class MenuBarHandler implements ActionListener{

    JExperimentManagerMainFrame jemmf;
    JTemplateAndSetupRightTabbedPanel  jtasrtp;
    JLeftTabbedPanel jltp;
    EMUI emui;
    
    public MenuBarHandler(JExperimentManagerMainFrame jemmf){
        this.jemmf = jemmf;
        this.jtasrtp=jemmf.getJtsrtp();
        this.emui=jemmf.getExpmanUI();
        this.jltp = jemmf.getJltp();
    }
    
    
    public void actionPerformed(ActionEvent e) {
        String s = e.getActionCommand();
        if(s.equalsIgnoreCase("save as")){
            FileTreeNodeAndRevisedDataProvider o = (FileTreeNodeAndRevisedDataProvider)jtasrtp.getSelectedComponent();
            if(o!=null){
                FileTreeNode ftn = (FileTreeNode)o.getTreeNode();
                File fparent =((File)ftn.getUserObject()).getParentFile();
                Object dat = o.getData();
                
                //      Vector data = (Vector)o.getData();
                      // final JFileChooser fc2 = new JFileChooser(fparent);
                      UIManager.put("FileChooser.readOnly", Boolean.TRUE);
                      final JFileChooserTreeNodeBrowser fc = new JFileChooserTreeNodeBrowser((FileTreeNode)ftn);
                
                      fc.showSaveDialog(this.jemmf);
                      fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
                      File f = null;
                      if(dat instanceof Vector){
                          f = fc.getSelectedFileCorrectedForXML();
                      }
                      else if(o.getSuffix().equalsIgnoreCase("BSH")){
                          f = fc.getSelectedFileCorrectedForBSH();
                      }
                      else if(o.getSuffix().equalsIgnoreCase("TXT")){
                          f = fc.getSelectedFileCorrectedForBSH();
                      }
                      
                if(f!=null){
                    boolean save = false;
                    if(f.exists()){
                        int n = JOptionPane.showConfirmDialog(jemmf,"File: "+f.getName()+" exists. Overwrite it?", "overwrite file?", JOptionPane.YES_NO_OPTION);
                        JOptionPane jops = new JOptionPane();
                        
                        
                        
                        if(n==0)save=true;
                    }else{
                        save=true;
                    }
                    if(save){
                        //jemmf.fs.getModel().reload();
                        //jemmf.fs.getModel()
                        boolean succ=false;
                        if(dat instanceof Vector){
                           succ = SavedExperimentsAndSettingsFile.writeAllParameterObjects(f, (Vector)dat);
                        }
                        else if(dat instanceof String){
                            succ = SavedExperimentsAndSettingsFile.writeBSHToFile(f,(String)dat);
                            
                        }
                        if(succ) {
                            
                            jemmf.displayTextOutputInBottomTextarea("Main", "Saving file "+f.getName()+" \n");
                            fc.getFileTreeNodeEnclosingDirectory().add(new FileTreeNode(f));
                            jemmf.fs.getModel().reload();
                            jemmf.fs.getModel().refreshTree(ftn);
                            jtasrtp.setTextToBlack(ftn);
                        }else{
                            jemmf.displayTextOutputInBottomTextarea("Main", "Error saving file "+f.getName()+" \n");
                        }
                    }
                    
                }
                
            }
        }
        else if(s.equalsIgnoreCase("Save")){
            FileTreeNodeAndRevisedDataProvider o = (FileTreeNodeAndRevisedDataProvider)jtasrtp.getSelectedComponent();
            FileTreeNode ftn = (FileTreeNode)o.getTreeNode();
            File fparent =((File)ftn.getUserObject()).getParentFile();
            Object dat = o.getData();
                
            
            File f = (File)ftn.getUserObject();
            
            
            if(f!=null){
                boolean succ=false;
                if(dat instanceof Vector){
                           succ = SavedExperimentsAndSettingsFile.writeAllParameterObjects(f, (Vector)dat);
                }
                else if(dat instanceof String){
                           succ = SavedExperimentsAndSettingsFile.writeBSHToFile(f,(String)dat);
                            
                }
                if(succ) {            
                          jemmf.displayTextOutputInBottomTextarea("Main", "Saving file "+f.getName()+" \n");
                          jemmf.fs.getModel().reload();
                          jemmf.fs.getModel().refreshTree(ftn);
                          jtasrtp.setTextToBlack(ftn);
                }
                else{
                            jemmf.displayTextOutputInBottomTextarea("Main", "Error saving file "+f.getName()+" \n");
                        }
                }
            
            
        }
        
        
        else if(s.equalsIgnoreCase("Parse trees")){
            try{
             System.out.println("HERE0");
            JComponent j = jltp.getSelectedTab();
             System.out.println("HERE1");
            TitledTabWithFontSettings tt = (TitledTabWithFontSettings)jltp.getSelectedTab();
             System.out.println("HERE2");
            JComponent jc2 = tt.getContentComponent();
             System.out.println("HERE3");
            Component[] jcarray = tt.getComponents();
             System.out.println("HERE4");
            System.err.println("THE COMPONENT COUNT IS: "+jcarray.length);
            
            
            System.err.println("THE TABCOUNT IS: "+jltp.getTabCount());
            if(j==null){
              //System.exit(-123456);
            }
            //if(emui==null)System.exit(-5432);
            emui.displayONOFFParseTreesOfConversationCorrespondingToTabbedPane(jltp.getSelectedTab().getContentComponent());
             
            }catch(Exception e2){
                System.err.println("ERROR DISPLAYING PARSE TREES: ");
            }
        }
        else if(s.equalsIgnoreCase("Dynamic text entry")){
            try{
              emui.displayONOFFDynamicTextDisplayOfConversationCorrespondingToTabbedPane(jltp.getSelectedTab().getContentComponent());
            }catch(Exception e2){}  
        }
        else if(s.equalsIgnoreCase("Toggle scrolling")){
            try{
              JComponent jc = (JComponent)jltp.getSelectedTab().getContentComponent();
              if(jc instanceof JTabbedPane){
                  emui.toggleScrolling((JTabbedPane)jc);
              }             
            }catch(Exception e2){}  
        }
  
        
    }
    
    
      public void buildMenu(){
        
        JMenuBar menuBar;
        JMenu menu, submenu;
       
        JMenuItem menuItem;        
       
        menuBar = new JMenuBar();
        //Build the first menu.
        menu = new JMenu("File"); 
        menuBar.add(menu);
        menuItem = new JMenuItem("Save"); menuItem.addActionListener(this);
        menu.add(menuItem);
        menuItem = new JMenuItem("Save as"); menuItem.addActionListener(this);  
        menu.add(menuItem);
        JSeparator js = new JSeparator();
        menu.add(js);
        menuItem = new JMenuItem("Exit");   menuItem.addActionListener(this);
        menu.add(menuItem);
       
        menuBar.add(menu);
        menu = new JMenu("View");
        menuBar.add(menu);
        menuItem = new JMenuItem("Parse trees"); menuItem.addActionListener(this);
        menu.add(menuItem);
        menuItem = new JMenuItem("Dynamic text entry"); menuItem.addActionListener(this);
        menu.add(menuItem);
        js = new JSeparator();
        menu.add(js);
        menuItem = new JMenuItem("Toggle scrolling"); menuItem.addActionListener(this);
        menu.add(menuItem);
        menu = new JMenu("Help");menu.addActionListener(this);
        menuBar.add(menu);
        jemmf.setJMenuBar(menuBar);      
    }
    

}

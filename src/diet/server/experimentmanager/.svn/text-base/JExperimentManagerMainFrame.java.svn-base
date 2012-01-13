/*
 * JExperimentManagerMainFrame.java
 *
 * Created on 07 January 2008, 12:37
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package diet.server.experimentmanager;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.File;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;


/**
 *
 * @author user
 */
public class JExperimentManagerMainFrame extends JFrame{
    
    /** Creates a new instance of JExperimentManagerMainFrame */
    
    JLeftTabbedPanel jltp;
    JTemplateAndSetupRightTabbedPanel jtsrtp = new JTemplateAndSetupRightTabbedPanel();

   
    JSplitPane jsplitpaneTree_Panels_LEFT_RIGHT;
    JSplitPane jsplitpaneDebugOutput_TOP_BOTTOM;
    //JSplitPane jsplitpaneRightLevel;
    JScrollPane leftScrollPane;
    JPanel rightTopPanel;
    JOutputPanels jos;
    FileSystemTree fs;
    EMUI expmanUI;
    
    JTabbedPane jtb = new JTabbedPane();
    
    MenuBarHandler mbh;
    
    public JExperimentManagerMainFrame(EMUI expmanUI,String s) {
       
        this.expmanUI=expmanUI;
        jltp = new JLeftTabbedPanel(expmanUI);
        fs = new FileSystemTree(expmanUI,s);
        fs.addTreeSelectionListener(new FileSystemTreeSelectionListener(this));
        fs.addMouseListener(new FileSystemTreeMouseListener(this,fs));
        fs.addMouseListener(new JFileSystemPopupMenu(fs));
                 
        leftScrollPane = new JScrollPane();
        rightTopPanel = new JPanel();
        rightTopPanel.setPreferredSize(new Dimension(500,500));
        
        leftScrollPane.getViewport().add(fs); 
        leftScrollPane.setPreferredSize(new Dimension(300,500));
        jsplitpaneTree_Panels_LEFT_RIGHT = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,leftScrollPane, jtsrtp); 
        jsplitpaneTree_Panels_LEFT_RIGHT.setOpaque(true);
        jsplitpaneTree_Panels_LEFT_RIGHT.setDividerLocation(0.3);
      
        
        this.jltp.displayPanelAddingIfNecessary("Setup & Templates",jsplitpaneTree_Panels_LEFT_RIGHT);
        
        jos= new JOutputPanels();
        
        jsplitpaneDebugOutput_TOP_BOTTOM =new JSplitPane(JSplitPane.VERTICAL_SPLIT,jltp, jos);
        
         
       
        
        
        
        this.setLayout(new BorderLayout());
        this.getContentPane().add(jsplitpaneDebugOutput_TOP_BOTTOM,BorderLayout.CENTER);
       
        mbh = new MenuBarHandler(this);
        mbh.buildMenu();
        
        
         this.pack();
        // System.exit(-1234510);
        this.validate();
        jsplitpaneDebugOutput_TOP_BOTTOM.setDividerLocation(jsplitpaneDebugOutput_TOP_BOTTOM.getHeight()-160);
        this.pack();
        this.validate();
        this.setVisible(true);
        
        
    
    }
    
 
    
    public void displayTextOutputInBottomTextarea(String stitle, String text){
        jos.outputTextCreatingPanelIfNecessary(stitle,text);
    }
    
    public void displayInTopRightPanel(FileTreeNode ftn){
        jtsrtp.displayComponentOfTreeNode(ftn);
    }
    
    
    public void setTopRightPanelDeprecated(File f){
        try{
         if(f.isDirectory()){
              //Do nothing, leave it as it is
         }   
         else if(f.getName().equalsIgnoreCase("General Settings.xml")){
          //   JGeneralSetupPanel jgsp = new JGeneralSetupPanel(f);
            // rightTopPanel.add(jgsp);
             //System.exit(-1);
             // this.remove(rightTopPanel);
             //this.rightTopPanel = jgsp;
             //this.add(rightTopPanel);        
             rightTopPanel.validate();
             rightTopPanel.repaint();
             
         }
         else if(f.getName().endsWith("xml")){
            // JParameterSetupPanel jpsp = new JParameterSetupPanel(f);
             this.remove(rightTopPanel);
             //this.rightTopPanel = jpsp;
             this.add(rightTopPanel);        
             rightTopPanel.validate();
             rightTopPanel.repaint();
         }     
        }catch (Exception e){
            System.out.println("Some error processing "+f.getName());
        }     
       
    }
    
    public void setTopRightPanelParameterFileDeprecated(File f){
       // JParameterSetupPanel jpsp = new JParameterSetupPanel(f);
       // this.rightTopPanel.add(jpsp);
        rightTopPanel.validate();
        rightTopPanel.repaint();
    }
    
    
    public void addConvIOtoLeftTabbedPane(String tabName,JTabbedPane jtp){
        this.jltp.displayPanelAddingIfNecessary(tabName,jtp);     
    }
    
    public void removeConvIOFromLeftTabbedPane(JComponent jc){
        this.jltp.removePanel(jc);
    }
    
     public EMUI getExpmanUI() {
        return expmanUI;
    }

    public JTemplateAndSetupRightTabbedPanel getJtsrtp() {
        return jtsrtp;
    }
    
   public JLeftTabbedPanel getJltp() {
        return jltp;
    }
    
    
    
}

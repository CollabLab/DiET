/*
 * FileSystemTreeSelectionListener.java
 *
 * Created on 16 January 2008, 21:05
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package diet.server.experimentmanager;
import java.io.File;

import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
/**
 *
 * @author user
 */
public class FileSystemTreeSelectionListener implements TreeSelectionListener {
       // DirectoryModel model;
       JExperimentManagerMainFrame mainFrame;

        public FileSystemTreeSelectionListener(JExperimentManagerMainFrame mainFrame ) {
           this.mainFrame=mainFrame;
        }
        public void valueChanged( TreeSelectionEvent e ) {
           System.out.println("DETECT THE SELECTION HERE");
           FileTreeNode ftn = (FileTreeNode)e.getPath().getLastPathComponent();
           File fileSysEntity = (File)  ((FileTreeNode)e.getPath().getLastPathComponent()).getUserObject();
           //this.mainFrame.setTopRightPanel(fileSysEntity);
        }   
          
  
}


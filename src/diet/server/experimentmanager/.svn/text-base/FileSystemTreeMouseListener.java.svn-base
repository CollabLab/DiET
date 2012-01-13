/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package diet.server.experimentmanager;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.tree.TreePath;

/**
 *
 * @author user
 */
public class FileSystemTreeMouseListener implements MouseListener{

    JExperimentManagerMainFrame mainFrame;
    FileSystemTree fst;
    
    public FileSystemTreeMouseListener(JExperimentManagerMainFrame mainFrame, FileSystemTree fst){
        this.mainFrame=mainFrame;
        this.fst=fst;
    }
    
    
    public void mouseClicked(MouseEvent e) {
        e.consume();
        if(e.getClickCount()==2){
           TreePath tp = fst.getPathForLocation(e.getX(), e.getY());
           if(tp!=null){
              FileTreeNode ftn = (FileTreeNode)tp.getLastPathComponent();
              TreePath dftnpath = new TreePath(ftn.getPath());
              fst.setSelectionPath(dftnpath);
              mainFrame.displayInTopRightPanel(ftn);
           }
        }
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void mousePressed(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
    }

}

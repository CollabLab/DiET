/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package diet.server.experimentmanager;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPopupMenu;
import javax.swing.tree.TreePath;

/**
 *
 * @author user
 */
public class JFileSystemPopupMenu extends JPopupMenu implements MouseListener, ActionListener{

   
    
    FileSystemTree ftree;
    
    public JFileSystemPopupMenu(FileSystemTree ftree){
        this.ftree = ftree;
    } 
            
    
    
    
     public void actionPerformed(ActionEvent e) {
        System.out.println(e.getSource().getClass().toString());
    }

    public void mouseClicked(MouseEvent e) {
        System.out.println("Not supported yet1.");
    }

    public void mouseEntered(MouseEvent e) {
        System.out.println("Not supported yet2.");
    }

    public void mouseExited(MouseEvent e) {
        System.out.println("Not supported yet3.");
    }

    public void mousePressed(MouseEvent e) {
        System.out.println("Not supported yet4.");
         if(e.isPopupTrigger()){
            maybeShowPopup(e);
        }
    }

    public void mouseReleased(MouseEvent e) {      
        if(e.isPopupTrigger()){
            maybeShowPopup(e);
        }
       
        
        
    }

    public void maybeShowPopup(MouseEvent e){
        TreePath selPath = ftree.getPathForLocation(e.getX(), e.getY()); 
            if(selPath==null)return;
            ftree.setSelectionPath(selPath);
            FileTreeNode ft = (FileTreeNode)selPath.getLastPathComponent();       
            JPopupMenu jp = ftree.getFileSystemModel().getMenuComponentsForRightClick(ft); 
            jp.setVisible(true);
            jp.show(ftree, e.getX(),e.getY());
    }
    
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package diet.server.experimentmanager;

import java.awt.Component;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

/**
 *
 * @author user
 */
public class FileSystemTreeRenderer extends DefaultTreeCellRenderer {

    FileSystemTree fst;
    FileSystemModelDefaultTreeModel fstm;
    
    public FileSystemTreeRenderer(FileSystemTree fst){
        this.fst=fst;
        this.fstm = fst.getFileSystemModel();
        
        
    }
    
    
    public Component getTreeCellRendererComponent(JTree tree,Object value,boolean sel,boolean expanded, boolean leaf,
                                              int row,
                                              boolean hasFocus){
        JLabel jl = (JLabel)super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
        Icon defaultIcon = jl.getIcon();
        FileTreeNode renderedComponent = (FileTreeNode)value;
        String topLevel = fstm.getTopLevelCategory(renderedComponent);
        
        //jl.setIcon(null);
        if(topLevel.equalsIgnoreCase("Saved experimental data")&&renderedComponent.getLevel()>=2){
            if(renderedComponent.isLeaf()){
                 jl.setIcon(null);
            }
            else{
                String experimentStatus = fstm.getStatusOfExperiments(renderedComponent);   
                ImageIcon leafIcon=null;
                if(experimentStatus.equalsIgnoreCase("INACTIVE")){
                     jl.setIcon(null);
                }
                else if(experimentStatus.equalsIgnoreCase("ACTIVEWAITING")){
                   leafIcon = new ImageIcon("selected.png");
                    jl.setIcon(leafIcon);                 
                }
                else {
                    leafIcon = new ImageIcon("selected.png");
                    jl.setIcon(leafIcon); 
                    //jl.setText("(1/2)  " +jl.getText());
               }
            
            }
        }    
        else if(renderedComponent.isLeaf()){
            jl.setIcon(null);
        }       
    
    
        
    return jl;
        
    }
    
    
    
}

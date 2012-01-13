/*
 * FileSystemTreeEditor.java
 *
 * Created on 12 January 2008, 20:42
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package diet.server.experimentmanager;
import java.awt.Component;
import java.awt.event.ActionEvent;

import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.tree.DefaultTreeCellEditor;
import javax.swing.tree.DefaultTreeCellRenderer;

/**
 *
 * @author user
 */
public class FileSystemTreeEditor extends DefaultTreeCellEditor{
    
    /**
     * Creates a new instance of FileSystemTreeEditor
     */
    
    DefaultTreeCellRenderer dtcr;
    
    public FileSystemTreeEditor(JTree t,FileSystemTreeRenderer fstr) {
       super(t,fstr);
    }
    
    
    public Component getTreeCellEditorComponent(JTree tree, Object value, boolean isSelected, boolean expanded, boolean leaf, int row){
        //System.exit(-10);
        
        String name = ((FileTreeNode)value).getNameWithoutSuffix();
                
        
        Component c = super.getTreeCellEditorComponent(tree, value, isSelected, expanded, leaf,row);
        DefaultTreeCellEditor.EditorContainer jtf = (EditorContainer)c;
        
        return c;
        //return new javax.swing.JTextField("TO BE EDITED2COMPONENT");
        //return new DefaultTreeCellRenderer();
    }
    
    
    public void actionPerformed(ActionEvent ae) {
      System.out.println("ACTIONPERFORMED");
   }
    
    public boolean stopCellEditing() {
            //String s = (String)getCellEditorValue();
    
            System.out.println("SEEING IF TO STOP ");//+s);
            return super.stopCellEditing();
        }
    
    public Object getCellEditorValue() {
            System.out.println("GETCELLEDITORVALUE");
            return super.getCellEditorValue();
            //return "EDITED VALUE";
    }
    
    public void valueChanged(TreeSelectionEvent te){
         System.out.println("valuechanged");
         super.valueChanged(te);
    }
    

    
}

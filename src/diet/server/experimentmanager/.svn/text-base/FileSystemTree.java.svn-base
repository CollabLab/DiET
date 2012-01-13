/*
 * FileSystemTree.java
 *
 * Created on 16 January 2008, 20:53
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package diet.server.experimentmanager;
import javax.swing.JTree;
import javax.swing.tree.TreePath;

/**
 *
 * @author user
 */
public class FileSystemTree extends JTree{
    
    /** Creates a new instance of FileSystemTree */
    FileSystemModelDefaultTreeModel model;

    
    
    
    public FileSystemTree(EMUI expmanUI,String s){
        super(new FileSystemModelDefaultTreeModel(expmanUI,s));
        model = (FileSystemModelDefaultTreeModel)super.getModel();
        model.setTree(this);
        System.out.println("INITING");
        init();
    }
    
    public void init(){
        setEditable(true);
        setLargeModel( true );        
        setRootVisible( false );
        setShowsRootHandles( true );
        putClientProperty( "JTree.lineStyle", "Angled" );    
        
        FileSystemTreeRenderer dtr = new FileSystemTreeRenderer(this);
        FileSystemTreeEditor dte = new FileSystemTreeEditor(this,dtr);
        this.setCellRenderer(dtr);
        this.setCellEditor(dte);
        
        
        
    }
    
     
    
  public FileSystemModelDefaultTreeModel getFileSystemModel(){
      return model;
  }  
    
  public String convertValueToText(Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
       // return ((FileTreeNode)value).getName();
       // System.out.println(value.getClass().toString());
      //System.err.println(value.toString());
      
      return ((FileTreeNode)value).getNameWithoutSuffix();
  }           
            
  
  public boolean editable = false;
  
  public boolean isPathEditable(TreePath path){
     return editable;
     //return false;
     //if(path.getPathCount()==2)return false;
     //return false;
     //FileTreeNode ftn = (FileTreeNode)path.getParentPath().getLastPathComponent();
     //if(ftn.getParent()==model.getRoot())return false;
     //return true;
 }
  
  public boolean hasBeenExpanded(TreePath path) {
    return true;
}
     public FileSystemModelDefaultTreeModel getModel() {
        return model;
    }
    
}

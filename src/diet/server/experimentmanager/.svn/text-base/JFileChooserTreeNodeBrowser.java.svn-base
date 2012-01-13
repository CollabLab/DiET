/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package diet.server.experimentmanager;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JFileChooser;


/**
 *
 * @author user
 */
public class JFileChooserTreeNodeBrowser extends JFileChooser implements ActionListener{

    public void actionPerformed(ActionEvent e) {
        System.err.println("ACTION PERFORMED +"+getCurrentDirectory());
        
    }

    public FileTreeNode originalftn;
    public FileTreeNode originalParentDir;
    
    public File prevDir;
    public FileTreeNode currentDirectoryFileTreeNode;
    
    public JFileChooserTreeNodeBrowser(FileTreeNode originalftn){
        super((File)((FileTreeNode)originalftn.getParent()).getUserObject());
        this.originalftn = originalftn;
        originalParentDir = ((FileTreeNode)originalftn.getParent());
        currentDirectoryFileTreeNode = originalParentDir;
        prevDir = (File)originalParentDir.getUserObject();
        
             
    }
    
    public boolean isTraversable(File f){
        System.err.println(" ----------------"+getCurrentDirectory());
        
        File fdircurrent = getCurrentDirectory();
        if(fdircurrent!=null){
            if(fdircurrent.equals(prevDir)){
                
            }else{
                if(prevDir.getParentFile()==null||prevDir.getParentFile().equals(fdircurrent)){
                    System.err.println("HAS GONE UP TOWARDS ROOT");
                    if(!currentDirectoryFileTreeNode.isRoot()){
                        currentDirectoryFileTreeNode=(FileTreeNode)currentDirectoryFileTreeNode.getParent(); 
                    }   
                    prevDir=fdircurrent;
                }
                else if(fdircurrent.getParentFile().equals(prevDir)){
                    System.err.println("HAS GONE DOWNWARDS");
                    if(currentDirectoryFileTreeNode.isRoot()){
                         File rootFile = (File)currentDirectoryFileTreeNode.getUserObject();
                         if(rootFile.equals(prevDir)){
                             currentDirectoryFileTreeNode = currentDirectoryFileTreeNode.getChildDirectoryWithName(fdircurrent);
                         }
                    }
                    else{
                      currentDirectoryFileTreeNode = currentDirectoryFileTreeNode.getChildDirectoryWithName(fdircurrent);
                    }
                    prevDir=fdircurrent;
                }
                else{
                    System.err.println("NO IDEA WHERE IN TREENODE");
                    System.exit(-123456);
                }
            }
                    
            
        }
        
        if(currentDirectoryFileTreeNode!=null){
            System.err.println(((File)currentDirectoryFileTreeNode.getUserObject()).getName());
        }else{
            System.err.println("NULL");
        }
        return super.isTraversable(f);
       
    }
    public FileTreeNode getFileTreeNodeEnclosingDirectory(){
        return this.currentDirectoryFileTreeNode;
    }

   
    public File getSelectedFileCorrectedForXML() {
        try{
          File f = super.getSelectedFile();
          String fname = f.getName();
          if(fname.endsWith(".XML")|| fname.endsWith(".Xml")||fname.endsWith(".xml")) return f;
            File f2 = new File(f.getAbsolutePath()+".xml");
          return f2;
        }catch (Exception e){
            return null;
        }  
        
    }
    
    public File getSelectedFileCorrectedForBSH() {
       try{ 
        File f = super.getSelectedFile();
        String fname = f.getName();
        if(fname.endsWith(".BSH")|| fname.endsWith(".Bsh")||fname.endsWith(".bsh")) return f;
         File f2 = new File(f.getAbsolutePath()+".bsh");
        return f2;
       }catch (Exception e){
           return null;
       } 
        
    }
   
    
}

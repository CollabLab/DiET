/*
 * FileSystemModelDefaultTreeModel.java
 *
 * Created on 16 January 2008, 16:02
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package diet.server.experimentmanager;
import java.io.File;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.JPopupMenu;
import javax.swing.JSeparator;
import javax.swing.SwingUtilities;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import diet.parameters.ExperimentSettings;
import diet.parameters.StringParameter;
import diet.parameters.ui.SavedExperimentsAndSettingsFile;
/**
 *
 * @author user
 */
public class FileSystemModelDefaultTreeModel extends DefaultTreeModel{
    
    /** Creates a new instance of FileSystemModelDefaultTreeModel */
   
    FileSystemTree fst;
    Hashtable statusOfExperiments = new Hashtable();
    EMUI expmanUI;
    
    
    public FileSystemModelDefaultTreeModel(EMUI expmanUI,FileTreeNode root) {
        super(root);
        this.root=root;
        sortTree((FileTreeNode)root);
        this.expmanUI=expmanUI;
    }
    
     public FileSystemModelDefaultTreeModel(EMUI expmanUI,String s) {
        super(new FileTreeNode(new File(s),true));
        this.root=(FileTreeNode)super.getRoot();
        sortTree((FileTreeNode)root);
        this.expmanUI=expmanUI;
    }
     
    public void setTree(FileSystemTree fst){
        this.fst = fst;
    }
    
    
    
    public void sortTree(FileTreeNode ftn){
        //Must be called after the tree is constructed and before the model is finished constructing
       Vector children = ftn.getAllChildren();
       if(getTopLevelCategory(ftn).equalsIgnoreCase("Saved experimental data")){
           if(ftn.getLevel()==1){
               Vector categories = new Vector();
               categories.addElement("scheduler.xml");
               categories.addElement("Scheduler.xml");
               categories.addElement("Scheduler.bsh");
               ftn.sortChildrenPrefixed(categories);          
           }
          
       }
       
       
       for(int i=0;i<children.size();i++){
           FileTreeNode child= (FileTreeNode)children.elementAt(i);
           sortTree(child);
       }
        
        
    }
    
    
    
    public void valueForPathChanged(TreePath path, Object newValue ) {
        
       
      //  FileTreeNode rootFTN=(FileTreeNode)root;
      //  rootFTN.removeAllChildren();
      //  this.setRoot(new FileTreeNode(new File("--")));
      //  rootFTN.removeAllChildren();
      //  this.nodeChanged(root);
        
       // System.exit(-23);
        
        
        SwingUtilities.updateComponentTreeUI(this.fst);
        
        
        FileTreeNode modifiednode = (FileTreeNode)path.getLastPathComponent();
       
       
        boolean succ = modifiednode.renameTo(newValue.toString());
       
        
        if(succ){
            //FileTreeNode newNode = new FileTreeNode(fnew);
            FileTreeNode modifiednodeParent = (FileTreeNode) modifiednode.getParent();
            //int indexOfOld =modifiednodeParent.getIndex(modifiednode);
            //modifiednode.removeFromParent();
            //modifiednodeParent.insert(newNode, indexOfOld);
            
            //modifiednode.setUserObject(fnew);
           
            
            final TreePath modifiedNodePath = path;
            final FileTreeNode modParentNode = (FileTreeNode)modifiednodeParent;
            final Object[] modParentPath = path.getParentPath().getPath();
            final int[] indices = new int[modParentNode.getAllChildren().size()];
            for(int i=0;i<modParentNode.getChildCount();i++){
               indices[i]=i;
             }
             final Object[] modifiedchildren = modParentNode.getAllChildren().toArray();
             SwingUtilities.invokeLater(new Runnable(){
            public void run(){
                fireTreeNodesChanged(modParentNode, modParentPath, indices, modifiedchildren);
                fst.setSelectionPath(modifiedNodePath);
                //fireTreeNodesInserted(root, path, childIndices, children);
                //fireTreeNodesRemoved(root, path, childIndices, children);
                //fireTreeStructureChanged(root, path, childIndices, children);
            } 
        });
            //modifiednode.setUserObject("REPLACETHINGS");
        }else{
            //System.exit(-45);
        }
        
        
       
        
        
       
       // final FileTreeNode modParentNode = (FileTreeNode)modifiednode.getParent();
       // final Object[] modParentPath = path.getParentPath().getPath();
       // final int[] indices ={ modParentNode.getIndex(modifiednode)};
       // final Object[] modifiedchildren = {modifiednode};
        
        
        
        FileTreeNode verify = (FileTreeNode)path.getLastPathComponent();
        File verifyf = (File)verify.getUserObject();
        System.out.println("VERIFY "+verifyf.getName());
        //this.reload();
        //fst.repaint();
        
    }

    
    public String getTopLevelCategory(FileTreeNode ftn){
       try{
        File f = (File)ftn.getUserObject();
        File rootFile = (File)((FileTreeNode)getRoot()).getUserObject();
        boolean foundTopLevelCategory = false;
        while(!foundTopLevelCategory){
           File f2 = f.getParentFile();
           //System.out.println("COMPARING "+f.getName()+" with "+f2.getName()+" root is: "+rootFile.getName());
           if(rootFile.equals(f2)){
               return f.getName();
           }
           f=f2;  
       }   
       }catch(Exception e){
           return "Error in implementation ";
       }
       return "";
    }
    
    
    public JPopupMenu getMenuComponentsForRightClick(FileTreeNode ftn){
          File rootFile = (File)((FileTreeNode)getRoot()).getUserObject();
          File rightClickedFile = (File)ftn.getUserObject();
          String topLevelCategory =this.getTopLevelCategory(ftn); 
          JPopupMenu jp = new JPopupMenu();
          
          
          
          if(rightClickedFile.getParentFile().equals(rootFile)){
             //IF RIGHT CLICK IS ON FIRST MENU 
             //File ftnf = (File)ftn.getUserObject();
             if(ftn.getNameWithoutSuffix().equalsIgnoreCase("Interventions")){
                MenuItem mi1 = new MenuItem("Create new Intervention",this, ftn);
                //JMenu submenu = new JMenu("Create new Template");
                
                MenuItem mi2 = new MenuItem("Create new Folder",this, ftn);
                mi1.setEnabled(false);
                mi2.setEnabled(false);
                jp.add(mi1);
                jp.add(mi2);
                return jp;
                //return jp;
             }
             else if(ftn.getNameWithoutSuffix().equalsIgnoreCase("Saved experimental data")){
                 MenuItem mi1 = new MenuItem("Refresh",this, ftn);
                 jp.add(mi1);
                 jp.validate();
                 return jp;
             }
             else{     
              // System.out.println(ftn.getParent().toString());                     
               MenuItem mi1 = new MenuItem("Not implemented yet!",this,ftn);
               jp.add(mi1);
               return jp;
             }
             
          }
          else if(topLevelCategory.equalsIgnoreCase("Interventions")){
                  if(isLeaf(ftn)){
                     MenuItem mi1 = new MenuItem("Run as new experiment",this,ftn);
                     JSeparator js1 = new JSeparator(); 
                     MenuItem mi2 = new MenuItem("Run locally as demo",this,ftn);
                     JSeparator js2 = new JSeparator();
                     MenuItem mi3 = new MenuItem("Add to existing experiment",this,ftn);
                     MenuItem mi4 = new MenuItem("Add to new experiment",this,ftn);
                     JSeparator js3 = new JSeparator();
                     MenuItem mi5 = new MenuItem("Copy",this, ftn);
                     MenuItem mi6 = new MenuItem("Rename",this,ftn);
                     MenuItem mi7 = new MenuItem("Delete",this,ftn); 
                     jp.add(mi1);
                     jp.add(js1);
                     jp.add(mi2);
                     jp.add(mi3);
                     jp.add(js2);
                     jp.add(mi4);
                     jp.add(mi5);
                     jp.add(mi6);
                     jp.add(mi7);
                     mi3.setEnabled(false);
                     mi4.setEnabled(false);
                     mi5.setEnabled(false);
                     mi6.setEnabled(true);
                     mi7.setEnabled(false);
                     
                     return jp;
                  }
                  else {
                      String a[] = rightClickedFile.list();
                      MenuItem mi1 = new MenuItem("Delete",this, ftn);
                      if(a.length==0){
                          mi1.setEnabled(false);
                      }
                      jp.add(mi1);
                      return jp;
                  }
          }
          else if(topLevelCategory.equalsIgnoreCase("Saved experimental data")){
              if(!ftn.isLeaf()){
                     String expStatus = (String)statusOfExperiments.get(ftn);
                     if(expStatus==null||expStatus.equalsIgnoreCase("INACTIVE")){
                         //MenuItem mi1 = new MenuItem("Restart experiment from initial state",this, ftn);                         
                         //MenuItem mi2 = new MenuItem("Restart as local demo from initial state",this, ftn);
                         //JSeparator js2 = new JSeparator();
                         MenuItem mi3 = new MenuItem("Attempt to resume experiment from last saved state",this, ftn);
                         MenuItem mi4 = new MenuItem("Attempt to resume local demo from last saved state",this,ftn);
                         JSeparator js3 = new JSeparator();
                         MenuItem mi5 = new MenuItem("Rename",this,ftn);
                         MenuItem mi6 = new MenuItem("Delete",this,ftn);
                         //jp.add(mi1);                   
                         //jp.add(mi2);
                         //jp.add(js2);
                         jp.add(mi3);
                         jp.add(mi4);
                         return jp;
                     }
                     else{
                         MenuItem mi1 = new MenuItem("Disable",this, ftn);
                         MenuItem mi2 = new MenuItem("Run locally",this, ftn);
                         JSeparator js = new JSeparator();
                         MenuItem mi3 = new MenuItem("Rename",this,ftn);
                         MenuItem mi4 = new MenuItem("Delete",this,ftn);
                         jp.add(mi1);
                         jp.add(mi2);
                         jp.add(js);
                         jp.add(mi3);
                         jp.add(mi4);
                         return jp;
                     }
                     
              }
              else{
                  MenuItem mi1 = new MenuItem("Save",this, ftn);
                  MenuItem mi2 = new MenuItem("Delete",this,ftn);
                  jp.add(mi1);
                  jp.add(mi2);
                  return jp;
              }
          }
          else{
               MenuItem mi1 = new MenuItem("Not implemented yet",this,ftn);
               jp.add(mi1);
               return jp;
          }           
    }
    
    
    /**
     * Called by "refresh" command from main menu of experimentmanager.
     * @param ftn
     */
    public void refreshTreeSubNodesExpanded(FileTreeNode ftn){
    final TreePath a = new TreePath(ftn.getPath());
         final FileTreeNode modParentNode = (FileTreeNode)ftn;
         final Object[] modParentPath = a.getParentPath().getPath();
         final int[] indices = new int[modParentNode.getAllChildren().size()];
         for(int i=0;i<modParentNode.getChildCount();i++){
               indices[i]=i;
         }
         final Object[] modifiedchildren = modParentNode.getAllChildren().toArray();
         SwingUtilities.invokeLater(new Runnable(){
            public void run(){
                //fireTreeNodesChanged(modParentNode, modParentPath, indices, modifiedchildren);
                fst.setSelectionPath(a);
                fireTreeNodesInserted(modParentNode, modParentPath, indices, modifiedchildren);
                //fireTreeNodesRemoved(root, path, childIndices, children);
                fireTreeStructureChanged(modParentNode, modParentPath, indices, modifiedchildren);
            } 
        });      
    }
    
    public void refreshTree(FileTreeNode ftn){
         final TreePath a = new TreePath(ftn.getPath());
         final FileTreeNode modParentNode = (FileTreeNode)ftn;
         final Object[] modParentPath = a.getParentPath().getPath();
         final int[] indices = new int[modParentNode.getAllChildren().size()];
         for(int i=0;i<modParentNode.getChildCount();i++){
               indices[i]=i;
         }
         final Object[] modifiedchildren = modParentNode.getAllChildren().toArray();
         SwingUtilities.invokeLater(new Runnable(){
            public void run(){
                fireTreeNodesChanged(modParentNode, modParentPath, indices, modifiedchildren);
                fst.setSelectionPath(a);
                //fireTreeNodesInserted(root, path, childIndices, children);
                //fireTreeNodesRemoved(root, path, childIndices, children);
                //fireTreeStructureChanged(root, path, childIndices, children);
            } 
        });      
    }
    public void selectFileTreeNode(FileTreeNode ftn){
            final TreePath a = new TreePath(ftn.getPath());
            SwingUtilities.invokeLater(new Runnable(){
            public void run(){       
                fst.setSelectionPath(a);
            } 
        });      
    
    }
    
    public void performActionFromMenu(String nameOfCommand,final FileTreeNode dftn){
         System.out.println("ACTIONPERFORMED "+nameOfCommand);
         if(nameOfCommand.equalsIgnoreCase("ACTIVATE")){
             this.statusOfExperiments.put(dftn, "ACTIVE");
             this.refreshTree(dftn);
             this.selectFileTreeNode(dftn);
         }
         else if(nameOfCommand.equalsIgnoreCase("DISABLE")){
             this.statusOfExperiments.put(dftn, "INACTIVE");
             this.refreshTree(dftn);
             this.selectFileTreeNode(dftn);
         }
         else if(nameOfCommand.equalsIgnoreCase("Rename")){
             TreePath dftnpath = new TreePath(dftn.getPath());
             fst.setSelectionPath(dftnpath);
             fst.editable=true;
             fst.startEditingAtPath(dftnpath);
             fst.editable = false;
         }
         else if(nameOfCommand.equalsIgnoreCase("Run locally as demo")){
             System.err.println("USEROBJECT IS: "+dftn.getUserObject().toString());
             //System.exit(-12345);
             this.expmanUI.runExperimentLocally(dftn);
         }
         else if(nameOfCommand.equalsIgnoreCase("Run as new experiment")){
             System.err.println("USEROBJECT IS: "+dftn.getUserObject().toString());
             //System.exit(-12345);
             this.expmanUI.runExperiment(dftn);
         }
         else if(nameOfCommand.equalsIgnoreCase("Run experiment as new")){
             System.err.println("USEROBJECT IS: "+dftn.getUserObject().toString());
             //System.exit(-12345);
             try{
                 File parentDir = (File)dftn.getUserObject();
                 File parameterFile = new File(parentDir,"expparameters.xml");
                 Vector v = SavedExperimentsAndSettingsFile.readParameterObjects(parameterFile);
                 ExperimentSettings expSett = new ExperimentSettings(v);
                 this.expmanUI.runExperiment(expSett);  
             }catch(Exception e){       
             }
         }
         else if(nameOfCommand.equalsIgnoreCase("Restart experiment from initial state")){
             System.err.println("USEROBJECT IS: "+dftn.getUserObject().toString());
             this.expmanUI.println("Main", "Attempting to restart experiment from initial state");
             //System.exit(-12345);
             try{
                 File parentDir = (File)dftn.getUserObject();
                 File parameterFile = new File(parentDir,"expparameters.xml");
                 Vector v = SavedExperimentsAndSettingsFile.readParameterObjects(parameterFile);
                 ExperimentSettings expSett = new ExperimentSettings(v);
                 this.expmanUI.runExperiment(expSett);  
             }catch(Exception e){       
             }
         }
         else if(nameOfCommand.equalsIgnoreCase("Restart as local demo from initial state")){
             System.err.println("USEROBJECT IS: "+dftn.getUserObject().toString());
             this.expmanUI.println("Main", "Attempting to restart as local demo from initial state");
             //System.exit(-12345);
             try{
                 File parentDir = (File)dftn.getUserObject();
                 File parameterFile = new File(parentDir,"expparameters.xml");
                 Vector v = SavedExperimentsAndSettingsFile.readParameterObjects(parameterFile);
                 ExperimentSettings expSett = new ExperimentSettings(v);
                 this.expmanUI.runExperimentLocally(expSett);  
             }catch(Exception e){       
             }
         }
         else if(nameOfCommand.equalsIgnoreCase("Attempt to resume experiment from last saved state")){
             System.err.println("USEROBJECT IS: "+dftn.getUserObject().toString());
             this.expmanUI.println("Main", "Attempting to resume experiment from last saved state");
             //System.exit(-12345);
             try{
                 File parentDir = (File)dftn.getUserObject();
                 File parameterFile = new File(parentDir,"expparameters.xml");
                 Vector v = SavedExperimentsAndSettingsFile.readParameterObjects(parameterFile);
                 ExperimentSettings expSett = new ExperimentSettings(v);
                 StringParameter p = new StringParameter("Recovery",parentDir.getCanonicalPath());
                 expSett.addParameter(p);
                 this.expmanUI.runExperiment(expSett);  
             }catch(Exception e){       
             }
         }
         else if(nameOfCommand.equalsIgnoreCase("Attempt to resume local demo from last saved state")){
             System.err.println("USEROBJECT IS: "+dftn.getUserObject().toString());
             this.expmanUI.println("Main", "Attempting to resume experiment locally as demo");
             //System.exit(-12345);
             try{
                 File parentDir = (File)dftn.getUserObject();
                 File parameterFile = new File(parentDir,"expparameters.xml");
                 Vector v = SavedExperimentsAndSettingsFile.readParameterObjects(parameterFile);
                 ExperimentSettings expSett = new ExperimentSettings(v);
                 StringParameter p = new StringParameter("Recovery",parentDir.getCanonicalPath());
                 expSett.addParameter(p);
                 this.expmanUI.runExperimentLocally(expSett);  
             }catch(Exception e){       
             }
         }
         else if(nameOfCommand.equalsIgnoreCase("Refresh")){
             try{
                 refreshTreeSubNodesExpanded(dftn);
                 
             }catch(Exception e){
                 
             }
         }
        
   
        // this.reload();
        // this.fst.repaint();
        
//         "ACTIVATE"
         
    }
    
    
    
    public String getStatusOfExperiments(FileTreeNode ftn){
        String experimentStatus = (String)statusOfExperiments.get(ftn);
        if(experimentStatus==null)return "INACTIVE";
        System.out.println("GETTING FTN "+experimentStatus);
        return experimentStatus;     
    }
    
    public void setStatusOfExperiment(FileTreeNode ftn,String status){
        statusOfExperiments.put(ftn, status);
        System.out.println("PUTTING FTN "+status);
        
    }
    
}
    


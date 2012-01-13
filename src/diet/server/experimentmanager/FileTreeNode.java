/*
 * FileTreeNode.java
 *
 * Created on 16 January 2008, 16:04
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package diet.server.experimentmanager;
import java.io.File;
import java.util.Vector;

import javax.swing.tree.DefaultMutableTreeNode;
/**
 *
 * @author user
 */
public class FileTreeNode extends  DefaultMutableTreeNode {
    
    /** Creates a new instance of FileTreeNode */
    //FileSystemModelDefaultTreeModel fsm;
     
    static final String turnbyturn = "TBT";
    static final String groupcomp = "GRP";
    static final String scheduler = "SCH";
    
    
    
    
    public FileTreeNode(File f,boolean isRoot){
        this(f);
        try{
          Vector v = (Vector)children.clone();
       
          this.removeAllChildren();
          System.out.println("CHILDRENSPROGSARE "+v.size());
          Vector v2 = this.sortTopVector(v);
          for(int i=0;i<v2.size();i++){
             FileTreeNode ftn = (FileTreeNode)v2.elementAt(i);
             System.out.println("SHOULD BE IN ORDER: "+((File)ftn.getUserObject()).getName());
             
             super.add(ftn);
             System.out.println("ADDING "+i);
           }   
         }catch (Exception e){
             System.err.println("ERROR creating FileTreeNode: "+f.getName());
             e.printStackTrace();
        }
    }
    
   
    
    
    public FileTreeNode(File f) {
        super(f); 
        String[] sprogNames = f.list();
        File[] sprogFileNames = f.listFiles();
        if(sprogNames==null)return;
        for(int i=0;i<sprogNames.length;i++){
            File fsprog = sprogFileNames[i];
            if(!fsprog.isHidden()){
              FileTreeNode sprog = new FileTreeNode(new File(f,sprogNames[i]));
              this.add(sprog);
            }  
        } 
    }   
       
    public FileTreeNode(File f,String s){
        super(new File(f,s));              
    }
    
    public Vector getAllChildren(){
        if(children==null)return new Vector();
        return children;
    }
    
   public Object getUserObject(){
       return super.getUserObject();
   }
    
    public void setUserObject(String o){
        super.setUserObject(o);
        //System.err.println("SETTING USER OBJECT------------"+o.getClass().toString()+"--------"+o.toString());
        //if(o instanceof String) System.exit(-1);
    }
   
    
    
    public boolean isDIR_XML_BSH(File f){
        if(f.isDirectory())return true;     
        String sNameFull = f.getName();
        if(sNameFull.endsWith(".xml")||sNameFull.endsWith(".Xml")||sNameFull.endsWith(".XML")){
            return true;   
        }
        if(sNameFull.endsWith(".bsh")||sNameFull.endsWith(".Bsh")||sNameFull.endsWith(".BSH")){
            return true;   
        }
        return false;
    }
    
    public String get_DIR_XML_BSH(){
         File f = (File)super.getUserObject();
         if(f.isDirectory())return "DIR"; 
          String sNameFull = f.getName();
        if(sNameFull.endsWith(".xml")||sNameFull.endsWith(".Xml")||sNameFull.endsWith(".XML")){
            return "xml";   
        }
        if(sNameFull.endsWith(".bsh")||sNameFull.endsWith(".Bsh")||sNameFull.endsWith(".BSH")){
            return "bsh";   
        }
        if(sNameFull.endsWith(".txt")||sNameFull.endsWith(".Txt")||sNameFull.endsWith(".TXT")){
            return "txt";   
        }
        if(sNameFull.endsWith(".dat")||sNameFull.endsWith(".Dat")||sNameFull.endsWith(".DAT")){
            return "dat";   
        }
        return "";         
    }
    
    public String get_TBT_GRP_SCH_DAT(){
        File f = (File)super.getUserObject();
        String sNameFull = f.getName();
        String sNameWithoutXMLorBSH;
        if(f.isDirectory())return "";
        if(sNameFull.endsWith(".xml")||sNameFull.endsWith(".Xml")||sNameFull.endsWith(".XML")){
            try{
              sNameWithoutXMLorBSH = sNameFull.substring(0,sNameFull.lastIndexOf("."));
              String type = sNameWithoutXMLorBSH.substring(sNameFull.lastIndexOf(".")+1,sNameWithoutXMLorBSH.length());
              return type;
            }catch(Exception e){
                return "";
            }  
        }
        else if(sNameFull.endsWith(".bsh")||sNameFull.endsWith(".Bsh")||sNameFull.endsWith(".BSH")){
            try{
               sNameWithoutXMLorBSH = sNameFull.substring(0,sNameFull.lastIndexOf("."));
               String type = sNameWithoutXMLorBSH.substring(sNameFull.lastIndexOf(".")+1,sNameWithoutXMLorBSH.length());
               return type;
            }catch (Exception e){
                return "";
            }   
        }
        else if(sNameFull.endsWith(".dat")||sNameFull.endsWith(".Dat")||sNameFull.endsWith(".DAT")){
            try{
               sNameWithoutXMLorBSH = sNameFull.substring(0,sNameFull.lastIndexOf("."));
               String type = sNameWithoutXMLorBSH.substring(sNameFull.lastIndexOf(".")+1,sNameWithoutXMLorBSH.length());
               return type;
            }catch (Exception e){
                return "";
            }   
        }
        else{
            return "Not implemented yet";
        }
    }
 
    
    
    public String getNameWithoutSuffix(){
        File f = (File)super.getUserObject();
        String sNameFull = f.getName();
        String sNameWithoutXMLorBSH;
        if(f.isDirectory())return f.getName();
        if(sNameFull.endsWith(".xml")||sNameFull.endsWith(".Xml")||sNameFull.endsWith(".XML")){
            sNameWithoutXMLorBSH = sNameFull.substring(0,sNameFull.lastIndexOf("."));
            try{
              String type = sNameWithoutXMLorBSH.substring(sNameFull.lastIndexOf(".")+1,sNameWithoutXMLorBSH.length());
              String sNameWithoutsuffix = sNameWithoutXMLorBSH.substring(0,sNameWithoutXMLorBSH.lastIndexOf("."));
              return sNameWithoutsuffix;
            }catch(Exception e){
              return sNameWithoutXMLorBSH;
            }
            
        }
        else if(sNameFull.endsWith(".bsh")||sNameFull.endsWith(".Bsh")||sNameFull.endsWith(".BSH")){
            sNameWithoutXMLorBSH = sNameFull.substring(0,sNameFull.lastIndexOf("."));
            try{
                String type = sNameWithoutXMLorBSH.substring(sNameFull.lastIndexOf(".")+1,sNameWithoutXMLorBSH.length());
                String sNameWithoutsuffix = sNameWithoutXMLorBSH.substring(0,sNameWithoutXMLorBSH.lastIndexOf("."));
                return sNameWithoutsuffix;
            }catch (Exception e){
                return sNameWithoutXMLorBSH;
            }    
        }
        else{
            return sNameFull;
        }    
    }
    
    public boolean renameTo(String s){
        
        String dirxmlbsh=get_DIR_XML_BSH();
        String tbtgrpsch = this.get_TBT_GRP_SCH_DAT();
        
        File f = (File)super.getUserObject();
        System.err.println("TO BE CHANGED OLDVALUEONDISK IS "+f.getName());
        System.err.println("TO BE CHANGED NEWVALUE IS "+s);
        File fnew;
        if(f.isDirectory()){
            fnew = new File(f.getParent(),s);
        }
        else{
            fnew = new File(f.getParent(),s+"."+tbtgrpsch+"."+dirxmlbsh);
        }
       
        
        System.err.println("FNEW IS "+fnew.getName());
        boolean succ = f.renameTo(fnew);
        System.err.println("F RENAMED IS "+f.getName());
        
        if(succ){
            
            FileTreeNode modifiednodeParent = (FileTreeNode) this.getParent();
            int indexOfOld = modifiednodeParent.getIndex(this);
            this.removeFromParent();
            //modifiednodeParent.add(newNode);
            modifiednodeParent.insert(this, indexOfOld);
            this.setUserObject(fnew);
            
            //modifiednode.setUserObject(fnew);
            
        }   
        return succ;
    }
    
    
    
    public boolean isLeaf() {
        //System.out.println("ISITLEAF");
        return ((File)super.getUserObject()).isFile();
        
    }
    
    
     public void sortChildrenPrefixed(Vector vPreFixes){
        if(children==null)return;
        Vector v = (Vector)children.clone(); 
        this.removeAllChildren();
        System.out.println("CHILDRENSPROGSARE "+v.size());
        Vector v2 = this.sortVector(vPreFixes,v);
        for(int i=0;i<v2.size();i++){
            FileTreeNode ftn = (FileTreeNode)v2.elementAt(i);
            System.out.println("SHOULD BE IN ORDER: "+((File)ftn.getUserObject()).getName());
            super.add(ftn);
            System.out.println("ADDING "+i);
        }  
        
     }
    
     static public Vector sortVector(Vector orderedStrings,Vector unsorted){
        Vector newVector = new Vector();
        for(int i=0;i<orderedStrings.size();i++){
            
             String s = (String)orderedStrings.elementAt(i);
             System.out.println("LOOKING FOR "+s);
             boolean foundString = false;
             for(int j=0;j<unsorted.size();j++){
                 FileTreeNode ftn = (FileTreeNode)unsorted.elementAt(j);
                 File f = (File)ftn.getUserObject();
                 if(f.getName().equalsIgnoreCase(s)){
                     System.out.println("FOUND: "+s);
                     foundString = true;
                     unsorted.remove(ftn);
                     
                     newVector.addElement(ftn);
                     break;
                 }
             }  
             if(!foundString){
                 System.out.println("UNABLE TO FIND "+s);
                 //System.exit(-42);
             }
         }
         newVector.addAll(unsorted);
         return newVector;
        
                 
         
         
     }
    
    
    
     private Vector sortTopVector(Vector oldVector){
         Vector newVector = new Vector();
         
         Vector stringsToLookFor = new Vector();
         stringsToLookFor.addElement("General settings.xml");
         stringsToLookFor.addElement("Interventions");
         stringsToLookFor.addElement("Group assignment");
         stringsToLookFor.addElement("Scheduler");
         stringsToLookFor.addElement("Saved experimental data");
         
         for(int i=0;i<stringsToLookFor.size();i++){
            
             String s = (String)stringsToLookFor.elementAt(i);
             System.out.println("LOOKING FOR "+s);
             boolean foundString = false;
             for(int j=0;j<oldVector.size();j++){
                 FileTreeNode ftn = (FileTreeNode)oldVector.elementAt(j);
                 File f = (File)ftn.getUserObject();
                 if(f.getName().equalsIgnoreCase(s)){
                     System.out.println("FOUND: "+s);
                     foundString = true;
                     oldVector.remove(ftn);
                     
                     newVector.addElement(ftn);
                     break;
                 }
             }  
             if(!foundString){
                 System.out.println("UNABLE TO FIND "+s);
                 System.exit(-42);
             }
         }
         newVector.addAll(oldVector);
         return newVector;
        
         
     }
    
     private String[] sortTopArrayDeprecated(String [] oldArray){
       //if(parent==null)return oldArray;
       System.out.println("SORTARRAY "+super.getPath().toString());
       if(parent!=null){
          
          if(parent!=null){
               FileTreeNode dfn = (FileTreeNode)this.getRoot();
               System.out.println(dfn.toString());
              System.out.println("PARENT:"+parent.toString());
              //System.exit(-5);
          }
          return oldArray;
       }
       
        
        System.exit(-23);
        String[] newArray = new String[oldArray.length];
        newArray[0]="Templates";
        newArray[1]="Tasks";
        newArray[2]="Participants";
        newArray[3]="Experiments";
        
         int indexOfNextFreeEntry = 4;
         for(int i=0;i<oldArray.length;i++){
             boolean found = false;
             for(int j=0;j<newArray.length;j++){
                 if(newArray[j].equals(oldArray[i]))found =true;
             }
             if(!found){
                  System.out.println(indexOfNextFreeEntry+" "+i);
                  newArray[indexOfNextFreeEntry]=oldArray[i];
                  indexOfNextFreeEntry++;
             }
         }
         return newArray;
  
    }
    public FileTreeNode getChildDirectoryWithName(File toLookFor){
        for(int i=0;i<children.size();i++){
            FileTreeNode ftn = (FileTreeNode)children.elementAt(i);
            File f = (File)ftn.getUserObject();
            if(toLookFor.equals(f)){
                return ftn;
            }
        }
        return null;
    }
    
}
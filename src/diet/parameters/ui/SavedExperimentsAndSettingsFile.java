/*
 * ExperimentSettingsFile.java
 *
 * Created on 17 January 2008, 13:02
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package diet.parameters.ui;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Writer;
import java.util.Vector;

import diet.parameters.Parameter;
import diet.parameters.ParameterFactory;
import diet.utils.FilenameToolkit;

/**
 *
 * @author user
 */
public class SavedExperimentsAndSettingsFile {
    
    
    public SavedExperimentsAndSettingsFile(File f){
        
    }
    
    
    //Create superclass that deals with everything as java files
    
    
    
    
    static public boolean saveParameterObjects (File currentFile, Vector vUpdatedParameters){
      
        
      System.out.println("SAVING JML TO "+currentFile.getAbsolutePath());
      File fBackupOfOldParameters;
      File ftempNewParameters;
        
      try{
        
       fBackupOfOldParameters = File.createTempFile("backupOLDParameters","tmp",new File(System.getProperty("user.dir")));  
       boolean backupSuccessful = FilenameToolkit.copy(currentFile,fBackupOfOldParameters);
       if(!backupSuccessful){
           fBackupOfOldParameters.delete();
           
           return false;
       }
       
       ftempNewParameters = File.createTempFile("newParams","tmp",new File(System.getProperty("user.dir")));
       boolean writeSucceeded = writeAllParameterObjects(ftempNewParameters,vUpdatedParameters);    
       if(writeSucceeded){
           boolean deleteSucceeded = currentFile.delete();
           if(deleteSucceeded){
                boolean renameSucceeded = ftempNewParameters.renameTo(currentFile);
                if(renameSucceeded){
                    fBackupOfOldParameters.delete();
                    return true;
                }
           }
       }
       ftempNewParameters.delete();
       currentFile.delete();
       fBackupOfOldParameters.renameTo(currentFile);
       System.err.println("Error (1) saving file: "+currentFile.getName());
       return false;
     
       
       
       }catch (Exception e){
              System.err.println("Error(2) saving file: "+currentFile.getName()+", "+ e.getMessage());
              System.exit(-123456);
              return false;
       }
    }
    
    
    
    static public boolean writeAllParameterObjects(File fNewFileName,Vector parameters){
        if(2<5){
            return saveVectorOfObjectsToNewTextFile(fNewFileName,parameters);
        }
        
        try{
            ObjectOutputStream oOut = new ObjectOutputStream(new FileOutputStream(fNewFileName));
            //XMLEncoder e = new XMLEncoder(new FileOutputStream(fNewFileName));
            for(int i=0;i<parameters.size();i++){
                Parameter p = (Parameter)parameters.elementAt(i);
                oOut.writeObject(p);
                oOut.flush();
            }
            
            oOut.close();
           
        }catch (Exception e){
            System.err.println("ERROR SAVING ALL PARAMETERS "+e.getMessage());
            System.exit(-452);
            return false;
        }
        return true;
    }
    
   
    
 static public Vector readParameterObjects(File f){
     Vector v = new Vector();
     
     try{
       ObjectInputStream oIn = new ObjectInputStream(new FileInputStream(f));  
       
       while(2<5){
           Object result = oIn.readObject();
           v.addElement(result);
           Parameter p = (Parameter)result;
           System.out.println("READING IN PARAMETER "+p.getID() + " with value "+p.getValue());
       }    
      }  catch(java.io.EOFException e){
          //System.out.println("ERROR "+e.toString());
          return v;
      }catch(Exception e2){
          //System.err.println("ERROR reading in file "+f.getName()+" "+e2.getMessage()); 
          //System.out.println("STACKTRACE");
          //e2.printStackTrace();
          if(2<5){
            return readVectorOfObjectsFromTextFile(f);
        }
    }
    return v;
 }
 
 

  static public String readBSHFromFile(File f){
        StringBuffer contents = new StringBuffer();

    //declared here only to make visible to finally clause
    BufferedReader input = null;
    try {
      //use buffering, reading one line at a time
      //FileReader always assumes default encoding is OK!
      input = new BufferedReader( new FileReader(f) );
      String line = null; //not declared within while loop
      /*
      * readLine is a bit quirky :
      * it returns the content of a line MINUS the newline.
      * it returns null only for the END of the stream.
      * it returns an empty String if two newlines appear in a row.
      */
      while (( line = input.readLine()) != null){
        contents.append(line);
        contents.append(System.getProperty("line.separator"));
      }
    }
    catch (FileNotFoundException ex) {
      ex.printStackTrace();
    }
    catch (IOException ex){
      ex.printStackTrace();
    }
    finally {
      try {
        if (input!= null) {
          //flush and close both "input" and its underlying FileReader
          input.close();
        }
      }
      catch (IOException ex) {
        ex.printStackTrace();
      }
    }
    
    System.err.println("CONTENTS ARE:"+contents);
    //System.exit(-234567);
    return contents.toString();
  }

  
    
    static public boolean writeBSHToFile(File f, String text){
      try{   
        
        if (f == null) {
           throw new IllegalArgumentException("File should not be null.");
        }
        if (!f.exists()) {
        //   throw new FileNotFoundException ("File does not exist: " + f);
        }
        if (f.isDirectory()) {
           throw new IllegalArgumentException("Should not be a directory: " + f);
        }
        if (!f.canWrite()) {
           //throw new IllegalArgumentException("File cannot be written: " + f);
        }

       //declared here only to make visible to finally clause; generic reference
       Writer output = null;
       try {
         //use buffering
         //FileWriter always assumes default encoding is OK!
           output = new BufferedWriter( new FileWriter(f) );
           output.write( text );
       
       }catch(Exception e){
           System.err.println(e.getMessage().toString());
           System.exit(-123412);
           return false;
       }    
       finally {
           //flush and close both "output" and its underlying FileWriter
           if (output != null) output.close();
       }
      }catch(Exception e){
          System.err.println(e.getMessage().toString());
           System.exit(-123524);
          return false;
      }    
      return true;
  }


    
    
    
   
    
    
    
   
    
 
 
 /**
  * Essentially the same method as readParameterObjects except with no implicit checking that the
  * contents of the vector are Parameter objects.
  * 
  * @param f File containing objects
  * @return Vector containing objects loaded from file
  */
 static public Vector readVectorOfObjectsFromFile(File f){
     Vector v = new Vector();
     
      try{
       ObjectInputStream oIn = new ObjectInputStream(new FileInputStream(f));  
       
       while(2<5){
           Object result = oIn.readObject();
           v.addElement(result);
       }    
      }  catch(java.io.EOFException e){
          //System.out.println("ERROR "+e.toString());
          return v;
      }catch(Exception e2){
          System.err.println("ERROR reading in file "+f.getName()+" "+e2.getMessage()); 
          System.out.println("STACKTRACE");
          e2.printStackTrace();
    }
    return v;
 }
     
 
  static public Vector readVectorOfStringsFromTextFile(File f){
      Vector ve = new Vector();
      try{
      BufferedReader bis = new BufferedReader(new FileReader(f));
      String line = null; //not declared within while loop
       while (( line = bis.readLine()) != null){
              ve.addElement(line);          
      }     
      }catch(Exception e){
          System.err.println("Could not load file "+f.getName());
      }
      
      return ve;
  }
 
 
  static public Vector readVectorOfObjectsFromTextFile(File f){
      Vector ve =new Vector();
      try{
          BufferedReader bis = new BufferedReader(new FileReader(f));
          String line = null; //not declared within while loop
          while (( line = bis.readLine()) != null){
              if(line.length()>4){
              Parameter p = ParameterFactory.getParameterFromString(line);
              ve.addElement(p);
              }
          
          }     
      }catch(Exception e){
          System.err.println("Could not load file "+f.getName());
      }
      
      return ve;
  }    
 
  static public boolean saveVectorOfObjectsToNewTextFile(File f,Vector v){
     try{ 
       BufferedWriter bw = new BufferedWriter(new FileWriter(f));
       for(int i=0;i<v.size();i++){
           Parameter p = (Parameter)v.elementAt(i);
           bw.write(ParameterFactory.generateString(p)+"\n");
       }
       bw.flush();
       bw.close();
     
     }catch(Exception e){
         System.err.println("COULD NOT SAVE PARAMETER");
         return false;
     }
      return true;
  }
 
}

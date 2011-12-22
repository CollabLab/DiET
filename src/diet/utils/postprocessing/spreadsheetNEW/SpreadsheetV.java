/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package diet.utils.postprocessing.spreadsheetNEW;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Vector;

/**
 *
 * @author user
 */
public class SpreadsheetV {
     
    public Vector headers = new Vector();
    public Vector columns = new Vector();
    public Hashtable columnsByName = new Hashtable();
             
    
     public SpreadsheetV(File f) {
        try {
          BufferedReader in = new BufferedReader(new FileReader(f));
          String str;
          str = in.readLine();
          String headernames[] = str.split("[|]");
          headers = new Vector(Arrays.asList(headernames));
          for(int i=0;i<headers.size();i++){
              String s = headernames[i];
              System.out.println(i+"HEADERNAMES-"+s);
              Vector v = new Vector();
              columns.addElement(v);
              columnsByName.put(s, v);
          }
          while ((str = in.readLine()) != null) {
              String[] rowString = str.split("[|]");
              for(int i=0;i<headernames.length;i++){
                  Vector vcol = (Vector)columns.elementAt(i);
                  String s = "";
                  if(i>=rowString.length){
                      vcol.addElement(s);   
                  }else{
                      s = rowString[i];
                      if(s==null)s="";
                      vcol.addElement(s);   
                  }
                  
              }
          }
        in.close();
    } catch (IOException e) {
        
    }
    }
    
    public String getValue(String columnName, int rowNumber){
        Vector v = (Vector)this.columnsByName.get(columnName);
        return (String)v.elementAt(rowNumber);
    } 
     
    public int getRowCount(){
        int size = -5;
        for(int i =0;i<columns.size();i++){
            Vector v = (Vector)columns.elementAt(i);
            if(i==0){
                size=v.size();
            }
            else{
                if(size!=v.size()){
                    System.err.println("ERROR WITH SIZES: "+v.size()+" is different from "+size);
                    //System.exit(-5);
                }
            }
        }
        return size;
    }
    
    public boolean isRowNumberCR(int rowNumber){
        Vector vtxt = (Vector)this.columnsByName.get("Txt");
        Vector v = (Vector)this.columnsByName.get("IntVChat");
        String s = (String)v.elementAt(rowNumber);
        if(s.equalsIgnoreCase("intervention")){
               String txttyped = (String)vtxt.elementAt(rowNumber);
               if(txttyped.indexOf("?")>-1){
                   return true;
               }
        }
        return false;
    }
    
    public boolean isRowNumberACK(int rowNumber){
        Vector vtxt = (Vector)this.columnsByName.get("Txt");
        Vector v = (Vector)this.columnsByName.get("IntVChat");
        String s = (String)v.elementAt(rowNumber);
        if(s.equalsIgnoreCase("intervention")){
               String txttyped = (String)vtxt.elementAt(rowNumber);
               if(txttyped.indexOf("?")<0){
                   return true;
               }
        }
        return false;
    }
    
    public Vector getAllIndicesOfInterventionCRs(){
        Vector indices = new Vector();
        Vector vtxt = (Vector)this.columnsByName.get("Txt");
        Vector v = (Vector)this.columnsByName.get("IntVChat");
        for(int i=0;i<v.size();i++){
            String s = (String)v.elementAt(i);
            if(s.equalsIgnoreCase("intervention")){
               String txttyped = (String)vtxt.elementAt(i);
               if(txttyped.indexOf("?")>-1){
                   indices.addElement(i);
               }
            }
        }
        return indices;
    }
    
    public String getTSTOfCR(int rowNumberOfCR){
        Vector v = (Vector)this.columnsByName.get("Subject");
        Vector vtxt = (Vector)this.columnsByName.get("Txt");
        String nameOfRecipient = (String)v.elementAt(rowNumberOfCR);
        String crTurn = (String)vtxt.elementAt(rowNumberOfCR);
        for(int i=rowNumberOfCR-1;i>=0;i--){
            String name = (String)v.elementAt(i);
            if(name.equalsIgnoreCase(nameOfRecipient)){
                String s = (String)vtxt.elementAt(i);
                //System.out.println(s+" "+crTurn);
                return s;
            }
        }
        return "NOTFOUNDSHOULDNOTHAPPEN1";
    }
    
    public String getCRString(int rowNumber){
        Vector vtxt = (Vector)this.columnsByName.get("Txt");
        String s = (String)vtxt.elementAt(rowNumber);
        return extractCRStringFromString(s);
    }
    
    public String extractCRStringFromString(String s){
        String s2 = s.substring(s.indexOf(":")+2,s.indexOf("?")-1);
        String s3 = s2.trim();
        return s3;  
    }
    
    public String extractAckStringFromString(String s){
        String s2 = s.substring(s.indexOf(":")+2, s.length());
        return s2;
    }
    
    public String getNextTurnBySameSpeaker(int rowNumber){
        Vector v = (Vector)this.columnsByName.get("Subject");
        Vector vtxt = (Vector)this.columnsByName.get("Txt");
        String nameOfRecipient = (String)v.elementAt(rowNumber);
        for(int i=rowNumber+1;i<=vtxt.size();i++){
             String name = (String)v.elementAt(i);
             if(name.equalsIgnoreCase(nameOfRecipient)){
                String s = (String)vtxt.elementAt(i);
                return s;
            }
        }
        return "NOTFOUNDSHOULDNOTHAPPEN2";
    }
        
    
}

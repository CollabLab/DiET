/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package diet.utils.postprocessing.spreadsheetNEW;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Vector;

import diet.utils.treekernel.ParserWrap;
/**
 *
 * @author user
 */
public class Spreadsheet {
    
    ParserWrap pw = new ParserWrap();
    
    String[] header;
    String[][] spreadsheet;
    /** Creates a new instance of Spreadsheet */
    public Spreadsheet(File f) {
        try {
        BufferedReader in = new BufferedReader(new FileReader(f));
        String str;
        str = in.readLine();
        header = str.split("[|]");
        System.out.println("HEADER0 "+header[0]);
        Vector allRows = new Vector();
        while ((str = in.readLine()) != null) {
            String[] rowString = str.split("[|]");
            for(int i=rowString.length;i<header.length;i++){
                rowString = addEmptyCells(rowString,1);
            }
            allRows.addElement(rowString);
            
        }
        
        spreadsheet = new String[allRows.size()][header.length+5];
        System.out.println("SPREADSHEET IS "+allRows.size());
        for(int i=0;i<allRows.size();i++){
            spreadsheet[i]=(String[])allRows.elementAt(i);
            System.out.println(spreadsheet[i][0]);
        }
        
        
        in.close();
        
        
    } catch (IOException e) {
        
    }
    }
   
   public String extractCRStringFromTurn(String s){
        String s2 = s.substring(s.indexOf(":")+2,s.indexOf("?")-1);
        String s3 = s2.trim();
        return s3;  
   } 
    
    
   public int getIndexOfColumnName(String s){
       for(int i=0;i<header.length;i++){
           String s2 = header[i];
           if(s2.equalsIgnoreCase(s)){
               return i;
           }
       }
       System.err.println("CAN't FIND COLUMN NAME "+s);
       System.exit(-1);
       return -1;
   }
    
   public void addEmptyColumn(String colHeader){
       header = addEmptyCells(header,1);
       header[header.length-1]=colHeader;  
       this.addOneEmptyColumnToSpreadsheetData();
       for(int i=0;i<spreadsheet.length;i++){
           //System.out.println(spreadsheet[i][header.length-1]+": "+i+" "+(header.length-1));
           spreadsheet[i][header.length-1]="";
       }       
   } 
    
   
   public int findColumnNumber(String headerName){
       for(int i=0;i<header.length;i++){
           String s = header[i];
           if(s.equals(headerName))return i;
       }
       return -1;
   }
   
   
   
   
   
   public void addOneEmptyColumnToSpreadsheetData(){
       for(int i=0;i<spreadsheet.length;i++){
           String [] oldArray = spreadsheet[i];
           String [] newArray = this.addEmptyCells(oldArray, 1);
           spreadsheet[i]=newArray;
           
       }
   }
    
   private  String[] addEmptyCells(String[] oldArray, int number){
       String[] newArray = new String[oldArray.length+number];
       for(int i=0;i<newArray.length;i++){
           if(i<oldArray.length){
             newArray [i] = oldArray[i];
           }
           else{
              newArray[i]="";
           }
       }
       return newArray;
   }
    
   public void writeSpreadsheetToFile(File f){
       try{
       //BufferedWriter bfw = new BufferedWriter(new FileWriter(f));
       
       PrintWriter pw = new PrintWriter(f);
       
       pw.println(createStringFromRow(header));
       
       for(int i=0;i<spreadsheet.length;i++){
           if(spreadsheet[i]!=null){
             
             pw.println(createStringFromRow(spreadsheet[i]));
             //System.out.println(i);
           }  
           else{
               System.out.println("NULL: "+i);
           }
       }
       pw.flush();
       pw.close();
       }catch (IOException e){
           System.err.println("ERROR writing spreadsheet to "+f.getName());
       }
   }
   
   private String createStringFromRow(Object[] o){
       String row="";
       for(int i=0;i<o.length;i++){
           row = row + (String)o[i]+"|";  
       }  
       //System.out.println(row);
       return row;
   }
    
  
}

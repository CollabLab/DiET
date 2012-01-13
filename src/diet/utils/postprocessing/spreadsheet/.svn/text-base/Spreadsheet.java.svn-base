/*
 * Spreadsheet.java
 *
 * Created on 19 January 2008, 18:03
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package diet.utils.postprocessing.spreadsheet;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Vector;

import diet.utils.stringsimilarity.StringSimilarityMeasure;
import diet.utils.treekernel.ParserWrap;
import diet.utils.treekernel.TreeKernel;
import edu.stanford.nlp.trees.Tree;
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
    
   public void addInterventionTXTColumn(){
      int intvchatIDX = getIndexOfColumnName("intvchat");
      int txtIDX = getIndexOfColumnName("Txt");
      
      
      this.addEmptyColumn("CRTEXT");
      
      for(int i=0;i<spreadsheet.length;i++){
          String  s = spreadsheet[i][intvchatIDX];
          if(s.equalsIgnoreCase("INTERVENTION")){
              String crtextTemp = spreadsheet[i][txtIDX];
              if(crtextTemp.indexOf("?")>0){
                   System.out.println(this.extractCRStringFromTurn(crtextTemp)+"----");
                   spreadsheet[i][getIndexOfColumnName("CRTEXT")]=this.extractCRStringFromTurn(crtextTemp);
              }   
          }         
      }    
   } 
   
   
    public void addTSTColumn(){
         this.addEmptyColumn("TST");
       int fulltextIDX= getIndexOfColumnName("RFULLTEXT");
       int contigturnIDX = getIndexOfColumnName("contigturns");
       int intvchatIDX = getIndexOfColumnName("intvchat");
       int txtIDX = getIndexOfColumnName("Txt");
       int dyadIDX = getIndexOfColumnName("DyadNo");
       int dyadName = getIndexOfColumnName("Subject");
       
       for(int i=0;i<spreadsheet.length;i++){
          String  s = spreadsheet[i][intvchatIDX];
          if(s.equalsIgnoreCase("INTERVENTION")){
              String crtextTemp = spreadsheet[i][txtIDX];
              if(crtextTemp.indexOf("?")>0){
                  boolean foundLastOfContigTST =false;
                  String tst="";
;                  for(int j=i-1;j>=0;j--){
                      if(!spreadsheet[j][dyadIDX].equalsIgnoreCase(spreadsheet[i][dyadIDX])){
                          System.out.println("("+i+")"+"("+j+") Stopping reached beginning of file");
                          break;
                      }
                      else if(spreadsheet[j][dyadName].equalsIgnoreCase(spreadsheet[i][dyadName])){
                          foundLastOfContigTST=true;
                          tst=(spreadsheet[j][txtIDX]+" "+tst).trim();
                          System.out.println("TST IS "+tst);
                      }
                      else if(foundLastOfContigTST){
                          break;
                      }
                      else if((i-j)>5);//System.exit(-5);
                      
                  }
                  if(foundLastOfContigTST){
                       spreadsheet[i][header.length-1]=tst;
                       
                  }
              }
          }
       }  
    }
   
   public void addTSTColumnDeprecated(){
       this.addEmptyColumn("TST");
       int fulltextIDX= getIndexOfColumnName("RFULLTEXT");
       int contigturnIDX = getIndexOfColumnName("contigturns");
       int intvchatIDX = getIndexOfColumnName("intvchat");
       int txtIDX = getIndexOfColumnName("Txt");
       int dyadIDX = getIndexOfColumnName("DyadNo");
       int dyadName = getIndexOfColumnName("Subject");
       
       for(int i=0;i<spreadsheet.length;i++){
          String  s = spreadsheet[i][intvchatIDX];
          if(s.equalsIgnoreCase("INTERVENTION")){
              String crtextTemp = spreadsheet[i][txtIDX];
              if(crtextTemp.indexOf("?")>0){
                  
                  
                  for(int j=i-1;j>=0;j--){
                      System.out.println("looking at:");
                      if(!spreadsheet[j][dyadIDX].equalsIgnoreCase(spreadsheet[i][dyadIDX])){
                          System.out.println("("+i+")"+"("+j+") Stopping reached beginning of file");
                          break;
                      }
                      else if(spreadsheet[j][dyadName].equalsIgnoreCase(spreadsheet[i][dyadName])){
                          
                          if(spreadsheet[j][contigturnIDX].length()>0){
                          
                             System.out.print("("+i+")"+"("+j+") FOUND FIRST ");
                             String fulltext = spreadsheet[j][contigturnIDX];
                             System.out.println(" "+fulltext);
                             spreadsheet[i][header.length-1]=fulltext;
                             
                             break;
                          }
                      }
                      else{
                          System.out.println();
                      }
        
                  }        
              }
          }
       }    
   } 
   
  
   public void addInterventionLexicalMatchingScores(){
     this.addEmptyColumn("resplexintv");     
     this.addEmptyColumn("RespLexresp");    
     int resplexIntv = this.getIndexOfColumnName("resplexintv");
     int resplexResp = this.getIndexOfColumnName("RespLexresp");
     int tstIDX = getIndexOfColumnName("TST");
     int fulltextIDX= getIndexOfColumnName("RFULLTEXT");
     int intvchatIDX = getIndexOfColumnName("intvchat");
     int txtIDX = getIndexOfColumnName("Txt");

         
     for(int i=0;i<spreadsheet.length;i++){
          String  s = spreadsheet[i][intvchatIDX];
          if(s.equalsIgnoreCase("INTERVENTION")){
             String crtextTemp = spreadsheet[i][txtIDX];
             if(crtextTemp.indexOf("?")>0){
                 String tst =  spreadsheet[i][tstIDX] ;
                 String resp = spreadsheet[i][fulltextIDX];
                 float[] similarity = StringSimilarityMeasure.getProportionOfWordsOfAInBandBinA(tst, resp);
                 //spreadsheet[i][resplexIntv]=""+similarity[0];
                 //spreadsheet[i][resplexResp]=""+similarity[1];
                 spreadsheet[i][header.length-1]="|"+this.compareTwoStringsTree(tst,resp)+"|"+this.compareTwoStringsTree(resp, tst);
                 
             }
          }            
     }             
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
   
   public void addNewColumnOfContiguousTurns(){ 
       this.addEmptyColumn("Contigturns");
      
       for(int i =0;i<spreadsheet.length;i++){
           String contigTurn = spreadsheet[i][19];
           System.out.println(contigTurn);
           boolean foundNextTurn = false;
           int j = i+1;
           
           while(!foundNextTurn){
               if(j>spreadsheet.length-1){
                   //spreadsheet[i][header.length-1]="FOUND END OF SPREADSHEET ";
                   foundNextTurn=true;
               }
               else if( !(spreadsheet[j][2].equalsIgnoreCase(spreadsheet[i][2]))){
                   //spreadsheet[i][header.length-1]="IS NOT SAME DYAD: "+spreadsheet[i][2]+" AND "+spreadsheet[j][2]+".";
                   foundNextTurn = true;
      
               }
               else if(spreadsheet[j][0].equals(spreadsheet[i][0])){
                   
                   if(spreadsheet[j][16].equals("CHATTEXT")){
                       contigTurn = contigTurn + ". "+ spreadsheet[j][19];
                       //spreadsheet[i][header.length-1]="SAME AS NEXT";
                   }
               }   
               else if(spreadsheet[j][16].equals("CHATTEXT")){
                       //spreadsheet[i][header.length-1]=i+" "+j+contigTurn;
                       foundNextTurn=true;
               }
               System.out.println(i+" "+j);
               if(foundNextTurn){
                   spreadsheet[i][header.length-1]=contigTurn+".";
                   i=j-1;
                   break;
               }
               j++;
           }
               
               
           }    
   }
   
   public void testThatEachRowIsOfADifferentParticipant(){
       String prevParticipant = "ADUMMYNAMEOFPARTICIPANT";
       String prevTurn = "DUMMYFORBEGINNING";
       for(int i =0;i<spreadsheet.length;i++){
           String contigTurn = spreadsheet[i][header.length-1];
           String participantname = spreadsheet[i][0];
           if(contigTurn!=null&!contigTurn.equals("")){ 
               if(participantname.equals(prevParticipant)){
                   System.out.println("----"+participantname+"-"+prevParticipant+     "IS THE SAME AS PREVIOUS AT LINE "+i+prevTurn+" AND "+contigTurn);
                   System.exit(-1);
               }
               else{
                   System.out.println("COMPARING "+i+prevTurn+" AND "+contigTurn);
                   prevParticipant = participantname;
                   prevTurn = ""+contigTurn;
               }
           }
           
       }    
   }
   
   
   public String compareTwoStringsLex(String s1, String s2){
       float[] similarity = StringSimilarityMeasure.getProportionOfWordsOfAInBandBinA(s1, s2);
       return "|"+similarity[0]+"|"+similarity[1];
   }
   
   
   public String compareTwoStrings(String s1, String s2){
        return compareTwoStringsLex(s1,s2);
   }
   public float compareTwoStringsTree(String s1,String s2){
       Tree t1 = pw.parse(s1);
       Tree t2 = pw.parse(s2);
       float f=0;
       try{
    	  TreeKernel.setIncludeWords(false);
    	  TreeKernel.setKernelNormalisation(false);
          f = (float) TreeKernel.resetAndCompute(t1, t2, TreeKernel.SUB_TREES);
       }catch (Exception e){
          f=0;
       }   
       System.out.println(" RETURNING: "+f);
       return f;
   }
   
   public void compareWholeSpreadsheet(){
       addEmptyColumn("MATCHING");
       for(int i =0;i<spreadsheet.length;i++){
           String turn1Text = spreadsheet[i][header.length-2];
           String turn1ID = spreadsheet[i][2];
           boolean foundNextTurn = false;
           int j=i+1;
           while(!foundNextTurn){
               if(j>spreadsheet.length-1){
                   i=j-1;
                   break;
               }
               else if( !(spreadsheet[j][2].equalsIgnoreCase(spreadsheet[i][2]))){
                   i=j-1;
                   break;
               }
               String turn2Text = spreadsheet[j][header.length-2];
               if(turn2Text!=null&&!turn2Text.equals("")){
                   foundNextTurn = true;
                   System.out.print("("+i+") "+turn1Text+"-"+turn2Text+i+" ");
                   spreadsheet[i][header.length-1]=compareTwoStrings(turn1Text, turn2Text);
                   i=j-1;
                   break;
               }
               j++;
           }
           
       }    
           
       
   }
   
   public void addOneEmptyColumnToSpreadsheetData(){
       for(int i=0;i<spreadsheet.length;i++){
           String [] oldArray = spreadsheet[i];
           String [] newArray = this.addEmptyCells(oldArray, 1);
           spreadsheet[i]=newArray;
           
       }
   }
   
   public String[][] addEmptyColumns(String[][] oldArray, int numbr){
       for(int i=0;i<oldArray.length;i++){
           oldArray[i]=addEmptyCells(oldArray[i],numbr);
       } 
       return oldArray;
   } 
    
   public  String[] addEmptyCells(String[] oldArray, int number){
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
    
   public void addColumn(){
       //int index = header
   } 
}

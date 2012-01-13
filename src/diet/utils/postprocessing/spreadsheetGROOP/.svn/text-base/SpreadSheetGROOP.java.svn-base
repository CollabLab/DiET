/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package diet.utils.postprocessing.spreadsheetGROOP;

import diet.utils.stringsimilarity.StringSimilarityMeasure;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Vector;

/**
 *
 * @author sre
 */
public class SpreadSheetGROOP {
    String[] header;
    String[][] spreadsheet;
    /** Creates a new instance of Spreadsheet */

    public static void main (String[] args){

        


         SpreadSheetGROOP spsg = new SpreadSheetGROOP(new File("testbed.csv"));
         Vector allNames = spsg.getAllSenderUsernames();
         for(int i=0;i<allNames.size();i++){
             String name = (String)allNames.elementAt(i);
             if(!name.equalsIgnoreCase("server")){
                 spsg.createContig(name);
             }
         }
         spsg.createAlignmentScores();
        spsg.writeSpreadsheetToFile(new File("output.csv"));
         
    }



    public SpreadSheetGROOP(File f) {
        try {
        BufferedReader in = new BufferedReader(new FileReader(f));
        String str;
        str = in.readLine();
        header = str.split("[|]");
        System.out.println("HEADER0 "+header[0]);
        Vector allRows = new Vector();
        while ((str = in.readLine()) != null) {
            String[] rowString = str.split("[|]");
            
            rowString = addEmptyCells(rowString,5);
            allRows.addElement(rowString);

        }

        spreadsheet = new String[allRows.size()][header.length+5];
        System.out.println("SPREADSHEET IS "+allRows.size()+"WIDTH "+header.length+5);
        for(int i=0;i<allRows.size();i++){
            spreadsheet[i]=(String[])allRows.elementAt(i);
            System.out.println(spreadsheet[i][7]+i);
            System.out.println(spreadsheet[i][header.length]+i);
        }


        in.close();


    } catch (IOException e) {

    }
  }


    public Vector getAllSenderUsernames(){
         Vector v = new Vector();
         for(int i=0;i<spreadsheet.length;i++){
             String senderusername = spreadsheet[i][1];
             boolean alreadyseen = false;
             for(int j=0;j<v.size();j++){
                 String s2 = (String)v.elementAt(j);
                 if(s2.equalsIgnoreCase(senderusername)){
                     alreadyseen = true;
                     break;
                 }
             }
             if(!alreadyseen)v.addElement(senderusername);
         }
         return v;
     }



     String[] a = {"sender", "Text","recipient"};


     public void createContig(String username){
         String currContig ="";
         int lastIndexOfContig =0;

         for(int i=0;i<spreadsheet.length;i++){
             String senderusername = spreadsheet[i][1];
             String recipientusername = spreadsheet[i][8].substring(2);
             String txtt = spreadsheet[i][7];


             if(senderusername.equalsIgnoreCase(username+"---")&& txtt.startsWith("/")){
                  //spreadsheet[i][20] = "--COMMAND--"+txtt;
             }

             else if(senderusername.equalsIgnoreCase(username) && !txtt.startsWith("/")){
                 lastIndexOfContig=i;
                 currContig= spreadsheet[i][7];
                 for(int j=i+1;j<spreadsheet.length;j++){
                     String s2 = spreadsheet[j][1];
                     String s2txt = spreadsheet[j][7];
                     if(s2.equalsIgnoreCase(senderusername) && !txtt.startsWith("/")){
                         String txt = spreadsheet[j][7];
                         currContig = currContig+"-"+txt;
                         lastIndexOfContig=j;
                        
                     }
                     else if(s2.equalsIgnoreCase(recipientusername) && !txtt.startsWith("/")){
                         //the contig needs to be saved at (i)
                         //i set to j
                         //contig is cleared
                         System.err.println(currContig);
                         spreadsheet[lastIndexOfContig][20] = currContig;

                         i=j;
                         currContig="";
                         break;
                     }
                 }

                 //searchforwards for contig or change
                 //if contig...fastforward to contig
                 //if change...add the contig text
             }

          }
     }



     public void createAlignmentScores(){
         for(int i=0;i<spreadsheet.length;i++){

             String txtContig = spreadsheet[i][20];
             if(txtContig!=null&&txtContig.length()>0){
                 String senderusername = spreadsheet[i][1];
                 String recipientusername = spreadsheet[i][8].substring(2);
                 for(int j=i+1;j<spreadsheet.length;j++){
                     String s2txtContig = spreadsheet[j][20];
                     String s2senderusername = spreadsheet[j][1];
                     if(s2senderusername.equalsIgnoreCase(recipientusername)&&s2txtContig!=null&&s2txtContig.length()>0){
                          spreadsheet[i][21]="ALIGNMENTSCORE: "+txtContig+"----"+s2txtContig+"("+j+")";

                          float[] a = StringSimilarityMeasure.getProportionOfWordsOfAInBandBinA(txtContig, s2txtContig);

                          spreadsheet [i][21] = ""+a[0];
                          spreadsheet [i][22] = ""+a[1];


                          break;
                     }
                 }
             }

         }
     }






     public void createContig(){





         for(int i=0;i<spreadsheet.length;i++){
             String senderusername = spreadsheet[i][1];
             String recipientWithComma = spreadsheet[i][8];
             String recipient = recipientWithComma.substring(2);
             for(int j=i+1;j<spreadsheet.length;j++){
                 String nnextusername  = spreadsheet[j][1];
                 if(nnextusername.equalsIgnoreCase(senderusername)){

                 }
             }

         }
         
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



}

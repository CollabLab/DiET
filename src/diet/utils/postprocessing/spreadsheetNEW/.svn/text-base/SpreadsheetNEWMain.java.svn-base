/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package diet.utils.postprocessing.spreadsheetNEW;

import java.io.File;
import java.util.Arrays;
import java.util.Vector;

import diet.server.conversationhistory.turn.ContiguousTurn;
import diet.server.conversationhistory.turn.TurnMazeGame;
import diet.utils.StringOperations;
import diet.utils.postprocessing.deletions.ConversationHistories;

/**
 *
 * @author user
 */
public class SpreadsheetNEWMain {

    /**
     * @param args the command line arguments
     */
    
    
    public static void captureALLClarificationsAndConversationHistories(){
        
        
        ConversationHistories chs =new ConversationHistories();
        File f = new File("testspreadsheetin.csv");
        SpreadsheetV spn = new SpreadsheetV(f);
        
        Vector vALLTSTs = spn.getAllIndicesOfInterventionCRs();
        int rowCount = spn.getRowCount();
        boolean processingIntervention = false;
        String dyadNo = "";
        
        ContiguousTurn tst =null;;
        TurnMazeGame cr=null;;
        ContiguousTurn responsePriorToAck=null;
        TurnMazeGame ack=null;
        ContiguousTurn nextTurnAfterAck=null;
        String participantAName="";
        String participantBName="";
        
        boolean pFoundTST = false;
        boolean pFoundResp = false;
        boolean pFoundAck = false;
        boolean pFoundNextTurn = false;
        
        int crsFOUND =0;
        int turnsELAPSEDSINCECR=0;
        for(int i=0;i<rowCount;i++){
            String newDyadNo = spn.getValue("DyadNo", i);
            
            
            //public TurnMazeGame addNormalMazeGameTurn(long onset, long enter, String senderUsername, String apparentSenderUsername, String text,
            //                Vector recipientsNames, boolean wasBlocked, Vector keyPresses,Vector documentUpdates,int turnNumber,String descType){
            
           
              long onset=-999999999;
              long enter=-999999999;
            try{
              String onsetS= (spn.getValue("Onset", i));
              String enterS= (spn.getValue("Enter", i));
              if(enterS==null||enterS.equalsIgnoreCase("")||enterS.equalsIgnoreCase(" ")){
                  enterS=""+onsetS;
              } 
              onset = Long.parseLong(onsetS);
              enter = Long.parseLong(enterS);
              }catch (Exception e){
                 System.err.println("ERROR PARSING LONG IN ROW "+i);
                 //System.exit(-5);
              }
              String senderUsername = spn.getValue("Subject", i);
              String text =spn.getValue("Txt", i);
              String descType = spn.getValue("FPLM", i); 
            
            if(true){
               if(!dyadNo.equalsIgnoreCase(newDyadNo)){
                  chs.createNewConversationHistory(dyadNo);
                  dyadNo=""+newDyadNo;
                  participantAName = spn.getValue("Subject",i);
                  for(int j = i;j<rowCount;j++){
                      String otherUsernameCandidate = spn.getValue("Subject", j);
                      if(!otherUsernameCandidate.equalsIgnoreCase(participantAName)){
                          participantBName=otherUsernameCandidate;
                          break;
                      }
                      String dyadNoOfCandidate = spn.getValue("DyadNo", i);
                      if(!dyadNoOfCandidate.equalsIgnoreCase(dyadNo)){
                          System.err.println("HAS TRIED TO FIND OTHER SUBJECT...CAME TO NEXT DYAD: "+i+"---"+dyadNo);
                          System.err.println("");
                          System.exit(-5);
                      }             
                  }          
               }
               String otherOne = StringOperations.returnOtherString(senderUsername, participantAName, participantBName);
               Vector vOtherOne = new Vector();
               vOtherOne.addElement(otherOne);
               System.err.print(i);
               String textToSave=""+text;
               String apparentSenderName = ""+senderUsername;
               
               String ackCROther="Other";
               if(spn.isRowNumberACK(i)){
                   textToSave=spn.extractAckStringFromString(text);
                   vOtherOne=new Vector();
                   vOtherOne.addElement(""+senderUsername);
                   senderUsername="server";
                   ackCROther="ACK";
                   apparentSenderName=otherOne;
               }
               else if(spn.isRowNumberCR(i)){
                   textToSave=spn.extractCRStringFromString(text);
                   
                   vOtherOne=new Vector();
                   vOtherOne.addElement(""+senderUsername);
                   senderUsername="server";
                   ackCROther="CR";
                   apparentSenderName=otherOne;
               }
               chs.addNormalMazeGameTurn(onset, enter, senderUsername, apparentSenderName, textToSave, vOtherOne, false, new Vector(), new Vector(), i, descType,ackCROther);
               
           }
        }   
            chs.processConversationHistoriesForCRRESPACKNEXT();
           
    }
    
    
    
    
    
    
    
    public static void main(String[] args) {
       captureALLClarificationsAndConversationHistories();
       //oldProcessing();
    }
    
    public static void oldProcessing() {
        // TODO code application logic here
        try{
          File f = new File("testspreadsheetin.csv");
          SpreadsheetV spn = new SpreadsheetV(f);
          Vector v = spn.getAllIndicesOfInterventionCRs();
          
          for(int i=0;i<v.size();i++){
              String cr = spn.getCRString((Integer)v.elementAt(i));
              String tst = spn.getTSTOfCR((Integer)v.elementAt(i));
              String resp = spn.getNextTurnBySameSpeaker((Integer)v.elementAt(i));
              System.out.println(i+": "+tst+"...."+cr+"....."+resp);
              String[] tstsplit = tst.split("[\\W]");
              String[] respsplit = resp.split("[\\W]");
              for(int j=0;j<tstsplit.length;j++){
                  System.out.println("..."+tstsplit[j]);
              }
              Vector tstV = new Vector(Arrays.asList(tstsplit));
              Vector respV = new Vector(Arrays.asList(respsplit));
              
              
          }
          
          
          
          
          File g = new File("testspreadsheetout.csv");
          //spn.writeSpreadsheetToFile(g);
        }catch(Exception e){
            System.out.println("ERROR ");
            e.printStackTrace();
            System.exit(-234);
            
        }
        
        
        
    }

}

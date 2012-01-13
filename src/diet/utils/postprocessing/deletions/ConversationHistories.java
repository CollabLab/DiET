/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package diet.utils.postprocessing.deletions;

import java.util.Vector;

import diet.server.conversationhistory.Conversant;
import diet.server.conversationhistory.ConversationHistory;
import diet.server.conversationhistory.turn.ContiguousTurn;
import diet.server.conversationhistory.turn.TurnMazeGame;

/**
 *
 * @author Greg
 */
public class ConversationHistories {
  
     public Vector conversationHistories = new Vector();
     private Vector clarificationRequests = new Vector();
     
     public ConversationHistory currentHistoryBeingProcessed;
     
     
     public void createNewConversationHistory(String name){
         currentHistoryBeingProcessed  = new ConversationHistory(null,name,null,null);
         conversationHistories.addElement(currentHistoryBeingProcessed);
     }
     
     //public TurnMazeGame addNormalMazeGameTurnAbbreviated(long onset, long enter, String senderUsername, String apparentSenderUsername)
     
     public TurnMazeGame addNormalMazeGameTurn(long onset, long enter, String senderUsername, String apparentSenderUsername, String text,
                            Vector recipientsNames, boolean wasBlocked, Vector keyPresses,Vector documentUpdates,int turnNumber,String descType,String ackCROther){
  
        TurnMazeGame t = currentHistoryBeingProcessed.saveMessageFromCRMazeGame(onset, enter, senderUsername, apparentSenderUsername, text, recipientsNames, wasBlocked, keyPresses, documentUpdates, turnNumber, descType,ackCROther);
        System.err.println(t.getText());
        return t;
     }   
     
     
     public void printALLCRs(){
         for(int i=0;i<this.clarificationRequests.size();i++){
             ClarificationRequest cr = (ClarificationRequest)clarificationRequests.elementAt(i);
             System.err.println("NEWCR AT "+i);
                    System.err.println(cr.tst.getTextString());
                    System.err.println(cr.cr.getTextString());
                    if(cr.responsePriorToAck!=null) System.err.println(cr.responsePriorToAck.getTextString());
                    if(cr.ack!=null)System.err.println(cr.ack.getTextString());
                    if(cr.nextTurnAfterAck!=null)System.err.println(cr.nextTurnAfterAck.getTextString());
                    Vector v =cr.tst.getStrings();
                    for(int j=0;j<v.size();j++){
                        String s = (String)v.elementAt(j);
                        System.err.println("TSTWORDS: "+s);
                    }
         }
     }
     
     
     
     public void processConversationHistoriesForCRRESPACKNEXT(){
         for(int i=0;i<this.conversationHistories.size();i++){
             ConversationHistory ch = (ConversationHistory)conversationHistories.elementAt(i);
             processCH(ch);
             //System.err.println("SIZEIS"+clarificationRequests.size());
             //ch.getMostRecentContiguousTurn().getApparentSender().
         }
         printALLCRs();
         ClarificationRequestIO.saveCH(clarificationRequests);
         
     }
     public void processCH(ConversationHistory ch){
         Vector turns = ch.getTurns();
         for(int i=0;i<turns.size();i++){
               TurnMazeGame t = (TurnMazeGame)turns.elementAt(i);
               if(t.isTSTCRRESPACKNEXT.equalsIgnoreCase("CR")){
                   System.out.println("FOUNDCRAT "+i+" "+t.getTextString());
                   String recipientOfCR = (String)((Conversant)t.getRecipients().elementAt(0)).getUsername();
                   ClarificationRequest crr = new ClarificationRequest(ch);
                   ContiguousTurn ctTST= ch.getPriorContiguousTurnOfConversant(recipientOfCR, t);
                   crr.tst=ctTST;
                   crr.cr=t;
                   boolean foundResponsePriorToAck=false;
                   int indexOfResponsePriorToAck=i;
                   for(int j=i+1;j<turns.size();j++){
                       TurnMazeGame t2 = (TurnMazeGame)turns.elementAt(j);
                       if(t2.getSender().getUsername().equalsIgnoreCase(recipientOfCR)){
                           crr.responsePriorToAck=ch.getEnclosingContiguousTurn(t2);
                           System.err.println("RESPPRIORTOACKIS "+crr.responsePriorToAck.getTextString());
                           foundResponsePriorToAck=true;
                           indexOfResponsePriorToAck=j;
                           break;
                       }
                       else if(((j>i)&&t2.isTSTCRRESPACKNEXT.equalsIgnoreCase("CR"))||t2.isTSTCRRESPACKNEXT.equalsIgnoreCase("ACK")){
                           break;
                       }
                   }
                   boolean foundAck=false;
                   int indexOfAck=i;
                   if(foundResponsePriorToAck){
                   for(int j=indexOfResponsePriorToAck;j<turns.size();j++){
                       TurnMazeGame t2 = (TurnMazeGame)turns.elementAt(j);
                       if(t2.isTSTCRRESPACKNEXT.equalsIgnoreCase("ACK")){
                           crr.ack=t2;
                           foundAck=true;
                           indexOfAck=j+1;
                           System.err.println("FOUND ACK AT "+i+":"+j+": "+t2.getTextString());
                           break;
                       }
                       else if(t2.isTSTCRRESPACKNEXT.equalsIgnoreCase("CR")){
                           break;
                       }
                   }
                   }
                   if(foundAck){
                      for(int j=indexOfAck;j<turns.size();j++){
                         TurnMazeGame t2 = (TurnMazeGame)turns.elementAt(j);
                         if(t2.getSender().getUsername().equalsIgnoreCase(recipientOfCR)){
                              System.err.println("FOUND NEXT TURN AT "+i+":"+j+": "+t2.getTextString());
                              crr.nextTurnAfterAck=ch.getEnclosingContiguousTurn(t2);
                              crr.isVALID=true;
                              break;
                         }
                         else if(t2.isTSTCRRESPACKNEXT.equalsIgnoreCase("CR")){
                           break;
                       }
                      }
                   }
                   
                   if(crr.isVALID){
                       this.clarificationRequests.add(crr);
                       crr.calculateNumberOfWordsSharedAndDeleted();
                       crr.calulateRatios();
                       crr.determineWOTFRAG();
                   }
               }
               
         }
         
     }
    
}  

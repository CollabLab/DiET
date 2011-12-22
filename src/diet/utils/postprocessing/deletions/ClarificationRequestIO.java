/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package diet.utils.postprocessing.deletions;

import java.io.File;
import java.io.PrintWriter;
import java.util.Vector;

import diet.server.conversationhistory.LexiconEntry;

/**
 *
 * @author Greg
 */
public class ClarificationRequestIO {

    
    
    
    static public void saveCH(Vector clarificationRequests){
        try{
          File f =File.createTempFile("crequest", ".csv",new File(System.getProperty("user.dir")));
          PrintWriter pw =new PrintWriter(f);
          pw.println(getHeader());
          for(int i=0;i<clarificationRequests.size();i++){
              ClarificationRequest cr = (ClarificationRequest)clarificationRequests.elementAt(i);
              pw.println(getRow(cr));
              
          }
          pw.close();
        }catch(Exception e){
            System.out.println("ERRORSAVINGFILE");
            e.printStackTrace();
        }
        
        
        
        
    }
    
    static public String getHeader(){
        
        return "WOTFRAG|TST|CR|RESPONSE|ACK|NEXT|RESPCONTAINSCR|NEXTCONTAINSCR"+
                "wrdsShared_TSTResp|"+
                "wrdsShared_TSTNext|" +
                "wrdsShared_RespNext|"+
                "wrdsShared_ALL3|"+
                
                "wrdsInTST_ButNotResp|"+
                "wrdsInTST_ButNotNext|"+
                "wrdsInRESP_ButNotNext|"+
                "wrdsInTST_ButNotInResp_ANDNOTINNEXT|"+
                
                "wrdsInResp_ButNotTST|"+
                "wrdsInNext_ButNotTST|"+
                "wrdsInNext_ButNotResp|"+
                "wrdsInNext_ButNotInTSTSndNotInResponse|"+
                
                "longestcommsubseqTSTResp|"+
                "longestcommsubseqTSTNext|"+
                "longestcommsubseqRespNext|"+
                
                "tstWrdsIntroByRecip|"+
                "tstWrdsIntroByOther|"+
                
                "respWrdsIntroByRecip|"+
                "respWrdsIntroByOther|"+
                
                "nextTWrdsIntroByRecip|"+
                "nextTWrdsIntroByOther|";
    }         
    
     static public String getRow(ClarificationRequest cr){
         if(!cr.isVALID)return "";
         if(2>5){
            return   "|"+"|"+  "|"+  "|"+ "|"+ "|"+ "|"+ 
                 
      "|"+
     "|"+
     "|"+ 
     "|"+
     
     "|"+
     "|"+
     "|"+
     "|"+
                    
     "|"+
     "|"+
     "|"+
     "|"+
     
     "|"+
     "|"+
     "|"+
     
     //longestCommonSubSequenceTSTAndResponseLENGTH =0;
     //longestCommonSubSequenceTSTAndNextTurnLENGTH=0;
     //public int longestCommonSubSequenceResponseAndNextTurnLENGTH=0;
     
     "|"+
     "|"+
     
     "|"+
     "|"+
     
     "|"+
     "|";
         }
         return  
                 cr.wotFRAG+
                 "|"+cr.tst.getTextString()+
                 "|"+ cr.cr.getTextString()+
                 "|"+ cr.responsePriorToAck.getTextString()+
                 "|"+ cr.ack.getTextString()+
                 "|"+ cr.nextTurnAfterAck.getTextString()+
                 
                 "|"+Boolean.toString(cr.respContainsCR)+
                 "|"+Boolean.toString(cr.nextContainsCR)+
                 
      "|"+s2(cr.wordsSharedByTSTAndResponse) +
     "|"+s2(cr.wordsSharedByTSTAndNextTurn)+
     "|"+s2(cr.wordsSharedByResponseAndNextTurn)+ 
     "|"+s2(cr.wordsSharedByAll3)+
     
     "|"+s2(cr.wordsInTSTButNotResponse)+
     "|"+s2(cr.wordsInTSTButNotNextTurn) +
     "|"+s2(cr.wordsInResponseButNotNextTurn) +
     "|"+s2(cr.wordsInTSTButNotInResponseAndNotInNextTurn) +
     
     "|"+s2(cr.wordsInResponseButNotTST) +
     "|"+s2(cr.wordsInNextTurnButNotTST) +
     "|"+s2(cr.wordsInNextTurnButNotResponse) +
     "|"+s2(cr.wordsInNextTurnButNotInTSTSndNotInResponse)+
     
     "|"+cr.longestCommonSubSequenceTSTAndResponseLENGTH +
     "|"+cr.longestCommonSubSequenceTSTAndNextTurnLENGTH+
     "|"+cr.longestCommonSubSequenceResponseAndNextTurnLENGTH+
     
     //longestCommonSubSequenceTSTAndResponseLENGTH =0;
     //longestCommonSubSequenceTSTAndNextTurnLENGTH=0;
     //public int longestCommonSubSequenceResponseAndNextTurnLENGTH=0;
     
     "|"+s2(cr.tstWordsIntroducedByCRRecipient) +
     "|"+s2(cr.tstWordsIntroducedByOther) +
     
     "|"+s2(cr.respWordsIntroducedByCRRecipient) +
     "|"+s2(cr.respWordsIntroducedByOther) +
     
     "|"+s2(cr.nextWordsIntroducedByCRRecipient) +
     "|"+s2(cr.nextWordsIntroducedByOther) ;
    }
    
    static public String s2(Vector v){
        return ""+v.size();
    } 
     
    static public String s2OLD(Vector v){
        String s2 ="";
        for(int i=0;i<v.size();i++){
            Object o = v.elementAt(i);
            if(o instanceof String){
               String s = (String)v.elementAt(i);
               s2=s2+" "+s;
            }else if(o instanceof LexiconEntry){
                LexiconEntry le = (LexiconEntry)o;
                s2=s2+" "+le.getWord();
            }   
        }
        return s2;
    }
}

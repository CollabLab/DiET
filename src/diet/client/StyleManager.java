/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package diet.client;

import java.util.Vector;

import diet.debug.Debug;
import diet.server.CbyC.DocInsert;
import diet.server.CbyC.DocInsertHeader;
import diet.server.Conversation;
import diet.server.ConversationController.DefaultConversationController;
import diet.server.Participant;

/**
 *
 * @author Greg
 */
public class StyleManager {

     
     DefaultConversationController cC;
     
     public StyleManager(DefaultConversationController cC){
         
         this.cC=cC;
     }
    
    
     public int getUniqueIntForRecipient(Participant recipient, Participant sender){
         Vector v = cC.getC().getParticipants().getAllParticipantsNewCopy();
         v.remove(recipient);
         int unique = v.indexOf(sender);
         if(unique<0)unique = v.size();
         return 1+unique;   
     }
     
     public int getUniqueIntForRecipientNoSender(Participant recipient)
     {
    	 Vector v = cC.getC().getParticipants().getAllParticipantsNewCopy();
         v.remove(recipient);
         return v.size()+1;
    	 
     }
     int prefixCount=0;
     
     public DocInsertHeader getPrefixForParticipant(String prefixCount,Participant recipient,Participant prefix){
         
         String styleName = "h"+ (cC.getC().getParticipants().getAllParticipants().size()-1);
         if(recipient==prefix){
            styleName = "hs";          
         }
         else{
            styleName = "h"+getUniqueIntForRecipient(recipient,prefix);
         }
         if(!Debug.showPrefixBeforeCBYCTurn){
            prefixCount = ""; 
         }
         else{
            prefixCount ="("+prefixCount+") ";
         }
         DocInsertHeader di = new DocInsertHeader("server",prefix.getUsername(),recipient.getUsername(),0,"\n"+prefixCount+prefix.getUsername()+"\n",styleName);
        //DocInsertHeader dih = new DocInsertHeader("server",prefix.getUsername(),recipient.getUsername(),0,"\n"+prefix.getUsername()+": ",styleName);
         
         return di;
    }
    
     
     
     
     
     
     
     
     
     
     
     
     
     
     
    public Vector getPrefixesForParticipantsDeprecated(Vector recipients,Participant prefix){
       
        Vector result = new Vector();
        for(int i=0;i<recipients.size();i++){
            Participant p = (Participant)recipients.elementAt(i);
            DocInsert di = getPrefixForParticipant("",p,prefix);
            result.addElement(di);
        }
        return result;
    }
     
     
}

package diet.server.ConversationController;
import diet.message.MessageChatTextFromClient;
import diet.message.MessageKeypressed;
import diet.message.MessageWYSIWYGDocumentSyncFromClientInsert;
import diet.message.MessageWYSIWYGDocumentSyncFromClientRemove;
import diet.message.MessageWYSIWYGTextSelectionFromClient;
import diet.server.Participant;
import java.util.logging.Level;
import java.util.logging.Logger;




public class CCCUSTOM1 extends DefaultConversationController{
   
    
        
    @Override
    public void processLoop(){
            
             c.resetFromIsTypingtoNormalChatAllAllowedParticipants(super.getIsTypingTimeOut());      
              
    }
    
   
  
    
    
    @Override
    public void processChatText(Participant sender,MessageChatTextFromClient mct){    
       
           // pTurnsElapsed.setValue(((Integer)pTurnsElapsed.getValue())+1);
           // super.expSettings.generateParameterEvent(pTurnsElapsed);
            
           String[] toStrip = {">:]", ":-)", ":)", ":o)", ":]", ":3", ":c)",
               ":>", "=]", "8)", "=)", ":}", ":^)", ">:D", ":-D", ":D", "8-D",
               "8D", "x-D", "xD", "X-D", "XD", "=-D", "=D", "=-3", "=3", "8-)",
               ":-))", ">:[", ":-(", ":(", ":-c", ":c", ":-<", ":<", ":-[",
               ":[", ":{", ">.>", "<.<", ">.<", ":-||", "D:<", "D:", "D8", "D;",
               "D=", "DX", "v.v", "D-':", ">;]", ";-)", ";)", "*-)", "*)",
               ";-]", ";]", ";D", ";^)", ">:P", ":-P", ":P", "X-P", "x-p", "xp",
               "XP", ":-p", ":p", "=p", ":-Þ", ":Þ", ":-b", ":b", ">:o", ">:O",
               ":-O", ":O", "°o°", "°O°", ":O", "o_O", "o_0", "o.O", "8-0",
               ">:\\", ">:/", ":-/", ":-.", ":/", ":\\", "=/", "=\\", ":S",
               ":|", ":-|", ">:X", ":-X", ":X", ":-#", ":#", ":$", "O:-)",
               "0:-3", "0:3", "O:-)", "O:)", "0;^)", ">:)", ">;)", ">:-)",
               "o/\\o", "^5", ">_>^", "^<_<", "|;-)", "|-O", "}:-)", "}:)",
               ":-&", ":&", "#-)", "%-)", "%)", ":-###..", ":###..", ":'-(",
               ":'(", ":'-)", ":')", "<:-|", "(-_-)", "ಠ_ಠ", "<*)))-{", "*\0/*",
               "@}-;-'---", "@>-->--", "~(_8^(I)", "5:-)", "//0-0\\\\","*<|:-)",
               "=:o]", ",:-)", "7:^]"};
           
           /*{">:]", ":-\\)", ":\\)", ":o\\)", ":]", ":3", ":c\\)",
               ":>", "=]", "8\\)", "=\\)", ":}", ":^\\)", ">:D", ":-D", ":D", "8-D",
               "8D", "x-D", "xD", "X-D", "XD", "=-D", "=D", "=-3", "=3", ">:\\[",
               ":-\\(", ":\\(", "", ":-c", ":c", ":-<", ":<", ":-\\[", ":\\[", ":\\{",
               ">.>", "<.<", ">.<", "D:<", "D:", "D8", "D;", "D=", "DX", "v.v",
               "D-':", ">;]", ";-\\)", ";\\)", "*-\\)", "*\\)", ";-]", ";]", ";D",
               ">:P", ":-P", ":P", "X-P", "x-p", "xp", "XP", ":-p", ":p", "=p",
               ":-Þ", ":Þ", ":-b", ":b", ">:o", ">:O", ":-O", ":O", "°o°", "°O°",
               ":O", "o_O", "o.O", ">:\\", ">:/", ":-/", ":-.", ":/", ":\\",
               "=/", "=\\", ":S", ":|", ">:X", ":-X", ":X", ":-#", ":#", ":$",
               "O:-\\)", "0:-3", "0:3", "O:-\\)", "O:\\)", ">:\\)", ">;\\)", ">:-\\)",
               "o/\\o", "^5", ">_>^", "^<_<", ":-o", ":o"};*/
           
           for(int i = 0; i < toStrip.length; i++)
           {
            // mct.setText(mct.getText().replace(toStrip[i], ""));
               mct.setText(mct.getText().replaceAll("(?<=(\\s|^))" + toStrip[i].replaceAll("([\\\\*+\\[\\](){}\\$.?\\^|])", "\\\\$1") + "($|\\s)", ""));
               // uses http://www.dreamincode.net/code/snippet3139.htm
           }
           // TODO: how to deal with emoticon-only messages? make it look like nothing was sent?
           
           // interrobang?!
           mct.setText(mct.getText().replaceAll("[\\?!]*\\?+[\\?!]*(?=$|\\s)", "?"));
           
           // change ! at the end of a sentence to .
           mct.setText(mct.getText().replaceAll("!+(?=\\s|$)", "."));
           
           
           // get rid of asterisks around *emphasized* words
           //mct.setText(mct.getText().replaceAll("(\\*)([a-z]+)(\\*)", mct.getText().split("(\\*)([a-z]+)(\\*)")[1]));
           mct.setText(mct.getText().replaceAll("(\\*)([a-zA-Z]+)(\\*)", "$2"));
           
           // strip "really" when not at the end of a sentence.
           // mct.setText(mct.getText().replaceAll("really\\s", ""));
           
           // If there are 2 caps in a row, make the whole thing lowercase
           /*if(mct.getText().matches(".*[A-Z]{2,}.*"))
           {
               mct.setText(mct.getText().toLowerCase());
           }*/
           mct.setText(mct.getText().replaceAll("([A-Z]{2,})", "$1".toLowerCase()));
           
           /*try {
               Thread.sleep(3000);
           } catch (InterruptedException ex) {
               Logger.getLogger(CCCUSTOM1.class.getName()).log(Level.SEVERE, null, ex);
           }*/
    
           c.relayTurnToAllOtherParticipants(sender,mct);
           c.sendLabelDisplayToAllowedParticipantsFromApparentOrigin(sender,"Status: OK",false);
          
           c.setNewTurnBeingConstructedNotRelayingOldTurnAddingOldTurnToHistory(sender, mct);
                   
    }
    
    @Override
    public void processKeyPress(Participant sender,MessageKeypressed mkp){
        //c.informIsTypingToAllowedParticipants(sender);//,mkp);
    }

    
    @Override
    public void processWYSIWYGTextInserted(Participant sender,MessageWYSIWYGDocumentSyncFromClientInsert mWYSIWYGkp){
          c.informIsTypingToAllowedParticipants(sender);
       
    }
    
    @Override
    public void processWYSIWYGTextRemoved(Participant sender,MessageWYSIWYGDocumentSyncFromClientRemove mWYSIWYGkp){
        //c.relayWYSIWYGTextRemovedToAllowedParticipants(sender,mWYSIWYGkp);
        //turnBeingConstructed.remove(mWYSIWYGkp.getOffset(),mWYSIWYGkp.getLength(),mWYSIWYGkp.getTimeStamp().getTime()); 
        //chOut.addMessage(sender,mWYSIWYGkp);
    }
    
    @Override
    public void processWYSIWYGSelectionChanged(Participant sender, MessageWYSIWYGTextSelectionFromClient mWYSIWYGSel){
        //c.relayWYSIWYGSelectionChangedToAllowedParticipants(sender,mWYSIWYGSel);
    }
    
   
   


    
    
   

   

}

package diet.server.ConversationController;
import java.util.Date;
import java.util.Vector;

import diet.client.WYSIWYGDocumentWithEnforcedTurntaking;
import diet.message.MessageChatTextFromClient;
import diet.message.MessageKeypressed;
import diet.message.MessageWYSIWYGDocumentSyncFromClientInsert;
import diet.message.MessageWYSIWYGDocumentSyncFromClientRemove;
import diet.message.MessageWYSIWYGTextSelectionFromClient;
import diet.message.MessageWYSIWYGTypingUnhinderedRequest;
import diet.server.DocChangesOutgoingSequenceFIFO;
import diet.server.Participant;
import diet.server.StringOfDocChangeInserts;
import diet.server.CbyC.DocChange;
import diet.server.CbyC.DocInsert;
import diet.textmanipulationmodules.TypoGenerator.TypoGenerator;
import edu.gwu.wordnet.PointerType;
import edu.stanford.nlp.ling.TaggedWord;




public class CCWITHINTURNINTERCEPTUHH extends DefaultConversationController {
    
    
   

     
    
    public CCWITHINTURNINTERCEPTUHH(){
        chOut.setMaxTrickleSize(0);        
    }
    
    
    
    @Override
    public void processLoop(){
            processLoopIntercept();
            processSendOutgoingQueue();      
    }
    
   
    @Override
    public void processChatText(Participant sender,MessageChatTextFromClient mct){    
       if(mct.getText().equalsIgnoreCase("me")){
          //c.sendArtificialTurnFromApparentOriginToRecipient(sender,sender,"you are typing less and less coherently");         
       }
       else{
           c.relayTurnToAllOtherParticipants(sender,mct);
           c.sendLabelDisplayToAllowedParticipantsFromApparentOrigin(sender,"Status: OK",false);
       }
    }
    
    @Override
    public void processKeyPress(Participant sender,MessageKeypressed mkp){
        c.informIsTypingToAllowedParticipants(sender);//,mkp);
    }
    
    @Override
    public void processWYSIWYGTextInserted(Participant sender,MessageWYSIWYGDocumentSyncFromClientInsert mWYSIWYGkp){
        //c.informIsTypingToAllowedParticipants(sender,mWYSIWYGkp);
        
        if(mWYSIWYGkp.getTextToAppendToWindow().endsWith("\n")){
            mostRecentTurnEndedWithNewLine = true;
        }
        else{
            mostRecentTurnEndedWithNewLine =false;
        }
        //c.relayWYSIWYGTextInsertedToAllchOut.addInsert(sender,mWYSIWYGkp);owedParticipants(sender,mWYSIWYGkp);
        chOut.addMessage(sender,mWYSIWYGkp);
        
        int trickleSize = chOut.getMaxTrickleSize();
        if(trickleSize<1){
            chOut.setMaxTrickleSize(0);
        }
        else{
            chOut.setMaxTrickleSize(chOut.getMaxTrickleSize()-5);
        }
        
       manipulateOutgoingQueue(mWYSIWYGkp);
    }
    
    @Override
    public void processWYSIWYGTextRemoved(Participant sender,MessageWYSIWYGDocumentSyncFromClientRemove mWYSIWYGkp){
        //c.relayWYSIWYGTextRemovedToAllowedParticipants(sender,mWYSIWYGkp);
        //turnBeingConstructed.remove(mWYSIWYGkp.getOffset(),mWYSIWYGkp.getLength(),mWYSIWYGkp.getTimeStamp().getTime()); 
        chOut.addMessage(sender,mWYSIWYGkp);
    }
    
    @Override
    public void processWYSIWYGSelectionChanged(Participant sender, MessageWYSIWYGTextSelectionFromClient mWYSIWYGSel){
        c.relayWYSIWYGSelectionChangedToAllowedParticipants(sender,mWYSIWYGSel);
    }
    
    Participant typist = null;
    Participant priorTypist = null;
    boolean mostRecentTurnEndedWithNewLine = true;
    
    
    
    
    boolean isFirstTurn =true;
    
    @Override
    public void processWYSIWYGTypingUnhinderedRequest(Participant sender,MessageWYSIWYGTypingUnhinderedRequest mWTUR){
        System.err.println("SERVER:-----------------------------REQUESTING to allow typingunhindered by: "+sender.getUsername());
     
        if(typist==null){           
            //flushAllOutgoingQueues;    
            typist=sender;
                        
            if(isFirstTurn && sender!=priorTypist&mostRecentTurnEndedWithNewLine&mWTUR.getOffsetFrmRight()==0&!mWTUR.getText().equals("")){ 
              c.sendWYSIWYGChangeInterceptionStatusToAllowedParticipants(sender,WYSIWYGDocumentWithEnforcedTurntaking.othertyping,"< "+typist.getUsername()+" >\n");
              c.sendWYSIWYGChangeInterceptionStatusToSingleParticipant(sender,WYSIWYGDocumentWithEnforcedTurntaking.typingunhindered,"< "+typist.getUsername()+" >\n");
              c.sendLabelDisplayToAllowedParticipantsFromApparentOrigin(typist,typist.getUsername()+"....is typing",true);
              DocChangesOutgoingSequenceFIFO chOutOLD = chOut;
              chOut = new DocChangesOutgoingSequenceFIFO(sender,0,new Date().getTime());
              if(priorTypist!=null){
                  c.wYSIWYGSetNewDocChangesIncomingSavingBothChangesTypedAndModifiedOutgoingQueue(priorTypist,mWTUR.getTimeStamp().getTime(),chOutOLD);
              }
              priorTypist=sender;
              isFirstTurn =false;
              //c.getDocChangesIncoming().setNewTurnAndReturnPreviousTurn(sender,mWTUR.getTimeStamp().getTime());
              
            }
            else if(sender!=priorTypist&mostRecentTurnEndedWithNewLine&mWTUR.getOffsetFrmRight()==0&!mWTUR.getText().equals("")){ 
              c.sendWYSIWYGChangeInterceptionStatusToAllowedParticipants(sender,WYSIWYGDocumentWithEnforcedTurntaking.othertyping,"\n< "+typist.getUsername()+" >\n");
              c.sendWYSIWYGChangeInterceptionStatusToSingleParticipant(sender,WYSIWYGDocumentWithEnforcedTurntaking.typingunhindered,"\n< "+typist.getUsername()+" >\n");
              c.sendLabelDisplayToAllowedParticipantsFromApparentOrigin(typist,typist.getUsername()+"....is typing",true);
              DocChangesOutgoingSequenceFIFO chOutOLD = chOut;
              chOut = new DocChangesOutgoingSequenceFIFO(sender,0,new Date().getTime());
              if(priorTypist!=null){
                  c.wYSIWYGSetNewDocChangesIncomingSavingBothChangesTypedAndModifiedOutgoingQueue(priorTypist,mWTUR.getTimeStamp().getTime(),chOutOLD);
              }
              priorTypist=sender;
              //c.getDocChangesIncoming().setNewTurnAndReturnPreviousTurn(sender,mWTUR.getTimeStamp().getTime());
              
            }
            else if(sender!=priorTypist&!mostRecentTurnEndedWithNewLine&mWTUR.getOffsetFrmRight()==0&!mWTUR.getText().equals("")){
              c.sendWYSIWYGChangeInterceptionStatusToAllowedParticipants(sender,WYSIWYGDocumentWithEnforcedTurntaking.othertyping,"\n\n< "+typist.getUsername()+" >\n");
              c.sendWYSIWYGChangeInterceptionStatusToSingleParticipant(sender,WYSIWYGDocumentWithEnforcedTurntaking.typingunhindered,"\n\n< "+typist.getUsername()+" >\n");
              c.sendLabelDisplayToAllowedParticipantsFromApparentOrigin(typist,typist.getUsername()+"....is typing",true);
              DocChangesOutgoingSequenceFIFO chOutOLD = chOut;
              chOut = new DocChangesOutgoingSequenceFIFO(sender,0,new Date().getTime());
               if(priorTypist!=null){
                  c.wYSIWYGSetNewDocChangesIncomingSavingBothChangesTypedAndModifiedOutgoingQueue(priorTypist,mWTUR.getTimeStamp().getTime(),chOutOLD);
               }
              priorTypist=sender;
              //c.getDocChangesIncoming().setNewTurnAndReturnPreviousTurn(sender,mWTUR.getTimeStamp().getTime());
              
            }
            else{
                //sender has regained the floor, is continuing with conversation
                //Or the sender is editing a previous part of text and hence no prefix is required
                //if 
                if(priorTypist!=sender){ //If another participant starts editing a preceding part of turn
                    DocChangesOutgoingSequenceFIFO chOutOLD = chOut;
                    c.wYSIWYGSetNewDocChangesIncomingSavingBothChangesTypedAndModifiedOutgoingQueue(priorTypist,mWTUR.getTimeStamp().getTime(),chOutOLD);
                    chOut = new DocChangesOutgoingSequenceFIFO(sender,0,new Date().getTime());
                    priorTypist = sender;
                }
                c.sendWYSIWYGChangeInterceptionStatusToAllowedParticipants(sender,WYSIWYGDocumentWithEnforcedTurntaking.othertyping,"");
                c.sendWYSIWYGChangeInterceptionStatusToSingleParticipant(sender,WYSIWYGDocumentWithEnforcedTurntaking.typingunhindered,"");
                c.sendLabelDisplayToAllowedParticipantsFromApparentOrigin(typist,typist.getUsername()+"....is typing",true);
            }       
        }
        //c.sendLabelDisplayToAllParticipantsInTheirOwnStatusWindow()
    }

    
    private void processLoopIntercept(){
       
        //if(2<5)return;
        if(typist!=null){
            //System.out.print("IN PROCESS LOOP2");
            if(!typist.isTyping(super.getIsTypingTimeOut())&&chOut.hasSentAllMessages()){  
                  System.err.println(c.getTurnBeingConstructed(typist).getParsedText());
                  c.sendWYSIWYGChangeInterceptionStatusToAllowedParticipants(typist,WYSIWYGDocumentWithEnforcedTurntaking.nooneelsetyping,"");
                  c.sendWYSIWYGChangeInterceptionStatusToSingleParticipant(typist,WYSIWYGDocumentWithEnforcedTurntaking.nooneelsetyping,"");                  
                  typist = null;
                  c.sendLabelDisplayToAllParticipantsInTheirOwnStatusWindow("Please type",false);
                  
            }
        }
    }


    
    private void processSendOutgoingQueue(){
       if(chOut!=null){
               DocChange dc = chOut.getNextDocumentChange();
               if(dc!=null) {
                   c.sendDocChangeToAllowedParticipants(chOut.getParticipant(),dc);
                   //System.out.println("---------------------------------------------------------------------------------------------------"+dc.getClass().toString());
               }
               else{
                  
               }
       }
    }


    
    
    private void manipulateOutgoingQueue(MessageWYSIWYGDocumentSyncFromClientInsert mWYSIWYGkp){
      try{   
        String textOfLatestMessage = mWYSIWYGkp.getTextToAppendToWindow();
        if(chOut.numberOfSubstitutionsPerformed()>2)return; //This needs to be done to speed the parser up
        if(!Character.isWhitespace(textOfLatestMessage.charAt(textOfLatestMessage.length()-1)))return;
        if(textOfLatestMessage.equalsIgnoreCase("\n"))return;
        if(chOut.getStringOfDocChangeInserts().getString().length()<6) return;
        int indexOfLastDotDots = chOut.getStringOfDocChangeInserts().getString().lastIndexOf("...");
        if(indexOfLastDotDots>0){
            if(chOut.getStringOfDocChangeInserts().getString().length()-indexOfLastDotDots<30)return;
        }
        
        int rr2 = r.nextInt(2);
        if(rr2==1)return;
        
        
        
        
        
        chOut.setRelayEnabled(false);
        StringOfDocChangeInserts entiresequence= chOut.getStringOfDocChangeInserts();
        String entiresequenceasstring = entiresequence.getString();
        //Vector entiresequenceParsed = c.getHistory().getParserWrapper().parseText(entiresequenceasstring);
        
        Vector v2 = c.getHistory().getParserWrapper().parseText(entiresequence.getString());
        Vector taggedWords = (Vector)v2.elementAt(0);
        
        
         
                
                Object[] repl = { "...sorry...umm.. "};
                int r2 = r.nextInt(5);
                if(r2==0){
                    repl  = new Object[]{"...uhhh..."};
                }
                else if(r2==1){
                    repl  = new Object[]{"...erm... "};
                }
                else if(r2==2){
                    repl  = new Object[]{"...sorry..uhh.."};
                }    
                else if(r2==3){
                    repl  = new Object[]{"...umm.."};
                }
                Vector vRepl =new TypoGenerator().createInsertRemoveSequence(repl,5);
                chOut.setMaxTrickleSize(chOut.getMaxTrickleSize()+vRepl.size()+4);
                chOut.addInsertsAndDeletes(vRepl);
                
                
                //Substitution done
                chOut.setRelayEnabled(true);
      }catch(Exception e){System.out.println("ERROR IN MANIPULATETURN");}
         
          chOut.setRelayEnabled(true);
       
        }
        

            
   
    
   
    int counter =0;

    private void processOutgoingSequenceFIFOToInsertCandidate(){
        
        //Should only process if last character added is enter or space or a specified timeout
     
        StringOfDocChangeInserts sodci = chOut.getStringOfDocChangeInserts();
        int indexOfUnsentChanges = chOut.getFirstIndexForChanges();
        
        Vector v2 = c.getHistory().getParserWrapper().parseText(sodci.getString());
        Vector taggedWords = (Vector)v2.elementAt(0);

       
       int indexInSodciStringOfNextWordCandidate = 0; 
       String sodciString = sodci.getString(); 
       
       
       //--------------------------
       
       Vector allInsertsSoFar = sodci.getSequence();
       
       Vector vToBeRemoved = new Vector();
       Vector vToBeAdded = new Vector();
        
       
       vToBeAdded.addElement(new DocInsert(0,Integer.toString(counter),null));
       vToBeAdded.addElement(new DocInsert(0,Integer.toString(counter),null));
       vToBeAdded.addElement(new DocInsert(0,Integer.toString(counter),null));
       vToBeAdded.addElement(new DocInsert(0,Integer.toString(counter),null));
       
       
      counter++;
       
       Vector allInsWords = new Vector();
       for(int i=0;i<taggedWords.size();i++){
            TaggedWord tw = (TaggedWord)taggedWords.elementAt(i);
            Vector insWord = new Vector();
            int beginIndex = sodciString.indexOf(tw.word(),indexInSodciStringOfNextWordCandidate);
            if(beginIndex>=indexInSodciStringOfNextWordCandidate){ //If it is found in the string
                
                int finishIndex = beginIndex+tw.word().length();
                System.out.println(i+":  found index at: "+beginIndex+": "+finishIndex+": ");
                Vector v3 = sodci.getSubSequence(beginIndex,finishIndex);
                if(!StringOfDocChangeInserts.getSubSequenceString(insWord).equalsIgnoreCase(tw.word())){
                    insWord = v3; //To check that the words are actually equal
                }
            }
            allInsWords.addElement(insWord);
       } 
        
       
       
       Vector allPossibleSubstitutions = new Vector();
        
        for(int i=0;i<taggedWords.size();i++){
           TaggedWord tw = (TaggedWord)taggedWords.elementAt(i);
           Vector vInsWords = (Vector)allInsWords.elementAt(i);
           boolean conductWordNetLookup = true; // To save processing time: Check if word in vector exists, check that word has not already been printed
           if(vInsWords==null){
              conductWordNetLookup = false;
           }
           else if (vInsWords.size()<3){ //Two letter words cause all kinds of problems with wordnet
               conductWordNetLookup = false;
           }
           //else if(!chOut.checkSequenceIsContinuousAndNotAlreadySentOrAlreadyChanged(vInsWords)) conductWordNetLookup = false; //
           
           if(conductWordNetLookup){
                  Vector v = c.getWordNetWrapper().getReplacementWord(tw.tag(),tw.word(),PointerType.HYPERNYM);
                  allPossibleSubstitutions.addElement(v);
           }
           else{
                  allPossibleSubstitutions.addElement(new Vector()); 
           }
        }
       
       
       
        for(int i=0;i<taggedWords.size();i++){
           TaggedWord tw = (TaggedWord)taggedWords.elementAt(i);
           Vector v = (Vector)allInsWords.elementAt(i);
           Vector v3 = ((Vector)allPossibleSubstitutions.elementAt(i));
           //js.print(i+": "+tw.word()+"---");
           if(v.size()==0){
             //  js.print("Couldn't find word: "+v3.size());
           }
           else{
             //  js.print(StringOfDocChangeInserts.getSubSequenceString(v)+": ");
           }
           //js.print("WORDNET: ");
           for(int j=0;j<v3.size();j++){   
               String s4 = (String)v3.elementAt(j);
             //  js.print(s4+"||||");
           }
           //js.println("");
        }
        
       /*
        Vector allPossibleSubstitutions = new Vector();
        
        for(int i=0;i<taggedWords.size();i++){
           TaggedWord tw = (TaggedWord)taggedWords.elementAt(i);
           Vector vInsWords = (Vector)allInsWords.elementAt(i);
           boolean conductWordNetLookup = true; // To save processing time: Check if word in vector exists, check that word has not already been printed
           if(vInsWords==null){
              conductWordNetLookup = false;
           }
           else if (vInsWords.size()<3){ //Two letter words cause all kinds of problems with wordnet
               conductWordNetLookup = false;
           }
           //else if(!chOut.checkSequenceIsContinuousAndNotAlreadySentOrAlreadyChanged(vInsWords)) conductWordNetLookup = false; //
           
           if(conductWordNetLookup){
                  Vector v = c.getWordNetWrapper().getReplacementWord(tw.tag(),tw.word(),PointerType.HYPERNYM);
                  allPossibleSubstitutions.addElement(v);
           }
           else{
                  allPossibleSubstitutions.addElement(new Vector()); 
           }
        }
       
        for(int i=0;i<taggedWords.size();i++){
           TaggedWord tw = (TaggedWord)taggedWords.elementAt(i);
           Vector v = (Vector)allInsWords.elementAt(i);
           Vector v3 = ((Vector)allPossibleSubstitutions.elementAt(i));
           js.print(i+": "+tw.word()+"---");
           if(v.size()==0){
               js.print("Couldn't find word: "+v3.size());
           }
           else{
               js.print(StringOfDocChangeInserts.getSubSequenceString(v)+": ");
           }
           js.print("WORDNET: ");
           for(int j=0;j<v3.size();j++){   
               String s4 = (String)v3.elementAt(j);
               js.print(s4+"||||");
           }
           js.println("");
        }
        
        
        
        
        
         /*
         
         for(int i=0;i<taggedWords.size();i++){
           TaggedWord tw = (TaggedWord)taggedWords.elementAt(i);
           Vector v = (Vector)allInsWords.elementAt(i);
           Vector v3 = ((Vector)allPossibleSubstitutions.elementAt(i));
           if(v3.size()!=0){
                String textToSubstitute = (String)v3.elementAt(0);
                Vector replacementIns = StringOfDocChangeInserts.getInsEquivalentOfString(textToSubstitute+" ");
                chOut.i3_replaceSequenceWithSequenceChangingTimestampOfEnsuingSequenceUsingOldTurnAsBasisFortypingTime(v,replacementIns);
           }
         }
        
        
        if(taggedWords.size()>10)System.exit(-1); */
        //Filter out the possible substitutions that have already occurred and can't be replaced'
        //The index is already given but not used: indexOfUnsentChanges
        //chOut.i3_insertChangesAt(vToAdd,indexOfUnsentChanges);
    }

   

}


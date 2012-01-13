package diet.server.ConversationController;
import java.util.Arrays;
import java.util.Date;
import java.util.Random;
import java.util.Vector;

import diet.client.WYSIWYGDocumentWithEnforcedTurntaking;
import diet.message.MessageChatTextFromClient;
import diet.message.MessageKeypressed;
import diet.message.MessageWYSIWYGDocumentSyncFromClientInsert;
import diet.message.MessageWYSIWYGDocumentSyncFromClientRemove;
import diet.message.MessageWYSIWYGTextSelectionFromClient;
import diet.message.MessageWYSIWYGTypingUnhinderedRequest;
import diet.parameters.ExperimentSettings;
import diet.parameters.IntParameter;
import diet.parameters.StringParameterFixed;
import diet.server.Conversation;
import diet.server.DocChangesOutgoingSequenceFIFO;
import diet.server.Participant;
import diet.server.StringOfDocChangeInserts;
import diet.server.CbyC.DocChange;
import diet.server.CbyC.DocInsert;
import diet.server.CbyC.DocRemove;
import diet.textmanipulationmodules.TypoGenerator.TypoGenerator;
import diet.utils.VectorToolkit;
import edu.gwu.wordnet.PointerType;
import edu.stanford.nlp.ling.TaggedWord;
import edu.stanford.nlp.trees.Tree;

public class CCSPLITUTTNONSBAR extends DefaultConversationController{
  
    
      
    private DocChangesOutgoingSequenceFIFO chOut = new DocChangesOutgoingSequenceFIFO(null,10,new Date().getTime()); //THIS WILL BE RESET ON FIRST RECEIPT OF TURN
    
    IntParameter pCountdownToIntervention=new IntParameter("Turns till Next intervention",10,10);
    IntParameter pCountdownToInterventionMax=new IntParameter("Turns between interventions",15,15);
    IntParameter parserTimeOut = new IntParameter("Parser Timeout",500,1000);
    IntParameter maxLengthOfParseAttempt = new IntParameter("Parser Max Length (chars)",70,70);
    IntParameter minDelayBetweenTurns = new IntParameter("Min inter-turn delay",250,250);
    IntParameter maxDelayBetweenTurns = new IntParameter("Max inter-turn delay",600,600);
    IntParameter avgTypingTimePerChar = new IntParameter("Avg typing time per char",20,20);
    StringParameterFixed splitIntervention = new StringParameterFixed();

   
    
    public void initialize(Conversation c,ExperimentSettings expSettings){
        super.initialize(c, expSettings);
        Vector v = new Vector();
        v.addElement("yes");
        v.addElement("no");
        StringParameterFixed spf = new StringParameterFixed("Display parse tree","no",v,"no");
        expSettings.addParameter(spf);
        
        Vector vI = new Vector();
        vI.addElement("AB");
        vI.addElement("BA");
        vI.addElement("BB");
        splitIntervention = new StringParameterFixed("Split Intervention", 0, vI, 0);
        expSettings.addParameter(splitIntervention);
        
        expSettings.addParameter(minDelayBetweenTurns);
        expSettings.addParameter(maxDelayBetweenTurns);
        expSettings.addParameter(avgTypingTimePerChar);
        
        Vector vThingstoBlock = new Vector();
        String[] vListToBlock = {"I "," I "," I\n"," me\n","i "," i "," i\n","my"," my "," my\n","mine"," mine "," mine\n","me "," me "," me\n"};
        vThingstoBlock = new Vector(Arrays.asList(vListToBlock));
        
    }
    
    @Override
    public void processLoop(){
        if(pCountdownToIntervention.getValue()<=0){
            
        }else{
            c.resetFromIsTypingtoNormalChatAllAllowedParticipants(super.getIsTypingTimeOut());
       
        }
    }
    
    Participant previousRecipient2 = null;
    Participant previousPreviousRecipient2 = null;
    Participant OriginFirstHalf = null;
    Participant OriginSecondHalf = null;
   
    @Override
    public void processChatText(Participant sender,MessageChatTextFromClient mct){    
        Vector v2 = new Vector();
        Tree tt = null;
        String ToSplit = null;
        Vector split = null;
        Vector split1 = new Vector();
        Vector allParticipants = c.getParticipants().getAllParticipants();
        Vector allOthers = c.getParticipants().getAllOtherParticipants(sender);
        Participant recipient1 = (Participant)allOthers.elementAt(0);
        Participant recipient2 = (Participant)allOthers.elementAt(1);
        int q1 = 0;
        int i = 0;
        Random WhichIntervention = new Random(); 
        int randomIndex = WhichIntervention.nextInt(3);
        splitIntervention.setValue(splitIntervention.getPermittedValues().elementAt(randomIndex));
        expSettings.generateParameterEvent(splitIntervention);
        
        if (pCountdownToIntervention.getValue()<=0&&mct.getText().length()>=15){
            ToSplit = mct.getText();
            q1 = mct.getText().length();
            q1 = q1/2;
            int index = ToSplit.indexOf(" ", q1); 
            split1.addElement(ToSplit.substring(0, index));
            split1.addElement(ToSplit.substring(index+1, ToSplit.length()));
            split = split1;
        }else if(pCountdownToIntervention.getValue()<=0){
            c.informParticipantBthatParticipantAIsTyping(sender,recipient1);
            c.informParticipantBthatParticipantAIsTyping(sender,recipient2);
            try{Thread.sleep(minDelayBetweenTurns.getValue()+r.nextInt(maxDelayBetweenTurns.getValue()-minDelayBetweenTurns.getValue())+q1*this.avgTypingTimePerChar.getValue());}catch(Exception e){}
        }else{
            pCountdownToIntervention.setValue(pCountdownToIntervention.getValue()-1);
            expSettings.generateParameterEvent(pCountdownToIntervention);
        }
        
        if(split!=null){    

             c.setNewTurnBeingConstructedNotRelayingOldTurnAddingOldTurnToHistory(sender,mct);
             
             Vector recipients = new Vector();
             recipients.addElement(recipient1);
             recipients.addElement(recipient2);
 
             //this is the bit that makes sure that the recipients cycle
             //in SBAR all from one person were being split to same one (due to ordering of allOthers)
             if(recipient2.equals(previousRecipient2)){
                 if(sender.equals(previousPreviousRecipient2)||previousPreviousRecipient2==null){
                    recipient2 = recipient1;
                    recipient1 = previousRecipient2;
                 }
             }else if(recipient2.equals(previousPreviousRecipient2)){
                 if(sender.equals(previousRecipient2)){
                    recipient2 = recipient1;
                    recipient1 = previousPreviousRecipient2;
                 }
             }            
             if(previousRecipient2!=null&&previousPreviousRecipient2!=null){
                 if(sender!=previousRecipient2&&sender!=previousPreviousRecipient2){
                     c.informIsTypingToAllowedParticipants(sender);
                     try{Thread.sleep(minDelayBetweenTurns.getValue()+r.nextInt(maxDelayBetweenTurns.getValue()-minDelayBetweenTurns.getValue())+q1*this.avgTypingTimePerChar.getValue());}catch(Exception e){}
                     c.sendLabelDisplayToAllowedParticipantsFromApparentOrigin(sender,"Status: OK",false);
                     c.relayTurnToAllOtherParticipants(sender,mct);
                     c.resetFromIsTypingtoNormalChatAllAllowedParticipants(super.getIsTypingTimeOut());
                     
                    return;             
                 }
             }
 
             String splitUtt = (String)split.elementAt(1);
             
             
             if(splitIntervention.getValue() == "AB"){
                OriginFirstHalf = sender;
             }else{//i.e. 1st half origin = fake                 
                OriginFirstHalf = recipient1;
             }
             if(splitIntervention.getValue() == "BA"){
                OriginSecondHalf = sender;
             }else{//i.e. 2nd half origin = fake
                OriginSecondHalf = recipient1;
             }

             //this is the bit that does all the fakery...
             c.informParticipantBthatParticipantAIsTyping(sender,recipient1);
             c.informParticipantBthatParticipantAIsTyping(OriginFirstHalf,recipient2);
             try{Thread.sleep(minDelayBetweenTurns.getValue()+r.nextInt(maxDelayBetweenTurns.getValue()-minDelayBetweenTurns.getValue())+splitUtt.length()*this.avgTypingTimePerChar.getValue());}catch(Exception e){}
             c.sendArtificialTurnFromApparentOriginToRecipient(sender,recipient1,(String)split.elementAt(0));
             c.sendArtificialTurnFromApparentOriginToRecipient(OriginFirstHalf,recipient2,(String)split.elementAt(0));
             c.informParticipantBthatParticipantAIsNotTypingAndStatusIsOK(sender,recipient1);
             c.informParticipantBthatParticipantAIsNotTypingAndStatusIsOK(OriginFirstHalf,recipient2);
             try{Thread.sleep(minDelayBetweenTurns.getValue()+r.nextInt(maxDelayBetweenTurns.getValue()-minDelayBetweenTurns.getValue()));}catch(Exception e){}
             c.informParticipantBthatParticipantAIsTyping(sender,recipient1);
             c.informParticipantBthatParticipantAIsTyping(OriginSecondHalf,recipient2);
             try{Thread.sleep(minDelayBetweenTurns.getValue()+r.nextInt(maxDelayBetweenTurns.getValue()-minDelayBetweenTurns.getValue())+splitUtt.length()*this.avgTypingTimePerChar.getValue()*2);}catch(Exception e){}
             c.sendArtificialTurnFromApparentOriginToRecipient(sender,recipient1,(String)split.elementAt(1));
             c.sendArtificialTurnFromApparentOriginToRecipient(OriginSecondHalf,recipient2,(String)split.elementAt(1));
             c.informParticipantBthatParticipantAIsNotTypingAndStatusIsOK(sender,recipient1);
             c.informParticipantBthatParticipantAIsNotTypingAndStatusIsOK(OriginSecondHalf,recipient2);
               
             previousPreviousRecipient2 = previousRecipient2;
             previousRecipient2 = recipient2;
                     
             pCountdownToIntervention.setValue(pCountdownToInterventionMax.getValue());
             expSettings.generateParameterEvent(pCountdownToIntervention);
             c.resetFromIsTypingtoNormalChatAllAllowedParticipants(super.getIsTypingTimeOut());
             }     
        
        else{
           c.sendLabelDisplayToAllowedParticipantsFromApparentOrigin(sender,"Status: OK",false);
           c.relayTurnToAllOtherParticipants(sender,mct);
           c.resetFromIsTypingtoNormalChatAllAllowedParticipants(super.getIsTypingTimeOut());
       }
       
    }
    
    @Override
    public void processKeyPress(Participant sender,MessageKeypressed mkp){
        //c.informIsTypingToAllowedParticipants(sender);//,mkp);
    }
    
    @Override
    public void processWYSIWYGTextInserted(Participant sender,MessageWYSIWYGDocumentSyncFromClientInsert mWYSIWYGkp){
        //need to sort out istyping
        if(pCountdownToIntervention.getValue()<=0){
             
        }else{ 
            c.informIsTypingToAllowedParticipants(sender);
        }
            
        if(mWYSIWYGkp.getTextToAppendToWindow().endsWith("\n")){
            mostRecentTurnEndedWithNewLine = true;
        }
        else{
            mostRecentTurnEndedWithNewLine =false;
        }
        chOut.addMessage(sender,mWYSIWYGkp);
       
    }
    
    @Override
    public void processWYSIWYGTextRemoved(Participant sender,MessageWYSIWYGDocumentSyncFromClientRemove mWYSIWYGkp){
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
              chOut = new DocChangesOutgoingSequenceFIFO(sender,10,new Date().getTime());
              if(priorTypist!=null){
                  c.wYSIWYGSetNewDocChangesIncomingSavingBothChangesTypedAndModifiedOutgoingQueue(priorTypist,mWTUR.getTimeStamp().getTime(),chOutOLD);
              }
              priorTypist=sender;
              isFirstTurn =false;
            }
            else if(sender!=priorTypist&mostRecentTurnEndedWithNewLine&mWTUR.getOffsetFrmRight()==0&!mWTUR.getText().equals("")){ 
              c.sendWYSIWYGChangeInterceptionStatusToAllowedParticipants(sender,WYSIWYGDocumentWithEnforcedTurntaking.othertyping,"\n< "+typist.getUsername()+" >\n");
              c.sendWYSIWYGChangeInterceptionStatusToSingleParticipant(sender,WYSIWYGDocumentWithEnforcedTurntaking.typingunhindered,"\n< "+typist.getUsername()+" >\n");
              c.sendLabelDisplayToAllowedParticipantsFromApparentOrigin(typist,typist.getUsername()+"....is typing",true);
              DocChangesOutgoingSequenceFIFO chOutOLD = chOut;
              chOut = new DocChangesOutgoingSequenceFIFO(sender,10,new Date().getTime());
              if(priorTypist!=null){
                  c.wYSIWYGSetNewDocChangesIncomingSavingBothChangesTypedAndModifiedOutgoingQueue(priorTypist,mWTUR.getTimeStamp().getTime(),chOutOLD);
              }
              priorTypist=sender;
            }
            else if(sender!=priorTypist&!mostRecentTurnEndedWithNewLine&mWTUR.getOffsetFrmRight()==0&!mWTUR.getText().equals("")){
              c.sendWYSIWYGChangeInterceptionStatusToAllowedParticipants(sender,WYSIWYGDocumentWithEnforcedTurntaking.othertyping,"\n\n< "+typist.getUsername()+" >\n");
              c.sendWYSIWYGChangeInterceptionStatusToSingleParticipant(sender,WYSIWYGDocumentWithEnforcedTurntaking.typingunhindered,"\n\n< "+typist.getUsername()+" >\n");
              c.sendLabelDisplayToAllowedParticipantsFromApparentOrigin(typist,typist.getUsername()+"....is typing",true);
              DocChangesOutgoingSequenceFIFO chOutOLD = chOut;
              chOut = new DocChangesOutgoingSequenceFIFO(sender,10,new Date().getTime());
               if(priorTypist!=null){
                  c.wYSIWYGSetNewDocChangesIncomingSavingBothChangesTypedAndModifiedOutgoingQueue(priorTypist,mWTUR.getTimeStamp().getTime(),chOutOLD);
               }
              priorTypist=sender;
            }
            else{
                //sender has regained the floor, is continuing with conversation
                //Or the sender is editing a previous part of text and hence no prefix is required
                if(priorTypist!=sender){ //If another participant starts editing a preceding part of turn
                    DocChangesOutgoingSequenceFIFO chOutOLD = chOut;
                    c.wYSIWYGSetNewDocChangesIncomingSavingBothChangesTypedAndModifiedOutgoingQueue(priorTypist,mWTUR.getTimeStamp().getTime(),chOutOLD);
                    chOut = new DocChangesOutgoingSequenceFIFO(sender,10,new Date().getTime());
                    priorTypist = sender;
                }
                c.sendWYSIWYGChangeInterceptionStatusToAllowedParticipants(sender,WYSIWYGDocumentWithEnforcedTurntaking.othertyping,"");
                c.sendWYSIWYGChangeInterceptionStatusToSingleParticipant(sender,WYSIWYGDocumentWithEnforcedTurntaking.typingunhindered,"");
                c.sendLabelDisplayToAllowedParticipantsFromApparentOrigin(typist,typist.getUsername()+"....is typing",true);
            }       
        }
    }

   
    private void processLoopIntercept(){
        if(typist!=null){
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
            chOut.setRelayEnabled(false);
            StringOfDocChangeInserts entiresequence= chOut.getStringOfDocChangeInserts();
            String entiresequenceasstring = entiresequence.getString();
            
            Vector v2 = c.getHistory().getParserWrapper().parseText(entiresequence.getString());
            Vector taggedWords = (Vector)v2.elementAt(0);

            int indexInSodciStringOfNextWordCandidate = 0; 
            Vector allInsWords = new Vector();
            for(int i=0;i<taggedWords.size();i++){
                TaggedWord tw = (TaggedWord)taggedWords.elementAt(i);
                Vector insWord = new Vector();
                int beginIndex = entiresequenceasstring.indexOf(tw.word(),indexInSodciStringOfNextWordCandidate);
                if(beginIndex>=indexInSodciStringOfNextWordCandidate){ //If it is found in the string
                
                int finishIndex = beginIndex+tw.word().length();
                Vector v3 = entiresequence.getSubSequence(beginIndex,finishIndex);
                    if(!StringOfDocChangeInserts.getSubSequenceString(insWord).equalsIgnoreCase(tw.word())){
                        insWord = v3; //To check that the words are actually equal
                        indexInSodciStringOfNextWordCandidate = finishIndex;//  (or plus one)
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
            else if(!chOut.checkSequenceIsContinuousAndNotAlreadySentOrAlreadyChanged(vInsWords)) conductWordNetLookup = false; //
            else if (i==taggedWords.size()-1 && !chOut.getLastCharacterIsSpaceOrNewLine()){
                conductWordNetLookup = false;//This is to prevent incomplete turns from being parsed
               //Strictly speaking, parsing should only be attempted on word boundaries
               //to save time
            }       
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
           for(int j=0;j<v3.size();j++){   
               String s4 = (String)v3.elementAt(j);
           }
       }
       
       for(int i=0;i<taggedWords.size();i++){
           TaggedWord tw = (TaggedWord)taggedWords.elementAt(i);
           Vector v = (Vector)allInsWords.elementAt(i);
           Vector v3 = ((Vector)allPossibleSubstitutions.elementAt(i));
           if(v3.size()!=0){
                Vector replacementIns = StringOfDocChangeInserts.getInsEquivalentOfString("COW");
                replacementIns.addElement(new DocRemove(1,1));
                replacementIns.addElement(new DocRemove(1,1));
                replacementIns.addElement(new DocRemove(1,1));
                replacementIns.addElement(new DocInsert(0,"u",null));
                replacementIns.addElement(new DocInsert(0,"m",null));
                replacementIns.addElement(new DocInsert(0,"m",null));
                
                System.out.println(chOut.displayInfo());
               
                String newSubstitution = c.getWordNetWrapper().getSingleWordFromList(v3);
                Object[] repl = {newSubstitution,newSubstitution.length(), "...sorry...um.. "};
                int r2 = r.nextInt(4);
                if(r2==0){
                    repl  = new Object[]{newSubstitution,newSubstitution.length(), "...uhh..."};
                }
                else if(r2==1){
                    repl  = new Object[]{newSubstitution,newSubstitution.length(), "...scuse me..um... "};
                }
                else if(r2==2){
                    repl  = new Object[]{newSubstitution,newSubstitution.length(), "...sorry..I..meant.."};
                }    
                Vector vRepl =new TypoGenerator().createInsertRemoveSequence(repl,5);           
                replacementIns = VectorToolkit.appendVector2ToVector1(vRepl,v);
                chOut.replaceSequenceWithSequenceChangingTimestampOfEnsuingSequenceUsingOldTurnAsBasisFortypingTime(v,replacementIns);
                //Substitution done
                chOut.setRelayEnabled(true);
                return;
           }
         }
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
           for(int j=0;j<v3.size();j++){   
               String s4 = (String)v3.elementAt(j);
           }
        }
    }
}

package diet.server.ConversationController;
import java.util.Arrays;
import java.util.Date;
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




public class CCSBAR extends DefaultConversationController{
    
       
    
    
    private DocChangesOutgoingSequenceFIFO chOut = new DocChangesOutgoingSequenceFIFO(null,10,new Date().getTime()); //THIS WILL BE RESET ON FIRST RECEIPT OF TURN
    
    IntParameter pCountdownToIntervention;//=new IntParameter("Turns till Next intervention",0,10);
    IntParameter pCountdownToInterventionMax;//=new IntParameter("Turns between interventions",10,20);
    IntParameter parserTimeOut;// = new IntParameter("Parser Timeout",500,1000);
    IntParameter maxLengthOfParseAttempt;// = new IntParameter("Parser Max Length (chars)",70,70);
    IntParameter minDelayBetweenTurns;// = new IntParameter("Min inter-turn delay",250,250);
    IntParameter maxDelayBetweenTurns;// = new IntParameter("Max inter-turn delay",600,600);
    IntParameter avgTypingTimePerChar;// = new IntParameter("Avg typing time per char",20,20);
    
    boolean isProcessingIntervention = false;

    
      
    public void initialize(Conversation c,ExperimentSettings expSettings){
        super.initialize(c,expSettings);
        pCountdownToIntervention = (IntParameter)expSettings.getParameter("Turns till Next intervention");      pCountdownToInterventionMax=new IntParameter("Turns between interventions",10,20);
        pCountdownToInterventionMax = (IntParameter)expSettings.getParameter("Turns between interventions");
        parserTimeOut = (IntParameter)expSettings.getParameter("Parser Timeout");
        maxLengthOfParseAttempt = (IntParameter)expSettings.getParameter("Parser Max Length (chars)");
        minDelayBetweenTurns = (IntParameter)expSettings.getParameter("Min inter-turn delay");
        maxDelayBetweenTurns = (IntParameter)expSettings.getParameter("Max inter-turn delay");
        avgTypingTimePerChar = (IntParameter)expSettings.getParameter("Avg typing time per char");
        
     
        Vector v = new Vector();
        v.addElement("yes");
        v.addElement("no");
        StringParameterFixed spf = new StringParameterFixed("Display parse tree","no",v,"no");
                
        Vector vThingstoBlock = new Vector();
        String[] vListToBlock = {"I "," I "," I\n"," me\n","i "," i "," i\n","my"," my "," my\n","mine"," mine "," mine\n","me "," me "," me\n"};
        vThingstoBlock = new Vector(Arrays.asList(vListToBlock));  
    }
    
    @Override
    public void processLoop(){
            if(!isProcessingIntervention)c.resetFromIsTypingtoNormalChatAllAllowedParticipants(super.getIsTypingTimeOut());
    
    }

    //int countdownToIntervention = 0;
    //int countdownToInterventionMax = 0;
    
    @Override 
    public void processChatText(Participant sender,MessageChatTextFromClient mct){    
      
        Vector v2 = new Vector();
        Tree tt = null;
        Vector split = null;
        
        
        if (pCountdownToIntervention.getValue()<=0&&mct.getText().length()<this.maxLengthOfParseAttempt.getValue()){
                
            v2 = c.getHistory().getParserWrapper().parseTextTimeOut(mct.getText(),parserTimeOut.getValue());
            tt = (Tree)v2.elementAt(1);
            split = c.getHistory().getParserWrapper().splitIntoSBAR(tt);  
        }
        else{
            pCountdownToIntervention.setValue(pCountdownToIntervention.getValue()-1);
            expSettings.generateParameterEvent(pCountdownToIntervention);
        }
        
        
        
        if(split!=null){    
              String secondPart = (String)split.elementAt(1);     
             if(secondPart.indexOf("I ")==0
                      ||secondPart.indexOf(" I ")>=0 
                      || secondPart.indexOf(" I\n")>=0 
                      || secondPart.indexOf(" me\n")>=0
                      ||secondPart.indexOf("i ")==0
                      ||secondPart.indexOf(" i ")>=0 
                      ||secondPart.indexOf(" i\n")>=0
                      || secondPart.indexOf("my")==0 
                      ||secondPart.indexOf(" my ")>=0 
                      ||secondPart.indexOf(" my\n")>=0
                      || secondPart.indexOf("mine")==0 
                      ||secondPart.indexOf(" mine ")>=0 
                      ||secondPart.indexOf(" mine\n")>=0){
                     
                  c.relayTurnToAllOtherParticipants(sender,mct);
                  c.sendLabelDisplayToAllowedParticipantsFromApparentOrigin(sender,"Status: OK",false);
                  return;                 
             }
             
             isProcessingIntervention=true;
              
             Vector allOthers = c.getParticipants().getAllOtherParticipants(sender);
             Participant recipient1 = (Participant)allOthers.elementAt(0);
             Participant recipient2 = (Participant)allOthers.elementAt(1);
             c.setNewTurnBeingConstructedNotRelayingOldTurnAddingOldTurnToHistory(sender,mct);
             
             //c.sendArtificialTurnFromApparentOriginToRecipient(sender,recipient1,(String)split.elementAt(0));
             //c.sendArtificialTurnFromApparentOriginToRecipient(sender,recipient2,(String)split.elementAt(0));
             Vector recipients = new Vector();
             recipients.addElement(recipient1);
             recipients.addElement(recipient2);
             c.sendArtificialTurnFromApparentOriginToRecipients(sender,recipients,(String)split.elementAt(0));
             
             //c.informIsTypingToAllowedParticipants(sender);
             //c.informIsTypingToAllowedParticipants(recipient1);
             //c.informParticipantBthatParticipantAIsNotTypingAndStatusIsOK(sender,recipient1);
             c.informParticipantBthatParticipantAIsNotTypingAndStatusIsOK(sender,recipient2);
             
             long sleepval = minDelayBetweenTurns.getValue()+r.nextInt(maxDelayBetweenTurns.getValue()-minDelayBetweenTurns.getValue());
             System.err.println("SLEEPING: "+minDelayBetweenTurns.getValue());
             System.err.println("MINDELAYBETWEENTURNS: "+maxDelayBetweenTurns.getValue());
             System.err.println("MAXDELAYBETWEENTURNS");
             System.err.println("SLEEPING FOR:"+sleepval);
             
            
             
             try{
                 Thread.sleep(sleepval);
             }catch(Exception e){
                 System.err.println("Thread woken up");
             }
             c.informParticipantBthatParticipantAIsTyping(sender,recipient1);
             c.informParticipantBthatParticipantAIsTyping(recipient1,recipient2);
             String splitUtt = (String)split.elementAt(1);
             sleepval = minDelayBetweenTurns.getValue()+r.nextInt(maxDelayBetweenTurns.getValue()-minDelayBetweenTurns.getValue())+splitUtt.length()*this.avgTypingTimePerChar.getValue();
             System.err.println("SECONDSLEEPVAL IS: "+sleepval);
             //try{Thread.sleep(2500 +splitUtt.length()*120);}catch(Exception e){}
             try{
                 Thread.sleep(5000+sleepval);
             }catch(Exception e){
             
             }
             c.sendArtificialTurnFromApparentOriginToRecipient(sender,recipient1,(String)split.elementAt(1));
             c.sendArtificialTurnFromApparentOriginToRecipient(recipient1,recipient2,(String)split.elementAt(1));
             c.informParticipantBthatParticipantAIsNotTypingAndStatusIsOK(sender,recipient1);
             c.informParticipantBthatParticipantAIsNotTypingAndStatusIsOK(recipient1,recipient2);
             
             pCountdownToIntervention.setValue(pCountdownToInterventionMax.getValue());
             expSettings.generateParameterEvent(pCountdownToIntervention);
             isProcessingIntervention=false;
             
        }
        else{
           c.relayTurnToAllOtherParticipants(sender,mct);
           //c.sendLabelDisplayToAllowedParticipantsFromApparentOrigin(sender,"Status: OK",false);
       }
       
    }
    
    @Override
    public void processKeyPress(Participant sender,MessageKeypressed mkp){
        //c.informIsTypingToAllowedParticipants(sender);//,mkp);
    }
    
    
    
    
    
    @Override
    public void processWYSIWYGTextInserted(Participant sender,MessageWYSIWYGDocumentSyncFromClientInsert mWYSIWYGkp){
        c.informIsTypingToAllowedParticipants(sender);
        
        if(mWYSIWYGkp.getTextToAppendToWindow().endsWith("\n")){
            mostRecentTurnEndedWithNewLine = true;
        }
        else{
            mostRecentTurnEndedWithNewLine =false;
        }
        //c.relayWYSIWYGTextInsertedToAllchOut.addInsert(sender,mWYSIWYGkp);owedParticipants(sender,mWYSIWYGkp);
        chOut.addMessage(sender,mWYSIWYGkp);
       
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
              chOut = new DocChangesOutgoingSequenceFIFO(sender,10,new Date().getTime());
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
              chOut = new DocChangesOutgoingSequenceFIFO(sender,10,new Date().getTime());
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
              chOut = new DocChangesOutgoingSequenceFIFO(sender,10,new Date().getTime());
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
                    chOut = new DocChangesOutgoingSequenceFIFO(sender,10,new Date().getTime());
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
        
        
        
        chOut.setRelayEnabled(false);
        StringOfDocChangeInserts entiresequence= chOut.getStringOfDocChangeInserts();
        String entiresequenceasstring = entiresequence.getString();
        //Vector entiresequenceParsed = c.getHistory().getParserWrapper().parseText(entiresequenceasstring);
        
        Vector v2 = c.getHistory().getParserWrapper().parseText(entiresequence.getString());
        Vector taggedWords = (Vector)v2.elementAt(0);
        
        
        
        
        
        //--------------------------------------------------------------------------------------------
       
       int indexInSodciStringOfNextWordCandidate = 0; 
       Vector allInsWords = new Vector();
       for(int i=0;i<taggedWords.size();i++){
            TaggedWord tw = (TaggedWord)taggedWords.elementAt(i);
            Vector insWord = new Vector();
            int beginIndex = entiresequenceasstring.indexOf(tw.word(),indexInSodciStringOfNextWordCandidate);
            if(beginIndex>=indexInSodciStringOfNextWordCandidate){ //If it is found in the string
                
                int finishIndex = beginIndex+tw.word().length();
                //System.out.println(i+":  found index at: "+beginIndex+": "+finishIndex+": ");
                Vector v3 = entiresequence.getSubSequence(beginIndex,finishIndex);
                if(!StringOfDocChangeInserts.getSubSequenceString(insWord).equalsIgnoreCase(tw.word())){
                    insWord = v3; //To check that the words are actually equal
                    indexInSodciStringOfNextWordCandidate = finishIndex;//  (or plus one)
                    //js.println()
                }
            }
            allInsWords.addElement(insWord);
       } 
        
       //--------------------------------------------------------------------------- 
       
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
               conductWordNetLookup = false;
               //This is to prevent incomplete turns from being parsed
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
       
       //----------------------------------------------------------------------------------
       
       for(int i=0;i<taggedWords.size();i++){
           TaggedWord tw = (TaggedWord)taggedWords.elementAt(i);
           Vector v = (Vector)allInsWords.elementAt(i);
           Vector v3 = ((Vector)allPossibleSubstitutions.elementAt(i));
           //js.print(i+": "+tw.word()+"---");
           if(v.size()==0){
             //  js.print("Couldn't find word: "+v3.size());
           }
           else{
               //js.print(StringOfDocChangeInserts.getSubSequenceString(v)+": ");
           }
           //js.print("WORDNET: ");
           for(int j=0;j<v3.size();j++){   
               String s4 = (String)v3.elementAt(j);
             //  js.print(s4+"||||");
           }
           //js.println("");
        }
       
       //--------------------------------------------------------------------------------------
       
       for(int i=0;i<taggedWords.size();i++){
           TaggedWord tw = (TaggedWord)taggedWords.elementAt(i);
           Vector v = (Vector)allInsWords.elementAt(i);
           Vector v3 = ((Vector)allPossibleSubstitutions.elementAt(i));
           if(v3.size()!=0){
                //String textToSubstitute = (String)v3.elementAt(0);
                //Vector replacementIns = StringOfDocChangeInserts.getInsEquivalentOfString(textToSubstitute+" ");
               
                Vector replacementIns = StringOfDocChangeInserts.getInsEquivalentOfString("COW");
                replacementIns.addElement(new DocRemove(1,1));
                replacementIns.addElement(new DocRemove(1,1));
                replacementIns.addElement(new DocRemove(1,1));
                replacementIns.addElement(new DocInsert(0,"u",null));
                replacementIns.addElement(new DocInsert(0,"m",null));
                replacementIns.addElement(new DocInsert(0,"m",null));
                
                //Object[] repl = {v.size(),"SORRY"};
                //replacementIns = diet.utils.VectorToolkit.appendVector2ToVector1(v,new TypoGenerator().createInsertRemoveSequence(repl,5));
                
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
       
         //---------------------------------------------------------------------------
       
       
       
       
       /*
        
        if(chOut.checkSequenceIsContinuousAndNotAlreadySentOrAlreadyChanged(vToBeRemoved)){
            js.println("Replacing sequence");
            chOut.replaceSequenceWithSequenceChangingTimestampOfEnsuingSequenceUsingOldTurnAsBasisFortypingTime(vToBeRemoved,vToBeAdded);
        }
        else{
            js.println("Not replacing sequence");
        }
         */
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

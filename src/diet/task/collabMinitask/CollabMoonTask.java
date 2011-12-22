/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package diet.task.collabMinitask;

import diet.debug.Debug;
import diet.message.MessageChatTextFromClient;
import diet.server.Conversation;
import diet.server.Participant;
import java.io.Serializable;
import java.lang.Exception;
import java.util.Hashtable;
import java.util.Random;
import java.util.Vector;

/**
 *
 * @author sre
 */
public class CollabMoonTask  extends CollabMiniTask implements Serializable{

    //String ipDDR = "http://"+this.getIPAddress();
    //String ipADDRESS = "http://"+this.getIPAddress()+"/01.txt";
    //String webADDRESS ;
    //String taskDescAddress;

    static Hashtable allScores = new Hashtable();
    static Hashtable biggestSuccSoFar = new Hashtable();

    String prefixA = "You are on the moon (Task1: Moon Task)\nThe objects you can see are:\n\n";
    String prefixB = "You are on the moon (Task1: Moon Task)\nThe objects you can see are:\n\n";

    String textForA ;
    String textForB ;

    Participant participant1;
    Participant participant2;

    Vector allObjects = new Vector();

    Vector currentSet = new Vector();
    Vector currentSetSorted = new Vector();
    Vector currentSetA = new Vector();
    Vector currentSetB = new Vector();


    DataBase db;

   
    Conversation c;
    Random r = new Random();
    //public int numberOfCorrectNexts = 0;

    public int minSelection = 3;
    public int range =1;

    public int minSelection4Threshold = 10;
    public int minSelection5Threshold = 30;
    public int minSelection6Threshold = 50;
    public int minSelection7Threshold = 80;
    public int minSelection8Threshold = 120;
    public int minSelection9Threshold = 140;
    public int minSelection10Threshold = 150;


    boolean canMoveToNextStep = false;
    boolean allowNoObjects = false;
    public boolean allowOverlap = false;
    public int overlapPB =7;
    public int overlapMinScoreThreshold = 9;


    public boolean deleteAllOnBadSelection = false;
    //boolean deleteOnSelectingOthers = false;
    public boolean deleteAllOnBadNext = true;

   public boolean isCurrentlyMoonTask = true;
   public int isPureAlphabetOnStartPB = 2;
   public int  isPureAlphabetPB = 4;


   public int numberOfAlphabets = 0;
   public int maxnumberofAlphabets = 999;


   

     public CollabMoonTask(Conversation c, boolean secondStage){
           //this.secondStage = false;
           this.isPureAlphabetOnStartPB=1;
           this.isPureAlphabetPB=1;
           this.c=c;
           db = new DataBase(secondStage);
           if(secondStage){
               this.isPureAlphabetOnStartPB=1;
               this.isPureAlphabetPB=1;
           }
     }


      

      


      public void step0_ChooseMoonOrAlphabet(){
          
              this.isCurrentlyMoonTask=false;
              allObjects = db.getRandomAlphabetTask(participant1, participant1);
              prefixA = "Sort the following words in alphabetical order:\n\n";
              prefixB = "Sort the following words in alphabetical order:\n\n";
              this.numberOfAlphabets++;

        
      }


      public void step1_ChooseSetOfObjects(){

        if(getScore(participant1)>overlapMinScoreThreshold && getScore(participant2)>overlapMinScoreThreshold)this.allowOverlap=true;

        int p1score = getScore(participant1);
        int p2score = getScore(participant2);

        int minScore = Math.min(p1score, p2score);

        if(minScore>minSelection4Threshold) range = 2;
        if(minScore>minSelection5Threshold) range = 3;
        if(minScore>minSelection6Threshold) range = 4;
        if(minScore>minSelection7Threshold) range = 5;
        if(minScore>minSelection8Threshold) range = 6;
        if(minScore>minSelection9Threshold) range = 7;
        if(minScore>minSelection10Threshold) range = 8;



        try{
        int nmb = minSelection + r.nextInt(range);

        if(nmb>=allObjects.size())nmb=allObjects.size()-1;

        Vector selection = new Vector();
        Conversation.printWSln("MOONTASK", "SIZE IS"+nmb);
        while(selection.size()<nmb){
            Object o = allObjects.elementAt(r.nextInt(allObjects.size()));
            if(!selection.contains(o)){
                selection.addElement(o);
                Conversation.printWSln("MOONTASK", "ADDING");
            }
        }


        currentSet = db.getJumbled(selection);
        //currentSet = (Vector)selection.clone();
        if(this.isCurrentlyMoonTask){
            currentSetSorted = db.getSortedVector(selection);
           
        }
        else{
            
            currentSetSorted = db.sortVectorAlphabetically(selection);
            System.err.println(currentSetSorted.size());
            
        }
      
        

        

        }catch (Exception e){
              Conversation.printWSln("SEQUENCES", e.toString());
              e.printStackTrace();
        }




    }


    public void step2_PartitionIntoTwoSubSets(){

        this.currentSetA=new Vector();
        this.currentSetB=new Vector();

        for(int i=0;i<currentSet.size();i++){
            Object o = currentSet.elementAt(i);
            int i2 = r.nextInt(2);
            if(i2==0){
                currentSetA.addElement(o);
                if(this.allowOverlap&&r.nextInt(this.overlapPB)==0)currentSetB.addElement(o);
            }
            else{
                currentSetB.addElement(o);
                if(this.allowOverlap&&r.nextInt(this.overlapPB)==0)currentSetA.addElement(o);
            }
            currentSetA = db.getJumbled(currentSetA);
            currentSetB = db.getJumbled(currentSetB);


        }

        if(!this.allowNoObjects){
            if(currentSetA.size()==0){
                Object o = currentSetB.elementAt(r.nextInt(currentSetB.size()));
                currentSetB.removeElement(o);
                currentSetA.addElement(o);
            }
            if(currentSetB.size()==0){
                Object o = currentSetA.elementAt(r.nextInt(currentSetA.size()));
                currentSetA.removeElement(o);
                currentSetB.addElement(o);
            }
        }
    }



    public void step3_SendObjectsToBothParticipants(){
        
        String aScore = "Your score is: "+this.getScore(participant1)+"\n\n";
        String bScore = "Your score is: "+this.getScore(participant2)+"\n\n";

        
        textForA = aScore+this.prefixA +"\n";
        textForB = bScore+this.prefixB +"\n";

        for(int i=0;i<this.currentSetA.size();i++){
           if(this.isCurrentlyMoonTask){
               String[] entry =  (String[])currentSetA.elementAt(i);
               textForA = textForA + entry[0] +"\n";
           }
           else{
                String entry =  (String)currentSetA.elementAt(i);
                textForA = textForA + entry +"\n";
           }
        }
        for(int i=0;i<this.currentSetB.size();i++){
           if(this.isCurrentlyMoonTask){
              String[] entry =  (String[])currentSetB.elementAt(i);
              textForB = textForB + entry[0] +"\n";
           }
           else{
               String entry =  (String)currentSetB.elementAt(i);
               textForB = textForB + entry +"\n";
           }
        }
        System.out.print(participant2.getUsername());
        System.out.print(participant1.getUsername());

        c.changeWebpage(this.participant1, "objectlist","", null, textForA);
        c.changeWebpage(this.participant2, "objectlist","", null, textForB);
        //System.err.println(textForA);
        //System.exit(-5);
        c.saveInfoAsFakeTurnFromServerWithAddresseesAsRecipients(textForA.replace('\n', '-'), participant1);
        c.saveInfoAsFakeTurnFromServerWithAddresseesAsRecipients(textForB.replace('\n', '-'), participant2);
    }



   
    public void participantJoinedConversation(Participant p) {
        //c.displayNEWWebpage(p, "instructions", "Instructions",this.taskDescAddress, 650, 600, false);
        if(participant1!=null&&participant2!=null){
            Conversation.printWSln("Main", "ERROR...trying to add "+p.getUsername()+ " but there are already "+ participant1.getUsername()+" and "+participant2.getUsername());
            return;
        }
        else if (this.participant1==null){
            participant1 = p;
            c.displayNEWWebpage(p, "objectlist", "TASKWINDOW","", 300, 300, false,false);
        }
        else if (this.participant2 == null) {
            participant2 = p;
            c.displayNEWWebpage(p, "objectlist", "TASKWINDOW","", 300, 300, false, false);
        }
        if(participant1!=null& participant2!=null){
            this.step0_ChooseMoonOrAlphabet();
            this.step1_ChooseSetOfObjects();
            this.step2_PartitionIntoTwoSubSets();
            this.step3_SendObjectsToBothParticipants();
            canMoveToNextStep = false;
        }
    }


    public void participantRejoinedConversation(Participant p){
        if (p==participant1||p==participant2){
            //c.displayNEWWebpage(p, "instructions", "Instructions",this.taskDescAddress, 500, 540, false);
            c.displayNEWWebpage(p, "objectlist", "TASKWINDOW","", 300, 300, false,false);

            
        }
        else {
            String namesOFP = ""; if(participant1!=null)namesOFP = namesOFP+participant1.getUsername();if(participant2!=null)namesOFP = namesOFP+"..."+participant2.getUsername();
            Conversation.printWSln("Main", "Error rejoining: "+p.getUsername()+" is being rejoined but participants are "+namesOFP);
            return;
        }
        if(p ==participant1){
            c.changeWebpage(this.participant1, "objectlist","", null, textForA);
            Conversation.printWSln("Main", "Rejoined as A: "+p.getUsername()+"..sending "+textForA);
        }
        else if(p ==participant2){
            c.changeWebpage(this.participant2, "objectlist","", null, textForB);
            Conversation.printWSln("Main", "Rejoined as B: "+p.getUsername()+"..sending "+textForB);
        }
    }

    Vector selSoFar = new Vector();

    public void processCommand(Participant pSender, MessageChatTextFromClient mct){
        if(Debug.debugBLOCKAGE)System.out.println("MCT3");


        String otherUserFeedback="";
        String verboseDiagnostics = "";

        Participant pAddresse = null;
        if(pSender==participant1)pAddresse=participant2;
        if(pSender==participant2)pAddresse=participant1;

        if(mct.getText().startsWith("/")){
            c.setNewTurnBeingConstructedNotRelayingOldTurnAddingOldTurnToHistory_WithAddresseeSavedAsRecipient(pSender, mct, pAddresse);
        }
       if(Debug.debugGROOP)System.err.println("PRCESSING CHATCOMMANDAAAAAA"+mct.getText());
       if(Debug.debugGROOP&&mct.getText().startsWith("/nbv") || mct.getText().startsWith("/NEXTDEBUG")) {
             otherUserFeedback = "****SELECTION CORRECT****NEXT SET OF OBJECTS****HURRY UP!";
             if(5>2&&Debug.debugGROOP)System.err.println("PRCESSING CHATCOMMAND1"+mct.getText());
             //this.numberOfCorrectNexts++;
             CollabMoonTask.biggestSuccSoFar.put(participant1, range);
             CollabMoonTask.biggestSuccSoFar.put(participant2, range);
              if(

              Debug.debugGROOP)System.err.println("PRCESSING CHATCOMMAND2"+mct.getText());
             this.updateScoresSuccess();
              if(Debug.debugGROOP)System.err.println("PRCESSING CHATCOMMAND3"+mct.getText());
             selSoFar.removeAllElements();
              if(Debug.debugGROOP)System.err.println("PRCESSING CHATCOMMAND4"+mct.getText());
             this.step0_ChooseMoonOrAlphabet();
              if(Debug.debugGROOP)System.err.println("PRCESSING CHATCOMMAND5"+mct.getText());
             this.step1_ChooseSetOfObjects();
              if(Debug.debugGROOP)System.err.println("PRCESSING CHATCOMMAND6"+mct.getText());
             this.step2_PartitionIntoTwoSubSets();
              if(Debug.debugGROOP)System.err.println("PRCESSING CHATCOMMAND7"+mct.getText());
             this.step3_SendObjectsToBothParticipants();
              if(Debug.debugGROOP)System.err.println("PRCESSING CHATCOMMAND8"+mct.getText());
             canMoveToNextStep = false;
             
       }
       else if(mct.getText().startsWith("/next") || mct.getText().startsWith("/NEXT"))
        {
                 if(!canMoveToNextStep){
                     if(this.deleteAllOnBadNext){
                         otherUserFeedback =  "****CAN'T GO TO NEXT STEP. SELECTION IS INCORRECT. PLEASE START AGAIN";
                         selSoFar.removeAllElements();
                     }
                     else{
                        otherUserFeedback =  "****CAN'T GO TO NEXT STEP. SELECTION IS INCORRECT";
                     }
                     
                 }
                 else{
                       otherUserFeedback = "****SELECTION CORRECT****NEXT SET OF OBJECTS****HURRY UP!";
                       //this.numberOfCorrectNexts++;
                       CollabMoonTask.biggestSuccSoFar.put(participant1, range);
                       CollabMoonTask.biggestSuccSoFar.put(participant2, range);
                       this.updateScoresSuccess();
                       selSoFar.removeAllElements();
                       this.step0_ChooseMoonOrAlphabet();
                       this.step1_ChooseSetOfObjects();
                       this.step2_PartitionIntoTwoSubSets();
                       this.step3_SendObjectsToBothParticipants();
                       canMoveToNextStep = false;
                 }
        }
        else if(mct.getText().startsWith("/listalloftheitems")){
               String text2 = "";
               for(int i =0;i<selSoFar.size();i++){
                   String[] s = (String[])selSoFar.elementAt(i);
                   text2 = text2+ s[0]+"---"+i+"\n";
               }
                
                otherUserFeedback = "-"+text2;
        }
        else if(mct.getText().startsWith("/restart")){
              selSoFar.removeAllElements();
              otherUserFeedback="*****The list of items is deleted....start again ";
        }
        else if(mct.getText().startsWith("/")){

                 String nam = mct.getText().substring(1);
                 Object o ;


                 o =  db.getObjectWithName(nam, currentSetSorted);


                 if(o==null){
                     otherUserFeedback = ("****OTHER PARTICIPANT TYPED A TYPO");
                 }
                 else if(selSoFar.contains(o))
                 {
                     otherUserFeedback ="****OTHER PARTICIPANT TRIED TO SELECT AN ITEM THAT'S ALREADY BEEN SELECTED";
                 }
                 else if(selSoFar.size() >= currentSetSorted.size()){
                     otherUserFeedback ="****OTHER USER SELECTED TOO MANY ITEMS...RESTARTING";
                     selSoFar=new Vector();
                 }

                 
                 else if(pSender == this.participant1 && currentSetB.contains(o)) {
                     selSoFar.addElement(o);
                     if(db.isASublistOfB(selSoFar, this.currentSetSorted)){
                         verboseDiagnostics = "CONTAINSGOOD1_SUBLIST";
                         if(selSoFar.size()==currentSetSorted.size()){
                               this.canMoveToNextStep=true;
                         }
                     }
                     else  {
                         verboseDiagnostics ="CONTAINSGOOD1_BUTBAD";
                         if(this.deleteAllOnBadSelection){
                             selSoFar = new Vector();
                             otherUserFeedback ="****Other participant made bad selection...deleting list...please start again";
                             verboseDiagnostics = verboseDiagnostics + "_deleting";
                         }
                     }
                 }
                 else if(pSender == this.participant1 && (currentSetA.contains(o)&&!currentSetB.contains(o))){
                     otherUserFeedback = "****OTHER PARTICIPANT TRIED TO SELECT ONE OF YOUR ITEMS INSTEAD OF THEIRS";
                     selSoFar = new Vector();
                 }
                 else if(pSender == this.participant2 && currentSetA.contains(o)){
                     selSoFar.addElement(o);
                     if(db.isASublistOfB(selSoFar, this.currentSetSorted)){
                         verboseDiagnostics = "CONTAINSGOOD2_SUBLIST";

                         if(selSoFar.size()==currentSetSorted.size()){
                             this.canMoveToNextStep=true;
                         }
                     }
                     else {
                         verboseDiagnostics ="CONTAINSGOOD1_BUTBAD";
                         if(this.deleteAllOnBadSelection){
                             selSoFar = new Vector();
                             otherUserFeedback ="****Other participant made bad selection...deleting list...please start again";
                             verboseDiagnostics = verboseDiagnostics + "_deleting";
                         }
                     }
                 }
                 else if(pSender == this.participant2 && currentSetB.contains(o)&&!currentSetA.contains(o)){
                     otherUserFeedback = "****OTHER PARTICIPANT TRIED TO SELECT ONE OF YOUR ITEMS INSTEAD OF THEIRS";
                     selSoFar = new Vector();
                 }

                


    }
     if(Debug.debugGROOP)System.err.println("PRCESSING CHATCOMMANDBBBB"+mct.getText());
     if(otherUserFeedback!=null&&otherUserFeedback.length()>0){
                        if(Debug.debugGROOP)System.err.println("PRCESSING CHATCOMMANDCCCC"+mct.getText());
                       //if(Debug.generateVERBOSELISTOUTPUTFORCOLLABTASK)otherUserFeedback = otherUserFeedback + verboseDiagnostics;
                       if(Debug.debugGROOP)System.err.println("PRCESSING CHATCOMMANDDDDD"+mct.getText());
                        if(pSender==this.participant1){
                            c.sendArtificialTurnFromApparentOriginToRecipient(participant1, participant2,otherUserFeedback);

                           if(Debug.debugGROOP){
                              // mct.setText("DEBUG");
                              // mct.timeStamp();
                              // c.setNewTurnBeingConstructedNotRelayingOldTurnAddingOldTurnToHistory_WithAddresseeSavedAsRecipient(pSender, mct, pAddresse);
                              // c.setNewTurnBeingConstructedNotRelayingOldTurnAddingOldTurnToHistory_WithAddresseeSavedAsRecipient(pSender, mct, pAddresse);
                              // c.setNewTurnBeingConstructedNotRelayingOldTurnAddingOldTurnToHistory_WithAddresseeSavedAsRecipient(pSender, mct, pAddresse);

                           }

                           //
                        }
                        else if(pSender == this.participant2){
                            c.sendArtificialTurnFromApparentOriginToRecipient(participant2, participant1, otherUserFeedback);
                        }
                       if(Debug.debugGROOP)System.err.println("PRCESSING CHATCOMMANDFFFF"+mct.getText());
                       else Conversation.printWSln("Main", "Error trying to sendfakeoutput2 to "+pSender.getUsername());
                 }



}

      public void updateScoresSuccess(){
          Integer p1score = (Integer) allScores.get(this.participant1);
          if(p1score==null){
              allScores.put(participant1, 1);
          }
          else{
              p1score = p1score+this.currentSet.size();
              allScores.put(participant1, p1score);
          }
          Integer p2score = (Integer) allScores.get(this.participant2);
          if(p1score==null){
              allScores.put(participant2, 1);
          }
          else{
              p2score = p2score+this.currentSet.size();
              allScores.put(participant2, p2score);
          }
      }

      

      public int getScore(Participant p){
              Integer pscore = (Integer) allScores.get(p);
              if(pscore==null){
                  allScores.put(p, 0);
                  return 0;
              }
              return pscore;
          }


      public Participant getParticipant1(){
          return this.participant1;
      }
      public Participant getParticipant2(){
          return this.participant2;
      }


     }




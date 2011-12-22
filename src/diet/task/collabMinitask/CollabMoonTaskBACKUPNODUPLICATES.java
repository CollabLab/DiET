/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package diet.task.collabMinitask;

import diet.debug.Debug;
import diet.message.MessageChatTextFromClient;
import diet.server.Conversation;
import diet.server.Participant;
import diet.task.TaskController;
import java.io.Serializable;
import java.util.Random;
import java.util.Vector;

/**
 *
 * @author sre
 */
public class CollabMoonTaskBACKUPNODUPLICATES  extends CollabMiniTask implements Serializable{

    //String ipDDR = "http://"+this.getIPAddress();
    //String ipADDRESS = "http://"+this.getIPAddress()+"/01.txt";
    String webADDRESS ;
    String taskDescAddress;


    String prefixA = "You are on the moon\nThe objects you can see are:\n\n";
    String prefixB = "You are on the moon\nThe objects you can see are:\n\n";

    String textForA ;
    String textForB ;

    Participant participant1;
    Participant participant2;

    Vector allObjects = new Vector();

    Vector currentSet = new Vector();
    Vector currentSetSorted = new Vector();
    Vector currentSetA = new Vector();
    Vector currentSetB = new Vector();


    DataBase db = new DataBase(false);

   
    Conversation c;
    Random r = new Random();

    public int minSelection = 3;
    public int range =1;

    boolean canMoveToNextStep = false;

    boolean allowNoObjects = false;

    boolean allowOverlap = false;

    boolean deleteAllOnBadSelection = false;
    boolean deleteOnSelectingOthers = false;
    boolean deleteAllOnBadNext = true;


    public CollabMoonTaskBACKUPNODUPLICATES(Conversation c, String ipAddress, String taskpage){
        this.c = c;
        this.webADDRESS=ipAddress;
        this.taskDescAddress=webADDRESS+"/"+taskpage;
        initObjects();
    }

 public void initObjects(){
        String[] s1 = {"two 100lbs tanks of OXYGEN",                  "OXYGEN",       "O",  "1"};   allObjects.addElement(s1);
        String[] s2 = {"5 gallons of WATER",                          "WATER",        "W",  "2"};   allObjects.addElement(s2);
        String[] s3 = {"stellar MAP",                                 "MAP",          "M",  "3"};   allObjects.addElement(s3);
        String[] s4 = {"FOOD concentrate",                            "FOOD",         "F",  "4"};   allObjects.addElement(s4);
        String[] s5 = {"solar powered RADIO transmitter and receiver","RADIO",        "R",  "5"};   allObjects.addElement(s5);
        String[] s6 = {"first aid KIT",                               "KIT",          "K",  "6"};   allObjects.addElement(s6);
        String[] s7 = {"PARACHUTE",                                   "PARACHUTE",    "P",  "7"};   allObjects.addElement(s7);
        String[] s8 = {"SIGNAL flares",                               "SIGNAL",       "S",  "8"};   allObjects.addElement(s8);
        String[] s9 = {"two .45 caliber GUNS",                        "GUNS",         "G",  "9"};   allObjects.addElement(s9);
        String[] s10 = {"portable HEATER",                            "HEATER",       "H",  "10"};  allObjects.addElement(s10);
        String[] s11 = {"magnetic COMPASS",                           "COMPASS",      "C",  "11"};  allObjects.addElement(s11);
        String[] s12 = {"cigarette LIGHTER",                          "LIGHTER",      "L",  "12"};  allObjects.addElement(s12);
    }



      public void step1_ChooseSetOfObjects(){

        try{
        int nmb = minSelection + r.nextInt(range);
        Vector selection = new Vector();
        Conversation.printWSln("SEQUENCES", "SIZE IS"+nmb);
        while(selection.size()<nmb){
            Object o = allObjects.elementAt(r.nextInt(allObjects.size()));
            if(!selection.contains(o)){
                selection.addElement(o);
                Conversation.printWSln("SEQUENCES", "ADDING");
            }
        }



        currentSet = db.getJumbled(selection);
        //currentSet = (Vector)selection.clone();
        currentSetSorted = db.getSortedVector(selection);
        String message = "The objects you have are:\n\n";

        for (int i=0;i<selection.size();i++){
             String[] entry =  (String[])selection.elementAt(i);
             message = message + entry[i]+"\n";
        }
        //display(message);

        }catch (Exception e){
              Conversation.printWSln("SEQUENCES", e.toString());
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
                if(this.allowOverlap&&r.nextInt(7)==4)currentSetB.addElement(o);
            }
            else{
                currentSetB.addElement(o);
                if(this.allowOverlap&&r.nextInt(7)==4)currentSetA.addElement(o);
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
        textForA = this.prefixA +"\n";
        textForB = this.prefixB +"\n";

        for(int i=0;i<this.currentSetA.size();i++){
           String[] entry =  (String[])currentSetA.elementAt(i);
           textForA = textForA + entry[0] +"\n";
        }
        for(int i=0;i<this.currentSetB.size();i++){
           String[] entry =  (String[])currentSetB.elementAt(i);
           textForB = textForB + entry[0] +"\n";
        }
        System.out.print(participant2.getUsername());
        System.out.print(participant1.getUsername());

        c.changeWebpage(this.participant1, "objectlist","", null, textForA);
        c.changeWebpage(this.participant2, "objectlist","", null, textForB);
    }



   
    public void participantJoinedConversation(Participant p) {
        c.displayNEWWebpage(p, "instructions", "Instructions",this.taskDescAddress, 650, 600, false,false);
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
            c.displayNEWWebpage(p, "objectlist", "TASKWINDOW","", 300, 300, false,false);
        }
        if(participant1!=null& participant2!=null){
            this.step1_ChooseSetOfObjects();
            this.step2_PartitionIntoTwoSubSets();
            this.step3_SendObjectsToBothParticipants();
            canMoveToNextStep = false;
        }
    }


    public void participantRejoinedConversation(Participant p){
        if (p==participant1||p==participant2){
            c.displayNEWWebpage(p, "instructions", "Instructions",this.taskDescAddress, 500, 540, false,false);
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
         String otherUserFeedback="";
          String verboseDiagnostics = "";
        if(mct.getText().startsWith("/")){
            c.setNewTurnBeingConstructedNotRelayingOldTurnAddingOldTurnToHistory(pSender, mct);
        }
        if(mct.getText().startsWith("/next")|| mct.getText().startsWith("/NEXT")){
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

                       selSoFar.removeAllElements();
                       this.step1_ChooseSetOfObjects();
                       this.step2_PartitionIntoTwoSubSets();
                       this.step3_SendObjectsToBothParticipants();
                       canMoveToNextStep = false;
                 }
        }
        else if(mct.getText().startsWith("/listall")){
               String text2 = "";
               for(int i =0;i<selSoFar.size();i++){
                   String[] s = (String[])selSoFar.elementAt(i);
                   text2 = text2+ s[0]+"---"+i+"\n";
               }
                
                otherUserFeedback = "-"+text2;
        }
        else if(mct.getText().startsWith("/hm")){
              
        }
        else if(mct.getText().startsWith("/restart")){
              selSoFar.removeAllElements();
              otherUserFeedback="*****The list of items is deleted....start again ";
        }
        else if(mct.getText().startsWith("/")){

                 String nam = mct.getText().substring(1);
                 Object o = db.getObjectWithName(nam, currentSetSorted);


                 if(o==null){
                     otherUserFeedback = ("****OTHER PARTICIPANT TYPED A TYPO");
                 }
                 else if(selSoFar.contains(o))
                 {
                     otherUserFeedback ="****OTHER PARTICIPANT IS TRYING TO SELECT AN ITEM THAT'S ALREADY BEEN SELECTED";
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
                 else if(pSender == this.participant1 && currentSetA.contains(o)){
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
                 else if(pSender == this.participant2 && currentSetB.contains(o)){
                     otherUserFeedback = "****OTHER PARTICIPANT TRIED TO SELECT ONE OF YOUR ITEMS INSTEAD OF THEIRS";
                     selSoFar = new Vector();
                 }

                


    }
     if(otherUserFeedback!=null&&otherUserFeedback.length()>0){
                       //if(Debug.generateVERBOSELISTOUTPUTFORCOLLABTASK)otherUserFeedback = otherUserFeedback + verboseDiagnostics;
                        if(pSender==this.participant1)c.sendArtificialTurnFromApparentOriginToRecipient(participant1, participant2,otherUserFeedback);
                       else if(pSender == this.participant2) c.sendArtificialTurnFromApparentOriginToRecipient(participant2, participant1, otherUserFeedback);
                       else Conversation.printWSln("Main", "Error trying to sendfakeoutput2 to "+pSender.getUsername());
                 }



}
}

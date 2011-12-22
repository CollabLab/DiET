/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package diet.server.ConversationController;

import diet.message.MessageChatTextFromClient;
import diet.message.MessageKeypressed;
import diet.parameters.ExperimentSettings;
import diet.server.Conversation;
import diet.server.Participant;
import diet.utils.stringsimilarity.StringSimilarityMeasure;
import java.awt.Color;
import java.net.InetAddress;
import java.util.Date;
import java.util.Vector;

/**
 *
 * @author sre
 */
public class CCSEQUENCECOPY extends DefaultConversationController{

    //String ipDDR = "http://"+this.getIPAddress();
    String nullADDRESS = "http://"+this.getIPAddress()+":81/blankspace.txt";
    String taskInstructionADDRESS = "http://"+this.getIPAddress()+":81/moontask.html";


    public String getIPAddress(){
        String address = "";
        try {
            InetAddress thisIp =InetAddress.getLocalHost();
            System.out.println("IP:"+thisIp.getHostAddress());

            address = thisIp.getHostAddress();
            Conversation.printWSln("Main", "Determining the address as: "+thisIp.getHostAddress());
         }
            catch(Exception e) {
            e.printStackTrace();
         }
        return address;
    }

    @Override
    public void initialize(Conversation c, ExperimentSettings expSettings) {
        super.initialize(c, expSettings);
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

    public int getRanking(String name,Vector allObjectsSearchedThrough ){
        int score = -9;
        for(int i=0;i<allObjectsSearchedThrough.size();i++){
             String[] arr = (String[])allObjectsSearchedThrough.elementAt(i);
             if(arr[0].equalsIgnoreCase(name)) score = i;
             if(arr[1].equalsIgnoreCase(name)) score = i;
             if(arr[2].equalsIgnoreCase(name)) score = i;       
        }
        return score;
    }

    public String[] getObjectWithName(String s,Vector objectsToSearchThrough){
        for(int i=0;i<objectsToSearchThrough.size();i++){
             String[] arr = (String[])objectsToSearchThrough.elementAt(i);
             if(arr[0].equalsIgnoreCase(s)) return arr;
             if(arr[1].equalsIgnoreCase(s)) return arr;
             if(arr[2].equalsIgnoreCase(s)) return arr;
        }
        return null;
    }

    
    public String getEval(String evls){
        if(2<5) return this.getScore_BestList(evls);
        try{
          Vector v = StringSimilarityMeasure.splitIntoWords(evls);
          String s = (String)v.elementAt(1);
          String value = this.getScore_BestItem(s);
          return value +"(looking for s)";
        }catch (Exception e){
            return "ERROR";
        }
    }

    public String getScore_BestList(String evls){
        Vector selectionOfStringsByParticipant = StringSimilarityMeasure.splitIntoWords(evls);
        selectionOfStringsByParticipant.removeElementAt(0);


        if(selectionOfStringsByParticipant.size()!=currentSet.size()) return "TYPO";
        Vector selection = new Vector();
        for(int i=0;i<selectionOfStringsByParticipant.size();i++){
            String s = (String)selectionOfStringsByParticipant.elementAt(i);
            String[] selArr = this.getObjectWithName(s, currentSet);
            if(selArr==null)return "ERROR";
            selection.addElement(selArr);
        }
        Vector orderedSelection = new Vector();


        if(selection.size()<2)return "ERROR";
        String[] arrHead = (String[])selection.elementAt(0);
        int smallestSoFar = this.getRanking(arrHead[1], allObjects);
        int largestSoFar = smallestSoFar;
        int selTooSmallCount =0;
        int selTooLargeCount =0;
        System.err.println("INITIALIZING toosmallcount "+selTooSmallCount+"...toolargecount"+selTooLargeCount+"...smallestSoFar"+smallestSoFar);

        for(int i=1;i<selection.size();i++){
             String[] arr = (String[])selection.elementAt(i);
             int arrRank = this.getRanking(arr[1], this.allObjects);
              System.err.println("SEARCHING for"+arr[1]     +"toosmallcount "+selTooSmallCount+"...toolargecount"+selTooLargeCount+"...smallestSoFar"+smallestSoFar);

             if(arrRank<smallestSoFar){
                 smallestSoFar = arrRank;
                 selTooSmallCount++;
             }
             if(arrRank>largestSoFar){
                 largestSoFar = arrRank;
                 selTooLargeCount++;
             }
        }
        if(selTooSmallCount==0) return "GREEN";
        if(selTooSmallCount==selection.size()) return "RED";
        return "ORANGE";
    }



    


    public String getScore_BestItem(String evls){
        //GREEN = BEST OF LIST
        //ORANGE = MIDDLE OF LIST
        //RED = WORST OF LIST
        int biggestScore = 0;
        int lowestScore = 20000;
        for(int i=0;i<this.currentSet.size();i++){
            String s[]  = (String[])currentSet.elementAt(i);
            int ranking = this.getRanking(s[1],allObjects);
            if(ranking>biggestScore){
                biggestScore = ranking;
            }
            if(ranking<lowestScore){
                lowestScore = ranking;
            }
           
        }
        System.out.println("BIGGEST SCORE"+biggestScore);
        System.out.println("LOWEST SCORE"+lowestScore);

        int evalRanked = this.getRanking(evls,allObjects);
        System.err.println("Looking for item:"+evls+" "+evalRanked);

        if(evalRanked<0) return "TYPO";
        if(evalRanked==biggestScore) return "RED";
        if(evalRanked==lowestScore) return "GREEN";
        if(evalRanked<biggestScore) {
            int indexOfCurrentSet = this.getRanking(evls, currentSet);
            if(indexOfCurrentSet<0)return "NOTANOPTION";
            return "ORANGE";
        }
        


        return "ERROR";
    }



    

    Participant participant1 ;
    Participant participant2 ;

    String prefixA = "Your objects are:\n";
    String prefixB = "Your objects are:\n";

    Vector allObjects = new Vector();

    Vector currentSet = new Vector();
    Vector currentSetA = new Vector();
    Vector currentSetB = new Vector();

    public int minSelection = 3;
    public int range =1;

    boolean allowNoObjects = false;
    boolean canMoveToNextStep = false;

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
        currentSet = (Vector)selection.clone();
        String message = "The objects you have are:\n\n";

        for (int i=0;i<selection.size();i++){
          String[] entry =  (String[])selection.elementAt(i);
           message = message + entry[i]+"\n";
        }
       // display(message);

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
            }
            else{
                currentSetB.addElement(o);
            }
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
        String textForA = this.prefixA +"\n";
        String textForB = this.prefixB +"\n";

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

      

        c.changeWebpage(this.participant2, "webpage",nullADDRESS, null, textForA);
        c.changeWebpage(this.participant1, "webpage",nullADDRESS, null, textForB);
    }


    @Override
    public void processKeyPress(Participant sender, MessageKeypressed mkp) {
        super.processKeyPress(sender, mkp);
    }



    @Override
    public void participantJoinedConversation(Participant p) {    
        c.displayNEWWebpage(p, "instructions", "TASKWINDOW",this.taskInstructionADDRESS, 500, 540, false,false);


        if(c.getParticipants().getAllParticipants().size()>2){
            Conversation.printWSln("Main", "ERROR...trying to add "+p.getUsername()+ " but there are already "+c.getParticipants().getAllParticipants().size());
        }
        else if (this.participant1==null){
            participant1 = p;
            c.displayNEWWebpage(p, "webpage", "TASKWINDOW",nullADDRESS, 300, 300, false,false);

            c.changeWebpage(p, "webpage", nullADDRESS, "Please wait for other partcipant to log on...\n\n"
                    + "When the other participant logs on this screen will show the instructions for the experiment", "");
            c.changeJProgressBar(p, "webpage", "STARTING", Color.green, 100);
        }
        else if (this.participant2 == null) {
            participant2 = p;
            c.displayNEWWebpage(p, "webpage", "TASKWINDOW",nullADDRESS, 300, 300, false,false);
            
        }
        super.participantJoinedConversation(p);
        if(c.getParticipants().getAllParticipants().size()==2){
            this.step1_ChooseSetOfObjects();
            this.step2_PartitionIntoTwoSubSets();
            this.step3_SendObjectsToBothParticipants();
            canMoveToNextStep = false;
        }


    }

    @Override
    public void participantRejoinedConversation(Participant p) {
        super.participantRejoinedConversation(p);

    }

    long strt = new Date().getTime();
    int val = 100;

    @Override
    public void processLoop() {
        super.processLoop();
        c.resetFromIsTypingtoNormalChatAllAllowedParticipants(super.getIsTypingTimeOut());

        long curr = new Date().getTime();
        if(this.participant2!=null&&curr-strt>1000){
            Color cCol = Color.GREEN;
            if (val<50)cCol = Color.orange;
            if (val<20)cCol = Color.red;
            c.changeJProgressBarsOfAllParticipants("webpage", "counting", cCol, val);
            val--;
            strt = new Date().getTime();
        }

    }


    @Override
    public void processChatText(Participant sender, MessageChatTextFromClient mct) {
            if(mct.getText().startsWith("/next")|| mct.getText().startsWith("/NEXT")){
                 if(!canMoveToNextStep){
                     c.setNewTurnBeingConstructedNotRelayingOldTurnAddingOldTurnToHistory(sender, mct);
                     c.sendArtificialTurnToAllOtherParticipants(sender, "MAKE SELECTION FIRST");
                     return;
                 }
                 this.step1_ChooseSetOfObjects();
                 this.step2_PartitionIntoTwoSubSets();
                 this.step3_SendObjectsToBothParticipants();
                 canMoveToNextStep = false;
            }
            if(mct.getText().startsWith("/sel")){
                String eval = this.getEval(mct.getText());
                if(eval.equalsIgnoreCase("RED")){
                    mct.setText("EVERYONE DIED");
                }
                else if (eval.equalsIgnoreCase("ORANGE")) {
                    mct.setText("SOME PEOPLE DIED");
                }
                else if (eval.equalsIgnoreCase("GREEN")) {
                    mct.setText("EVERYONE SURVIVED");
                    canMoveToNextStep = true;
                }
                else{
                    mct.setText("ERROR");
                }
                
            }
             super.processChatText(sender, mct);
    }
}
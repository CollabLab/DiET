/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package diet.server.ConversationController;

import diet.debug.Debug;
import diet.message.MessageChatTextFromClient;
import diet.message.MessageKeypressed;
import diet.parameters.ExperimentSettings;
import diet.server.Conversation;
import diet.server.Participant;
import diet.task.collabMinitask.CollabMoonTask;
import java.awt.Color;
import java.util.Date;
import java.util.Hashtable;
import java.util.Random;
import java.util.Vector;

/**
 *
 * @author sre
 */
public class CCGROOP extends DefaultConversationController{




    public CollabMoonTask getCT(String name){
        CollabMoonTask cmt;
        Participant p = c.getParticipants().findParticipantWithEmail("000000");
        cmt = (CollabMoonTask)ht.get(p);
        return cmt;
    }
   
    static public String ipADDRESS = "localhost";
    int webserverport = 81;
    String ipADRESSWEB = "http://"+ipADDRESS+":"+webserverport+"/";

    //public int sizeOfGroup =500;

    Hashtable ht = new Hashtable();
    

    public static String[] acceptableIDBACKUP =
    {"000000", "010101","020202","030303","040404","050505","060606","070707",
     "080808", "090909","101010","111111","121212","131313","141414","151515",
     "161616", "171717","181818","191919","202020","212121","222222","232323",
     "242424", "252525","262626","272727","282828","292929","303030","313131",
     "323232", "333333","343434"};

    public static String[] acceptableID = {"T0LT0L", "T0RT0R",  "T1LT1L", "T1RT1R", "T2LT2L","T2RT2R","T3LT3L","T3RT3R","T4LT4L", "T4RT4R", "T5LT5L", "T5RT5R", "T6LT6L","T6RT6R", "T7LT7L", "T7RT7R", "T8LT8L",  "T8RT8R", "T9LT9L", "T9RT9R", "T10LT10L", "T10RT10R", "T11LT11L", "T11RT11R", "T12LT12L", "T12RT12R", "T13LT13L", "T13RT13R" , "T14LT14L", "T14RT14R", "T15LT15L", "T15RT15R"};


   

    @Override
    public void initialize(Conversation c, ExperimentSettings expSettings) {
        super.initialize(c, expSettings);
        
        
    }

    public String getNameOfParticipant(int i){
        String needle ="";
        String base = "";
        if(i==0)base ="00";
        else if(i<10)base = "0"+i;
        else{
            base =""+i;
        }
        for(int j=0;j<3;j++){
            needle = needle+base;
        }
        return needle;
    }


    public synchronized void connect(int p1Number, int p2Number, boolean secondStage){
        
        String p1Name = "___";
        String p2Name = "___";
        try{

        //p1Name =     this.getNameOfParticipant(p1Number);
        //p2Name =     this.getNameOfParticipant(p2Number);
        Participant p1 = (Participant)c.getParticipants().getAllParticipants().elementAt(p1Number);//c.getParticipants().findParticipantWithEmail(p1Name);
        Participant p2 = (Participant)c.getParticipants().getAllParticipants().elementAt(p2Number);//c.getParticipants().findParticipantWithEmail(p2Name);


        
        CollabMoonTask cmt = new CollabMoonTask(c,secondStage);
        ht.put(p1, cmt);
        ht.put(p2, cmt);
        cmt.participantJoinedConversation(p1);
        cmt.participantJoinedConversation(p2);

        System.err.println("A2) "+p1Name+" "+p2Name);
        System.err.println("A2) "+p1.getParticipantID()+" "+p2.getParticipantID());
        System.err.println("A3) "+cmt.getParticipant1().getParticipantID()+" "+cmt.getParticipant2().getParticipantID());
       

        }catch(Exception e){
            System.err.println("ERROR TRYING TO CONNECT"+ p1Name+"--"+p2Name);
            Conversation.printWSln("Main","ERROR TRYING TO CONNECT"+ p1Name+"--"+p2Name);
           c.printAllP();
            e.printStackTrace();
            
        }
    }



    


    public synchronized Participant getPartner(Participant p){
         CollabMoonTask ct  = (CollabMoonTask)this.ht.get(p);
         if(ct==null)return null;
         if(ct.getParticipant1()==p){
             return ct.getParticipant2();
         }
         if(ct.getParticipant2()==p){
             return ct.getParticipant1();
         }
         return null;

    }

    


    public boolean requestParticipantJoinConversation(String participantID) {
        for(int i = 0; i<acceptableID.length;i++){
            String permittedId = acceptableID[i];
            if(permittedId.equalsIgnoreCase(participantID))return true;
        }
        Conversation.printWSln("Main", "DID NOT PERMIT "+participantID+ "to join conversation");
        return false;
    }
  
    @Override
    public void processKeyPress(Participant sender, MessageKeypressed mkp) {
        //No typing messages
    }



    @Override
    public synchronized void participantJoinedConversation(Participant p) {
         Conversation.printWSln("Main", "PARTICIPANTJOINED CONVERSATION: there are(a): "+c.getParticipants().getAllParticipants().size());
         this.addParticipantToSeating(p);
         Conversation.printWSln("Main", "PARTICIPANTJOINED CONVERSATION: there are(b): "+c.getParticipants().getAllParticipants().size());

          c.displayNEWWebpage(p, "objectlist", "TASKWINDOW","", 300, 300, false,false);
          c.changeWebpage(p, "objectlist", "", "CONNECTING TO REMOTE SERVER: WWW.UNI.CHATCLIENT.CO.UK...SUCCESS....\n"
                  + "Please wait for everyone else to log on\n"
                        + "Please don't type anything\n"
                        + "Please reread the instructions to make sure you understand\n","");
          c.changeJProgressBar(p, "objectlist", "Time till speaker change", Color.green, 100);

          //if(Debug.debugGROOP&&c.getParticipants().getAllParticipants().size()==4)this.strt();
        
    }

    
    @Override
    public synchronized void participantRejoinedConversation(Participant p) {
       try{
           c.displayNEWWebpage(p, "objectlist", "TASKWINDOW","", 300, 300, false,false);
                 c.changeJProgressBar(p, "objectlist", "Time till speaker change", Color.green, 100);

           CollabMoonTask ct = (CollabMoonTask)this.ht.get(p);
           if(ct!=null){
               ct.participantRejoinedConversation(p);
               System.err.println("SUCCESSFULLY ALLOWED "+p.getUsername()+" to rejoin the conversation");
               Conversation.printWSln("Main", "SUCCESSFULLY ALLOWED "+p.getUsername()+" to rejoin the conversation");
           }
           
        }catch(Exception e){
               System.err.println("---error rejoining conversation");
               e.printStackTrace();
               Conversation.printWSln("Main", "SUCCESSFULLY ALLOWED "+p.getUsername()+" to rejoin the conversation");
        }

    }



    
    


    @Override
    public void processChatText(Participant sender, MessageChatTextFromClient mct) {
         
         //mct.setText("CHANGING "+sender.getUsername()+"--to--"+getAlias(sender));

         if(mct.getText().startsWith("/")){
              
             
             if(Debug.debugBLOCKAGE){System.out.println("MCT7PRCA1");System.out.flush();};
             CollabMoonTask ct = (CollabMoonTask)this.ht.get(sender);
             if(Debug.debugBLOCKAGE){System.out.println("MCT7PRCA2");System.out.flush();};
              if(ct==null){
                  if(2>5&&Debug.debugGROOP)System.err.println("PRCESSING CHATTEXTSTART3"+mct.getText());
                  Conversation.printWSln("Main", "Error--"+sender.getUsername()+" is sending command, but isn't assigned to task");
              }
              else{
                  ct.processCommand(sender, mct);
                  
              }

          }
          else{
             if(Debug.debugBLOCKAGE){System.out.println("MCT7PRCB1");System.out.flush();};
              Participant partner = this.getPartner(sender);
              if(Debug.debugBLOCKAGE){System.out.println("MCT7PRCB2");System.out.flush();};
              if(partner==null){
                  if(Debug.debugBLOCKAGE){System.out.println("MCT7PRCB3");System.out.flush();};
                  Conversation.printWSln("Main", "Error--"+sender.getUsername()+" is sending message, but has no partner");
                  if(Debug.debugBLOCKAGE){System.out.println("MCT7PRCB4");System.out.flush();};
              }
              else{
                  if(Debug.debugBLOCKAGE){System.out.println("MCT7PRCB5");System.out.flush();};
                   c.relayTurnToParticipant(sender, partner, mct);
                   if(Debug.debugBLOCKAGE){System.out.println("MCT7PRCB6");System.out.flush();};
              }



          }
          if(Debug.debugBLOCKAGE){System.out.println("MCT3");System.out.flush();}

    }


    @Override
    public void processLoop() {
        try{
            System.out.println("INTIME");
            this.bideTime();
            System.out.println("OUT");
           super.processLoop();
           //c.resetFromIsTypingtoNormalChatAllAllowedParticipants(super.getIsTypingTimeOut());
        }catch (Exception e){
            e.printStackTrace();
        }
    }
//              1  2  3  4  5  6  7  8 
long[] times = {9,17,24,30,35,40,45,50,55,60,65,70,85,90};


int[][][] groups = null; //this is the one that is run

int[][][] group2   = initializeArrays2();
int[][][] group4   = initializeArrays4();
int[][][] group6   = initializeArrays6();
int[][][] group8   = initializeArrays8();
int[][][] group10  = initializeArrays10();
int[][][] group12  = initializeArrays12();
int[][][] group14  = initializeArrays14();
int[][][] group16  = initializeArrays16();
int[][][] group18  = initializeArrays18();
int[][][] group20  = initializeArrays20();
int[][][] group22  = initializeArrays22();
int[][][] group24  = initializeArrays24();
int[][][] group26  = initializeArrays26();



int[][][] group8B   = initializeArrays8B();
int[][][] group10B  = initializeArrays10B();
int[][][] group12B  = initializeArrays12B();
int[][][] group14B  = initializeArrays14B();
int[][][] group16B  = initializeArrays16B();
int[][][] group18B  = initializeArrays18B();
int[][][] group20B  = initializeArrays20B();
int[][][] group22B  = initializeArrays22B();
int[][][] group24B  = initializeArrays24B();
int[][][] group26B  = initializeArrays26B();

int[][][] group8M   = initializeArrays8M();
int[][][] group10M  = initializeArrays10M();
int[][][] group12M  = initializeArrays12M();
int[][][] group14M  = initializeArrays14M();
int[][][] group16M  = initializeArrays16M();
int[][][] group18M  = initializeArrays18M();
int[][][] group20M  = initializeArrays20M();
int[][][] group22M  = initializeArrays22M();
int[][][] group24M  = initializeArrays24M();
int[][][] group26M  = initializeArrays26B();



public int[][][] initializeArrays2REAL(){
    int[][] trial1 = {{0,1}};
    int[][] trial2 = {{1,0}};
    int[][] trial3 = {{0,1}};
    int[][] trial4 = {{1,0}};
    int[][][] manipulation = {trial1,trial2,trial3,trial4};
    return manipulation;
}

public int[][][] initializeArrays2(){
    int[][] trial1 = {{0,1}};
    int[][] trial2 = {{1,0}};
    int[][][] manipulation = {trial1,trial2};
    return manipulation;
}


public int[][][] initializeArrays4(){
      int[][] trial1 = {{0,1}, {2,3}};
      int[][] trial2 = {{0,2} ,{1,3}};
      int[][] trial3 = {{0,1}, {2,3}};
      int[][] trial4 = {{0,2} ,{1,3}};
      int[][] trial5 = {{0,1}, {2,3}};
      int[][] trial6 = {{0,2} ,{1,3}};
      int[][] trial7 = {{0,3} ,{1,2}};
      int[][][] manipulation = {trial1,trial2,trial3,trial4,trial5,trial6, trial7};
      return manipulation;
}

//14 participants -   6 B (but one fewer turn)
//6 participants -    4W
//8 participants -    2W 2B


public int[][][] initializeArrays6(){
      int[][] trial1 = {{0,1}, {2,3}, {4,5}};
      int[][] trial2 = {{0,2} ,{1,3}, {4,5}};
      int[][] trial3 = {{0,1}, {2,3}, {4,5}};
      int[][] trial4 = {{0,2} ,{1,3}, {4,5}};
      int[][] trial5 = {{0,1}, {2,3}, {4,5}};
      int[][] trial6 = {{0,2} ,{1,3}, {4,5}};
      int[][] trial7 = {{0,3} ,{1,2}, {4,5}};
      int[][][] manipulation = {trial1,trial2,trial3,trial4,trial5,trial6, trial7};
      return manipulation;
}

public int[][][] initializeArrays8(){
      int[][] trial1 = {{0,1}, {2,3},      {4,5}, {6,7}};
      int[][] trial2 = {{0,2} ,{1,3},      {4,6} ,{5,7}};
      int[][] trial3 = {{0,1}, {2,3},      {4,5}, {6,7}};
      int[][] trial4 = {{0,2} ,{1,3},      {4,6} ,{5,7}};
      int[][] trial5 = {{0,1} ,{2,3},      {4,5} ,{6,7}};
      int[][] trial6 = {{0,2} ,{1,3},      {4,6} ,{5,7}};
      int[][] trial7 = {{0,3} ,{1,2},      {4,7} ,{5,6}};
      int[][][] manipulation = {trial1,trial2,trial3,trial4,trial5,trial6, trial7};
      return manipulation;
}

public int[][][] initializeArrays8B(){
    int[][][] a = initializeArrays8();
    int[][] trialreplacement = {{0,4},{3,7},{1,5},{2,6}};
    a[6] = trialreplacement;
    return a;
}

public int[][][] initializeArrays8M(){
    int[][][] a = initializeArrays8();
    int[][] trialreplacement = {{0,3},{1,6},{4,7},{2,5}};
    a[6] = trialreplacement;
    return a;
}


public int[][][] initializeArrays10(){
      int[][] trial1 = {{0,1}, {2,3},      {4,5}, {6,7},  {8,9}};
      int[][] trial2 = {{0,2} ,{1,3},      {4,6} ,{5,7},  {8,9}};
      int[][] trial3 = {{0,1}, {2,3},      {4,5}, {6,7},  {8,9}};
      int[][] trial4 = {{0,2} ,{1,3},      {4,6} ,{5,7},  {8,9}};
      int[][] trial5 = {{0,1} ,{2,3},      {4,5} ,{6,7},  {8,9}};
      int[][] trial6 = {{0,2} ,{1,3},      {4,6} ,{5,7},  {8,9}};
      int[][] trial7 = {{0,3} ,{1,2},      {4,7} ,{5,6} , {8,9}};
      int[][][] manipulation = {trial1,trial2,trial3,trial4,trial5,trial6, trial7};
      return manipulation;
}

public int[][][] initializeArrays10B(){
    int[][][] a = initializeArrays10();
    int[][] trialreplacement = {{0,4},{3,7},{1,5},{2,6}, {8,9}};
    a[6] = trialreplacement;
    return a;
}

public int[][][] initializeArrays10M(){
    int[][][] a = initializeArrays10();
    int[][] trialreplacement = {{0,3},{1,6},{4,7},{2,5}, {8,9}};
    a[6] = trialreplacement;
    return a;
}


public int[][][] initializeArrays12(){
      int[][] trial1 = {{0,1}, {2,3},      {4,5}, {6,7},     {8,9},   {10,11}};
      int[][] trial2 = {{0,2} ,{1,3},      {4,6} ,{5,7},     {8,10},  {9,11}};
      int[][] trial3 = {{0,1}, {2,3},      {4,5}, {6,7},     {8,9},   {10,11}};
      int[][] trial4 = {{0,2} ,{1,3},      {4,6} ,{5,7},     {8,10},  {9,11}};
      int[][] trial5 = {{0,1} ,{2,3},      {4,5} ,{6,7},     {8,9},   {10,11}};
      int[][] trial6 = {{0,2} ,{1,3},      {4,6} ,{5,7},     {8,10},  {9,11}};
      int[][] trial7 = {{0,3} ,{1,2},      {4,7} ,{5,6},     {8,11},  {9,10}};
      int[][][] manipulation = {trial1,trial2,trial3,trial4,trial5,trial6, trial7};
      return manipulation;
}




public int[][][] initializeArrays12B(){
    int[][][] a = initializeArrays12();
    int[][] trialreplacement = {{0,5},{3,6},{1,8},{2,11}, {4,9}, {7,10}};
    a[6] = trialreplacement;
    return a;
}





public int[][][] initializeArrays12M(){
    int[][][] a = initializeArrays12();
    int[][] trialreplacement = {{0,3},{2,5},{4,7},{6,9}, {8,11}, {1,10}};
    a[6] = trialreplacement;
    return a;
}


public int[][][] initializeArrays14(){
      int[][] trial1 = {{0,1}, {2,3},      {4,5}, {6,7},     {8,9},   {10,11},  {12,13}};
      int[][] trial2 = {{0,2} ,{1,3},      {4,6} ,{5,7},     {8,10},  {9,11},   {12,13}};
      int[][] trial3 = {{0,1}, {2,3},      {4,5}, {6,7},     {8,9},   {10,11},  {12,13}};
      int[][] trial4 = {{0,2} ,{1,3},      {4,6} ,{5,7},     {8,10},  {9,11},   {12,13}};
      int[][] trial5 = {{0,1} ,{2,3},      {4,5} ,{6,7},     {8,9},   {10,11},  {12,13}};
      int[][] trial6 = {{0,2} ,{1,3},      {4,6} ,{5,7},     {8,10},  {9,11},   {12,13}};
      int[][] trial7 = {{0,3} ,{1,2},      {4,7} ,{5,6},     {8,11},  {9,10},   {12,13}};
      int[][][] manipulation = {trial1,trial2,trial3,trial4,trial5,trial6, trial7};
      return manipulation;
}


public int[][][] initializeArrays14M(){
    int[][][] a = initializeArrays12();
    int[][] trialreplacement = {{0,3},{2,5},{4,7},{6,9}, {8,11}, {1,10},  {12,13}};
    a[6] = trialreplacement;
    return a;
}


public int[][][] initializeArrays14B(){
    int[][][] a = initializeArrays14();
    int[][] trialreplacement = {{0,5},{3,6},{1,8},{2,11}, {4,9}, {7,10}, {12,13}};
    a[6] = trialreplacement;
    return a;
}


public int[][][] initializeArrays16(){
      int[][] trial1 = {{0,1}, {2,3},      {4,5}, {6,7},     {8,9},   {10,11},   {12,13},   {14,15}};
      int[][] trial2 = {{0,2} ,{1,3},      {4,6} ,{5,7},     {8,10},  {9,11},    {12,14},   {13,15}};
      int[][] trial3 = {{0,1}, {2,3},      {4,5}, {6,7},     {8,9},   {10,11},   {12,13},   {14,15}};
      int[][] trial4 = {{0,2} ,{1,3},      {4,6} ,{5,7},     {8,10},  {9,11},    {12,14},   {13,15}};
      int[][] trial5 = {{0,1} ,{2,3},      {4,5} ,{6,7},     {8,9},   {10,11},   {12,13},   {14,15}};
      int[][] trial6 = {{0,2} ,{1,3},      {4,6} ,{5,7},     {8,10},  {9,11},    {12,14},   {13,15}};
      int[][] trial7 = {{0,3} ,{1,2},      {4,7} ,{5,6},     {8,11},  {9,10},    {12,15},   {13,14}};
      int[][][] manipulation = {trial1,trial2,trial3,trial4,trial5,trial6, trial7};
      return manipulation;
}

public int[][][] initializeArrays16B(){
    int[][][] a = initializeArrays16();
    int[][] trialreplacement = {{0,4},{3,7},{1,5},{2,6}, {8,12}, {11,15}, {9,13}, {10,14}};
    a[6] = trialreplacement ;
    return a;
}

public int[][][] initializeArrays16M(){
    int[][][] a = initializeArrays16();
    int[][] trialreplacement = {{0,3} ,{1,5},    {4,7} ,{2,6},     {8,11},  {9,13},   {12,15},  {10,14}};
    a[6] = trialreplacement ;
    return a;
}


public int[][][] initializeArrays18(){
      int[][] trial1 = {{0,1}, {2,3},      {4,5}, {6,7},     {8,9},   {10,11},   {12,13},   {14,15},     {16,17}};
      int[][] trial2 = {{0,2} ,{1,3},      {4,6} ,{5,7},     {8,10},  {9,11},    {12,14},   {13,15},     {16,17}};
      int[][] trial3 = {{0,1}, {2,3},      {4,5}, {6,7},     {8,9},   {10,11},   {12,13},   {14,15},     {16,17}};
      int[][] trial4 = {{0,2} ,{1,3},      {4,6} ,{5,7},     {8,10},  {9,11},    {12,14},   {13,15},     {16,17}};
      int[][] trial5 = {{0,1} ,{2,3},      {4,5} ,{6,7},     {8,9},   {10,11},   {12,13},   {14,15},     {16,17}};
      int[][] trial6 = {{0,2} ,{1,3},      {4,6} ,{5,7},     {8,10},  {9,11},    {12,14},   {13,15},     {16,17}};
      int[][] trial7 = {{0,3} ,{1,2},      {4,7} ,{5,6},     {8,11},  {9,10},    {12,15},   {13,14},     {16,17}};
      int[][][] manipulation = {trial1,trial2,trial3,trial4,trial5,trial6, trial7};
      return manipulation;
}

public int[][][] initializeArrays18M(){
    int[][][] a = initializeArrays16();
    int[][] trialreplacement = {{0,3} ,{1,5},    {4,7} ,{2,6},     {8,11},  {9,13},   {12,15},  {10,14}, {16,17}};
    a[6] = trialreplacement ;
    return a;
}


public int[][][] initializeArrays18B(){
    int[][][] a = initializeArrays18();
    int[][] trialreplacement = {{0,4},{3,7},{1,5},{2,6}, {8,12}, {11,15}, {9,13}, {10,14}, {16,17}};
    a[6] = trialreplacement ;
    return a;
}



public int[][][] initializeArrays20(){
      int[][] trial1 = {{0,1}, {2,3},      {4,5}, {6,7},     {8,9},   {10,11},   {12,13},   {14,15},     {16,17}, {18,19}};
      int[][] trial2 = {{0,2} ,{1,3},      {4,6} ,{5,7},     {8,10},  {9,11},    {12,14},   {13,15},     {16,18}, {17,19}};
      int[][] trial3 = {{0,1}, {2,3},      {4,5}, {6,7},     {8,9},   {10,11},   {12,13},   {14,15},     {16,17}, {18,19}};
      int[][] trial4 = {{0,2} ,{1,3},      {4,6} ,{5,7},     {8,10},  {9,11},    {12,14},   {13,15},     {16,18}, {17,19}};
      int[][] trial5 = {{0,1} ,{2,3},      {4,5} ,{6,7},     {8,9},   {10,11},   {12,13},   {14,15},     {16,17}, {18,19}};
      int[][] trial6 = {{0,2} ,{1,3},      {4,6} ,{5,7},     {8,10},  {9,11},    {12,14},   {13,15},     {16,18}, {17,19}};
      int[][] trial7 = {{0,3} ,{1,2},      {4,7} ,{5,6},     {8,11},  {9,10},    {12,15},   {13,14},     {16,19}, {17,18}};
      int[][][] manipulation = {trial1,trial2,trial3,trial4,trial5,trial6, trial7};
      return manipulation;
}





public int[][][] initializeArrays20B(){
    int[][][] a = initializeArrays20();
    int[][] trialreplacement = {{0,9},{3,10},{1,12},{2,15}, {4,13}, {7,14}, {5,16}, {6,19}, {8,17}, {11,18}};
    a[6] = trialreplacement ;
    return a;
}

public int[][][] initializeArrays20M(){
    int[][][] a = initializeArrays20();
    int[][] trialreplacement = {{0,3} ,{2,6},      {4,7} ,{5,9},     {8,11},  {10,14},    {12,15},   {13,17},     {16,19}, {18,1}};
    a[6] = trialreplacement ;
    return a;
}





public int[][][] initializeArrays22(){
      int[][] trial1 = {{0,1}, {2,3},      {4,5}, {6,7},     {8,9},   {10,11},   {12,13},   {14,15},     {16,17},   {18,19},    {20,21}};
      int[][] trial2 = {{0,2} ,{1,3},      {4,6} ,{5,7},     {8,10},  {9,11},    {12,14},   {13,15},     {16,18},   {17,19},    {20,21}};
      int[][] trial3 = {{0,1}, {2,3},      {4,5}, {6,7},     {8,9},   {10,11},   {12,13},   {14,15},     {16,17},   {18,19},    {20,21}};
      int[][] trial4 = {{0,2} ,{1,3},      {4,6} ,{5,7},     {8,10},  {9,11},    {12,14},   {13,15},     {16,18},   {17,19},    {20,21}};
      int[][] trial5 = {{0,1} ,{2,3},      {4,5} ,{6,7},     {8,9},   {10,11},   {12,13},   {14,15},     {16,17},   {18,19},    {20,21}};
      int[][] trial6 = {{0,2} ,{1,3},      {4,6} ,{5,7},     {8,10},  {9,11},    {12,14},   {13,15},     {16,18},   {17,19},    {20,21}};
      int[][] trial7 = {{0,3} ,{1,2},      {4,7} ,{5,6},     {8,11},  {9,10},    {12,15},   {13,14},     {16,19},   {17,18},    {20,21}};
      int[][][] manipulation = {trial1,trial2,trial3,trial4,trial5,trial6, trial7};
      return manipulation;
}




public int[][][] initializeArrays22B(){
    int[][][] a = initializeArrays22();
    int[][] trialreplacement = {{0,9},{3,10},{1,12},{2,15}, {4,13}, {7,14}, {5,16}, {6,19}, {8,17}, {11,18},{20,21}};
    a[6] = trialreplacement ;
    return a;
}


public int[][][] initializeArrays22M(){
    int[][][] a = initializeArrays20();
    int[][] trialreplacement = {{0,3} ,{2,6},  {4,7} ,{5,9},  {8,11},  {10,14},  {12,15},  {13,17}, {16,19}, {18,1}, {20,21}};
    a[6] = trialreplacement ;
    return a;
}


public int[][][] initializeArrays24(){
      int[][] trial1 = {{0,1}, {2,3},      {4,5}, {6,7},     {8,9},   {10,11},   {12,13},   {14,15},     {16,17}, {18,19},   {20,21}, {22,23}};
      int[][] trial2 = {{0,2} ,{1,3},      {4,6} ,{5,7},     {8,10},  {9,11},    {12,14},   {13,15},     {16,18}, {17,19},   {20,22}, {21,23}};
      int[][] trial3 = {{0,1}, {2,3},      {4,5}, {6,7},     {8,9},   {10,11},   {12,13},   {14,15},     {16,17}, {18,19},   {20,21}, {22,23}};
      int[][] trial4 = {{0,2} ,{1,3},      {4,6} ,{5,7},     {8,10},  {9,11},    {12,14},   {13,15},     {16,18}, {17,19},   {20,22}, {21,23}};
      int[][] trial5 = {{0,1} ,{2,3},      {4,5} ,{6,7},     {8,9},   {10,11},   {12,13},   {14,15},     {16,17}, {18,19},   {20,21}, {22,23}};
      int[][] trial6 = {{0,2} ,{1,3},      {4,6} ,{5,7},     {8,10},  {9,11},    {12,14},   {13,15},     {16,18}, {17,19},   {20,22}, {21,23}};
      int[][] trial7 = {{0,3} ,{1,2},      {4,7} ,{5,6},     {8,11},  {9,10},    {12,15},   {13,14},     {16,19}, {17,18},   {20,23}, {21,22}};
      int[][][] manipulation = {trial1,trial2,trial3,trial4,trial5,trial6, trial7};
      return manipulation;
}

public int[][][] initializeArrays24B(){
    int[][][] a = initializeArrays24();
    int[][] trialreplacement = {{0,4},{3,7},{1,5},{2,6}, {8,12}, {11,15}, {9,13}, {10,14},{16,20},{19,23},{17,21},{18,22}};
    a[6] = trialreplacement ;
    return a;
}

public int[][][] initializeArrays24M(){
    int[][][] a = initializeArrays24();
    int[][] trialreplacement = {{0,3} ,{2,6},  {4,7} ,{5,9},  {8,11},  {10,14},  {12,15},  {13,17}, {16,19}, {18,21}, {20,23},   {22,1}};
    a[6] = trialreplacement ;
    return a;
}



public int[][][] initializeArrays26(){
      int[][] trial1 = {{0,1}, {2,3},      {4,5}, {6,7},     {8,9},   {10,11},   {12,13},   {14,15},     {16,17}, {18,19},   {20,21}, {22,23}, {24,25}};
      int[][] trial2 = {{0,2} ,{1,3},      {4,6} ,{5,7},     {8,10},  {9,11},    {12,14},   {13,15},     {16,18}, {17,19},   {20,22}, {21,23}, {24,25}};
      int[][] trial3 = {{0,1}, {2,3},      {4,5}, {6,7},     {8,9},   {10,11},   {12,13},   {14,15},     {16,17}, {18,19},   {20,21}, {22,23}, {24,25}};
      int[][] trial4 = {{0,2} ,{1,3},      {4,6} ,{5,7},     {8,10},  {9,11},    {12,14},   {13,15},     {16,18}, {17,19},   {20,22}, {21,23}, {24,25}};
      int[][] trial5 = {{0,1} ,{2,3},      {4,5} ,{6,7},     {8,9},   {10,11},   {12,13},   {14,15},     {16,17}, {18,19},   {20,21}, {22,23}, {24,25}};
      int[][] trial6 = {{0,2} ,{1,3},      {4,6} ,{5,7},     {8,10},  {9,11},    {12,14},   {13,15},     {16,18}, {17,19},   {20,22}, {21,23}, {24,25}};
      int[][] trial7 = {{0,3} ,{1,2},      {4,7} ,{5,6},     {8,11},  {9,10},    {12,15},   {13,14},     {16,19}, {17,18},   {20,23}, {21,22}, {24,25}}; 
      int[][][] manipulation = {trial1,trial2,trial3,trial4,trial5,trial6, trial7};
      return manipulation;
}

public int[][][] initializeArrays26B(){
    int[][][] a = initializeArrays26();
    int[][] trialreplacement = {{0,4},{3,7},{1,5},{2,6}, {8,12}, {11,15}, {9,13}, {10,14},{16,20},{19,23},{17,21},{18,22}};
    a[6] = trialreplacement ;
    return a;
}


public int[][][] initializeArrays24__3GROUPSOF8(){
      int[][] trial1 = {{0,1},{2,3},{4,5},{6,7},        {8,9}, {10,11},{12,13},{14,15},      {16,17},{18,19},{20,21},{22,23}};
      int[][] trial2 = {{1,2},{3,4},{5,6},{7,0},        {9,10},{11,12},{13,14},{15,8},       {17,18},{19,20},{21,22},{23,16}};
      int[][] trial3 = {{0,6},{1,3},{2,4},{5,7},        {8,14},{9,11},{10,12},{13,15},       {17,20},{18,23},{19,22},{21,16}};
      int[][] trial4 = {{0,5},{1,4},{2,7},{3,6},        {8,13},{9,12},{10,15},{14,14},       {17,22},{18,21},{19,16},{20,23}};
      int[][] trial5 = {{0,3},{1,6},{2,5},{7,4},        {8,11},{9,14},{10,13},{12,15},       {16,18},{23,19},{22,20},{21,17}};
      int[][] trial6 = {{0,2},{1,5},{3,7},{4,6},        {8,10},{9,13},{11,15},{12,14},       {17,20},{18,23},{19,22},{21,16}};
      int[][] trial7 = {{0,8},{2,10},{6,16},{4,18},     {12,20},{14,22},{1,5},{3,7},         {17,21},{19,23},{11,15},{9,13}};
      int[][][] manipulation = {trial1,trial2,trial3,trial4,trial5,trial6,trial7};
      return manipulation;
}





public void doPairings(int trialno, int[][][] matrix){

    int[][] currTrial;
    boolean secondStage = false;
    try{
       if(trialno==matrix.length-1){
             Conversation.printWSln("Main", "DOING FINALTRIAL");
             secondStage =true;
             currTrial = matrix[matrix.length-1];
             for(int i=0;i<currTrial.length;i++){
                 int[] pair = currTrial[i];
                 for(int j=0;j<pair.length;j++){
                      System.out.println("PAIR["+i+"]"+"["+j+"] ="+currTrial[i][j]);
                 }

             }

            
             //System.exit(-59);
       }
       else if(trialno>=matrix.length-1){
            Conversation.printWSln("Main", "DOING FINALTRIALAGAIN");
            secondStage = true;
            currTrial = matrix[matrix.length-1];
       }
       else{
           secondStage = false;
           currTrial = matrix[trialno];
           int trialsTillSecondStage = matrix.length-trialno;
           Conversation.printWSln("Main", "COUNTDOWN TO FINALTRIAL IN: "+trialsTillSecondStage);
       }


     for(int i=0;i<currTrial.length;i++){
         int[] pairing = currTrial[i];
         this.connect(pairing[0], pairing[1], secondStage);
     }

        
     }catch(Exception e){
         System.err.println("ERROR IN DO PAIRINGS");
         Conversation.printWSln("Main", "ERROR IN DOPAIRIING");
         e.printStackTrace();
     }
}




public int currState=-1;



public long startTime;
public long currStateStart;
public long currStateLength;



public void strtM(){
    group8 = group8M;
    group10= group10M;
    group12=group12M;
    group14=group14M;
    group16=group16M;
    group18=group18M;
    group20=group20M;
    group22=group22M;
    group24=group24M;
    this.sortVEctorsWithin();
    this.decideGROOP(c.getParticipants().getAllParticipants().size());
    this.setState(0);
}

public void strtW(){
    this.sortVEctorsWithin();
    this.decideGROOP(c.getParticipants().getAllParticipants().size());
    this.setState(0);
}


public void strtB(){
            group8 = group8B;
            group10 = group10B;
            group12 = group12B;
            group14 = group14B;
            group16 = group16B;
            group18 = group18B;
            group20 = group20B;
            group22 = group22B;
            group24 = group24B;
            group26 = group26B;
            this.sortVectorsBetween();
            this.decideGROOP(c.getParticipants().getAllParticipants().size());
            this.setState(0);
}


public void strtOLD(){
        this.decideGROOP(c.getParticipants().getAllParticipants().size());
        this.setState(0);
    }

private void discardLast(){
      Vector ps = c.getParticipants().getAllParticipants();
      Participant p = (Participant)ps.lastElement();
      c.changeWebpage(p, "objectlist","", null,"Hi...please read this before doing anything....unfortunately because we need an even number of participants for this experiment...youve been randomly selected to sit here..please sit for 10 mins...we might need to ask you to help with someting in a momanet..");
}

public void decideGROOP(int peopleLoggedInAndReadyToStart){
    Conversation.printWSln("Main", "There are "+peopleLoggedInAndReadyToStart);
    System.err.println("There are "+peopleLoggedInAndReadyToStart);
     if(peopleLoggedInAndReadyToStart==2) {
         groups = group2;
     }
     else if(peopleLoggedInAndReadyToStart == 3) {
         groups = group2;
     }
     else if(peopleLoggedInAndReadyToStart == 4) {
        groups = group4;
     }
     else if(peopleLoggedInAndReadyToStart == 5) {
         groups = group4;
     }
     else if(peopleLoggedInAndReadyToStart == 6) {
         groups = group6;
     }
     else if(peopleLoggedInAndReadyToStart == 7) {
         groups = group6;
     }
     else if(peopleLoggedInAndReadyToStart == 8) {
         groups = group8;
     }
    else if(peopleLoggedInAndReadyToStart == 9) {
         groups = group8;
     }
    else if(peopleLoggedInAndReadyToStart == 10) {
         groups = group10;
     }
    else if(peopleLoggedInAndReadyToStart == 11) {
         groups = group10;
     }
    else if(peopleLoggedInAndReadyToStart == 12) {
         groups = group12;
     }
    else if(peopleLoggedInAndReadyToStart == 13) {
         groups = group12;
     }
    else if(peopleLoggedInAndReadyToStart == 14) {
         groups = group14;
     }
    else if(peopleLoggedInAndReadyToStart == 15) {
         groups = group14;
     }
    else if(peopleLoggedInAndReadyToStart == 16) {
         groups = group16;
     }
    else if(peopleLoggedInAndReadyToStart == 17) {
         groups = group16;
     }
    else if(peopleLoggedInAndReadyToStart == 18) {
         groups = group18;
     }
    else if(peopleLoggedInAndReadyToStart == 19) {
         groups = group18;
     }
    else if(peopleLoggedInAndReadyToStart == 20) {
         groups = group20;
     }
    else if(peopleLoggedInAndReadyToStart == 21) {
         groups = group20;
     }
    else if(peopleLoggedInAndReadyToStart == 22) {
         groups = group22;
     }
    else if(peopleLoggedInAndReadyToStart == 23) {
         groups = group22;
     }
    else if(peopleLoggedInAndReadyToStart == 24) {
         groups = group24;
     }
    else if(peopleLoggedInAndReadyToStart == 25) {
         groups = group24;
     }
    else if(peopleLoggedInAndReadyToStart == 26) {
         groups = group26;
     }
    else if(peopleLoggedInAndReadyToStart == 27) {
         groups = group26;
     }
    else if(peopleLoggedInAndReadyToStart == 28) {
         //groups = group28;
     }
    else if(peopleLoggedInAndReadyToStart == 29) {
         //groups = group28;
     }
    else if(peopleLoggedInAndReadyToStart == 30) {
         //groups = group30;
     }

     startTime=new Date().getTime();
}


private long timeOfLastStateDisplay  =new Date().getTime();

public  void bideTime(){

    try{Thread.sleep(500);}catch (Exception e){e.printStackTrace();}


    System.out.println("SINTIME1");
    long currTime = new Date().getTime();
    if(currState==-1){
        if(currTime-this.timeOfLastStateDisplay>5000){
            Conversation.printWSln("STATES", "STILL WAITING FOR PARTICIPANTS TO LOG IN AND FOR START");
            this.timeOfLastStateDisplay=new Date().getTime();
        }
        System.out.println("SINTIME1b");
        return;
    }
    if(currState>=times.length){
        if(currTime-this.timeOfLastStateDisplay>5000){
            Conversation.printWSln("Main", "CAN'T GO TO NEXT STATE..NO MORE STATES TO TRANSITION TO");
            this.timeOfLastStateDisplay=new Date().getTime();
        }
        System.out.println("SINTIME1c");
        return;
    }

    System.out.println("SINTIME2");
    if( currState<times.length-1){
        long transitionTime = times[currState]*60000;
        long runningTime = currTime-startTime;
        runningTime = runningTime * Debug.debugGROOPSpeedUpTime;

        if(runningTime>=transitionTime) {
             Conversation.printWSln("Main", "TRANSITIONING FROM STATE "+currState+" to "+ (currState+1) +" runningTime = "+runningTime+" transitionTime:"+transitionTime+" currTime"+currTime);
             System.out.println("SETINTIME222");
             this.setState(currState+1);
             System.out.println("SETOUTTIME2223");
        }


        if(currTime-this.timeOfLastStateDisplay>5000){
            Conversation.printWSln("Main", "Transitioning To next state in:"+((transitionTime-runningTime)/1000)+" times["+currState+"] ="+times[currState]);
            this.timeOfLastStateDisplay=new Date().getTime();
        }

    }

      System.out.println("SINTIME4b");

     double timeSpentInState = currTime -currStateStart ;
     double proportionOfStateComplete = timeSpentInState/(double)currStateLength;


     double percentageComplete = proportionOfStateComplete*100;
     percentageComplete = percentageComplete* Debug.debugGROOPSpeedUpTime;
     //if(Debug.debugGROOP)System.err.println("GRROOPTIMER-CURRTIME:"+currTime);
     //if(Debug.debugGROOP)System.err.println("GRROOPTIMER-TIMESPENT:"+timeSpentInState);
     // if(Debug.debugGROOP)System.err.println("GRROOPTIMERB-CURRSTATESTART"+currStateStart);
     //if(Debug.debugGROOP)System.err.println("GRROOPTIMERC-CURRSTATELENGTH"+currStateLength);
     //if(Debug.debugGROOP)System.err.println("GRROOPTIMERPROPORTION:"+proportionOfStateComplete);
      System.out.println("SINTIME5b");
       c.changeJProgressBarsOfAllParticipants("objectlist", "Time till speaker change ", Color.green, 100-(int)percentageComplete);
       System.out.println("SINTIME6b");
       inc++;
}

int inc =0;


//List of times - at each time threshold, as specified in the single list, one it simply tries to set the state to the next one
//If there is a trial with that state number, it sets to that time


public synchronized void nextState(){
    Conversation.printWSln("Main", "Manually Changing from "+currState+" to"+ currState+1);
    setState(currState+1);
}


public synchronized void setState(int newState){
    this.generateNEWAliases();
    currState=newState;
    this.currStateStart= new Date().getTime();
    if(currState<times.length-1){
             this.currStateLength= (times[currState+1]-times[currState])*60000;
             this.doPairings(newState, groups);
    }
    else if (currState<times.length){
             
    }
    else{
             this.currStateLength= 9999999999999L;
             Conversation.printWSln("Main", "Can't transition any more...final state:");

   }
}


static Hashtable aliases  = new Hashtable();
static Random r2 = new Random();
static Vector usedNames = new Vector();


 static public String generateNewName(){
     boolean nameAlreadyChosen = false;
     int sNameIndex = r2.nextInt(10000);
     String sName = "Participant"+sNameIndex;
     for(int i=0;i<usedNames.size();i++){
         String s = (String)usedNames.elementAt(i);
         if(s.equalsIgnoreCase(sName)){
             nameAlreadyChosen = true;
         }
     }
     while (nameAlreadyChosen){
         sNameIndex = r2.nextInt(10000);
         sName = "Participant"+sNameIndex;
         for(int i=0;i<usedNames.size();i++){
            String s = (String)usedNames.elementAt(i);
            if(i==usedNames.size()-1){
                nameAlreadyChosen = false;
            }
         }
     }
     return sName;

 }


 static public String getAlias(Participant p){
       String currName = (String)aliases.get(p);
       if(currName==null) {
           String newName = generateNewName();
           aliases.put(p, newName);
           usedNames.addElement(newName);
           return newName;
       }
       return currName;
 }



 public void generateNEWAliases(){
     Vector vP = c.getParticipants().getAllParticipants();
     for(int i=0;i<vP.size();i++){
          Participant p = (Participant )vP.elementAt(i);
          String newName = generateNewName();
          aliases.put(p, newName);
          usedNames.addElement(newName);
     }
 }



 public void sortVectorsBetween(){
     this.sortVEctorsWithin();
 }


 public void sortVEctorsWithin(){
     Vector output = new Vector();
     for(int i=0;i<seating.length;i++){
           Participant[] table = seating[i];
           if(table[0]!=null){
               output.addElement(table[0]);
           }
      }
     for(int i=0;i<seating.length;i++){
           Participant[] table = seating[i];
           if(table[1]!=null){
               output.addElement(table[1]);
           }
      }

     c.getParticipants().changeParticipants(output);
 }






 Participant[][] seating =  {{null,null}, {null,null}, {null,null}, {null,null}, {null,null},{null,null},{null,null},{null,null},{null,null},{null,null},{null,null},{null,null},{null,null},{null,null},{null,null},{null,null},{null,null}, {null,null},{null,null},{null,null},{null,null}};
 public void addParticipantToSeating(Participant p){
     String pID = p.getParticipantID();
     if       (pID.equalsIgnoreCase("T0LT0L"))  seating [0][0] = p;
     else if  (pID.equalsIgnoreCase("T0RT0R"))  seating [0][1] = p;

     else if  (pID.equalsIgnoreCase("T1LT1L"))  seating [1][0] = p;
     else if  (pID.equalsIgnoreCase("T1RT1R"))  seating [1][1] = p;

     else if  (pID.equalsIgnoreCase("T2LT2L"))  seating [2][0] = p;
     else if  (pID.equalsIgnoreCase("T2RT2R"))  seating [2][1] = p;

     else if  (pID.equalsIgnoreCase("T3LT3L"))  seating [3][0] = p;
     else if  (pID.equalsIgnoreCase("T3RT3R"))  seating [3][1] = p;

     else if  (pID.equalsIgnoreCase("T4LT4L"))  seating [4][0] = p;
     else if  (pID.equalsIgnoreCase("T4RT4R"))  seating [4][1] = p;

     else if  (pID.equalsIgnoreCase("T5LT5L"))  seating [5][0] = p;
     else if  (pID.equalsIgnoreCase("T5RT5R"))  seating [5][1] = p;

     else if  (pID.equalsIgnoreCase("T6LT6L"))  seating [6][0] = p;
     else if  (pID.equalsIgnoreCase("T6RT6R"))  seating [6][1] = p;

     else if  (pID.equalsIgnoreCase("T7LT7L"))  seating [7][0] = p;
     else if  (pID.equalsIgnoreCase("T7RT7R"))  seating [7][1] = p;

     else if  (pID.equalsIgnoreCase("T8LT8L"))  seating [8][0] = p;
     else if  (pID.equalsIgnoreCase("T8RT8R"))  seating [8][1] = p;

     else if  (pID.equalsIgnoreCase("T9LT9L"))  seating [9][0] = p;
     else if  (pID.equalsIgnoreCase("T9RT9R"))  seating [9][1] = p;

     else if  (pID.equalsIgnoreCase("T10LT10L"))  seating [10][0] = p;
     else if  (pID.equalsIgnoreCase("T10RT10R"))  seating [10][1] = p;

     else if  (pID.equalsIgnoreCase("T11LT11L"))  seating [11][0] = p;
     else if  (pID.equalsIgnoreCase("T11RT11R"))  seating [11][1] = p;

     else if  (pID.equalsIgnoreCase("T12LT12L"))  seating [12][0] = p;
     else if  (pID.equalsIgnoreCase("T12RT12R"))  seating [12][1] = p;

     else if  (pID.equalsIgnoreCase("T13LT13L"))  seating [13][0] = p;
     else if  (pID.equalsIgnoreCase("T13RT13R"))  seating [13][1] = p;

     else if  (pID.equalsIgnoreCase("T14LT14L"))  seating [14][0] = p;
     else if  (pID.equalsIgnoreCase("T14RT14R"))  seating [14][1] = p;

     else if  (pID.equalsIgnoreCase("T15LT15L"))  seating [15][0] = p;
     else if  (pID.equalsIgnoreCase("T15RT15R"))  seating [15][1] = p;
     else {
         System.err.println("CAN'T MAKE SENSE OF THE SEATING ARRANGEMENT "+p.getParticipantID()+" +"+p.getUsername());
         Conversation.printWSln("Main", "CAN'T MAKE SENSE OF THE SEATING ARRANGEMENT "+p.getParticipantID()+" +"+p.getUsername());
     }

 }


}







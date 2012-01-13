/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package diet.server.ConversationController;

import diet.client.WebpageAndImageDisplay;
import diet.message.MessageChatTextFromClient;
import diet.message.MessageKeypressed;
import diet.message.MessagePopup;
import diet.message.MessageWYSIWYGDocumentSyncFromClientInsert;
import diet.parameters.ExperimentSettings;
import diet.server.Conversation;
import diet.server.Participant;
import java.awt.Color;
import java.io.File;
import java.util.Arrays;
import java.util.Date;
import java.util.Hashtable;
import java.util.Vector;

/**
 *
 * @author sre
 */
public class CCPICTURESTIMULI5 extends DefaultConversationController{

    String sbfolder = "stimuliset8";
    public String redbackground = "<html><head><style type=\"text/css\">body {color: white; background: red;}div { font-size: 200%;}</style></head><body><div>INCORRECT </div></body></html>";
    
    WebpageAndImageDisplay wid = new WebpageAndImageDisplay(null);
  
    Score sc = new Score();
    
    String sUSERDIR = System.getProperty("user.dir")+File.separatorChar+"webserver"+File.separatorChar+sbfolder;
    File f = new File(sUSERDIR);
    File[] flist = f.listFiles();

    
    
    public static int mode =0;
    
    public static int mode0START =0;
    public static int mode1TESTPHASE_DIRECTOR = 1;
    public static int mode2TESTPHASE_POSTDIRECTOR_PREMATCHER = 2;
    public static int mode3TESTPHASE_MATCHER =3;
    public static int mode4POSTTESTPHASE =4;
    public static int mode5TRAININGPHASE = 5;
    public static int mode6TRAININGPHASECOMPLETE = 6;
    public static int mode7TESTPHASE_DIRECTOR = 7;
    public static int mode8TESTPHASE_POSTDIRECTOR_PREMATCHER = 8;
    public static int mode9TESTPHASE_MATCHER = 9;
    

    @Override
    public boolean requestParticipantJoinConversation(String participantID) {
        System.err.println("CHECKING PARTICIPANTID AGAIN");
        if(participantID.startsWith("-"))return false;
        return true;
    }

    Participant matcher;
    Participant director;

    File directorFile;
    File matcherFile;

    int matchercorrectFaceNumber ;
    int directorcorrectFaceNumber;

    String fileName;

    String imgsrcD ="";
    String imgsrcM = "";

    int currSetMatcher;
    int currSetDirector;

    int numberOfIncorrectGuessesForCurrentSet =0;
    public int maxNumberOfIncorrectGuesses = 1;

    public int numberOfConsecutiveCorrect =0;
    //public int numberOfCorrectguesses=0;


    public synchronized String gotoSet(int srch, boolean alternateDM){
         if(alternateDM){
            Participant temp = this.director;
            this.director = this.matcher;
            matcher = temp;
        }


        String num =""+srch;
        if(srch<10){
            num="0" + num;
        }
        boolean foundmatcher = false;
        boolean founddirector = false;

        int searchindex = 0;
        String matcherSelection ="";
        String directorSelection = "";
        while((!foundmatcher|!founddirector)&searchindex<flist.length){
                    File f = flist[searchindex];
                    //System.out.println("--"+f.getName());
                    System.err.println("FILENAMEIS"+f.getName());
                    System.err.println("LOoKIING FOR----d0"+num+"_");
                    if(f.getName().startsWith("m0"+num+"_")){
                       //System.exit(-22);
                        foundmatcher = true;
                        matcherFile = f;
                        matcherSelection = f.getName().substring(f.getName().length()-6,f.getName().length()-4);
                    }
                    if(f.getName().startsWith("d0"+num+"_")){
                        founddirector = true;
                        directorFile = f;
                        directorSelection = f.getName().substring(f.getName().length()-6,f.getName().length()-4);
                    }
                    searchindex++;
        }

       
        try{
        this.directorcorrectFaceNumber=Integer.parseInt(directorSelection);
        this.matchercorrectFaceNumber=Integer.parseInt(matcherSelection);

        //System.err.println("matchernumber"+matcherNumber+"-------DirectorNumber:"+directorNumber);System.exit(-5);
        }catch(Exception e){
            System.err.println("ERROR PARSING"+directorSelection+"-"+matcherSelection);
            System.exit(-222);
        }
        imgsrcD = ":81/"+sbfolder+"/"+directorFile.getName();
        imgsrcM = ":81/"+sbfolder+"/"+matcherFile.getName();



        // urlD = this.redbackground;

        try{
            if(matcher ==null)System.exit(-1231235);
            c.changeWebpageImage_OnServer(matcher, "ID1", imgsrcM);
            c.changeWebpageImage_OnServer(director, "ID1", imgsrcD);



           
            wid.changeWebpage(matcher.getUsername(), "<html><img src='"+"http://localhost:81/"+sbfolder+"/"+matcherFile.getName() + "'></img>", "", "");
            wid.changeWebpage(director.getUsername(), "<html><img src='"+"http://localhost:81/"+sbfolder+"/"+directorFile.getName()+ "'></img>", "", "");
            //c.changeWebpageTextAndColour(matcher, "ID1","correct", "red", "white");
            numberOfIncorrectGuessesForCurrentSet =0;
        }

        catch(Exception e){
            e.printStackTrace();
            System.err.println("ERRORINMETHOD");
        }
        currSetMatcher = srch;
        currSetDirector = srch;
        c.saveDataToConversationHistory("DATA:"+srch+"-"+directorFile.getName()+matcherFile.getName());
        return "";
    }


    //int director1Number;
    //int director2Number;

    //String testIMGSRCD1;
    //String testIMGSRCD2;
    
    
     //File director1File;
     //File  director2File;
    
    //Participant director1;
    //Participant director2;
    
    
    
    
    
    
    
    



    public synchronized void processSelection(Participant p,String t){
        if(p!=matcher){
            c.sendArtificialTurnToRecipient(p, "****You shouldn't be making this selection - your partner needs to make the selection*****", 0);
            return;
        }

        try{
             t = t.replaceAll(" " , "");
             int i = Integer.parseInt(t.substring(1, t.length()));
             if(i == this.matchercorrectFaceNumber){
                   // String remainingTrials = "You have "+this.numberOfTrainingTrials - (this.)
                     sc.updateSuccess();
                     
                     //c.changeWebpageTextAndColour(matcher, "ID1","Correct.\n You have made "+this.numberOfConsecutiveCorrect+ " consecutive correct guesses", "green", "white");
                     //c.changeWebpageTextAndColour(director, "ID1","Correct.\nYou have made "+this.numberOfConsecutiveCorrect+ " consecutive correct guesses", "green", "white");
                     //c.sendArtificialTurnToRecipient(director, "****Correct. You have made "+this.numberOfConsecutiveCorrect+ " consecutive guesses****", 0);
                     //c.sendArtificialTurnToRecipient(matcher,  "****Correct. You have made "+this.numberOfConsecutiveCorrect+ " consecutive guesses****", 0);
                     
                     c.changeWebpageTextAndColour(matcher, "ID1","Correct.\n Your score is "+sc.getScore(), "green", "white");
                     c.changeWebpageTextAndColour(director, "ID1","Correct.\n Your score is "+sc.getScore(), "green", "white");
                     c.sendArtificialTurnToRecipient(director, "Correct. Your score is "+sc.getScore(), 0);
                     c.sendArtificialTurnToRecipient(matcher,  "Correct. Your score is "+sc.getScore(), 0);
                     
                     
                     
                     Conversation.printWSln("Main", p.getUsername()+" made the CORRECT selection of "+i+"...moving to trial"+this.currSetMatcher+"--"+this.currSetMatcher+"---CONSECUTIVECORRECT:"+this.numberOfConsecutiveCorrect+"---INCORRECTGUESSESFORCURRENTSET"+this.numberOfIncorrectGuessesForCurrentSet+ "Score is: "+sc.getScore());
                     Conversation.printWSln("Main", matcher.getUsername()+"---"+director.getUsername());
                     Thread.sleep(5000);
                     c.changeWebpageTextAndColour(matcher, "ID1","", "black", "black");
                     c.changeWebpageTextAndColour(director, "ID1","", "black", "black");
                     if(this.numberOfIncorrectGuessesForCurrentSet==0)numberOfConsecutiveCorrect ++;
                     this.gotoSet(currSetDirector+1, true);
             }
             else{
                 sc.updateUnsuccessful();
                 numberOfConsecutiveCorrect=0;
                 if(this.numberOfIncorrectGuessesForCurrentSet>=this.maxNumberOfIncorrectGuesses){
                     c.changeWebpageTextAndColour(matcher, "ID1","Incorrect\nyou used all your guesses\n Your score is "+sc.getScore(), "red", "white");
                     c.changeWebpageTextAndColour(director, "ID1","Incorrect\nyou used all your guesses\n Your score is "+sc.getScore(), "red", "white");
                     c.sendArtificialTurnToRecipient(director, "****Incorrect. You used all your guesses**** Your score is "+sc.getScore(), 0);
                     c.sendArtificialTurnToRecipient(matcher, "****Incorrect. You used all your guesses**** Your score is "+sc.getScore(), 0);
                     Conversation.printWSln("Main", p.getUsername()+" made INCORRECT selection of "+i+"...EXHAUSTED ALL THE GUESSES...MOVING TO TRIAL:"+this.currSetDirector);

                     Thread.sleep(2000);
                     c.changeWebpageTextAndColour(matcher, "ID1","", "black", "black");
                     c.changeWebpageTextAndColour(director, "ID1","", "black", "black");
                     this.gotoSet(currSetDirector+1, true);
                 }
                 else{
                    Conversation.printWSln("Main", p.getUsername()+ "made INCORRECT selection of"+i+"..still has "+(this.maxNumberOfIncorrectGuesses-this.numberOfIncorrectGuessesForCurrentSet)+"..guesses left on this trial."+"Your score is "+sc.getScore());
                    numberOfIncorrectGuessesForCurrentSet++;
                 }

                  c.sendArtificialTurnToRecipient(director, "****INCORRECT SELECTION...You have "+ (maxNumberOfIncorrectGuesses-numberOfIncorrectGuessesForCurrentSet)     +" guesses left*****"+"Your score is "+sc.getScore(), 0);
                  c.sendArtificialTurnToRecipient(matcher, "****INCORRECT SELECTION...You have "+ (maxNumberOfIncorrectGuesses-numberOfIncorrectGuessesForCurrentSet)     +" guesses left*****"+"Your score is "+sc.getScore(), 0);
                  
             }

         }catch (Exception e){
             c.sendArtificialTurnToRecipient(p, "****YOU MADE A TYPO - TYPE BACKSLASH FOLLOWED BY THE NUMBER *****", 0);
         }
    }

   

    @Override
    public void initialize(Conversation c, ExperimentSettings expSettings) {
        super.initialize(c, expSettings);
        Arrays.sort(flist);
    }



    

    @Override
    public void processKeyPress(Participant sender, MessageKeypressed mkp) {
         if(this.mode==mode5TRAININGPHASE)super.processKeyPress(sender,mkp);
         //c.saveKeypressToFile(sender, mkp);
    }

    
    @Override
    public void processPopupResponse(final Participant origin, MessagePopup mp) {
           this.popupReceiptcount++;
           if(this.popupReceiptcount==2 & this.mode==mode0START){
               popupReceiptcount=0;
               this.initiateFirstTestPhaseDirector(this.firstteststartindex, this.firsttestlength);
           }
           else if(this.popupReceiptcount==2 & this.mode==this.mode2TESTPHASE_POSTDIRECTOR_PREMATCHER){
               popupReceiptcount=0;
               this.initiateFirstTestPhaseMatcher(this.firstteststartindex, this.firsttestlength, startofnormaltrials);
           }
           else if(this.popupReceiptcount==2 & this.mode==this.mode4POSTTESTPHASE){
               popupReceiptcount=0;
               this.initiateTrainingPhase(startofnormaltrials);
               
           }
           else if(this.popupReceiptcount==2 & this.mode==this.mode6TRAININGPHASECOMPLETE){
               popupReceiptcount=0;
               this.initiateSecondTestPhaseDirector(this.secondteststartindex, this.secondtestlength);
           }
           else if(this.popupReceiptcount==2 & this.mode==this.mode8TESTPHASE_POSTDIRECTOR_PREMATCHER){
               popupReceiptcount=0;
               this.initiateSecondTestPhaseMatcher(this.secondteststartindex, this.secondtestlength, startofnormaltrials);
           }
          
           
           
    }
    
     
    

    
    public void showTestPopupsOnClient(String popupMessage){
        this.popupReceiptcount=0;
        c.showPopupOnClientQueryInfo(director, popupMessage, new String[]{"START"}, "Test trial", 0);
        c.showPopupOnClientQueryInfo(matcher, popupMessage, new String[]{"START"}, "Test trial", 0);   
    }

    
    
    
    public synchronized String gotoSet2D(int srch){
         /////
    /*     Vector v = new Vector();
         v.addElement(directorFile.getName());
         c.saveDataToFile("DESCRIPTION", director.getParticipantID(), director.getUsername(), ""+currSetDirector, new Date().getTime(), new Date().getTime(), "", v);
         v = new Vector();
         v.addElement(matcherFile.getName());
         c.saveDataToFile("DESCRIPTION", matcher.getParticipantID(), matcher.getUsername(), ""+currSetMatcher, new Date().getTime(), new Date().getTime(), "", v);
         ////
      */  
         currSetDirector = srch;
         currSetMatcher= 10+srch;
        
        
        String numD =""+srch;
        if(srch<10){
            numD="0" + numD;
        }
        String numM =""+(srch+10);
       
         
        
        
        
        
        boolean foundmatcher = false;
        boolean founddirector = false;

        int searchindex = 0;
        String matcherSelection ="";
        String directorSelection = "";
        while((!foundmatcher|!founddirector)&searchindex<flist.length){
                    File f = flist[searchindex];
                    if(f.getName().startsWith("d0"+numM+"_")){
                       //System.exit(-22);
                        foundmatcher = true;
                        matcherFile = f;
                        matcherSelection = f.getName().substring(f.getName().length()-6,f.getName().length()-4);
                    }
                    if(f.getName().startsWith("d0"+numD+"_")){
                        founddirector = true;
                        directorFile = f;
                        directorSelection = f.getName().substring(f.getName().length()-6,f.getName().length()-4);
                    }
                    searchindex++;
        }

       
        try{
        this.directorcorrectFaceNumber=Integer.parseInt(directorSelection);
        this.matchercorrectFaceNumber=Integer.parseInt(matcherSelection);

        //System.err.println("matchernumber"+matcherNumber+"-------DirectorNumber:"+directorNumber);System.exit(-5);
        }catch(Exception e){
            System.err.println("ERROR PARSING"+directorSelection+"-"+matcherSelection);
            System.exit(-223);
        }
        imgsrcD = ":81/"+sbfolder+"/"+directorFile.getName();
        imgsrcM = ":81/"+sbfolder+"/"+matcherFile.getName();



        // urlD = this.redbackground;

        try{
            if(matcher ==null)System.exit(-1231235);
            c.changeWebpageImage_OnServer(matcher, "ID1", imgsrcM);
            c.changeWebpageImage_OnServer(director, "ID1", imgsrcD);



           
            wid.changeWebpage(matcher.getUsername(), "<html><img src='"+"http://localhost:81/"+sbfolder+"/"+matcherFile.getName() + "'></img>", "", "");
            wid.changeWebpage(director.getUsername(), "<html><img src='"+"http://localhost:81/"+sbfolder+"/"+directorFile.getName()+ "'></img>", "", "");
            //c.changeWebpageTextAndColour(matcher, "ID1","correct", "red", "white");
            numberOfIncorrectGuessesForCurrentSet =0;
        }

        catch(Exception e){
            e.printStackTrace();
            System.err.println("ERRORINMETHOD");
        }
       
        c.saveDataToConversationHistory("DATA(2D):"+srch+"-"+directorFile.getName()+"--"+matcherFile.getName());
        return "";
    }


    public synchronized String gotoSet2M(int srch){
         currSetDirector = 10+srch;
         currSetMatcher=   srch;
        
        
        String numM =""+srch;
        if(srch<10){
            numM="0" + numM;
        }
        String numD =""+(srch+10);
       
         
        
        
        
        
        
        boolean foundmatcher = false;
        boolean founddirector = false;

        int searchindex = 0;
        String matcherSelection ="";
        String directorSelection = "";
        while((!foundmatcher|!founddirector)&searchindex<flist.length){
                    File f = flist[searchindex];
                    if(f.getName().startsWith("m0"+numM+"_")){
                        //System.exit(-22);
                        foundmatcher = true;
                        matcherFile = f;
                        matcherSelection = f.getName().substring(f.getName().length()-6,f.getName().length()-4);
                    }
                    if(f.getName().startsWith("m0"+numD+"_")){
                        founddirector = true;
                        directorFile = f;
                        directorSelection = f.getName().substring(f.getName().length()-6,f.getName().length()-4);
                    }
                    searchindex++;
        }

       //System.exit(-9);
        try{
       // this.matcherNumber=Integer.parseInt(matcherSelection);
        this.directorcorrectFaceNumber=Integer.parseInt(directorSelection);
        this.matchercorrectFaceNumber=Integer.parseInt(matcherSelection);

        //System.err.println("matchernumber"+matcherNumber+"-------DirectorNumber:"+directorNumber);System.exit(-5);
        }catch(Exception e){
            System.err.println("ERROR PARSING"+directorSelection+"-"+matcherSelection+"/---"+numD+"---"+numM);
            e.printStackTrace();
            System.exit(-223);
            
        }
        imgsrcD = ":81/"+sbfolder+"/"+directorFile.getName();
        imgsrcM = ":81/"+sbfolder+"/"+matcherFile.getName();



        // urlD = this.redbackground;

        try{
            if(matcher ==null)System.exit(-1231235);
            c.changeWebpageImage_OnServer(matcher, "ID1", imgsrcM);
            c.changeWebpageImage_OnServer(director, "ID1", imgsrcD);



           
            wid.changeWebpage(matcher.getUsername(), "<html><img src='"+"http://localhost:81/"+sbfolder+"/"+matcherFile.getName() + "'></img>", "", "");
            wid.changeWebpage(director.getUsername(), "<html><img src='"+"http://localhost:81/"+sbfolder+"/"+directorFile.getName()+ "'></img>", "", "");
            //c.changeWebpageTextAndColour(matcher, "ID1","correct", "red", "white");
            numberOfIncorrectGuessesForCurrentSet =0;
        }

        catch(Exception e){
            e.printStackTrace();
            System.err.println("ERRORINMETHOD");
        }
        
       // c.saveDataToConversationHistory("DATA(2M):"+srch+"-"+directorFile.getName()+"--"+matcherFile.getName());
        c.saveDataToConversationHistory("DATA(2M):"+srch+"-"+director.getUsername()+"--"+directorFile.getName()+"--"+matcher.getUsername()+"---"+matcherFile.getName());
        return "";
    }

    
    //int numDnum ;
    //int numMnum ; 

    
    String subfolder = "";

    @Override
    public void participantJoinedConversation(Participant p) {    
        
        c.displayNEWWebpage(p, "ID1", "Faces", "", 1162, 695, false,false);
        c.changeJProgressBar(p, "ID1", "", Color.gray, 100);
        wid.displayNEWWebpage(p.getUsername(),  p.getUsername(), "", 1162, 534, false,false,false);

        if(c.getParticipants().getAllParticipants().size()<2){
             director = p;
             c.changeWebpage(p, "ID1", "", "Waiting for other participant to log in...please wait", "");
         }
        else if(c.getParticipants().getAllParticipants().size()==2){
            matcher=p;  
            //initiateTestPhaseDirector(1,3,20);
            showTestPopupsOnClient(message1BeforeTestSetDirector);
            // this.initiateTrainingPhase(21);
            //this.initiateSecondTestPhase();
        }
        

        
       


       
    }

    @Override
    public void participantRejoinedConversation(Participant p) {
        
    }


    
    @Override
    public void processLoop() {
        super.processLoop();
        if(mode==CCPICTURESTIMULI5.mode5TRAININGPHASE)c.resetFromIsTypingtoNormalChatAllAllowedParticipants(super.getIsTypingTimeOut());
        
        //if(matcher!=null)c.sendArtificialTurnToRecipient(matcher, "MESSAGE", 0);
        //if(director!=null)c.sendArtificialTurnToRecipient(director, "MESSAGE", 0);
    }

    
    
    
    
    

    @Override
    public void processChatText(Participant sender, MessageChatTextFromClient mct) {
        
        if(mode==CCPICTURESTIMULI5.mode5TRAININGPHASE&&mct.getText().startsWith("/")){
           this.processSelection(sender, mct.getText());
        }
        else if (mode==CCPICTURESTIMULI5.mode5TRAININGPHASE){
             super.processChatText(sender, mct);
        }
        
        
        
    }


  
    
  
  
public synchronized void initiateFirstTestPhaseDirector(final int startindex, final int numberoftrials){
    
    Thread t = new Thread(){
        public void run(){
        mode=CCPICTURESTIMULI5.mode1TESTPHASE_DIRECTOR;
        
        for(int i=startindex;i<startindex+numberoftrials;i++){
                  gotoSet2D(i);
                  singleDirectorTrial(i);
                  saveData();
                  try { Thread.sleep(pauseBetweenSetsDirectorTrial);} catch (Exception e){ e.printStackTrace();}   
        }
        mode=CCPICTURESTIMULI5.mode2TESTPHASE_POSTDIRECTOR_PREMATCHER;
        showTestPopupsOnClient(message2AfterTestSetDirectorBeforeTestSetMatcher);
        c.changeWebpageTextAndColour(director, "ID1",message2AfterTestSetDirectorBeforeTestSetMatcherWINDOW,"black", "white");
        c.changeWebpageTextAndColour(matcher, "ID1",message2AfterTestSetDirectorBeforeTestSetMatcherWINDOW, "black", "white");
        //saveData();
        }};
         t.start();      
    }



public synchronized void initiateFirstTestPhaseMatcher(final int startindex, final int numberoftrials, final int returnindex){
        Thread t = new Thread(){
        public void run(){
        
        mode=CCPICTURESTIMULI5.mode3TESTPHASE_MATCHER;

        for(int i=startindex;i<startindex+numberoftrials;i++){
                  singleMatcherTrial(i);
                  try { Thread.sleep(pauseBetweenSetsMatcherTrial);} catch (Exception e){ e.printStackTrace();}   
        }
        
        mode = CCPICTURESTIMULI5.mode4POSTTESTPHASE;
        
        
        
       c.changeWebpageTextAndColour(director, "ID1",message3AfterTestPhaseWindow, "black", "white");
       c.changeWebpageTextAndColour(matcher, "ID1",message3AfterTestPhaseWindow, "black", "white");
       
       c.changeJProgressBar(director, "ID1", "", Color.gray, 0);
       c.changeJProgressBar(matcher, "ID1", "",Color.GRAY,0);
       
       showTestPopupsOnClient(message3AfterTestPhase);
       
       c.changeClientInterface_clearMaintextEntryWindow(director);
       c.changeClientInterface_clearMaintextEntryWindow(matcher);
        
      
        
        
    
        
        
        }};
         t.start();      
    }


public synchronized void initiateSecondTestPhase(){
    this.showTestPopupsOnClient(this.message4BeforeSecondTestSetDirector);
    this.mode=this.mode6TRAININGPHASECOMPLETE;
}



public synchronized void initiateSecondTestPhaseDirector(final int startindex, final int numberoftrials){
    
    Thread t = new Thread(){
        public void run(){
        mode=CCPICTURESTIMULI5.mode7TESTPHASE_DIRECTOR;
        
        for(int i=startindex;i<startindex+numberoftrials;i++){
                  gotoSet2D(i);
                  singleDirectorTrial(i);
                  saveData();
                  try { Thread.sleep(pauseBetweenSetsDirectorTrial);} catch (Exception e){ e.printStackTrace();}   
        }
        mode=CCPICTURESTIMULI5.mode8TESTPHASE_POSTDIRECTOR_PREMATCHER;
        showTestPopupsOnClient(message5AfterSecondTestSetDirectorBeforeTestSetMatcher);
        c.changeWebpageTextAndColour(director, "ID1",message2AfterTestSetDirectorBeforeTestSetMatcherWINDOW,"black", "white");
        c.changeWebpageTextAndColour(matcher, "ID1",message2AfterTestSetDirectorBeforeTestSetMatcherWINDOW, "black", "white");
        //saveData();
        }};
         t.start();      
    }



public synchronized void initiateSecondTestPhaseMatcher(final int startindex, final int numberoftrials, final int returnindex){
        Thread t = new Thread(){
        public void run(){
        mode = CCPICTURESTIMULI5.mode9TESTPHASE_MATCHER;
        for(int i=startindex;i<startindex+numberoftrials;i++){
                  singleMatcherTrial(i);
                  try { Thread.sleep(pauseBetweenSetsMatcherTrial);} catch (Exception e){ e.printStackTrace();}   
        }
        
        
       c.changeWebpageTextAndColour(director, "ID1",message3AfterTestPhaseWindow, "black", "white");
       c.changeWebpageTextAndColour(matcher, "ID1",message3AfterTestPhaseWindow, "black", "white");
       
       c.changeJProgressBar(director, "ID1", "", Color.gray, 0);
       c.changeJProgressBar(matcher, "ID1", "",Color.GRAY,0);
       
       showTestPopupsOnClient(message3AfterTestPhase);
      
        
      
        
        
    
        
        
        }};
         t.start();      
    }




  public synchronized void initiateTrainingPhase(final int trainingphasestart){
      this.mode=this.mode5TRAININGPHASE;
      Thread t = new Thread(){
        public void run(){
             c.sendAndEnableLabelDisplayToALLStatusWindowsOfParticipant(matcher, "Status: OK", false, true);
             
             gotoSet(trainingphasestart,true);
             c.changeClientInterface_allowENTERSEND(matcher, true);
             c.changeClientInterface_allowENTERSEND(director, true);
        
             c.changeClientInterface_enableTextEntry(matcher);
             c.changeClientInterface_enableTextEntry(director);
  
        }};
        t.start();      
  }


public void saveWhoIsLookingAtWhat(){
    String directorIsLookingAt = "";
    String matcherIsLookingAt =  "";
    
    try{
    directorIsLookingAt = ""+this.director.getUsername()+": "+this.directorFile.getName()+"---"+this.directorcorrectFaceNumber;
    matcherIsLookingAt = ""+this.matcher.getUsername()+": "+this.matcherFile.getName()+"---"+this.matchercorrectFaceNumber;
    }
    catch(Exception e){
       
    }
    finally{
       c.saveDataToFile("WHOISLOOKINGAT", director.getUsername(), director.getUsername(), new Date().getTime(), new Date().getTime(), "(D)"+ directorIsLookingAt, null);
       c.saveDataToFile("WHOISLOOKINGAT", matcher.getUsername(), matcher.getUsername(), new Date().getTime(), new Date().getTime(), "(M)"+ matcherIsLookingAt, null);
    }
}


public synchronized void singleMatcherTrial(int startIndex){
    long startTime = new Date().getTime();
    gotoSet2M(startIndex); 
    saveWhoIsLookingAtWhat();
    c.changeClientInterface_clearMaintextEntryWindow(matcher);
    c.changeClientInterface_clearMaintextEntryWindow(director);
    
    String sD = (String)rt.getDescription(matcher,currSetDirector);
    String sM = (String)rt.getDescription(director, currSetMatcher);
    
    String[] sMArray = sM.split("\n");
    String[] sDArray = sD.split("\n");
    
    for(int i = 0; i < sDArray.length;i++){
        if(sDArray[i]!=null)c.sendArtificialTurnToRecipient(matcher, sDArray[i], 0);
    }
    String chOutPutForD = matcher.getUsername()+" is now looking at "+ this.matcherFile.getName()+"..having to select "+this.matchercorrectFaceNumber+ " using this description:"+ sD.replaceAll("\\r\\n|\\r|\\n", "(NEWLINE)");
    c.saveDataToFile("WHOISLOOKINGAT", "server", "server", new Date().getTime(), new Date().getTime(), chOutPutForD, null);
    
    
    for(int i=0;i<sMArray.length;i++){
        if(sMArray[i]!=null)c.sendArtificialTurnToRecipient(director, sMArray[i], 0);
    }
     
    String chOutPutForM = director.getUsername()+" is now looking at "+ this.directorFile.getName()+"..having to select "+directorcorrectFaceNumber+ " using this description:"+ sM.replaceAll("\\r\\n|\\r|\\n", "(NEWLINE)");
    c.saveDataToFile("WHOISLOOKINGAT", "server", "server", new Date().getTime(), new Date().getTime(), chOutPutForM, null);
    
    
    System.err.println("sD: "+sD+" director: "+director.getUsername()+"..."+currSetDirector);
    System.err.println("sM: "+sM+" matcher: "+matcher.getUsername()+ "..."+currSetMatcher);
    //System.exit(-5);//.println("sM: "+sM);
    
    c.changeClientInterface_allowENTERSEND(director,true);
    c.changeClientInterface_allowENTERSEND(matcher,true);
        
     c.changeClientInterface_enableTextEntry(matcher);
     c.changeClientInterface_enableTextEntry(director);
    
    c.sendLabelDisplayToParticipantInOwnStatusWindow(matcher, "Please choose which face is being described - write the number below", true);
    c.sendLabelDisplayToParticipantInOwnStatusWindow(director, "Please choose which face is being described - write the number below", true);
        
     
     
    while(new Date().getTime()-startTime < lengthOfEachMatcherTrial){
            long proportionOfTrialElapsed = 100*(new Date().getTime()-startTime)/this.lengthOfEachMatcherTrial;
            Color cColor = Color.GREEN;
            if(proportionOfTrialElapsed > 50) cColor=Color.ORANGE;
            if(proportionOfTrialElapsed > 80) cColor=Color.RED;
            long timeleftsecs = ((startTime + lengthOfEachMatcherTrial)-new Date().getTime())/1000;
            c.changeJProgressBar(director, "ID1", "You have "+(timeleftsecs)+" seconds remaining", cColor, 100-(int)proportionOfTrialElapsed);
            c.changeJProgressBar(matcher, "ID1", "You have "+(timeleftsecs)+" seconds remaining", cColor,  100-(int)proportionOfTrialElapsed);
             try { wait(500);} catch (Exception e){ e.printStackTrace();} 
        }
    
    
     try{
            c.changeClientInterface_disableTextEntry(matcher);
            c.changeClientInterface_disableTextEntry(director);
            c.sendLabelDisplayToParticipantInOwnStatusWindow(matcher, "Loading next set of faces...please wait", true);
            c.sendLabelDisplayToParticipantInOwnStatusWindow(director, "Loading next set of faces...please wait", true);
            
            
            c.changeClientInterface_clearTextEntryField(director);
            c.changeClientInterface_clearTextEntryField(matcher);
            c.changeWebpageTextAndColour(director, "ID1","Loading next set of faces...please wait", "black", "white");
            c.changeWebpageTextAndColour(matcher, "ID1","Loading next set of faces...please wait", "black", "white");
           
        }catch (Exception e){
           e.printStackTrace();
        }
    
    
    
    System.err.println("(A)SINGLEMATCHERTRIALLOOKINGFOR: "+sM+"------"+matcherFile.getName()+currSetMatcher);
    System.err.println("(B)SINGLEMATCHERTRIALLOOKINGFOR: "+sD+"------"+directorFile.getName()+currSetDirector);
    
    
    
}


public void saveData(){
    Vector v = c.getParticipants().getAllParticipants();
    for(int i=0;i<v.size();i++){
        Participant p = (Participant)v.elementAt(i);
        Hashtable participantsDescriptions = rt.getParticipantsDescriptions(p);
        Object[] o= participantsDescriptions.keySet().toArray();
        for(int j=0;j<o.length;j++){
            Integer isKEY = (Integer)o[j];
            String value = ((String)participantsDescriptions.get(isKEY)).replaceAll("\\r\\n|\\r|\\n", "[NEWLINE]");;
            if (value==null)value="(NULL)";
            System.err.println("VALUENINCOLLECTION"+i+":::::"+isKEY);
            c.saveDataToFile("DESCRIPTION", p.getUsername(), p.getUsername(), new Date().getTime(), new Date().getTime(), isKEY+"---"+value, null);
        } 
    }
    
    
}



public synchronized void singleDirectorTrial(int startIndex){
        saveWhoIsLookingAtWhat();
        
        long startTime = new Date().getTime();
        
         c.changeClientInterface_enableTextEntry(matcher);
         c.changeClientInterface_enableTextEntry(director);
        
        
        c.sendLabelDisplayToParticipantInOwnStatusWindow(matcher, "Please type your description of the face into the box below", true);
        c.sendLabelDisplayToParticipantInOwnStatusWindow(director, "Please type your description of the face into the box below", true);
        c.changeClientInterface_allowENTERSEND(director,false);
        c.changeClientInterface_allowENTERSEND(matcher,false);
        
       
        
        while(new Date().getTime()-startTime < lengthOfEachDirectorTrial){
            long proportionOfTrialElapsed = 100*(new Date().getTime()-startTime)/this.lengthOfEachDirectorTrial;
            Color cColor = Color.GREEN;
            if(proportionOfTrialElapsed > 50) cColor=Color.ORANGE;
            if(proportionOfTrialElapsed > 80) cColor=Color.RED;
            long timeleftsecs = ((startTime + lengthOfEachDirectorTrial)-new Date().getTime())/1000;
            c.changeJProgressBar(director, "ID1", "You have "+(timeleftsecs)+" seconds remaining", cColor, 100-(int)proportionOfTrialElapsed);
            c.changeJProgressBar(matcher, "ID1", "You have "+(timeleftsecs)+" seconds remaining", cColor,  100-(int)proportionOfTrialElapsed);
            try { wait(500);} catch (Exception e){ e.printStackTrace();} 
        }
        try{
            c.changeClientInterface_disableTextEntry(matcher);
            c.changeClientInterface_disableTextEntry(director);
            c.sendLabelDisplayToParticipantInOwnStatusWindow(matcher, "Loading next set of faces...please wait", true);
            c.sendLabelDisplayToParticipantInOwnStatusWindow(director, "Loading next set of faces...please wait", true);
            
            
            c.changeClientInterface_clearTextEntryField(director);
            c.changeClientInterface_clearTextEntryField(matcher);
            c.changeWebpageTextAndColour(director, "ID1","Loading next set of faces...please wait", "black", "white");
            c.changeWebpageTextAndColour(matcher, "ID1","Loading next set of faces...please wait", "black", "white");
           
        }catch (Exception e){
           e.printStackTrace();
        }

             
             
}

    @Override
    public synchronized void processWYSIWYGTextInserted(Participant sender, MessageWYSIWYGDocumentSyncFromClientInsert mWYSIWYGkp) {
        //super.processWYSIWYGTextInserted(sender, mWYSIWYGkp);
        System.err.println(mWYSIWYGkp.getTextTyped()+ "---"+mWYSIWYGkp.getAllTextInWindow());
        //if(this.mode==this.mode1TESTPHASE_DIRECTOR||this.mode==this.mode7TESTPHASE_DIRECTOR);
        
        if(sender==director && (this.mode==this.mode1TESTPHASE_DIRECTOR||this.mode==this.mode7TESTPHASE_DIRECTOR)){
            rt.saveDescription(director, currSetDirector, mWYSIWYGkp.getAllTextInWindow());
        }
        else if(sender==matcher &&(this.mode==this.mode1TESTPHASE_DIRECTOR||this.mode==this.mode7TESTPHASE_DIRECTOR)){
            rt.saveDescription(matcher, currSetMatcher, mWYSIWYGkp.getAllTextInWindow());
        }
        //c.saveDataToFile("", sbfolder, fileName, pauseBetweenSets, pauseBetweenSets, s, null);
        
       // String newLineRemoved = (mWYSIWYGkp.getAllTextInWindow().replaceAll("\n", "((NEWLINE))")).replaceAll("\\n", "(NEWLINE)").replaceAll("\\\n", "((NEWLINE))");
        String newLineRemoved = mWYSIWYGkp.getAllTextInWindow().replaceAll("\\r\\n|\\r|\\n", "(NEWLINE)");

        c.saveKeypressDocumentchangeToFile (sender, mWYSIWYGkp.getTimeStamp().getTime(),newLineRemoved);
        
    }
      
     //Hashtable htDescriptions = new Hashtable();
     //Hashtable htOrigins = new Hashtable();
     
     Repository rt = new Repository();
     
     int firstteststartindex =1;
     int firsttestlength = 2;
     
     int secondteststartindex = 6;
     int secondtestlength = 5;
     
     
     
     int  startofnormaltrials = 21;
     
     public long lengthOfEachDirectorTrial = 15000;//27000;
     public long lengthOfEachMatcherTrial = 15000;//27000;
     public long pauseBetweenSetsDirectorTrial = 1000;
     public long pauseBetweenSetsMatcherTrial = 1000;

     
     int popupReceiptcount = 0;    
     int numberOfTrainingTrials = 80;     
     int maxPointsbeforeFinalTest = 200;
     
     
     String message1BeforeTestSetDirector = "Please read this all of this message.\n"
                + "After you and the other participant press START,\n"
                + "the experiment will automatically play 5 sets of 4 faces\n"
                + "\n "
                + "One of the faces will have a box round it.\n\n"
                + "Your task is to describe the face so that your partner\n"
                + "would be able to identify which face you're talking about\n\n"
                + "\n"
                + "You only get "+(lengthOfEachDirectorTrial/1000)+ " seconds to describe each face\n\n"
                + "So please be as quick and as accurate as possible\n";
               
     
     String message2AfterTestSetDirectorBeforeTestSetMatcherWINDOW = "Next stage";
     String message2AfterTestSetDirectorBeforeTestSetMatcher = "OK. You have now finished describing faces\n"
             + "You will now be given your partner's descriptions of faces\n"
             + "and your task is to work out which face they are describing\n"
             + "For each set you will need to select the face you think they're describing\n"
             + "Only one of the faces fits the description - which one is it?";
     
     
    String message3AfterTestPhaseWindow = "The test phase is finished. <br>"
              + "In the next phase you need to cooperate with your partner<br> "
                 + "you see the same faces - one of you has a box round the face<br>"
                 + "the other person has a set of numbered faces<br>"
                 + "The person with the numbers needs to select the same face<br>"
                 + "<br>";
     
     String message3AfterTestPhase = "OK. You have now finished the test phase"
             + "Now you need to collaborate with the other partner\n"
             + "On each trial, both of you will have the same faces in front of you.\n"
             + "The order of the faces is randomly jumbled up\n"
             + "One of you will have a set with one face that is selected in a box\n"
             + "The other person will have a set with only numbers\n"
             + "Your task is to complete it as quickly and as accurately as possible\n"
             + "Once you have reached "+this.maxPointsbeforeFinalTest+ " points, you are free to go\n"
             + "So if you finish all the sets in 30 mins you will still get paid in full!\n"
             + "So please be as quick and as accurate as possible!";

     
     
      String message4BeforeSecondTestSetDirector = 
                "You are now going to do 5 more sets of faces\n"
              + "where you only have a limited time to describe each face\n"
              + "\n"
              + "Your partner will be describing a different set of faces\n"
              + "You won't be able to see what your partner is typing\n";
              
                
     
     String message5AfterSecondTestSetDirectorBeforeTestSetMatcher = "OK. You have now finished describing faces\n"
             + "You will now be given your partner's descriptions of faces\n"
             + "and your task is to work out which face they are describing\n"
             + "For each set you will need to select the face you think they're describing\n"
             + "One of the faces fits the description - which one is it?";
     
     

     class Score{
          
         int score =0;
         
         int success_increment =5;
         int unsuccess_decrement =2;
         
         public Score(){
          }

         public void updateSuccess(){
             score = score+success_increment;
         }
         
         public void updateUnsuccessful(){
             score = score - unsuccess_decrement;
             if(score<0)score=0;
         }
         
         public int getScore(){
             return score;
         }
         
     }
     

     
     
     
     
     public class Repository{
         
        Hashtable participants = new Hashtable();
         
         
         public Repository(){
             
         }
         
         public Hashtable getParticipantsDescriptions(Participant p){
              Hashtable participantsDescriptions = (Hashtable)participants.get(p);
              if(participantsDescriptions==null){
                  participantsDescriptions = new Hashtable();
                  participants.put(p, participantsDescriptions);
                  
              }
              return participantsDescriptions;
         }
         
         
         public void saveDescription(Participant origin, int setNumber, String description){
              Hashtable participantsDescriptions = this.getParticipantsDescriptions(origin);
              participantsDescriptions.put(setNumber, description);
             
         }
         
         
         public String  getDescription(Participant origin,int setNumber){
              Hashtable participantsDescriptions = this.getParticipantsDescriptions(origin);
              String retValue = (String)participantsDescriptions.get(setNumber);
              //System.exit(-5);
              if(retValue==null)retValue="";
              return retValue;
         }
         
         
     }
     
     
     
     
}



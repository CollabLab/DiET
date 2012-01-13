/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package diet.server.ConversationController;

import diet.client.WebpageAndImageDisplay;
import diet.message.MessageChatTextFromClient;
import diet.message.MessageKeypressed;
import diet.message.MessagePopup;
import diet.parameters.ExperimentSettings;
import diet.server.Conversation;
import diet.server.Participant;
import java.awt.Color;
import java.io.File;
import java.util.Arrays;
import java.util.Date;

/**
 *
 * @author sre
 */
public class CCPICTURESTIMULI_BACKUP extends DefaultConversationController{

    String sbfolder = "stimuliset4";
    public String redbackground = "<html><head><style type=\"text/css\">body {color: white; background: red;}div { font-size: 200%;}</style></head><body><div>INCORRECT </div></body></html>";
    
    WebpageAndImageDisplay wid = new WebpageAndImageDisplay(null);


    int stimuli = 1;
    //String sbfolder = "stimuliset4";
    String s = System.getProperty("user.dir")+File.separatorChar+"webserver"+File.separatorChar+sbfolder;
    File f = new File(s);
    File[] flist = f.listFiles();

    String uiMessageCorrect =   "Correct, you have X ";
    String uiMessageIncorrect = "Incorrect";
    String uiMessageTooManyInCorrect = "Too many incorrect";



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

    int matcherNumber ;
    int directorNumber;

    String fileName;

    String imgsrcD ="";
    String imgsrcM = "";

    int currSet;

    int numberOfIncorrectGuessesForCurrentSet =0;
    public int maxNumberOfIncorrectGuesses = 2;

    public int numberOfConsecutiveCorrect =0;



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
        this.directorNumber=Integer.parseInt(directorSelection);
        this.matcherNumber=Integer.parseInt(matcherSelection);

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
        currSet = srch;
        c.saveDataToConversationHistory("DATA:"+srch+"-"+directorFile.getName()+matcherFile.getName());
        return "";
    }


    
    public synchronized String gotoSetDIRECTORONLY(int srch, Participant dir){
         
        if(dir==this.director){
            
        }else{
            Participant temp = this.director;
            this.director = dir;
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
        this.directorNumber=Integer.parseInt(directorSelection);
        this.matcherNumber=Integer.parseInt(matcherSelection);

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
            //c.changeWebpageImage_OnServer(matcher, "ID1", imgsrcM);
            c.changeWebpageImage_OnServer(director, "ID1", imgsrcD);



           
            wid.changeWebpage(matcher.getUsername(), "<html><img src='"+"http://localhost:81/"+sbfolder+"/"+matcherFile.getName() + "'></img>", "", "");
            //wid.changeWebpage(director.getUsername(), "<html><img src='"+"http://localhost:81/"+sbfolder+"/"+directorFile.getName()+ "'></img>", "", "");
            //c.changeWebpageTextAndColour(matcher, "ID1","correct", "red", "white");
            numberOfIncorrectGuessesForCurrentSet =0;
        }

        catch(Exception e){
            e.printStackTrace();
            System.err.println("ERRORINMETHOD");
        }
        currSet = srch;
        c.saveDataToConversationHistory("DATA(DIRECTORONLY):"+srch+"-"+directorFile.getName()+matcherFile.getName());
        return "";
    }


    



    public synchronized void processSelection(Participant p,String t){
        if(p!=matcher){
            c.sendArtificialTurnToRecipient(p, "****You shouldn't be making this selection - your partner needs to make the selection*****", 0);
            return;
        }

        try{
             t = t.replaceAll(" " , "");
             int i = Integer.parseInt(t.substring(1, t.length()));
             if(i == this.matcherNumber){
                     c.changeWebpageTextAndColour(matcher, "ID1","Correct.\n You have made "+this.numberOfConsecutiveCorrect+ " consecutive correct guesses", "green", "white");
                     c.changeWebpageTextAndColour(director, "ID1","Correct.\nYou have made "+this.numberOfConsecutiveCorrect+ " consecutive correct guesses", "green", "white");
                     c.sendArtificialTurnToRecipient(director, "****Correct. You have made "+this.numberOfConsecutiveCorrect+ " consecutive guesses****", 0);
                     c.sendArtificialTurnToRecipient(matcher,  "****Correct. You have made "+this.numberOfConsecutiveCorrect+ " consecutive guesses****", 0);
                     Conversation.printWSln("Main", p.getUsername()+" made the CORRECT selection of "+i+"...moving to trial"+this.currSet+"---CONSECUTIVECORRECT:"+this.numberOfConsecutiveCorrect+"---INCORRECTGUESSESFORCURRENTSET"+this.numberOfIncorrectGuessesForCurrentSet);
                     Conversation.printWSln("Main", matcher.getUsername()+"---"+director.getUsername());
                     Thread.sleep(5000);
                     c.changeWebpageTextAndColour(matcher, "ID1","", "black", "black");
                     c.changeWebpageTextAndColour(director, "ID1","", "black", "black");
                     if(this.numberOfIncorrectGuessesForCurrentSet==0)numberOfConsecutiveCorrect ++;
                     this.gotoSet(currSet+1, true);
             }
             else{
                 numberOfConsecutiveCorrect=0;
                 if(this.numberOfIncorrectGuessesForCurrentSet>this.maxNumberOfIncorrectGuesses){
                     c.changeWebpageTextAndColour(matcher, "ID1","Incorrect\nyou used all your guesses", "red", "white");
                     c.changeWebpageTextAndColour(director, "ID1","Incorrect\nyou used all your guesses", "red", "white");
                     c.sendArtificialTurnToRecipient(director, "****Incorrect. You used all your guesses****", 0);
                     c.sendArtificialTurnToRecipient(matcher, "****Incorrect. You used all your guesses****", 0);
                     Conversation.printWSln("Main", p.getUsername()+" made INCORRECT selection of "+i+"...EXHAUSTED ALL THE GUESSES...MOVING TO TRIAL:"+this.currSet);

                     Thread.sleep(2000);
                     c.changeWebpageTextAndColour(matcher, "ID1","", "black", "black");
                     c.changeWebpageTextAndColour(director, "ID1","", "black", "black");
                     this.gotoSet(currSet+1, true);
                 }
                 else{
                    // c.changeWebpageTextAndColour(matcher, "ID1","Incorrect\nTry again", "red", "black");
                     //c.changeWebpageTextAndColour(director, "ID1","Incorrect\nTry again", "red", "black");
                    Conversation.printWSln("Main", p.getUsername()+ "made INCORRECT selection of"+i+"..still has "+(this.maxNumberOfIncorrectGuesses-this.numberOfIncorrectGuessesForCurrentSet)+"..guesses left on this trial");
                     //Thread.sleep(2000);
                     //c.changeWebpageTextAndColour(matcher, "ID1","", "black", "black");
                     //c.changeWebpageTextAndColour(director, "ID1","", "black", "black");
                     //c.changeWebpageImage_OnServer(matcher, "ID1", imgsrcM);
                     //c.changeWebpageImage_OnServer(director, "ID1", imgsrcD);
                     numberOfIncorrectGuessesForCurrentSet++;
                 }

                  c.sendArtificialTurnToRecipient(p, "****INCORRECT SELECTION...You have "+ (maxNumberOfIncorrectGuesses-numberOfIncorrectGuessesForCurrentSet)     +" guesses left*****", 0);
                  
             }

         }catch (Exception e){
             c.sendArtificialTurnToRecipient(p, "****YOU MADE A TYPO - TYPE BACKSLASH FOLLOWED BY THE NUMBER *****", 0);
         }
    }

   

    @Override
    public void initialize(Conversation c, ExperimentSettings expSettings) {
        super.initialize(c, expSettings);
        Arrays.sort(flist);

        //String s = "ab%%cardefghijklm%%car";
        //String s2 = s.replaceAll("%%car", "BBBB");
        //System.err.println(s2);
        //System.exit(-123123);
      //  JOptionPane.YES_NO_CANCEL_OPTION,
    //JOptionPane.QUESTION_MESSAGE,
        
       // JOptionPane.s
        //int i = JOptionPane.showConfirmDialog(null, " ", ""+3);
        //JOptionPane.showInputDialog(null, "message","title",JOptionPane.YES_NO_CANCEL_OPTION);
        //int i = JOptionPane.showConfirmDialog(null, "message", "title", JOptionPane.OK_OPTION);
       // System.err.println(i);
       // System.exit(-5);
        
        
        
    }



    

    @Override
    public void processKeyPress(Participant sender, MessageKeypressed mkp) {
         super.processKeyPress(sender,mkp);
    }

    @Override
    public void processPopupResponse(final Participant origin, MessagePopup mp) {
         this.conductTestTrial(origin);   
    }
    
    public void conductTestTrial(final Participant p){
        
        Thread t = new Thread(){
             public void run(){
                 long startTime = new Date().getTime();
                 gotoSetDIRECTORONLY(1, p);   
                 Participant p2 = (Participant)c.getParticipants().getAllOtherParticipants(p).elementAt(0);    
                 c.changeWebpageTextAndColour(p2, "ID1", "Initiating test phase...the other participant is answering", "white", "black");
                 
                 
                 while (new Date().getTime()-startTime<lengthOfFormulation){
                     try{
                          Thread.sleep(250);
                          long timeelapsed = new Date().getTime()-startTime;
                          final long proportiondone = 100*timeelapsed/lengthOfFormulation;
                          long timeleftsecs = (lengthOfFormulation-timeelapsed)/1000;
                          
                         
                          //c.changeWebpage(p2, "ID1", "", "Waiting for other participant to log in...please wait", "");
                         
                          
                          if(timeelapsed<lengthOfFormulation){
                               Color cColor = Color.RED;
                               if(proportiondone<50)cColor=Color.ORANGE;
                               if(proportiondone<80)cColor=Color.green;
                               c.changeJProgressBar(p, "ID1", "You have "+(timeleftsecs)+" seconds remaining", cColor, 100-(int)proportiondone);
                               if(!sentformulationTorecipient)c.changeWebpageTextAndColour(p2, "ID1", "You will see the faces in "+(delayBeforeRecipientGetsFaceSet-timeelapsed), "white", "black");
                          }
                          if(timeelapsed > delayBeforeRecipientGetsFaceSet & !sentformulationTorecipient){
                               c.changeWebpageImage_OnServer(matcher, "ID1", imgsrcM);
                               sentformulationTorecipient = true;
                          }
                          if(timeelapsed > lengthOfFormulation){
                               c.sendArtificialTurnFromApparentOriginToRecipient(director, matcher, s);
                               c.sendAndEnableLabelDisplayToALLStatusWindowsOfParticipant(director, "wait for other", true, false);
                          }
                          if(timeelapsed> lengthOfFormulation + lengthOfResponse){
                         
                     }
                          
                          //System.exit(-5);
                     }catch (Exception e){
                         e.printStackTrace();
                     }       
                     
                 }
             }
         };
         t.start();      
    }
    

    long lengthOfFormulation = 25000;
    long delayBeforeRecipientGetsFaceSet = 10000;
    long lengthOfResponse = 20000;
    
    boolean testmode = false;
    boolean sentformulationTorecipient = false;
    
    String bufferedtext ="";
    
    
    public void initiatePopupTrial(final Participant p){
        //c.changeJProgressBar(p, "ID1", "Time till speaker change", Color.orange, 50); 
        c.showPopupOnClientQueryInfo(p, "You have X Seconds to\n describe the face\n When you are ready press OK", new String[]{"START"}, "Test trial", 0);
        this.testmode=true;   
    }

    
    
    
    
    
    
    String subfolder = "";

    @Override
    public void participantJoinedConversation(Participant p) {    
        
        //String imgsrc2 = ":81/stimuliset1/img01_d_4.jpg";
        
        //c.changeWebpageImage_OnServer(p, "ID1", imgsrc2);
        //c.sendArtificialTurnToRecipient(p, "THIS IS A MESSAGE", 0);
        c.displayNEWWebpage(p, "ID1", "Faces", "", 1420, 401, false,false);
        c.changeJProgressBar(p, "ID1", "", Color.gray, 100);

        //this.initiatePopupTrial(p);
        
        //try{Thread.sleep(10000);}catch (Exception e){}
       // wid.displayNEWWebpage(p.getUsername(), s, s, stimuli, stimuli, isCompleted, isCompleted);
        wid.displayNEWWebpage(p.getUsername(),  p.getUsername(), "", 1420, 401, false,false,false);

        if(c.getParticipants().getAllParticipants().size()<2){
             director = p;
             //c.displayNEWWebpage(p, "ID1", "Faces", "", 920, 310, false);
             c.changeWebpage(p, "ID1", "", "Waiting for other participant to log in...please wait", "");
             //System.exit(-422223);
         }
         else if (5<2){
             matcher =p;
             for(int i=1;i<100;i++){
                 this.gotoSet(i,true);
                    try{ Thread.sleep(2000); }catch (Exception e){  }
             }
        System.exit(-5);
        }
        else{
           matcher=p;
           this.gotoSet(1,true);
        }

         ;
       


       
    }

    @Override
    public void participantRejoinedConversation(Participant p) {
        
    }


    
    @Override
    public void processLoop() {
        super.processLoop();
        c.resetFromIsTypingtoNormalChatAllAllowedParticipants(super.getIsTypingTimeOut());

    }

    
    
    
    
    

    @Override
    public void processChatText(Participant sender, MessageChatTextFromClient mct) {
        if(testmode){
            c.sendArtificialTurnToRecipient(sender, "***Your messagea are being buffered****", 0);
            c.sendArtificialTurnToRecipient(sender, "***They will be sent to the other participant in one go****", 0);
            //this.bufferedtext = bufferedtext + "\n" + mct.getText();
        }
        if(mct.getText().startsWith("/")){
           this.processSelection(sender, mct.getText());
        }
        else if (!testmode){
            this.initiatePopupTrial(sender);
        }
        else{
            super.processChatText(sender, mct);
        }
        
        
    }


   



}


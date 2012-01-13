/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package diet.task.collabMinitaskProceduralComms3;

//import java.util.Vector;

import diet.debug.Debug;
import diet.server.ConversationController.CCGROOP3SEQ3;
import diet.server.Participant;
import diet.task.collabMinitaskProceduralComms.Move;
import diet.task.collabMinitaskProceduralComms.Moves;
import diet.task.collabMinitaskProceduralComms.MovesFactory;
import diet.utils.StringOperations;
import java.util.Date;
import java.util.Hashtable;
import java.util.Random;
import java.util.Vector;


/**
 *
 * @author sre
 */
public class AlphabeticalTask3WAY {

   MovesFactory mf;
   CCGROOP3SEQ3 cC;
   Participant a;
   Participant b;
   Participant c;
   Moves mves;

   //String currSetRepresentation = "";
   String allFeedback = "";


   String aForegroundColour = "BLACK"; String aBackgroundColour = "WHITE";
   String bForegroundColour = "BLACK";  String bBackgroundColour = "WHITE";
   String cForegroundColour = "BLACK";  String cBackgroundColour = "WHITE";

   String aHTML ="";
   String bHTML = "";
   String cHTML ="";


   public boolean showNegativeFeedback = false;
   public boolean flashOnSuccess = true;

   
   private boolean onlyALLOW=true;
   private boolean xorALLOW=false;
   private boolean andALLOW=false;
   private int maxSize = 1;

   public JCRSender2Participants3 jcrs;

   public AlphabeticalTask3WAY(CCGROOP3SEQ3 cC) {
       this.cC =cC;
   }

   public void changeSettings(int mSize, boolean o, boolean x, boolean a){
       this.maxSize=mSize;
       this.onlyALLOW=o;
       this.xorALLOW=x;
       this.andALLOW=a;
   }

  public Participant getOther(Participant p){
      if(p==a)return b;
      if(p==b)return a;
      return null;
  }
  public Participant getOverhearer(Participant p){
      return c;
  }


  public void startTask(Participant a, Participant b, Participant c){
        
        this.a=a;
        this.b=b;
        this.c=c;

        jcrs= new JCRSender2Participants3(cC,this,a,b,maxSize,onlyALLOW,xorALLOW,andALLOW);
        jcrs.setVisible(true);
        jcrs.pack();
        jcrs.repaint();


        mves = new Moves(a,b);
        try{
           mves.createNewMoveSet(maxSize, onlyALLOW, xorALLOW, andALLOW);
        }catch (Exception e){e.printStackTrace();}
        
        this.divideAandBMovesBetween3();
        this.displayMovesOn3ScreensHTML("Please start");
        this.saveSetToFile();

        String chatTextWindowA = "Please start! Your score is: "+ this.getScore(a);
        String chatTextWindowB = "Please start! Your score is: "+ this.getScore(b);
        String chatTextWindowC = "Please start! Your score is: "+ this.getScore(b);



        cC.getC().sendArtificialTurnToRecipient(a, chatTextWindowA, 0);
        cC.getC().sendArtificialTurnToRecipient(b, chatTextWindowB, 0);
        cC.getC().sendArtificialTurnToRecipient(c, chatTextWindowC, 0);
  }

  public void displayMovesOn3ScreensHTML(String prefix){
     /*   this.aHTML = Moves.asString(this.vAllMovesForBToSelectSentToA);
        this.bHTML = Moves.asString(this.vAllMovesForAToSelectSentToB);
        this.cHTML="Some of the words for "+a.getUsername()+" are:<br> "+Moves.asString(this.vAllMovesForAToSelectSentToC) + "<br><br>"
                 + "Some of the words for "+b.getUsername()+" are:<br> "+Moves.asString(this.vAllMovesForBToSelectSentToC);

        allFeedback="";
        Debug.printDBG_andclear(mves.getAllMovesToString());

         cC.getC().changeWebpageTextAndColour(a, "ID1", prefix+" YOUR SCORE IS: "+this.getScore(a)+"<br><br>"+
                 "The words for "+ b.getUsername()+" are :<br>"+ "\n"+ aHTML,aForegroundColour, aBackgroundColour);
         cC.getC().changeWebpageTextAndColour(b, "ID1", prefix+" YOUR SCORE IS: "+this.getScore(b)+"<br><br>"+
                 "The words for"+ a.getUsername()+ " are:<br>"+"\n"+bHTML, bForegroundColour, bBackgroundColour);
         cC.getC().changeWebpageTextAndColour(c, "ID1", prefix+" YOUR SCORE IS: "+this.getScore(b)+"<br><br>"+
                 cHTML, bForegroundColour, bBackgroundColour);
*/
         Debug.printDBG_andclear(mves.getAllMovesToString());

         aHTML = prefix+"<br>YOUR SCORE IS: "+this.getScore(a)+"<br><br>"+ "Some of the words for "+ b.getUsername()+" are :<br>"+ "\n"+Moves.asString(this.vAllMovesForBToSelectSentToA);
         bHTML = prefix+"<br>YOUR SCORE IS: "+this.getScore(b)+"<br><br>"+ "Some of the words for "+ a.getUsername()+ " are:<br>"+"\n"+Moves.asString(this.vAllMovesForAToSelectSentToB);
         cHTML= prefix+"<br>YOUR SCORE IS: "+this.getScore(b)+"<br><br>"
                 + "Some of the words for "+a.getUsername()+" are:<br> "+Moves.asString(this.vAllMovesForAToSelectSentToC) + "<br><br>"
                 + "Some of the words for "+b.getUsername()+" are:<br> "+Moves.asString(this.vAllMovesForBToSelectSentToC);

         this.swapColours(a);
         this.swapColours(b);
         this.swapColours(c);

         cC.getC().changeWebpageTextAndColour(a, "ID1",aHTML,aForegroundColour, aBackgroundColour);
         cC.getC().changeWebpageTextAndColour(b, "ID1",bHTML, bForegroundColour, bBackgroundColour);
         cC.getC().changeWebpageTextAndColour(c, "ID1",cHTML, bForegroundColour, bBackgroundColour);



  }






   Random r = new Random();


   public void divideAandBMovesBetween2(){
       vAllMovesForBToSelectSentToA = new Vector(); //String sAllMovesForBToSelectSentToA = "";
       vAllMovesForBToSelectSentToC = new Vector(); //String sAllMovesForBToSelectSentToC = "";
       vAllMovesForAToSelectSentToB = new Vector(); //String sAllMovesForAToSelectSentToB  = "";
       vAllMovesForAToSelectSentToC = new Vector(); //String sAllMovesForAToSelectSentToC = "";

       vAllMovesForBToSelectSentToA = (Vector)mves.getWordsForBToSelect().clone();
       vAllMovesForAToSelectSentToB = (Vector)mves.getWordsForAToSelect().clone();
   }



   public void divideAandBMovesBetween3(){
       vAllMovesForBToSelectSentToA = new Vector(); //String sAllMovesForBToSelectSentToA = "";
       vAllMovesForBToSelectSentToC = new Vector(); //String sAllMovesForBToSelectSentToC = "";
       vAllMovesForAToSelectSentToB = new Vector(); //String sAllMovesForAToSelectSentToB  = "";
       vAllMovesForAToSelectSentToC = new Vector(); //String sAllMovesForAToSelectSentToC = "";

       Vector vAllMovesForBToSelect = (Vector)mves.getWordsForBToSelect().clone();
       Vector vAllMovesForAToSelect = (Vector)mves.getWordsForAToSelect().clone();
       for(int i=0;i<vAllMovesForBToSelect.size();i++){
            int whichone = r.nextInt(3);
            if(whichone==0||whichone==1)vAllMovesForBToSelectSentToA.add(vAllMovesForBToSelect.elementAt(i));
            if(whichone==2)vAllMovesForBToSelectSentToC.add(vAllMovesForBToSelect.elementAt(i));
       }
       for(int i=0;i<vAllMovesForAToSelect.size();i++){
            int whichone = r.nextInt(3);
            if(whichone==0||whichone==1)vAllMovesForAToSelectSentToB.add(vAllMovesForAToSelect.elementAt(i));
            if(whichone==2)vAllMovesForAToSelectSentToC.add(vAllMovesForAToSelect.elementAt(i));
       }
   }




   public void divideAandBMovesBetween3OLD(){
       vAllMovesForBToSelectSentToA = new Vector(); //String sAllMovesForBToSelectSentToA = "";
       vAllMovesForBToSelectSentToC = new Vector(); //String sAllMovesForBToSelectSentToC = "";
       vAllMovesForAToSelectSentToB = new Vector(); //String sAllMovesForAToSelectSentToB  = "";
       vAllMovesForAToSelectSentToC = new Vector(); //String sAllMovesForAToSelectSentToC = "";

       Vector vAllMovesForBToSelect = (Vector)mves.getWordsForBToSelect().clone();
       Vector vAllMovesForAToSelect = (Vector)mves.getWordsForAToSelect().clone();
       for(int i=0;i<vAllMovesForBToSelect.size();i++){
            int whichone = r.nextInt(2);
            if(whichone==0)vAllMovesForBToSelectSentToA.add(vAllMovesForBToSelect.elementAt(i));
            if(whichone==1)vAllMovesForBToSelectSentToC.add(vAllMovesForBToSelect.elementAt(i));
       }
       for(int i=0;i<vAllMovesForAToSelect.size();i++){
            int whichone = r.nextInt(2);
            if(whichone==0)vAllMovesForAToSelectSentToB.add(vAllMovesForAToSelect.elementAt(i));
            if(whichone==1)vAllMovesForAToSelectSentToC.add(vAllMovesForAToSelect.elementAt(i));
       }
   }

     Vector vAllMovesForBToSelectSentToA = new Vector();
     Vector vAllMovesForBToSelectSentToC = new Vector();
     Vector vAllMovesForAToSelectSentToB = new Vector();
     Vector vAllMovesForAToSelectSentToC = new Vector();



    public void saveSetToFile(){
        try{
         String sAllMovesForBToSelectSentToA = Moves.asString(this.vAllMovesForBToSelectSentToA);
         String sAllMovesForBToSelectSentToC = Moves.asString(this.vAllMovesForBToSelectSentToC);
         String sAllMovesForAToSelectSentToB = Moves.asString(this.vAllMovesForAToSelectSentToB);
         String sAllMovesForAToSelectSentToC = Moves.asString(this.vAllMovesForAToSelectSentToC);

         

         long senddate = new Date().getTime();

         cC.getC().saveDataToFile("SELINFO", "server","server",senddate,senddate,"A: "+a.getUsername()+"  B: "+b.getUsername()+" C: "+c.getUsername(),null);
         cC.getC().saveDataToFile("SELINFO", b.getUsername(), a.getUsername(), senddate, senddate,"FORB_SENTTO_A"+sAllMovesForBToSelectSentToA , null);
         cC.getC().saveDataToFile("SELINFO", b.getUsername(), c.getUsername(), senddate, senddate,"FORB_SENTTO_C"+sAllMovesForBToSelectSentToC , null);
         cC.getC().saveDataToFile("SELINFO", a.getUsername(), b.getUsername(), senddate, senddate,"FORA_SENTTO_B"+sAllMovesForAToSelectSentToB , null);
         cC.getC().saveDataToFile("SELINFO", a.getUsername(), c.getUsername(), senddate, senddate,"FORA_SENTTO_C"+sAllMovesForAToSelectSentToC, null);
       }catch(Exception e){
            e.printStackTrace();
       }
    }

    public void nextSet(String prefix){
        try{
        mves.createNewMoveSet(maxSize,onlyALLOW,xorALLOW,andALLOW);
 
        }catch (Exception e){e.printStackTrace();}

        this.divideAandBMovesBetween3();
        this.displayMovesOn3ScreensHTML(prefix);
        this.saveSetToFile();

        allFeedback="";
        Debug.printDBG_andclear(mves.getAllMovesToString());

        String chatTextWindowA = prefix+". Your score is: "+ this.getScore(a);
        String chatTextWindowB = prefix+". Your score is: "+ this.getScore(b);
        String chatTextWindowC = prefix+". Your score is: "+ this.getScore(b);

        cC.getC().sendArtificialTurnToRecipient(a, chatTextWindowA, 0);
        cC.getC().sendArtificialTurnToRecipient(b, chatTextWindowB, 0);
        cC.getC().sendArtificialTurnToRecipient(c, chatTextWindowC, 0);
    }


    public void swapColours(Participant p){
        //System.exit(-4);
        String temp;
        if(p==a){
            temp=this.aBackgroundColour;
            this.aBackgroundColour=this.aForegroundColour;
            this.aForegroundColour=temp;
            //cC.getC().changeWebpageTextAndColour(a, "ID1", "CORRECT. YOUR SCORE IS: "+this.getScore(a)+"<br>"+"YOUR WORDS ARE:<br>"+ "\n"+ this.aHTML,aForegroundColour, aBackgroundColour);
            cC.getC().changeWebpageTextAndColour(a, "ID1", this.aHTML,aForegroundColour, aBackgroundColour);

        }
        else if(p==b){
            temp=this.bBackgroundColour;
            this.bBackgroundColour=bForegroundColour;
            this.bForegroundColour=temp;
            cC.getC().changeWebpageTextAndColour(b, "ID1", this.bHTML, bForegroundColour, bBackgroundColour);
        }
        else if(p==c){
            temp=this.cBackgroundColour;
            this.cBackgroundColour=cForegroundColour;
            this.cForegroundColour=temp;
            cC.getC().changeWebpageTextAndColour(c, "ID1", this.cHTML, cForegroundColour, cBackgroundColour);
        }
        else{
            //System.exit(-5);
        }
    }


    public boolean showPositiveFeedback = false;;


    public void provideFeedback(Participant sender,Participant pRecipient, String message){
       

        if (flashOnSuccess&&message.equalsIgnoreCase(Moves.correctSET)){
           //cC.getC().sendArtificialTurnToRecipient(pRecipient, "****CORRECT! NEXT SET OF WORDS****", 0);
         if(showPositiveFeedback)   this.swapColours(pRecipient);
       }
       else if(flashOnSuccess && message.equalsIgnoreCase(Moves.correctSIMULTANEOUSWORD)){
            if(showPositiveFeedback)this.swapColours(pRecipient);
            if(showPositiveFeedback)this.swapColours(sender);
            if(showPositiveFeedback)cC.getC().sendArtificialTurnToRecipient(pRecipient, "CORRECT WORD SELECTED", 0);
            if(showPositiveFeedback)cC.getC().sendArtificialTurnToRecipient(sender, "CORRECT WORD SELECTED", 0);
            // System.exit(-05);
       }
       else if (flashOnSuccess&&message.startsWith("CORRECT")){
            if(showPositiveFeedback)this.swapColours(pRecipient);
            if(showPositiveFeedback) cC.getC().sendArtificialTurnToRecipient(pRecipient, "CORRECT WORD SELECTED", 0);
       }
       else if (this.showNegativeFeedback){
           cC.getC().sendArtificialTurnToRecipient(pRecipient, message, 0);
       }
    }
    

    public void processSelection(Participant p,String s){
        if(p==c)return;
        allFeedback = allFeedback + "------"+p.getUsername()+"------"+s+"-----";

        s=s.trim();
        if(s.equalsIgnoreCase("///next")){
            nextSet("Timeout...next set");
        }
        else{
             String name = s.substring(1).trim();
             String message = mves.evaluate(p,name);

             Participant recipientOfMessageToOther=null;//  = this.getOther(p);
             if(p==a)recipientOfMessageToOther = b;
             if(p==b)recipientOfMessageToOther =a;


             //if (message.equalsIgnoreCase(Moves.correctSET)){
             if (message.startsWith(Moves.correctSET)){
                 long numberOfWords = mves.moves.size();
                 this.addScore(p, numberOfWords);
                 this.addScore(recipientOfMessageToOther, (long)mves.moves.size());
                 nextSet("CORRECT. NEXT SET OF WORDS");
                 provideFeedback(p,recipientOfMessageToOther,message);
             }
             else if(message != null & !message.equalsIgnoreCase("")){
                 allFeedback = allFeedback + recipientOfMessageToOther.getUsername()+":"+message+"\n";
                 provideFeedback(p,recipientOfMessageToOther,message);

                // provideFeedback(a,recipientOfMessageToOther,message);

                 //System.exit(-05);
             }
             else{
                 allFeedback = allFeedback+ recipientOfMessageToOther.getUsername()+": NOMESSAGE\n";
             }
             Debug.printDBG_andclear(mves.getAllMovesToString()+"\n"+allFeedback);
             Vector vAdd = new Vector();
             vAdd.add(""+mves.getLastSel());
             Move mostRec = mves.getMostRecentDiscussed();
             if(mostRec!=null)vAdd.add(""+mostRec.getName());

             cC.getC().saveDataToFile("TASK", p.getParticipantID(), p.getUsername(), new Date().getTime(), new Date().getTime(), message, vAdd);
             
        }

    }
    public int getLastSel(){
        return mves.getLastSel();
    }


    public Object[] getMostRecentAndAllMovesForString(String turntext){
        String[] turntextsplit = StringOperations.splitOnlyText(turntext);
        //Vector allMovesthatmatch = mves.findMovesForWords(turntextsplit);

        Object[] mostRecent_And_AllMoves = mves.doesTextMentionMostRecent_andFindAllMovesForWords(turntextsplit);
        return mostRecent_And_AllMoves;
    }



    public Participant getOtherD(Participant p){
        if (p==this.a)return b;
        if(p==this.b)return a;
        return null;
    }

    
    static Hashtable scores = new Hashtable();
    static public long getScore(Participant p){
         Object o = scores.get(p);
        if(o==null){
            scores.put(p, (long)0);
        }
        Object score = scores.get(p);
        if(score==null)score =(long)0;
        return (Long)score;
    }

    static public void addScore(Participant p, long numberOfCorrect){
        if(scores==null)scores=new Hashtable();
        Object o = scores.get(p);
        if(o==null){
            scores.put(p, (long)0);
        }
        long score = (Long)scores.get(p);
        score=score+numberOfCorrect;
        scores.put(p, score);
    }

    public void swapBWithC(){
        Participant temp = b;
        b=c;
        c=temp;
        mves.swapCForB(b);
        this.jcrs.swapBWithC(b);
    }
    public void swapAWithC(){
        Participant temp=a;
        a=c;
        c=temp;
        mves.swapCForA(a);
        this.jcrs.swapAWithC(a);
    }

    public Participant getC(){
        return c;
    }


}

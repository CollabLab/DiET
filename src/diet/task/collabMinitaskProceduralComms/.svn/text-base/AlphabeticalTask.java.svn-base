/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package diet.task.collabMinitaskProceduralComms;

//import java.util.Vector;

import diet.debug.Debug;
import diet.server.ConversationController.CCGROOP3SEQ2;
import diet.server.Participant;
import diet.utils.StringOperations;
import java.util.Date;
import java.util.Hashtable;
import java.util.Vector;


/**
 *
 * @author sre
 */
public class AlphabeticalTask {

   MovesFactory mf;
   CCGROOP3SEQ2 cC;
   Participant a;
   Participant b;
   Moves mves;

   //String currSetRepresentation = "";
   String allFeedback = "";


   String aForegroundColour = "BLACK"; String aBackgroundColour = "WHITE";
   String bForegroundColour = "BLACK";  String bBackgroundColour = "WHITE";

   String aHTML ="";
   String bHTML = "";



   public boolean showNegativeFeedback = false;
   public boolean flashOnSuccess = true;

   
   private boolean onlyALLOW=true;
   private boolean xorALLOW=false;
   private boolean andALLOW=false;
   private int maxSize = 1;

   public JCRSender2Participants jcrs;

   public AlphabeticalTask(CCGROOP3SEQ2 cC) {
        this.cC =cC;
   }

   public void changeSettings(int mSize, boolean o, boolean x, boolean a){
       this.maxSize=mSize;
       this.onlyALLOW=o;
       this.xorALLOW=x;
       this.andALLOW=a;
   }

  public void startTask(Participant a, Participant b){
        
        this.a=a;
        this.b=b;

        jcrs= new JCRSender2Participants(cC,this,a,b,maxSize,onlyALLOW,xorALLOW,andALLOW);
        jcrs.setVisible(true);
        jcrs.pack();
        jcrs.repaint();


        mves = new Moves(a,b);
        try{
           mves.createNewMoveSet(maxSize, onlyALLOW, xorALLOW, andALLOW);
        }catch (Exception e){e.printStackTrace();}
        this.aHTML = mves.getWordsForBToSelectHTML();
        this.bHTML = mves.getWordsForAToSelectHTML();
        allFeedback="";
        Debug.printDBG_andclear(mves.getAllMovesToString());

         cC.getC().changeWebpageTextAndColour(a, "ID1", "PLEASE START. YOUR SCORE IS: "+this.getScore(a)+"<br>"+"YOUR WORDS ARE:<br>"+ "\n"+ aHTML,aForegroundColour, aBackgroundColour);
         cC.getC().changeWebpageTextAndColour(b, "ID1", "PLEASE START.  YOUR SCORE IS: "+this.getScore(b)+"<br>"+"YOUR WORDS ARE:<br>"+"\n"+bHTML, bForegroundColour, bBackgroundColour);

      
        String chatTextWindowA = "Please start! Your score is: "+ this.getScore(a);
        String chatTextWindowB = "Please start! Your score is: "+ this.getScore(b);

        cC.getC().sendArtificialTurnToRecipient(a, chatTextWindowA, 0);
        cC.getC().sendArtificialTurnToRecipient(b, chatTextWindowB, 0);
  }



    public void nextSet(String prefix){
        try{
        mves.createNewMoveSet(maxSize,onlyALLOW,xorALLOW,andALLOW);
        
        }catch (Exception e){e.printStackTrace();}
        this.aHTML = mves.getWordsForBToSelectHTML();
        this.bHTML = mves.getWordsForAToSelectHTML();

        cC.getC().changeWebpageTextAndColour(a, "ID1", "YOUR SCORE IS: "+this.getScore(a)+"<br>"+"YOUR WORDS ARE:<br>"+ "\n"+ aHTML,aForegroundColour, aBackgroundColour);
        cC.getC().changeWebpageTextAndColour(b, "ID1", "YOUR SCORE IS: "+this.getScore(b)+"<br>"+"YOUR WORDS ARE:<br>"+"\n"+bHTML, bForegroundColour, bBackgroundColour);
        
        allFeedback="";
        Debug.printDBG_andclear(mves.getAllMovesToString());

        String chatTextWindowA = prefix +" Your score is: "+ this.getScore(a);
        String chatTextWindowB = prefix+" Your score is: "+ this.getScore(b);

        cC.getC().sendArtificialTurnToRecipient(a, chatTextWindowA, 0);
        cC.getC().sendArtificialTurnToRecipient(b, chatTextWindowB, 0);
    }


    public void swapColours(Participant p){
        //System.exit(-4);
        String temp;
        if(p==a){
            temp=this.aBackgroundColour;
            this.aBackgroundColour=this.aForegroundColour;
            this.aForegroundColour=temp;
            cC.getC().changeWebpageTextAndColour(a, "ID1", "CORRECT. YOUR SCORE IS: "+this.getScore(a)+"<br>"+"YOUR WORDS ARE:<br>"+ "\n"+ this.aHTML,aForegroundColour, aBackgroundColour);
        }
        else if(p==b){
            temp=this.bBackgroundColour;
            this.bBackgroundColour=bForegroundColour;
            this.bForegroundColour=temp;
            cC.getC().changeWebpageTextAndColour(b, "ID1", "CORRECT. YOUR SCORE IS: "+this.getScore(b)+"<br>"+"YOUR WORDS ARE:<br>"+"\n"+ this.bHTML, bForegroundColour, bBackgroundColour);
        }
        else{
            //System.exit(-5);
        }
    }


    public void provideFeedback(Participant sender,Participant pRecipient, String message){
       if (flashOnSuccess&&message.equalsIgnoreCase(Moves.correctSET)){
           //cC.getC().sendArtificialTurnToRecipient(pRecipient, "****CORRECT! NEXT SET OF WORDS****", 0);
           this.swapColours(pRecipient);
       }
       else if(flashOnSuccess && message.equalsIgnoreCase(Moves.correctSIMULTANEOUSWORD)){
            this.swapColours(pRecipient);
            this.swapColours(a);
       }
       else if (flashOnSuccess&&message.startsWith("CORRECT")){
            this.swapColours(pRecipient);
       }
       else if (this.showNegativeFeedback){
           cC.getC().sendArtificialTurnToRecipient(pRecipient, message, 0);
       }
    }
    

    public void processSelection(Participant p,String s){
        allFeedback = allFeedback + "------"+p.getUsername()+"------"+s+"-----";

        s=s.trim();
        if(s.equalsIgnoreCase("///next")){
            nextSet("Timeout...next set");
        }
        else{
             String name = s.substring(1).trim();
             String message = mves.evaluate(p,name);

             Participant recipientOfMessageToOther  = this.getOther(p);
             //if (message.equalsIgnoreCase(Moves.correctSET)){
             if (message.startsWith(Moves.correctSET)){
                 long numberOfWords = mves.moves.size();
                 this.addScore(p, numberOfWords);
                 this.addScore(recipientOfMessageToOther, (long)mves.moves.size());
                 nextSet("CORRECT");
                 provideFeedback(p,recipientOfMessageToOther,message);
             }
             else if(message != null & !message.equalsIgnoreCase("")){
                 allFeedback = allFeedback + recipientOfMessageToOther.getUsername()+":"+message+"\n";
                 provideFeedback(p,recipientOfMessageToOther,message);
             }
             else{
                 allFeedback = allFeedback+ recipientOfMessageToOther.getUsername()+": NOMESSAGE\n";
             }
             Debug.printDBG_andclear(mves.getAllMovesToString()+"\n"+allFeedback);
             cC.getC().saveDataToFile("TASK", p.getParticipantID(), p.getUsername(), new Date().getTime(), new Date().getTime(), message, null);
             
        }

    }

    public Object[] getMostRecentAndAllMovesForString(String turntext){
        String[] turntextsplit = StringOperations.splitOnlyText(turntext);
        //Vector allMovesthatmatch = mves.findMovesForWords(turntextsplit);

        Object[] mostRecent_And_AllMoves = mves.doesTextMentionMostRecent_andFindAllMovesForWords(turntextsplit);
        return mostRecent_And_AllMoves;
    }



    public Participant getOther(Participant p){
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

}

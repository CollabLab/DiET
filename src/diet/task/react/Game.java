/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package diet.task.react;

import diet.debug.Debug;
import diet.debug.DebugWindow;
import diet.server.Participant;
import diet.utils.StringOperations;
import java.util.Date;
import java.util.Random;
import java.util.Vector;

/**
 *
 * @author sre
 */
public class Game {

    ReactTaskController rtc;
    DebugWindow dbgW= new DebugWindow();

     Move[] moves;

     String pA_UI="";
     String pB_UI="";
     
     Vector pARules;
     Vector pBRules;

     Set ms;

     int numberOfConsecutiveCorrect =0;
     int thresholdOfCorrectForNextSequence = 8;
     int currentSetNo =0;


    public Game(ReactTaskController rtc){
        this.rtc=rtc;
        this.ms= new SetA(rtc);
        this.swapSetAndChangeButtonLayout(0);
       
    }

    
  
    public void doNextSetThread(){
           setSETSIZ(4+r.nextInt(9));
           correctSequenceDone(); 
           this.thresholdOfCorrectForNextSequence=1;
            Thread t = new Thread(){
               public void run(){
                   while(2<5){
                    //  correctSequenceDone();
                         try{
                             Thread.sleep(4000);
                              setSETSIZ(1+r.nextInt(9));
                              changeFirst();
                              getMovesSummary();
                              displayMoves();
                             System.out.println("SHOULDBECHANGINGFIRST");
                           }catch (Exception e){
                              e.printStackTrace();
                          }
                   }
               }  
            };
        //t.start();
    }

    


    

    public synchronized void correctSequenceDone(){
        if(numberOfConsecutiveCorrect > thresholdOfCorrectForNextSequence){
            currentSetNo++;
            swapSetAndChangeButtonLayout(currentSetNo);
            String dispmsg = "CORRECT!\n\nNEXT SET OF NUMBERS\n\n";
            //this.displayMessage(rtc.pA,dispmsg,rtc.pB,dispmsg);
            
            swapSetAndChangeButtonLayout(currentSetNo);
            this.updateParticipantsUI(dispmsg);
            numberOfConsecutiveCorrect=0;
        }
        else{
            numberOfConsecutiveCorrect++;
            restartSAMESETJUMBLEDLAYOUT();
            String dispmsg = "correct!\n\nYou have done "+this.numberOfConsecutiveCorrect+"\n consecutive correct \n for this set";
            this.displayMessage(rtc.pA,dispmsg,rtc.pB,dispmsg);

        }
    }
    public void incorrectSequence(){
         restartERRORMADE();
    }

    public void nextSet(){
         swapSetAndChangeButtonLayout(currentSetNo+1);
    }



    
    
    
    
    public synchronized void swapSetAndChangeButtonLayout(int setNo){
        if(requestSWAP)this.performSwap();
        this.moves=ms.getSet(setNo);
        this.currentSetNo=setNo;
        this.numberOfConsecutiveCorrect=0;
        this.generateExperimentInstructionsForParticipant(rtc.pA);
        this.generateExperimentInstructionsForParticipant(rtc.pB);
        rtc.cC.getC().changeWebpage(rtc.pA, "ID1", "", this.pA_UI, "");
        rtc.cC.getC().changeWebpage(rtc.pB, "ID1", "", this.pB_UI, "");
        MessageChangeButtonSetToClient mcbsctcForBOTH = new MessageChangeButtonSetToClient("server","server",null,new Rules(pARules));
        this.rtc.jRTFA.changeButtonSet(mcbsctcForBOTH.getNames());
        this.rtc.jRTFB.changeButtonSet(mcbsctcForBOTH.getNames());
        this.rtc.cC.getC().sendTaskMoveToParticipant(rtc.pA, mcbsctcForBOTH);
        this.rtc.cC.getC().sendTaskMoveToParticipant(rtc.pB, mcbsctcForBOTH);
    }

    

    public int getSet(){
        return this.currentSetNo;
    }


    public int getConsecutiveCorrect(){
        return this.numberOfConsecutiveCorrect;
    }

    
    
    
    private void check(){
        ///If nothing is selected, then 
            ///re-arrange sequence so that the first one in the list is the selection
            ///Then do normal evaluation
        ///If is restarting following error....
            //They can reselect anything that they've already selected
                 //If they select something they've already selected then re-arrange list so that's where they restart from (AND HAVE TO RE-ERASE THE LIST FROM THE RIGHT)????
                //If they don't select something they've already selected, then display error message at them
     
    
    }
    
    
    
    
    
    
    public synchronized Object[] evaluateStateChangeButtonPressed(Participant origin,SelectionState sS, long timestampOfReceiptOnServer){
    // dbgW.append("SELECTIONBY");


        for(int i=0;i<moves.length;i++){
            if(!moves[i].isComplete){
               boolean isMoveOK = moves[i].isMoveOK(origin,sS, timestampOfReceiptOnServer);
               Move mveCopy = moves[i].getFullCopy();
               if(moves[i].isComplete&i==moves.length-1){
                     correctSequenceDone();
                     
                     
                     System.err.println("------------------------------------");
                     System.err.println(this.getMovesSummary());
                     //if(2<4)System.exit(-5);
                     
                     
                     Object[] evl = new Object[4];
                     evl[0] = mveCopy;
                     evl[1]= "COMPLETEDSEQUENCE";
                     evl[2]= i;
                     evl[3]= moves.length;
                     return evl;
                     //return new Object[]{"COMPLETEDSEQUENCE",moves[i]);
                     //return "COMPLETEDSEQUENCE";
                }
                String outcome ="OK";
                if(!isMoveOK){
                    incorrectSequence();
                    outcome = "ERROR_RESTARTING";
                }
                 //outcome = outcome + ","+moves[i].getInfo();
                 Object[] evl = new Object[4];
                 evl[0] =  mveCopy;
                 evl[1]= outcome;
                 evl[2]= i;
                 evl[3]= moves.length;
                 return evl;
            }

        }

        if(Debug.debugREACTChangeThread){
            for(int i=0;i<moves.length;i++){
                System.err.println("================"+moves[i].getInfo());
            }
        }
        
                Object[] evl = new Object[4];
                evl[0] = null;
                evl[1]=  "THISPROBABLYSHOULDNTHAPPEN";
                evl[2]= -9999;
                evl[3]= moves.length;
                return evl;
       
        
    }

    Random r = new Random();






   public synchronized void restartSAMESETJUMBLEDLAYOUT(){
        Move mve = moves[0];
        Participant starter = null;
        String messagedisplaySTARTER ="";
        String messagedisplayOTHER ="";

        if(mve instanceof MoveONLY){
             starter = ((MoveONLY)mve).p;
             messagedisplaySTARTER = "CORRECT.\n"
                     + "Same set:\n"
                     + "Start with "+((MoveONLY)mve).name;
             messagedisplayOTHER = "CORRECT\n"
                     + "Same set\n"
                     + "The OTHER starts: ";
        }
        else if(mve instanceof MoveAND) {
             messagedisplaySTARTER = "Correct\n"
                     + "start with "+ ((MoveAND)mve).a;
             messagedisplayOTHER = "Correct\n"
                     + "start with "+ ((MoveAND)mve).b;
             starter = this.rtc.pA;
        }
        this.displayMessage(starter, messagedisplaySTARTER, rtc.getOther(starter),messagedisplayOTHER);
        MessageChangeButtonSetToClient mcbsctcForBOTH = new MessageChangeButtonSetToClient("server","server",null,null);
       
         this.rtc.jRTFA.changeButtonSet(mcbsctcForBOTH.getNames());
         this.rtc.jRTFB.changeButtonSet(mcbsctcForBOTH.getNames());
         this.rtc.cC.getC().sendTaskMoveToParticipant(rtc.pA, mcbsctcForBOTH);
         this.rtc.cC.getC().sendTaskMoveToParticipant(rtc.pB, mcbsctcForBOTH);
         for(int i=0;i<moves.length;i++){
             moves[i].reset();
        }
   }


    public synchronized void shuffleList_BeCareful_TheUIToParticipantsMustBeUpdatedAsWell(){
        Vector v = new Vector();
        for(int i=0;i<moves.length;i++){
            Move mve = moves[i];
            int idx = 0; if(v.size()>0) idx = r.nextInt(v.size());
            v.insertElementAt(mve, idx);
        }
        for(int i=0;i<v.size();i++){
            Move mve = (Move)v.elementAt(i);
            this.moves[i]=mve;
        }
    }

     public synchronized void changeFirstBUGGY(){
         if(moves.length==1){
             moves[0].reset();
             return;
         }
         
         Vector v = new Vector();
         for(int i=0;i<moves.length;i++){
             Move mve = moves[i];
             v.add(mve);
             mve.reset();
         }
         Object first = v.elementAt(0);
         v.remove(first);
         int insertIndex = r.nextInt(v.size());
         v.insertElementAt(first, insertIndex);
         for(int i=0;i<v.size();i++){
            Move mve = (Move)v.elementAt(i);
            this.moves[i]=mve;
        }
     }
    
    
     public synchronized void changeFirst(){
         if(moves.length==1){
             moves[0].reset();
             return;
         }
         
         int startIndex =1+r.nextInt(moves.length-1);
         Vector v = new Vector();
         for(int i=startIndex;i<moves.length;i++){
             Move mve = moves[i];
             v.add(mve);
             mve.reset();
         }
         for(int i=0;i<startIndex;i++){
             Move mve = moves[i];
             v.add(mve);
             mve.reset();
         }
         for(int i=0;i<v.size();i++){
            Move mve = (Move)v.elementAt(i);
            this.moves[i]=mve;
        }
         
        for(int j=0;j<moves.length;j++){
              try{
                  rtc.cC.getC().saveDataToFile("REARRANGEDSET", "server", "server", new Date().getTime(), new Date().getTime(), "", moves[j].getInfoAsVector());
              }catch (Exception e){
                  //rtc.getGame().rtc.cC.getC().saveDataToFile("NEWSET", "server", "server", new Date().getTime(), new Date().getTime(), sequence, null);
                  e.printStackTrace();
                 
              }
          }

    }

    public synchronized void gotoSetNo(int i){
        this.swapSetAndChangeButtonLayout(i);
    }


    public synchronized void setSETSIZ(int i){
        ms.setSize(i);
    }

    public void displayMoves(){
        this.dbgW.setText(getMovesSummary());
        this.dbgW.append("\n");
        this.dbgW.append("----------------------\n");
        this.dbgW.append("\n");
        this.dbgW.append(rtc.pA.getUsername()+"\n");
        this.dbgW.append("\n");
        this.dbgW.append(this.pA_UI+"\n");
        this.dbgW.append("\n");
        this.dbgW.append("\n");
        this.dbgW.append("----------------------\n");
        this.dbgW.append("\n");
        this.dbgW.append(rtc.pB.getUsername()+"\n");
        this.dbgW.append("\n");
        this.dbgW.append(this.pB_UI+"\n");
    }

    
    
    public String getMovesSummary(){
        String output ="";
        output = "Set number:" +this.getSet() + "\nNumber of consecutive: " +this.getConsecutiveCorrect() +"\n";
        for(int i=0;i<moves.length;i++){
            Move mve = moves[i];
            if(mve.isComplete) {
                output =  output + "[*]";
            }
            else {
                output = output + "   ";
            }
            output = output + " "+ mve.getInfo()+"\n";
        }
       return output;
    }
    
    

    public synchronized void restartERRORMADE(){
      try{
        this.generateExperimentInstructionsForParticipant(rtc.pA);
        this.generateExperimentInstructionsForParticipant(rtc.pB);
        rtc.cC.getC().changeWebpage(rtc.pA, "ID1", "", this.pA_UI, "");
        rtc.cC.getC().changeWebpage(rtc.pB, "ID1", "", this.pB_UI, "");
        displayMoves();
        changeFirst();
        displayMoves();
        //System.exit(-5);
     }catch (Exception e){
         e.printStackTrace();
          //System.exit(-6);
     }
   
      
        Move mve = moves[0];
        Participant starter = null;
        String messagedisplaySTARTER ="";
        String messagedisplayOTHER ="";

        if(mve instanceof MoveONLY){
             starter = ((MoveONLY)mve).p;
             messagedisplaySTARTER = "ONE OF YOU\nMADE AN ERROR\n\n\nYou must start with "+((MoveONLY)mve).name;
             messagedisplayOTHER = "ONE OF YOU\nMADE AN ERROR\n\nWait for your partner\nto start";
            // rtc.changeColourOfWord(starter, ((MoveONLY)mve).name, Color.BLACK, Color.WHITE);
             //rtc.changeColourOfWord(starter, ((MoveONLY)mve).name, Color.BLACK, Color.WHITE);
             
        }
        else if(mve instanceof MoveAND) {
             messagedisplaySTARTER = "ONE OF YOU\nMADE AN ERROR\n\n"  +     "You must start with "+ ((MoveAND)mve).a+"\n\nAt the same time\n your partner\nstarts with "+((MoveAND)mve).b;
             messagedisplayOTHER =   "ONE OF YOU\nMADE AN ERROR\n\n"  +     "You must start with "+ ((MoveAND)mve).b+"\n\nAt the same time\nyour partner\nmust start with "+((MoveAND)mve).a;
             starter = this.rtc.pA;
             //rtc.changeColourOfWord(starter, ((MoveAND)mve).getMoveForParticipant(starter), Color.BLACK, Color.WHITE);
             //rtc.changeColourOfWord(rtc.getOther(starter), ((MoveAND)mve).getMoveForParticipant(rtc.getOther(starter)), Color.BLACK, Color.WHITE);
        }
        this.displayMessage(starter, messagedisplaySTARTER, rtc.getOther(starter),messagedisplayOTHER);

       


        for(int i=0;i<moves.length;i++){
             moves[i].reset();
        }

    }
   
    public void updateParticipantsUI(String prefixtoBOTH){
             Move mve = moves[0];
        Participant starter = null;
        String messagedisplaySTARTER ="";
        String messagedisplayOTHER ="";

        if(mve instanceof MoveONLY){
             starter = ((MoveONLY)mve).p;
             messagedisplaySTARTER = prefixtoBOTH+"You must start with "+((MoveONLY)mve).name;
             messagedisplayOTHER = prefixtoBOTH+"Wait for your partner\nto start";
            // rtc.changeColourOfWord(starter, ((MoveONLY)mve).name, Color.BLACK, Color.WHITE);
             //rtc.changeColourOfWord(starter, ((MoveONLY)mve).name, Color.BLACK, Color.WHITE);
             
        }
        else if(mve instanceof MoveAND) {
             messagedisplaySTARTER =  prefixtoBOTH+   "You must start with "+ ((MoveAND)mve).a+"\n\nAt the same time\n your partner\nstarts with "+((MoveAND)mve).b;
             messagedisplayOTHER =   prefixtoBOTH+   "You must start with "+ ((MoveAND)mve).b+"\n\nAt the same time\nyour partner\nmust start with "+((MoveAND)mve).a;
             starter = this.rtc.pA;
        }
        this.displayMessage(starter, messagedisplaySTARTER, rtc.getOther(starter),messagedisplayOTHER);
      }
    
    
    
    
    public void displayMessage(final Participant a, String aMessage, final Participant b, String bMessage){
        MessageDisplayMessageOnReactTaskWindow mdmortwA= new MessageDisplayMessageOnReactTaskWindow("server","server",aMessage,false);
        MessageDisplayMessageOnReactTaskWindow mdmortwB= new MessageDisplayMessageOnReactTaskWindow("server","server",bMessage,false);
        this.rtc.cC.getC().sendTaskMoveToParticipant(a, mdmortwA);
        if( a==rtc.pA){
            rtc.jRTFA.setDisableAndShowMessage(aMessage);
            rtc.jRTFB.setDisableAndShowMessage(bMessage);
        }
        else if(a==rtc.pB){
            rtc.jRTFA.setDisableAndShowMessage(bMessage);
            rtc.jRTFB.setDisableAndShowMessage(aMessage);
        }

        
        this.rtc.cC.getC().sendTaskMoveToParticipant(b, mdmortwB);
         Thread r = new Thread(){
            public void run(){
                try{
                    Thread.sleep(2500);
                }catch (Exception e){

                }
                MessageDisplayMessageOnReactTaskWindow rebegin= new MessageDisplayMessageOnReactTaskWindow("server","server","",true);
                rtc.cC.getC().sendTaskMoveToParticipant(a,rebegin);
                //rtc.jRTFA.setDisableAndShowMessage("");
                rtc.jRTFA.reEnableShowGrid();
                //rtc.jRTFB.setDisableAndShowMessage("");
                rtc.jRTFB.reEnableShowGrid();
                rtc.cC.getC().sendTaskMoveToParticipant(b, rebegin);
            }
        };
        r.start();
    }



    public synchronized void generateExperimentInstructionsForParticipant(Participant p){
        Vector rules = new Vector();
        String uiForP ="";

        for(int i=0;i<this.moves.length;i++){
          Move mvePRIOR = moves[i];
          Move mveNEXT ;
          if(i==moves.length-1){
              mveNEXT=moves[0];
          }
          else{
              mveNEXT=moves[i+1];
          }


          String priorFORP =  mvePRIOR.getMoveForParticipant(p);
          String priorFOROTHER =  mvePRIOR.getMoveForParticipant(this.rtc.getOther(p));
          String nextFORP =  mveNEXT.getMoveForParticipant(p);
          String nextFOROTHER =  mveNEXT.getMoveForParticipant(this.rtc.getOther(p));
          

          String next ="";
          String prior=StringOperations.appWS_ToRight("",10);
          if(nextFORP!=null){
              String[] rule = new String[3];
              rule[0]  =priorFORP;
              rule[1] = priorFOROTHER;
              rule[2] = nextFORP;
              rules.addElement(rule);

              if(priorFORP !=null){
                   prior = StringOperations.appWS_ToRight("YOU: "+priorFORP,10);

               }
               if(priorFOROTHER !=null&&priorFORP!=null){
                    prior = prior +",  ";
               }
               else{
                    prior = prior +"   ";
               }
               if(priorFOROTHER !=null){
                   prior = prior + StringOperations.appWS_ToRight("OTHER: "+priorFOROTHER,10);
               }
               else{
                   prior = prior + StringOperations.appWS_ToRight("",10);
               }



              if(nextFOROTHER !=null){
                   next =   StringOperations.appWS_ToRight("YOU: "+nextFORP,10) +    StringOperations.appWS_ToRight( " , OTHER: "+nextFOROTHER,10);
               }
               else{
                   next = "YOU: "+nextFORP;
               }
               uiForP = uiForP+ prior+"  --->   "+next+"\n";
          }
          
        }
    // System.exit(-5);
         //rtc.cC.getC().changeWebpageTextAndColour(p, "ID1", uiForP , "white", "black");
         //rtc.cC.getC().changeWebpage(p, uiForP, uiForP, uiForP, uiForP);


          if(p==rtc.pA){
              this.pARules=rules;
              this.pA_UI=uiForP;
          }
          else if(p==rtc.pB){
              this.pBRules=rules;
              this.pB_UI=uiForP;
          }


    }



    public synchronized void generateExperimentInstructionsForParticipantNEWER(Participant p){
        Vector rules = new Vector();
        String uiForP ="";

        for(int i=0;i<this.moves.length;i++){
          Move mvePRIOR = moves[i];
          Move mveNEXT ;
          if(i==moves.length-1){
              mveNEXT=moves[0];
          }
          else{
              mveNEXT=moves[i+1];
          }


          String priorFORP =  mvePRIOR.getMoveForParticipant(p);
          String priorFOROTHER =  mvePRIOR.getMoveForParticipant(this.rtc.getOther(p));
          String nextFORP =  mveNEXT.getMoveForParticipant(p);
          String nextFOROTHER =  mveNEXT.getMoveForParticipant(this.rtc.getOther(p));


          String next ="";
          String prior=StringOperations.appWS_ToRight("",10);
          if(nextFORP!=null){
              String[] rule = new String[3];
              rule[0]  =priorFORP;
              rule[1] = priorFOROTHER;
              rule[2] = nextFORP;
              rules.addElement(rule);

              if(priorFORP !=null){
                   prior = StringOperations.appWS_ToRight("YOU: "+priorFORP,10);

               }
               if(priorFOROTHER !=null&&priorFORP!=null){
                    prior = prior +",  ";
               }
               else{
                    prior = prior +"   ";
               }
               if(priorFOROTHER !=null & priorFORP!=null){
                   if(priorFOROTHER.equalsIgnoreCase(priorFORP)){
                       prior = "After you both press "+priorFOROTHER +" simultaneously";
                   }
                   else{
                       prior = "After you press "+priorFORP+" and your parter presses "+priorFOROTHER+" simultaneously";
                   }
               }
               else if (priorFORP==null){
                   prior = "After your partner presses "+priorFOROTHER;
               }
               else{
                   prior = "After you press "+priorFORP;
               }



              if(nextFOROTHER !=null){
                   if(nextFOROTHER.equalsIgnoreCase(nextFORP)){
                        next = "You have to press " + nextFORP + "simultaneously";
                   }
                   else{
                        next = "You have to press " + nextFORP + " at the same time as your partner presses "+ nextFOROTHER;
                   }

                   //next =   StringOperations.appWS_ToRight("YOU: "+nextFORP,10) +    StringOperations.appWS_ToRight( " , OTHER: "+nextFOROTHER,10);

                    
              }
               else{
                   //next = "YOU: "+nextFORP;
                   next = "You have to press " + nextFORP;
               }
               uiForP = uiForP+ prior+"  --->   "+next+"\n";
          }

        }
    // System.exit(-5);
         //rtc.cC.getC().changeWebpageTextAndColour(p, "ID1", uiForP , "white", "black");
         //rtc.cC.getC().changeWebpage(p, uiForP, uiForP, uiForP, uiForP);


          if(p==rtc.pA){
              this.pARules=rules;
              this.pA_UI=uiForP;
          }
          else if(p==rtc.pB){
              this.pBRules=rules;
              this.pB_UI=uiForP;
          }


    }


    boolean requestSWAP = false;
    String swaptype ="";
    int swapsize=2;
    
    private void performSwap(){
        try{
           rtc.cC.getC().saveDataToFile("SETCHANGE: "+swaptype+"--"+swapsize, "server","server" , new Date().getTime(), new Date().getTime(), "", null);
       }catch (Exception e){
           e.printStackTrace();
       }
        
        Set s=null;
        if(swaptype.equalsIgnoreCase("A"))  s = new SetA(rtc);
        if(swaptype.equalsIgnoreCase("AR"))  s = new SetAReversed(rtc);
        if(swaptype.equalsIgnoreCase("B"))  s = new SetB(rtc);
        if(swaptype.equalsIgnoreCase("BR"))  s = new SetBR(rtc);
        if(s!=null){
            s.setSize(swapsize);
            this.ms=s;
        }
        requestSWAP = false;
    }
    
    
    
    public synchronized void swap(String type, int size){
        Vector allNumbers = new Vector();
        allNumbers.addElement(new Integer(1));
        allNumbers.addElement(new Integer(2));
        allNumbers.addElement(new Integer(3));
        allNumbers.addElement(new Integer(4));
        allNumbers.addElement(new Integer(5));
        allNumbers.addElement(new Integer(6));
        allNumbers.addElement(new Integer(7));
        allNumbers.addElement(new Integer(8));
        allNumbers.addElement(new Integer(9));
        
        Vector jumbled = new Vector();
        while(allNumbers.size()>0){
            Integer i = (Integer)allNumbers.elementAt(r.nextInt(allNumbers.size()));
            jumbled.addElement(i);
        }
        int x1=(Integer)jumbled.elementAt(0);
        int x2=(Integer)jumbled.elementAt(1);
        int x3=(Integer)jumbled.elementAt(2);
        int x4=(Integer)jumbled.elementAt(3);
        int x5=(Integer)jumbled.elementAt(4);
        int x6=(Integer)jumbled.elementAt(5);
        int x7=(Integer)jumbled.elementAt(6);
        int x8=(Integer)jumbled.elementAt(7);
        int x9=(Integer)jumbled.elementAt(8);
        
        
        
    }

}


///MoveONLY                  MoveAND
//
//
///
///
///





///MoveONLY                  MoveAND
//
//
///
///
///